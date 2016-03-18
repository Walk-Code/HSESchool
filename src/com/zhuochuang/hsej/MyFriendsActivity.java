package com.zhuochuang.hsej;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.model.DefineHandler;
import com.model.EventManager;
import com.util.TabBarView;
import com.util.TabBarView.OnItemSelectedListeners;
import com.util.TabBarView.TabBarAdapter;
/**
 * 我的好友（我的聊天，通讯录）
 * @author Dion
 *
 */
@SuppressLint("HandlerLeak")
public class MyFriendsActivity extends BaseActivity{
	//顶部bar
	private TabBarView mTabBarView = null;
	private String [] mTabBarName = null;

	private Fragment [] mFragments = null;
	private FragmentManager mFragmentManager = null;
	private FragmentTransaction mFragmentTransaction = null;
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_myfriends);
		
		findViewById(R.id.ico_addfriend).setVisibility(View.VISIBLE);
		
		setTitleText(R.string.my_friends_title);//标题,我的好友
		
		mTabBarName = getResources().getStringArray(R.array.tabbar_name_my_firends);
		
		mFragments = new Fragment[mTabBarName.length];
		mFragments[0] = new MyFriendsChatFragment();
		mFragments[1] = new MyFriendsAddressBookFragment();
		initTabBar();
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case EventManager.EventType_MsgMyfriendChange:
				case EventManager.EventType_MsgChange:
					if(mTabBarView != null){
						mTabBarView.notifyChange();
					}
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void initTabBar(){
		mTabBarView = (TabBarView)mMainLayout.findViewById(R.id.tabBar_myfriends);
		
		mTabBarView.setAdapter(new TabBarAdapter() {
			
			@Override
			public View getSeletedView(int position, View convertView) {
				if(convertView == null){
					convertView = LayoutInflater.from(MyFriendsActivity.this).inflate(R.layout.tabbar_cell_myclassmate, null);
				}
				//TabBar Name
				((TextView)convertView.findViewById(R.id.textview)).setText(mTabBarName[position]);
				convertView.findViewById(R.id.line).setVisibility(View.VISIBLE);
				
				switch (position) {
				case 0:
					if(DefineHandler.getMsgNotifyType(MyFriendsActivity.this, DefineHandler.MsgType_FriendChat) != 0){
						convertView.findViewById(R.id.view_redpoint).setVisibility(View.VISIBLE);
					}
					else{
						convertView.findViewById(R.id.view_redpoint).setVisibility(View.GONE);
					}
					break;
				case 1:
					if(DefineHandler.getMsgNotifyType(MyFriendsActivity.this, DefineHandler.MsgType_FriendContact) != 0){
						convertView.findViewById(R.id.view_redpoint).setVisibility(View.VISIBLE);
					}
					else{
						convertView.findViewById(R.id.view_redpoint).setVisibility(View.GONE);
					}
					break;
				default:
					break;
				}
				return convertView;
			}
			
			@Override
			public View getNOSeletedView(int position, View convertView) {
				if(convertView == null){
					convertView = LayoutInflater.from(MyFriendsActivity.this).inflate(R.layout.tabbar_cell_myclassmate, null);
				}
				//TabBar Name
				((TextView)convertView.findViewById(R.id.textview)).setText(mTabBarName[position]);
				convertView.findViewById(R.id.line).setVisibility(View.GONE);
				switch (position) {
				case 0:
					if(DefineHandler.getMsgNotifyType(MyFriendsActivity.this, DefineHandler.MsgType_FriendChat) != 0){
						convertView.findViewById(R.id.view_redpoint).setVisibility(View.VISIBLE);
					}
					else{
						convertView.findViewById(R.id.view_redpoint).setVisibility(View.GONE);
					}
					break;
				case 1:
					if(DefineHandler.getMsgNotifyType(MyFriendsActivity.this, DefineHandler.MsgType_FriendContact) != 0){
						convertView.findViewById(R.id.view_redpoint).setVisibility(View.VISIBLE);
					}
					else{
						convertView.findViewById(R.id.view_redpoint).setVisibility(View.GONE);
					}
					break;
				default:
					break;
				}
				return convertView;
			}
			
			@Override
			public int getCount() {
				return mTabBarName.length;
			}
		});
		
		mTabBarView.setOnItemSelectedListener(new OnItemSelectedListeners() {
			
			@Override
			public void onItemSelected(int position) {
				mFragmentManager = getSupportFragmentManager();
				mFragmentTransaction = mFragmentManager.beginTransaction();
				
				mFragmentTransaction.replace(R.id.fragment_group_myfriends, mFragments[position]);
				mFragmentTransaction.addToBackStack(null);
				mFragmentTransaction.commitAllowingStateLoss();
			}
		});
		mTabBarView.selectItem(0);
	}
	
	public void onAddfriendClick(View view){
		startActivity(new Intent(this, FriendAddActivity.class));
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
        	finish();
            return true;
        }
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventManager.getInstance().removeHandlerListenner(mHandler);
	}
}
