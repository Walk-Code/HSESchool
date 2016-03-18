package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.TaskType;
import com.util.TabBarView;
import com.util.TabBarView.OnItemSelectedListeners;
import com.util.TabBarView.TabBarAdapter;

public class InformationActivity extends BaseActivity{
	
	private TabBarView mTabView = null;
	
	private Fragment [] mFragments;
	private FragmentManager mFragmentManager = null;
	private FragmentTransaction mFragmentTransaction = null;
	
	private JSONArray mDateArray = null; 
	
	private int mCurrentTab = -1; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);
		
		setTitleText(getIntent().getStringExtra("name"));
		
		switch (getIntent().getIntExtra("type", -1)) {
		case 3:
			showDialogCustom(DIALOG_CUSTOM);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("parentId", getIntent().getStringExtra("typeId"));
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_InformationListTypes, params, this);
			break;
			
		case 4:
			initView ();
			((TabBarView)findViewById(R.id.tabbar_beauty_hs)).setVisibility(View.GONE);
		break;

		default:
			break;
		}
	}
	
	
	private void initTabBarView(){
		
		mTabView = (TabBarView)findViewById(R.id.tabbar_beauty_hs);
		mTabView.setAdapter(new TabBarAdapter() {
			
			@Override
			public View getSeletedView(int position, View convertView) {
				if(convertView == null){
					convertView = View.inflate(InformationActivity.this, R.layout.tabbar_cell_myclassmate, null);
				}
				((TextView)convertView.findViewById(R.id.textview)).setText(mDateArray.optJSONObject(position).optString("name"));
				convertView.findViewById(R.id.line).setVisibility(View.VISIBLE);
				return convertView;
			}
			
			@Override
			public View getNOSeletedView(int position, View convertView) {
				if(convertView == null){
					convertView = View.inflate(InformationActivity.this, R.layout.tabbar_cell_myclassmate, null);
				}
				((TextView)convertView.findViewById(R.id.textview)).setText(mDateArray.optJSONObject(position).optString("name"));
				convertView.findViewById(R.id.line).setVisibility(View.GONE);
				return convertView;
			}
			
			@Override
			public int getCount() {
				if(mDateArray != null && mDateArray.length() > 0){
					return mDateArray.length();
				}
				return 0;
			}
		});
		
		mFragmentManager = getSupportFragmentManager();
		mTabView.setOnItemSelectedListener(new OnItemSelectedListeners() {
			
			@Override
			public void onItemSelected(int position) {
				try {
					if(mCurrentTab != position){
						mCurrentTab = position;
						if(mFragments[position] == null){
							mFragments[position] = new InformationFragment();
							((InformationFragment)mFragments[position]).setTypeId(mDateArray.optJSONObject(position).optString("id"));
						}
						else{
							((InformationFragment)mFragments[position]).setTypeId(mDateArray.optJSONObject(position).optString("id"));
						}
					}
					mFragmentTransaction = mFragmentManager.beginTransaction();
					mFragmentTransaction.replace(R.id.fragmet_group_beauty_hs, mFragments[position]);
					mFragmentTransaction.addToBackStack(null);
					mFragmentTransaction.commitAllowingStateLoss();
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		
		
		if(mDateArray != null && mDateArray.length() >0){
			if(getIntent().hasExtra("id")){
				for(int i = 0 ; i<mDateArray.length(); i++){
					JSONObject obj = mDateArray.optJSONObject(i);
					if(obj != null && obj.has("id") && getIntent().getStringExtra("id").equalsIgnoreCase(obj.optString("id"))){
						mTabView.selectItem(i);
						return;
					}
				}
			}
			mTabView.selectItem(0);
		}
		
	}
	
	private void initView (){
		Fragment fragment = new InformationFragment();
		((InformationFragment)fragment).setTypeId(getIntent().getStringExtra("typeId"));
		
		mFragmentManager = getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(R.id.fragmet_group_beauty_hs, fragment);
		mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commitAllowingStateLoss();
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
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		
		removeDialogCustom();
		
		if(result == null){
			//如果为空，则隐藏tabbar
			((TabBarView)findViewById(R.id.tabbar_beauty_hs)).setVisibility(View.GONE);
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(InformationActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			((TabBarView)findViewById(R.id.tabbar_beauty_hs)).setVisibility(View.GONE);//如果为空，则隐藏tabbar
			return;
		}
		
		switch (type) {
		case TaskOrMethod_InformationListTypes:
			if(result instanceof JSONObject){
				if(((JSONObject)result).has("parentName")){
					setTitleText(((JSONObject)result).optString("parentName"));
				}
				
				if(((JSONObject)result).has("items")){
					mDateArray = ((JSONObject)result).optJSONArray("items");
					mFragments = new Fragment[mDateArray.length()];
					initTabBarView();
				}
			}
			break;
		default:
			break;
		}
		
	}
	
}