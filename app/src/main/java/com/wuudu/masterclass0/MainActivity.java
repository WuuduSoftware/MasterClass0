package com.wuudu.masterclass0;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {

	private VideoView mVV;
	private Spinner spinner;
	private int nvideos = 6;
	private List<Uri> UriList;
	private int myId;
	private Button boton;
	private int spinpos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		spinpos = 0;
		
		boton = (Button) findViewById(R.id.button1);
		boton.setEnabled(false);
		mVV = (VideoView) findViewById(R.id.videoView1);
		MediaController mMC = new MediaController(this);
		mMC.setAnchorView(mVV);
		mVV.setMediaController(mMC);
		mVV.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				boton.setEnabled(true);
				mVV.setBackgroundColor(Color.BLUE);
			}
		});
		mVV.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
                return what == 100;
			}
		});

		UriList = new ArrayList<Uri>();
		List<String> NameList = new ArrayList<String>();
		int id;
		String uriPath;
		for (int i=0; i<nvideos; i++) {
			NameList.add("v" + Integer.toString(i+1));
			id = getResources().getIdentifier(NameList.get(i), "raw", getPackageName());
			uriPath = "android.resource://" + getPackageName() + "/" + id;
			UriList.add(Uri.parse(uriPath));
		}
		ArrayAdapter<String> spinAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, NameList);
		spinAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setAdapter(spinAd);
//		spinner.setSelection(0);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spinpos = position;
				boton.setEnabled(false);
				if (mVV.isPlaying())
					mVV.stopPlayback();
				mVV.setBackgroundColor(Color.RED);
				mVV.setVideoURI(UriList.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { }
		});
		
//		String videoName = "v2";
//		int id = getResources().getIdentifier(videoName, "raw", getPackageName());
//
//		String uriPath = "android.resource://" + getPackageName() + "/" + id;
//		Uri uri = Uri.parse(uriPath);
		
//		mVV.setVideoURI(UriList.get(0));
	}
	
	public void onButtonClicked(View v) {
		onClicked();
	}

	public void onDownloadClicked(View v) {
//		downloadVideo(2);
		downloadFile(spinpos+1);
	}

	public void onPlayClicked(View v) {
		boton.setEnabled(false);
		if (mVV.isPlaying())
			mVV.stopPlayback();
		mVV.setBackgroundColor(Color.BLUE);
		String vPath = getFilesDir() + File.separator + Constants.FILE_VIDEO + Integer.toString(2) + ".3gp";
		Uri mUri = Uri.parse(vPath);
		if (vPath!=null)
			mVV.setVideoURI(mUri);
//		String vPath = getVideoPath(3);
//		if (vPath!=null)
//			mVV.setVideoPath(vPath);
	}

	private void onClicked() {
		mVV.setBackgroundColor(Color.TRANSPARENT);
		if (mVV.isPlaying())
			mVV.pause();
		else
			mVV.start();
	}
	
	private void downloadVideo(final int idVid) {
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				msg.what = 0;
				try {
					String urlStr = Constants.HTTP_SERVER + Constants.GET_VIDEO + 
							"user=" + "0" +
							"&idvid=" + Integer.toString(idVid);
					URL url = new URL(urlStr);
					URLConnection urlConn = url.openConnection();
					if ((urlConn instanceof HttpURLConnection)) {
						HttpURLConnection httpConn = (HttpURLConnection) urlConn;
						httpConn.setAllowUserInteraction(true);
						httpConn.setInstanceFollowRedirects(true);
					    httpConn.setRequestProperty("User-Agent", "Android");
					    httpConn.setRequestProperty("Content-Language", "es-ES");
					    httpConn.setRequestMethod("GET");
					    httpConn.connect();

                        int resCode = httpConn.getResponseCode();
					    if (resCode == HttpURLConnection.HTTP_OK) { // Si todo va bien
                            InputStream in = httpConn.getInputStream(); // descarga el Json
							if (in != null) {
							    String text = convertStreamToString(in);
							    if (!text.startsWith("ERROR")) {
							        OutputStreamWriter writer = new OutputStreamWriter(getBaseContext().openFileOutput(Constants.FILE_VIDEO + idVid + ".3gp", Context.MODE_PRIVATE));
							        writer.write(text);
							        writer.close();
								    in.close();
								    msg.what = 1;
							    }
							}
					    }
					}
				} catch (Exception e) {
					msg.what = 0;
					messageHandler.sendMessage(msg);
				}
				messageHandler.sendMessage(msg);
			}
		}.start();
	}
	
	private String convertStreamToString(InputStream is) {
	    Scanner s = new Scanner(is, "ISO-8859-1").useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	private String getVideoPath(int idVid) {
		String mPath = null;
		File mFile = new File(getBaseContext().getFilesDir(), Constants.FILE_VIDEO + idVid + ".3gp");
		if (mFile.exists())
			mPath = mFile.getAbsolutePath();
		return mPath;
	}

	private Handler messageHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0: // Internet Connection Error. What to do?
				Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
				break;
			case 1:
				if (msg.arg1>0)
					Toast.makeText(MainActivity.this, "Done " + msg.arg1, Toast.LENGTH_LONG).show();
				else
					Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	
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
							"user=" + "0" +
							"&idvid=" + Integer.toString(idVid) + 
							"&ext=" + ext;
					URL url = new URL(urlStr);
		            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		 
		            urlConnection.setRequestMethod("GET");
		            urlConnection.setDoOutput(true);
		 
		            //connect
		            urlConnection.connect();
		 
		//	        OutputStreamWriter writer = new OutputStreamWriter(getBaseContext().openFileOutput(Constants.FILE_VIDEO + idVid + ".3gp", Context.MODE_PRIVATE));
		            //set the path where we want to save the file          
		//            File SDCardRoot = Environment.getExternalStorageDirectory();
		            //create a new file, to save the downloaded file
		            File file = new File(getBaseContext().getFilesDir(), Constants.FILE_VIDEO + Integer.toString(idVid) + ext);
		//            File file = new File(SDCardRoot,"downloaded_file.png");
		  
		            FileOutputStream fileOutput = openFileOutput(file.getName(), Context.MODE_PRIVATE);
//		            FileOutputStream fileOutput = new FileOutputStream(file);
		 
		            //Stream used for reading the data from the internet
		            InputStream inputStream = urlConnection.getInputStream();
		            int totalSize = urlConnection.getContentLength();
		            //create a buffer...
		            byte[] buffer = new byte[1024];
		            int bufferLength;
		 
		            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
		                fileOutput.write(buffer, 0, bufferLength);
		            }
		            //close the output stream when complete //
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
    
}
