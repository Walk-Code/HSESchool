package com.zhuochuang.hsej;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.PullToRefreshExpandableListView;
import com.layout.PullToRefreshExpandableListView.OnRefreshListener;
import com.model.Configs;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.Utils;

@SuppressLint("InflateParams")
public class MyClassmateHobbyGroupFragment extends BaseFragment{

	private PullToRefreshExpandableListView mExListView;
	private BaseExpandableListAdapter mAdapter;
	
	private JSONArray mDataArr;
	
	private boolean mIsNeedRefresh = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mMainLayout = (ViewGroup) LayoutInflater.from(mActivity).inflate(R.layout.fragment_myclassmate_hobbygroup, null);
		initListView();
	}
	
	private void initListView(){
		mExListView = (PullToRefreshExpandableListView) mMainLayout.findViewById(R.id.pullto_exlistview);
		
		mExListView.setAdapter(mAdapter = new BaseExpandableListAdapter() {
			
			@Override
			public boolean isChildSelectable(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = LayoutInflater.from(mActivity).inflate(R.layout.listcell_myclassmate_hobbygroup_group, null);
					convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(mActivity, 25)));
				}
				
				JSONObject obj = (JSONObject) getGroup(groupPosition);
				if(obj != null){
					((TextView)convertView.findViewById(R.id.textview)).setText(
							String.format(mActivity.getResources().getString(R.string.hobbygroup_group), 
									obj.optString("name"), getChildrenCount(groupPosition)));
				}
				return convertView;
			}
			
			@Override
			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				if(mDataArr != null && mDataArr.length() > 0){
					return mDataArr.length();
				}
				return 0;
			}
			
			@Override
			public Object getGroup(int groupPosition) {
				// TODO Auto-generated method stub
				if(mDataArr != null && mDataArr.length() > 0){
					return mDataArr.optJSONObject(groupPosition);
				}
				return null;
			}
			
			@Override
			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				JSONObject obj = (JSONObject) getGroup(groupPosition);
				if(obj != null && obj.has("hobbyGroups")){
					JSONArray arr = obj.optJSONArray("hobbyGroups");
					if(arr != null && arr.length() > 0){
						return arr.length();
					}
				}
				return 0;
			}
			
			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = LayoutInflater.from(mActivity).inflate(R.layout.listcell_myclassmate_hobbygroup_child, null);
					convertView.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(mActivity, 50)));
				}
				JSONObject obj = (JSONObject) getChild(groupPosition, childPosition);
				if(obj != null){
					((TextView)convertView.findViewById(R.id.textview)).setText(obj.optString("name"));
					ImageLoader.getInstance().displayImage(obj.optString("image"), ((ImageView)convertView.findViewById(R.id.imageview)), ImageLoaderConfigs.displayImageOptionsPreRound);
				}
				return convertView;
			}
			
			@Override
			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				JSONObject obj = (JSONObject) getGroup(groupPosition);
				if(obj != null && obj.has("hobbyGroups")){
					JSONArray arr = obj.optJSONArray("hobbyGroups");
					if(arr != null && arr.length() > 0){
						return arr.optJSONObject(childPosition);
					}
				}
				return null;
			}
		});
		
		mExListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		mExListView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_HobbygroupListTypes, null, MyClassmateHobbyGroupFragment.this);
			}
		});
		
		mExListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				
				if(mDataArr != null && mDataArr.length() > 0){
					JSONObject obj = mDataArr.optJSONObject(groupPosition);
					if(obj != null && obj.has("hobbyGroups")){
						JSONArray arr = obj.optJSONArray("hobbyGroups");
						if(arr != null && arr.length() > 0){
							Intent intent = new Intent(mActivity, HobbyGroupListActivity.class);
							intent.putExtra("data", arr.optJSONObject(childPosition).toString());
							startActivity(intent);
						}
					}
				}
				
				return false;
			}
		});
		
		mExListView.startRefresh();
	}
	
	@Override
	protected void onReceive(String brodecast) {
		// TODO Auto-generated method stub
		super.onReceive(brodecast);
		if(brodecast.equalsIgnoreCase(Configs.LoginStateChange)){
			mIsNeedRefresh = true;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mIsNeedRefresh){
			mIsNeedRefresh = false;
			if (!mExListView.isStackFromBottom()) {
				mExListView.setStackFromBottom(true);
			}
			mExListView.setStackFromBottom(false);
			mExListView.startRefresh();
		}
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		
		if(mExListView != null){
			mExListView.onRefreshComplete();
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
		case TaskOrMethod_HobbygroupListTypes:
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				//mDataArr = null;
				mDataArr = ((JSONObject)result).optJSONArray("items");
			}
			mExListView.setAdapter(mAdapter);
			//mAdapter.notifyDataSetChanged();
			
			for (int i = 0; i < mExListView.getCount(); i++) {
				   mExListView.expandGroup(i);
			}
			break;

		default:
			break;
		}
	}

}
