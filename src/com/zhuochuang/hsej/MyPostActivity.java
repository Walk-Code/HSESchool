package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import com.layout.emoji.EmojiUtils;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.SharedPreferenceHandler;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;

public class MyPostActivity extends BaseActivity{

	private PullToRefreshListView mListView;
	private ContentAdapter mAdapter;
	private JSONArray mDataArr;
	
	private boolean mIsCompile = false;
	private boolean mIsReadmore = false;
	
	private int mPageNo = 1;
	
	private HashMap<String, String> mIdList;
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_mypost);
		setTitleText(R.string.mypost_title);
		
		mMainLayout.findViewById(R.id.ico_postedit).setVisibility(View.VISIBLE);
		mMainLayout.findViewById(R.id.textview).setVisibility(View.VISIBLE);
		initListView();
		
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
	private void initIdList(){
		if(mIdList == null){
			mIdList = new HashMap<String, String>();
		}
	}
	
	public void onPostEditClick(View view){
		initIdList();
		mIdList.clear();
		
		mIsCompile = !mIsCompile;
		
		//mMainLayout.findViewById(R.id.ico).setVisibility(mIsCompile? View.GONE : View.VISIBLE);
		//mMainLayout.findViewById(R.id.textview).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
		
		((TextView)mMainLayout.findViewById(R.id.textview)).setText(mIsCompile? getResources().getString(R.string.done) : getResources().getString(R.string.edit));
		mMainLayout.findViewById(R.id.bottom).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}
	
	public void OnDeleteClick(View view){
		initIdList();
		String ids = "";
		for(String id : mIdList.keySet()){
			ids += id + ",";
		}
		
		if(ids.contains(",")){
			ids = ids.substring(0, ids.length() - 1);
		}
		
		if(ids != null && ids.length() > 0){
			showDialogCustom(DIALOG_CUSTOM);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("postIds", ids);
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_HobbygroupDeletePosts, params, MyPostActivity.this);
		}
	}
	
	private void initListView(){
		mListView = (PullToRefreshListView) mMainLayout.findViewById(R.id.pullto_listview);
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
				if(convertView == null){
					convertView = View.inflate(MyPostActivity.this, R.layout.listcell_post, null);
					convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				}

				convertView.setPadding(0, 0, mIsCompile? -Utils.dipToPixels(MyPostActivity.this, 50) : 0, 0);
				convertView.findViewById(R.id.icolayout).setVisibility(mIsCompile? View.VISIBLE : View.GONE);
				
				JSONObject obj = mDataArr.optJSONObject(position);
				if(obj != null){
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
						name = getResources().getString(R.string.retract) + name;
					}
					else{
						convertView.findViewById(R.id.view_hongqi).setVisibility(View.GONE);
					}
					((TextView)convertView.findViewById(R.id.textview_title)).setText(name);
					((EmojiTextView)convertView.findViewById(R.id.textview_desc)).setEmojiText(obj.optString("intro"));
			
					//((TextView)convertView.findViewById(R.id.textview_date)).setText(String.format(getResources().getString(R.string.mypost_date), Utils.friendlyTime(MyPostActivity.this, obj.optLong("createDate"))));
					((TextView)convertView.findViewById(R.id.textview_date)).setText(Utils.friendlyTime(MyPostActivity.this, obj.optLong("createDate")));
				
					initIdList();
					convertView.findViewById(R.id.ico_option).setBackgroundResource(mIdList.containsKey(obj.optString("id"))? R.drawable.list_option_tick : R.drawable.list_option_no);
					convertView.findViewById(R.id.icolayout).setOnClickListener(new OnDeleteIcoClick(obj.optString("id")));
				
					((TextView)convertView.findViewById(R.id.textview_readcount)).setText(Utils.getChangeCount(obj.optInt("clickAmount", 0)));
					((TextView)convertView.findViewById(R.id.textview_commentcount)).setText(Utils.getChangeCount(obj.optInt("discussionNum", 0)));
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
				params.put("pageNo", mPageNo);
				params.put("pageSize", 20);
				params.put("fromId", DataLoader.getInstance().getHeaderUsetId());
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_HobbygroupListPosts, params, MyPostActivity.this);
			}
		});
		
		mListView.setRemoreable(false);
		mListView.setOnRemoreListener(new OnRemoreListener() {
			
			@Override
			public void onRemore() {
				// TODO Auto-generated method stub
				mIsReadmore = true;
				mPageNo++;
				HashMap<String, Object> params = new HashMap<String, Object>();
				try {
					params.put("pageNo", mPageNo);
					params.put("pageSize", 20);
					params.put("fromId", SharedPreferenceHandler.getXPSUserId(MyPostActivity.this));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_HobbygroupListPosts, params, MyPostActivity.this);
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(mDataArr == null || mDataArr.length() == 0){
					return;
				}
				JSONObject obj = mDataArr.optJSONObject(position - 1);
				if(obj != null){
					Intent intent = new Intent(MyPostActivity.this, PostDetailsActivity.class);
					intent.putExtra("id", obj.optString("id"));
					intent.putExtra("resouceType", 9);
					startActivity(intent);
					
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
	
	class OnDeleteIcoClick implements OnClickListener{

		private String id;
		
		public OnDeleteIcoClick(String id){
			this.id = id;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			initIdList();
			if(mIdList.containsKey(id)){
				mIdList.remove(id);
			}
			else{
				mIdList.put(id, id);
			}
			
			v.findViewById(R.id.ico_option).setBackgroundResource(mIdList.containsKey(id)? R.drawable.list_option_tick : R.drawable.list_option_no);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventManager.getInstance().removeHandlerListenner(mHandler);
	}
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		
		if(mListView != null){
			mListView.complete();
		}
		
		if(result == null){
			removeDialogCustom();
			return;
		}
		
		if(result instanceof Error){
			removeDialogCustom();
			Toast.makeText(MyPostActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_HobbygroupListPosts:
			removeDialogCustom();
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				JSONArray temp = ((JSONObject)result).optJSONArray("items");
				if(temp != null && temp.length() > 10){
					mListView.setRemoreable(true);
				}
				else{
					mListView.setRemoreable(false);
				}
				EmojiUtils.replaceEmojiIntro(temp);
				if(mIsReadmore){
					mIsReadmore = false;
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
			
		case TaskOrMethod_HobbygroupDeletePosts:
			/*if (!mListView.isStackFromBottom()) {
				mListView.setStackFromBottom(true);
			}
			mListView.setStackFromBottom(false);
			mListView.startRefresh();*/
			if(mListView != null){
				if (!mListView.isStackFromBottom()) {
					mListView.setStackFromBottom(true);
				}
				mListView.setStackFromBottom(false);
			}
			mPageNo = 1;
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("pageNo", mPageNo);
			params.put("pageSize", 20);
			params.put("fromId", DataLoader.getInstance().getHeaderUsetId());
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_HobbygroupListPosts, params, MyPostActivity.this);
			break;

		default:
			break;
		}
	}
}
