package com.zhuochuang.hsej.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochuang.hsej.R;

public class SecondHandandLostRecruitItem extends LinearLayout {
	public CheckBox shlrCBox;
	public TextView shlrTitle;
	public TextView shlrTvGoing;
	public TextView shlrStatus;
	public TextView shlrContent;
	public TextView shlrAddress;
	public TextView shlrDate;
	public LinearLayout shlrEditLayout;
	public Button shlrBtnFinish;
	public Button shlrBtnEdit;

	public SecondHandandLostRecruitItem(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		View view = View.inflate(context,
				R.layout.fm_secend_handand_lost_recruit_item, this);
		shlrCBox = (CheckBox) view.findViewById(R.id.recruit_service_cb_select);
		shlrTitle = (TextView) view.findViewById(R.id.recruit_service_tv_title);
		shlrTvGoing = (TextView) view
				.findViewById(R.id.recruit_service_tv_isgoing);
		shlrStatus = (TextView) view
				.findViewById(R.id.recruit_service_tv_status);
		shlrContent = (TextView) view
				.findViewById(R.id.recruit_service_tv_desc);
		shlrAddress = (TextView) view
				.findViewById(R.id.recruit_service_tv_address);
		shlrDate = (TextView) view.findViewById(R.id.recruit_service_tv_date);
		shlrEditLayout = (LinearLayout) view
				.findViewById(R.id.recruit_service_layout_edit);
		shlrBtnFinish = (Button) view.findViewById(R.id.btn_finish);
		shlrBtnEdit = (Button) view.findViewById(R.id.btn_edit);

	}

}
