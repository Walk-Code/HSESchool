package com.zhuochuang.hsej.store;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;

public class StoreTodaySpecialActivity extends BaseActivity{
	private PullToRefreshListView mListView;
	private JSONArray mItemArr;
	private ContentAdapter mAdapter;
	
		@Override
		protected void onCreate(Bundle arg0) {
			super.onCreate(arg0);
			((HSESchoolApp)getApplication()).addActivityStore(this);
			setContentView(R.layout.store_todayspecial);
			setTitleText(R.string.today_special);
			initView();
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_HotRecommendlistGoods, null, this);
		}

		private void initView() {
			mListView = (PullToRefreshListView) findViewById(R.id.pullToListView_today_special);
			mListView.setAdapter(mAdapter = new ContentAdapter(){
				@Override
				public int getCount() {
					if(null != mItemArr){
						return mItemArr.length();
					}
					return 0;
				}

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					if(convertView == null){						
						convertView = LayoutInflater.from(StoreTodaySpecialActivity.this).inflate(R.layout.store_special_item, null);						
					}
					JSONObject obj = mItemArr.optJSONObject(position);	
					JSONObject goods = obj.optJSONObject("goods");
									
					((TextView)convertView.findViewById(R.id.tv_details)).setText(goods.optString("name"));
					((TextView) convertView.findViewById(R.id.tv_prices)).setText(getResources().getString(R.string.money_sigh)+new DecimalFormat("#.00").format(goods.optDouble("price")));
					((TextView)convertView.findViewById(R.id.tv_mark_price)).setText(getResources().getString(R.string.money_sigh)+new DecimalFormat("#.00").format(goods.optDouble("money")));
					((TextView)convertView.findViewById(R.id.tv_mark_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					ImageLoader.getInstance().displayImage(goods.optString("image"), (ImageView)convertView.findViewById(R.id.good_image),ImageLoaderConfigs.displayImageOptionsBg);
					return convertView;
				}				
			}); 
			
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					JSONObject obj = mItemArr.optJSONObject(arg2-1);
					Intent intent = new Intent(StoreTodaySpecialActivity.this, StoreGoodsMoreDetailsActivity.class);
					intent.putExtra("shopId", obj.optJSONObject("goods").optString("id"));
					startActivity(intent);
				}
			});
			
			mListView.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_HotRecommendlistGoods, null, StoreTodaySpecialActivity.this);
				}
			});
			
			mListView.startRefresh();
		}
		
		@Override
		public void taskFinished(TaskType type, Object result, boolean isHistory) {
			super.taskFinished(type, result, isHistory);
			if(mListView != null){
				mListView.complete();
			}
			
			if(null == result){
				return;
			}
			
			if(result instanceof Error){
				Toast.makeText(StoreTodaySpecialActivity.this, ((Error)result).getMessage(),Toast.LENGTH_SHORT).show();
			}
			if(type == TaskType.TaskOrMethod_HotRecommendlistGoods){
				if(result instanceof JSONObject){
					if(((JSONObject)result).has("items")){
						mItemArr = ((JSONObject)result).optJSONArray("items");
						if(null != mItemArr && 0 < mItemArr.length()){							
							mAdapter.notifyDataSetChanged();
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
