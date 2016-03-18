package com.zhuochuang.hsej.adapter;

import java.util.ArrayList;
import java.util.List;

import com.zhuochuang.hsej.entity.CommunityStructEntity.StructEntity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class StructSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
	private Context context;
	private List<StructEntity> names;

	public StructSpinnerAdapter(Context context) {
		this.context = context;
		this.names = new ArrayList<StructEntity>();
	}

	public void setData(List<StructEntity> names) {
		this.names = names;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return names.size();
	}

	@Override
	public Object getItem(int position) {
		return names.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = View.inflate(context,
					android.R.layout.simple_list_item_1, null);
		TextView modeName = (TextView) convertView;
		modeName.setText(names.get(position).getName());
		modeName.setTextColor(Color.rgb(76, 76, 76));
		modeName.setHeight(50);
		modeName.setTextSize(16);
		return convertView;
	}

}
