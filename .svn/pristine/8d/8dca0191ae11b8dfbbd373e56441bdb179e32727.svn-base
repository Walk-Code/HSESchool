package com.zhuochuang.hsej.store;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.model.ImageLoaderConfigs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.a.a.a.a.e;
import com.util.Utils;
import com.zhuochuang.hsej.R;

public class ServiceZoneAdapter extends BaseAdapter{
	private LayoutInflater inflater = null;
	private JSONArray mGoodsList = null;
	private DisplayMetrics mWindowManager=null;
	private Context mContext;
	private int mRows = 2;
	private String mDataType;
	
	public ServiceZoneAdapter(JSONArray mGoodsList,Context c) {
		super();
		this.mGoodsList = mGoodsList;
		inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWindowManager = c.getResources().getDisplayMetrics();
		mContext = c;
	}
	
	public void setData(JSONArray data, String dataType){
		this.mGoodsList = null;
		this.mGoodsList = data;
		this.mDataType = dataType;
	}

	@Override
	public int getCount() {
		if(StoreDetailActivity.isGridView){
			if(mGoodsList == null) return 0;
			
			return (mGoodsList.length() / mRows) + (mGoodsList.length() % mRows > 0 ? 1 : 0);
		}
		return mGoodsList == null ? 0 : mGoodsList.length();
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			if(StoreDetailActivity.isGridView){
				convertView = inflater.inflate(R.layout.listcell_store_grid, parent, false);
			}else {
				convertView = inflater.inflate(R.layout.store_list_layout, parent, false);
			}
		}
		
		if(StoreDetailActivity.isGridView){
			for(int i = 0; i < mRows; i++){
				JSONObject goodsList = mGoodsList.optJSONObject(position * mRows + i);
				LinearLayout contentView = (LinearLayout) convertView;
				setItemView(contentView.getChildAt(i % 2 == 0 ? 0 : 1), goodsList);
			}
		}else {
			JSONObject goodsList = mGoodsList.optJSONObject(position);
			setItemView(convertView, goodsList);
		}
		
		
		return convertView;
	}
	
	public void setItemView(View v, JSONObject obj){
		if(v == null){
			return;
		}
		ImageView image = ((ImageView)v.findViewById(R.id.good_image)); 
		if(obj == null){
			image.setVisibility(View.INVISIBLE);
			return;
		}
		image.setVisibility(View.VISIBLE);
		
		((TextView)v.findViewById(R.id.tv_details)).setText(obj.optString("name"));
		((TextView)v.findViewById(R.id.tv_price)).setText(mContext.getResources().getString(R.string.money_sigh)+new DecimalFormat("#.00").format(obj.optDouble("price")));
		((TextView)v.findViewById(R.id.tv_stock)).setText(mContext.getResources().getString(R.string.goods_already_sold)+obj.optString("sales")+ mContext.getResources().getString(R.string.goods_inventory2));
		if(mDataType.equalsIgnoreCase("newGoods")){
			String createDate = "";
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				createDate = dateFormat.format(new Date(obj.optLong("createDate")));
			} catch (Exception e) {
				// TODO: handle exception
			}
			((TextView)v.findViewById(R.id.tv_time)).setText(createDate);
		}
		
		if(obj.has("money")){
			if(StoreDetailActivity.isGridView){
				((TextView)v.findViewById(R.id.tv_mark_price)).setText("");
			}
			else{
				((TextView)v.findViewById(R.id.tv_mark_price)).setText(mContext.getResources().getString(R.string.money_sigh)+new DecimalFormat("#.00").format(obj.optDouble("money")));
				((TextView)v.findViewById(R.id.tv_mark_price)).getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
			}
		}
		ImageLoader.getInstance().displayImage(obj.optString("image"), image, ImageLoaderConfigs.displayImageOptionsBg);
		
		if(StoreDetailActivity.isGridView){
			image.setLayoutParams(new LinearLayout.LayoutParams(
					mWindowManager.widthPixels / 2 -Utils.dipPx(mContext, 31),
					mWindowManager.widthPixels / 2 -Utils.dipPx(mContext, 31)));
		}else {
			v.findViewById(R.id.line).setVisibility(View.VISIBLE);
		}
	}

	/*@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vIewHolder;
		vIewHolder = new ViewHolder();
		if(convertView == null){
			if(StoreDetailActivity.isGridView){
				convertView = inflater.inflate(R.layout.store_grid_layout, parent, false);
				vIewHolder.vImageGoods = (ImageView) convertView.findViewById(R.id.good_image);
				vIewHolder.vImageGoods.setLayoutParams(new LinearLayout.LayoutParams((mWindowManager.widthPixels-vIewHolder.vImageGoods.getWidth())/2,
						(mWindowManager.widthPixels-vIewHolder.vImageGoods.getWidth())/2));	
				
				
			}else{
				convertView = inflater.inflate(R.layout.store_list_layout, parent, false);
				vIewHolder.vImageGoods = (ImageView) convertView.findViewById(R.id.good_image);
				vIewHolder.vImageGoods.setLayoutParams(new LinearLayout.LayoutParams((mWindowManager.widthPixels-vIewHolder.vImageGoods.getWidth())/4,
						(mWindowManager.widthPixels-vIewHolder.vImageGoods.getWidth())/4));				
			}			
			vIewHolder.vDetails = (TextView) convertView.findViewById(R.id.tv_details);
			vIewHolder.vPrice = (TextView) convertView.findViewById(R.id.tv_price);
			vIewHolder.vMarkPrice = (TextView) convertView.findViewById(R.id.tv_mark_price);			
			vIewHolder.vStock = (TextView) convertView.findViewById(R.id.tv_stock);
			convertView.setTag(vIewHolder);
		}else{
			vIewHolder = (ViewHolder) convertView.getTag();
		}
		JSONObject goodsList = mGoodsList.optJSONObject(position);
		if(goodsList == null){
			return convertView;
		}		
				
		vIewHolder.vDetails.setText(goodsList.optString("name"));
		vIewHolder.vPrice.setText(mContext.getResources().getString(R.string.money_sigh)+new DecimalFormat("#.00").format(goodsList.optDouble("price")));
		vIewHolder.vStock.setText(mContext.getResources().getString(R.string.goods_already_sold)+goodsList.optString("sales")+"件");
		if(goodsList.has("money")){
			vIewHolder.vMarkPrice.setText(mContext.getResources().getString(R.string.money_sigh)+new DecimalFormat("#.00").format(goodsList.optDouble("money")));
			vIewHolder.vMarkPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
		}
		ImageLoader.getInstance().displayImage(goodsList.optString("image"), vIewHolder.vImageGoods, ImageLoaderConfigs.displayImageOptionsBg);
		return convertView;
	}*/
	class ViewHolder{
		TextView vDetails,vPrice,vStock,vMarkPrice;
		ImageView vImageGoods;		
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
