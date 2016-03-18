package com.zhuochuang.hsej.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MBaseAdapter<T> extends BaseAdapter{
	protected List<T> datas;
	protected Context context;

	public MBaseAdapter(Context context, List<T> datas) {
		this.datas = datas;
		this.context = context;
	}

	public MBaseAdapter(Context context) {
		this.context = context;
		this.datas = new ArrayList<T>();
	}

	public void setData(List<T> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	public void addData(List<T> datas) {
		datas.addAll(datas);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

}
