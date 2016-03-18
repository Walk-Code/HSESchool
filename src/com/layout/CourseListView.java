package com.layout;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.util.Utils;
import com.zhuochuang.hsej.CourseListMsgActivity;
import com.zhuochuang.hsej.R;

/**
 * single
 * @author Administrator
 *
 */
public class CourseListView extends LinearLayout{

	Context mContext;
	View mConvertView;
	
	public CourseListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initConvertView(context);
	}
	
	public CourseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initConvertView(context);
	}

	private void initConvertView(Context context){
		mContext = context;
		mConvertView = View.inflate(context, R.layout.view_courselist, null);
		this.addView(mConvertView, new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public void setData(final JSONObject obj, boolean isSingleKc){
		if(obj == null){
			return;
		}
		String djj = obj.optString("djj");
		if(Utils.isTextEmpty(djj)){
			return;
		}
		
		if(Utils.isTextEmpty(obj.optString("kcmc"))){
			mConvertView.findViewById(R.id.textview_bookname).setVisibility(View.GONE);
		}
		if(Utils.isTextEmpty(obj.optString("skdd"))){
			mConvertView.findViewById(R.id.textview_address).setVisibility(View.GONE);
		}
		/*if(Utils.isTextEmpty(obj.optString("qsz"))){
			mConvertView.findViewById(R.id.textview_week).setVisibility(View.GONE);
		}
		if(Utils.isTextEmpty(obj.optString("rkls"))){
			mConvertView.findViewById(R.id.textview_teachername).setVisibility(View.GONE);
		}*/
		if(isSingleKc){
			((TextView)mConvertView.findViewById(R.id.textview_bookname)).setSingleLine(true);
			((TextView)mConvertView.findViewById(R.id.textview_bookname)).setEllipsize(TextUtils.TruncateAt.END);
			((TextView)mConvertView.findViewById(R.id.textview_address)).setSingleLine(true);
			((TextView)mConvertView.findViewById(R.id.textview_address)).setEllipsize(TextUtils.TruncateAt.END);
		}
		else{
			((TextView)mConvertView.findViewById(R.id.textview_bookname)).setSingleLine(false);
			((TextView)mConvertView.findViewById(R.id.textview_bookname)).setMaxLines(2);
			((TextView)mConvertView.findViewById(R.id.textview_bookname)).setEllipsize(TextUtils.TruncateAt.END);
			((TextView)mConvertView.findViewById(R.id.textview_address)).setSingleLine(false);
			((TextView)mConvertView.findViewById(R.id.textview_address)).setMaxLines(2);
			((TextView)mConvertView.findViewById(R.id.textview_address)).setEllipsize(TextUtils.TruncateAt.END);
		}
		((TextView)mConvertView.findViewById(R.id.textview_bookname)).setText(obj.optString("kcmc"));
		((TextView)mConvertView.findViewById(R.id.textview_address)).setText(obj.optString("skdd"));
		//((TextView)mConvertView.findViewById(R.id.textview_week)).setText(obj.optString("qsz"));
		//((TextView)mConvertView.findViewById(R.id.textview_teachername)).setText(obj.optString("rkls"));
		
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, CourseListMsgActivity.class);
				intent.putExtra("data", obj.toString());
				mContext.startActivity(intent);
			}
		});
	}
}
