package com.zhuochuang.hsej.store;

import com.zhuochuang.hsej.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class RootListViewAdapter extends BaseAdapter{
	private Context context;

	private LayoutInflater inflater;
	
	private String[] items;
	
	private int selectedPosition = -1;
	
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public RootListViewAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return items.length != 0 ? items.length : 0;
	}
	public void setItems(String[] items) {
		this.items = items;
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){					
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.store_detail_popupwindow_item,parent,false);
			holder.item_text =(TextView) convertView.findViewById(R.id.item_name_text);
			holder.item_layout = (LinearLayout)convertView.findViewById(R.id.root_item);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		/**
		 * 该项被选中时改变背景色
		 */
		if(selectedPosition == position){
			holder.item_layout.setBackgroundColor(Color.parseColor("#f0f0f0"));
			holder.item_text.setTextColor(Color.RED);
		}else{

			holder.item_layout.setBackgroundColor(Color.WHITE);
			holder.item_text.setTextColor(Color.BLACK);
		}
		holder.item_text.setText(items[position]);
		return convertView;
	}
	class ViewHolder{
		TextView item_text;
		LinearLayout item_layout;
	}
	

}
