package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.model.Configs;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.MD5;

public class PasswordResetActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_passwordreset);
		setTitleText(R.string.passwordreset_title);
		
		((EditText) findViewById(R.id.editview_new)).setInputType(0x81 | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
		((EditText) findViewById(R.id.editview_newmore)).setInputType(0x81 | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
	}
	
	public void onTipBtnClick(View view){
		switch (view.getId()) {
		case R.id.textview_confirm:
			String newPass = ((EditText)findViewById(R.id.editview_new)).getText().toString();
			String newPassMore = ((EditText)findViewById(R.id.editview_newmore)).getText().toString();
			
			if(newPass == null || newPass.length() == 0 || newPass.replaceAll(" ", "").length() == 0){
				Toast.makeText(PasswordResetActivity.this, getResources().getString(R.string.passwordreset_hint4), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(newPassMore == null || newPassMore.length() == 0 || newPassMore.replaceAll(" ", "").length() == 0){
				Toast.makeText(PasswordResetActivity.this, getResources().getString(R.string.passwordreset_hint5), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(!newPass.equalsIgnoreCase(newPassMore)){
				Toast.makeText(PasswordResetActivity.this, getResources().getString(R.string.passwordreset_hint6), Toast.LENGTH_SHORT).show();
				return;
			}
			showDialogCustom(DIALOG_CUSTOM);
			HashMap<String, Object> params = new HashMap<String, Object>();
			try {
				params.put("password", MD5.getStringMD5String(MD5.getStringMD5String(Configs.EncryptConfuse + newPass)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			params.put("tag", 1);
			params.put("key", getIntent().getStringExtra("key"));
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserResetPwd, params, PasswordResetActivity.this);
			break;

		default:
			break;
		}
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
			Toast.makeText(PasswordResetActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserResetPwd:
			if(result instanceof JSONObject){
				Toast.makeText(PasswordResetActivity.this, getResources().getString(R.string.passwordreset_hint7), Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						((HSESchoolApp)getApplication()).finishActivityList();
						PasswordResetActivity.this.finish();
					}
				}, 800);
			}
			break;

		default:
			break;
		}
	}
}
