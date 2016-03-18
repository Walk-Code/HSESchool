package com.zhuochuang.hsej.store;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.TaskType;
import com.util.ContentAdapter;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;

public class StoreCategoryActivity extends BaseActivity{
	private String mCategoryList[];
	private TextView toolsTextViews[];
	private View views[];
	private ScrollView scrollView;
	private int scrllViewWidth = 0, scrollViewMiddle = 0;
	private NoScrollViewPager shop_pager;
	private int currentItem = 0;
	private JSONArray array = null;
	private JSONArray mItemArr;
	private Map<String,Object> map;
	private boolean isScroll = true;	
	private ListView mLeftListView;
	private BaseAdapter mLeftListAdapter;
	private PinnedHeaderListView mPinnedHeaderListView;
	private SectionedAdapter mSectionedAdapter;
	private int mCurrentSelect;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		mMainLayout.removeAllViews();
		setContentView(R.layout.store_category);
		initView();
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_Shop_ListShopType, null, this);
	}	
	
	private void initView() {		
		map = new HashMap<String, Object>();	
		mPinnedHeaderListView=(PinnedHeaderListView) findViewById(R.id.pinnedListView);
		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout header1 = (LinearLayout) inflator.inflate(R.layout.list_item, null);
        ((TextView) header1.findViewById(R.id.textItem)).setText("HEADER 1");
        LinearLayout header2 = (LinearLayout) inflator.inflate(R.layout.list_item, null);
        ((TextView) header2.findViewById(R.id.textItem)).setText("HEADER 2");
        LinearLayout footer = (LinearLayout) inflator.inflate(R.layout.list_item, null);
        ((TextView) footer.findViewById(R.id.textItem)).setText("FOOTER");		
		mLeftListView = (ListView) findViewById(R.id.left_listview);
		mLeftListView.setAdapter(mLeftListAdapter = new ContentAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = ViewGroup.inflate(StoreCategoryActivity.this, R.layout.listcell_store_category_root, null);
				}
				TextView textName = (TextView)convertView.findViewById(R.id.text_name);
				textName.setText(mCategoryList[position]);
				
				if(position == mCurrentSelect){
					convertView.setBackgroundColor(Color.WHITE);
					textName.setTextColor(Color.RED);
					convertView.findViewById(R.id.line).setVisibility(View.INVISIBLE);
				}else {
					convertView.setBackgroundColor(Color.parseColor("#F1EFF0"));
					textName.setTextColor(Color.BLACK);
					convertView.findViewById(R.id.line).setVisibility(View.VISIBLE);
				}
				
				return convertView;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mCategoryList == null ? 0 : mCategoryList.length;
			}
		});
		
		mLeftListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
				mCurrentSelect = position;
				mLeftListAdapter.notifyDataSetChanged();
				
				isScroll = false;
				/*for (int i = 0; i < mLeftListView.getChildCount(); i++){
					if (i == position){
						mLeftListView.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
						((TextView)mLeftListView.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.RED);
					}else{
						mLeftListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
						((TextView)mLeftListView.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.BLACK);
					}
				}*/

				int rightSection = 0;
				for(int i = 0;i < position;i ++){
					rightSection += mSectionedAdapter.getCountForSection(i)+1;
				}
				mPinnedHeaderListView.setSelection(rightSection);				
			}
		});
		
		//((EditText)findViewById(R.id.search_result)).setCursorVisible(false);
		(findViewById(R.id.search_result)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StoreCategoryActivity.this, StoreSearchActivity.class);
				intent.putExtra("isShop", true);
				startActivity(intent);				
			}
		});
		
		/*((EditText)findViewById(R.id.search_result)).setOnEditorActionListener(new OnEditorActionListener() {
					
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if(actionId ==EditorInfo.IME_ACTION_SEARCH){							
							//跳转到商品列表界面 
							 Intent intent = new Intent(StoreCategoryActivity.this,StoreSearchStoreListActivity.class);
							 intent.putExtra("SearchResult",((EditText)findViewById(R.id.search_result)).getText().toString());
							 startActivity(intent);							
							 return true;
						}
						return false;
					}
				});
		
		*/ findViewById(R.id.store_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});		
	}
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(StoreCategoryActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		if(type == TaskType.TaskOrMethod_Shop_ListShopType){
			if(result instanceof JSONObject){
				if(((JSONObject)result).has("items")){
					mItemArr = ((JSONObject)result).optJSONArray("items");
					if(0 < mItemArr.length()){
						findViewById(R.id.centent_layout).setVisibility(View.VISIBLE);
						mCategoryList  = new String[mItemArr.length()];
						for(int i = 0;i < mItemArr.length();i++){
							JSONObject object = mItemArr.optJSONObject(i);
							mCategoryList[i] = object.optString("name");							
							JSONArray shopList = object.optJSONArray("shopList");
							map.put(mCategoryList[i], shopList);							
						}				
						mSectionedAdapter = new SectionedAdapter(this, mCategoryList, map);
						mPinnedHeaderListView.setAdapter(mSectionedAdapter);
						//mLeftListView.setAdapter(new ArrayAdapter<String>(this,R.layout.store_category_list_item, mCategoryList));
						mLeftListAdapter.notifyDataSetChanged();
					}else{
						
					}
				}
			 }
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((HSESchoolApp)getApplication()).removeActivityStore(this);
	}
}
