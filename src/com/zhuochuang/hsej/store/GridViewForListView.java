package com.zhuochuang.hsej.store;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewForListView extends GridView{
	public GridViewForListView(Context context) {  
        super(context);  
    }  
  
    public GridViewForListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public GridViewForListView(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
  
    }  
    
    @Override  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){  
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, mExpandSpec);  
   }  
}
