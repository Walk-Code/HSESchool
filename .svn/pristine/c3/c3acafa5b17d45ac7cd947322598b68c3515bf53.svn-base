package com.zhuochuang.hsej;

import com.model.EventManager;
import com.zhuochuang.hsej.store.StoreOrderActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StorePayFinishActivity extends BaseActivity implements
		OnClickListener {

	private Button btnBack;
	private Button btnLookOrder;
	private TextView tvMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_pay_finish);

		setTitleText(R.string.text_pay_success);
		findViewById(R.id.group_left).setVisibility(View.GONE);
		tvMessage = (TextView) findViewById(R.id.pay_message);

		String delNumber = getIntent().getStringExtra("dealNumber");
		tvMessage.setText(R.string.text_pay_success_notice);

		initView();

	}

	private void initView() {
		btnLookOrder = (Button) findViewById(R.id.pay_btn_look);
		btnLookOrder.setOnClickListener(this);
		btnBack = (Button) findViewById(R.id.pay_btn_back);
		btnBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_btn_look:
			startActivity(new Intent(StorePayFinishActivity.this,
					StoreOrderActivity.class));
			break;
		case R.id.pay_btn_back:
			EventManager.getInstance().sendMessage(EventManager.EventType_StoreHomepageLoad, new Object());
			((HSESchoolApp) getApplication()).finishActivityListStore();
			break;
		default:
			break;
		}
		StorePayFinishActivity.this.finish();
	}

}
