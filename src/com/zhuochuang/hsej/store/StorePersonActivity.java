package com.zhuochuang.hsej.store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.ListViewForScrollView;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.AutoGallery;
import com.util.ContentAdapter;
import com.util.PageGuide;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.LoginActivity;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.WebViewActivity;

public class StorePersonActivity extends BaseActivity{
	private ListViewForScrollView  mListView;
	private LinearLayout mLinearLayout;
	private AutoGallery mAutoGallery;
	private PageGuide mPageGuide;
	private ContentAdapter mContentAdapter;
	private JSONArray mDynameArr;
	private JSONArray mFocusArr;
	private JSONArray mGoodsArr;
	private JSONObject mStoreDetail;
	private PopupWindow mPopupWindow;
	private LinearLayout popupLayout;
	private String [] items;
	private String mGoodsType;
	private static final int EVALUATESTAR = 5;
	private AutoGallery mGgalleryTextScroll;
	private ContentAdapter mTextScrollAdapter;
	private ContentAdapter mGoodsAdapter;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		mMainLayout.removeAllViews();
		setContentView(R.layout.store_persion);	
		showDialogCustom(DIALOG_CUSTOM);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_ShopShopData, 
		DataLoader.getInstance().getShopShopDataTypeParams(getIntent().getStringExtra("shopId")),this);
		init();
	}

	private void init() {	
		mListView = (ListViewForScrollView ) findViewById(R.id.store_hot_goods);
		mLinearLayout = (LinearLayout) findViewById(R.id.store_evaluate);		
		mAutoGallery = (AutoGallery) findViewById(R.id.gallery_banner);
		mPageGuide = (PageGuide) findViewById(R.id.pageguide);
		mGgalleryTextScroll = (AutoGallery) findViewById(R.id.gallery_bulltin);
		mAutoGallery.setAdapter(mContentAdapter = new ContentAdapter(){
			
			@Override
			public int getCount() {
				if(mFocusArr == null){
					return 0;
				}
				//判断图片的数量
				return Integer.MAX_VALUE;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(position >= mFocusArr.length()){
					position = position % mFocusArr.length();
				}
				if(convertView == null){
					convertView = LayoutInflater.from(StorePersonActivity.this).inflate(R.layout.store_page_item, null);
					convertView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, Utils.realSchoolBannerHeight(StorePersonActivity.this)));					
				}
				JSONObject object = mFocusArr.optJSONObject(position);
				ImageLoader.getInstance().displayImage(object.optString("image"), ((ImageView)convertView.findViewById(R.id.images)), ImageLoaderConfigs.displayImageOptionsBg);
				return convertView;
			}						
		});
		
		mAutoGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(position >= mFocusArr.length()){
					position = position % mFocusArr.length();
				}
				mPageGuide.setSelect(position);			
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		mAutoGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= mFocusArr.length()) {
	                position = position % mFocusArr.length();
	            }
				if(null != mFocusArr){
					JSONObject object = mFocusArr.optJSONObject((position));				
					Intent intent = new Intent(StorePersonActivity.this,StoreGoodsMoreDetailsActivity.class);
					if(object.has("goodsId")){
						if(null != object.optString("goodsId")){
							intent.putExtra("shopId",object.optString("goodsId"));
							startActivity(intent);
						}
					}
				}
				
			}			
		});				

		mGgalleryTextScroll.setAdapter(mTextScrollAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				if(mDynameArr == null || mDynameArr.length() == 0){
					return 0;
				}
				return Integer.MAX_VALUE;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = View.inflate(StorePersonActivity.this, R.layout.store_text_scroll_item, null);
					convertView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				}
				if (position >= mDynameArr.length()) {
	                position = position % mDynameArr.length();
	            }
				JSONObject obj = mDynameArr.optJSONObject(position);			
				if(obj != null){
					((TextView)convertView.findViewById(R.id.store_rctivitv_news)).setText(obj.optString("name"));
					//ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)convertView.findViewById(R.id.imageview)), ImageLoaderConfigs.displayImageOptionsBg);
				}
				return convertView;
			}			
		});
		
		mGgalleryTextScroll.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (position >= mDynameArr.length()) {
	                position = position % mDynameArr.length();
	            }
				if(null != mDynameArr && 0 != mDynameArr.length()){					
					JSONObject obj = mDynameArr.optJSONObject(position);
					Intent intent = new Intent(StorePersonActivity.this, WebViewActivity.class);
					intent.putExtra("result", obj.optString("content"));
					intent.putExtra("title", getResources().getString(R.string.postdetails_title));
					startActivity(intent);
				}
			}
		});
		
		findViewById(R.id.store_collection).setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(!DataLoader.getInstance().isLogin()){
					StorePersonActivity.this.startActivity(new Intent(StorePersonActivity.this, LoginActivity.class));
					((Activity) StorePersonActivity.this).overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
					return;
				}				
				int statues = 0;
				if(mStoreDetail != null && mStoreDetail.has("favoriteStatus") && mStoreDetail.optString("favoriteStatus").equals("0")){
					statues = 1;
				}
				else{
					statues = 0;
				}
				showDialogCustom(DIALOG_CUSTOM);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_FavoriteApply,
						DataLoader.getInstance().setFavoriteTypeParams("16", mStoreDetail.optString("id"), statues+""), StorePersonActivity.this);
			}
		});
		
		findViewById(R.id.store_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StorePersonActivity.this.finish();				
			}
		});
		
		findViewById(R.id.store_title_image).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT ,Utils.realServiceStoreImageHeight(StorePersonActivity.this)));				
		//搜索框				
		//((EditText)findViewById(R.id.search_result)).setCursorVisible(false);
		findViewById(R.id.search_result).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//((EditText)findViewById(R.id.search_result)).setCursorVisible(true);
				Intent intent = new Intent(StorePersonActivity.this,StoreSearchActivity.class);
				intent.putExtra("shopId", getIntent().getStringExtra("shopId"));
				intent.putExtra("isProduct", true);
				startActivity(intent);
			}
		});
		
		/*((EditText)findViewById(R.id.search_result)).setOnEditorActionListener(new OnEditorActionListener() {
					
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if(actionId ==EditorInfo.IME_ACTION_SEARCH){							
							 Intent intent = new Intent(StorePersonActivity.this,StoreDetailActivity.class);
							 intent.putExtra("SearchResult",((EditText)findViewById(R.id.search_result)).getText().toString());
							 startActivity(intent);							
							 return true;
						}
						return false;
					}
				});*/
			
		((StickyScrollView)findViewById(R.id.store_scrollview)).smoothScrollTo(0, 0);
		
		mListView.setAdapter(mGoodsAdapter = new ContentAdapter(){
			@Override
			public int getCount() {
				if(mGoodsArr != null && mGoodsArr.length()>0){
					return mGoodsArr.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder = null;
				if(convertView == null ){
					viewHolder = new ViewHolder();
					convertView = LayoutInflater.from(StorePersonActivity.this).inflate(R.layout.store_list_layout, null);
					viewHolder.imageview =  (ImageView)convertView.findViewById(R.id.good_image);
					viewHolder.name = (TextView) convertView.findViewById(R.id.tv_details);
					viewHolder.price = ((TextView)convertView.findViewById(R.id.tv_price));
					viewHolder.stock = ((TextView)convertView.findViewById(R.id.tv_stock));
					viewHolder.markPrice = ((TextView)convertView.findViewById(R.id.tv_mark_price));
					convertView.setTag(viewHolder);
				}
				JSONObject obj = mGoodsArr.optJSONObject(position);
				viewHolder = (ViewHolder) convertView.getTag();
				android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) viewHolder.imageview.getLayoutParams();
				params.width = (Utils.getScreenWidth(getApplicationContext())-viewHolder.imageview.getWidth())/4;
				params.height = (Utils.getScreenWidth(getApplicationContext())-viewHolder.imageview.getWidth())/4;
				viewHolder.imageview.setLayoutParams(params);
//				viewHolder.imageview.setLayoutParams(new RelativeLayout.LayoutParams((Utils.getScreenWidth(getApplicationContext())-viewHolder.imageview.getWidth())/4
//						,(Utils.getScreenWidth(getApplicationContext())-viewHolder.imageview.getWidth())/4));
				
				ImageLoader.getInstance().displayImage(obj.optString("image"), viewHolder.imageview,
				ImageLoaderConfigs.displayImageOptionsBg);
				viewHolder.name.setText(obj.optString("name"));
				viewHolder.price.setText(getResources().getString(R.string.money_sigh)+obj.optString("price"));
				viewHolder.stock.setText(getResources().getString(R.string.goods_already_sold)+obj.optString("sales") + getString(R.string.goods_inventory2));
				viewHolder.markPrice.setText(getString(R.string.money_sigh) + obj.optString("money"));
				viewHolder.markPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
				
				return convertView;
			}
			
			class ViewHolder{
				ImageView imageview;
				TextView name;
				TextView price;
				TextView stock;
				TextView markPrice;
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				JSONObject obj = mGoodsArr.optJSONObject(position);
				Intent intent = new Intent(StorePersonActivity.this,StoreGoodsMoreDetailsActivity.class);
				intent.putExtra("shopId", obj.optLong("id")+"");
				startActivity(intent);
			}
		});
		
		findViewById(R.id.store_persion_all).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(0 < mGoodsType.length()){
					Intent intent = new Intent(StorePersonActivity.this,StoreDetailActivity.class);									
					intent.putExtra("shopId", getIntent().getStringExtra("shopId"));
					intent.putExtra("storeGoods", 1);
					intent.putExtra("sort", "all");
					intent.putExtra("num", 0);
					intent.putExtra("category", items);
				    intent.putExtra("Data",mGoodsType);
					startActivity(intent);
			}else{
				Toast.makeText(StorePersonActivity.this,getResources().getString(R.string.get_data_failed) , Toast.LENGTH_SHORT).show();
			  }
		   }
		});
		
		findViewById(R.id.store_persion_catagory).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(0 < mGoodsType.length()){
					    String[] tempItems = new String[items.length - 1];
					    for(int i = 0; i < tempItems.length; i ++){
						    tempItems[i] = items[i + 1];
					    }
					    
					    JSONArray tempArr = null;
						try {
							tempArr = new JSONArray(mGoodsType.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    JSONArray tempArr2 = new JSONArray();
					    for(int i = 1; i < tempArr.length(); i ++){
					    	tempArr2.put(tempArr.optJSONObject(i));
					    }
					    
					    
						Intent intent = new Intent(StorePersonActivity.this,StoreGoodsCategoryActivity.class);									
						intent.putExtra("category", tempItems);
						intent.putExtra("shopId", getIntent().getStringExtra("shopId"));
						intent.putExtra("goodsType", tempArr2.toString());
						startActivity(intent);
						System.out.println("=====tye " + mGoodsType);
						System.out.println("=====tye " + items);
				}else{
					Toast.makeText(StorePersonActivity.this,getResources().getString(R.string.get_data_failed) , Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void setEvaluateStar(int length){
		 int show = length/2;//显示全部星
		 int t = length%2;//显示半星
		 ImageView [] evaluateStar = new ImageView[EVALUATESTAR];
		 LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dipPx(this, 14),Utils.dipPx(this, 14),1);
		 for(int i = 0;i < 5; i++){
			 if(i > EVALUATESTAR-1) break;
			 	evaluateStar[i] = new ImageView(StorePersonActivity.this);				 				
				if(i <= show-1){
					 evaluateStar[i].setImageResource(R.drawable.icon_grade_pre);				
				 }else if(i > show-1){
					 if(t == 1){
						 evaluateStar[i].setImageResource(R.drawable.icon_grade_halve);	
						 t++;
					 }else{
						 evaluateStar[i].setImageResource(R.drawable.icon_grade_nor); 					 
					 }
				 }
			 layoutParams.setMargins(Utils.dipPx(this, 5),Utils.dipPx(this, 1),0,Utils.dipPx(this, 2));
			 evaluateStar[i].setLayoutParams(new LinearLayout.LayoutParams(1,1)); 
			 mLinearLayout.addView(evaluateStar[i],layoutParams); 
		 }
	 }
		
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		
		if(result == null){
			findViewById(R.id.store_persion).setVisibility(View.GONE);
			return;
		}else{
			removeDialogCustom();
			findViewById(R.id.store_persion).setVisibility(View.VISIBLE);
		}
		
		if(result instanceof Error){
			Toast.makeText(StorePersonActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		findViewById(R.id.store_persion).setVisibility(View.VISIBLE);
		switch(type){
		case TaskOrMethod_ShopShopData :
			if(result instanceof JSONObject ){
				if(((JSONObject)result).has("dynamic")){
					mDynameArr = ((JSONObject)result).optJSONArray("dynamic");
					mGgalleryTextScroll.setSelection(mDynameArr.length() * 1000000);
					mGgalleryTextScroll.setLength(mDynameArr.length());
					mGgalleryTextScroll.setDuration(5000);
					mGgalleryTextScroll.setAutoScroll();					
					if(null != mDynameArr && 0 < mDynameArr.length()){						 
						mTextScrollAdapter.notifyDataSetChanged();						
				    }
				}
				if(((JSONObject)result).has("items")){
					mStoreDetail = ((JSONObject)result).optJSONObject("items");	
					ImageLoader.getInstance().displayImage(mStoreDetail.optString("image"), ((ImageView)findViewById(R.id.store_title_image)) ,ImageLoaderConfigs.displayImageOptionsBg);				
					ImageLoader.getInstance().displayImage(mStoreDetail.optString("logo"), ((ImageView)findViewById(R.id.store_logo)) ,ImageLoaderConfigs.displayImageOptionsRoundCenter5);
					((TextView)findViewById(R.id.store_name)).setText(mStoreDetail.optString("name"));					
					if(mStoreDetail.optString("favoriteStatus").equals("0")){
						((ImageView)findViewById(R.id.store_collection_image)).setImageResource(R.drawable.icon_collect_pre);
					}
					else{
						((ImageView)findViewById(R.id.store_collection_image)).setImageResource(R.drawable.icon_collect_nor);
					}
				}
				//图片广告
				if(((JSONObject)result).has("focus")){
					mFocusArr = ((JSONObject)result).optJSONArray("focus");	
				  if(mFocusArr !=null && mFocusArr.length()>0){	
					mPageGuide.setParams(mFocusArr.length(), Utils.dipToPixels(StorePersonActivity.this, 7), Utils.dipToPixels(StorePersonActivity.this, 7));
					mAutoGallery.setSelection(mFocusArr.length() * 1000000);
					mAutoGallery.setLength(mFocusArr.length());
					mAutoGallery.setDuration(4000);
					mAutoGallery.setAutoScroll();
					if(mAutoGallery != null && mAutoGallery.getAdapter() != null){
						((ContentAdapter)mAutoGallery.getAdapter()).notifyDataSetChanged();
					}
				  }
				}
				if(((JSONObject)result).has("goods")){			
					mGoodsArr = ((JSONObject)result).optJSONArray("goods");	
					if(null != mGoodsArr && 0 < mGoodsArr.length()){
						mGoodsAdapter.notifyDataSetChanged();
					}
				}
				
				if(((JSONObject)result).has("goodsType")){
					JSONArray goodsType = ((JSONObject)result).optJSONArray("goodsType");
					items = new String [goodsType.length()];
					mGoodsType = goodsType.toString();	
					for(int i = 0;i < goodsType.length();i++){
						JSONObject obj = goodsType.optJSONObject(i);
						items[i] = obj.optString("name");
					}
				}
				setEvaluateStar(((JSONObject)result).optInt("evaluate"));
			}			
			break;
		case TaskOrMethod_FavoriteApply:
			try {
				if(mStoreDetail != null && mStoreDetail.has("favoriteStatus") && mStoreDetail.optString("favoriteStatus").equals("0")){
					mStoreDetail.put("favoriteStatus", "1");
					((ImageView)findViewById(R.id.store_collection_image)).setImageResource(R.drawable.icon_collect_nor);
				}
				else{
					mStoreDetail.put("favoriteStatus", "0");
					((ImageView)findViewById(R.id.store_collection_image)).setImageResource(R.drawable.icon_collect_pre);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}
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
