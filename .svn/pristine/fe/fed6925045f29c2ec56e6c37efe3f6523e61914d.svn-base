package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import com.model.Configs;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyAccountChangeActivity extends BaseActivity{

	JSONObject mUserInfoObj;
	String mLastContent;
	int mChangeType = 0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_change_usermsg);
		setTitleText(R.string.myaccount_change_title);
		mUserInfoObj = DataLoader.getInstance().getUserInfoObj();
		if(mUserInfoObj == null){
			return;
		}
		mChangeType = getIntent().getIntExtra("type", 0);
		switch (mChangeType) {
		case Configs.AccountChangeType_NickName:
			mLastContent = mUserInfoObj.optString("nickName");
			((EditText)findViewById(R.id.edittext)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
			break;
		case Configs.AccountChangeType_Introduce:
			mLastContent = mUserInfoObj.optString("selfIntroduction");
			((EditText)findViewById(R.id.edittext)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
			break;
		case Configs.AccountChangeType_Specialty:
			mLastContent = mUserInfoObj.optString("speciality");
			((EditText)findViewById(R.id.edittext)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
			break;
		case Configs.AccountChangeType_Declaration:
			mLastContent = mUserInfoObj.optString("declaration");
			((EditText)findViewById(R.id.edittext)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
			break;
		default:
			break;
		}
		((EditText)findViewById(R.id.edittext)).setText(mLastContent);
		((EditText)findViewById(R.id.edittext)).setHint(getIntent().getStringExtra("hint"));
		/*CharSequence text = ((EditText)findViewById(R.id.edittext)).getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable)text;
		    Selection.setSelection(spanText, text.length());
		}*/
		
		mLastContent = getIntent().getStringExtra("content");
		findViewById(R.id.textview_right_text).setVisibility(View.VISIBLE);
		findViewById(R.id.textview_right_text).setEnabled(false);
		((TextView)findViewById(R.id.textview_right_text)).setText(getResources().getString(R.string.confirm));
		((TextView)findViewById(R.id.textview_right_text)).setTextColor(Color.parseColor("#999999"));
		
		
		((EditText)findViewById(R.id.edittext)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				String str = ((EditText)findViewById(R.id.edittext)).getText().toString();
				if(Utils.isTextEmpty(str)){
					findViewById(R.id.textview_right_text).setEnabled(false);
					((TextView)findViewById(R.id.textview_right_text)).setTextColor(Color.parseColor("#999999"));
				}
				else if(str.equalsIgnoreCase(mLastContent)){
					findViewById(R.id.textview_right_text).setEnabled(false);
					((TextView)findViewById(R.id.textview_right_text)).setTextColor(Color.parseColor("#999999"));
				}
				else{
					findViewById(R.id.textview_right_text).setEnabled(true);
					((TextView)findViewById(R.id.textview_right_text)).setTextColor(Color.parseColor("#d74f51"));
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void onRightBtnClick(View view){
		showDialogCustom(DIALOG_CUSTOM);
		HashMap<String, Object> params = new HashMap<String, Object>();
		switch (mChangeType) {
		case Configs.AccountChangeType_NickName:
			params.put("nickName", ((EditText)findViewById(R.id.edittext)).getText().toString());
			break;
		case Configs.AccountChangeType_Introduce:
			params.put("nickName", mUserInfoObj.optString("nickName"));
			params.put("speciality", mUserInfoObj.optString("speciality"));
			params.put("declaration", mUserInfoObj.optString("declaration"));
			params.put("selfIntroduction", ((EditText)findViewById(R.id.edittext)).getText().toString());
			break;
		case Configs.AccountChangeType_Specialty:
			params.put("nickName", mUserInfoObj.optString("nickName"));
			params.put("declaration", mUserInfoObj.optString("declaration"));
			params.put("selfIntroduction", mUserInfoObj.optString("selfIntroduction"));
			params.put("speciality", ((EditText)findViewById(R.id.edittext)).getText().toString());
			break;
		case Configs.AccountChangeType_Declaration:
			params.put("nickName", mUserInfoObj.optString("nickName"));
			params.put("speciality", mUserInfoObj.optString("speciality"));
			params.put("selfIntroduction", mUserInfoObj.optString("selfIntroduction"));
			params.put("declaration", ((EditText)findViewById(R.id.edittext)).getText().toString());
			break;
		default:
			break;
		}
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserSetProfile, params, MyAccountChangeActivity.this);
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
			Toast.makeText(MyAccountChangeActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_UserSetProfile:
			Toast.makeText(MyAccountChangeActivity.this, R.string.myaccount_change_success, Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					setResult(Activity.RESULT_OK);
					MyAccountChangeActivity.this.finish();
				}
			}, 500);
			break;

		default:
			break;
		}
	}
}
