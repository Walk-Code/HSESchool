package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.model.DataLoader;
import com.model.TaskType;
import com.zhuochuang.hsej.entity.CommonServiceEntity;

public class CommonServiceActivity extends BaseActivity implements
		OnClickListener {

	private CommonServiceEntity mServiceEntity;

	private WebView mWebView;
	private Button btnJoin;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_common_service);

		initView();
		initData();
	}

	private void initView() {
		findViewById(R.id.textview_right_text).setVisibility(View.GONE);
		btnJoin = (Button) findViewById(R.id.common_service_btn_join);
		mWebView = (WebView) findViewById(R.id.webview_comm_server);
		btnJoin.setOnClickListener(this);
	}

	private void initData() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", getIntent().getStringExtra("id"));
		showDialogCustom(100);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_CommonServerInfo, params, this);
	}

	private void inflaterData() {
		setTitleText(mServiceEntity.getName());
		mWebView.loadData(mServiceEntity.getContent(),
				"text/html; charset=UTF-8", null);

		if (mServiceEntity.isApplyStatus()) {
			btnJoin.setText(R.string.text_looksingn);
		} else {
			btnJoin.setText(R.string.text_join);
		}
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		if (TextUtils.isEmpty(DataLoader.getInstance().getUsetInfoKey("id"))) {
			intent = new Intent(CommonServiceActivity.this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_bottom_in,
					R.anim.push_bottom_out);
		} else {
			intent = new Intent(CommonServiceActivity.this,
					CommonServiceSignUpActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("comm_entity", mServiceEntity);
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		removeDialogCustom();
		if (result == null) {
			return;
		}
		if (result instanceof Error) {
			Toast.makeText(CommonServiceActivity.this,
					((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		if (result instanceof JSONObject) {
			switch (type) {
			case TaskOrMethod_CommonServerInfo:
				if ("1".equals(((JSONObject) result).optString("result"))) {
					mServiceEntity = new Gson().fromJson(
							((JSONObject) result).optString("item"),
							CommonServiceEntity.class);
					inflaterData();
				} else {
					Toast.makeText(this,
							R.string.activity_common_service_get_fail,
							Toast.LENGTH_SHORT).show();
					return;
				}

				break;

			default:
				break;
			}

		}
	}
}
