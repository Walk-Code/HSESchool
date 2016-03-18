package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.TaskType;

public class LoginRegisterFirstActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivity(LoginRegisterFirstActivity.this);
		setContentView(R.layout.activity_login_register_first);
		setTitleText(R.string.login_welcome);
	}
	
	public void onTipBtnClick(View view){
		switch (view.getId()) {
		case R.id.textview_loginwelcome_next:
			
			String name = ((EditText)findViewById(R.id.editview_name)).getText().toString();
			String number = ((EditText)findViewById(R.id.editview_card)).getText().toString();
			
			if(name == null || name.length() == 0 || name.replaceAll(" ", "").length() == 0){
				Toast.makeText(LoginRegisterFirstActivity.this, getResources().getString(R.string.login_welcome_hint1), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(number == null || number.length() == 0 || number.replaceAll(" ", "").length() == 0){
				Toast.makeText(LoginRegisterFirstActivity.this, getResources().getString(R.string.login_welcome_hint2_null), Toast.LENGTH_SHORT).show();
				return;
			}
			showDialogCustom(DIALOG_CUSTOM);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("name", name);
			params.put("number", number);
			params.put("sign", 1);
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserRegister, params, LoginRegisterFirstActivity.this);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((HSESchoolApp)getApplication()).removeActivity(LoginRegisterFirstActivity.this);
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
			Toast.makeText(LoginRegisterFirstActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserRegister:
			if(result instanceof JSONObject){
				//this.finish();
	           // overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
				Intent intent = new Intent(LoginRegisterFirstActivity.this, LoginRegisterSecondActivity.class);
				intent.putExtra("id", ((JSONObject)result).optString("id"));
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}
}
