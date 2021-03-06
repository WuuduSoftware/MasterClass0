package com.wuudu.masterclass0;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Toast;

public class ItemVideoFrameActivity extends Activity implements SurfaceHolder.Callback, OnPreparedListener, MediaPlayerControl {

	private MediaPlayer mMP;
	private SurfaceHolder mSH;
	private SurfaceView mSV;
	private MediaController mMC;
	
	 //////////////////////
	// ACTIVITY FUNCTIONS //
	 //////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoframe);
		
		mSV = (SurfaceView) findViewById(R.id.SV);
		mSH = mSV.getHolder();
		mSH.addCallback(this);
		mMC = new MediaController(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mMC.hide();
		mMP.release();
		mMP = null;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMP!=null) {
			mMP.release();
			mMP = null;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mMC.show();
		return false;
	}
	
	 ////////////////////////////
	// SURFACE HOLDER FUNCTIONS //
	 ////////////////////////////
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mMP = new MediaPlayer();
		mMP.setDisplay(mSH);
		try {
			mMP.setDataSource(this, ItemActivity2.mDM.source);//.setDataSource(vPath);
			mMP.prepare(); // Files
//			mMP.prepareAsync(); // Streams
			mMP.setOnPreparedListener(this);
			mMP.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMP.setScreenOnWhilePlaying(true);
		} catch (IllegalArgumentException e) {
			Toast.makeText(this, "Error IllegalArgumentException", Toast.LENGTH_LONG).show();
		} catch (SecurityException e) {
			Toast.makeText(this, "Error SecurityException", Toast.LENGTH_LONG).show();
		} catch (IllegalStateException e) {
			Toast.makeText(this, "Error IllegalStateException", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(this, "Error IOException", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) { }

	@Override
	public void onPrepared(MediaPlayer mp) {
		mMC.setMediaPlayer(this);
		mMC.setAnchorView(mSV);
//		mMP.start();
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				mMC.setEnabled(true);
				mMC.show();
			}
		});
	}
	
	 //////////////////////////////////
	// MEDIAPLAYER CONTROLS FUNCTIONS //
	 //////////////////////////////////
		
	@Override
	public void start() {
		mMP.start();
	}

	@Override
	public void pause() {
		mMP.pause();
	}

	@Override
	public int getDuration() {
		return mMP.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		return mMP.getCurrentPosition();
	}

	@Override
	public void seekTo(int pos) {
		mMP.seekTo(pos);
	}

	@Override
	public boolean isPlaying() {
		return mMP.isPlaying();
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}
	
}
