package com.wuudu.masterclass0;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class MainActivity2 extends Activity {

//	private Spinner spinner;
//	private int nvideos = 6;
//	public static List<Uri> UriList;
	private int myId = 0;
	public static int spinpos;
	public static List<DMove> DMList;
	public static List<DLevel> DLList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		String uriPath;
		DLList = new ArrayList<DLevel>();

		DLevel mDL1 = new DLevel();
		mDL1.name = "Básico";
		for (int i=0; i<3; i++) {
			uriPath = getFilesDir() + File.separator + Constants.FILE_VIDEO + Integer.toString(i+1) + ".3gp";
			DMove mDM = new DMove();
			mDM.title = "v" + Integer.toString(i+1);
			mDM.descr = "Descripción detallada";
			mDM.source = Uri.parse(uriPath);
			mDL1.Moves.add(mDM);
		}
		DLList.add(mDL1);
		
		DLevel mDL2 = new DLevel();
		mDL2.name = "Medio";
		for (int i=0; i<3; i++) {
			uriPath = getFilesDir() + File.separator + Constants.FILE_VIDEO + Integer.toString(i+1) + ".3gp";
			DMove mDM = new DMove();
			mDM.title = "v" + Integer.toString(i+1);
			mDM.descr = "Descripción detallada";
			mDM.source = Uri.parse(uriPath);
			mDL2.Moves.add(mDM);
		}
		DLList.add(mDL2);
		
		DLevel mDL3 = new DLevel();
		mDL3.name = "Avanzado";
		for (int i=3; i<6; i++) {
			uriPath = getFilesDir() + File.separator + Constants.FILE_VIDEO + Integer.toString(i+1) + ".mp4";
			DMove mDM = new DMove();
			mDM.title = "v" + Integer.toString(i+1);
			mDM.descr = "Descripción detallada";
			mDM.source = Uri.parse(uriPath);
			mDL3.Moves.add(mDM);
		}
		DLList.add(mDL3);
		
//		UriList = new ArrayList<Uri>();
//		List<String> NameList = new ArrayList<String>();
//		String uriPath = "";
//		for (int i=0; i<3; i++) {
//			NameList.add("v" + Integer.toString(i+1));
//			uriPath = getFilesDir() + File.separator + Constants.FILE_VIDEO + Integer.toString(i+1) + ".3gp";
//			UriList.add(Uri.parse(uriPath));
//			
//			DMove mDM = new DMove();
//			mDM.id = i;
//			mDM.title = "v" + Integer.toString(i+1);
//			mDM.descr = "Descripción detallada";
//			mDM.source = Uri.parse(uriPath);
//			DMList.add(mDM);
//		}
//		for (int i=3; i<6; i++) {
//			NameList.add("v" + Integer.toString(i+1));
//			uriPath = getFilesDir() + File.separator + Constants.FILE_VIDEO + Integer.toString(i+1) + ".mp4";
//			UriList.add(Uri.parse(uriPath));
//			
//			DMove mDM = new DMove();
//			mDM.id = i;
//			mDM.title = "v" + Integer.toString(i+1);
//			mDM.descr = "Descripción detallada";
//			mDM.source = Uri.parse(uriPath);
//			DMList.add(mDM);
//		}
//		ArrayAdapter<String> spinAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, NameList);
//		spinAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner = (Spinner) findViewById(R.id.spinner1);
//		spinner.setAdapter(spinAd);
//		spinpos = 0;
//		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				spinpos = position;
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) { }
//		});
	}
	
	public void onPlayClicked(View v) {
//		startActivity(new Intent(this, VideoFrameActivity.class));
	}

	public void onDMShowClicked(View v) {
		startActivity(new Intent(this, ItemActivity.class));
	}

	public void onPanelesClicked(View v) {
		startActivity(new Intent(this, ActivityPanels3.class));
	}

	public void onDownloadClicked(View v) {
		downloadFile(spinpos+1);
	}

    private void downloadFile(final int idVid) {
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				msg.what = 0;
		        try {
		        	String ext = "3gp";
		        	if (idVid>3)
		        		ext = "mp4";
					String urlStr = Constants.HTTP_SERVER + Constants.GET_VIDEO + 
							"user=" + myId +
							"&idvid=" + Integer.toString(idVid) + 
							"&ext=" + ext;
					URL url = new URL(urlStr);
		            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		            urlConnection.setRequestMethod("GET");
		            urlConnection.setDoOutput(true);
		            urlConnection.connect();
		            File file = new File(getBaseContext().getFilesDir(), Constants.FILE_VIDEO + Integer.toString(idVid) + "." + ext);
		            FileOutputStream fileOutput = openFileOutput(file.getName(), Context.MODE_PRIVATE);
		            InputStream inputStream = urlConnection.getInputStream();
		            int totalSize = urlConnection.getContentLength();
		            byte[] buffer = new byte[1024];
		            int bufferLength;
		            while ( (bufferLength = inputStream.read(buffer)) > 0 )
		                fileOutput.write(buffer, 0, bufferLength);
		            fileOutput.close();
		            msg.what = 1;
		            msg.arg1 = totalSize;
		        } catch (Exception e) {
					msg.what = 0;
					messageHandler.sendMessage(msg);
		        }
				messageHandler.sendMessage(msg);
			}
		}.start();
    }

	private Handler messageHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0: // Internet Connection Error. What to do?
				Toast.makeText(MainActivity2.this, "Error", Toast.LENGTH_LONG).show();
				break;
			case 1:
				if (msg.arg1>0)
					Toast.makeText(MainActivity2.this, "Done " + msg.arg1, Toast.LENGTH_LONG).show();
				else
					Toast.makeText(MainActivity2.this, "Done", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

}
