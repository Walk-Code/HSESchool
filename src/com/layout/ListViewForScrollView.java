package com.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewForScrollView extends ListView{
	public ListViewForScrollView(Context context) {
		super(context);
	}
	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context,attrs);

	}
	public ListViewForScrollView(Context context, AttributeSet attrs,
			int destyle) {
		super(context,attrs,destyle);
		
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);		
	}
	
}
