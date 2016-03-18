package com.zhuochuang.hsej;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.model.Configs;
import com.model.DataLoader;
import com.model.LoginParamsItem;
import com.model.SharedPreferenceHandler;
import com.model.TaskType;
import com.util.MD5;

public class ChangePasswordActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_changepassword);
		setTitleText(R.string.mycenter_cell10);
		
		((EditText) findViewById(R.id.editview_oldpass)).setInputType(0x81 | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
		((EditText) findViewById(R.id.editview_newpass)).setInputType(0x81 | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
		((EditText) findViewById(R.id.editview_newpass_repetition)).setInputType(0x81 | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
	}
	
	public void onCommitClick(View view){
		String old = ((EditText)findViewById(R.id.editview_oldpass)).getText().toString();
		String news = ((EditText)findViewById(R.id.editview_newpass)).getText().toString();
		String newsRe = ((EditText)findViewById(R.id.editview_newpass_repetition)).getText().toString();
		
		if(TextUtils.isEmpty(old)){
			Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.changepass_old_toast), Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(news)){
			Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.changepass_new_toast), Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(newsRe)){
			Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.changepass_new_re_toast), Toast.LENGTH_SHORT).show();
			return;
		}
		if(!news.equalsIgnoreCase(newsRe)){
			Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.changepass_new_unre_toast), Toast.LENGTH_SHORT).show();
			return;
		}
		
		showDialogCustom(DIALOG_CUSTOM);
		HashMap<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("password", MD5.getStringMD5String(MD5.getStringMD5String(Configs.EncryptConfuse + old)));
			params.put("newPassword", MD5.getStringMD5String(MD5.getStringMD5String(Configs.EncryptConfuse + news)));
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserUpdatePwd, params, ChangePasswordActivity.this);
		} 
		catch (Exception e) {
			// TODO: handle exception
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
			Toast.makeText(ChangePasswordActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_UserUpdatePwd:
			Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.changepass_success), Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						LoginParamsItem item  = SharedPreferenceHandler.getLoginParamsItem(ChangePasswordActivity.this);
						item.userPassword = MD5.getStringMD5String(MD5.getStringMD5String(Configs.EncryptConfuse + 
								((EditText)findViewById(R.id.editview_newpass)).getText().toString()));
						SharedPreferenceHandler.saveLoginParams(ChangePasswordActivity.this, item);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ChangePasswordActivity.this.finish();
				}
			}, 800);
			break;

		default:
			break;
		}
	}

}
