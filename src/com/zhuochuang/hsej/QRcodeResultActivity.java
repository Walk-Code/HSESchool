package com.zhuochuang.hsej;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.model.DataLoader;

public class QRcodeResultActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_qr_result);
		setTitleText(R.string.qrscan_result);
		
		((TextView)findViewById(R.id.textview_result)).setText(getIntent().getStringExtra("result"));
	}
	
	public void onOpenClick(View view){
		//Intent intent = new Intent("android.intent.action.VIEW",Uri.parse(getIntent().getStringExtra("result")));
		
		String url = getIntent().getStringExtra("result");
		Intent intent = null;
		
		if(url != null && url.length() > 0 && url.contains("/app/thirdparty/") 
				&& url.contains("needLogin=1") && !DataLoader.getInstance().isLogin()){
			intent = new Intent(QRcodeResultActivity.this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
			return;
		}
		
		intent = new Intent(QRcodeResultActivity.this, WebViewActivity.class);
		intent.putExtra("url", url);
		startActivity(intent);
	}
}
