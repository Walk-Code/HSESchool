package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.TaskType;
import com.util.ContentAdapter;
import com.util.Utils;

/**
 * 通告列表
 * @author Dion
 *
 */
@SuppressLint("InflateParams")
public class AnnouncementActivity extends ListViewPullActivity{

	private JSONArray mDataArr; //通知集
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitleText(R.string.announcement_title);
		mListView.setBackgroundColor(Color.TRANSPARENT);
		mListView.setRemoreable(false);
		mListView.startRefresh();
		
	}

	@Override
	public void getHeaderView() {
		
	}
	
	@Override
	public void getAdapter() {
		
		mListAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				if(mDataArr != null &&mDataArr.length() >0){
					return mDataArr.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				if(convertView == null){
					convertView = LayoutInflater.from(AnnouncementActivity.this).inflate(R.layout.listcell_announcement, null);
				}
				
				JSONObject mDataObj = mDataArr.optJSONObject(position);
				
				//标题
				((TextView)convertView.findViewById(R.id.announcement_title)).setText(mDataObj.optString("name"));
				//通知内容
				((TextView)convertView.findViewById(R.id.announcement_content)).setText(mDataObj.optString("content"));
				//通知创建时间
				((TextView)convertView.findViewById(R.id.announcement_time)).setText(Utils.getAlmostTime(AnnouncementActivity.this, mDataObj.optLong("createDate")));
				
				if(mDataObj.has("isUrgent") && mDataObj.optBoolean("isUrgent")){
					convertView.findViewById(R.id.icon_urgent_announcement).setVisibility(View.VISIBLE);
				}else{
					convertView.findViewById(R.id.icon_urgent_announcement).setVisibility(View.GONE);
				}
				
				return convertView;
			}
		};
	}

	@Override
	public void OnItemClickListener(AdapterView<?> parent, View view,
			int position, long id) {
		
		if(mDataArr != null && mDataArr.optJSONObject(position-1).has("id")){
			/*Intent mIntent = new Intent(AnnouncementActivity.this, AnnouncementDetailsActivity.class);
			mIntent.putExtra("informationId", mDataArr.optJSONObject(position-1).optLong("id"));
			startActivity(mIntent);*/
			
			Intent intent = new Intent(AnnouncementActivity.this, InformationDetailsActivity.class);
			intent.putExtra("id", mDataArr.optJSONObject(position-1).optString("id"));
			intent.putExtra("resouceType", 4);
			startActivity(intent);
		}
		
	}

	@Override
	public void OnRefreshListener() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("typeId", getIntent().getStringExtra("id"));
		params.put("pageNo", 1);
		params.put("pageSize", 20);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_InformationListInformations, params, AnnouncementActivity.this);
	}

	@Override
	public void OnRemoreListener() {
	}
	
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		
		mListView.complete();
		
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(AnnouncementActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_InformationListInformations:
			if(result instanceof JSONObject){
				
				if(((JSONObject) result).has("items")){
					mDataArr = ((JSONObject) result).optJSONArray("items");
				}
				
				if(mListAdapter != null){
					mListAdapter.notifyDataSetChanged();
				}
			}
			break;

		default:
			break;
		}
	}
}
