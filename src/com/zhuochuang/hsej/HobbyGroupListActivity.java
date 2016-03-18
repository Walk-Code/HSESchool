package com.zhuochuang.hsej;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.util.TabBarView;
import com.util.TabBarView.OnItemSelectedListeners;
import com.util.TabBarView.TabBarAdapter;

public class HobbyGroupListActivity extends BaseActivity{

	private TabBarView mTabBarView;
	private String[] mTabNames;
	private Fragment [] mFragments;
	private FragmentManager mFragmentManager = null;
	private FragmentTransaction mFragmentTransaction = null;
	
	private JSONArray mTabArr;
	private int mCurrentTab = -1;
	
	private JSONObject mGetDataObj;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_hobbygrouplist);
		mTabNames = getResources().getStringArray(R.array.hobbygroup_listtab);
		//mMainLayout.findViewById(R.id.ico_share).setVisibility(View.VISIBLE);
		
		mFragmentManager = getSupportFragmentManager();
		mFragments = new Fragment[mTabNames.length];
		
		try {
			mGetDataObj = new JSONObject(getIntent().getStringExtra("data"));
			((TextView)mMainLayout.findViewById(R.id.textview_title)).setText(mGetDataObj.optString("name"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initTabBarView();
	}
	
	public void onShareClick(View view){
		
	}
	
	private void initTabBarView(){
		mTabBarView = (TabBarView) mMainLayout.findViewById(R.id.layout_tabbar);
		mTabBarView.setAdapter(new TabBarAdapter() {
			@Override
			public View getSeletedView(int position, View convertView) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = View.inflate(HobbyGroupListActivity.this, R.layout.tabbar_cell_myclassmate, null);
				}
				convertView.findViewById(R.id.line).setVisibility(View.VISIBLE);
				((TextView)convertView.findViewById(R.id.textview)).setText(mTabNames[position]);
				((TextView)convertView.findViewById(R.id.textview)).setTextColor(Color.argb(255, 222, 79, 81));
				return convertView;
			}
			
			@Override
			public View getNOSeletedView(int position, View convertView) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = View.inflate(HobbyGroupListActivity.this, R.layout.tabbar_cell_myclassmate, null);
				}
				convertView.findViewById(R.id.line).setVisibility(View.GONE);
				((TextView)convertView.findViewById(R.id.textview)).setText(mTabNames[position]);
				((TextView)convertView.findViewById(R.id.textview)).setTextColor(Color.argb(255, 127, 127, 127));
				return convertView;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mTabNames.length;
			}
		});
		
		mTabBarView.setOnItemSelectedListener(new OnItemSelectedListeners() {
			@Override
			public void onItemSelected(int position) {
				// TODO Auto-generated method stub
				if(mCurrentTab != position){
					mCurrentTab = position;
					if(mFragments[mCurrentTab] == null){
						mFragments[mCurrentTab] = new HobbyGroupListFragment();
						((HobbyGroupListFragment)mFragments[mCurrentTab]).setTabType(mCurrentTab);
						//((HobbyGroupListFragment)mFragments[mCurrentTab]).setIsAllList(mCurrentTab == 0? true : false);
						((HobbyGroupListFragment)mFragments[mCurrentTab]).setGetDataObj(mGetDataObj);
					}
					mFragmentTransaction = mFragmentManager.beginTransaction();
					mFragmentTransaction.replace(R.id.layout_group, mFragments[position]);
					mFragmentTransaction.addToBackStack(null);
					mFragmentTransaction.commitAllowingStateLoss();
				}
			}
		});
		
		mTabBarView.selectItem(0);
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
}
