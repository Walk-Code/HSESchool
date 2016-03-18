package com.layout;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.util.Utils;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.SurveyTopicActivity.OnCheckListener;

public class SurveyTopicListCellView extends LinearLayout{

	private View mConvertView;
	private LinearLayout mGroupCheck;
	
	private JSONArray mOptionsArr;
	private HashMap<String, Boolean> mCheckStatu = new HashMap<String, Boolean>();
	
	private SurveyTopicCheckView[] mSurveyTopicCheckView;
	private int mType = 0;
	private JSONObject mOptionsItemObj;
	private String mType2Str;
	private String mId;
	private String mValueStr;
	private OnCheckListener mOnCheckListener;
	
	private Context mContext;
	
	public SurveyTopicListCellView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public SurveyTopicListCellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context){
		mContext = context;
		mConvertView = View.inflate(context, R.layout.listcell_surveytopic_most, null);
		mGroupCheck = (LinearLayout) mConvertView.findViewById(R.id.group_check);
		this.addView(mConvertView);
	}
	
	public void setData(Context context, JSONObject obj, int position, OnCheckListener listener){
		if(obj == null || mConvertView == null){
			return;
		}
		mOptionsItemObj = obj;
		mOnCheckListener = listener;
		mId = obj.optString("id");
		mType = obj.optInt("type");
		
		((TextView)mConvertView.findViewById(R.id.textview_position)).setText((position + 1) + ":");
		((TextView)mConvertView.findViewById(R.id.textview_name)).setText(obj.optString("name"));
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = Utils.dipToPixels(context, 20);
		lp.topMargin = Utils.dipToPixels(context, 5);
		lp.rightMargin = Utils.dipToPixels(context, 15);
		
		mGroupCheck.removeAllViewsInLayout();
		if(mType == 2){
			SurveyTopicCheckView surveyTopicCheckView = new SurveyTopicCheckView(context);
			surveyTopicCheckView.setData(obj, mType, getChackStatu(obj.optString("id")), mOnCheckListener);
			surveyTopicCheckView.setOnClickListener(new OnOptionsClick(obj.optString("id")));
			mGroupCheck.addView(surveyTopicCheckView, lp);
		}
		else{
			mOptionsArr = obj.optJSONArray("items");
			if(mOptionsArr != null && mOptionsArr.length() > 0){
				//if(mSurveyTopicCheckView == null){
					mSurveyTopicCheckView = new SurveyTopicCheckView[mOptionsArr.length()];
				//}
				for(int i = 0; i < mOptionsArr.length(); i++){
					JSONObject options = mOptionsArr.optJSONObject(i);
					if(mSurveyTopicCheckView[i] == null){
						mSurveyTopicCheckView[i] = new SurveyTopicCheckView(context);
					}
					mSurveyTopicCheckView[i].setData(options, mType, getChackStatu(options.optString("id")), mOnCheckListener);
					mSurveyTopicCheckView[i].setOnClickListener(new OnOptionsClick(options.optString("id")));
					mGroupCheck.addView(mSurveyTopicCheckView[i], lp);
					if(mType == 2){
						break;
					}
				}
			}
		}
	}
	
	class OnOptionsClick implements OnClickListener{

		private String optionsId;
		
		public OnOptionsClick(String id){
			optionsId = id;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (mType) {
			case 0:
				if(mOptionsArr != null && mSurveyTopicCheckView != null){
					for(int i = 0; i < mOptionsArr.length(); i++){
						mSurveyTopicCheckView[i].setCheckStatu(false);
					}
					mCheckStatu.clear();
				}
				break;
			}
			if(mCheckStatu.containsKey(optionsId)){
				mCheckStatu.remove(optionsId);
			}
			else{
				/*if(mCheckStatu.size() >= mOptionsItemObj.optInt("maxNumber", 1)){
					Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.survey_option_max), mOptionsItemObj.optString("maxNumber")), Toast.LENGTH_SHORT).show();
					return;
				}*/
				mCheckStatu.put(optionsId, true);
			}
			mValueStr = "";
			for(String id : mCheckStatu.keySet()){
				mValueStr += id + ",";
			}
			if(mValueStr != null && mValueStr.contains(",")){
				mValueStr = mValueStr.substring(0, mValueStr.length() - 1);
			}
			
			mOnCheckListener.onCheckChange(mId, mValueStr);
			
			((SurveyTopicCheckView)v).setCheckStatu(getChackStatu(optionsId));
		}
	}
	
	private boolean getChackStatu(String id){
		if(mCheckStatu == null || mCheckStatu.size() == 0 || !mCheckStatu.containsKey(id)){
			return false;
		}
		
		return mCheckStatu.get(id);
	}
	
	public HashMap<String, Boolean> getChackStatu(){
		if(mCheckStatu == null){
			mCheckStatu = new HashMap<String, Boolean>();
		}
		
		return mCheckStatu;
	}
	
	public int getTypes(){
		return mType;
	}
	
	public String getIds(){
		return mId;
	}
	
	public String getValues(){
		return mValueStr;
	}
}
