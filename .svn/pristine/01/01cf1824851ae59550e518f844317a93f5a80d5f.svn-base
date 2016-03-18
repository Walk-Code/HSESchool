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
import android.widget.Toast;

import com.model.DataLoader;
import com.model.TaskType;
import com.util.TabBarView;
import com.util.TabBarView.OnItemSelectedListeners;
import com.util.TabBarView.TabBarAdapter;

public class SchoolyardServiceMoreActivity extends BaseActivity{

	private TabBarView mTabBarView;
	private String[] mTabNames;
	private Fragment [] mFragments;
	private FragmentManager mFragmentManager = null;
	private FragmentTransaction mFragmentTransaction = null;
	
	private JSONArray mTabArr;
	private int mCurrentTab = -1;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_schoolyardservice_more);
		setTitleText(R.string.service_more);
		
		showDialogCustom(DIALOG_CUSTOM);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_CampusListTypes, null, SchoolyardServiceMoreActivity.this);
	}
	
	private void initTabBarView(){
		mTabBarView = (TabBarView) mMainLayout.findViewById(R.id.layout_tabbar);
		mTabBarView.setAdapter(new TabBarAdapter() {
			@Override
			public View getSeletedView(int position, View convertView) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = View.inflate(SchoolyardServiceMoreActivity.this, R.layout.tabbar_cell_myclassmate, null);
				}
				convertView.findViewById(R.id.line).setVisibility(View.VISIBLE);
				if(position == 0){
					((TextView)convertView.findViewById(R.id.textview)).setText(getResources().getString(R.string.all));
				}
				else{
					JSONObject obj = getItems(position);
					if(obj != null){
						((TextView)convertView.findViewById(R.id.textview)).setText(obj.optString("name"));
					}
				}
				((TextView)convertView.findViewById(R.id.textview)).setTextColor(Color.argb(255, 222, 79, 81));
				return convertView;
			}
			
			@Override
			public View getNOSeletedView(int position, View convertView) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = View.inflate(SchoolyardServiceMoreActivity.this, R.layout.tabbar_cell_myclassmate, null);
				}
				convertView.findViewById(R.id.line).setVisibility(View.GONE);
				if(position == 0){
					((TextView)convertView.findViewById(R.id.textview)).setText(getResources().getString(R.string.all));
				}
				else{
					JSONObject obj = getItems(position);
					if(obj != null){
						((TextView)convertView.findViewById(R.id.textview)).setText(obj.optString("name"));
					}
				}
				((TextView)convertView.findViewById(R.id.textview)).setTextColor(Color.argb(255, 127, 127, 127));
				return convertView;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(mTabArr != null && mTabArr.length() > 0){
					return mTabArr.length() + 1;
				}
				return 1;
			}
		});
		
		mTabBarView.setOnItemSelectedListener(new OnItemSelectedListeners() {
			@Override
			public void onItemSelected(int position) {
				// TODO Auto-generated method stub
				if(mCurrentTab != position){
					mCurrentTab = position;
					if(mFragments[mCurrentTab] == null){
						mFragments[mCurrentTab] = new SchoolyardServiceMoreFragment();
						if(mCurrentTab == 0){
							((SchoolyardServiceMoreFragment)mFragments[mCurrentTab]).setTypeId("");
						}
						else{
							JSONObject obj = getItems(position);
							if(obj != null){
								((SchoolyardServiceMoreFragment)mFragments[mCurrentTab]).setTypeId(obj.optString("id"));
							}
						}
					}
					try {
						mFragmentTransaction = mFragmentManager.beginTransaction();
						mFragmentTransaction.replace(R.id.layout_group, mFragments[position]);
						mFragmentTransaction.addToBackStack(null);
						mFragmentTransaction.commitAllowingStateLoss();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		});
		
		mTabBarView.selectItem(0);
	}
	
	private JSONObject getItems(int position){
		if(mTabArr != null && mTabArr.length() > 0){
			return mTabArr.optJSONObject(position - 1);
		}
		return null;
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
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		removeDialogCustom();
		
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(SchoolyardServiceMoreActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_CampusListTypes:
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				mTabArr = ((JSONObject)result).optJSONArray("items");
			}
			if(mTabArr != null && mTabArr.length() > 0){
				mFragmentManager = getSupportFragmentManager();
				mFragments = new Fragment[mTabArr.length() + 1];
				initTabBarView();
			}
			break;

		default:
			break;
		}
	}
}
