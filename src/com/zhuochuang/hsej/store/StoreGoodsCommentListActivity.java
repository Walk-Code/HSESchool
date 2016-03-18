package com.zhuochuang.hsej.store;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.PullToRefreshListView.OnRemoreListener;
import com.layout.photoview.PhotoViewer;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.StaticGridView;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;

public class StoreGoodsCommentListActivity extends BaseActivity{
	private PullToRefreshListView mListView;
	private JSONArray mDataArr;
	private ContentAdapter mAdapter;
	private LinearLayout mHeaderView;
	ImageView [] evaluateStar ;
	ImageView [] evaluatePhoto ;
	LinearLayout mCommentLayout;
	
	
	int mPageNum = 1;
	boolean mIsReadMore = false;
	
	/**
	 * 
	 * 评论星级写在XML
	 */
	@Override
	protected void onCreate(Bundle arg0) {	
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		setContentView(R.layout.store_goods_comment);
		setTitleText(R.string.stores_goods_comment);
		
		initView();		
	}
	
	private void initView() {
		evaluateStar = new ImageView[5];
		evaluatePhoto = new ImageView[3];
		mListView = (PullToRefreshListView) findViewById(R.id.pullToListView_goods_comment);
	
		mListView.setAdapter(mAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				return mDataArr != null ? mDataArr.length() : 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = LayoutInflater.from(StoreGoodsCommentListActivity.this).inflate(R.layout.store_goods_comment_item, null);				
				}			
				
				JSONObject obj = mDataArr.optJSONObject(position);
				if(obj != null){
					mCommentLayout = (LinearLayout)convertView.findViewById(R.id.store_comment_layout);
					String nickname = obj.optString("nickName");
					if(!Utils.isTextEmpty(nickname) && nickname.length() > 12){
						nickname = nickname.substring(0, 12) + "...";
					}
					
					((TextView)convertView.findViewById(R.id.store_comment_author)).setText(nickname);
					 int length = obj.optInt("evaluate");
					 for(int i = 0; i < mCommentLayout.getChildCount(); i++){
						 View view = mCommentLayout.getChildAt(i);
						 if(i < length){
							 view.setBackgroundResource(R.drawable.icon_grade_pre);
						 }
						 else{
							 view.setBackgroundResource(R.drawable.icon_grade_nor);
						 }
					 }
					  
					 ((TextView)convertView.findViewById(R.id.store_goods_coommen)).setText(mDataArr.optJSONObject(position).optString("content"));
					 ((TextView)convertView.findViewById(R.id.store_goods_coomment_creattime)).setText(getStringFromLong(mDataArr.optJSONObject(position).optLong("createDate"))+"");
					 
					 if(obj.has("images")){
						 final JSONArray list = mDataArr.optJSONObject(position).optJSONArray("images");
						 StaticGridView commentPicGridView = (StaticGridView) convertView.findViewById(R.id.gridpic);
						 commentPicGridView.setVisibility(View.VISIBLE);
						 commentPicGridView.setAdapter(new ContentAdapter(){
								@Override
								public int getCount() {
									// TODO Auto-generated method stub
									if(list != null && list.length() > 0){
										return list.length();
									}
									return 0;
								}

								@Override
								public View getView(int position, View convertView, ViewGroup parent) {
									// TODO Auto-generated method stub
									if (convertView == null) {
										convertView = View.inflate(StoreGoodsCommentListActivity.this, R.layout.listcell_store_comment_image, null);
										int width = Utils.getScreenWidth(StoreGoodsCommentListActivity.this) / 5 - Utils.dipPx(StoreGoodsCommentListActivity.this, 7);
										convertView.setLayoutParams(new AbsListView.LayoutParams(width, width));
									}
									JSONObject obj = list.optJSONObject(position);
									if (obj != null) {
										ImageLoader.getInstance().displayImage(obj.optString("path"), ((ImageView) convertView.findViewById(R.id.imageview)),
												ImageLoaderConfigs.displayImageOptionsBg);
									}
									return convertView;
								}
							});
						 commentPicGridView.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								if(list == null || list.length() == 0){
									return;
								}
								PhotoViewer photoViewer = new PhotoViewer(StoreGoodsCommentListActivity.this, arg2);
								photoViewer.setPathArr(list);
								photoViewer.showPhotoViewer(arg1);
							}
						});
					 }
					 else{
						 convertView.findViewById(R.id.gridpic).setVisibility(View.GONE);
					 }
				}
				return convertView;
			}
		});
		
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				mPageNum = 1;
				mIsReadMore = false;
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_Goods_GoodsEvaluate, 
						DataLoader.getInstance().setGoodsEvaluateTypeParams(getIntent().getStringExtra("goodsId"), mPageNum), StoreGoodsCommentListActivity.this);
			}
		});
		mListView.setOnRemoreListener(new OnRemoreListener() {
			
			@Override
			public void onRemore() {
				// TODO Auto-generated method stub
				mPageNum++;
				mIsReadMore = false;
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_Goods_GoodsEvaluate, 
						DataLoader.getInstance().setGoodsEvaluateTypeParams(getIntent().getStringExtra("goodsId"), mPageNum), StoreGoodsCommentListActivity.this);
			}
		});
		mListView.setRemoreable(false);
		mListView.startRefresh();
	}
	
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
		if(result == null){
			return;
		}
		if(result instanceof Error){
			Toast.makeText(StoreGoodsCommentListActivity.this,((Error) result).getMessage(),Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_Goods_GoodsEvaluate:
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				JSONArray temp = ((JSONObject)result).optJSONArray("items");
				if(temp != null && temp.length() > 19){
					mListView.setRemoreable(true);
				}
				else{
					mListView.setRemoreable(false);
				}
				if(mIsReadMore){
					mIsReadMore = false;
					mDataArr = DataLoader.getInstance().joinJSONArray(mDataArr, temp);
				}
				else{
					mDataArr = temp;
				}
				if(mDataArr == null || mDataArr.length() == 0){
					setTitleText(R.string.stores_goods_comment);
				}
				else{
					setTitleText(getResources().getString(R.string.stores_goods_comment)+"("+((JSONObject)result).optInt("total", 0)+")");
				}
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
			}
			else{
				mListView.setRemoreable(false);
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
