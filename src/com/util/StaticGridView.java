package com.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.GridView;

public class StaticGridView extends GridView{

	public StaticGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}
	
	public StaticGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	/**
	  * 设置不滚�?
	  */
	 @Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	  int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	    MeasureSpec.AT_MOST);
	  super.onMeasure(widthMeasureSpec, expandSpec);
	 }
}
