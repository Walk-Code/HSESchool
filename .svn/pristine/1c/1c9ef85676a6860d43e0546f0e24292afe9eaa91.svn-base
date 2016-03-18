package com.zhuochuang.hsej.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochuang.hsej.R;

public class CustomDialog extends Dialog implements OnClickListener {
	private Button btnPos;
	private Button btnNeg;
	private String btnPosName;
	private String btnNegName;
	private String title;
	private LinearLayout layoutView;

	public CustomDialog(Context context, String title, String button1Name,
			String button2Name) {
		super(context, R.style.custom_dialog);
		btnPosName = button1Name;
		btnNegName = button2Name;
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_custom_dialog);

		btnPos = (Button) findViewById(R.id.btn_positive);
		btnPos.setText(btnPosName);
		btnPos.setOnClickListener(this);
		btnNeg = (Button) findViewById(R.id.btn_negative);
		btnNeg.setText(btnNegName);
		btnNeg.setOnClickListener(this);
		((TextView) findViewById(R.id.custom_title)).setText(title);
		layoutView = (LinearLayout) findViewById(R.id.view_container);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_positive:
			if (listener != null) {
				listener.onPositiveClick(view);
			} else {
				dismiss();
			}
			break;

		case R.id.btn_negative:
			if (listener != null) {
				listener.onNegativeClick(view);
			} else {
				dismiss();
			}
			break;
		}
	}

	public void setMContentView(View view) {
		layoutView.addView(view);
	}

	private OnCDClickListener listener;

	public interface OnCDClickListener {
		public void onPositiveClick(View view);

		public void onNegativeClick(View view);
	}

	public void setOnCDClickListener(OnCDClickListener listener) {
		this.listener = listener;
	}

}
