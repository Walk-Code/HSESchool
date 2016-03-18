package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.model.Configs;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskType;
import com.util.MD5;
import com.util.Utils;

public class LoginActivity extends BaseActivity{

	private int mUserType = 2;
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_login);
		setTitleText(R.string.login_title);
		findViewById(R.id.textview_forgetpass).setVisibility(View.VISIBLE);
		
		//((EditText) findViewById(R.id.editview_people)).setInputType(InputType.TYPE_CLASS_NUMBER);
		((EditText) findViewById(R.id.editview_password)).setInputType(0x81 | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
		
		findViewById(R.id.textview_login).setClickable(true);
		findViewById(R.id.textview_login).setEnabled(true);
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Object[] objs = (Object[]) msg.obj;
				if(objs == null){
					return;
				}
				switch (msg.what) {
				case EventManager.EventType_LoginForRegister:
					((EditText) findViewById(R.id.editview_people)).setText((String) objs[0]);
					((EditText) findViewById(R.id.editview_password)).setText((String) objs[1]);
					mUserType = 3;
					findViewById(R.id.check1).setBackgroundResource(R.drawable.sign_p);
					findViewById(R.id.check2).setBackgroundResource(R.drawable.sign_n);
					showDialogCustom(DIALOG_CUSTOM);
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("sign", mUserType);
					params.put("account", objs[0]);
					try {
						params.put("password", MD5.getStringMD5String(MD5.getStringMD5String(Configs.EncryptConfuse + (String) objs[1])));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserLogin, params, LoginActivity.this);
					break;

				default:
					break;
				}
			}
		});
	}
	
	@Override
	public void onBackClick(View v) {
		// TODO Auto-generated method stub
		super.onBackClick(v);
		overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
	}
	
	public void onItemSignClick(View view){
		switch (view.getId()) {
		case R.id.sign_1:
			mUserType = 3;
			findViewById(R.id.check1).setBackgroundResource(R.drawable.sign_p);
			findViewById(R.id.check2).setBackgroundResource(R.drawable.sign_n);
			break;
			
		case R.id.sign_2:
			mUserType = 2;
			findViewById(R.id.check1).setBackgroundResource(R.drawable.sign_n);
			findViewById(R.id.check2).setBackgroundResource(R.drawable.sign_p);
			break;

		default:
			break;
		}
	}
	
	public void onTipBtnClick(View view){
		switch (view.getId()) {
		case R.id.textview_login:
			
			String account = ((EditText) findViewById(R.id.editview_people)).getText().toString();
			if(account == null || account.length() == 0 || account.replaceAll(" ", "").length() == 0){
				Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_account_null), Toast.LENGTH_SHORT).show();
				return;
			}
			
			String password = ((EditText) findViewById(R.id.editview_password)).getText().toString();
			if(password == null || password.length() == 0 || password.replaceAll(" ", "").length() == 0){
				Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_password_null), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(mUserType == 3 && !Utils.isMobileNO(account)){
				Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_register_phone_valid), Toast.LENGTH_SHORT).show();
				return;
			}
			
			showDialogCustom(DIALOG_CUSTOM);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("sign", mUserType);
			params.put("account", account);
			try {
				params.put("password", MD5.getStringMD5String(MD5.getStringMD5String(Configs.EncryptConfuse + password)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserLogin, params, LoginActivity.this);
			
			break;
			
		case R.id.textview_register:
			startActivity(new Intent(LoginActivity.this, LoginRegisterFirstActivity.class));
			break;
			
		case R.id.textview_forgetpass:
			startActivity(new Intent(LoginActivity.this, PasswordFindActivity.class));
			break;

		default:
			break;
		}
	}

	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
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
			Toast.makeText(LoginActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserLogin:
			if(result instanceof JSONObject){
				Intent intent = new Intent();
				intent.setAction(Configs.LoginStateChange);
				sendBroadcast(intent);
				
				this.finish();
	            overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
			}
			break;

		default:
			break;
		}
	}

}
