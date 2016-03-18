package com.zhuochuang.hsej.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.layout.ListViewForScrollView;
import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRemoreListener;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskType;
import com.util.ContentAdapter;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;

public class StoreDetailActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	public static Boolean isGridView = false;
	private GridView mGridView;
	private PullToRefreshListView mListView;
	private ImageView mImageViewChange;
	private String [] mItems ;
	private JSONArray mItemArr;
	private ServiceZoneAdapter mAdapter;
	private int selectedPosition = 0;
	private PopupWindow mPopupWindow;
	private LinearLayout popupLayout;
	private ListView rootListView;
	private ListViewForScrollView subListView;
	private Boolean mLock = false;
	private int mType;
	private JSONArray mGoodsType; 
	private ContentAdapter mRootAdapter;
	private ContentAdapter mSubAdapter; 
	private Map<String,Object> map;
	private JSONArray mSubDataArr;
	private String mSchema="";
	private String mShopId="";
	private String mParentType="";
	private String mSubType="";
	private String mResult="";
	private int mParentId;
	private int mNativeCode;
	private Handler mHandler;
	private Map<Integer,List<Integer>> mNativeCodeMap;
	private List<Integer> mNativiecode;
	
	private boolean mIsFirstLoad = true;
	int mPageNo = 1;
	int mPageSize = 10;
	boolean mIsReadMore = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		mMainLayout.removeAllViews();
		setContentView(R.layout.store_details);	
		showDialogCustom(DIALOG_CUSTOM);
		mNativeCodeMap = new HashMap<Integer,List<Integer>>();
		mNativiecode = new ArrayList<Integer>();
		if( StoreGoodsCategoryActivity.STOREGOODS == getIntent().getIntExtra("storeGoods", 0)){		
			((EditText)findViewById(R.id.search_result)).setText(getIntent().getStringExtra("StoreGoodsCategoryActivity"));		
			if(getIntent().getStringExtra("StoreGoodsCategoryActivity") != null){
				((EditText)findViewById(R.id.search_result)).setSelection(getIntent().getStringExtra("StoreGoodsCategoryActivity").length());
			}			
			String name = parseData(getIntent().getStringExtra("name"));
		    mShopId = parseData(getIntent().getStringExtra("shopId"));
		    mParentType = parseData(getIntent().getStringExtra("parentType"));
			mSubType = parseData(getIntent().getStringExtra("subType"));
			String sort = parseData(getIntent().getStringExtra("sort"));
			((TextView)findViewById(R.id.details_list)).setText(getIntent().getStringExtra("parentName"));
			if(null != getIntent().getStringExtra("Data")){
				map = new HashMap<String, Object>();
				mItems = getIntent().getStringArrayExtra("category");
				try {
					mGoodsType = new JSONArray(getIntent().getStringExtra("Data"));	
					if(null != mGoodsType && 0 < mGoodsType.length()){						
						for(int i = 0;i < mGoodsType.length();i++){								
							JSONObject obj = mGoodsType.optJSONObject(i);
							if(null != obj){
								mNativiecode.add(obj.optInt("id"));//父累Id	
								JSONArray array = obj.optJSONArray("children");	
								if(null != array){									
									map.put(mItems[i], array);						
									List<Integer> list = new ArrayList<Integer>();
									for(int j = 0;j < array.length();j++){
										JSONObject subObj = array.optJSONObject(j);
										if(null != subObj){										
											list.add(subObj.optInt("id"));
											mNativeCodeMap.put(mNativiecode.get(i),list);//子类Id							
										}
									}								
								}
							}
						}	
					  }
					} catch (Exception e) {
						e.printStackTrace();
				} 
			};
			if(0 != getIntent().getIntExtra("nativeCode",0)){				
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GoodsTypeListGoodsType,
						DataLoader.getInstance().setListGoodTypeParams(getIntent().getIntExtra("nativeCode",0)+""), this);
				
			}else{
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GoodsListGoods,
						 DataLoader.getInstance().setGoodsListGoodsTypeParams(name,mShopId,mParentType,mSubType,sort, mPageNo), this);
			}
			
			if(null != getIntent().getStringExtra("Data")){			
				if(0 == getIntent().getIntExtra("num", 0)){
					((TextView)findViewById(R.id.details_list)).setText(mItems[getIntent().getIntExtra("num", 0)]);
					selectedPosition = getIntent().getIntExtra("num", 0);				
				}			
			}		
		}else{
			findViewById(R.id.search_main).setVisibility(View.VISIBLE);
			findViewById(R.id.details_list_layout).setVisibility(View.GONE);
			((EditText)findViewById(R.id.search_result)).setText(getIntent().getStringExtra("SearchResult"));
			mShopId = getIntent().getStringExtra("shopId") == null ? "" : getIntent().getStringExtra("shopId");
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GoodsListGoods,
					DataLoader.getInstance().setGoodsListGoodsTypeParams(getIntent().getStringExtra("SearchResult"),mShopId,"","","", mPageNo), this);
		}
		initView();	
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case EventManager.EventType_ShoppingCar:
					Object [] obj = (Object[]) msg.obj;				
					if(0 < obj.length && 1 < obj.length){	
						int count = Integer.parseInt(obj[0] == null ? "" : obj[0].toString());
						EventManager.getInstance().sendMessage(EventManager.getInstance().EventType_ShoppingCar, obj[0]);	
					}
					break;
				default:
					break;
				}
			}
			
		});
	}
	
	private void initView() {	
		mSchema = "all";		
		((TextView) findViewById(R.id.tv_alls)).setTextColor(Color.RED);
		mGridView = (GridView) findViewById(R.id.app_grid);
		mListView = (PullToRefreshListView) findViewById(R.id.app_list);
		mListView.setOnRemoreListener(new OnRemoreListener() {
			
			@Override
			public void onRemore() {
				// TODO Auto-generated method stub
				mIsReadMore = true;
				mPageNo ++;
				orderByData();
			}
		});
		((EditText)findViewById(R.id.search_result)).setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId ==EditorInfo.IME_ACTION_SEARCH){
					showDialogCustom(DIALOG_CUSTOM);
					DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GoodsListGoods,
							DataLoader.getInstance().setGoodsListGoodsTypeParams(((EditText)findViewById(R.id.search_result)).getText().toString(),"","","","", mPageNo),
							StoreDetailActivity.this);						
					 return true;
				}
				return false;
			}
		});
		(mImageViewChange = (ImageView) findViewById(R.id.imagechange)).setOnClickListener(this);	
		((TextView) findViewById(R.id.tv_alls)).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_saless)).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_news)).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_pricess)).setOnClickListener(this);
		((TextView)findViewById(R.id.details_list)).setOnClickListener(this);
		findViewById(R.id.search_result).setOnClickListener(this);
		findViewById(R.id.main_left).setOnClickListener(this);		
		mListView.setOnItemClickListener(this);
		mGridView.setOnItemClickListener(this);			
	}
	//筛选
	private void getParendType(JSONArray data){
		for(int i = 0 ; i < data.length();i++){
			JSONObject obj = data.optJSONObject(i);			
			if(getIntent().getIntExtra("nativeCode",0) == obj.optInt("id")){			
				((TextView)findViewById(R.id.details_list)).setText(obj.optString("name"));
			}
		}
	}
	
	private void changePage(){
		showDialogCustom(DIALOG_CUSTOM);
		mPageNo = 1;
		mListView.setRemoreable(true);
		cleanColor();
		orderByData();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagechange:
			isGridView = !isGridView;
			updateLayout();
			break;
		case R.id.tv_alls:
			mSchema = "all";
			changePage();
			((TextView) findViewById(R.id.tv_alls)).setTextColor(Color.RED);		
			break;
		case R.id.tv_saless:
			mSchema = "sales";
			changePage();
			((TextView) findViewById(R.id.tv_saless)).setTextColor(Color.RED);
			break;
		case R.id.tv_news:
			mSchema = "newGoods";
			changePage();
			((TextView) findViewById(R.id.tv_news)).setTextColor(Color.RED);
			break;	
		case R.id.tv_pricess:
			changePage();
			if(!mLock){
				mSchema = "priceDesc";
				mLock = true;
				((ImageView)findViewById(R.id.store_price)).setImageResource(R.drawable.levelbar_price_high);
			}else{
				mSchema = "priceAsc";
				mLock = false;
				((ImageView)findViewById(R.id.store_price)).setImageResource(R.drawable.levelbar_price_low);
			}
			((TextView) findViewById(R.id.tv_pricess)).setTextColor(Color.RED);
			break;	
		case R.id.details_list:
			if(mType == 0){
				showPopupWindow(Utils.getScreenWidth(StoreDetailActivity.this), Utils.getScreenHeight(StoreDetailActivity.this));
			}else{
								
			}
			break;
		case R.id.search_result:
			((EditText)findViewById(R.id.search_result)).setCursorVisible(true);
			break;
		case R.id.store_back:
			finish();
			break;
		case R.id.main_left:
			finish();
			break;		
		default:
			break;
		}
		
	}
	
	//private 
	
	private void orderByData(){
		if(!"".equals(mResult)){					
		}else{
			mResult = getIntent().getStringExtra("SearchResult");
			if(null == mResult){
				mResult = "";
			}
		}	
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GoodsListGoods,
				DataLoader.getInstance().setGoodsListGoodsTypeParams(mResult,mShopId,mParentType,mSubType,mSchema, mPageNo), this);		
	}
	
	@SuppressLint("ResourceAsColor")
	public void cleanColor() {
		((TextView) findViewById(R.id.tv_alls)).setTextColor(getResources().getColor(R.color.text));
		((TextView) findViewById(R.id.tv_saless)).setTextColor(getResources().getColor(R.color.text));
		((TextView) findViewById(R.id.tv_news)).setTextColor(getResources().getColor(R.color.text));
		((TextView) findViewById(R.id.tv_pricess)).setTextColor(getResources().getColor(R.color.text));
		((ImageView)findViewById(R.id.store_price)).setImageResource(R.drawable.levelbar_priceicon);
	}

	public void updateLayout() {		
		if(isGridView){
				/*if (mGridView == null) {
					mGridView = (GridView) findViewById(R.id.app_grid);
					mGridView.setOnItemClickListener(this);
				}
				if(null != mItemArr){
					mGridView.setAdapter(mAdapter);
					mGridView.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
					//mGridView.setSelection(0);
					mImageViewChange.setImageResource(R.drawable.nav_icon_listview);
				}*/
			if (mListView == null) {
				mListView = (PullToRefreshListView) findViewById(R.id.app_list);
				mListView.setOnItemClickListener(this);
			}
			if(null != mItemArr){
				mListView.setAdapter(mAdapter);
				mListView.setVisibility(View.VISIBLE);
				mGridView.setVisibility(View.GONE);
				//mListView.setSelection(0);
				mImageViewChange.setImageResource(R.drawable.nav_icon_listview);
			}
			}else {
				if (mListView == null) {
					mListView = (PullToRefreshListView) findViewById(R.id.app_list);
					mListView.setOnItemClickListener(this);
				}
				if(null != mItemArr){
					mListView.setAdapter(mAdapter);
					mListView.setVisibility(View.VISIBLE);
					mGridView.setVisibility(View.GONE);
					//mListView.setSelection(0);
					mImageViewChange.setImageResource(R.drawable.nav_icon_gridview);
				}
		  }
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(mItemArr == null || mItemArr.length() == 0){
			return;
		}
		JSONObject obj = mItemArr.optJSONObject(position - 1);
		if(obj != null){
			Intent intent = new Intent(StoreDetailActivity.this, StoreGoodsMoreDetailsActivity.class);
			intent.putExtra("shopId", obj.optString("id"));
			intent.putExtra("count", getIntent().getIntExtra("count", 0));
			startActivity(intent);
		}
	}
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);		
		mListView.complete();
		if(result == null){	
			new AlertDialog.Builder(StoreDetailActivity.this).setCancelable(false)
			.setMessage(StoreDetailActivity.this.getResources().getString(R.string.load_fail))
			.setPositiveButton(StoreDetailActivity.this.getResources()
					.getString(R.string.load_again),new DialogInterface.OnClickListener() {
				@Override
				public void onClick(
						DialogInterface dialog,int which) {
					showDialogCustom(DIALOG_CUSTOM);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							removeDialogCustom();
							
						}
					}, 500);
				}
			}).setNegativeButton(StoreDetailActivity.this.getResources().getString(R.string.store_shopping_car_cancle),null).show();	
			return;
		}else{
			removeDialogCustom();
		}
		
		if(result instanceof Error){
			Toast.makeText(this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_GoodsListGoods:
			if(result instanceof JSONObject ){
				if(((JSONObject)result).has("items")){
					JSONArray tempArr = ((JSONObject)result).optJSONArray("items");
					if(mIsReadMore){
						mItemArr = DataLoader.getInstance().joinJSONArray(mItemArr, tempArr);
					}else {
						mItemArr = tempArr;			
						if(null != mItemArr && 0 < mItemArr.length()){
							if(0 == mItemArr.length()){
								Toast.makeText(StoreDetailActivity.this,getResources().getString(R.string.goods_search_null),Toast.LENGTH_SHORT).show();
							}
						}
					}
					
					if(mAdapter == null){
						mAdapter = new ServiceZoneAdapter(mItemArr, StoreDetailActivity.this);
					}
					mAdapter.setData(mItemArr, mSchema);
					
					if(null == tempArr || tempArr.length() < mPageSize){
						mIsReadMore = false;
						mListView.setRemoreable(false);
					}
					
					mAdapter.notifyDataSetChanged();
					updateLayout();
					
					if(mItemArr == null || mItemArr.length() == 0){
						findViewById(R.id.noData).setVisibility(View.VISIBLE);
						mListView.setVisibility(View.INVISIBLE);
					}else {
						findViewById(R.id.noData).setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
					}
				}			
			}
			break;			
		case TaskOrMethod_GoodsTypeListGoodsType:
			if(result instanceof JSONObject ){
				if(((JSONObject)result).has("items")){
					mParentType = ((JSONObject)result).optJSONArray("items").optJSONObject(0).optString("parentId");
					if(null != ((JSONObject)result).optJSONArray("items").optJSONObject(0).optString("id")){						
						if(mIsFirstLoad){
							mSubType = "";
							mIsFirstLoad = false;
						}else {
							mSubType = ((JSONObject)result).optJSONArray("items").optJSONObject(0).optString("id");
						}
						
						mParentId = ((JSONObject)result).optJSONArray("items").optJSONObject(0).optInt("parentId");	
						for(int i = 0; i < mNativiecode.size();i++){				
							if(mParentId == mNativiecode.get(i)){
								selectedPosition = i;
								((TextView)findViewById(R.id.details_list)).setText(mGoodsType.optJSONObject(i).optString("name"));
							}
						}
					}		
					mParentId = ((JSONObject)result).optJSONArray("items").optJSONObject(0).optInt("parentId");
					DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GoodsListGoods,
							            DataLoader.getInstance().setGoodsListGoodsTypeParams("","",mParentType,mSubType,"", mPageNo), this);
				}				
			}
			break;						
		default:
			break;
		}		
	}
	public String parseData(String data){
		if(null!= data){
			return data;
		}
		return "";
	}

	private void showPopupWindow(int width,int heigh){
	    
		LayoutInflater inflater = LayoutInflater.from(StoreDetailActivity.this);
		popupLayout = (LinearLayout) inflater.inflate(R.layout.store_detail_popupwindow, null, false);
		rootListView = (ListView) popupLayout.findViewById(R.id.root_listview);
		subListView = (ListViewForScrollView) popupLayout.findViewById(R.id.sub_listview);
		mPopupWindow = new PopupWindow(popupLayout, width,heigh, true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.showAsDropDown(findViewById(R.id.store_line));	
		mPopupWindow.update();
		rootListView.setAdapter(mRootAdapter = new ContentAdapter(){
			
			@Override
			public int getCount() {
				return mItems != null ? mItems.length : 0;
			}
			
			@Override
			public View getView(int position, View convertView,ViewGroup parent) {
				ViewHolder holder;
				if(convertView == null){					
					holder = new ViewHolder();
					convertView = LayoutInflater.from(StoreDetailActivity.this).inflate(R.layout.store_detail_popupwindow_item,parent,false);
					holder.item_text =(TextView) convertView.findViewById(R.id.item_name_text);
					holder.item_layout = (LinearLayout)convertView.findViewById(R.id.root_item);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder)convertView.getTag();
				}
				//设置点击的状态				
				if(selectedPosition == position){
					holder.item_layout.setBackgroundColor(Color.parseColor("#f0f0f0"));
					holder.item_text.setTextColor(Color.RED);
				}else{
					holder.item_layout.setBackgroundColor(Color.WHITE);
					holder.item_text.setTextColor(Color.BLACK);
				}
							
				holder.item_text.setText(mItems[position]);
				return convertView;
			}
			class ViewHolder{
				TextView item_text;
				LinearLayout item_layout;
			}					
		});		
		new Handler().post(new Runnable() {			
			@Override
			public void run() {
				subListView.setAdapter(mSubAdapter = new ContentAdapter(){

					@Override
					public int getCount() {
						if(selectedPosition == 0){
							return 0;
						}
						mSubDataArr =  (JSONArray) map.get(mItems[selectedPosition]);											
						return mSubDataArr == null ? 0: mSubDataArr.length();
					}

					@Override
					public View getView(int position, View convertView,ViewGroup parent) {
						ViewHolder holder;
						if (convertView == null) {
							holder = new ViewHolder();
							convertView = LayoutInflater.from(StoreDetailActivity.this).inflate(R.layout.store_popupwindow_item,parent,false);
							holder.item_text = (TextView) convertView.findViewById(R.id.store_popupwindowItem);
							holder.item_sum =  (TextView) convertView.findViewById(R.id.store_popupwindowItem_count);
							convertView.setTag(holder);
						}else{
							holder = (ViewHolder) convertView.getTag();
						}						
						holder.item_text.setText(mSubDataArr.optJSONObject(position).optString("name"));
						holder.item_sum.setText(mSubDataArr.optJSONObject(position).optString("goodsSum"));
						return convertView;
					}
					
					class ViewHolder{
						TextView item_text;
						TextView item_sum;
					}
				});				
			}
		});
		
		rootListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
				selectedPosition = position;
				((TextView)findViewById(R.id.details_list)).setText(mItems[selectedPosition]);	
				if(position == 0){
					mPopupWindow.dismiss();
					
					cleanColor();
					mSchema = "all";
					((TextView) findViewById(R.id.tv_alls)).setTextColor(Color.RED);		
					showDialogCustom(DIALOG_CUSTOM);
					mSubType = "";
					mParentType = "";
					mPageNo = 1;
					mListView.setRemoreable(true);
					orderByData();
					return;
				}
				
				mRootAdapter.notifyDataSetChanged();
				mSubAdapter.notifyDataSetChanged();
				mParentType = mNativiecode.get(position)+"";				
			}
		});
		
		subListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
				mPageNo = 1;
				mListView.setRemoreable(true);
				mPopupWindow.dismiss();							
				List<Integer> listSub;	
				if("".equals(mParentType)){
					mParentType = "0";
				}
				listSub = mNativeCodeMap.get(Integer.parseInt(mParentType));
				for(int i = 0 ; i < listSub.size(); i ++){
					if(i == position){
						mSubType = listSub.get(i)+"";
						if(0 == listSub.get(i)){			
							mSubType = "";
						}
					}
				}
				if(0 == Integer.parseInt(mParentType)){
					mParentType = "";
				}
				showDialogCustom(DIALOG_CUSTOM);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GoodsListGoods,
						DataLoader.getInstance().setGoodsListGoodsTypeParams("",mShopId,mParentType,mSubType,"", mPageNo), StoreDetailActivity.this);
				((TextView)findViewById(R.id.details_list)).setText(mSubDataArr.optJSONObject(position).optString("name"));
			}
		});
		
		popupLayout.findViewById(R.id.store_detail_view).getBackground().setAlpha(100);
			
	}

	 public void backgroundAlpha(float bgAlpha){  
	        WindowManager.LayoutParams lp = getWindow().getAttributes();  
	        lp.alpha = bgAlpha;  
	        getWindow().setAttributes(lp);  
	    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((HSESchoolApp)getApplication()).removeActivityStore(this);
	}  
	 	
}
