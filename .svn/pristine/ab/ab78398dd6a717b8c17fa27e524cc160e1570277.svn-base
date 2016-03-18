package com.layout.emoji;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zhuochuang.hsej.R;

@SuppressLint("InflateParams")
public class EmojiAdapter extends BaseAdapter {
	private final List<EmojiBean> _eBeans;
	private final Context _context;

	public EmojiAdapter(List<EmojiBean> eBeans, Context context) {
		super();
		_eBeans = eBeans;
		_context = context;
	}

	@Override
	public int getCount() {
		return _eBeans.size();
	}

	@Override
	public EmojiBean getItem(int position) {
		return _eBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(_context).inflate(R.layout.emoji_item, null);
			holder.iconImg = (ImageView) convertView.findViewById(R.id.img_icon);
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iconImg.setImageResource(_eBeans.get(position).getResId());
		return convertView;
	}

	class ViewHolder {
		ImageView iconImg;
	}

}
