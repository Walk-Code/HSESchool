package com.layout;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.util.Utils;
import com.zhuochuang.hsej.R;



public class BottomListDialog {

    OnItemSelectListener mOnItemSelectListener;
    ArrayList<String> mItemText;
    LinearLayout view;
    Dialog mDialog;
    Context mContext;
	
	
	public BottomListDialog(Context context, ArrayList<String> itemText){
		mContext = context;
		mItemText = itemText;
		initView();
	}
	
	@SuppressWarnings("deprecation")
	private void initView(){
		view = new LinearLayout(mContext);
		view.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(params);
		view.setBackgroundColor(Color.rgb(166, 166, 166));
		
		//item view
		for(int i = 0; i < mItemText.size(); i ++){
			Button itemBtn = new Button(mContext);
			itemBtn.setText(mItemText.get(i));
			itemBtn.setTextColor(Color.rgb(75, 75, 75));
			itemBtn.setPadding(0, Utils.dipToPixels(mContext, 10), 0, Utils.dipToPixels(mContext, 10));
			itemBtn.setBackgroundResource(R.drawable.bottom_pop_selector);
			itemBtn.setTextSize(16.f);
			ViewGroup.LayoutParams textParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dipToPixels(mContext, 50));
			itemBtn.setLayoutParams(textParams);
			itemBtn.setTag(i);
			itemBtn.setOnClickListener(clickListener);
			
			if(i < mItemText.size()){
				TextView line = new TextView(mContext);
				line.setBackgroundColor(Color.rgb(210, 210, 210));
				ViewGroup.LayoutParams lineParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dipToPixels(mContext, 0.5f));
				line.setLayoutParams(lineParams);
				
				view.addView(line);
			}
			
			view.addView(itemBtn);
		}
		
		//cancel view 
		Button cancelBtn = new Button(mContext);
		cancelBtn.setText(mContext.getString(R.string.cancel));
		cancelBtn.setTextColor(Color.rgb(75, 75, 75));
		cancelBtn.setPadding(0, Utils.dipToPixels(mContext, 5), 0, Utils.dipToPixels(mContext, 10));
		
		cancelBtn.setBackgroundResource(R.drawable.bottom_pop_selector);
		cancelBtn.setTextSize(16.f);
		LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dipToPixels(mContext, 50));
		cancelBtn.setLayoutParams(cancelParams);
		cancelParams.topMargin = Utils.dipToPixels(mContext, 5);
		cancelBtn.setTag(-1);
		cancelBtn.setOnClickListener(clickListener);
		view.addView(cancelBtn);
		
		mDialog = new Dialog(mContext, R.style.transparentFrameWindowStyle);
		mDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = mDialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity)mContext).getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		mDialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		mDialog.setCanceledOnTouchOutside(true);
		
	}
	
	public void show(){
		if(mDialog != null && !mDialog.isShowing())
		mDialog.show();
	}
	
	public boolean isShowing(){
		return mDialog == null ? false : mDialog.isShowing();
	}
	
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = (Integer) v.getTag();
			
			if(mOnItemSelectListener != null){
				mOnItemSelectListener.onItemClick(id);
			}
			mDialog.cancel();
		}
	};
	
	public interface OnItemSelectListener {
		public void onItemClick(int position);
	}

	public void setItemSelectListener(OnItemSelectListener listener){
		this.mOnItemSelectListener = listener;
	}
}
