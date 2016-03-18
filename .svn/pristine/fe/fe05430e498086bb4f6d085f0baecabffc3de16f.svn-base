package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.model.Configs;
import com.model.DataLoader;
import com.model.SharedPreferenceHandler;
import com.model.TaskType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.utils.OauthHelper;
/**
 * 设置
 * @author sam
 * modify kris
 *
 */
public class SettingActivity extends BaseActivity implements OnClickListener{
	private JSONObject mUserInfo;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		mUserInfo = DataLoader.getInstance().getUserInfoObj();
		if(mUserInfo != null){
			setContentView(R.layout.activity_setting);
			initView();
		}
	}
	
	protected void initView(){
		setTitleText(getString(R.string.setting_title));
		findViewById(R.id.textview_right_text).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.textview_right_text)).setText(getString(R.string.setting_save));
		
		((CheckBox) findViewById(R.id.friend_check)).setChecked(mUserInfo.optBoolean("needApproval"));
		((CheckBox) findViewById(R.id.search_check)).setChecked(mUserInfo.optBoolean("findable"));
		((CheckBox) findViewById(R.id.phone_check)).setChecked(mUserInfo.optBoolean("phoneVisible"));
		((CheckBox) findViewById(R.id.email_check)).setChecked(mUserInfo.optBoolean("emailVisible"));
		((CheckBox) findViewById(R.id.stranger_check)).setChecked(mUserInfo.optBoolean("attentionable"));
		
		findViewById(R.id.setting_btn_logout).setOnClickListener(this);
		findViewById(R.id.settting_app).setOnClickListener(this);
		findViewById(R.id.setting_add_friend).setOnClickListener(this);
		findViewById(R.id.setting_search).setOnClickListener(this);
		findViewById(R.id.setting_phone).setOnClickListener(this);
		findViewById(R.id.setting_email).setOnClickListener(this);
		findViewById(R.id.setting_stranger).setOnClickListener(this);
		
		if(isAuthenticatedAndTokenNotExpired(SHARE_MEDIA.SINA)) {
			findViewById(R.id.setting_sharesina_logout).setBackgroundResource(R.drawable.btn_red_selector);
			findViewById(R.id.setting_sharesina_logout).setEnabled(true);
		}
		else{
			findViewById(R.id.setting_sharesina_logout).setBackgroundResource(R.drawable.send_bg_gray);
			findViewById(R.id.setting_sharesina_logout).setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setting_btn_logout:
			showDialogCustom(DIALOG_CUSTOM);
            DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserLogout, null, SettingActivity.this);
			break;
		case R.id.settting_app:

			break;
		case R.id.setting_add_friend:
			CheckBox friendBox = (CheckBox) findViewById(R.id.friend_check);
			friendBox.setChecked(!friendBox.isChecked());
			break;
		case R.id.setting_search:
			CheckBox searchBox = (CheckBox) findViewById(R.id.search_check);
			searchBox.setChecked(!searchBox.isChecked());
			break;
		case R.id.setting_phone:
			CheckBox phoneBox = (CheckBox) findViewById(R.id.phone_check);
			phoneBox.setChecked(!phoneBox.isChecked());
			break;
		case R.id.setting_email:
			CheckBox emailBox = (CheckBox) findViewById(R.id.email_check);
			emailBox.setChecked(!emailBox.isChecked());
			break;
		case R.id.setting_stranger:
			CheckBox strangerBox = (CheckBox) findViewById(R.id.stranger_check);
			strangerBox.setChecked(!strangerBox.isChecked());
			break;

		default:
			break;
		}
	}

	public void onRightBtnClick(View v){
		showDialogCustom(DIALOG_CUSTOM);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("needApproval", ((CheckBox) findViewById(R.id.friend_check)).isChecked());//别人请求加我时，是否需要我审核
		params.put("findable", ((CheckBox) findViewById(R.id.search_check)).isChecked());//陌生人是否可以搜索到我
		params.put("phoneVisible", ((CheckBox) findViewById(R.id.phone_check)).isChecked());//手机号码是否可见
		params.put("emailVisible", ((CheckBox) findViewById(R.id.email_check)).isChecked());//邮箱是否可见
		params.put("attentionable", ((CheckBox) findViewById(R.id.stranger_check)).isChecked());//陌生人是否可关注我

		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserSetProperties, params, this);
	}
	
	public void logOut(final Context context, final SHARE_MEDIA plat) {
        if(isAuthenticatedAndTokenNotExpired(plat)) {
        	showDialogCustom(DIALOG_CUSTOM);
        	((HSESchoolApp)getApplicationContext()).umSocialServiceShare.deleteOauth(context, plat,
                new SocializeClientListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onComplete(int status,SocializeEntity entity) {
                	removeDialogCustom();
                     if (status == 200) {
                    	 OauthHelper.removeTokenExpiresIn(context, plat);
                    	 Toast.makeText(context, SettingActivity.this.getResources().getString(R.string.setting_share_logout_success),Toast.LENGTH_SHORT).show();
                     }
                     else {
                    	 Toast.makeText(context, SettingActivity.this.getResources().getString(R.string.setting_share_logout_fail),Toast.LENGTH_SHORT).show();
                     }
                     if(isAuthenticatedAndTokenNotExpired(plat)) {
                    	 findViewById(R.id.setting_sharesina_logout).setBackgroundResource(R.drawable.btn_red_selector);
              			findViewById(R.id.setting_sharesina_logout).setEnabled(true);
             		}
             		else{
             			findViewById(R.id.setting_sharesina_logout).setBackgroundResource(R.drawable.send_bg_gray);
             			findViewById(R.id.setting_sharesina_logout).setEnabled(false);
             		}
                }
            });
        }
	}
	
	private boolean isAuthenticatedAndTokenNotExpired(SHARE_MEDIA plat){
		return OauthHelper.isAuthenticatedAndTokenNotExpired(SettingActivity.this, plat);
	}
	
	public void onShareLogoutClick(View view){
		switch (view.getId()) {
		case R.id.setting_sharesina_logout:
			logOut(SettingActivity.this, SHARE_MEDIA.SINA);
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
			Toast.makeText(SettingActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		JSONObject obj = null;  
		switch (type) {
		case TaskOrMethod_UserLogout:
			obj = (JSONObject) result;
			if(obj != null && obj.has("result") && obj.optString("result").equalsIgnoreCase("1")){
				//startActivity(new Intent(SettingActivity.this, LoginActivity.class));
				Intent intent = new Intent();
				intent.setAction(Configs.LoginStateChange);
				sendBroadcast(intent);
				SettingActivity.this.finish();
			}
			break;
		case TaskOrMethod_UserSetProperties:
			obj = (JSONObject) result;
			if(obj != null && obj.has("result") && obj.optString("result").equalsIgnoreCase("1")){
				
				try {
					mUserInfo.put("needApproval", ((CheckBox) findViewById(R.id.friend_check)).isChecked());
					mUserInfo.put("findable", ((CheckBox) findViewById(R.id.search_check)).isChecked());
					mUserInfo.put("phoneVisible", ((CheckBox) findViewById(R.id.phone_check)).isChecked());
					mUserInfo.put("emailVisible", ((CheckBox) findViewById(R.id.email_check)).isChecked());
					mUserInfo.put("attentionable", ((CheckBox) findViewById(R.id.stranger_check)).isChecked());
					SharedPreferenceHandler.saveUserInfo(this, mUserInfo.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(this, getString(R.string.setting_save_success), Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
