package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
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
import com.model.Configs;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;

@SuppressLint({ "InflateParams", "HandlerLeak" })
public class MyClassmateClubFragment extends BaseFragment{

	private PullToRefreshListView mListView;
	private ContentAdapter mAdapter;
	private JSONArray mDataArr;
	
	private boolean mIsNeedRefresh = false;
	private boolean mIsReadmore = false;
	
	private Handler mHandler;
	
	private int mPageNo = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mMainLayout = (ViewGroup) LayoutInflater.from(mActivity).inflate(R.layout.fragment_myclassmate_club, null);
		initListView();
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Object[] objs = (Object[]) msg.obj;
				switch (msg.what) {
				case EventManager.EventType_ClubAttention:
					if(mDataArr == null || mDataArr.length() == 0){
						return;
					}
					for(int i = 0; i < mDataArr.length(); i++){
						JSONObject obj = mDataArr.optJSONObject(i);
						if(obj != null && obj.has("id") && obj.optString("id").equalsIgnoreCase((String)objs[0])){
							try {
								obj.put("applicants", objs[1]);
							} catch (Exception e) {
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
					convertView = View.inflate(mActivity, R.layout.listcell_myclassmate_club, null);
					convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dipToPixels(mActivity, 150)));
				}
				JSONObject obj = mDataArr.optJSONObject(position);
				if(obj != null){
					((TextView)convertView.findViewById(R.id.textview_title)).setText(obj.optString("name"));
					((TextView)convertView.findViewById(R.id.textview_count)).setText(obj.optString("applicants"));
					ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)convertView.findViewById(R.id.imageview)), ImageLoaderConfigs.displayImageOptionsBg);
				}
				return convertView;
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(mActivity, CommunitydetailsActivity.class);
				if(mDataArr != null && mDataArr.length() > 0){
					JSONObject obj = mDataArr.optJSONObject(arg2 - 1);
					intent.putExtra("id", obj.optString("id"));
				}
				startActivity(intent);
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
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_CommunityListCommunities, params, MyClassmateClubFragment.this);
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
				params.put("pageNo", mPageNo);
				params.put("pageSize", 20);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_CommunityListCommunities, params, MyClassmateClubFragment.this);
			}
		});
		
		mListView.startRefresh();
	}
	
	@Override
	protected void onReceive(String brodecast) {
		// TODO Auto-generated method stub
		super.onReceive(brodecast);
		if(brodecast.equalsIgnoreCase(Configs.LoginStateChange)){
			mIsNeedRefresh = true;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mIsNeedRefresh){
			mIsNeedRefresh = false;
			if (!mListView.isStackFromBottom()) {
				mListView.setStackFromBottom(true);
			}
			mListView.setStackFromBottom(false);
			mListView.startRefresh();
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
			Toast.makeText(mActivity, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_CommunityListCommunities:
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				JSONArray temp = ((JSONObject)result).optJSONArray("items");
				if(temp != null && temp.length() > 19){
					mListView.setRemoreable(true);
				}
				else{
					mListView.setRemoreable(false);
				}
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

		default:
			break;
		}
	}

}
