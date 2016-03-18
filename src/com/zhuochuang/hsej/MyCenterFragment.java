package com.zhuochuang.hsej;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.model.DataLoader;
import com.model.DefineHandler;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.Utils;
import com.zhuochuang.hsej.store.StoreOrderActivity;
import com.zhuochuang.hsej.store.StoreOrderStatusActivity;

@SuppressLint("InflateParams")
public class MyCenterFragment extends BaseFragment{

	boolean mIsLogin = true;
	
	JSONObject mUserInfoObj;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mMainLayout = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mycenter, null);
	}
	
	private void initLoginStatue(){
		mUserInfoObj = DataLoader.getInstance().getUserInfoObj();
		if(mUserInfoObj != null){
			mMainLayout.findViewById(R.id.mycenter_logined_layout).setVisibility(View.VISIBLE);
			mMainLayout.findViewById(R.id.mycenter_store).setVisibility(View.VISIBLE);
			((TextView)mMainLayout.findViewById(R.id.mycenter_desc)).setTextColor(Color.WHITE);
			((TextView)mMainLayout.findViewById(R.id.mycenter_name)).setText(DataLoader.getInstance().getUsetInfoKey("nickName"));
			((TextView)mMainLayout.findViewById(R.id.mycenter_desc)).setText(
							DataLoader.getInstance().getUsetInfoKey("bjmc"));
			((TextView)mMainLayout.findViewById(R.id.mycenter_rank)).setVisibility(View.VISIBLE);
			((TextView)mMainLayout.findViewById(R.id.mycenter_rank)).setText("头衔："+
					DataLoader.getInstance().getUsetInfoKey("honoraryTitle"));
			//((TextView)mMainLayout.findViewById(R.id.textview_integral)).setText(DataLoader.getInstance().getUsetInfoKey("integral"));
			((TextView)mMainLayout.findViewById(R.id.textview_integral)).setText(DefineHandler.getMsgNotifyType(mActivity, DefineHandler.MsgType_Integral) + "");
			
			ImageLoader.getInstance().displayImage(DataLoader.getInstance().getUsetInfoKey("headImage"), 
			     ((ImageView)mMainLayout.findViewById(R.id.mycenter_imageview_header)), ImageLoaderConfigs.displayImageOptionsRoundCenter);
			
			setStoreData();
		}
		else{
			mMainLayout.findViewById(R.id.mycenter_logined_layout).setVisibility(View.GONE);
			mMainLayout.findViewById(R.id.mycenter_store).setVisibility(View.GONE);
			((TextView)mMainLayout.findViewById(R.id.mycenter_rank)).setVisibility(View.GONE);
			((TextView)mMainLayout.findViewById(R.id.mycenter_desc)).setTextColor(Color.argb(255, 239, 212, 205));
			((TextView)mMainLayout.findViewById(R.id.mycenter_name)).setText(mActivity.getResources().getString(R.string.mycenter_login));
			((TextView)mMainLayout.findViewById(R.id.mycenter_desc)).setText(mActivity.getResources().getString(R.string.mycenter_login_notify));
			ImageLoader.getInstance().displayImage("", 
				     ((ImageView)mMainLayout.findViewById(R.id.mycenter_imageview_header)), ImageLoaderConfigs.displayImageOptionsRoundCenter);
		}
	}
	
	private void setStoreData(){
		int obligation = DefineHandler.getMsgNotifyType(mActivity, DefineHandler.MsgType_StoreObligation);
		int receiving = DefineHandler.getMsgNotifyType(mActivity, DefineHandler.MsgType_StoreReceiving);
		int evaluate = DefineHandler.getMsgNotifyType(mActivity, DefineHandler.MsgType_StoreEvaluate);
		int refund = DefineHandler.getMsgNotifyType(mActivity, DefineHandler.MsgType_StoreRefund);
		
		mMainLayout.findViewById(R.id.textview_unread1).setVisibility(obligation == 0? View.GONE : View.VISIBLE);
		mMainLayout.findViewById(R.id.textview_unread2).setVisibility(receiving == 0? View.GONE : View.VISIBLE);
		mMainLayout.findViewById(R.id.textview_unread3).setVisibility(evaluate == 0? View.GONE : View.VISIBLE);
		mMainLayout.findViewById(R.id.textview_unread4).setVisibility(refund == 0? View.GONE : View.VISIBLE);
		
		((TextView)mMainLayout.findViewById(R.id.textview_unread1)).setText(obligation + "");
		((TextView)mMainLayout.findViewById(R.id.textview_unread2)).setText(receiving + "");
		((TextView)mMainLayout.findViewById(R.id.textview_unread3)).setText(evaluate + "");
		((TextView)mMainLayout.findViewById(R.id.textview_unread4)).setText(refund + "");
	}

	public void onMycenterClick(View v) {
		switch (v.getId()) {
		case R.id.mycenter_group_header:
			if(mUserInfoObj != null){
				startActivity(new Intent(mActivity, MyAccountActivity.class));
			}
			else{
				startActivity(new Intent(mActivity, LoginActivity.class));
				mActivity.overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
			}
			break;
		case R.id.mycenter_message:
			startActivity(new Intent(mActivity, MyMessageActivity.class));
			EventManager.getInstance().sendMessage(EventManager.EventType_MsgChange, new Object());
			break;
		case R.id.mycenter_friends:
			Intent intent = new Intent(mActivity, MyFriendsActivity.class);
			startActivity(intent);
			break;
		case R.id.mycenter_post:
			startActivity(new Intent(mActivity, MyPostActivity.class));
			break;
		case R.id.mycenter_integral:
			startActivity(new Intent(mActivity, MyIntegralActivity.class));
			break;
		case R.id.mycenter_collect:
			startActivity(new Intent(mActivity, MyFavoriteActivity.class));
			break;
		case R.id.mycenter_card:
			startActivity(new Intent(mActivity, MyCardActivity.class));
			break;
		case R.id.mycenter_changepass:
			startActivity(new Intent(mActivity, ChangePasswordActivity.class));
			break;
		case R.id.mycenter_setting:
			if(mUserInfoObj != null){
				startActivity(new Intent(mActivity, SettingActivity.class));
			}
			else{
				startActivity(new Intent(mActivity, LoginActivity.class));
				mActivity.overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
			}
			break;
		case R.id.mycenter_about:
			intent = new Intent(mActivity, WebViewActivity.class);
			String url = DataLoader.getInstance().getSettingKey("aboutUrl");
			if(url != null){
				url += url.contains("?") ? "&version=" : "?version=";
				intent.putExtra("url", url + Utils.getVersionName(mActivity)+"("+Utils.getVersionNumber(mActivity)+")");
				startActivity(intent);
			}
			break;
		case R.id.mycenter_feedback:
			if(mUserInfoObj != null){
				intent = new Intent(mActivity, FeedbackActivity.class);
				intent.putExtra("isFeedback", true);
				startActivity(new Intent(intent));
			}
			else{
				startActivity(new Intent(mActivity, LoginActivity.class));
				mActivity.overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
			}
			break;
			//**************store
		case R.id.mycenter_store_allorder:
			startActivity(new Intent(mActivity, StoreOrderActivity.class));
			break;
		case R.id.group_store1:
			intent = new Intent(mActivity, StoreOrderStatusActivity.class);
			intent.putExtra("orderStatus", "1");
			intent.putExtra("flag", "1");
			intent.putExtra("title", getResources().getString(R.string.mycenter_storeorder_cell1));
			startActivity(intent);
			break;
		case R.id.group_store2:
			intent = new Intent(mActivity, StoreOrderStatusActivity.class);
			intent.putExtra("orderStatus", "2,3,4,5");
			intent.putExtra("flag", "2");
			intent.putExtra("title", getResources().getString(R.string.mycenter_storeorder_cell2));
			startActivity(intent);
			break;
		case R.id.group_store3:
			intent = new Intent(mActivity, StoreOrderStatusActivity.class);
			intent.putExtra("orderStatus", "6");
			intent.putExtra("flag", "3");
			intent.putExtra("title", getResources().getString(R.string.mycenter_storeorder_cell3));
			startActivity(intent);
			break;
		case R.id.group_store4:
			intent = new Intent(mActivity, StoreOrderStatusActivity.class);
			intent.putExtra("orderStatus", "7,8");
			intent.putExtra("flag", "4");
			intent.putExtra("title", getResources().getString(R.string.mycenter_storeorder_cell4));
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	private void notifyChangeUnreadStatu(){
		if(DefineHandler.getMsgNotifyType(mActivity, DefineHandler.MsgType_Msg) != 0){
			mMainLayout.findViewById(R.id.icon_unread_msg).setVisibility(View.VISIBLE);
		}
		else{
			mMainLayout.findViewById(R.id.icon_unread_msg).setVisibility(View.GONE);
		}
		
		if(DefineHandler.isMsgMyfriendVisible(mActivity)){
			mMainLayout.findViewById(R.id.icon_unread_friend).setVisibility(View.VISIBLE);
		}
		else{
			mMainLayout.findViewById(R.id.icon_unread_friend).setVisibility(View.GONE);
		}
		/*if(DefineHandler.getMsgNotifyType(mActivity, DefineHandler.MsgType_Friend) != 0){
			mMainLayout.findViewById(R.id.icon_unread_friend).setVisibility(View.VISIBLE);
		}
		else{
			mMainLayout.findViewById(R.id.icon_unread_friend).setVisibility(View.GONE);
		}*/
		//mMainLayout.findViewById(R.id.icon_unread_msg).setVisibility(View.VISIBLE);
		//mMainLayout.findViewById(R.id.icon_unread_friend).setVisibility(View.VISIBLE);
		//mMainLayout.findViewById(R.id.icon_unread_integral).setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initLoginStatue();
		if(DataLoader.getInstance().isLogin()){
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserUntreated, null, MyCenterFragment.this);
		}
		else{
			EventManager.getInstance().sendMessage(EventManager.EventType_MsgChange, new Object());
		}
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		switch (type) {
		case TaskOrMethod_UserUntreated:
			initLoginStatue();
			notifyChangeUnreadStatu();
			EventManager.getInstance().sendMessage(EventManager.EventType_MsgChange, new Object());
			break;

		default:
			break;
		}
	}
}
