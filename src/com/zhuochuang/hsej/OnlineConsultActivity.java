package com.zhuochuang.hsej;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.util.TabBarView;
import com.util.TabBarView.OnItemSelectedListeners;
import com.util.TabBarView.TabBarAdapter;

public class OnlineConsultActivity extends BaseActivity {
	
	private TabBarView mTabBar = null;
	
	private Fragment [] mFragments = null;
	private FragmentManager mFragmentManager = null;
	private FragmentTransaction mFragmentTransaction = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setTitleText(R.string.online_consult_title);
		setContentView(R.layout.activity_online_consult);
		initView();
	}
	
	private void initView(){
		
		final String [] mTabBarName = getResources().getStringArray(R.array.online_question_text);
		
		mFragments = new Fragment[2];
		mFragments[0] = new OnlineQuestionFragment();
		mFragments[1] = new OnlineConsultChatFragment();
		
		mTabBar = (TabBarView)findViewById(R.id.online_consult_tabbar);
		
		mTabBar.setAdapter(new TabBarAdapter() {
			
			@Override
			public View getSeletedView(int position, View convertView) {
				if(convertView == null){
					convertView = LayoutInflater.from(OnlineConsultActivity.this).inflate(R.layout.tabbar_cell_myclassmate, null);
				}
				//TabBar Name
				((TextView)convertView.findViewById(R.id.textview)).setText(mTabBarName[position]);
				convertView.findViewById(R.id.line).setVisibility(View.VISIBLE);
				return convertView;
			}
			
			@Override
			public View getNOSeletedView(int position, View convertView) {
				if(convertView == null){
					convertView = LayoutInflater.from(OnlineConsultActivity.this).inflate(R.layout.tabbar_cell_myclassmate, null);
				}
				//TabBar Name
				((TextView)convertView.findViewById(R.id.textview)).setText(mTabBarName[position]);
				convertView.findViewById(R.id.line).setVisibility(View.GONE);
				return convertView;
			}
			
			@Override
			public int getCount() {
				return 2;
			}
		});
		
		mTabBar.setOnItemSelectedListener(new OnItemSelectedListeners() {
			
			@Override
			public void onItemSelected(int position) {
				
				mFragmentManager = getSupportFragmentManager();
				mFragmentTransaction = mFragmentManager.beginTransaction();
				
				mFragmentTransaction.replace(R.id.fragment_online_consult, mFragments[position]);
				
				mFragmentTransaction.addToBackStack(null);
				mFragmentTransaction.commitAllowingStateLoss();
			}
		});
		
		if(getIntent().getIntExtra("resourceType", -1) == 13){
			mTabBar.selectItem(1);
		}else{
			mTabBar.selectItem(0);
		}
		
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
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
			View view = getCurrentFocus();
			if(isShouldHideInput(view, ev)){
				InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				if(view != null && manager != null){
					manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private  boolean isShouldHideInput(View v, MotionEvent event) {  
		
        if (v != null && (v instanceof EditText)) {  
        	View parent = (View)v.getParent();//获取EditText所在父控件
            int[] leftTop = { 0, 0 };  
            //当前的location位置  
            parent.getLocationInWindow(leftTop);  
            int left = leftTop[0];  
            int top = leftTop[1];  
            int bottom = top + parent.getHeight();  
            int right = left + parent.getWidth();  
            if (event.getX() > left && event.getX() < right  
                    && event.getY() > top && event.getY() < bottom) {  
                // 点击的是parent区域，保留点击EditText的事件  
                return false;  
            }
        }  
        return true;  
    }  
	
}
