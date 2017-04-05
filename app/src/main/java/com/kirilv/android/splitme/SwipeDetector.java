package com.kirilv.android.splitme;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeDetector implements OnTouchListener {

    private final GestureDetector gestureDetector;
    private ISwipe notifier;
    private Action swipeAction = Action.None;

    public SwipeDetector (Context ctx, ISwipe notifier) {
        this.gestureDetector = new GestureDetector(ctx, new GestureListener());
        this.notifier = notifier;
    }
    
    public static enum Action {
		LR, // Left to Right
		RL, // Right to Left
		TB, // Top to bottom
		BT, // Bottom to Top
		None // when no action was detected
	}

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
        	swipeAction = Action.None;
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
    
    @Override
	public boolean onTouch(View view, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
    
    public GestureDetector getGestureDetector() {
        return  gestureDetector;
    }
    
    public Action getAction() {
    	return swipeAction; 
    }

    private void onSwipeRight() {
    	swipeAction = Action.LR;
    	notifier.onSwipe();
    }

    private void onSwipeLeft() {
    	swipeAction = Action.RL;
    	notifier.onSwipe();
    }

    private void onSwipeTop() {
    	swipeAction = Action.BT;
    }

    private void onSwipeBottom() {
    	swipeAction = Action.TB;
    }
    
	public interface ISwipe {

		public void onSwipe();
	}
}