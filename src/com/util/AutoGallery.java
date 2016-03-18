package com.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
@SuppressLint("ClickableViewAccessibility")
public class AutoGallery extends Gallery{
	
	private float mLastX, mLastY;
	private final int DIRECTION_LEFT = 1000;
	private final int DIRECTION_RIGHT = 1001;
	private final int DIRECTION_NONE = 1002;
	private int mDirection;
	private int mLength = 0;
	private int mDuration = 4000;
	private int mScrollLength = 0;
	private boolean isAutoScroll = false;
	private Handler mHandler;
	private RunUpdateTime mRunUpdateTime;
	private Context mContext;
	
	public AutoGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		setHorizontalFadingEdgeEnabled(false);
		mScrollLength = mContext.getResources().getDisplayMetrics().widthPixels / 8;
	}
	
	public AutoGallery(Context context, AttributeSet attr) {
		super(context, attr);
		// TODO Auto-generated constructor stub
		mContext = context;
		setHorizontalFadingEdgeEnabled(false);
		mScrollLength = mContext.getResources().getDisplayMetrics().widthPixels / 8;
	}
	
	public void setDuration(int duration){
		mDuration = duration;
	}
	
	public void setLength(int length){
		mLength = length;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastX = event.getX();
			mLastY = event.getY();
			isAutoScroll = false;
			removeScroll();
			break;
			
		case MotionEvent.ACTION_MOVE:
			if(Math.abs(mLastX - event.getX()) - Utils.dipToPixels(mContext, 70) > Math.abs(mLastY - event.getY())){
				if(mLastX - event.getX() < 0){
					mDirection = DIRECTION_LEFT;
				}
				else if(mLastX - event.getX() > Utils.dipToPixels(mContext, 70)){
					mDirection = DIRECTION_RIGHT;
				}
				else{
					mDirection = DIRECTION_NONE;
				}
			}
			else{
				mDirection = DIRECTION_NONE;
			}
			break;
			
		case MotionEvent.ACTION_UP:
			setAutoScroll();
			break;
			
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return super.onTouchEvent(event);
	}
  
    @Override  
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {  
        // e1是按下的事件，e2是抬起的事件  
    	try {
    		int keyCode;
            if (mDirection == DIRECTION_LEFT) {
                keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
                onKeyDown(keyCode, null);
            }
            else if (mDirection == DIRECTION_RIGHT) {
                keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
                //onScroll(null, null, Utils.dipToPixels(mContext, 10), 0);
                onScroll(null, null, mScrollLength, 0);
                onKeyDown(keyCode, null);
            }
		} 
    	catch (Exception e) {
			// TODO: handle exception
		}
        return false;
    }

	public void slide(int direction) {
		AudioManager audiomanage = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
		try {
			audiomanage.unloadSoundEffects();
		} catch (Exception e) {
			// TODO: handle exception
		} catch (Error e) {
			// TODO: handle exception
		}
		if(mLength > 1){
	    	this.mDirection = direction;
	    	this.onFling(null, null, 0, 0);
		}
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AudioManager audiomanage = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
				try {
					audiomanage.unloadSoundEffects();
				} catch (Exception e) {
					// TODO: handle exception
				} catch (Error e) {
					// TODO: handle exception
				}
			}
		}, 100);
    }
    
    public void setAutoScroll(){
    	if(mLength > 1){
    		isAutoScroll = true;
    		RunUpdateTime runUpdateTime = new RunUpdateTime();
    		mHandler = new Handler();
    		mHandler.postDelayed(runUpdateTime, mDuration);
    		this.mRunUpdateTime = runUpdateTime;
    	}
	}
	
	private void removeScroll(){
		if(mLength > 1 && mHandler != null && mRunUpdateTime != null){
			mHandler.removeCallbacks(mRunUpdateTime);
		}
	}
	
	class RunUpdateTime implements Runnable{
        @Override
        public void run(){
        	if(isAutoScroll){
        		slide(DIRECTION_RIGHT);
        	}
        	if(mHandler != null && mRunUpdateTime != null){
        		mHandler.removeCallbacks(mRunUpdateTime);
            	mRunUpdateTime = this;
            	mHandler.postDelayed(this, mDuration);
        	}
        }
    }
}
