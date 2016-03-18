package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;

@SuppressLint("HandlerLeak")
public class SearchMoreActivity extends ListViewPullActivity{

	private JSONArray mDataList;
	private int mPageNo = 1;
	private boolean mIsReadmore = false;
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		switch (getIntent().getIntExtra("resourceType", 0)) {
		case 3:
			setTitleText(R.string.source_name3);
			break;
		case 4:
			setTitleText(R.string.source_name4);
			break;
		case 5:
			setTitleText(R.string.source_name5);
			break;
		default:
			break;
		}
		
		mListView.startRefresh();
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case EventManager.EventType_VoteChange:
				case EventManager.EventType_SurveyChange:
					if(mDataList == null || mDataList.length() == 0){
						return;
					}
					for(int i = 0; i < mDataList.length(); i++){
						JSONObject obj = mDataList.optJSONObject(i);
						try {
							if(obj.optString("id").equalsIgnoreCase((String) ((Object[])msg.obj)[0])){
								obj.put("favoriteStatus", "6");
								break;
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					if(mListAdapter != null){
						mListAdapter.notifyDataSetChanged();
					}
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public void getHeaderView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAdapter() {
		// TODO Auto-generated method stub
		mListAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(mDataList != null && mDataList.length() > 0){
					return mDataList.length();
				}
				return 0;
			}

			@Override
			public int getItemViewType(int position) {
				// TODO Auto-generated method stub
				if(mDataList == null){
					return 0;
				}
				JSONObject obj = mDataList.optJSONObject(position);
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
				// TODO Auto-generated method stub
				ViewHolder1 hodler1 = null;
				ViewHolder2 hodler2 = null;
				int type = getItemViewType(position);
				if(convertView == null){
					switch (type) {
					case 0:
						switch (getIntent().getIntExtra("resourceType", 0)) {
						case 3:
							convertView = View.inflate(SearchMoreActivity.this, R.layout.listcell_activities, null);
							break;
						case 4:
							convertView = View.inflate(SearchMoreActivity.this, R.layout.listcell_beauty_hs, null);
							break;
						case 5:
							convertView = View.inflate(SearchMoreActivity.this, R.layout.listcell_schoolyardservice_more, null);
							convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(SearchMoreActivity.this, 80)));
							break;
						}
						hodler1 = new ViewHolder1();
						hodler1.convertView = convertView;
						convertView.setTag(hodler1);
						break;

					case 1:
						convertView = View.inflate(SearchMoreActivity.this, R.layout.listcell_surveytopic, null);
						hodler2 = new ViewHolder2();
						hodler2.convertView = convertView;
						convertView.setTag(hodler2);
						break;
					default:
						break;
					}
				}
				else{
					switch (type) {
					case 0:
						hodler1 = (ViewHolder1) convertView.getTag();
						break;
					case 1:
						hodler2 = (ViewHolder2) convertView.getTag();
						break;
					}
				}
				
				JSONObject obj = mDataList.optJSONObject(position);
				if(obj != null){
					switch (type) {
					case 0:
						switch (getIntent().getIntExtra("resourceType", 0)) {
						case 3:
							((TextView)hodler1.convertView.findViewById(R.id.textview_title)).setText(obj.optString("name"));
							((TextView)hodler1.convertView.findViewById(R.id.textview_addresstime)).setText(
									String.format(getResources().getString(R.string.activitydetails_addresstime), obj.optString("address"), 
											Utils.getTimeQuantum(SearchMoreActivity.this, obj.optLong("startTime", 0), obj.optLong("endTime", 0))));
							((TextView)hodler1.convertView.findViewById(R.id.textview_date)).setText(Utils.getAlmostTimeDay(SearchMoreActivity.this, obj.optLong("createDate")));

							switch (obj.optInt("startStatus", 0)) {
							case 0:
								hodler1.convertView.findViewById(R.id.textview_statu).setBackgroundResource(R.drawable.bg_activity_statu_going);
								((TextView)hodler1.convertView.findViewById(R.id.textview_statu)).setTextColor(Color.argb(255, 233, 78, 81));
								((TextView)hodler1.convertView.findViewById(R.id.textview_statu)).setText(getResources().getString(R.string.activitylist_unstart));
								break;
							case 1:
								hodler1.convertView.findViewById(R.id.textview_statu).setBackgroundResource(R.drawable.bg_activity_statu_going);
								((TextView)hodler1.convertView.findViewById(R.id.textview_statu)).setTextColor(Color.argb(255, 233, 78, 81));
								((TextView)hodler1.convertView.findViewById(R.id.textview_statu)).setText(getResources().getString(R.string.activitylist_going));
								break;
							case 2:
								hodler1.convertView.findViewById(R.id.textview_statu).setBackgroundResource(R.drawable.bg_activity_statu_finish);
								((TextView)hodler1.convertView.findViewById(R.id.textview_statu)).setTextColor(Color.argb(255, 190, 190, 190));
								((TextView)hodler1.convertView.findViewById(R.id.textview_statu)).setText(getResources().getString(R.string.activitylist_finish));
								break;
							case 8:
								hodler1.convertView.findViewById(R.id.textview_statu).setBackgroundResource(R.drawable.bg_activity_statu_finish);
								((TextView)hodler1.convertView.findViewById(R.id.textview_statu)).setTextColor(Color.argb(255, 190, 190, 190));
								((TextView)hodler1.convertView.findViewById(R.id.textview_statu)).setText(getResources().getString(R.string.activitylist_aborted));
								break;
							default:
								break;
							}
							
							String numStr = String.format(getResources().getString(R.string.activitylist_personcount), obj.optString("applicants", "0"), obj.optString("maxNumber", "0"));
							((TextView)hodler1.convertView.findViewById(R.id.textview_personcount)).setText(Utils.getActivityNum(numStr, obj.optString("applicants", "0")));
							break;
						case 4:
							ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)hodler1.convertView.findViewById(R.id.image_beautyhs_schoolyard)), 
									ImageLoaderConfigs.displayImageOptionsBg);
							((TextView)hodler1.convertView.findViewById(R.id.title_beautyhs_schoolyard)).setText(obj.optString("name"));
							((TextView)hodler1.convertView.findViewById(R.id.date_beautyhs_schoolyard)).setText(Utils.getAlmostTime(SearchMoreActivity.this, obj.optLong("createDate")));
							((TextView)hodler1.convertView.findViewById(R.id.content_beautyhs_schoolyard)).setText(obj.optString("content"));
							break;
						case 5:
							((TextView)hodler1.convertView.findViewById(R.id.textview_title)).setText(obj.optString("name"));
							((TextView)hodler1.convertView.findViewById(R.id.textview_desc)).setText(obj.optString("description"));
							ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)hodler1.convertView.findViewById(R.id.imageview)), ImageLoaderConfigs.displayImageOptionsBg);
							break;
						}	
						break;
					case 1:
						((TextView)hodler2.convertView.findViewById(R.id.textview)).setText(obj.optString("description"));
						break;
					default:
						break;
					}
				}
				return convertView;
			}
		};
	}
	
	class ViewHolder1{
		View convertView;
	}
	
	class ViewHolder2{
		View convertView;
	}
	
	@Override
	public void OnItemClickListener(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		if(mDataList == null || mDataList.length() == 0){
			return;
		}
		JSONObject obj = mDataList.optJSONObject(position - 1);
		if(obj != null){
			DataLoader.getInstance().openResource(SearchMoreActivity.this, getIntent().getIntExtra("resourceType", 0), obj);
		}
	}

	@Override
	public void OnRefreshListener() {
		// TODO Auto-generated method stub
		mPageNo = 1;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("key", getIntent().getStringExtra("key"));
		params.put("pageNo", mPageNo);
		params.put("pageSize", 20);
		switch (getIntent().getIntExtra("resourceType", 0)) {
		case 3:
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_MycampusMoreActivities, params, SearchMoreActivity.this);
			break;
		case 4:
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_MycampusMoreInformations, params, SearchMoreActivity.this);
			break;
		case 5:
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_MycampusMoreServers, params, SearchMoreActivity.this);
			break;
		default:
			break;
		}
	}

	@Override
	public void OnRemoreListener() {
		// TODO Auto-generated method stub
		mPageNo++;
		mIsReadmore = true;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("key", getIntent().getStringExtra("key"));
		params.put("pageNo", mPageNo);
		params.put("pageSize", 20);
		switch (getIntent().getIntExtra("resourceType", 0)) {
		case 3:
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_MycampusMoreActivities, params, SearchMoreActivity.this);
			break;
		case 4:
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_MycampusMoreInformations, params, SearchMoreActivity.this);
			break;
		case 5:
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_MycampusMoreServers, params, SearchMoreActivity.this);
			break;
		default:
			break;
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
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(SearchMoreActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		JSONArray tempArr = null;
		switch (type) {
		case TaskOrMethod_MycampusMoreActivities:
			if(result instanceof JSONObject && ((JSONObject)result).has("activities")){
				tempArr = ((JSONObject)result).optJSONArray("activities");
			}
			break;
		case TaskOrMethod_MycampusMoreInformations:
			if(result instanceof JSONObject && ((JSONObject)result).has("informations")){
				tempArr = ((JSONObject)result).optJSONArray("informations");
			}
			break;
		case TaskOrMethod_MycampusMoreServers:
			if(result instanceof JSONObject && ((JSONObject)result).has("services")){
				tempArr = ((JSONObject)result).optJSONArray("services");
			}
			break;
		default:
			break;
		}
		if(tempArr != null && tempArr.length() > 10){
			mListView.setRemoreable(true);
		}
		else{
			mListView.setRemoreable(false);
		}
		
		if(mIsReadmore){
			mIsReadmore = false;
			mDataList = DataLoader.getInstance().joinJSONArray(mDataList, tempArr);
		}
		else{
			mDataList = tempArr;
		}
		
		if(mListAdapter != null){
			mListAdapter.notifyDataSetChanged();
		}
	}
}
