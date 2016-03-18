package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.PullToRefreshListView.OnRemoreListener;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.ContentAdapter;
import com.util.Utils;

public class MyIntegralActivity extends BaseActivity{

	private View mHeaderView;
	private PullToRefreshListView mListView;
	private ContentAdapter mAdapter;
	private JSONArray mDataArr;
	
	private boolean mIsReadmore = false;
	private int mPageNo = 1;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_myintegral);
		setTitleText(R.string.mycenter_cell4);
		findViewById(R.id.textview_jfsm).setVisibility(View.VISIBLE);
		
		mHeaderView = View.inflate(MyIntegralActivity.this, R.layout.listcell_myintegral_header, null);
		
		mListView = (PullToRefreshListView) findViewById(R.id.pullto_listview);
		mListView.addHeaderView(mHeaderView);
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
					convertView = View.inflate(MyIntegralActivity.this, R.layout.listcell_myintegral, null);
				}
				JSONObject obj = mDataArr.optJSONObject(position);
				if(obj != null){
					int count = obj.optInt("integral");
					if(count > 0){
						((TextView)convertView.findViewById(R.id.textview_count)).setTextColor(Color.argb(255, 215, 81, 72));
						((TextView)convertView.findViewById(R.id.textview_count)).setText("+" + count);
					}
					else{
						((TextView)convertView.findViewById(R.id.textview_count)).setTextColor(Color.argb(255, 72, 215, 136));
						((TextView)convertView.findViewById(R.id.textview_count)).setText(count +"");
					}
					((TextView)convertView.findViewById(R.id.textview_title)).setText(obj.optString("optitle"));
					((TextView)convertView.findViewById(R.id.textview_date)).setText(Utils.getIntegralDate(MyIntegralActivity.this, obj.optLong("createDate", 0)));
				}
				return convertView;
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
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserListIntegrals, params, MyIntegralActivity.this);
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
				try {
					params.put("pageNo", mPageNo);
					params.put("pageSize", 20);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserListIntegrals, params, MyIntegralActivity.this);
			}
		});
		
		mListView.startRefresh();
	}
	
	public void onJfsmClick(View view){
		Intent intent = new Intent(MyIntegralActivity.this, WebViewActivity.class);
		String url = DataLoader.getInstance().getSettingKey("integralUrl");
		intent.putExtra("url", url);
		startActivity(intent);
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
			Toast.makeText(MyIntegralActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserListIntegrals:
			if(result instanceof JSONObject){
				if(mHeaderView != null){
					//((TextView)mHeaderView.findViewById(R.id.textview_all)).setText(((JSONObject)result).optString("integral"));
					int count = ((JSONObject)result).optInt("integral");
					/*if(count > 0){
						((TextView)mHeaderView.findViewById(R.id.textview_count)).setTextColor(Color.argb(255, 215, 81, 72));
						((TextView)mHeaderView.findViewById(R.id.textview_count)).setText("+" + count);
					}
					else{
						((TextView)mHeaderView.findViewById(R.id.textview_count)).setTextColor(Color.argb(255, 72, 215, 136));
						((TextView)mHeaderView.findViewById(R.id.textview_count)).setText(count +"");
					}*/
					((TextView)mHeaderView.findViewById(R.id.textview_count)).setText(count +"");
					DataLoader.getInstance().updateUsetInfoKey("integral", ((JSONObject)result).optString("integral"));
				}
				
				if(((JSONObject)result).has("items")){
					JSONArray temp = ((JSONObject)result).optJSONArray("items");
					if(temp != null && temp.length() > 10){
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
