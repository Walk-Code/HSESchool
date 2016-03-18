package com.zhuochuang.hsej.store;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.model.Configs;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.WebViewActivity;

public class StoreOrderDetailActivity extends BaseActivity{
	private JSONObject mDataObj;
	private String mean;
	private String[] mStatuStr;
	
	JSONArray mOrderGoodsList;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		setContentView(R.layout.store_my_order_detail);
		setTitleText(R.string.stores_my_order_detail);
		mStatuStr = getResources().getStringArray(R.array.store_pay);
		if(null != getIntent().getStringExtra("OrderData")){			
			try {
				mDataObj = new JSONObject(getIntent().getStringExtra("OrderData"));			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(mDataObj != null && mDataObj.has("orderGoods")){
				mOrderGoodsList = mDataObj.optJSONArray("orderGoods");
			}
			initView();
		}
		
	}
	private void initView() {
		if(mDataObj == null){
			return;
		}
		((TextView)findViewById(R.id.store_name)).setText(mDataObj.optString("shopName"));
		((TextView)findViewById(R.id.store_my_order_count_sum)).setText(getResources().getString(R.string.money_sigh) + new DecimalFormat("#.00").format(mDataObj.optDouble("money")));
		((TextView)findViewById(R.id.store_my_order_sum)).setText(getResources().getString(R.string.money_sigh) + new DecimalFormat("#.00").format(mDataObj.optDouble("deliverFee")));
		
		String [] lable=getResources().getStringArray(R.array.store_order_lable);
		
		((TextView)findViewById(R.id.order_number)).setText(lable[0]+mDataObj.optString("orderNum"));
		((TextView)findViewById(R.id.order_create_time)).setText(lable[1]+getStringFromLong(mDataObj.optLong("createDate")));
		if(0 != mDataObj.optLong("paymentDate")){			
			((TextView)findViewById(R.id.order_pay_time)).setText(lable[2]+getStringFromLong(mDataObj.optLong("paymentDate")));
		}
		if(0 != mDataObj.optLong("deliverDate")){						
			((TextView)findViewById(R.id.order_send_time)).setText(lable[3]+getStringFromLong(mDataObj.optLong("deliverDate")));
		}
		if(0 != mDataObj.optLong("successDate")){									
			((TextView)findViewById(R.id.order_success_time)).setText(lable[4]+getStringFromLong(mDataObj.optLong("successDate")));
		}
		
		String deliverType = mDataObj.optString("means");
		if(!Utils.isTextEmpty(deliverType) && deliverType.equalsIgnoreCase("1")){
			//自取
			findViewById(R.id.group_top).setVisibility(View.GONE);
			findViewById(R.id.order_freight).setVisibility(View.GONE);
			((TextView)findViewById(R.id.store_shopping_delivery)).setText(getResources().getString(R.string.store_buyer_way));
			((TextView)findViewById(R.id.store_address)).setText(getResources().getString(R.string.goods_consignee_shopname) + mDataObj.optString("address"));
		}
		else{
			JSONObject deliverObj = mDataObj.optJSONObject("orderDeliver");
			if(deliverObj != null){
				findViewById(R.id.group_top).setVisibility(View.VISIBLE);
				((TextView)findViewById(R.id.store_consignee)).setText(getResources().getString(R.string.goods_consignee) + deliverObj.optString("name"));
				((TextView)findViewById(R.id.store_consignee_phone)).setText(deliverObj.optString("phone"));
				((TextView)findViewById(R.id.store_address)).setText(getResources().getString(R.string.goods_consignee_shopname2) + deliverObj.optString("address"));
			}
			else{
				findViewById(R.id.group_top).setVisibility(View.GONE);
			}
			findViewById(R.id.order_freight).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.store_shopping_delivery)).setText(getResources().getString(R.string.store_shopping_delivery));
		}
		
		/*if(mDataObj.optString("means").equals("0")){
			((TextView)findViewById(R.id.store_shopping_delivery)).setText(getResources().getString(R.string.store_shopping_delivery));
			JSONObject deliverObj = mDataObj.optJSONObject("orderDeliver");
			if(deliverObj != null){
				findViewById(R.id.group_top).setVisibility(View.VISIBLE);
				((TextView)findViewById(R.id.store_consignee)).setText(getResources().getString(R.string.goods_consignee) + deliverObj.optString("name"));
				((TextView)findViewById(R.id.store_consignee_phone)).setText(deliverObj.optString("phone"));
				((TextView)findViewById(R.id.store_address)).setText(getResources().getString(R.string.goods_consignee_shopname2) + deliverObj.optString("address"));
			}
			else{
				findViewById(R.id.group_top).setVisibility(View.GONE);
			}
		}
		else{
			((TextView)findViewById(R.id.store_shopping_delivery)).setText(getResources().getString(R.string.store_buyer_way));
			findViewById(R.id.group_top).setVisibility(View.GONE);
			((TextView)findViewById(R.id.store_address)).setText(getResources().getString(R.string.goods_consignee_shopname) + mDataObj.optString("address"));
		}*/
		int count = 0;
		if(mOrderGoodsList != null && mOrderGoodsList.length() > 0){
			for(int i = 0; i < mOrderGoodsList.length(); i++){
				JSONObject goods = mOrderGoodsList.optJSONObject(i);
				if(goods != null){
					count += goods.optInt("number", 0);
				}
			}
		}
		((TextView)findViewById(R.id.store_my_order_count)).setText(String.format(getResources().getString(R.string.sum_to_order), count +""));
		
		ListViewForScrollView listView = (ListViewForScrollView) findViewById(R.id.order_goods_list);
		listView.setAdapter(new ContentAdapter(){

			@Override
			public int getCount() {				
				return (mOrderGoodsList == null || mOrderGoodsList.length() == 0) ? 0 : mOrderGoodsList.length();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(null == convertView){
					convertView = LayoutInflater.from(StoreOrderDetailActivity.this).inflate(R.layout.store_order_list_item, null);
				}				
				JSONObject goods = mOrderGoodsList.optJSONObject(position);
				ImageLoader.getInstance().displayImage(goods.optString("goodsImage"),
						(ImageView)convertView.findViewById(R.id.store_my_order_goods_images),ImageLoaderConfigs.displayImageOptionsBg);
				((TextView)convertView.findViewById(R.id.store_my_order_goods_name)).setText(goods.optString("goodsName"));
				((TextView)convertView.findViewById(R.id.store_my_order_goods_price)).setText(getResources().getString(R.string.money_sigh)+new DecimalFormat("#.00").format(goods.optDouble("price")));
				((TextView)convertView.findViewById(R.id.store_my_order_goods_sum)).setText("X"+goods.optString("number"));						
				((TextView)convertView.findViewById(R.id.store_my_order_goods_color)).setText(getResources().getString(R.string.goods_color)+goods.optString("colour"));
				((TextView)convertView.findViewById(R.id.store_my_order_goods_size)).setText(getResources().getString(R.string.goods_size)+goods.optString("spec"));				
				return convertView;
			}
			
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mOrderGoodsList == null || mOrderGoodsList.length() == 0){
					return;
				}
				JSONObject goods = mOrderGoodsList.optJSONObject(arg2);
				Intent intent = new Intent(StoreOrderDetailActivity.this, StoreGoodsMoreDetailsActivity.class);
				intent.putExtra("shopId", goods.optString("goodsId"));
				startActivity(intent);
			}
		});
		
		findViewById(R.id.store_name).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StoreOrderDetailActivity.this,StorePersonActivity.class);
				intent.putExtra("shopId",mDataObj.optString("shopId"));
				startActivity(intent);
			}
		});
		changeButStatus(mDataObj.optInt("status"));
		findViewById(R.id.store_my_order_to_service).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialogCustom(DIALOG_CUSTOM);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_AfterSaleService,
						DataLoader.getInstance().setAfterSaleServiceParams(mDataObj.optString("shopId")), StoreOrderDetailActivity.this);
			}
		});
		findViewById(R.id.store_my_order_confirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				Intent intent = null;
				switch (mDataObj.optInt("status")) {
				case 1:	
					showDialogCustom(DIALOG_CUSTOM);
					DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_OrdersGetPayUrl,
							DataLoader.getInstance().setOrderNumTypeParams(mDataObj.optString("orderNum")),StoreOrderDetailActivity.this);
					break;						
				case 5:
				    /*intent = new Intent(StoreOrderActivity.this, StoreOrderDetailActivity.class);	
				    intent.putExtra("GoodsData", orderGoods.toString());*/
					new AlertDialog.Builder(StoreOrderDetailActivity.this).setCancelable(false)
					.setMessage(getResources().getString(R.string.stores_confirm_receipt))
					.setPositiveButton(getResources().getString(R.string.confirm),new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							String mean = "";
							if(mDataObj.has("means")){
								mean = mDataObj.optString("means");
							}
							showDialogCustom(DIALOG_CUSTOM);
							DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_OrdersUpdateOrderStatus,
									DataLoader.getInstance().setUpdateOrderStatusTypeParams(mean,mDataObj.optString("orderNum"),"6"), StoreOrderDetailActivity.this);
						}
					}).setNegativeButton(getResources().getString(R.string.store_shopping_car_cancle),null).show();
					break;
				case 6:
				    intent = new Intent(StoreOrderDetailActivity.this, StoreGoodsListEvaluateActivity.class);	
				    //intent.putExtra("GoodsData", mDataObj.optJSONArray("orderGoods").toString());
				    intent.putExtra("orderNum", mDataObj.optString("orderNum"));
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
	}
	public void changeButStatus(int status){
		
		switch (status) {
		case 1:
			if(2 == mDataObj.optInt("payType")){					
				((TextView)findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[1]);
				((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
				((TextView)findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
			}
			else{					
				((TextView)findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[0]);
				((TextView)findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
				((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
			}
			break;
		case 2:							
			((TextView)findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[1]);
			((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
			((TextView)findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
			break;
			//待配送
		case 3:			
			((TextView)findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[2]);
			//((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(R.color.black);
			((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
			break;
		case 4:	
			((TextView)findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[3]);
			((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
			/*((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
			((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));*/
			break;
		case 5:
			//确认收货
			((TextView)findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[5]);
			((TextView)findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.bg_store_black);
			((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#212121"));
			
			findViewById(R.id.store_my_order_to_service).setVisibility(View.VISIBLE);
			//((TextView)findViewById(R.id.store_my_order_to_service)).setTextColor(Color.parseColor("#898989"));
			break;
		case 6:
			//评价界面
			((TextView)findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_goods_to_evaluate));
			((TextView)findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
			((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
			
			findViewById(R.id.store_my_order_to_service).setVisibility(View.VISIBLE);
			//((TextView)findViewById(R.id.store_my_order_to_service)).setTextColor(Color.parseColor("#898989"));
			break;
		case 7:
			//已取消 已退款，退款中
			((TextView)findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[7]);
			((TextView)findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
			((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
			
			findViewById(R.id.store_my_order_to_service).setVisibility(View.VISIBLE);
			//((TextView)findViewById(R.id.store_my_order_to_service)).setTextColor(Color.parseColor("#898989"));
			break;
		case 8:
			//已退款
			((TextView)findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[8]);
			((TextView)findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
			((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
			
			findViewById(R.id.store_my_order_to_service).setVisibility(View.VISIBLE);
			//((TextView)findViewById(R.id.store_my_order_to_service)).setTextColor(Color.parseColor("#898989"));
			break;
		case 9:
			//已评价
			((TextView)findViewById(R.id.store_my_order_confirm)).setText(mStatuStr[9]);
			((TextView)findViewById(R.id.store_my_order_confirm)).setBackgroundResource(R.drawable.shape_store_car_bg);
			((TextView)findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
			
			findViewById(R.id.store_my_order_to_service).setVisibility(View.VISIBLE);
			//((TextView)findViewById(R.id.store_my_order_to_service)).setTextColor(Color.parseColor("#898989"));
			break;		
		default:
			break;
		}
		
	}
	public static String getStringFromLong(long millis){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(millis);
	    return format.format(date);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Configs.StoreOrder_finish && resultCode == Activity.RESULT_OK){
			StoreOrderDetailActivity.this.finish();
		}
	}
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		removeDialogCustom();
		if (result == null) {
			return;
		}
		
		if (result instanceof Error) {
			Toast.makeText(StoreOrderDetailActivity.this,((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch(type){
		case TaskOrMethod_OrdersUpdateOrderStatus:
			if(result instanceof JSONObject){
				if(1 == ((JSONObject)result).optInt("result"))
				finish();
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
							StoreOrderDetailActivity.this);										
				}				
			}			
			break;
		case TaskOrMethod_GetWay:
			removeDialogCustom();
			Intent intent = new Intent(StoreOrderDetailActivity.this, WebViewActivity.class);					
			intent.putExtra("result",result.toString());
			startActivityForResult(intent, Configs.StoreOrder_finish);
			break;
		case TaskOrMethod_AfterSaleService:
			if(result instanceof JSONObject){
				JSONObject resultObj = (JSONObject)result;
				if(resultObj != null && resultObj.has("items")){
					JSONObject item = resultObj.optJSONObject("items");
					
					intent = new Intent(StoreOrderDetailActivity.this, WebViewActivity.class);
					intent.putExtra("result", item.optString("intro"));
					intent.putExtra("title", item.optString("name"));
					startActivity(intent);
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
