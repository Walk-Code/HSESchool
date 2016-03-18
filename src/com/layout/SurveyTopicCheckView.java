package com.layout;

import org.json.JSONObject;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.util.Utils;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.SurveyTopicActivity.OnCheckListener;

public class SurveyTopicCheckView extends LinearLayout{

	private View mConvertView;
	private int mType;
	
	public SurveyTopicCheckView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initSurveyView(context);
	}
	
	public SurveyTopicCheckView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initSurveyView(context);
	}

	public void initSurveyView(Context context){
		mConvertView = View.inflate(context, R.layout.listcell_surveytopic_check, null);
		this.addView(mConvertView);
	}
	
	public void setData(final JSONObject obj, int type, boolean isCheck, final OnCheckListener listener){
		if(obj == null || mConvertView == null){
			return;
		}
		mType = type;
		((TextView)mConvertView.findViewById(R.id.textview_name)).setText(obj.optString("options"));
		mConvertView.findViewById(R.id.edittext).setVisibility(View.GONE);
		mConvertView.findViewById(R.id.view_check).setVisibility(View.VISIBLE);
		mConvertView.findViewById(R.id.textview_name).setVisibility(View.VISIBLE);
		switch (type) {
		case 0:
			mConvertView.findViewById(R.id.view_check).setBackgroundResource(isCheck? R.drawable.radiotrue : R.drawable.radioflase);
			break;
		case 1:
			mConvertView.findViewById(R.id.view_check).setBackgroundResource(isCheck? R.drawable.checktrue : R.drawable.checkflase);
			break;
		case 2:
			mConvertView.findViewById(R.id.edittext).setVisibility(View.VISIBLE);
			mConvertView.findViewById(R.id.view_check).setVisibility(View.GONE);
			mConvertView.findViewById(R.id.textview_name).setVisibility(View.GONE);
			((EditText)mConvertView.findViewById(R.id.edittext)).addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					String str = ((EditText)mConvertView.findViewById(R.id.edittext)).getText().toString();
					if(!Utils.isTextEmpty(str)){
						str = "other___" + str;
					}
					listener.onCheckChange(obj.optString("id"), str);
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			//mConvertView.findViewById(R.id.edittext).setEnabled(false);
			break;
		default:
			break;
		}
		setCheckStatu(isCheck);
	}
	
	public void setCheckStatu(boolean isCheck){
		switch (mType) {
		case 0:
			mConvertView.findViewById(R.id.view_check).setBackgroundResource(isCheck? R.drawable.radiotrue : R.drawable.radioflase);
			break;
		case 1:
			mConvertView.findViewById(R.id.view_check).setBackgroundResource(isCheck? R.drawable.checktrue : R.drawable.checkflase);
			break;
		case 2:
			break;
		default:
			break;
		}
	}
}
