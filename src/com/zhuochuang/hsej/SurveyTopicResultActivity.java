package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.SurveyTopicResultListCellView;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.ContentAdapter;
import com.util.Utils;

public class SurveyTopicResultActivity extends ListViewActivity{

	private View mHeaderView;
	private View mHeaderView2;
	private JSONObject mDataObj;
	private JSONArray mDataArr;
	private int mScreenWidth;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitleText(R.string.survey_result);
		mMainLayout.setBackgroundColor(Color.WHITE);
		mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		showDialogCustom(DIALOG_CUSTOM);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("resourceType", getIntent().getIntExtra("resouceType", 0));
		params.put("resourceId", getIntent().getStringExtra("id"));
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_SurveyGetTopic, params, SurveyTopicResultActivity.this);

	}

	@Override
	public void getHeaderView() {
		// TODO Auto-generated method stub
		mHeaderView = View.inflate(SurveyTopicResultActivity.this, R.layout.header_surveytopic, null);
		mListView.addHeaderView(mHeaderView, null, false);
		
		mHeaderView2 = View.inflate(SurveyTopicResultActivity.this, R.layout.header_survey_result, null);
		mHeaderView2.setVisibility(View.GONE);
		mListView.addHeaderView(mHeaderView2, null, false);
	}

	@Override
	public void getAdapter() {
		// TODO Auto-generated method stub
		mListAdapter = new ContentAdapter(){

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
					convertView = new SurveyTopicResultListCellView(SurveyTopicResultActivity.this);
				}
				JSONObject obj = mDataArr.optJSONObject(position);
				((SurveyTopicResultListCellView)convertView).setData(SurveyTopicResultActivity.this, obj, position, mDataObj.optInt("voteNum"), mScreenWidth);
				return convertView;
			}
		};
	}

	@Override
	public void getFooterView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnItemClickListener(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		
	}
	
	private void setHeaderData(){
		if(mDataObj != null && mHeaderView != null){
			mHeaderView.findViewById(R.id.textview_joinnum).setVisibility(View.VISIBLE);
			
			((TextView)mHeaderView.findViewById(R.id.textview_joinnum)).setText(String.format(getResources().getString(R.string.survey_result_personnum), mDataObj.optString("voteNum")));
			((TextView)mHeaderView.findViewById(R.id.textview_title)).setText(mDataObj.optString("name"));
			((TextView)mHeaderView.findViewById(R.id.textview_time)).setText(Utils.getTimeQuantum(SurveyTopicResultActivity.this, 
					mDataObj.optLong("startTime", 0), mDataObj.optLong("endTime", 0)));
		}
		if(mDataObj != null && mHeaderView2 != null){
			mHeaderView2.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);

		removeDialogCustom();
		
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(SurveyTopicResultActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_SurveyGetTopic:
			if(result instanceof JSONObject && ((JSONObject)result).has("item")){
				mListView.setVisibility(View.VISIBLE);
				mDataObj = ((JSONObject)result).optJSONObject("item");
				setHeaderData();
				if(mDataObj != null && mDataObj.has("items")){
					mDataArr = mDataObj.optJSONArray("items");
				}
			}
			if(mListAdapter != null){
				mListAdapter.notifyDataSetChanged();
			}
			break;

		case TaskOrMethod_SurveySubmit:
			break;
		default:
			break;
		}
	}
}
