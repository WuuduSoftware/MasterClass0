package com.wuudu.masterclass0;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class ActivityPanels3 extends Activity {

	private boolean pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multipane3);
		
		pos = false;
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			View rightPane = findViewById(R.id.right);
			rightPane.setTranslationX(100);
			View leftPane = findViewById(R.id.left);
			leftPane.setOnTouchListener(new OnSwipeTouchListener(this) {
				@Override
				public void onSwipeLeft() {
					goLeft();
					super.onSwipeLeft();
				}
				
				@Override
				public void onSwipeRight() {
					goRight();
					super.onSwipeRight();
				}
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return super.onTouch(v, event);
				}
				
			});
		}
	}
	
	private void goLeft() {
		if (pos) {
			View rightPane = findViewById(R.id.right);
			rightPane.setTranslationX(100);
			pos = (!pos);
		}
	}
	
	private void goRight() {
		if (!pos) {
			View rightPane = findViewById(R.id.right);
			rightPane.setTranslationX(300);
			pos = (!pos);
		}
	}
	
}
