package com.zhuochuang.hsej.store;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.ListViewForScrollView;
import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.PullToRefreshListView.OnRemoreListener;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.WebViewActivity;

public class StoreOrderStatusActivity extends BaseActivity{
	private PullToRefreshListView mListView;
	private ContentAdapter mAdapter;
	private JSONArray mDataArr;
	private String mItem[];
	private String mItems[];
	private String[] mStatuStr;
	
	private String[] mStatuscree;	
	private String status;
	Handler mHandler;
	
	int mPageNum = 1;
	boolean mIsReadMore = false;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		setContentView(R.layout.store_order_status);
		mItem = getResources().getStringArray(R.array.store_order_selection);//store_order_status
		mItems = getResources().getStringArray(R.array.store_order_status);
		mStatuStr = getResources().getStringArray(R.array.store_pay);
		
		mStatuscree =  getResources().getStringArray(R.array.store_orders_scree);
		
		if(getIntent().hasExtra("title")){
			setTitleText(getIntent().getStringExtra("title"));
		}
		status = getIntent().getStringExtra("orderStatus");
		initListView();
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_OrdersListOrders,DataLoader.getInstance().getListOrdersTypeParams(status,""), StoreOrderStatusActivity.this);			
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case EventManager.EventType_EvaluateFinish:
					if(mListView != null){
						mListView.startRefresh();
					}
					break;

				default:
					break;
				}
			}
			
		});
	}

	private void initListView() {
		mListView = (PullToRefreshListView) findViewById(R.id.pullToListView_order);
		mListView.setAdapter(mAdapter = new ContentAdapter(){

			@Override
			public int getCount() {			
				return null != mDataArr ? mDataArr.length() : 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = LayoutInflater.from(StoreOrderStatusActivity.this).inflate(R.layout.store_my_order_list_item, null);					
				}
			    final JSONObject obj = mDataArr.optJSONObject(position);
				final JSONArray orderGoods = obj.optJSONArray("orderGoods");			
				((TextView)convertView.findViewById(R.id.store_name)).setText(obj.optString("shopName"));
				((TextView)convertView.findViewById(R.id.store_pay_status)).setText(returnStatus(obj.optInt("status", 0), mStatuscree, obj.optString("payType")));
				((TextView)convertView.findViewById(R.id.store_my_order_number)).setText(getResources()
						.getString(R.string.order_number)+obj.optString("orderNum"));				
				((TextView)convertView.findViewById(R.id.store_my_order_time)).setText(getResources()
						.getString(R.string.order_time)+getStringFromLong(obj.optLong("createDate")));								
				ListViewForScrollView listView = (ListViewForScrollView) convertView.findViewById(R.id.order_list);
				
				convertView.findViewById(R.id.store_name).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(StoreOrderStatusActivity.this, StorePersonActivity.class);
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
							convertView = LayoutInflater.from(StoreOrderStatusActivity.this).inflate(R.layout.store_order_list_item, null);					
						}
						JSONObject goods = orderGoods.optJSONObject(position);
						ImageLoader.getInstance().displayImage(goods.optString("goodsImage"),
								(ImageView)convertView.findViewById(R.id.store_my_order_goods_images),ImageLoaderConfigs.displayImageOptionsBg);
						((TextView)convertView.findViewById(R.id.store_my_order_goods_name)).setText(goods.optString("goodsName"));
						((TextView)convertView.findViewById(R.id.store_my_order_goods_price)).setText(getResources().getString(R.string.money_sigh)+new DecimalFormat("#.00").format(goods.optDouble("price")));
						((TextView)convertView.findViewById(R.id.store_my_order_goods_sum)).setText("X"+goods.optString("number"));
						((TextView)convertView.findViewById(R.id.store_deliverytype)).setText(returnDeliveryType(obj.optString("payType")));
						if(!("null".equals(goods.optString("colour")))){							
							((TextView)convertView.findViewById(R.id.store_my_order_goods_color)).setText(getResources().getString(R.string.goods_color)+goods.optString("colour"));
						}
						if(!("null".equals(goods.optString("spec")))){							
							((TextView)convertView.findViewById(R.id.store_my_order_goods_size)).setText(getResources().getString(R.string.goods_size)+goods.optString("spec"));
						}
						return convertView;
					}
					
				});
				
				listView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(StoreOrderStatusActivity.this,StoreOrderDetailActivity.class);
						intent.putExtra("OrderData", obj.toString());
						startActivity(intent);						
					}					
				});
				
				switch (obj.optInt("status", 0)) {
				case 1:
					if(obj.optString("payType").equals("1")){
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_businesses_to_be_confirmed));
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
					}
					else{
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_confirm));
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
					}
					break;
				case 2:
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_confirm));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
					break;
				case 3:
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_wait_delivery));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
					break;
				case 4:
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.mycenter_storeorder_cell2));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
					break;
				case 5:
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.store_check_goods));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#212121"));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.bg_store_black);
					break;
				case 6:
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_goods_to_evaluate));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
					break;
				case 7:
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_refund_order));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
					break;
				case 8:
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_alread_order));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
					break;
				}
				final int Orderstatus = obj.optInt("status");
				convertView.findViewById(R.id.store_my_order_confirm).setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						switch(Orderstatus){
						case 1:	
							showDialogCustom(DIALOG_CUSTOM);
							DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_OrdersGetPayUrl,
									DataLoader.getInstance().setOrderNumTypeParams(obj.optString("orderNum")),StoreOrderStatusActivity.this);
							break;
						case 2:
							
							break;
						case 5:
							/*Intent intent = new Intent(StoreOrderStatusActivity.this,StoreOrderDetailActivity.class);
							intent.putExtra("OrderData", obj.toString());
							startActivity(intent);*/
							//确认收货
							new AlertDialog.Builder(StoreOrderStatusActivity.this).setCancelable(false)
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
	    									DataLoader.getInstance().setUpdateOrderStatusTypeParams(mean,obj.optString("orderNum"),"6"), StoreOrderStatusActivity.this);
	    						}
	    					}).setNegativeButton(getResources().getString(R.string.store_shopping_car_cancle),null).show();
							break;
						case 6:
							Intent intent1 = new Intent(StoreOrderStatusActivity.this, StoreGoodsListEvaluateActivity.class);
							//intent1.putExtra("GoodsData", orderGoods.toString());
							intent1.putExtra("orderNum", obj.optString("orderNum"));
							startActivity(intent1);
							break;
						default:
							break;
						}			
					}						
				});
				return convertView;
			}
			
		});
		
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				mPageNum = 1;
				mIsReadMore = false;
				HashMap<String, Object> params=new HashMap<String, Object>();
				params.put("status", status);
				params.put("pageNo", mPageNum);
				DataLoader.getInstance().startTaskForResult(
						TaskType.TaskOrMethod_OrdersListOrders,params, StoreOrderStatusActivity.this);						
			}
		});
		
		mListView.setOnRemoreListener(new OnRemoreListener() {
			@Override
			public void onRemore() {
				mPageNum++;
				mIsReadMore = true;
				HashMap<String, Object> params=new HashMap<String, Object>();
				params.put("status", status);
				params.put("pageNo", mPageNum);
				DataLoader.getInstance().startTaskForResult(
						TaskType.TaskOrMethod_OrdersListOrders,params, StoreOrderStatusActivity.this);	
			}
		});
		mListView.setRemoreable(false);
		mListView.startRefresh();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(null != mListView){
			mListView.startRefresh();
		}
	}
	public static String getStringFromLong(long millis){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(millis);
	    return format.format(date);
	}  
	
	public String returnStatus(int status, String [] data, String payType){
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
				statu = data[0];
			}
			break;
		case 3:
			statu = data[1];
			break;
		case 4:
			statu = data[2];
			break;
		case 5:
			statu = data[3];
			break;
		case 6:
			statu = data[4];
			break;
		case 7:
			statu = data[5];
			break;
		case 8:
			break;
		case 9:
			statu = data[4];
			break;
		}
		return statu;
		
		/*if(1 == statusNumber){
			return getResources().getString(R.string.stores_new_order);
		}
		else if(2 == statusNumber){
			if(payType.equalsIgnoreCase("2")){
				return "";
			}
			else{
				return data[statusNumber];
			}
		}
		else{
			return data[statusNumber];
		}
		return "";*/
	}
	
	public String returnDeliveryType(String means){
		if(means.equals("1")){
			return getResources().getString(R.string.store_pay_online);
		}else{
			return getResources().getString(R.string.store_goods_cash);
		}
	}
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		if(null != mListView){
			mListView.complete();
		}
		
		if (result == null) {
			removeDialogCustom();
			return;
		}
		
		if (result instanceof Error) {
			removeDialogCustom();
			Toast.makeText(StoreOrderStatusActivity.this,((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch(type){
		case TaskOrMethod_OrdersListOrders:
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				JSONArray temp = ((JSONObject)result).optJSONArray("items");
				if(temp != null && temp.length() > 9){
					mListView.setRemoreable(true);
				}else{
					mListView.setRemoreable(false);
				}
				if(mIsReadMore){
					mIsReadMore = false;
					mDataArr = DataLoader.getInstance().joinJSONArray(mDataArr, temp);
				}else{
					mDataArr = temp;
				}
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
			}
		break;
		
		case TaskOrMethod_OrdersGetPayUrl:
			if(result instanceof JSONObject && ((JSONObject) result).has("items")){
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GetWay,
						DataLoader.getInstance().setGetWayTypeParams(
								((JSONObject) result).optJSONObject("items").optString("sign"),
								((JSONObject) result).optJSONObject("items").optString("request_xml"),
								((JSONObject) result).optJSONObject("items").optString("pay_type")),
						StoreOrderStatusActivity.this);
			}
			else{
				removeDialogCustom();
			}
			break;
		case TaskOrMethod_GetWay:
			removeDialogCustom();
			Intent intent = new Intent(StoreOrderStatusActivity.this, WebViewActivity.class);					
			intent.putExtra("result",result.toString());
			StoreOrderStatusActivity.this.startActivity(intent);
			break;
		case TaskOrMethod_OrdersUpdateOrderStatus:
			removeDialogCustom();
			if(mListView != null){
				mListView.startRefresh();
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
