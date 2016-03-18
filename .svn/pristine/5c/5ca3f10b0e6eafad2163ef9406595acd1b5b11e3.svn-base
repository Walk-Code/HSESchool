package com.zhuochuang.hsej;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EnglishScoreQueryResultDetailsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_english_score_query_result_details);
		setTitleText(R.string.activity_english_score_query_result_details_title);

		initData();
	}

	private void initData() {
		Intent intent = getIntent();
		((TextView)findViewById(R.id.details_stu_name)).setText(intent.getStringExtra("stuName"));
		((TextView)findViewById(R.id.details_school_name)).setText(intent.getStringExtra("schName"));
		String [] englishGrade=getResources().getStringArray(R.array.english_grade);
		String examType="4".equalsIgnoreCase(intent.getStringExtra("examType"))?englishGrade[0]:englishGrade[1];
		((TextView)findViewById(R.id.details_exam_type)).setText(examType);
		((TextView)findViewById(R.id.details_exam_numb)).setText(intent.getStringExtra("stuZkzh"));
		((TextView)findViewById(R.id.details_total_score)).setText(intent.getStringExtra("totalScore"));
		((TextView)findViewById(R.id.details_listen_score)).setText(intent.getStringExtra("listenScore"));
		((TextView)findViewById(R.id.details_read_score)).setText(intent.getStringExtra("readScore"));
		((TextView)findViewById(R.id.details_write_score)).setText(intent.getStringExtra("writeScore"));
	}
}
