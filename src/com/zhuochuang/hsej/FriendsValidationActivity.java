package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.layout.CustomListCell;
import com.layout.CustomListCell.onButtonListener;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;

/**
 * 好友验证
 * @author Dion
 *
 */
public class FriendsValidationActivity extends ListViewPullActivity {
	
	private ProgressDialog mProgressDialog = null;
	
	private JSONArray mDataArray = null;
	
	private CustomListCell mCustomListCell = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//activity title : 好友验证
		setTitleText(R.string.verification_friends_title);
		
		mListView.startRefresh();
		mListView.setRemoreable(false);
		showDialogCustom(DIALOG_CUSTOM);
	}
	
	
	@Override
	public void getHeaderView() {
		
	}

	@Override
	public void getAdapter() {
		mListAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				if(mDataArray != null && mDataArray.length()>0){
					return mDataArray.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = LayoutInflater.from(FriendsValidationActivity.this).inflate(R.layout.listcell_firends_validation, null);
				}
				
				final JSONObject obj = mDataArray.optJSONObject(position);
				
				final CustomListCell customListCell= (CustomListCell)convertView.findViewById(R.id.validation_firends);
				//customListCell.setTitleView(obj.optString("nickName"));
				customListCell.setTitleView(obj.optString("nickName"));
				
				ImageLoader.getInstance().displayImage(
						obj.optString("headImage"), 
						customListCell.getImageView(), 
						ImageLoaderConfigs.displayImageOptionsRound);
				
				//点击button 同意添加好友
				customListCell.setOnButtonListener(new onButtonListener() {
					
					@Override
					public void OnButtonListener() {
						mCustomListCell = customListCell;
						
						showDialogCustom(DIALOG_CUSTOM);
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("toUserIds", obj.optLong("id"));
						params.put("status", 4);
						DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserApprove, params, FriendsValidationActivity.this);
					}
				});
				return convertView;
			}
		};
		
	}

	@Override
	public void OnItemClickListener(AdapterView<?> parent, View view,
			int position, long id) {
		
		if(mDataArray != null && mDataArray.length() > 0){
			Intent intent = new Intent(FriendsValidationActivity.this, PersonPageActivity.class);
			//intent.putExtra("data", mDataArray.optJSONObject(position-1).toString());
			intent.putExtra("id", mDataArray.optJSONObject(position-1).optString("id"));
			startActivity(intent);
		}
		
	}

	@Override
	public void OnRefreshListener() {
		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("userId", "1");
		params.put("status", 2);
		params.put("type", 2);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserListFriends, params, FriendsValidationActivity.this);
	}

	@Override
	public void OnRemoreListener() {
	}

	
	@Override
	public void taskStarted(TaskType type) {
		super.taskStarted(type);
	}


	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		
		mListView.complete();
		
		if(result == null){
			removeDialogCustom();
			return;
		}
		
		if(result instanceof Error){
			removeDialogCustom();
			Toast.makeText(FriendsValidationActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserListFriends:
		removeDialogCustom();
			if(result instanceof JSONObject ){
				if(((JSONObject)result).has("items")){
					mDataArray = ((JSONObject)result).optJSONArray("items");
					if(mDataArray != null && mDataArray.length() > 0){
						if(mListAdapter != null){
							mListAdapter.notifyDataSetChanged();
						}
					}
				}
			}
			break;
		case TaskOrMethod_UserApprove:
			if(result instanceof JSONObject ){
				removeDialogCustom();
				/*if(((JSONObject)result).has("result")){
					if(((JSONObject)result).optInt("result") == 1){
						mCustomListCell.getButtonView().setVisibility(View.GONE);
						mCustomListCell.getTextView().setVisibility(View.VISIBLE);
						
						EventManager.getInstance().sendMessage(EventManager.EventType_RefreshAddressBook, 1);
						
					}else{
						Toast.makeText(FirendsValidationActivity.this, R.string.fail_to_add_friend, Toast.LENGTH_SHORT).show();
					}
				}*/
				EventManager.getInstance().sendMessage(EventManager.EventType_RefreshAddressBook, 1);
				
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("status", 2);
				params.put("type", 2);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserListFriends, params, FriendsValidationActivity.this);
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

	
	
}
