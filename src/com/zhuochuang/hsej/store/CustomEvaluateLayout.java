package com.zhuochuang.hsej.store;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.layout.photoview.PhotoViewer;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.StaticGridView;
import com.util.Utils;
import com.zhuochuang.hsej.R;

public class CustomEvaluateLayout extends LinearLayout{
	private TextView mComtent;
	private TextView mComment_time;
	private Context mContext;
	private static final int EVALUATESTAR = 5;
	private LinearLayout mEvaluateLayout;
	
	StaticGridView mCommentPicGridView;
	ContentAdapter mAdapterGrid;
	JSONArray mCommentPicList;
	
	public CustomEvaluateLayout(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public CustomEvaluateLayout(Context context,AttributeSet  attrs) {
		super(context,attrs);
		mContext = context;
		init();
		
	}
	private void init() {
		LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.store_assess_layout, this);
		mComtent = (TextView) findViewById(R.id.evaluate);
		mComment_time = (TextView) findViewById(R.id.comment_time);
		mEvaluateLayout = (LinearLayout) findViewById(R.id.store_goods_evaluate_image_layout);
		
		mCommentPicGridView = (StaticGridView) findViewById(R.id.gridpic);
		
		mCommentPicGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(mCommentPicList == null || mCommentPicList.length() == 0){
					return;
				}
				PhotoViewer photoViewer = new PhotoViewer(mContext, arg2);
				photoViewer.setPathArr(mCommentPicList);
				photoViewer.showPhotoViewer(arg1);
			}
		});
	}
	
	public void setPicList(JSONArray list){
		mCommentPicList = list;
		/*if(list != null && list.length() > 0){
			int line = 0;
			int yu = list.length() % 5;
			if(yu == 0){
				line = list.length() / 5;
			}
			else{
				line = list.length() / 5 + 1;
			}
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (Utils.getScreenWidth(mContext) / 5 - Utils.dipPx(mContext, 5)) * line);
			lp.setMargins(0, Utils.dipPx(mContext, 5), Utils.dipPx(mContext, 5),  Utils.dipPx(mContext, 5));
			mCommentPicGridView.setLayoutParams(lp);
		}*/
		mCommentPicGridView.setAdapter(mAdapterGrid = new ContentAdapter(){

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(mCommentPicList != null && mCommentPicList.length() > 0){
					return mCommentPicList.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = View.inflate(mContext, R.layout.listcell_store_comment_image, null);
					int width = Utils.getScreenWidth(mContext) / 5 - Utils.dipPx(mContext, 7);
					convertView.setLayoutParams(new AbsListView.LayoutParams(width, width));
				}
				JSONObject obj = mCommentPicList.optJSONObject(position);
				if (obj != null) {
					ImageLoader.getInstance().displayImage(obj.optString("path"), ((ImageView) convertView.findViewById(R.id.imageview)),
							ImageLoaderConfigs.displayImageOptionsBg);
				}
				return convertView;
			}
		});
	}
	
	public void setData(JSONObject obj){
		if(obj == null){
			return;
		}
	}

	 public void setClient(String name) {
		if(!Utils.isTextEmpty(name) && name.length() > 12){
			name = name.substring(0, 12) + "...";
		}
		((TextView) findViewById(R.id.client)).setText(name);
	}	
	 
	public void setComtent(String evaluate) {  
		 mComtent.setText(evaluate);  
    }  
	 
	public void setComment_time(long millis){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			 Date date = new Date(millis);
			 mComment_time.setText(format.format(date));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	 }
	
	 public void setEvaluateStar(int length){
		 ImageView [] evaluateStar = new ImageView[EVALUATESTAR];
		 LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dipPx(mContext, 15),Utils.dipPx(mContext, 15),1);
		 for(int i = 0;i < EVALUATESTAR; i++){
			 evaluateStar[i] = new ImageView(mContext);
			 if(i > length-1){
				 evaluateStar[i].setImageResource(R.drawable.icon_grade_nor); 
			 }
			 else{
				 evaluateStar[i].setImageResource(R.drawable.icon_grade_pre);			
			 }
			 evaluateStar[i].setScaleType(ScaleType.CENTER_INSIDE);
			 layoutParams.setMargins(Utils.dipPx(mContext, 5),Utils.dipPx(mContext, 1),0,Utils.dipPx(mContext, 2));
			 evaluateStar[i].setLayoutParams(new LinearLayout.LayoutParams(1,1)); 
			 mEvaluateLayout.addView(evaluateStar[i],layoutParams); 
		 }
	 }
}
