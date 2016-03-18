package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.PullToRefreshListView.OnRemoreListener;
import com.model.Configs;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.ContentAdapter;

@SuppressLint("InflateParams")
public class OnlineQuestionFragment extends BaseFragment{
	
private PullToRefreshListView mListView = null;
	
	private ContentAdapter mAdapter = null;

	private JSONArray mDateArray = null;
	
	private int pageNo = 1;
	
	private boolean isGetMoreInfo = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mMainLayout = (ViewGroup)LayoutInflater.from(mActivity).inflate(R.layout.fragment_chat_my_firends, null);
		
		initListView ();
	}
	
	private void initListView (){
		
		mListView = (PullToRefreshListView)mMainLayout.findViewById(R.id.pullToListView_chat_myfirends);
		mListView.setAdapter(mAdapter = new ContentAdapter(){
			@Override
			public int getCount() {
				if(mDateArray != null && mDateArray.length() > 0){
					return mDateArray.length();
				}
				return 0;
			}
			
			@Override
			public int getItemViewType(int position) {
				// TODO Auto-generated method stub
				JSONObject obj = mDateArray.optJSONObject(position);
				if(obj != null && obj.has("isSurveyTopic") && obj.optBoolean("isSurveyTopic")){
					return 1;
				}
				if(obj != null && obj.has("isVoteTopic") && obj.optBoolean("isVoteTopic")){
					return 1;
				}
				
				return 0;
			}

			@Override
			public int getViewTypeCount() {
				// TODO Auto-generated method stub
				return 2;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				ViewHolderSurvey holderSurvey = null;
				int viewType = getItemViewType(position);
				if(convertView == null){
					switch (viewType) {
					case 0:
						convertView = LayoutInflater.from(mActivity).inflate(R.layout.listcell_online_consult, null);
						holder = new ViewHolder();
						holder.convertView = convertView;
						convertView.setTag(holder);
						break;
					case 1:
						convertView = LayoutInflater.from(mActivity).inflate(R.layout.listcell_surveytopic, null);
						holderSurvey = new ViewHolderSurvey();
						holderSurvey.convertView = convertView;
						convertView.setTag(holderSurvey);
						break;
					default:
						break;
					}
				}
				else{
					switch (viewType) {
		            case 0:  
		            	holder = (ViewHolder) convertView.getTag();
		                break;  
		            case 1:
		            	holderSurvey = (ViewHolderSurvey) convertView.getTag();
		            	break;
		            }
				}
				JSONObject obj = mDateArray.optJSONObject(position);
				
				if(obj != null){
					switch (viewType) {
		            case 0:  
		            	((TextView)holder.convertView.findViewById(R.id.online_consult_text)).setText((position+1) +". "+obj.optString("name"));
		                break;  
		            case 1:
		            	((TextView)holderSurvey.convertView.findViewById(R.id.textview)).setText(obj.optString("description"));
		            	break;
		            }
				}
				
				return convertView;
			}
		});
		
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				pageNo = 1;
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("pageNo", pageNo);
				params.put("pagerSize", 20);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_EnrolListqa, params, OnlineQuestionFragment.this);
			}
		});
		
		mListView.setRemoreable(true);
		mListView.setOnRemoreListener(new OnRemoreListener() {
			
			@Override
			public void onRemore() {
				isGetMoreInfo = true;
				HashMap<String, Object> params = new HashMap<String, Object>();
				pageNo += 1;
				params.put("pageNo", pageNo);
				params.put("pagerSize", 20);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_EnrolListqa, params, OnlineQuestionFragment.this);
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mDateArray != null && mDateArray.length() >0){
					
					JSONObject obj = mDateArray.optJSONObject(arg2-1);
					if(obj != null){
						Intent intent = null;
						if(obj.has("isSurveyTopic") && obj.optBoolean("isSurveyTopic")){
							if(obj.has("favoriteStatus") && obj.optString("favoriteStatus").equalsIgnoreCase("6")){
								intent = new Intent(mActivity, SurveyTopicResultActivity.class);
							}
							else{
								intent = new Intent(mActivity, SurveyTopicActivity.class);
							}
						}
						else if(obj.has("isVoteTopic") && obj.optBoolean("isVoteTopic")){
							intent = new Intent(mActivity, VoteActivity.class);
							intent.putExtra("favoriteStatus", obj.optInt("favoriteStatus", 0));
						}
						else{
							intent = new Intent(mActivity, InformationDetailsActivity.class);
						}
						intent.putExtra("id", obj.optString("id"));
						intent.putExtra("resouceType", 4);
						startActivity(intent);
					}
				}
			}
		});
		
		mListView.startRefresh();
	}

	class ViewHolder{
		View convertView;
	}
	
	class ViewHolderSurvey{
		View convertView;
	}
	
	@Override
	protected void onReceive(String brodecast) {
		// TODO Auto-generated method stub
		super.onReceive(brodecast);
		if(brodecast.equalsIgnoreCase(Configs.LoginStateChange)){
			if(mListView != null){
				if (!mListView.isStackFromBottom()) {
					mListView.setStackFromBottom(true);
				}
				mListView.setStackFromBottom(false);
				mListView.startRefresh();
			}
		}
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
		Toast.makeText(mActivity, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
		return;
	}
	
	switch (type) {
	case TaskOrMethod_EnrolListqa:
		 JSONArray array = null;
		if(result instanceof JSONObject && ((JSONObject)result).has("items")){
			array = ((JSONObject)result).optJSONArray("items");
			
		}
		
		if(array != null && array.length() > 19){
			mListView.setRemoreable(true);
		}else{
			mListView.setRemoreable(false);
		}
		
		if(isGetMoreInfo){
			isGetMoreInfo = false;
			mDateArray = DataLoader.getInstance().joinJSONArray(mDateArray, array);
		}else{
			mDateArray = array;
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
