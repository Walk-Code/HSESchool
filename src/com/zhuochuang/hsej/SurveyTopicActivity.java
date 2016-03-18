package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.SurveyTopicListCellView;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskType;
import com.util.ContentAdapter;
import com.util.Utils;

public class SurveyTopicActivity extends ListViewActivity{

	private View mHeaderView;
	private JSONObject mDataObj;
	private JSONArray mDataArr;
	private OnCheckListener mOnCheckListener;
	private HashMap<String, String> mCheckMap = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitleText(R.string.survey_title);
		mMainLayout.setBackgroundColor(Color.WHITE);
		mListView.setVisibility(View.GONE);
		setOnCheckListener();
		showDialogCustom(DIALOG_CUSTOM);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("resourceType", getIntent().getIntExtra("resouceType", 0));
		params.put("resourceId", getIntent().getStringExtra("id"));
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_SurveyGetTopic, params, SurveyTopicActivity.this);
	}

	@Override
	public void getHeaderView() {
		// TODO Auto-generated method stub
		mHeaderView = View.inflate(SurveyTopicActivity.this, R.layout.header_surveytopic, null);
		mListView.addHeaderView(mHeaderView, null, false);
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
					convertView = new SurveyTopicListCellView(SurveyTopicActivity.this);
				}
				JSONObject obj = mDataArr.optJSONObject(position);
				((SurveyTopicListCellView)convertView).setData(SurveyTopicActivity.this, obj, position, mOnCheckListener);
				return convertView;
			}
		};
	}

	@Override
	public void getFooterView() {
		// TODO Auto-generated method stub
		View footerView = View.inflate(SurveyTopicActivity.this, R.layout.footer_survey, null);
		mListView.addFooterView(footerView);
	}

	@Override
	public void OnItemClickListener(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		
	}
	
	private void setHeaderData(){
		if(mDataObj == null || mHeaderView == null){
			return;
		}
		((TextView)mHeaderView.findViewById(R.id.textview_title)).setText(mDataObj.optString("name"));
		((TextView)mHeaderView.findViewById(R.id.textview_time)).setText(Utils.getTimeQuantum(SurveyTopicActivity.this, 
				mDataObj.optLong("startTime", 0), mDataObj.optLong("endTime", 0)));
	}
	
	public void onSurveyCommit(View view){
		if(mDataObj == null || mDataArr == null || mDataArr.length() == 0){
			return;
		}
		
		if(mCheckMap == null || mCheckMap.size() == 0){
			for(int i = 0; i < mDataArr.length(); i++){
				JSONObject obj = mDataArr.optJSONObject(i);
				if(obj != null && !obj.optString("type").equalsIgnoreCase("2")){
					Toast.makeText(SurveyTopicActivity.this, String.format(getResources().getString(R.string.survey_option_min), obj.optString("name"), 
									obj.optString("minNumber", "1")), Toast.LENGTH_SHORT).show();
					return;
				}
			}
			return;
		}
		
		for(int i = 0; i < mDataArr.length(); i++){
			JSONObject obj = mDataArr.optJSONObject(i);
			//文本
			if(obj != null && !obj.optString("type").equalsIgnoreCase("2")){
				if(!mCheckMap.containsKey(obj.optString("id"))){
					Toast.makeText(SurveyTopicActivity.this, 
							String.format(getResources().getString(R.string.survey_option_min), obj.optString("name"), "1"), Toast.LENGTH_SHORT).show();
					return;
				}
				//多选
				if(obj.optString("type").equalsIgnoreCase("1")){
					boolean isUnderMinNumber = false;
					String values = mCheckMap.get(obj.optString("id"));
					if(values != null && values.length() > 0){
						if(values.contains(",")){
							String[] valueArr = values.split(",");
							if(valueArr == null || valueArr.length < obj.optInt("minNumber", 1)){
								isUnderMinNumber = true;
							}
						}
						else{
							if(obj.optInt("minNumber", 1) > 1){
								isUnderMinNumber = true;
							}
						}
					}
					else{
						isUnderMinNumber = true;
					}
					if(isUnderMinNumber){
						Toast.makeText(SurveyTopicActivity.this, String.format(getResources().getString(R.string.survey_option_min), obj.optString("name"), 
										obj.optString("minNumber")), Toast.LENGTH_SHORT).show();
						return;
					}
				}
			}
		}
		
		JSONObject paramsObj = new JSONObject();
		try {
			paramsObj.put("id", mDataObj.optString("id"));
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
		JSONArray arr = new JSONArray();
		for(String key : mCheckMap.keySet()){
			JSONObject obj = new JSONObject();
			try {
				obj.put("id", key);
				String s = mCheckMap.get(key);
				if(s.contains("other___")){
					obj.put("other", s.split("___")[1]);
				}
				else{
					obj.put("value", s);
				}
				arr.put(obj);
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			paramsObj.put("items", arr);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		showDialogCustom(DIALOG_CUSTOM);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("items", paramsObj.toString());
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_SurveySubmit, params, SurveyTopicActivity.this);
		/*for(int i = 0; i < mListAdapter.getCount(); i++){
			View convertView = mListAdapter.getView(i, null, null);
			if(convertView != null && convertView instanceof SurveyTopicListCellView){
				System.out.println("==99===" + ((SurveyTopicListCellView)convertView).getIds() 
						+ "==" + ((SurveyTopicListCellView)convertView).getValues()
						+ "==" + ((SurveyTopicListCellView)convertView).getTypes());
			}
		}*/
	}
	
	public interface OnCheckListener{
		public void onCheckChange(String id, String value);
	}
	
	public void setOnCheckListener(){
		mOnCheckListener = new OnCheckListener() {
			@Override
			public void onCheckChange(String id, String value) {
				// TODO Auto-generated method stub
				if(value == null || value.length() == 0){
					if(mCheckMap.containsKey(id)){
						mCheckMap.remove(id);
					}
				}
				else{
					mCheckMap.put(id, value);
				}
			}
		};
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
			Toast.makeText(SurveyTopicActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
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
			if(result instanceof JSONObject){
				
				EventManager.getInstance().sendMessage(EventManager.EventType_SurveyChange, getIntent().getStringExtra("id"));
				Intent intent = new Intent(SurveyTopicActivity.this, SurveyTopicResultActivity.class);
				intent.putExtra("id", getIntent().getStringExtra("id"));
				intent.putExtra("resouceType", getIntent().getIntExtra("resouceType", 0));
				startActivity(intent);
				SurveyTopicActivity.this.finish();
			}
			break;
		default:
			break;
		}
	}

}
