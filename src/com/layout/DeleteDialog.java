package com.layout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuochuang.hsej.R;

public class DeleteDialog {

	private Dialog mDialog;
	private OnItemSelectListener mOnItemSelectListener;
	
	public interface OnItemSelectListener {
		public void onItemClick(int position);
	}

	public void setItemSelectListener(OnItemSelectListener listener){
		this.mOnItemSelectListener = listener;
	}
	
	public DeleteDialog(Context context){
		initView(context);
	}
	
	@SuppressWarnings("deprecation")
	private void initView(Context context){
		LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_delete, null);
		mDialog = new Dialog(context, R.style.transparentFrameWindowStyle);
		mDialog.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = mDialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		mDialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		mDialog.setCanceledOnTouchOutside(true);
		
		for(int i = 0; i < layout.getChildCount(); i++){
			View v = layout.getChildAt(i);
			if(v instanceof TextView){
				v.setOnClickListener(new OnItemListener());
			}
		}
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
	
	class OnItemListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(mOnItemSelectListener != null){
				mOnItemSelectListener.onItemClick(v.getId());
			}
			cancel();
		}
	};

}
