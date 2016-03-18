package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.PullToRefreshListView.OnRemoreListener;
import com.layout.emoji.EmojiTextView;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;
import com.zhuochuang.hsej.store.StoreGoodsMoreDetailsActivity;
import com.zhuochuang.hsej.store.StorePersonActivity;

@SuppressLint({ "InflateParams", "HandlerLeak" })
public class MyFavoriteFragment extends BaseFragment{

	private PullToRefreshListView mListView;
	private ContentAdapter mAdapter;
	
	private int mType = 0;
	private int mCurrentIndex = 0;
	private int mPageNo = 1;
	private boolean mIsCompile = false;
	private boolean mIsReadMore = false;
	
	private HashMap<String, String> mIdList = new HashMap<String, String>();
	
	private JSONArray mDataArr;
	private Handler mHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mMainLayout = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_myfavorite, null);
		
		initListView();
		if(mType == 3){
			EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					Object[] objs = (Object[]) msg.obj;
					if(objs == null){
						return;
					}
					switch (msg.what) {
					case EventManager.EventType_PostCommentCount:
						if(mDataArr == null || mDataArr.length() == 0){
							return;
						}
						for(int i = 0; i < mDataArr.length(); i++){
							JSONObject obj = mDataArr.optJSONObject(i);
							if(obj != null && obj.optString("id").equalsIgnoreCase((String) objs[0])){
								try {
									obj.put("discussionNum", objs[1]);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
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
			});
		}
	}
	
	public void setType(int type){
		mType = type;
	}

	public void setIdList(HashMap<String, String> idList){
		mIdList = idList;
	}
	
	public void setIsCompile(boolean isCompile){
		mIsCompile = isCompile;
	}
	
	public void notifyChange(boolean isCompile){
		mIsCompile = isCompile;
		if(!mIsCompile){
			mIdList.clear();
		}
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}
	
	public void refreshCurrentPage(){
		if(mListView != null){
			if (!mListView.isStackFromBottom()) {
				mListView.setStackFromBottom(true);
			}
			mListView.setStackFromBottom(false);
			mListView.startRefresh();
		}
	}
	
	private String getResourceType(){
		String resourceType = "";
		switch (mType) {
		case 0:
			resourceType = "4";
			break;
		case 1:
			resourceType = "3";
			break;
		case 2:
			resourceType = "2";
			break;
		case 3:
			resourceType = "9";
			break;
		case 4:
			resourceType = "8";
			break;
		case 5:
			resourceType = "16";
			break;
		case 6:
			resourceType = "15";
			break;

		default:
			break;
		}
		return resourceType;
	}
	
	private void initListView(){
		mListView = (PullToRefreshListView) mMainLayout.findViewById(R.id.pullto_listview);
		mListView.setBackgroundColor(Color.WHITE);
		mListView.setAdapter(mAdapter = new ContentAdapter(){
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(mDataArr != null && mDataArr.length() > 0){
					return mDataArr.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				JSONObject obj = mDataArr.optJSONObject(position);
				switch (mType) {
				case 0:
					if(convertView == null){
						//convertView = ViewGroup.inflate(mActivity, R.layout.listcell_post, null);
						convertView = View.inflate(mActivity, R.layout.listcell_beauty_hs, null);
						convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
					}
					convertView.findViewById(R.id.icolayout).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
					convertView.setPadding(0, 0, mIsCompile? -Utils.dipToPixels(mActivity, 40) : 0, 0);
					
					if(obj != null){
						ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)convertView.findViewById(R.id.image_beautyhs_schoolyard)), ImageLoaderConfigs.displayImageOptionsBg);
						((TextView)convertView.findViewById(R.id.title_beautyhs_schoolyard)).setText(obj.optString("name"));
						((TextView)convertView.findViewById(R.id.content_beautyhs_schoolyard)).setText(obj.optString("description"));
						((TextView)convertView.findViewById(R.id.date_beautyhs_schoolyard)).setText(Utils.getAlmostTime(mActivity, obj.optLong("createDate")));
						
						if(mIdList != null && mIdList.containsKey(obj.optString("id"))){
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_tick);
						}
						else{
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_no);
						}
						convertView.findViewById(R.id.icolayout).setOnClickListener(new OnDeleteIconListener(obj));
					}
					break;
					
				case 1:
					if(convertView == null){
						convertView = View.inflate(mActivity, R.layout.listcell_activities, null);
						convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
					}
					convertView.findViewById(R.id.icolayout).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
					convertView.setPadding(0, 0, mIsCompile? -Utils.dipToPixels(mActivity, 50) : 0, 0);
					if(obj != null){
						((TextView)convertView.findViewById(R.id.textview_title)).setText(obj.optString("name"));
						((TextView)convertView.findViewById(R.id.textview_addresstime)).setText(
								String.format(getResources().getString(R.string.activitydetails_addresstime), obj.optString("address"), 
										Utils.getTimeQuantum(mActivity, obj.optLong("startTime", 0), obj.optLong("endTime", 0))));
						((TextView)convertView.findViewById(R.id.textview_date)).setText(Utils.getAlmostTimeDay(mActivity, obj.optLong("createDate")));

						switch (obj.optInt("startStatus", 0)) {
						case 0:
							convertView.findViewById(R.id.textview_statu).setBackgroundResource(R.drawable.bg_activity_statu_going);
							((TextView)convertView.findViewById(R.id.textview_statu)).setTextColor(Color.argb(255, 233, 78, 81));
							((TextView)convertView.findViewById(R.id.textview_statu)).setText(getResources().getString(R.string.activitylist_unstart));
							break;
						case 1:
							convertView.findViewById(R.id.textview_statu).setBackgroundResource(R.drawable.bg_activity_statu_going);
							((TextView)convertView.findViewById(R.id.textview_statu)).setTextColor(Color.argb(255, 233, 78, 81));
							((TextView)convertView.findViewById(R.id.textview_statu)).setText(getResources().getString(R.string.activitylist_going));
							break;
						case 2:
							convertView.findViewById(R.id.textview_statu).setBackgroundResource(R.drawable.bg_activity_statu_finish);
							((TextView)convertView.findViewById(R.id.textview_statu)).setTextColor(Color.argb(255, 190, 190, 190));
							((TextView)convertView.findViewById(R.id.textview_statu)).setText(getResources().getString(R.string.activitylist_finish));
							break;
						case 8:
							convertView.findViewById(R.id.textview_statu).setBackgroundResource(R.drawable.bg_activity_statu_finish);
							((TextView)convertView.findViewById(R.id.textview_statu)).setTextColor(Color.argb(255, 190, 190, 190));
							((TextView)convertView.findViewById(R.id.textview_statu)).setText(getResources().getString(R.string.activitylist_aborted));
							break;
						default:
							break;
						}
						
						String numStr = String.format(getResources().getString(R.string.activitylist_personcount), obj.optString("applicants", "0"), obj.optString("maxNumber", "0"));
						
						((TextView)convertView.findViewById(R.id.textview_personcount)).setText(Utils.getActivityNum(numStr, obj.optString("applicants", "0")));
						
						if(mIdList != null && mIdList.containsKey(obj.optString("id"))){
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_tick);
						}
						else{
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_no);
						}
						convertView.findViewById(R.id.icolayout).setOnClickListener(new OnDeleteIconListener(obj));
					}
					break;
					
				case 2:
					if(convertView == null){
						convertView = View.inflate(mActivity, R.layout.listcell_myclassmate_club, null);
						convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(mActivity, 150)));
					}
					convertView.findViewById(R.id.icolayout).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
					convertView.setPadding(0, 0, mIsCompile? -Utils.dipToPixels(mActivity, 50) : 0, 0);
					if(obj != null){
						if(mIdList != null && mIdList.containsKey(obj.optString("id"))){
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_tick);
						}
						else{
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_no);
						}
						convertView.findViewById(R.id.icolayout).setOnClickListener(new OnDeleteIconListener(obj));
						
						((TextView)convertView.findViewById(R.id.textview_title)).setText(obj.optString("name"));
						((TextView)convertView.findViewById(R.id.textview_count)).setText(obj.optString("applicants"));
						ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)convertView.findViewById(R.id.imageview)), ImageLoaderConfigs.displayImageOptionsBg);
					}
					break;
					
				case 3:
					if(convertView == null){
						convertView = View.inflate(mActivity, R.layout.listcell_post, null);
						convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
					}
					convertView.findViewById(R.id.icolayout).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
					convertView.setPadding(0, 0, mIsCompile? -Utils.dipToPixels(mActivity, 40) : 0, 0);
					if(obj != null){
						if(mIdList != null && mIdList.containsKey(obj.optString("id"))){
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_tick);
						}
						else{
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_no);
						}
						convertView.findViewById(R.id.icolayout).setOnClickListener(new OnDeleteIconListener(obj));
					
						convertView.findViewById(R.id.textview_imagecount).setVisibility(View.GONE);
						String imgLength = "";
						String path = "";
						if(obj.has("images")){
							JSONArray imgs = obj.optJSONArray("images");
							if(imgs != null && imgs.length() > 0){
								JSONObject pathObj = imgs.optJSONObject(0);
								if(pathObj != null){
									path = pathObj.optString("path");
								}
								convertView.findViewById(R.id.textview_imagecount).setVisibility(imgs.length() > 1? View.VISIBLE : View.GONE);
								imgLength = String.format(getResources().getString(R.string.mypost_imagecount), imgs.length());
							}
						}
						ImageLoader.getInstance().displayImage(path, ((ImageView)convertView.findViewById(R.id.imageview)), ImageLoaderConfigs.displayImageOptionsBg);
						((TextView)convertView.findViewById(R.id.textview_imagecount)).setText(imgLength);
						
						String name = obj.optString("name");
						if(obj.has("sequence") && !Utils.isTextEmpty(obj.optString("sequence"))){
							convertView.findViewById(R.id.view_hongqi).setVisibility(View.VISIBLE);
							name = mActivity.getResources().getString(R.string.retract) + name;
						}
						else{
							convertView.findViewById(R.id.view_hongqi).setVisibility(View.GONE);
						}
						((TextView)convertView.findViewById(R.id.textview_title)).setText(name);
						//((TextView)convertView.findViewById(R.id.textview_title)).setText(obj.optString("name"));
						((EmojiTextView)convertView.findViewById(R.id.textview_desc)).setEmojiText(obj.optString("intro"));
						((TextView)convertView.findViewById(R.id.textview_date)).setText(Utils.friendlyTime(mActivity, obj.optLong("createDate")));
						convertView.findViewById(R.id.ico_option).setBackgroundResource(mIdList.containsKey(obj.optString("id"))? R.drawable.list_option_tick : R.drawable.list_option_no);
						convertView.findViewById(R.id.icolayout).setOnClickListener(new OnDeleteIconListener(obj));
						((TextView)convertView.findViewById(R.id.textview_readcount)).setText(Utils.getChangeCount(obj.optInt("clickAmount", 0)));
						((TextView)convertView.findViewById(R.id.textview_commentcount)).setText(Utils.getChangeCount(obj.optInt("discussionNum", 0)));
					}
					break;
					
				case 4:
					if(convertView == null){
						convertView = View.inflate(mActivity, R.layout.listcell_myclassmate_hobbygroup_child, null);
						convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(mActivity, 50)));
					}
					convertView.findViewById(R.id.icolayout).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
					convertView.setPadding(0, 0, mIsCompile? -Utils.dipToPixels(mActivity, 50) : 0, 0);
					if(obj != null){
						((TextView)convertView.findViewById(R.id.textview)).setText(obj.optString("name"));
						ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)convertView.findViewById(R.id.imageview)), ImageLoaderConfigs.displayImageOptionsBg);
						
						if(mIdList != null && mIdList.containsKey(obj.optString("id"))){
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_tick);
						}
						else{
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_no);
						}
						convertView.findViewById(R.id.icolayout).setOnClickListener(new OnDeleteIconListener(obj));
					}
					break;

				case 5:
					if(convertView == null){
						convertView = View.inflate(mActivity, R.layout.listcell_myfavorite_shop, null);
						convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(mActivity, 80)));
					}
					convertView.findViewById(R.id.icolayout).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
					convertView.setPadding(0, 0, mIsCompile? -Utils.dipToPixels(mActivity, 50) : 0, 0);
					if(obj != null){
						((TextView)convertView.findViewById(R.id.textview_store_shop)).setText(obj.optString("name"));
						ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)convertView.findViewById(R.id.image_store_shop)), ImageLoaderConfigs.displayImageOptionsBg);
						
						if(mIdList != null && mIdList.containsKey(obj.optString("id"))){
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_tick);
						}
						else{
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_no);
						}
						convertView.findViewById(R.id.icolayout).setOnClickListener(new OnDeleteIconListener(obj));
					}
					break;
				case 6:
					if(convertView == null){
						convertView = View.inflate(mActivity, R.layout.listcell_myfavorite_goods, null);
						convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(mActivity, 120)));
					}
					convertView.findViewById(R.id.icolayout).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
					convertView.setPadding(0, 0, mIsCompile? -Utils.dipToPixels(mActivity, 50) : 0, 0);
					if(obj != null){
						((TextView)convertView.findViewById(R.id.textview_store_goodsname)).setText(obj.optString("name"));
						((TextView)convertView.findViewById(R.id.textview_store_goodsprice)).setText(
								mActivity.getResources().getString(R.string.tag_yuan) + obj.optString("price"));
						ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)convertView.findViewById(R.id.image_store_goods)), ImageLoaderConfigs.displayImageOptionsBg);
						
						if(mIdList != null && mIdList.containsKey(obj.optString("id"))){
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_tick);
						}
						else{
							convertView.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_no);
						}
						convertView.findViewById(R.id.icolayout).setOnClickListener(new OnDeleteIconListener(obj));
					}
					break;
				default:
					break;
				}
				return convertView;
			}
		});
		
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mPageNo = 1;
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("resourceType", getResourceType());
			//	params.put("resourceId", "");
				params.put("status", "0");
				params.put("pageNo", mPageNo);
				params.put("pageSize", 20);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_FavoriteList, params, MyFavoriteFragment.this);
			}
		});
		
		mListView.setRemoreable(true);
		mListView.setOnRemoreListener(new OnRemoreListener() {
			
			@Override
			public void onRemore() {
				// TODO Auto-generated method stub
				mPageNo++;
				mIsReadMore = true;
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("resourceType", getResourceType());
				params.put("resourceId", "");
				params.put("status", "0");
				params.put("pageNo", mPageNo);
				params.put("pageSize", 20);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_FavoriteList, params, MyFavoriteFragment.this);
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				JSONObject obj = mDataArr.optJSONObject(position - 1);
				if(obj == null){
					return;
				}
				Intent intent = null;
				switch (mType) {
				case 0:
					intent = new Intent(mActivity, InformationDetailsActivity.class);
					intent.putExtra("id", obj.optString("id"));
					intent.putExtra("resouceType", 4);
					break;
				case 1:
					intent = new Intent(mActivity, ActivityDetailsActivity.class);
					intent.putExtra("id", obj.optString("id"));
					break;
				case 2:
					intent = new Intent(mActivity, CommunitydetailsActivity.class);
					intent.putExtra("id", obj.optString("id"));
					break;
				case 3:
					intent = new Intent(mActivity, PostDetailsActivity.class);
					intent.putExtra("id", obj.optString("id"));
					break;
				case 4:
					intent = new Intent(mActivity, HobbyGroupListActivity.class);
					intent.putExtra("data", obj.toString());
					break;
				case 5:
					//intent = new Intent(mActivity, PostDetailsActivity.class);
					//intent.putExtra("id", obj.optString("id"));
					intent = new Intent(mActivity, StorePersonActivity.class);
					intent.putExtra("shopId", obj.optString("id"));
					break;
				case 6:
					intent = new Intent(mActivity, StoreGoodsMoreDetailsActivity.class);
					intent.putExtra("shopId", obj.optString("id"));
					break;

				default:
					break;
				}
				
				if(intent != null){
					mActivity.startActivity(intent);
				}
				
				if(mType == 3){
					try {
						obj.put("clickAmount", obj.optInt("clickAmount", 0) + 1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(mAdapter != null){
								mAdapter.notifyDataSetChanged();
							}
						}
					}, 800);
				}
			}
		});
		
		mListView.startRefresh();
	}
	
	class OnDeleteIconListener implements OnClickListener{
		JSONObject obj;
		
		public OnDeleteIconListener(JSONObject obj){
			this.obj = obj;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mIdList.containsKey(obj.optString("id"))){
				mIdList.remove(obj.optString("id"));
				v.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_no);
			}
			else{
				mIdList.put(obj.optString("id"), obj.optString("name"));
				v.findViewById(R.id.ico_option).setBackgroundResource(R.drawable.list_option_no);
			}
			if(mAdapter != null){
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventManager.getInstance().removeHandlerListenner(mHandler);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		mListView.complete();
		
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			try {
				Toast.makeText(mActivity, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return;
		}
		
		switch (type) {
		case TaskOrMethod_FavoriteList:
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
			}
			else{
				mListView.setRemoreable(false);
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
