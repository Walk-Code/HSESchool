package com.zhuochuang.hsej.store;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.model.EventManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;
import com.zhuochuang.hsej.R;
/*
 * 观察者未注销
 */
public class StorePopupWindowToShopCar extends PopupWindow{
	private Context mContext;
	private OnPopupWindowsClickListener mOnPopupWindowsClickListener;
	private ContentAdapter mAdapterColor;
	private ContentAdapter mAdapterSize;
	private List<String> mList = new ArrayList<String>();//第一个gridview
	private List<String> mList_ = new ArrayList<String>();//第二个gridview
	private PopupWindow popWindow;
	private CustomImageView mCustomImageView;
	private TextView mPrice,mSpecification,mStock;
	private int mColor = 0,mSize = 0;
	private String mShopId;
	private int mInventory;
	private LinearLayout mColorLayout;
	private LinearLayout mSizeLayout;
	private LinearLayout mLines,mLiness;
	public StorePopupWindowToShopCar(Context mContext) {
		super();
		this.mContext = mContext;
		initView();		
	}
	
	private void initView() {  		
		View view = ViewGroup.inflate(mContext, R.layout.store_details_car, null);
		setContentView(view);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(Utils.getScreenHeight(mContext)/5*3 + Utils.dipPx(mContext, 55));
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setOutsideTouchable(true);
		setAnimationStyle(R.style.popupwindow_animation_store_car);
		mPrice = (TextView) view.findViewById(R.id.store_goods_doh);
		mSpecification = (TextView) view.findViewById(R.id.store_size);	
		mStock = (TextView) view.findViewById(R.id.store_goods_stock);
		mLines = (LinearLayout) view.findViewById(R.id.store_lines);
		mLiness = (LinearLayout) view.findViewById(R.id.store_line);
		mColorLayout = (LinearLayout) view.findViewById(R.id.store_color_layout);
		mSizeLayout = (LinearLayout) view.findViewById(R.id.store_size_layout);
		//退出购物车
		view.findViewById(R.id.store_car_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StorePopupWindowToShopCar.this.dismiss();
			}
		});
		
		view.findViewById(R.id.store_car_commint).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StorePopupWindowToShopCar.this.dismiss();
				String color = null;
				String size = null; 
				if((0 != mList.size()) && (0 != mList_.size())){
					color = mList.get(mColor);
					size = mList_.get(mSize);
				}
				EventManager.getInstance().sendMessage(EventManager.getInstance().EventType_UpdateUI,1,color,size,mPrice.getText().toString().replace(mContext.getResources().getString(R.string.money_sigh),""),mShopId);			
			   }
		});
		
		mCustomImageView =  (CustomImageView) view.findViewById(R.id.store_car_images);
		GridView gridView = (GridView) view.findViewById(R.id.goods_color);
		GridView gridView_ = (GridView) view.findViewById(R.id.goods_size);			
		gridView.setAdapter(mAdapterColor = new ContentAdapter(){
			private int mSelection;
			@Override
			public void clearSelect(int position){
				mSelection = position;
			}
			@Override
			public int getCount() {			
				return mList.size() == 0 ? 0 :mList.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(null == convertView){
					convertView = LayoutInflater.from(mContext).inflate(R.layout.store_details_car_item, null);
				}			
				((TextView)convertView.findViewById(R.id.store_detail_car_item)).setText(mList.get(position));
				if(mSelection == position){
					((TextView)convertView.findViewById(R.id.store_detail_car_item)).setBackgroundResource(R.drawable.item_pressed);
					((TextView)convertView.findViewById(R.id.store_detail_car_item)).setTextColor(Color.WHITE);
				}else{
					((TextView)convertView.findViewById(R.id.store_detail_car_item)).setBackgroundResource(R.drawable.item_normal);
					((TextView)convertView.findViewById(R.id.store_detail_car_item)).setTextColor(Color.BLACK);
				}
				return convertView;
			}			
		});
		
		gridView_.setAdapter(mAdapterSize = new ContentAdapter(){
			private int SelectPostion;
			@Override
			public void clearSelect(int position) {
				super.clearSelect(position);
				SelectPostion = position;
			}

			@Override
			public int getCount() {			
				return mList_.size() == 0 ? 0 :mList_.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(null == convertView){
					convertView = LayoutInflater.from(mContext).inflate(R.layout.store_details_car_item, null);
				}		
				((TextView)convertView.findViewById(R.id.store_detail_car_item)).setText(mList_.get(position));	
				if(SelectPostion == position){
					((TextView)convertView.findViewById(R.id.store_detail_car_item)).setBackgroundResource(R.drawable.item_pressed);
					((TextView)convertView.findViewById(R.id.store_detail_car_item)).setTextColor(Color.WHITE);
				}else{
					((TextView)convertView.findViewById(R.id.store_detail_car_item)).setBackgroundResource(R.drawable.item_normal);
					((TextView)convertView.findViewById(R.id.store_detail_car_item)).setTextColor(Color.BLACK);
				}
				return convertView;
			}			
		});
		
		if(null != mAdapterColor){
			mAdapterColor.registerDataSetObserver(mDataSetObserver);
			gridView.setOnItemClickListener(new OnItemClickListener() {	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mColor = position;
					mAdapterColor.clearSelect(position);
					mAdapterColor.notifyDataSetChanged();
					//Toast.makeText(mContext, "颜色-->>" + position, 1).show();  				
				}
			});
		}
		
		if(null != mAdapterSize){
			mAdapterSize.registerDataSetObserver(mDataSetObserver);
			gridView_.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					mSize = position;
					mAdapterSize.clearSelect(position);
					mAdapterSize.notifyDataSetChanged();				
				}
			});
		}
	}
	
	public void setOnPopupWindowOnClickListener(OnPopupWindowsClickListener listen){
		mOnPopupWindowsClickListener = listen;
	}
	
	private void showDialog(){
        View view = ViewGroup.inflate(mContext, R.layout.store_car_hint, null);
        view.findViewById(R.id.store_car_hints).getBackground().setAlpha(200);
        popWindow = new PopupWindow(view, Utils.getScreenWidth(mContext)/3*2, Utils.getScreenHeight(mContext)/5, true);  
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);
        popWindow.showAtLocation(view.findViewById(R.id.store_car_hints),Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
	}
	
	public void changeData(List<String> color,List<String> size,String imagePath,String price,String shopId,int inventory) {		
	
		if(null != color && null != size){
			mList.addAll(color);
			mList_.addAll(size);			
		}else{
			mList.add("");
			mList_.add(null);
		}
		if(inventory < 0){
			inventory = 0;
		}
		mInventory = inventory;
        ImageLoader.getInstance().displayImage(imagePath, mCustomImageView);
        mPrice.setText(mContext.getResources().getString(R.string.money_sigh)+price);
        mStock.setText(mContext.getResources().getString(R.string.goods_inventory) + " " + inventory + mContext.getResources().getString(R.string.goods_inventory2));
        mShopId = shopId;
        if((0 < mList.size() && 0 < mList_.size()) && (mList.get(0) != null && mList_.get(0) != null)){       	
	        mSpecification.setText(mContext.getResources().getString(R.string.goods_choose2) + " "+color.get(mColor)+" "+size.get(mSize));
	        mAdapterColor.notifyDataSetChanged();
	        mAdapterSize.notifyDataSetChanged();
        }
        else{
        	mLines.setVisibility(View.GONE);
        	mLiness.setVisibility(View.GONE);
        	mColorLayout.setVisibility(View.GONE);
        	mSizeLayout.setVisibility(View.GONE);
        }
    }
	
	public interface OnPopupWindowsClickListener{
		public void onPopupWindowItemClick(int position);
	}
		
	@Override
	public void dismiss() {
		super.dismiss();		
		/*if(null != mAdapterSize) mAdapterSize.unregisterDataSetObserver(mDataSetObserver);	
		if(null != mAdapterColor) mAdapterColor.unregisterDataSetObserver(mDataSetObserver);*/
	}

	private DataSetObserver mDataSetObserver = new DataSetObserver() {

		@Override
		public void onChanged() {
			//super.onChanged();
			 if(0 < mList.size() || 0 < mList_.size()){
				 mSpecification.setText(mContext.getResources().getString(R.string.goods_choose2) + " "+mList.get(mColor)+" "+mList_.get(mSize));
			 }		 
		}
		
		@Override
		public void onInvalidated() {
			super.onInvalidated();
		}		
	};
}
