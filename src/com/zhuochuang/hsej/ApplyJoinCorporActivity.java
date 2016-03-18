package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.Utils;
import com.zhuochuang.hsej.entity.PayResultEntity;

public class ApplyJoinCorporActivity extends BaseActivity {
	private int applyCost = 0;
	private int commId;
	private PayResultEntity mPayResultEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_join_corpor);

		setTitleText(R.string.title_activity_apply_join_corpor);
		initView();
	}

	private void initView() {
		commId = Integer.parseInt(getIntent().getStringExtra("comm_id"));
		applyCost = Integer.parseInt(getIntent().getStringExtra("comm_cost"));
		JSONObject object = DataLoader.getInstance().getUserInfoObj();

		((EditText) findViewById(R.id.apply_et_schid)).setText(object
				.optString("xh"));
		((EditText) findViewById(R.id.apply_et_name)).setText(object
				.optString("xm"));
		((EditText) findViewById(R.id.apply_et_classid)).setText(object
				.optString("bjmc"));
		((EditText) findViewById(R.id.apply_et_sex)).setText("0"
				.equalsIgnoreCase(object.optString("xb")) ? "女" : "男");
		((EditText) findViewById(R.id.apply_et_idcard)).setText(object
				.optString("sfzh"));
		((EditText) findViewById(R.id.apply_et_phone)).setText(object
				.optString("phone"));
		((EditText) findViewById(R.id.apply_et_address)).setText(object
				.optString("ssAddress"));
		if (applyCost > 0) {
			findViewById(R.id.apply_cost_layout).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.apply_tv_cost))
					.setText(String
							.format(getResources().getString(
									R.string.list_amount_text), applyCost));
		} else {
			findViewById(R.id.apply_cost_layout).setVisibility(View.GONE);
		}
		Utils.removeSoftKeyboard(this);

		findViewById(R.id.apply_btn_submit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText etPhone = ((EditText) findViewById(R.id.apply_et_phone));
						if (!Utils.isMobileNO(etPhone.getText().toString())) {
							Toast.makeText(
									ApplyJoinCorporActivity.this,
									getResources().getString(
											R.string.text_qsrzqdsjhm),
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (applyCost > 0) {
							HashMap<String, Object> payParams = new HashMap<String, Object>();
							payParams.put("resourceType", 2);
							payParams.put("resourceId", commId);
							showDialogCustom(100);
							DataLoader.getInstance().startTaskForResult(
									TaskType.TaskOrMethod_CommonGetPayUrl,
									payParams, ApplyJoinCorporActivity.this);
						} else {
							startApply();
						}
					}
				});
	}

	public void startApply() {
		HashMap<String, Object> applyParams = new HashMap<String, Object>();
		applyParams.put("type", 1);
		applyParams.put("phone", ((EditText) findViewById(R.id.apply_et_phone))
				.getText().toString());
		applyParams.put("communityId", commId);
		showDialogCustom(100);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_ApplyCommunity, applyParams, this);
	}

	public void loadPayData() {
		HashMap<String, Object> payParams = new HashMap<String, Object>();
		payParams.put("sign", mPayResultEntity.getItems().getSign());
		payParams.put("pay_type", mPayResultEntity.getItems().getPay_type());
		payParams.put("request_xml", mPayResultEntity.getItems()
				.getRequest_xml());
		showDialogCustom(100);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_GetWay, payParams, this);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		removeDialogCustom();
		if (result == null)
			return;
		if (result instanceof Error) {
			Toast.makeText(this, ((Error) result).getMessage(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_CommonGetPayUrl:
			Gson gson2 = new Gson();
			mPayResultEntity = gson2.fromJson(result.toString(),
					PayResultEntity.class);
			if (mPayResultEntity.getResult() == 1) {
				loadPayData();
			} else {
				Toast.makeText(this, R.string.text_get_pay_fail,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case TaskOrMethod_GetWay:
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra("result", result.toString());
			startActivityForResult(intent, 100);
			break;
		case TaskOrMethod_ApplyCommunity:
			Toast toast = new Toast(this);
			toast.setGravity(Gravity.CENTER, 0, 0);
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.view_layout_toast, null);
			toast.setView(layout);
			toast.setDuration(Toast.LENGTH_SHORT);
			TextView textView = (TextView) layout.findViewById(R.id.message);
			if ("1".equalsIgnoreCase(((JSONObject) result).optString("result"))) {
				textView.setText(R.string.text_join_success);
			} else {
				textView.setText(R.string.text_join_fail);
			}
			toast.show();
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			startApply();
		}
	}

}
