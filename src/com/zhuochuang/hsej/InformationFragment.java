package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
/**
 * 校园资讯
 * @author Dion
 *
 */
public class InformationFragment extends BaseFragment{
	
	private PullToRefreshListView mListView = null;
	private ContentAdapter mListViewAdapter;
	private int dateCount = 0;
	private String typeId = null;
	private JSONArray mDateArray = null;
	private int mPagerNum = 1;
	private boolean isGetMoreInformation = false;
	
	private Handler mHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainLayout = (ViewGroup)LayoutInflater.from(mActivity).inflate(R.layout.fragment_beauty_hs_schoolyard, null);
		initListView ();
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case EventManager.EventType_VoteChange:
				case EventManager.EventType_SurveyChange:
					if(mDateArray == null || mDateArray.length() == 0){
						return;
					}
					for(int i = 0; i < mDateArray.length(); i++){
						JSONObject obj = mDateArray.optJSONObject(i);
						try {
							if(obj.optString("id").equalsIgnoreCase((String) ((Object[])msg.obj)[0])){
								obj.put("favoriteStatus", "6");
								break;
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					if(mListViewAdapter != null){
						mListViewAdapter.notifyDataSetChanged();
					}
					break;

				default:
					break;
				}
			}
		});
	}

	private void initListView (){
		
		mListView = (PullToRefreshListView)mMainLayout.findViewById(R.id.pullToListView_schoolyard_beautyhs);
		mListView.setAdapter(mListViewAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				if(mDateArray != null && mDateArray.length() >0){
					return mDateArray.length();
				}
				return 0;
			}

			@Override
			public int getItemViewType(int position) {
				// TODO Auto-generated method stub
				JSONObject obj = mDateArray.optJSONObject(position);
				if(obj != null && obj.has("isSurveyTopic") && obj.optBoolean("isSurveyTopic")){
					return 2;
				}
				if(obj != null && obj.has("isVoteTopic") && obj.optBoolean("isVoteTopic")){
					return 2;
				}
				if(mActivity.getIntent().getBooleanExtra("isAnnouncement", false)){
					return 0;
				}
				return 1;
			}

			@Override
			public int getViewTypeCount() {
				// TODO Auto-generated method stub
				return 3;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				ViewHolderAnnouncement holderAnno = null;
				ViewHolderInformation holderInfo = null;
				ViewHolderSurveyTopic holderSurvey = null;
				int viewType = getItemViewType(position);
				if (convertView == null) {
					switch (viewType) {
					case 0:
						convertView = LayoutInflater.from(mActivity).inflate(R.layout.listcell_announcement, null);
						holderAnno = new ViewHolderAnnouncement();
						holderAnno.converView = convertView;
						convertView.setTag(holderAnno);
						break;
					case 1:
						convertView = LayoutInflater.from(mActivity).inflate(R.layout.listcell_beauty_hs, null);
						holderInfo = new ViewHolderInformation();
						holderInfo.converView = convertView;
						convertView.setTag(holderInfo);
						break;
					case 2:
						convertView = LayoutInflater.from(mActivity).inflate(R.layout.listcell_surveytopic, null);
						holderSurvey = new ViewHolderSurveyTopic();
						holderSurvey.converView = convertView;
						convertView.setTag(holderSurvey);
						break;
					}
				}
				else{
					switch (viewType) {
		            case 0:  
		            	holderAnno = (ViewHolderAnnouncement) convertView.getTag();
		                break;  
		            case 1:
		            	holderInfo = (ViewHolderInformation) convertView.getTag();
		            	break;
		            case 2:  
		            	holderSurvey = (ViewHolderSurveyTopic) convertView.getTag();
		                break;  
		            }
				}
				
				JSONObject obj = mDateArray.optJSONObject(position);
				if(obj != null){
					switch (viewType) {
		            case 0:
		            	//标题
						((TextView)holderAnno.converView.findViewById(R.id.announcement_title)).setText(obj.optString("name"));
						//通知内容
						((TextView)holderAnno.converView.findViewById(R.id.announcement_content)).setText(obj.optString("description"));
						//通知创建时间
						((TextView)holderAnno.converView.findViewById(R.id.announcement_time)).setText(Utils.getAlmostTime(mActivity, obj.optLong("createDate")));
						
						if(obj.has("isUrgent") && obj.optBoolean("isUrgent")){
							holderAnno.converView.findViewById(R.id.icon_urgent_announcement).setVisibility(View.VISIBLE);
						}
						else{
							holderAnno.converView.findViewById(R.id.icon_urgent_announcement).setVisibility(View.GONE);
						}
						
						if(obj.has("readStatus") && obj.optString("readStatus").equalsIgnoreCase("10")){
							holderAnno.converView.findViewById(R.id.sign_unread).setVisibility(View.INVISIBLE);
						}
						else{
							holderAnno.converView.findViewById(R.id.sign_unread).setVisibility(View.VISIBLE);
						}
		                break;  
		            case 1:
		            	ImageLoader.getInstance().displayImage(
								obj.optString("image"), 
								((ImageView)holderInfo.converView.findViewById(R.id.image_beautyhs_schoolyard)), 
								ImageLoaderConfigs.displayImageOptionsBg);
						//列表item，内容信息
						((TextView)holderInfo.converView.findViewById(R.id.title_beautyhs_schoolyard)).setText(obj.optString("name"));
						//((TextView)holderInfo.converView.findViewById(R.id.date_beautyhs_schoolyard)).setText(Utils.getAlmostTime(obj.optLong("createDate")));
						((TextView)holderInfo.converView.findViewById(R.id.date_beautyhs_schoolyard)).setText(Utils.getAlmostTime(mActivity, obj.optLong("createDate")));
						((TextView)holderInfo.converView.findViewById(R.id.content_beautyhs_schoolyard)).setText(obj.optString("description"));
		            	break;
		            case 2:
		            	/*ImageLoader.getInstance().displayImage(
								obj.optString("image"), 
								((ImageView)holderSurvey.converView.findViewById(R.id.imageview)), 
								ImageLoaderConfigs.displayImageOptionsBg);*/
		            	((TextView)holderSurvey.converView.findViewById(R.id.textview)).setText(obj.optString("description"));
		                break;  
		            }
				}
				
				
				return convertView;
			}
		});
		
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				HashMap<String, Object> params = new HashMap<String, Object>();
				mPagerNum = 1;
				params.put("typeId", typeId);
				params.put("pageNo", mPagerNum);
				params.put("pageSize", 10);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_InformationListInformations, params, InformationFragment.this);
			}
		});
		
		mListView.setRemoreable(true);
		mListView.setOnRemoreListener(new OnRemoreListener() {
			
			@Override
			public void onRemore() {
				HashMap<String, Object> params = new HashMap<String, Object>();
				mPagerNum += 1;
				isGetMoreInformation = true;
				params.put("typeId", typeId);
				params.put("pageNo", mPagerNum);
				params.put("pageSize", 10);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_InformationListInformations, params, InformationFragment.this);
			}
		});
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mDateArray != null && mDateArray.length() > 0){
					JSONObject obj = mDateArray.optJSONObject(arg2-1);
					if(obj == null){
						return;
					}
					Intent intent = null;
					if(obj.has("isSurveyTopic") && obj.optBoolean("isSurveyTopic")){
						if(!DataLoader.getInstance().isLogin()){
							startActivity(new Intent(mActivity, LoginActivity.class));
							mActivity.overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
							return;
						}
						if(obj.has("favoriteStatus") && obj.optString("favoriteStatus").equalsIgnoreCase("6")){
							intent = new Intent(mActivity, SurveyTopicResultActivity.class);
						}
						else{
							intent = new Intent(mActivity, SurveyTopicActivity.class);
						}
					}
					else if(obj.has("isVoteTopic") && obj.optBoolean("isVoteTopic")){
						if(!DataLoader.getInstance().isLogin()){
							startActivity(new Intent(mActivity, LoginActivity.class));
							mActivity.overridePendingTransition(R.anim.push_bottom_in,  R.anim.push_bottom_out);
							return;
						}
						intent = new Intent(mActivity, VoteActivity.class);
						intent.putExtra("favoriteStatus", obj.optInt("favoriteStatus", 0));
					}
					else{
						intent = new Intent(mActivity, InformationDetailsActivity.class);
					}
					intent.putExtra("id", obj.optString("id"));
					intent.putExtra("resouceType", 4);
					startActivity(intent);
					
					if(mActivity.getIntent().getBooleanExtra("isAnnouncement", false)){
						try {
							obj.put("readStatus", "10");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(mListViewAdapter != null){
							mListViewAdapter.notifyDataSetChanged();
						}
					}
				}
			}
			
		});
		
		mListView.startRefresh();
	}

	class ViewHolderInformation{
		View converView;
	}
	
	class ViewHolderAnnouncement{
		View converView;
	}
	
	class ViewHolderSurveyTopic{
		View converView;
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
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventManager.getInstance().removeHandlerListenner(mHandler);
	}

	@Override
	public void taskStarted(TaskType type) {
		super.taskStarted(type);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		
		if(mListView != null){
			mListView.complete();
		}
		if(result == null){
			return ;
		}
		
		if(result instanceof Error){
			Toast.makeText(mActivity, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return ;
		}
		
		switch (type) {
		case TaskOrMethod_InformationListInformations:
			JSONArray array = null;
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				array = ((JSONObject)result).optJSONArray("items");
			}
			
			if(array != null && array.length() > 9){
				mListView.setRemoreable(true);
			}else{
				mListView.setRemoreable(false);
			}
			
			if(isGetMoreInformation){
				isGetMoreInformation = false;
				mDateArray = DataLoader.getInstance().joinJSONArray(mDateArray, array);
			}else{
				mDateArray = array;
			}
			
			if(mListViewAdapter != null){
				mListViewAdapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
	}
	

	@Override
	public void taskIsCanceled(TaskType type) {
		super.taskIsCanceled(type);
	}
	
	public String setTypeId(String id){
		return typeId = id;
	}
}
