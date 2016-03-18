package com.zhuochuang.hsej;

import org.json.JSONObject;

import android.os.Bundle;
import android.widget.TextView;

/**
 * 课程表 单个信息
 * @author Administrator
 *
 */
public class CourseListMsgActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_courseist_msg);
		setTitleText(R.string.kcb_details_title);
		
		JSONObject obj = null;
		try {
			obj = new JSONObject(getIntent().getStringExtra("data"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(obj == null){
			return;
		}
		
		((TextView)findViewById(R.id.textview_kc)).setText(obj.optString("kcmc"));
		((TextView)findViewById(R.id.textview_jiaos)).setText(obj.optString("skdd"));
		((TextView)findViewById(R.id.textview_ls)).setText(obj.optString("rkls"));
		((TextView)findViewById(R.id.textview_jies)).setText(obj.optString("ksj"));
		((TextView)findViewById(R.id.textview_zs)).setText(obj.optString("qsz"));
	}
}