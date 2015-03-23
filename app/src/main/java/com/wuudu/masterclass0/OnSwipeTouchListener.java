package com.wuudu.masterclass0;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {

	private final GestureDetector mGD;
	
	public OnSwipeTouchListener(Context context) {
		mGD = new GestureDetector(context, new MyGestureListener());
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGD.onTouchEvent(event);
	}
	
	public void onSwipeLeft() { }

	public void onSwipeRight() { }

	private final class MyGestureListener extends SimpleOnGestureListener {
		
		private static final int DISTANCE_THRESHOLD = 100;
		private static final int VELOCITY_THRESHOLD = 100;
		
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			float distanceX = e2.getX() - e1.getX();
			float distanceY = e2.getY() - e1.getY();
			if ((Math.abs(distanceX) > Math.abs(distanceY)) 
				&& (Math.abs(distanceX) > DISTANCE_THRESHOLD) 
				&& (Math.abs(velocityX) > VELOCITY_THRESHOLD)) {
				if (distanceX > 0)
					onSwipeRight();
				else
					onSwipeLeft();
				return true;
			}
			else
				return false;
		}
		
	}
	
}
