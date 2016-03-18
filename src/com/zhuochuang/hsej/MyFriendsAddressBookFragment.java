package com.zhuochuang.hsej;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.sortlistview.CharacterParser;
import com.layout.sortlistview.PinyinComparator;
import com.layout.sortlistview.SideBar;
import com.layout.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.layout.sortlistview.SortAdapter;
import com.layout.sortlistview.SortModel;
import com.model.DataLoader;
import com.model.DefineHandler;
import com.model.EventManager;
import com.model.TaskType;
/**
 * 通讯录
 * @author Dion
 *
 */
/**
 * 通讯录
 * @author Dion
 *modify kris
 */
public class MyFriendsAddressBookFragment extends BaseFragment{

	private PullToRefreshListView mListView = null;
	
	private SortAdapter adapter = null;
	private SideBar mSideBar = null;
	private CharacterParser mCharacterParser = CharacterParser.getInstance();	//中文转字母
	private PinyinComparator mPinyinComparator = new PinyinComparator();	//
	
	
	private String [] dates = null;
	private List<SortModel> dataList = null;
	private JSONArray mDataArray = null;
	private View mHeaderView = null;
	
	private Handler mHandler = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mMainLayout = (ViewGroup)LayoutInflater.from(mActivity).inflate(R.layout.fragment_address_book, null);
		
		initListView();
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				Object[] objs = (Object[])msg.obj;
				switch (msg.what) {
				case EventManager.EventType_RefreshAddressBook:
					if(objs != null && objs.length>0){
						if((Integer)objs[0] == 1){
							if(mListView != null){
								if (!mListView.isStackFromBottom()) {
									mListView.setStackFromBottom(true);
								}
								mListView.setStackFromBottom(false);
								mListView.startRefresh();

							}
						}
					}
					break;

				default:
					break;
				}
				
				
			}
		}); 
	}
	
	private void initListView(){
		
		mListView = (PullToRefreshListView)mMainLayout.findViewById(R.id.firend_pullToRefresh_ListView);
		
		//new firends
		mHeaderView = LayoutInflater.from(mActivity).inflate(R.layout.header_newfirend_address_book, null);
		mListView.addHeaderView(mHeaderView);
		if(DefineHandler.getMsgNotifyType(mActivity, DefineHandler.MsgType_FriendContact) != 0){
			mHeaderView.findViewById(R.id.newfirend_state).setVisibility(View.VISIBLE);
		}
		else{
			mHeaderView.findViewById(R.id.newfirend_state).setVisibility(View.GONE);
		}
		mHeaderView.findViewById(R.id.new_firends).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent =	new Intent(mActivity, FriendsValidationActivity.class);
				startActivity(intent);
				
				mHeaderView.findViewById(R.id.newfirend_state).setVisibility(View.GONE);
				DefineHandler.updateMsgNotify(mActivity, DefineHandler.MsgType_FriendContact);
				EventManager.getInstance().sendMessage(EventManager.EventType_MsgChange, new Object());
			}
		});
		
		adapter = new SortAdapter(mActivity);
		mListView.setAdapter(adapter);
		
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				HashMap<String, Object> params = new HashMap<String, Object>();
//				params.put("userId", "1");
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserListFriends, params, MyFriendsAddressBookFragment.this);
			}
		});
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mDataArray == null || mDataArray.length() == 0){
					return;
				}
				
				String id = dataList.get(arg2 -2).getId();
				if(id.equalsIgnoreCase(DataLoader.getInstance().getHeaderUsetId())){
					startActivity(new Intent(mActivity, MyPageActivity.class));
				}
				else{
					Intent intent = new Intent(mActivity, PersonPageActivity.class);
					intent.putExtra("id", id);
					startActivity(intent);
				}
			}
		});
		
		mSideBar = (SideBar)mMainLayout.findViewById(R.id.sidrbar);
		
		mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					mListView.setSelection(position);
				}
			}
		});
		
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
	public void onDestroy() {
		super.onDestroy();
		EventManager.getInstance().removeHandlerListenner(mHandler);
	}

	@Override
	public void taskStarted(TaskType type) {
		// TODO Auto-generated method stub
		super.taskStarted(type);
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
			try {
				Toast.makeText(mActivity, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserListFriends:
			if(result instanceof JSONObject){
				if(((JSONObject)result).has("items")){
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
			}
			
			if(mDataArray == null || mDataArray.length() == 0){
				mHeaderView.findViewById(R.id.textview_msgnull).setVisibility(View.VISIBLE);
			}
			else{
				mHeaderView.findViewById(R.id.textview_msgnull).setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void taskIsCanceled(TaskType type) {
		// TODO Auto-generated method stub
		super.taskIsCanceled(type);
	}
}
