package com.zhuochuang.hsej.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
	private Context context;
	private String[] modes;

	public MSpinnerAdapter(Context context, String[] modes) {
		this.context = context;
		this.modes = modes;
	}

	@Override
	public int getCount() {
		return modes.length;
	}

	@Override
	public Object getItem(int position) {
		return modes[position];
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
		modeName.setText(getItem(position).toString());
		modeName.setTextColor(Color.rgb(76, 76, 76));
		modeName.setHeight(50);
		modeName.setTextSize(16);
		return convertView;
	}

}
