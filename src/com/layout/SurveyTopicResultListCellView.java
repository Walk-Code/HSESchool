package com.layout;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.util.Utils;
import com.zhuochuang.hsej.R;

public class SurveyTopicResultListCellView extends LinearLayout{

	private View mConvertView;
	private LinearLayout mGroupResult;
	private SurveyTopicResultPercenView[] mSurveyTopicResultPercenView;
	
	private JSONArray mOptionsArr;
	
	public SurveyTopicResultListCellView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public SurveyTopicResultListCellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context){
		mConvertView = View.inflate(context, R.layout.listcell_surveytopic_result, null);
		mGroupResult = (LinearLayout) mConvertView.findViewById(R.id.group_result);
		this.addView(mConvertView);
	}

	public void setData(Context context, JSONObject obj, int position, int voteNum, int screenWidth){
		if(obj == null || mConvertView == null){
			return;
		}
		
		((TextView)mConvertView.findViewById(R.id.textview_position)).setText((position + 1) + ":");
		((TextView)mConvertView.findViewById(R.id.textview_name)).setText(obj.optString("name"));
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.topMargin = Utils.dipToPixels(context, 5);
		lp.bottomMargin = Utils.dipToPixels(context, 5);
		mGroupResult.removeAllViewsInLayout();
		mOptionsArr = obj.optJSONArray("items");
		if(mOptionsArr != null && mOptionsArr.length() > 0){
			mSurveyTopicResultPercenView = new SurveyTopicResultPercenView[mOptionsArr.length()];
			for(int i = 0; i < mOptionsArr.length(); i++){
				JSONObject options = mOptionsArr.optJSONObject(i);
				mSurveyTopicResultPercenView[i] = new SurveyTopicResultPercenView(context);
				mSurveyTopicResultPercenView[i].setData(options, voteNum, screenWidth, i, obj.optString("type"));
				mGroupResult.addView(mSurveyTopicResultPercenView[i], lp);
			}
			postInvalidate();
		}
	}
}
