package com.zhuochuang.hsej;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.util.TabBarView;
import com.util.TabBarView.OnItemSelectedListeners;
import com.util.TabBarView.TabBarAdapter;

@SuppressLint("InflateParams")
public class MyClassmateFragment extends BaseFragment{

	private TabBarView mTabBarView;
	private String[] mTabNames;
	private Fragment [] mFragments;
	private FragmentManager mFragmentManager = null;
	private FragmentTransaction mFragmentTransaction = null;
	
	private int mCurrentTab = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mMainLayout = (ViewGroup) LayoutInflater.from(mActivity).inflate(R.layout.fragment_myclassmate, null);
		mTabNames = getResources().getStringArray(R.array.channel_classmate);
		
		mFragmentManager = getChildFragmentManager();
		mFragments = new Fragment[mTabNames.length];
		mFragments[0] = new MyClassmateHobbyGroupFragment();
		mFragments[1] = new MyClassmateClubFragment();
		initTabBarView();
	}
	
	private void initTabBarView(){
		mTabBarView = (TabBarView) mMainLayout.findViewById(R.id.layout_tabbar);
		mTabBarView.setAdapter(new TabBarAdapter() {
			@Override
			public View getSeletedView(int position, View convertView) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = View.inflate(mActivity, R.layout.tabbar_cell_myclassmate, null);
				}
				((TextView)convertView.findViewById(R.id.textview)).setText(mTabNames[position]);
				convertView.findViewById(R.id.line).setVisibility(View.VISIBLE);
				return convertView;
			}
			
			@Override
			public View getNOSeletedView(int position, View convertView) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = View.inflate(mActivity, R.layout.tabbar_cell_myclassmate, null);
				}
				((TextView)convertView.findViewById(R.id.textview)).setText(mTabNames[position]);
				convertView.findViewById(R.id.line).setVisibility(View.GONE);
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
					mFragmentTransaction = mFragmentManager.beginTransaction();
					mFragmentTransaction.replace(R.id.layout_group, mFragments[position]);
					mFragmentTransaction.addToBackStack(null);
					mFragmentTransaction.commitAllowingStateLoss();
				}
			}
		});
		
		mTabBarView.selectItem(0);
	}
}
