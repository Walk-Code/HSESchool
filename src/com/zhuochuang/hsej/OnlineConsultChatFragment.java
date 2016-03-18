package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.CustomCommentView;
import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.emoji.EmojiUtils;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.Utils;

@SuppressLint("InflateParams")
public class OnlineConsultChatFragment extends BaseFragment{
	
	private PullToRefreshListView mListView;
	private ChatMsgViewAdapter mListAdapter;
	private JSONArray mDataArr;
	private TextView mBtnSend;
	private int mPageSize = 10, mPageNo = 1;
	private long mFriendId; 
	
	private CustomCommentView mCustomCommentView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mMainLayout = (ViewGroup)LayoutInflater.from(mActivity).inflate(R.layout.activity_feedback, null);
		initView();
		mCustomCommentView = (CustomCommentView) mMainLayout.findViewById(R.id.custom_commentview);
		mCustomCommentView.setOnSendListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onSendClick();
			}
		});
	}
	
	protected void initView(){
		mListView = (PullToRefreshListView)mMainLayout.findViewById(R.id.listView);
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("pageSize", mPageSize);
				params.put("pageNo", mPageNo);
				params.put("resourceType", "13");
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_MessageListAdminMsgs, params, OnlineConsultChatFragment.this);
			}
		});
		JSONObject userInfo = DataLoader.getInstance().getUserInfoObj();
		
		String fromId = "";
		String fromHeader = "";
		if(userInfo != null){
			fromId = userInfo.optString("id");
			fromHeader = userInfo.optString("headImage");
		}
		
		mListAdapter = new ChatMsgViewAdapter(mActivity, fromId, fromHeader);
		mListAdapter.setMsgType(13);
		mListView.setAdapter(mListAdapter);
		mListView.startRefresh();
		
		/*mBtnSend = (TextView) mMainLayout.findViewById(R.id.btn_send);
		mBtnSend.setEnabled(false);
    	mBtnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				send();
			}
		});
    	mEditTextContent = (EditText) mMainLayout.findViewById(R.id.et_sendmessage);
    	mEditTextContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String content = mEditTextContent.getText().toString();
				if(content == null || content.length() == 0 || content.replaceAll(" ", "").length() == 0){
					mBtnSend.setTextColor(Color.argb(255, 54, 54, 54));
					mBtnSend.setBackgroundResource(R.drawable.send_bg_gray);
					mBtnSend.setEnabled(false);
				}
				else{
					mBtnSend.setTextColor(Color.argb(255, 241, 241, 241));
					mBtnSend.setBackgroundResource(R.drawable.btn_red_selector);
					mBtnSend.setEnabled(true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});*/
	}

	/*private void send(){
		
		String sendMsg = mEditTextContent.getText().toString();
		if(sendMsg == null || sendMsg.length() == 0 || sendMsg.replaceAll(" ", "").length() == 0){
			Toast.makeText(mActivity, getString(R.string.feedback_please_input), Toast.LENGTH_SHORT).show();
			return;
		}
		
		showDialogCustom();
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("toResource", "13");
		params.put("content", sendMsg);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_MessageSendMsg, params, this);
	}*/
	
	public void onSendClick(){
		if(!Utils.isInternetAvaiable(mActivity)){
			Toast.makeText(mActivity, getResources().getString(R.string.internet_avaiable_false), Toast.LENGTH_SHORT).show();
			return;
		}
		String sendMsg = mCustomCommentView.getEditView().getText().toString();
		if(sendMsg == null || sendMsg.length() == 0 || sendMsg.replaceAll(" ", "").length() == 0){
			Toast.makeText(mActivity, getString(R.string.feedback_please_input), Toast.LENGTH_SHORT).show();
			return;
		}
		showDialogCustom();
			
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("toResource", "13");
		params.put("content", EmojiUtils.convertToUnicode(sendMsg));
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_MessageSendMsg, params, OnlineConsultChatFragment.this);
	}
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		
		if(mListView != null){
			mListView.onRefreshComplete();
		}
		removeDialogCustom();
       
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(mActivity, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}

		switch (type) {
		case TaskOrMethod_MessageListAdminMsgs:
			if(result instanceof JSONObject && ((JSONObject) result).has("items")){
				if(mPageNo == 1){
					mDataArr = null;
				}
				JSONArray newArr = ((JSONObject) result).optJSONArray("items"); 
				if(newArr != null && newArr.length() > 0){
					EmojiUtils.replaceEmoji(newArr);
					int lastIndex = newArr.length();
					mDataArr = DataLoader.getInstance().joinJSONArray(newArr, mDataArr);
					
					mListAdapter.setData(mDataArr);
					mListAdapter.notifyDataSetChanged();
					
					if(mPageNo == 1){
						mListView.setSelection(mDataArr.length() - 1);
					}else {
						mListView.setSelection(lastIndex - 1);
					}

					mPageNo ++;
				}
			}
			break;
		case TaskOrMethod_MessageSendMsg:
			if(result instanceof JSONObject && ((JSONObject) result).has("item")){
				if(mCustomCommentView != null){
					mCustomCommentView.resetCustomEditview(mActivity);
				}
				//Utils.removeSoftKeyboard(mActivity);
				
				JSONObject tempObj = ((JSONObject) result).optJSONObject("item");
				
				if(tempObj != null){
					EmojiUtils.replaceEmoji(tempObj);
					JSONObject newMsg = new JSONObject();
					try {
						newMsg.put("id", tempObj.optString("id"));
						newMsg.put("createDate", tempObj.optString("createDate"));
						newMsg.put("content", tempObj.optString("content"));
						
						String fromId = "";
						String fromName = "";
						JSONObject userInfo = DataLoader.getInstance().getUserInfoObj();
						if(userInfo != null){
							fromId = userInfo.optString("id");
							fromName = userInfo.optString("headImage");
						}
						JSONObject fromUser = new JSONObject();
						fromUser.put("id", fromId);
						fromUser.put("nickName", fromName);
						newMsg.put("fromUser", fromUser);
						
						if(mDataArr == null){
							mDataArr = new JSONArray();
						}
						mDataArr.put(newMsg);
						mListAdapter.setData(mDataArr);
						mListAdapter.notifyDataSetChanged();
						mListView.setSelection(mDataArr.length() - 1);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			
			break;
		
			default:
				break;
		}
	}
	

}
