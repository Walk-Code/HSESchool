package com.zhuochuang.hsej.store;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView.OnEditorActionListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;

public class StoreSearchStoreListActivity extends BaseActivity{
	private ListView mListView;
	private ContentAdapter mShopAdapter;
	private ImageView [] evaluateStar;
	private ImageView [] GoodsPhoto;
	private JSONArray mShopArr;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		mMainLayout.removeAllViews();
		setContentView(R.layout.store_search_shop);
		showDialogCustom(DIALOG_CUSTOM);
		((EditText)findViewById(R.id.search_result)).setText(getIntent().getStringExtra("SearchResult"));
		initView();
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_ShopListShop,
				DataLoader.getInstance().setShopListTypeParams(getIntent().getStringExtra("SearchResult")),StoreSearchStoreListActivity.this);
	}
	private void initView() {
		GoodsPhoto = new ImageView[3];
		evaluateStar = new ImageView[5];
		mListView = (ListView) findViewById(R.id.search_store_list);
		mListView.setAdapter(mShopAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				return null != mShopArr ? mShopArr.length() : 0;
			}

			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				if(null == convertView){
					convertView = LayoutInflater.from(StoreSearchStoreListActivity.this).inflate(R.layout.store_search_shoplist_item, null);						
				}
				JSONObject obj = mShopArr.optJSONObject(position);
				int length = obj.optInt("evaluate");
				LinearLayout.LayoutParams layoutParamsEvaluate = new LinearLayout.LayoutParams(Utils.dipPx(StoreSearchStoreListActivity.this, 18),Utils.dipPx(StoreSearchStoreListActivity.this, 18),1);
				LinearLayout mCommentLayout = (LinearLayout)convertView.findViewById(R.id.search_evaluate_layout);
				mCommentLayout.removeAllViews();
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT,1);
				((TextView)convertView.findViewById(R.id.search_store_name)).setText(obj.optString("name"));
				((TextView)convertView.findViewById(R.id.store_detail)).setText(obj.optString("intro"));
				((TextView)convertView.findViewById(R.id.store_count)).setText(
						String.format(getString(R.string.stores_pro_total), obj.optString("goodsSum")));
				
				ImageLoader.getInstance().displayImage(obj.optString("logo"), (ImageView)convertView.findViewById(R.id.store_thumbnail),ImageLoaderConfigs.displayImageOptionsBg);
				JSONArray mPhoto  = obj.optJSONArray("goodsList");
				//图片	
				Resources mRes=getResources();
				for(int count = 0;count < mPhoto.length() && count < 3;count ++){
					JSONObject objs = mPhoto.optJSONObject(count);
					int id=mRes.getIdentifier("item_image_" + count, "id", getPackageName());
					ImageView imageView=(ImageView) convertView.findViewById(id);
					ImageLoader.getInstance().displayImage(objs.optString("image"), imageView);
				}				
			//评论
				for(int i = 0;i < 5; i++){
					 evaluateStar[i] = new ImageView(StoreSearchStoreListActivity.this);
					 if(i > length-1){
						 evaluateStar[i].setImageResource(R.drawable.icon_grade_nor); 
					 }else{
					     evaluateStar[i].setImageResource(R.drawable.icon_grade_pre);
					 }
					 evaluateStar[i].setPadding(1, 5, 5,1);
					 mCommentLayout.addView(evaluateStar[i],layoutParamsEvaluate); 
				 }
				
				return convertView;
			}
		 
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {		
				Intent intent = new Intent(StoreSearchStoreListActivity.this, StorePersonActivity.class);
				intent.putExtra("shopId",mShopArr.optJSONObject(position).optString("id"));
				startActivity(intent);
			}			
		});
		
		findViewById(R.id.store_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		((EditText)findViewById(R.id.search_result)).setOnEditorActionListener(new OnEditorActionListener() {
					
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if(actionId ==EditorInfo.IME_ACTION_SEARCH){					
							DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_ShopListShop,
									DataLoader.getInstance().setShopListTypeParams(((EditText)findViewById(R.id.search_result)).getText().toString()),StoreSearchStoreListActivity.this);
							 return true;
						}
						return false;
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
			Toast.makeText(StoreSearchStoreListActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_ShopListShop:
			if(((JSONObject)result).has("items")){
				mShopArr = ((JSONObject)result).optJSONArray("items"); 				
				if(null != mShopArr){
					removeDialogCustom();
					mShopAdapter.notifyDataSetChanged();
				}			
				
				if(null == mShopArr || mShopArr.length() == 0){
					findViewById(R.id.noData).setVisibility(View.VISIBLE);
					mListView.setVisibility(View.INVISIBLE);
				}else {
					findViewById(R.id.noData).setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
				}
			}
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((HSESchoolApp)getApplication()).removeActivityStore(this);
	}
}
