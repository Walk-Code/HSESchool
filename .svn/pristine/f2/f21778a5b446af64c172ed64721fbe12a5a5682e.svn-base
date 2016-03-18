package com.layout;

import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.model.DefineHandler;
import com.util.Utils;
import com.zhuochuang.hsej.R;

public class SurveyTopicResultPercenView extends LinearLayout{

	private View mConvertView;
	private Context mContext;
	
	public SurveyTopicResultPercenView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initSurveyView(context);
	}

	public SurveyTopicResultPercenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initSurveyView(context);
	}
	
	public void initSurveyView(Context context){
		mContext = context;
		mConvertView = View.inflate(context, R.layout.listcell_surveytopic_result_options, null);
		this.addView(mConvertView);
	}
	
	public void setData(JSONObject obj, int voteNum, int screenWidth, int position, String type){
		if(obj == null){
			return;
		}
		((TextView)mConvertView.findViewById(R.id.textview_name)).setText(obj.optString("options"));
		if(!type.equalsIgnoreCase("2")){
			mConvertView.findViewById(R.id.textview_percen).setVisibility(View.VISIBLE);
			mConvertView.findViewById(R.id.view_percen).setVisibility(View.VISIBLE);
			double number = obj.optDouble("number", 0);
			String progressPoint = String.format("%.1f", (number / (voteNum))* 100);
			
			((TextView)mConvertView.findViewById(R.id.textview_percen)).setText(progressPoint + "%");
			//mConvertView.findViewById(R.id.view_percen).setBackgroundColor(Color.parseColor("#6cd0f7"));
			
			int realProgress;
			if(number != 0){
				int width = screenWidth - Utils.dipToPixels(mContext, 95);
				realProgress = (int) (((double) width / (double)voteNum) * number);
			}
			else{
				realProgress = Utils.dipToPixels(mContext, 3);
			}
			LinearLayout.LayoutParams lpl = new LinearLayout.LayoutParams(realProgress, Utils.dipToPixels(mContext, 3));
			//lpl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			mConvertView.findViewById(R.id.view_percen).setLayoutParams(lpl);
			mConvertView.findViewById(R.id.view_percen).setBackgroundColor(DefineHandler.getSurveyColor(position));
		}
		else{
			mConvertView.findViewById(R.id.textview_percen).setVisibility(View.GONE);
			mConvertView.findViewById(R.id.view_percen).setVisibility(View.GONE);
		}
	}
}
