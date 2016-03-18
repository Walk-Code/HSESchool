package com.zhuochuang.hsej.store;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.photoalbum.Bimp;
import com.model.Configs;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;

public class StoreGoodsListEvaluateActivity extends BaseActivity{
	private PullToRefreshListView mListView;
	private ContentAdapter mAdapter;
	private JSONArray mDataArr;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		setContentView(R.layout.store_order_status);
		setTitleText(getResources().getString(R.string.stores_goods_evaluate));
		initView();
		/*try {
			mDataArr = new JSONArray(getIntent().getStringExtra("GoodsData"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private void getData(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("orderNum", getIntent().getStringExtra("orderNum"));
		params.put("pageNo", 1);
		params.put("pageSize", 100);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_OrdersListOrders, params, StoreGoodsListEvaluateActivity.this);
	}

	private void initView() {
		mListView = (PullToRefreshListView) findViewById(R.id.pullToListView_order);
		mListView.setAdapter(mAdapter = new ContentAdapter(){

			
			@Override
			public int getCount() {	
				if(mDataArr != null && mDataArr.length() > 0){
					return mDataArr.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView,ViewGroup parent) {
				if(null == convertView){
					convertView = LayoutInflater.from(StoreGoodsListEvaluateActivity.this).inflate(R.layout.store_evaluate_itme,null);
				}
				final JSONObject obj = mDataArr.optJSONObject(position);
				if(obj.optBoolean("isEvaluate", false)){
					convertView.findViewById(R.id.store_my_order_confirm).setEnabled(false);
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_bg);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_goods_evaluate_already));
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#898989"));
				}
				else{
					convertView.findViewById(R.id.store_my_order_confirm).setEnabled(true);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setText(getResources().getString(R.string.stores_goods_evaluates));
					convertView.findViewById(R.id.store_my_order_confirm).setBackgroundResource(R.drawable.shape_store_car_pay_bg);
					((TextView)convertView.findViewById(R.id.store_my_order_confirm)).setTextColor(Color.parseColor("#DD514F"));
				}
				((TextView)convertView.findViewById(R.id.product_name)).setText(obj.optString("goodsName"));
				ImageLoader.getInstance().displayImage(obj.optString("goodsImage"),(ImageView)convertView.findViewById(R.id.product_image)
							,ImageLoaderConfigs.displayImageOptionsBg);
				
				convertView.findViewById(R.id.store_my_order_confirm).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(StoreGoodsListEvaluateActivity.this, StorePublishEvaluateActivity.class);
						intent.putExtra("goodId", obj.optString("id"));
						intent.putExtra("goodsName", obj.optString("goodsName"));
						intent.putExtra("img", obj.optString("goodsImage"));
						startActivityForResult(intent, Configs.REQUESTCODE_Evaluate);
					}
				});
				
				return convertView;
			}			
		});
		
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getData();
			}
		});
		mListView.startRefresh();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case Configs.REQUESTCODE_Evaluate:
				//StoreGoodsEvaluateActivity.this.finish();
				if(null != mListView){
					mListView.startRefresh();
				}
				break;
			}
	    }
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((HSESchoolApp)getApplication()).removeActivityStore(this);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		if(null != mListView){
			mListView.complete();
		}
		if (result == null) {
			return;
		}
		if (result instanceof Error) {
			Toast.makeText(StoreGoodsListEvaluateActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_OrdersListOrders:
			if(result instanceof JSONObject && ((JSONObject) result).has("items")){
				JSONArray temp = ((JSONObject) result).optJSONArray("items");
				if(temp != null && temp.length() > 0){
					try {
						mDataArr = temp.optJSONObject(0).optJSONArray("orderGoods");
					} 
					catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			if(mAdapter != null){
				mAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}
}
