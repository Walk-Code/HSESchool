package com.zhuochuang.hsej;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.model.DataLoader;
import com.model.TaskListener;
import com.model.TaskType;
import com.umeng.socialize.sso.UMSsoHandler;

public class BaseActivity extends FragmentActivity implements TaskListener{

	protected LinearLayout mMainLayout;
	protected final static int DIALOG_CUSTOM = 1001;
	protected final static int DIALOG_UnCancel = 1002;
	protected ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mMainLayout = (LinearLayout) View.inflate(this, R.layout.activity_base, null);
		setContentView(mMainLayout);
	}
	
	@Override
	public void setContentView(int id){
		mMainLayout.addView(View.inflate(this, id, null), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	public void onBackClick(View v){
		finish();
	}
	
	public void setTitleText(int resId){
		((TextView)mMainLayout.findViewById(R.id.textview_title)).setText(getResources().getString(resId));
	}
	
	public void setTitleText(String title){
		((TextView)mMainLayout.findViewById(R.id.textview_title)).setText(title);
	}
	
	public void setTitleMarginRight(int right){
		FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) mMainLayout.findViewById(R.id.textview_title).getLayoutParams();
		lp.rightMargin = right;
		mMainLayout.findViewById(R.id.textview_title).setLayoutParams(lp);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = ((HSESchoolApp)getApplicationContext()).umSocialServiceShare.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
	
	public void onCancelMethod(){
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		removeDialogCustom();
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		if(id == DIALOG_UnCancel){
			mProgressDialog.setCancelable(false);
		}
		else{
			mProgressDialog.setCancelable(true);
		}
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				onCancelMethod();
			}
		});
		return mProgressDialog;
	}
	
	@SuppressWarnings("deprecation")
	public void showDialogCustom(int id){
		try {
			showDialog(id);
			if(mProgressDialog != null){
				mProgressDialog.setContentView(R.layout.loading_custom);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void removeDialogCustom(){
		try {
			if(mProgressDialog != null && mProgressDialog.isShowing()){
				mProgressDialog.cancel();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//DataLoader.getInstance().isAppRunning = true;
		DataLoader.getInstance().pushApplication(BaseActivity.this);
	}

	@Override
	public void taskStarted(TaskType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskIsCanceled(TaskType type) {
		// TODO Auto-generated method stub
		
	}

}
