package com.zhuochuang.hsej.store;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.model.ImageLoaderConfigs;
import com.model.data.GroupInfo;
import com.model.data.ProductInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.Utils;
import com.zhuochuang.hsej.R;

public class ShopCartListViewAdapter extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Context mContext;
	private ModifyCountInterface modifyCountInterface;
	private CheckInterface checkInterface;
	public static final int SHOPCART_FLAG_GROUP = 0;
	public static final int SHOPCART_FLAG_CHILDREN = 1;
	public static final String SHOPCART_TYPE = "type";
	public static final String SHOPCART_DATA = "data";
	public static final String SHOPCART_PARENT_ID = "parent_id";
	public static final String SHOPCART_PARENT_POSITION = "parent_position";
		
	public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
		this.modifyCountInterface = modifyCountInterface;
	}

	public void setCheckInterface(CheckInterface checkInterface) {
		this.checkInterface = checkInterface;
	}

	public ShopCartListViewAdapter(List<Map<String, Object>> list, Context context) {
		super();
		this.list = list;
		this.mContext = context;
	}
	
	public void setData(List<Map<String, Object>> list){
		this.list = list;
	}

	@Override
	public int getCount() {
		if(list != null && list.size() > 0){
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(list != null && list.size() > 0){
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("CutPasteId")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {		
		int type = getItemViewType(position);
		switch (type) {
		case SHOPCART_FLAG_GROUP:
			final GroupHolder gholder;
			if(convertView == null){
				gholder = new GroupHolder();//store_title
				convertView = LayoutInflater.from(mContext).inflate(R.layout.store_shopping_car_group, null);	
				gholder.group_check = (FrameLayout) convertView.findViewById(R.id.group_check);
				gholder.cb_check = (CheckBox) convertView.findViewById(R.id.store_title_check);
				gholder.tv_group_name=(TextView) convertView.findViewById(R.id.store_name);
				convertView.setTag(gholder);
			}
			else{
				gholder=(GroupHolder) convertView.getTag();
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Object> group=(Map<String, Object>) getItem(position);
			if(group != null){
				final GroupInfo groupinfo=(GroupInfo) group.get(SHOPCART_DATA);		
				if(groupinfo!=null){
					String shopName = groupinfo.getName();
					if(!Utils.isTextEmpty(shopName) && shopName.length() > 12){
						shopName = shopName.substring(0, 12) + "...";
					}
					gholder.tv_group_name.setText(shopName);
					gholder.cb_check.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							//gholder.cb_check.setChecked(!gholder.cb_check.isChecked());
							groupinfo.setChoosed(gholder.cb_check.isChecked());
							checkInterface.checkGroup(position, gholder.cb_check.isChecked());
						}
					});
					gholder.cb_check.setChecked(groupinfo.isChoosed());
				}
				
				convertView.findViewById(R.id.store_title).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,StorePersonActivity.class);
						intent.putExtra("shopId", groupinfo.getId());
						mContext.startActivity(intent);
					}
				});
			}
			break;

		case SHOPCART_FLAG_CHILDREN:			
			final ChildrenHolder cholder;
			if(convertView == null){
				cholder=new ChildrenHolder();
				convertView=View.inflate(mContext, R.layout.store_shopping_car_item, null);
				convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dipPx(mContext, 100)));
				cholder.group_check = (FrameLayout) convertView.findViewById(R.id.group_check);
				cholder.cb_check = (CheckBox) convertView.findViewById(R.id.product_check);
				cholder.iv_pic = (ImageView) convertView.findViewById(R.id.product_image);
				cholder.tv_product_name = (TextView) convertView.findViewById(R.id.product_name);
				cholder.tv_price = (TextView) convertView.findViewById(R.id.producter_price);
				cholder.tv_mark_price = (TextView) convertView.findViewById(R.id.product_markPrice);
				cholder.iv_increase = (ImageView) convertView.findViewById(R.id.product_remove);
				cholder.iv_decrease = (ImageView) convertView.findViewById(R.id.product_add);
				cholder.tv_color = (TextView) convertView.findViewById(R.id.product_color);
				cholder.tv_size = (TextView) convertView.findViewById(R.id.product_size);
				cholder.tv_count = (TextView) convertView.findViewById(R.id.product_count);
				cholder.group_minus = (FrameLayout) convertView.findViewById(R.id.group_minus);
				cholder.group_add = (FrameLayout) convertView.findViewById(R.id.group_add);
				cholder.tv_mark_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				convertView.setTag(cholder);
			}
			else{
				cholder=(ChildrenHolder) convertView.getTag();
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> children = (Map<String, Object>) getItem(position);
			final ProductInfo productInfo = (ProductInfo) children.get(SHOPCART_DATA);
			if(productInfo != null){
				ImageLoader.getInstance().displayImage(productInfo.getImageUrl(),(ImageView)convertView.findViewById(R.id.product_image),ImageLoaderConfigs.displayImageOptionsBg);
				cholder.tv_product_name.setText(productInfo.getName());
				cholder.tv_price.setText(mContext.getResources().getString(R.string.money_sigh) + new DecimalFormat("#.00").format(productInfo.getPrice()));
				cholder.tv_mark_price.setText(mContext.getResources().getString(R.string.money_sigh) + new DecimalFormat("#.00").format(productInfo.getMarkPrice()));
				//cholder.tv_color.setText(mContext.getResources().getString(R.string.goods_color)+" "+productInfo.getColor());
				//cholder.tv_size.setText(mContext.getResources().getString(R.string.goods_size)+" "+productInfo.getSize());
				cholder.tv_color.setText(productInfo.getColor());
				cholder.tv_size.setText(productInfo.getSize());
				cholder.tv_count.setText(productInfo.getCount()+"");
				
				if(0 == productInfo.getCount()){
					cholder.iv_increase.setImageResource(R.drawable.number_reduce_no);
				}
				else{
					cholder.iv_increase.setImageResource(R.drawable.number_reduce);
				}
				
				cholder.cb_check.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						//cholder.cb_check.setChecked(!cholder.cb_check.isChecked());
						productInfo.setChoosed(cholder.cb_check.isChecked());
						checkInterface.checkChild(position, cholder.cb_check.isChecked());
					}
				});
				
				cholder.group_minus.setOnClickListener(new OnClickListener() {				
					@Override
					public void onClick(View v) {
						boolean isChecked = cholder.cb_check.isChecked();
						modifyCountInterface.doIncrease(position, cholder.tv_count, isChecked);
					}
				});
				
				cholder.group_add.setOnClickListener(new OnClickListener() {		
					@Override
					public void onClick(View v) {
						boolean isChecked = cholder.cb_check.isChecked();
						modifyCountInterface.doDecrease(position, cholder.tv_count, isChecked);
					}
				});
				
				cholder.cb_check.setChecked(productInfo.isChoosed());
			}
			
			convertView.findViewById(R.id.store_childs).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, StoreGoodsMoreDetailsActivity.class);
					intent.putExtra("shopId", productInfo.getId());
					mContext.startActivity(intent);
				}
			});
			break;
		}
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		if(list != null && list.size() > 0){
			Map<String, Object> item=list.get(position);
			return (Integer) item.get(SHOPCART_TYPE);
		}
		return 0;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	private class GroupHolder{
		FrameLayout group_check;
		CheckBox cb_check;
		TextView tv_group_name;
	}
	
	private class ChildrenHolder{
		FrameLayout group_check;
		CheckBox cb_check;
		ImageView iv_pic;
		TextView tv_product_name;
		TextView tv_product_desc;
		TextView tv_price;
		TextView tv_mark_price;
		TextView tv_size;
		TextView tv_color;
		ImageView iv_increase;
		TextView tv_count;
		ImageView iv_decrease;
		FrameLayout group_add;
		FrameLayout group_minus;
	}
	
	
	public interface ModifyCountInterface{
		
		public void doIncrease(int position,View showCountView,boolean isChecked);
		
		public void doDecrease(int position,View showCountView,boolean isChecked);
	}
	
	
	public interface CheckInterface{
		
		public void checkGroup(int position,boolean isChecked);
		
		public void checkChild(int position,boolean isChecked);
	}	

}
