package com.zhuochuang.hsej.store;

import org.json.JSONArray;
import org.json.JSONObject;

import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskType;
import com.util.ContentAdapter;
import com.zhuochuang.hsej.ListViewPullActivity;
import com.zhuochuang.hsej.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

public class StoreAddressPickerActivity extends ListViewPullActivity{

	JSONArray mAddressList;
	int mPickerIndex = 0;
	Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitleText(R.string.address_picker);
		findViewById(R.id.textview_right_text).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.textview_right_text)).setText(R.string.manager);
		mPickerIndex = getIntent().getIntExtra("index", 0);
		mListView.startRefresh();
		mListView.setRemoreable(false);
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case EventManager.EventType_UpdateAddressPicker:
					if(mListView != null){
						mListView.startRefresh();
					}
					break;
				default:
					break;
				}
			}
		});
	}

	public void onRightBtnClick(View view){
		Intent intent = new Intent(StoreAddressPickerActivity.this,StoreShoppingAddressActivity.class);
		startActivity(intent);
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
				if(mAddressList != null){
					return mAddressList.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = ViewGroup.inflate(StoreAddressPickerActivity.this, R.layout.listcell_store_picker, null);
				}
				JSONObject obj = mAddressList.optJSONObject(position);
				if(obj != null){
					((TextView) convertView.findViewById(R.id.textview_name)).setText(obj.optString("name"));
					((TextView) convertView.findViewById(R.id.textview_phone)).setText(obj.optString("phone"));
					((TextView) convertView.findViewById(R.id.textview_address)).setText(obj.optString("address"));
					if(obj.optInt("mark") == 1 ){
						convertView.findViewById(R.id.textview_default).setVisibility(View.VISIBLE);
					}
					else{
						convertView.findViewById(R.id.textview_default).setVisibility(View.GONE);
					}
					if(mPickerIndex == position){
						convertView.findViewById(R.id.view_picker).setVisibility(View.VISIBLE);
					}
					else{
						convertView.findViewById(R.id.view_picker).setVisibility(View.GONE);
					}
				}
				return convertView;
			}
		};
	}

	@Override
	public void OnItemClickListener(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if(mAddressList != null && mAddressList.length() > 0){
			JSONObject obj = mAddressList.optJSONObject(position - 1);
			if(obj != null){
				mPickerIndex = position - 1;
				if(mListAdapter != null){
					mListAdapter.notifyDataSetChanged();
				}
				EventManager.getInstance().sendMessage(EventManager.EventType_UpdateAddress, 
						obj.optString("name"), obj.optString("phone"), obj.optString("address"), mPickerIndex, obj.optString("id"));
				StoreAddressPickerActivity.this.finish();
			}
		}
	}

	@Override
	public void OnRefreshListener() {
		// TODO Auto-generated method stub
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserAddressListUserAddress, null, StoreAddressPickerActivity.this);
	}

	@Override
	public void OnRemoreListener() {
		// TODO Auto-generated method stub
		
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
			Toast.makeText(StoreAddressPickerActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(type == TaskType.TaskOrMethod_UserAddressListUserAddress){
			mAddressList = new JSONArray();
			if(((JSONObject)result).has("items")){
				mAddressList = ((JSONObject)result).optJSONArray("items");
			}
			if(mAddressList == null || mAddressList.length() == 0){
				EventManager.getInstance().sendMessage(EventManager.EventType_UpdateAddress, null);
			}
			if(mListAdapter != null){
				mListAdapter.notifyDataSetChanged();
			}
		}
	}
}
