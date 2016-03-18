package com.zhuochuang.hsej.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;

public abstract class SHLBaseAdapter<T> extends MBaseAdapter<T> {
	protected OnEFClickListener mClickListener;// 接口回调

	public SHLBaseAdapter(Context context) {
		super(context);
	}

	public SHLBaseAdapter(Context context, List<T> datas) {
		super(context, datas);
	}

	public void setOnEFClickListener(OnEFClickListener listener) {
		this.mClickListener = listener;
	}

	public interface OnEFClickListener {
		public void onEditClick(View view, int position);

		public void onFinishClick(View view, int position);

		public void onChecked(boolean isChecked, int position);
	}
}
