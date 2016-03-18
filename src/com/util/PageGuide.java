package com.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zhuochuang.hsej.R;

public class PageGuide extends LinearLayout{
	private FrameLayout pointLayout[] = null;
	private View point[] = null;
	private int count;
	private Context context;
	private int resOn = R.drawable.pageguide_p, resOff = R.drawable.pageguide_n;
	
	public PageGuide(Context context, AttributeSet attr){
		super(context, attr);
		this.context = context;
	}
	
	public void setRes(int resOn, int resOff){
		this.resOn = resOn;
		this.resOff = resOff;
	}
	
	public void setParams(int count, int w ,int h){
		removeAllViews();
		this.count = count;
		
		pointLayout = new FrameLayout[count];
		point = new View[count];
		
		setOrientation(LinearLayout.HORIZONTAL);
		for(int i = 0; i < count; i++){
			pointLayout[i] = new FrameLayout(context);
			point[i] = new View(context);
			point[i].setBackgroundResource(resOff);
			pointLayout[i].addView(point[i], new FrameLayout.LayoutParams(w, h, Gravity.CENTER));
			addView(pointLayout[i], new LinearLayout.LayoutParams(w + Utils.dipToPixels(context, 6), h + Utils.dipToPixels(context, 10), 1));
		}
		
		if(count == 1) removeAllViews();
	}
	
	public void setSelect(int position){
		for(int i = 0; i < count; i++){
			point[i].setBackgroundResource(resOff);
		}
		if(point[position] != null){
			point[position].setBackgroundResource(resOn);
		}
		if(count == 1) removeAllViews();
	}
}
