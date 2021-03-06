package com.layout;

import com.util.Utils;
import com.zhuochuang.hsej.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityJoinDialog {

	private Dialog mDialog;
	private OnClickListener mOnClickListener;
	String mActivityName;
	boolean mIsNotifyToFriends = true;
	
	public void setOnCheckListener(OnClickListener listener){
		mOnClickListener = listener;
	}
	
	public ActivityJoinDialog(Context context, String activityName){
		mActivityName = activityName;
		initView(context);
	}
	
	private void initView(Context context){
		final LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_dialog_activity_join, null);
		((TextView)layout.findViewById(R.id.textview_content)).setText(
				String.format(context.getResources().getString(R.string.activitydetails_dialog_content), mActivityName));
		
		mDialog = new Dialog(context, R.style.transparentFrameWindowStyleJoinActivity);
		mDialog.setContentView(layout, new LayoutParams(Utils.dipToPixels(context, 280), LayoutParams.WRAP_CONTENT));
		/*Window window = mDialog.getWindow();
		// 设置显示动画
		//window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		//wl.x = 0;
		//wl.y = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.gravity = Gravity.CENTER;
		// 设置显示位置
		mDialog.onWindowAttributesChanged(wl);*/
		// 设置点击外围解散
		mDialog.setCanceledOnTouchOutside(false);
		
		layout.findViewById(R.id.group_check).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mIsNotifyToFriends = ! mIsNotifyToFriends;
				layout.findViewById(R.id.view_check).setBackgroundResource(mIsNotifyToFriends? R.drawable.list_red_option_tick : R.drawable.option_no);
			}
		});
		
		layout.findViewById(R.id.btn_close).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancel();
			}
		});
		
		layout.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mOnClickListener != null){
					mOnClickListener.onClick(v);
				}
				cancel();
			}
		});
	}
	
	public boolean getIsCheck(){
		return mIsNotifyToFriends;
	}
	
	public void show(){
		cancel();
		if(mDialog != null && !mDialog.isShowing()){
			mDialog.show();
		}
	}
	
	public void cancel(){
		if(mDialog != null && mDialog.isShowing()){
			mDialog.cancel();
		}
	}
}
