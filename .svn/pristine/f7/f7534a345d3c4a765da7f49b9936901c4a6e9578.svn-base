package com.zhuochuang.hsej.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;

public class WXEntryActivity extends WXCallbackActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReq(BaseReq req) {
		// TODO Auto-generated method stub
		super.onReq(req);
		//req.checkArgs()
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		super.onResp(resp);
	}

	@Override
	protected void handleIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.handleIntent(intent);
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null && bundle.containsKey("_wxapi_baseresp_errcode")
					&& bundle.getInt("_wxapi_baseresp_errcode") == 0){
				Toast.makeText(WXEntryActivity.this, getResources().getString(R.string.umeng_friend_share_success), Toast.LENGTH_SHORT).show();
				((HSESchoolApp)getApplication()).shareOutstation();
			}
		}
	}
}
