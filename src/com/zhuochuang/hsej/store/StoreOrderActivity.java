package com.zhuochuang.hsej.store;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.ListViewForScrollView;
import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.PullToRefreshListView.OnRemoreListener;
import com.model.Configs;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.WebViewActivity;
import com.zhuochuang.hsej.store.StorePupopWindow.onPopupWindowItemClickListener;

public class StoreOrderActivity extends BaseActivity {
	private StorePupopWindow pupopWindow;
	private String[] items;
	private String[] item;
	private String[] mFilter;
	private String[] mStatuscree;
	private String[] mStatuStr;
	private PullToRefreshListView mListView;
	private JSONArray mItemsArr;
	private JSONArray mFilterArr;
	private ContentAdapter mAdapter;
	private String mStatus;
	private boolean mIsRefresh, mIsReadMore;
	private int mSelecterIndex = 0;
	int mPageSize = 20, mPageNo = 1;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		mMainLayout.removeAllViews();
		setContentView(R.layout.store_check_order);
		items = getResources().getStringArray(R.array.store_order_selection);
		mFilter = getResources().getStringArray(R.array.store_orders_status);
		item = getResources().getStringArray(R.array.store_order_status);
		mStatuStr = getResources().getStringArray(R.array.store_pay);
		mStatuscree =  getResources().getStringArray(R.array.store_orders_scree);
		initView();
		findViewById(R.id.search_list).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(StoreOrderActivity.this, StoreOrderSearchActivity.class));
			}
		});
	}

	private void initView() {
		mStatus = "";
		mListView = (PullToRefreshListView) findViewById(R.id.store_order_list);
		pupopWindow = new StorePupopWindow(StoreOrderActivity.this, 1,2);
		pupopWindow.changeData(Arrays.asList(mFilter),getIntent().getIntExtra("com.zhuochuang.hsej.store", 0));
		pupopWindow.setOnPoPupWindowClickListener(new onPopupWindowItemClickListener() {
			@Override
			public void onPopupWindowItemClick(int position) {
				//mParams.alpha = 0.8f;
				//getWindow().setAttributes(mParams);
				switch (position) {
				case 0:
					mStatus = "";
					break;
				case 1:
					mStatus = "1";
					break;
				case 2:
					mStatus = "3";
					break;
				case 3:
					mStatus = "2,3,4,5";
					break;
				case 4:
					mStatus = "6,9";
					break;
				/*case 5:
					mStatus = "6";
					break;*/
				case 5:
					mStatus = "7";
					break;
				case 6:
					mStatus = "8";
					break;
				default:
					break;
				}
				String name = mFilter[position];
				//mSelecterIndex = position;
				if(position == 0){
					name = getResources().getString(R.string.store_my_order);
				}
				((TextView) findViewById(R.id.details_list)).setText(name);			 
				/*if (0 != position) {										
					mFilterArr = screening(position);
					//mAdapter.notifyDataSetChanged();
				}else{
					mFilterArr = mItemsArr;
				}
				mAdapter.notifyDataSetChanged();*/
				
				if(mListView != null){
					mListView.startRefresh();
				}
			}
		});
		
		pupopWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
				findViewById(R.id.details_list).setVisibility(View.VISIBLE);
				findViewById(R.id.main_left).setVisibility(View.VISIBLE);
				findViewById(R.id.details_list_).setVisibility(View.GONE);
			}
		});

		findViewById(R.id.details_list).setOnClickListener(
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					backgroundAlpha(0.8f);
					findViewById(R.id.details_list).setVisibility(View.GONE);
					findViewById(R.id.details_list_).setVisibility(View.VISIBLE);
					findViewById(R.id.main_left).setVisibility(View.GONE);
					//mParams.alpha = 0.8f;
					pupopWindow.showAsDropDown(findViewById(R.id.store_detail_title));
				}
			});
		
		findViewById(R.id.main_left).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		mListView.setAdapter(mAdapter = new ContentAdapter() {

			@Override
			public int getCount() {
				if(mFilterArr != null){
					return mFilterArr.length();
				}
				return 0;
			}

			@SuppressLint("ResourceAsColor") @Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = LayoutInflater.from(StoreOrderActivity.this).inflate(R.layout.store_my_order_list_item, null);					
				}
			    final JSONObject obj = mFilterArr.optJSONObject(position);
				final JSONArray orderGoods = obj.optJSONArray("orderGoods");			
				((TextView)convertView.findViewById(R.id.store_name)).setText(obj.optString("shopName"));

				((TextView)convertView.findViewById(R.id.store_pay_status)).setText(returnStatus(obj.optInt("status", 0), obj.optString("payType")));
				
				((TextView)convertView.findViewById(R.id.store_my_order_number)).setText(getResources()
						.getString(R.string.order_number)+obj.optString("orderNum"));				
				((TextView)convertView.findViewById(R.id.store_my_order_time)).setText(getResources()
						.getString(R.string.order_time)+getStringFromLong(obj.optLong("createDate")));
				ListViewForScrollView listView = (ListViewForScrollView) convertView.findViewById(R.id.order_list);
				
				convertView.findViewById(R.id.store_name).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(StoreOrderActivity.this, StorePersonActivity.class);
						intent.putExtra("shopId",obj.optString("shopId"));
						startActivity(intent);
					}
				});
				int count = 0;
				if(orderGoods != null && orderGoods.length() > 0){
					for(int i = 0; i < orderGoods.length(); i++){
						JSONObject goods = orderGoods.optJSONObject(i);
						if(goods != null){
							count += goods.optInt("number", 0);
						}
					}
				}
				((TextView)convertView.findViewById(R.id.store_my_order_sum)).setText(String.format(getResources().getString(R.string.sum_to_order), count +"")+" "+
						getResources().getString(R.string.sum_to_sign)+new DecimalFormat("#.00").format(obj.optDouble("money")));
				
				listView.setAdapter(new ContentAdapter(){

					@Override
					public int getCount() {			
						return orderGoods.length() == 0 ? 0 : orderGoods.length();
					}

					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if(convertView == null){
							convertView = LayoutInflater.from(StoreOrderActivity.this).inflate(R.layout.store_order_list_item, null);					
						}
						JSONObject goods = orderGoods.optJSONObject(position);
						ImageLoader.getInstance().displayImage(goods.optString("goodsImage"),
								(ImageView)convertView.findViewById(R.id.store_my_order_goods_images),ImageLoaderConfigs.displayImageOptionsBg);
						((TextView)convertView.findViewById(R.id.store_my_order_goods_name)).setText(goods.optString("goodsName"));
						((TextView)convertView.findViewById(R.id.store_my_order_goods_price)).setText(getResources().getString(R.string.money_sigh)+new DecimalFormat("#.00").format(goods.optDouble("price")));
						((TextView)convertView.findViewById(R.id.store_my_order_goods_sum)).setText("X"+goods.optString("number"));
						((TextView)convertView.findViewById(R.id.store_deliverytype)).setText(returnDeliveryType(obj.optString("payType")));
						if(!"null".equals(goods.optString("colour"))){							
							((TextView)convertView.findViewById(R.id.store_my_order_goods_color)).setText(getResources().getString(R.string.goods_color)+goods.optString("colour"));
						}
						
						if(!"null".equals(goods.optString("spec"))){							
							((TextView)convertView.findViewById(R.id.store_my_order_goods_size)).setText(getResources().getString(R.string.goods_size)+goods.optString("spec"));
						}
						return convertView;
					}
					
				});
				
				listView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(StoreOrderActivity.this,StoreOrderDetailActivity.class);
						intent.putExtra("OrderData", obj.toString());
						startActivity(intent);						
					}					
				});
				((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#212121"));
				convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.bg_store_black);
				
				switch (obj.optInt("status", 0)) {
				case 1:
					if(2 == obj.optInt("payType")){					
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[1]);
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
						convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_bg);
					}
					else{					
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[0]);
						convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
					}
					break;
				case 2:							
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[1]);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_bg);
				
					//((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
					break;
					//待配送
				case 3:			
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[2]);
					//((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(R.color.black);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_bg);
					break;
				case 4:	
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[3]);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_bg);
					/*((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));*/
					break;
				case 5:
					//确认收货
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[5]);
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.bg_store_black);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#212121"));
					break;
				case 6:
					//评价界面
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_goods_to_evaluate));
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
					break;
				case 7:
					//已取消 已退款，退款中
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[7]);
					//convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
					//((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_bg);
					break;
				case 8:
					//已退款
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[8]);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					//((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
					//((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_bg);
					break;
				case 9:
					//已评价
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[9]);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					/*((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));*/
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_bg);
					break;		
				default:
					break;
				}
				
				final int Orderstatus = obj.optInt("status");
				convertView.findViewById(R.id.store_my_order_confirm).setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {			
						Intent intent = null;
						switch(Orderstatus){
						case 1:	
							showDialogCustom(DIALOG_CUSTOM);
							DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_OrdersGetPayUrl,
									DataLoader.getInstance().setOrderNumTypeParams(obj.optString("orderNum")),StoreOrderActivity.this);
							break;						
						case 5:
						    /*intent = new Intent(StoreOrderActivity.this, StoreOrderDetailActivity.class);	
						    intent.putExtra("GoodsData", orderGoods.toString());*/
							new AlertDialog.Builder(StoreOrderActivity.this).setCancelable(false)
	    					.setMessage(getResources().getString(R.string.stores_confirm_receipt))
	    					.setPositiveButton(getResources().getString(R.string.confirm),new DialogInterface.OnClickListener() {
	    						@Override
	    						public void onClick(DialogInterface dialog,int which) {
	    							String mean = "";
	    							if(obj.has("means")){
	    								mean = obj.optString("means");
	    							}
	    							showDialogCustom(DIALOG_CUSTOM);
	    							DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_OrdersUpdateOrderStatus,
	    									DataLoader.getInstance().setUpdateOrderStatusTypeParams(mean,obj.optString("orderNum"),"6"), StoreOrderActivity.this);
	    						}
	    					}).setNegativeButton(getResources().getString(R.string.store_shopping_car_cancle),null).show();
							break;
						case 6:
						    intent = new Intent(StoreOrderActivity.this, StoreGoodsListEvaluateActivity.class);	
						    //intent.putExtra("GoodsData", orderGoods.toString());
						    intent.putExtra("orderNum", obj.optString("orderNum"));
							break;
						case 9:
							//查看评价
							//intent = new Intent(StoreOrderActivity.this, StoreGoodsCommentListActivity.class);
							
							//intent.putExtra("goodsId", value);
						default:
							break;
						}
						if(null != intent){
							startActivity(intent);
						}
				   }						
				});			
				return convertView;
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {	
				/*try {
					Intent intent = new Intent(StoreOrderActivity.this,StoreOrderDetailActivity.class);
					intent.putExtra("OrderData", mFilterArr.get(position).toString());
					startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		});
	  mListView.setOnRefreshListener(new OnRefreshListener() {
		@Override
		public void onRefresh() {
			mIsRefresh = true;
			mPageNo = 1;
			DataLoader.getInstance().startTaskForResult(
					TaskType.TaskOrMethod_OrdersListOrders,DataLoader.getInstance().getListOrdersTypeParams(mStatus, "", mPageNo, mPageSize), StoreOrderActivity.this);			
		}});
	  mListView.setRemoreable(false);
	  mListView.setOnRemoreListener(new OnRemoreListener() {
		
		@Override
		public void onRemore() {
			// TODO Auto-generated method stub
			mIsReadMore = true;
			mPageNo ++;
			DataLoader.getInstance().startTaskForResult(
					TaskType.TaskOrMethod_OrdersListOrders,DataLoader.getInstance().getListOrdersTypeParams(mStatus, "", mPageNo, mPageSize), StoreOrderActivity.this);			
		}
	  });
	  
	
	  mListView.startRefresh();
	}
	
	public String returnDeliveryType(String means){
		if(means.equals("1")){
			return getResources().getString(R.string.store_pay_online);
		}else{
			return getResources().getString(R.string.store_goods_cash);
		}
	}
	
	public void backgroundAlpha(float bgAlpha){  
        WindowManager.LayoutParams lp = getWindow().getAttributes();  
       lp.alpha = bgAlpha; 
        getWindow().setAttributes(lp);  
}  
	
	//返回订单状态
	public String returnStatus(int status, String payType){
		String statu = "";
		switch(status){
		case 1:
			statu = getResources().getString(R.string.stores_new_order);//mStatuscree[6];
			break;
		case 2:
			if(payType.equalsIgnoreCase("2")){
				statu = "";
			}
			else{
				statu = mStatuscree[0];
			}
			break;
		case 3:
			statu = mStatuscree[1];
			break;
		case 4:
			statu = mStatuscree[2];
			break;
		case 5:
			statu = mStatuscree[3];
			break;
		case 6:
			statu = mStatuscree[4];
			break;
		case 7:
			statu = mStatuscree[5];
			break;
		case 8:
			break;
		case 9:
			statu = mStatuscree[4];
			break;
		}
		return statu;
	}
	//筛选
	public JSONArray screening(int status){
		JSONArray array = new JSONArray();
		if(mItemsArr.length() > 0 && mItemsArr != null){
			for(int i = 0 ;i < mItemsArr.length(); i++ ){
				JSONObject obj = mItemsArr.optJSONObject(i);				
				switch (status) {
				case 1:
					if(1 == obj.optInt("status")){
						array.put(obj);
					}
					break;
				case 2:
					if(3 == obj.optInt("status")){
						array.put(obj);
					}
					break;
				case 3:
					if(4 == obj.optInt("status")){
						array.put(obj);
					}
					break;
				case 4:
					if(6 == obj.optInt("status")){
						array.put(obj);
					}
					break;
				case 5:
					if(6 == obj.optInt("status")){
						array.put(obj);
					}
					break;
				case 6:
					if(7 == obj.optInt("status")){
						array.put(obj);
					}
					break;
				case 7:
					if(8 == obj.optInt("status")){
						array.put(obj);
					}
					break;
				default:
					break;
				}
			}			
		}
		return array;
	}
	//时间
	public static String getStringFromLong(long millis){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(millis);
	    return format.format(date);
	}  
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		if(mListView != null){
			mListView.complete();
		}
		removeDialogCustom();
		if (result == null) {
			return;
		}
		
		if (result instanceof Error) {
			Toast.makeText(StoreOrderActivity.this,((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_OrdersListOrders:
			if(result instanceof JSONObject){
				/*if (((JSONObject) result).has("items")) {
					mItemsArr = ((JSONObject) result).optJSONArray("items");			
					if (mItemsArr.length() > 0 && mItemsArr != null) {
						mListView.setVisibility(View.VISIBLE);
						mFilterArr = mItemsArr;
						mAdapter.notifyDataSetChanged();
					}
				}	*/
				
				JSONArray tempArr = ((JSONObject) result).optJSONArray("items");
				if(mIsRefresh){
					mListView.setVisibility(View.VISIBLE);
					mItemsArr = tempArr;
					mFilterArr = mItemsArr;
					mAdapter.notifyDataSetChanged();
					mIsRefresh = false;
					mPageNo = 1;
				}
				
				if(mIsReadMore){
					mItemsArr = DataLoader.getInstance().joinJSONArray(mItemsArr, tempArr);
					mIsReadMore = false;
					
					if(mSelecterIndex != 0){
						mFilterArr = screening(mSelecterIndex);
					}else{
						mFilterArr = mItemsArr;
					}
				}
				
				if(tempArr != null && tempArr.length() >= mPageSize){
					mListView.setRemoreable(true);
				}else {
					mListView.complete();
					mListView.setRemoreable(false);
				}
				
				
				mAdapter.notifyDataSetChanged();
			}			
			break;
		case TaskOrMethod_OrdersGetPayUrl:
			if(result instanceof JSONObject){		
				if (((JSONObject) result).has("items")) {						
					DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GetWay,
							DataLoader.getInstance().setGetWayTypeParams(
									((JSONObject) result).optJSONObject("items").optString("sign"),
									((JSONObject) result).optJSONObject("items").optString("request_xml"),
									((JSONObject) result).optJSONObject("items").optString("pay_type")),
							StoreOrderActivity.this);										
				}				
			}			
			break;
		case TaskOrMethod_GetWay:
			removeDialogCustom();
			Intent intent = new Intent(StoreOrderActivity.this, WebViewActivity.class);					
			intent.putExtra("result",result.toString());
			StoreOrderActivity.this.startActivityForResult(intent, Configs.StoreOrder_finish);
			break;
			
		case TaskOrMethod_OrdersUpdateOrderStatus:
			if(mListView != null){
				mListView.startRefresh();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Configs.StoreOrder_finish && resultCode == Activity.RESULT_OK){
			showDialogCustom(DIALOG_CUSTOM);
			DataLoader.getInstance().startTaskForResult(
					TaskType.TaskOrMethod_OrdersListOrders, null, this);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((HSESchoolApp)getApplication()).removeActivityStore(this);
	}
}
