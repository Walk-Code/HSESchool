package com.zhuochuang.hsej.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

	public CustomImageView(Context context) {
		super(context);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int mode=MeasureSpec.getMode(widthMeasureSpec);
		// int size=MeasureSpec.getSize(widthMeasureSpec)/4*3;
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

}
