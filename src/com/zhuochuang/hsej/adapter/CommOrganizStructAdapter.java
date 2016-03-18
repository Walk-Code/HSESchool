package com.zhuochuang.hsej.adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuochuang.hsej.R;

public class CommOrganizStructAdapter extends BaseAdapter {
	protected JSONArray datas;
	protected Context context;

	public CommOrganizStructAdapter(Context context) {
		this.context = context;
		datas = new JSONArray();
	}

	public void setData(JSONArray datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.length();
	}

	@Override
	public Object getItem(int position) {
		return datas.opt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = View.inflate(context,
					R.layout.activity_community_organiz_structe_item, null);
		TextView tvName = (TextView) convertView.findViewById(R.id.item_name);
		TextView tvPersonName = (TextView) convertView
				.findViewById(R.id.item_personName);
		JSONObject object = datas.optJSONObject(position);
		tvName.setText(object.optString("name"));
		tvPersonName.setText(object.optString("personName"));
		return convertView;
	}

}
