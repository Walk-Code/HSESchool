package com.zhuochuang.hsej.store;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.MainActivity;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.WebViewActivity;

public class StorePayFinishActivity extends BaseActivity{
	private static final int MAINACTIVITY_SCHOOLFRAGMENT = 1;
	private JSONObject mResult;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mMainLayout.removeAllViews();
		setContentView(R.layout.store_good_ppurchase_succeeded);		
		try {
			mResult = new JSONObject(getIntent().getStringExtra("result"));			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		initView();	
	}

	private void initView() {	
		((TextView)findViewById(R.id.store_order_num)).setText(getResources().getString(R.string.serial_number)+mResult.optString("dealNumber"));	
		findViewById(R.id.store_home_back).setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {				
				Intent intent = new Intent(StorePayFinishActivity.this,MainActivity.class);
				intent.putExtra("StorePayFinishActivity", MAINACTIVITY_SCHOOLFRAGMENT);
				startActivity(intent);
			}
		});
	
		findViewById(R.id.store_check_order).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StorePayFinishActivity.this,StoreOrderActivity.class);
				startActivity(intent);
			}
		});
		//store_order_pay
		((TextView)findViewById(R.id.store_order_pay)).setOnClickListener(new OnClickListener() {	
			
			@Override
			public void onClick(View arg0) {					
					Intent intent  = new Intent(StorePayFinishActivity.this,WebViewActivity.class);				
					intent.putExtra("url",mResult.optJSONObject("items").optString("payUrl"));
					intent.putExtra("postData","sign="+mResult.optJSONObject("items").optString("sign")+
												"&request_xml="+mResult.optJSONObject("items").optString("request_xml")+
												"&pay_type="+mResult.optJSONObject("items").optString("pay_type"));
					startActivity(intent);
			}
		});
	}
}
