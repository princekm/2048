package com.newgen.swipe;
import com.newgen.game.PlayScreen;
import com.newgen.gfx.PM;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
public class OnSwipeTouchListener implements OnTouchListener 
{

    private final GestureDetector gestureDetector;
    private PlayScreen context;
    public OnSwipeTouchListener (PlayScreen context)
    {
    	this.context=context;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private final int SWIPE_THRESHOLD = 25;
        private final int SWIPE_VELOCITY_THRESHOLD = 50;

        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
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
            } catch 
            (Exception exception) 
            {
                exception.printStackTrace();
            }
            return true;
        }
    }

    public void onSwipeRight() 
    {
    	if(context.enable)
    	{
   // 		context.performUndo();
    	//	context.loadMAT();
    		//context.show();
    		context.shiftRight();
    		context.updateScore();
    	//	context.checkHighScore();
    		context.enable=false;
    		context.genDelayed();
    	}
    	
    }

    public void onSwipeLeft() 
    {
    	if(context.enable)
    	{
    		context.shiftLeft();
    		context.updateScore();
    //		context.checkHighScore();
    		context.enable=false;
    		context.genDelayed();
    	}
    }

    public void onSwipeTop() 
    {
    	if(context.enable)
    	{
    		context.shiftUp();
    		context.updateScore();
  //  		context.checkHighScore();
    		context.enable=false;
    		context.genDelayed();
    	}
    }

    public void onSwipeBottom() 
    {
    	if(context.enable)
    	{
    		context.shiftDown();
    		context.updateScore();
//    		context.checkHighScore();
    		context.enable=false;
    		context.genDelayed();
    	}
    }

	@Override
	public boolean onTouch(View arg0, MotionEvent event)
	{
		 return gestureDetector.onTouchEvent(event);
	}
}