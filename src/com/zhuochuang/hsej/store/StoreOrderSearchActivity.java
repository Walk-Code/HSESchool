package com.zhuochuang.hsej.store;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.layout.ListViewForScrollView;
import com.layout.PullToRefreshListView;
import com.model.Configs;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.WebViewActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class StoreOrderSearchActivity extends BaseActivity{

	ListView mListView;
	ContentAdapter mAdapter;
	//StorePupopWindow pupopWindow;
	
	String[] mStatuStr;
	String[] mStatuscree;
	
	JSONArray mItemsArr;
	JSONArray mFilterArr;
	
	String mSearchStr;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mMainLayout.findViewById(R.id.navbar_base).setVisibility(View.GONE);
		setContentView(R.layout.store_order_search_activity);
		mStatuStr = getResources().getStringArray(R.array.store_pay);
		mStatuscree = getResources().getStringArray(R.array.store_orders_scree);
		initListView();
		((EditText) findViewById(R.id.editview)).setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_SEARCH || (event!=null && event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
					 String str = ((EditText) findViewById(R.id.editview)).getText().toString();
					 if(Utils.isTextEmpty(str)){
						 Toast.makeText(StoreOrderSearchActivity.this, getResources().getString(R.string.search_hint), Toast.LENGTH_SHORT).show();
						 return true;
					 }
					 mSearchStr = str;
					 Utils.removeSoftKeyboard(StoreOrderSearchActivity.this);
					 showDialogCustom(DIALOG_CUSTOM);
					 getData();
					 return true;
	            }
				return true;
			}
		});
	}
	
	private void getData(){
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_OrdersListOrders,DataLoader.getInstance().getListOrdersTypeParams("", mSearchStr, 1, 1000), StoreOrderSearchActivity.this);			
	}
	
	public void onCancelClick(View view){
		finish();
	}
	
	public void backgroundAlpha(float bgAlpha){  
        WindowManager.LayoutParams lp = getWindow().getAttributes();  
        lp.alpha = bgAlpha; 
        getWindow().setAttributes(lp);  
	}
	
	public static String getStringFromLong(long millis){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(millis);
	    return format.format(date);
	}
	
	private void initListView(){
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setAdapter(mAdapter = new ContentAdapter() {

			@Override
			public int getCount() {
				if(mFilterArr != null && mFilterArr.length() > 0){
					return mFilterArr.length();
				}
				return 0;
			}

			@SuppressLint("ResourceAsColor") @Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = LayoutInflater.from(StoreOrderSearchActivity.this).inflate(R.layout.store_my_order_list_item, null);					
				}
			    final JSONObject obj = mFilterArr.optJSONObject(position);
			    if(obj != null){
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
							Intent intent = new Intent(StoreOrderSearchActivity.this, StorePersonActivity.class);
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
								convertView = LayoutInflater.from(StoreOrderSearchActivity.this).inflate(R.layout.store_order_list_item, null);					
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
							Intent intent = new Intent(StoreOrderSearchActivity.this,StoreOrderDetailActivity.class);
							intent.putExtra("OrderData", obj.toString());
							startActivity(intent);						
						}					
					});
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#212121"));
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_bg);
					
					switch (obj.optInt("status", 0)) {
					case 1:
						if(2 == obj.optInt("payType")){					
							((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[1]);
							((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
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
						break;
						//待配送
					case 3:			
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[2]);
						//((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(R.color.black);
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
						break;
					case 4:	
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[3]);
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
						/*((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));*/
						break;
					case 5:
						//确认收货
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[5]);
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#212121"));
						convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.bg_store_black);
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
						break;
					case 8:
						//已退款
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[8]);
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
						//((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
						//((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
						break;
					case 9:
						//已评价
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[9]);
						((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
						/*((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));*/
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
										DataLoader.getInstance().setOrderNumTypeParams(obj.optString("orderNum")),StoreOrderSearchActivity.this);
								break;						
							case 5:
							    /*intent = new Intent(StoreOrderSearchActivity.this, StoreOrderDetailActivity.class);	
							    intent.putExtra("GoodsData", orderGoods.toString());*/
								new AlertDialog.Builder(StoreOrderSearchActivity.this).setCancelable(false)
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
		    									DataLoader.getInstance().setUpdateOrderStatusTypeParams(mean,obj.optString("orderNum"),"6"), StoreOrderSearchActivity.this);
		    						}
		    					}).setNegativeButton(getResources().getString(R.string.store_shopping_car_cancle),null).show();
								break;
							case 6:
							    intent = new Intent(StoreOrderSearchActivity.this, StoreGoodsListEvaluateActivity.class);	
							    //intent.putExtra("GoodsData", orderGoods.toString());
							    intent.putExtra("orderNum", obj.optString("orderNum"));
								break;
							case 9:
								//查看评价
								//intent = new Intent(StoreOrderSearchActivity.this, StoreGoodsCommentListActivity.class);
								
								//intent.putExtra("goodsId", value);
							default:
								break;
							}
							if(null != intent){
								startActivity(intent);
							}
					   }						
					});
			    }
				return convertView;
			}
		});
	}
	
	public String returnDeliveryType(String means){
		if(means.equals("1")){
			return getResources().getString(R.string.store_pay_online);
		}
		else{
			return getResources().getString(R.string.store_goods_cash);
		}
	}
	
	//返回订单状态
	public String returnStatus(int status, String payType){
		String statu = "";
		switch(status){
		case 1:
			statu = getResources().getString(R.string.stores_new_order);//mStatuscree[0];
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
			//statu = mStatuscree[5];
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Configs.StoreOrder_finish && resultCode == Activity.RESULT_OK){
			showDialogCustom(DIALOG_CUSTOM);
			getData();
		}
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
			Toast.makeText(StoreOrderSearchActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_OrdersListOrders:
			if(result instanceof JSONObject && ((JSONObject) result).has("items")){
				mFilterArr = ((JSONObject) result).optJSONArray("items");
				if(mFilterArr == null || mFilterArr.length() == 0){
					new AlertDialog.Builder(StoreOrderSearchActivity.this) 
					.setCancelable(false)
				 	.setMessage(R.string.ordersearch_null)
				 	.setPositiveButton(getResources().getString(R.string.confirm), null)
				 	.show();
					return;
				}
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
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
							StoreOrderSearchActivity.this);										
				}				
			}			
			break;
		case TaskOrMethod_GetWay:
			removeDialogCustom();
			Intent intent = new Intent(StoreOrderSearchActivity.this, WebViewActivity.class);					
			intent.putExtra("result",result.toString());
			startActivityForResult(intent, Configs.StoreOrder_finish);
			break;
			
		case TaskOrMethod_OrdersUpdateOrderStatus:
			getData();
			break;
		default:
			break;
		}
	}
}
