package com.zhuochuang.hsej.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.util.TabBarView.TabBarAdapter;
import com.zhuochuang.hsej.R;

/**
 * 失物招领跟二手页面tab适配器
 * 
 * @author Administrator
 * 
 */
public class SecondHandandLostTabAdapter implements TabBarAdapter {
	private Context context;
	private List<String> tabNames;
	protected int mCurrentPosition;
	protected View mView;
	protected int num = 0;

	public SecondHandandLostTabAdapter(Context context, List<String> tabNames) {
		this.context = context;
		this.tabNames = tabNames;
	}

	@Override
	public View getSeletedView(int position, View convertView) {
		mCurrentPosition = position;
		mView = convertView;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.tabbar_cell_myclassmate, null);
		}
		TextView textName = ((TextView) convertView.findViewById(R.id.textview));
		textName.setText(tabNames.get(position));
		convertView.findViewById(R.id.line).setVisibility(View.VISIBLE);
		return convertView;
	}

	@Override
	public View getNOSeletedView(int position, View convertView) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.tabbar_cell_myclassmate, null);
		}
		TextView textName = ((TextView) convertView.findViewById(R.id.textview));
		if (num > 0) {
			String formatStr = context.getResources().getString(
					R.string.text_tab_num);
			textName.setText(tabNames.get(position)
					+ String.format(formatStr, num));
		} else {
			textName.setText(tabNames.get(position));
		}
		convertView.findViewById(R.id.line).setVisibility(View.GONE);

		return convertView;
	}

	@Override
	public int getCount() {
		return tabNames.size();
	}

}
