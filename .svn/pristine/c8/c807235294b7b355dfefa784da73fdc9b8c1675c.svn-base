package com.zhuochuang.hsej.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuochuang.hsej.CommunityQueryPreviousActivity;
import com.zhuochuang.hsej.R;

public class CommQueryPriviousAdapter extends BaseAdapter {
	protected String[] datas;
	protected Context context;

	public CommQueryPriviousAdapter(Context context, String[] datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.length;
	}

	@Override
	public Object getItem(int position) {
		return datas[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (datas == null) {
			return convertView;
		}
		if (convertView == null)
			convertView = View.inflate(context,
					R.layout.activity_comm_query_item, null);
		TextView tvName = (TextView) convertView.findViewById(R.id.item_name);
		String year = datas[position];
		tvName.setText(datas[position]);
		if ((CommunityQueryPreviousActivity.currentYear == null && position == 0)
				|| year.equals(CommunityQueryPreviousActivity.currentYear)) {
			convertView.findViewById(R.id.item_image).setVisibility(
					View.VISIBLE);
		} else {
			convertView.findViewById(R.id.item_image).setVisibility(View.GONE);
		}
		return convertView;
	}
}
