package com.zhuochuang.hsej.store;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskType;
import com.util.ContentAdapter;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.R;


public class StoreShoppingAddressActivity extends BaseActivity{
	private ListView mListView;
	private JSONArray mItemArr;
	private ContentAdapter mAdapter;
	private static final int RESULTCODE = Activity.RESULT_OK;
	private int mIndex = 0;
	boolean mIsOpenView = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		setContentView(R.layout.store_choose_address);
		setTitleText(R.string.contacter_address);
		mIndex = getIntent().getIntExtra("index", 0);
		initView();
		showDialogCustom(DIALOG_CUSTOM);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserAddressListUserAddress, null, this);
	}
	
	private void initView() {
		mListView = (ListView) findViewById(R.id.store_choose_address_listview);
		mListView.setAdapter(mAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				if(mItemArr != null){
					return mItemArr.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = LayoutInflater.from(StoreShoppingAddressActivity.this).inflate(R.layout.store_shopping_address_item, null);
				}
				JSONObject obj = mItemArr.optJSONObject(position);
				((TextView) convertView.findViewById(R.id.receiving_name)).setText(obj.optString("name").toString());
				((TextView) convertView.findViewById(R.id.receiving_phone)).setText(obj.optString("phone").toString());
				((TextView) convertView.findViewById(R.id.receiving_address)).setText(obj.optString("address").toString());
				if(obj.optInt("mark") == 1 ){
				 ((TextView) convertView.findViewById(R.id.address_default)).setVisibility(View.VISIBLE);
				}
				else{
				 ((TextView) convertView.findViewById(R.id.address_default)).setVisibility(View.GONE);
				}
				return convertView;
			}
		  });
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(null != mItemArr){
					JSONObject obj = mItemArr.optJSONObject(position);
					Intent intent = new Intent(StoreShoppingAddressActivity.this,StoreUpdateShoppingAddressActivity.class);
					intent.putExtra("Name", obj.optString("name"));
					intent.putExtra("Phone", obj.optString("phone"));
					intent.putExtra("Address", obj.optString("address"));
					intent.putExtra("mark", obj.optInt("mark", 0));
					intent.putExtra("id", obj.optString("id"));
					startActivity(intent);
				}
			}
		});
		
		findViewById(R.id.add_address).setVisibility(View.VISIBLE);
		//添加新地址
		findViewById(R.id.store_add_address).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(StoreShoppingAddressActivity.this, StoreAddAddressActivity.class));
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mIsOpenView){
			showDialogCustom(DIALOG_CUSTOM);
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserAddressListUserAddress, null, this);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((HSESchoolApp)getApplication()).removeActivityStore(this);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		removeDialogCustom();
		mIsOpenView = true;
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(StoreShoppingAddressActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(type == TaskType.TaskOrMethod_UserAddressListUserAddress){
			mItemArr = new JSONArray();
			if(((JSONObject)result).has("items")){
				mItemArr = ((JSONObject)result).optJSONArray("items");
			}
			if(mItemArr == null || mItemArr.length() == 0){
				EventManager.getInstance().sendMessage(EventManager.EventType_UpdateAddress, null);
			}
			if(mAdapter != null){
				mAdapter.notifyDataSetChanged();
			}
		}
	}
}
