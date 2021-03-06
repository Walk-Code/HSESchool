package com.zhuochuang.hsej.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.util.ContentAdapter;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.R;

public class StoreSearchActivity extends BaseActivity{
	private ListView mListView;
	ArrayAdapter mAdapter;
	ContentAdapter mShopAdapter;
	private PopupWindow popupWindow;
	View mHeaderView,mFooterView;
	private String [] mItem;
	private EditText mSearchEdit;
	private List<String> mHistoryList;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mMainLayout.removeAllViews();
		setContentView(R.layout.store_search_activity);
		initView();		
	}
	
	private void initView() {	
		mHistoryList = new ArrayList<String>();
		mSearchEdit = (EditText)findViewById(R.id.store_search_edittext);
		mListView = (ListView) findViewById(R.id.search_listview);
		mHeaderView = LayoutInflater.from(StoreSearchActivity.this).inflate(R.layout.store_search_header_item, null);
		mFooterView = LayoutInflater.from(StoreSearchActivity.this).inflate(R.layout.store_search_footer_item, null);
		mItem = getResources().getStringArray(R.array.store_search);			
		showHeaderFooderView();
		showHeaderFooter();
		
		//红色
		if(getIntent().getBooleanExtra("isShop", false)){
			findViewById(R.id.store_search_title).setBackgroundColor(Color.WHITE);
			((TextView)findViewById(R.id.search_product)).setText(mItem[1]);
			((TextView)findViewById(R.id.search_product)).setCompoundDrawables(null, null, null, null);
			findViewById(R.id.store_search_title).setBackgroundColor(Color.parseColor("#DD514F"));
		}
		
		//白色
		if(getIntent().getBooleanExtra("isProduct", false)){
			findViewById(R.id.store_search_title).setBackgroundColor(Color.WHITE);
			((TextView)findViewById(R.id.search_product)).setCompoundDrawables(null, null, null, null);
			findViewById(R.id.search_layout).setBackgroundResource(R.drawable.shape_store_personal_bg);
			((TextView)findViewById(R.id.store_cancle)).setTextColor(Color.parseColor("#9e9e9e"));
			((TextView)findViewById(R.id.search_product)).setText(mItem[0]);
		}
		
		if(!getIntent().getBooleanExtra("isProduct", false) && !getIntent().getBooleanExtra("isShop", false)){
			((TextView)findViewById(R.id.search_product)).setText(mItem[0]);
		}
		
		mListView.setAdapter(mAdapter = new ArrayAdapter<String>(StoreSearchActivity.this, 
				R.layout.store_search_item,R.id.store_search_item,chageListData()));
		
		/*((EditText)findViewById(R.id.store_search_edittext)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {						
					StoreSearchActivity.this.mAdapter.getFilter().filter(s);
					if(((EditText)findViewById(R.id.store_search_edittext)).getText().toString().length() > 0){	
						for(int i = 0;i < chageListData().size();i++){	
							if(chageListData().get(i).equals(s+"")){								
								findViewById(R.id.head_item).setVisibility(View.VISIBLE);
								findViewById(R.id.foot_item).setVisibility(View.VISIBLE);
								break;
							}else{	
								if(null != findViewById(R.id.head_item) && null != findViewById(R.id.foot_item)){
									findViewById(R.id.head_item).setVisibility(View.GONE);
									findViewById(R.id.foot_item).setVisibility(View.GONE);
								}
							}
						}	
					}else{
						if(null != findViewById(R.id.head_item) && null != findViewById(R.id.foot_item)){
							findViewById(R.id.head_item).setVisibility(View.VISIBLE);
							findViewById(R.id.foot_item).setVisibility(View.VISIBLE);
						}
					}
					mAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {								
			}
			
			@Override
			public void afterTextChanged(Editable s) {				
			}
		});*/
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intent;
				if(id == -1){
					return;
				}
				if(((TextView)findViewById(R.id.search_product)).getText().toString().equals(mItem[0])){
					intent = new Intent(StoreSearchActivity.this,StoreDetailActivity.class);
					if(getIntent().hasExtra("shopId")){
						intent.putExtra("shopId", getIntent().getStringExtra("shopId"));
					}
				}else{
					intent = new Intent(StoreSearchActivity.this,StoreSearchStoreListActivity.class);
				}
				if(0 < chageListData().size()){
				 intent.putExtra("SearchResult",chageListData().get(position-1));
				 startActivity(intent);
				}
			}
		});
		
		@SuppressWarnings("static-access")
		LayoutInflater inflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
		View contentview  = inflater.inflate(R.layout.store_popupwindow, null);
	    popupWindow = new PopupWindow(contentview,Utils.getScreenWidth(getApplicationContext())/4,ViewGroup.LayoutParams.WRAP_CONTENT);
	    popupWindow.setFocusable(true);  
	    popupWindow.setBackgroundDrawable(new BitmapDrawable());   
	    popupWindow.setOutsideTouchable(true);
		ListView listView = (ListView) contentview.findViewById(R.id.store_popupwindow);
		listView.setAdapter(new ArrayAdapter<String>(StoreSearchActivity.this,R.layout.store_product_popupwindow_item,R.id.store_popupwindowItem,getData()));
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				popupWindow.dismiss();
				((TextView)findViewById(R.id.search_product)).setText(getData().get(position).toString());
				/*if(0 < chageListData().size()){
					mListView.addHeaderView(mHeaderView);
					mListView.addFooterView(mFooterView);
				}else{
					mListView.removeHeaderView(mHeaderView);
					mListView.removeFooterView(mFooterView);
				}*/
				
				mHistoryList = chageListData();
				mListView.setAdapter(mAdapter = new ArrayAdapter<String>(StoreSearchActivity.this,R.layout.store_search_item,R.id.store_search_item,mHistoryList));
				showHeaderFooter();
			}
		});
		
		findViewById(R.id.search_product).setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(!getIntent().getBooleanExtra("isProduct", false) && !getIntent().getBooleanExtra("isShop", false)){
					popupWindow.showAsDropDown(v,0,0);				
				}
			}
		});
		//搜索
		mSearchEdit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId ==EditorInfo.IME_ACTION_SEARCH){					
					Intent intent = null;
					if(((TextView)findViewById(R.id.search_product)).getText().toString().equals(mItem[0])){
						saveList(StoreSearchActivity.this,"ProductSearch","ProductSearch");
						intent = new Intent(StoreSearchActivity.this,StoreDetailActivity.class);	
						if(getIntent().hasExtra("shopId")){
							intent.putExtra("shopId", getIntent().getStringExtra("shopId"));
						}
					}else{
						saveList(StoreSearchActivity.this,"StoreSearch","StoreSearch");
						intent = new Intent(StoreSearchActivity.this,StoreSearchStoreListActivity.class);						
					}
					 intent.putExtra("SearchResult",mSearchEdit.getText().toString());
					 startActivity(intent);					
					
					 return true;
				}
				return false;
			}
		});
		
		findViewById(R.id.store_cancle).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private List<String> chageListData(){
		mHistoryList.clear();
		if(((TextView)findViewById(R.id.search_product)).getText().toString().equals(mItem[0])){
			for(String str : Arrays.asList(loadList(this,"ProductSearch","ProductSearch"))){
				mHistoryList.add(str);
				if(str.equals("")) mHistoryList.remove("");
			}
			//showHeaderFooter();
			return mHistoryList;
		}else{
			for(String str : Arrays.asList(loadList(this,"StoreSearch","StoreSearch"))){
				mHistoryList.add(str);
				if(str.equals("")) mHistoryList.remove("");
			}
			//showHeaderFooter();
			return mHistoryList;			
		}
		
	}
	
	public void showHeaderFooderView(){
		mListView.addHeaderView(mHeaderView,null,false);//添加头部
		mListView.addFooterView(mFooterView,null,true);//添加脚部							
		if(chageListData().size() > 0){			
			mListView.setAdapter(mAdapter = new ArrayAdapter<String>(StoreSearchActivity.this,R.layout.store_search_item,R.id.store_search_item,chageListData()));		
		}
		findViewById(R.id.store_search_footer_item).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				chageListData().clear();
				if(((TextView)findViewById(R.id.search_product)).getText().toString().equals(mItem[0])){					
					clearList(StoreSearchActivity.this,"ProductSearch","ProductSearch");
				}else{
					clearList(StoreSearchActivity.this,"StoreSearch","StoreSearch");
				}
				mAdapter.notifyDataSetChanged();
				mHeaderView.setVisibility(View.GONE);
				mFooterView.setVisibility(View.GONE);
			}
		});
	}
	
	private void showHeaderFooter(){
		if(0 < mHistoryList.size()){			
			mHeaderView.setVisibility(View.VISIBLE);
			mFooterView.setVisibility(View.VISIBLE);
		}else{
			mHeaderView.setVisibility(View.GONE);
			mFooterView.setVisibility(View.GONE);
		}
	}
	
	public List getData(){
		return Arrays.asList(getResources().getStringArray(R.array.store_search));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(null != mAdapter){
			mAdapter.notifyDataSetChanged();
		}
	}
	//保存输入
	public  void saveList(Context content,String key,String fileName) {
		String searchValue = mSearchEdit.getText().toString();	    
	    SharedPreferences preferences = content.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	    String saveString = preferences.getString(key, "");
	    String[] hisArrays = saveString.split(","); 
        for(int i = 0;i < hisArrays.length;i++) { 
            if(hisArrays[i].equals(searchValue)){ 
                return; 
            } 
        } 
        StringBuilder sb = new StringBuilder(saveString); 
        sb.append(searchValue + ","); 
        preferences.edit().putString(key, sb.toString()).commit(); 
        //Toast.makeText(StoreSearchActivity.this, sb.toString(), Toast.LENGTH_LONG).show(); 	    
	 }

	//获取 
	public String[] loadList(Context content,String key,String fileName) {
	    SharedPreferences preferences = content.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	    String strJson = preferences.getString(key, ""); 
		String save_history = preferences.getString(key, ""); 			 
		return save_history.split(","); 	    
	}
	public void clearList(Context content,String key,String fileName){
		SharedPreferences preferences = content.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		preferences.edit().clear().commit();
	}
	
}
