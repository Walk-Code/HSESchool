package com.zhuochuang.hsej;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.sortlistview.CharacterParser;
import com.layout.sortlistview.PinyinComparator;
import com.layout.sortlistview.SortAdapter;
import com.layout.sortlistview.SortModel;
import com.model.DataLoader;
import com.model.TaskType;

/**
 * for share
 * @author Administrator
 *
 */
@SuppressLint("DefaultLocale")
public class ContactListActivity extends BaseActivity{

	private PullToRefreshListView mListView;
	private SortAdapter adapter = null;
	private CharacterParser mCharacterParser = CharacterParser.getInstance();	//中文转字母
	private PinyinComparator mPinyinComparator = new PinyinComparator();	//
	
	private String [] dates = null;
	private List<SortModel> dataList = null;
	private JSONArray mDataArray = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pulltolistview);
		setTitleText(R.string.my_friends_title);
		
		mListView = (PullToRefreshListView)findViewById(R.id.puulto_listview);
		mListView.setAdapter(adapter = new SortAdapter(ContactListActivity.this));
		
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserListFriends, null, ContactListActivity.this);
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mDataArray == null || mDataArray.length() == 0){
					return;
				}
				
				String xm = "";
				try {
					String ids = dataList.get(arg2 -1).getId();
					xm = dataList.get(arg2 -1).getName();
					((HSESchoolApp)getApplication()).setShareToUserid(ids);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				String title = "";
				if(((HSESchoolApp)getApplication()).mShareObj != null){
					title = ((HSESchoolApp)getApplication()).mShareObj.optString("name");
				}
				new AlertDialog.Builder(ContactListActivity.this) 
				.setCancelable(false)
			 	.setMessage(String.format(getResources().getString(R.string.umeng_friend_sharemsg), title, xm))
			 	.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						showDialogCustom(DIALOG_CUSTOM);
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("operation", 4);
						params.put("resourceType", ((HSESchoolApp)getApplication()).mResourceType);
						params.put("resourceId", ((HSESchoolApp)getApplication()).mResourceId);
						params.put("toUserId", ((HSESchoolApp)getApplication()).mToUserId);
						DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserShare, params, ContactListActivity.this);
					}
				})
			 	.setNegativeButton(getResources().getString(R.string.cancel), null)
			 	.show();
			}
		});
		mListView.setRemoreable(false);
		mListView.startRefresh();
	}
	
	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		if(mDataArray == null || mDataArray.length() <= 0){
			return null;
		}
		
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			sortModel.setId(mDataArray.optJSONObject(i).optString("id"));
			sortModel.setHeadImage(mDataArray.optJSONObject(i).optString("headImage"));
			//汉字转换成拼音
			String pinyin = mCharacterParser.getSelling(date[i]);
			String sortString  = null;
			try{
				sortString = pinyin.substring(0, 1).toUpperCase();
			}catch(Exception e){
				System.out.println("fffff" + e);
			}
			// 正则表达式，判断首字母是否是英文字母
			if(sortString != null && sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		
		Collections.sort(mSortList, mPinyinComparator);
		return mSortList;
		
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		if(mListView != null){
			mListView.complete();
		}
		removeDialogCustom();
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(ContactListActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserListFriends:
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				mDataArray = ((JSONObject)result).optJSONArray("items");
				
				if(mDataArray != null && mDataArray.length() > 0){
					dates = new String[mDataArray.length()];
					for (int i = 0; i < mDataArray.length(); i++) {
						dates[i] = mDataArray.optJSONObject(i).optString("nickName");
					}
				}
				
				if(dates != null && dates.length > 0){
					dataList = filledData(dates);
				}
				
				if(dataList != null){
					adapter.updateListView(dataList);
				}
			}
			break;

		case TaskOrMethod_UserShare:
			Toast.makeText(ContactListActivity.this, getResources().getString(R.string.umeng_friend_share_success), Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ContactListActivity.this.finish();
				}
			}, 800);
			break;
		default:
			break;
		}
	}
}
