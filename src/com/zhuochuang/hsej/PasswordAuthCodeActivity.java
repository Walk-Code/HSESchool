package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.TaskType;

public class PasswordAuthCodeActivity extends BaseActivity{

	JSONObject mDataObj;
	JSONObject mAuthCodeObj;
	CountDownTimer mCountDownTimer;
	String mAuthCode = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivity(PasswordAuthCodeActivity.this);
		setContentView(R.layout.activity_password_authcode);
		setTitleText(R.string.passwordreset_title);
		
		try {
			mDataObj = new JSONObject(getIntent().getStringExtra("data"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(mDataObj == null){
			return;
		}
		
		String tele = mDataObj.optString("phone");
		if(tele != null && tele.length() == 11){
			tele = tele.substring(0, 3) + "****" + tele.substring(7, 11);
		}
		
		((TextView)findViewById(R.id.textview_phone)).setText(String.format(getResources().getString(R.string.passwordauthcode_hint3), tele));
	}
	
	private void createTimer(){
		mCountDownTimer = new CountDownTimer(mAuthCodeObj.optInt("seconds") * 1000, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				findViewById(R.id.textview_seconds).setEnabled(false);
				((TextView)findViewById(R.id.textview_seconds)).setText(millisUntilFinished / 1000 + "s");
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				findViewById(R.id.textview_seconds).setEnabled(true);
				((TextView)findViewById(R.id.textview_seconds)).setText(getResources().getString(R.string.passwordauthcode_hint1));
				mAuthCode = "unknown";
			}
		};
	}
	
	public void onTipBtnClick(View view){
		switch (view.getId()) {
		case R.id.textview_seconds:
			showDialogCustom(DIALOG_CUSTOM);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("phone", mDataObj.optString("phone"));
			params.put("key", mDataObj.optString("key"));
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserGetCode, params, PasswordAuthCodeActivity.this);
			break;
		case R.id.textview_next:
			if(mAuthCode == null){
				Toast.makeText(PasswordAuthCodeActivity.this, getResources().getString(R.string.passwordauthcode_hint6), Toast.LENGTH_SHORT).show();
				return;
			}
			String code = ((EditText)findViewById(R.id.editview_authcode)).getText().toString();
			if(code == null || code.length() == 0 || code.replaceAll(" ", "").length() == 0){
				Toast.makeText(PasswordAuthCodeActivity.this, getResources().getString(R.string.passwordauthcode_hint2), Toast.LENGTH_SHORT).show();
				return;
			}
			if(mAuthCode.equalsIgnoreCase("unknown")){
				Toast.makeText(PasswordAuthCodeActivity.this, getResources().getString(R.string.passwordauthcode_hint4), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(!mAuthCode.equalsIgnoreCase(code)){
				Toast.makeText(PasswordAuthCodeActivity.this, getResources().getString(R.string.passwordauthcode_hint5), Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent(PasswordAuthCodeActivity.this, PasswordResetActivity.class);
			intent.putExtra("key", mDataObj.optString("key"));
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mCountDownTimer != null){
			mCountDownTimer.cancel();
		}
		((HSESchoolApp)getApplication()).removeActivity(PasswordAuthCodeActivity.this);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		removeDialogCustom();
		
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(PasswordAuthCodeActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserGetCode:
			if(result instanceof JSONObject && ((JSONObject)result).has("item")){
				mAuthCodeObj = ((JSONObject)result).optJSONObject("item");
				mAuthCode = mAuthCodeObj.optString("code");
				createTimer();
				if(mCountDownTimer != null){
					mCountDownTimer.start();
				}
			}
			break;

		default:
			break;
		}
	}
}
