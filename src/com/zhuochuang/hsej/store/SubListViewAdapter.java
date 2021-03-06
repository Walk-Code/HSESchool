package com.zhuochuang.hsej.store;

import com.zhuochuang.hsej.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SubListViewAdapter extends BaseAdapter{
	private String[][] sub_items;
	private Context context;
	private int root_position;
	private LayoutInflater inflater;
	private int actions;
	public SubListViewAdapter(Context context, String[][] sub_items,int position,int action) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.sub_items = sub_items;
		this.root_position = position;
		actions = action;
	}
	
	@Override
	public int getCount() {
		return sub_items[root_position].length != 0 ? sub_items[root_position].length : 0;
	}
	
	public void setSelectPosition(int position){
		root_position = position;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			if(actions == 0){
				convertView = inflater.inflate(R.layout.store_popupwindow_item,parent,false);
				holder.item_text = (TextView) convertView.findViewById(R.id.store_popupwindowItem);
				holder.item_sum =  (TextView) convertView.findViewById(R.id.store_popupwindowItem_count);
			}else{
				convertView = inflater.inflate(R.layout.store_home_page_shop,parent,false);
				holder.StoreName = (TextView) convertView.findViewById(R.id.shop_name);
				
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item_text.setText(sub_items[root_position][position]);
		return convertView;
	}
	
	class ViewHolder{
		TextView item_text;
		TextView item_sum;
		ImageView StoreImage;
		TextView StoreName;
	}
}
