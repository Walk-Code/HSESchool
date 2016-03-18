package com.zhuochuang.hsej.store;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.util.ContentAdapter;
import com.zhuochuang.hsej.R;

public class StorePupopWindow extends PopupWindow{
	private Context mContext;
	private onPopupWindowItemClickListener mClickListen;
	//private ArrayAdapter mAdapter;
	private ContentAdapter mContentAdapter;
	private List<String> list = new ArrayList<String>();
	private int width = 0;
	private ImageView mImageView;
	private TextView mTextView;
	private int action;
	private int index;
	private int layout;
	public StorePupopWindow(Context c,int action,int layout){
		mContext = c;
		initView();	
		this.action = action;
		this.layout = layout;
	}

	private void initView() {
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		int layouts;
		if(layout != 2){
			layouts = R.layout.store_popupwindow_order;
		}else{
			layouts = R.layout.store_popupwindow;
		}
		View popupWindow  = layoutInflater.inflate(layouts, null);
		setContentView(popupWindow);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		update();
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setOutsideTouchable(true);	
		ListView listView = (ListView) popupWindow.findViewById(R.id.store_popupwindow);
		listView.setAdapter(mContentAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				return list.size()!=0 ? list.size() : 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
	
				if(convertView == null){
					convertView = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
							      .inflate(R.layout.store_popupwindow_item,parent,false);					
				  }				
				 mTextView = (TextView)convertView.findViewById(R.id.store_popupwindowItem);
				 mTextView.setText(list.get(position));
				 mImageView = (ImageView) convertView.findViewById(R.id.store_popupwindowItem_image);
				if(action == 1){
				((TextView)convertView.findViewById(R.id.store_popupwindowItem_count)).setVisibility(View.GONE);
					if(position == index){
						mTextView.setTextColor(Color.RED);
						mImageView.setVisibility(View.VISIBLE);
					}else{
						mTextView.setTextColor(Color.BLACK);
						mImageView.setVisibility(View.GONE);
					}
			
				}else if(action == 2){
					((TextView)convertView.findViewById(R.id.store_popupwindowItem_count)).setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.store_popupwindowItem)).setVisibility(View.GONE);
					((TextView)convertView.findViewById(R.id.store_persion_popup)).setText(list.get(position));
				}				
				return convertView;
			}
	
			
		});	
		setListViewHeightBasedOnChildren(listView);		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				StorePupopWindow.this.dismiss();
				index = position;
				if(mClickListen != null){
					mClickListen.onPopupWindowItemClick(position);
				}
				
			}
		});		
	}
   
	public void setOnPoPupWindowClickListener(onPopupWindowItemClickListener clickListen){
		this.mClickListen = clickListen;
	}
	
	public void changeData(List<String> lists,int orderNumber){
		list.addAll(lists);
		index = orderNumber;
		mContentAdapter.notifyDataSetChanged();
	}
	
	public interface onPopupWindowItemClickListener{
		void onPopupWindowItemClick(int position);
	}	
	//设置ListView的高度
	public void setListViewHeightBasedOnChildren(ListView listView) {  
		  
		ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            // listItem.measure(0, 0);
            listItem.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        
        listView.setLayoutParams(params);

		 } 
	//dp转换成px
	public int Dp2Px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	}
}
