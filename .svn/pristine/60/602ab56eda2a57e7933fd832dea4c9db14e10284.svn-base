package com.zhuochuang.hsej;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.model.Configs;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.Utils;
import com.zhuochuang.hsej.entity.EnglishScoreEntity;
import com.zhuochuang.hsej.view.CustomDialog.OnCDClickListener;

public class EnglishScoreQueryResultActivity extends BaseActivity {
	private EnglishScoreEntity mScoreEntity;
	private final int DIALOG_QUERY = 0x101;
	private LinearLayout mainLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_english_score_query_result);
		setTitleText(R.string.activity_english_score_query_result_title);

		showDialogCustom(DIALOG_QUERY);
		Utils.removeSoftKeyboard(EnglishScoreQueryResultActivity.this);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_GetEnglishScore, null, this);
	}

	private void initData() {
		mainLayout.setVisibility(View.VISIBLE);
		Resources mResources = getResources();
		double passScore = Double.parseDouble(mScoreEntity.getPassScore());
		double myScore = Double.parseDouble(mScoreEntity.getTotalScore());
		((TextView) findViewById(R.id.english_result_pass_score))
				.setText(String.format(
						mResources.getString(R.string.text_tgfs),
						mScoreEntity.getPassScore()));
		((TextView) findViewById(R.id.english_result_my_score))
				.setText(mScoreEntity.getTotalScore());
		String type = mScoreEntity.getType();
		if (myScore >= passScore) {
			((TextView) findViewById(R.id.english_result_info)).setText(String
					.format(mResources.getString(R.string.english_pass_info),
							type));
		} else {
			((TextView) findViewById(R.id.english_result_info)).setText(String
					.format(mResources
							.getString(R.string.english_not_pass_info), type));

		}
		((TextView) findViewById(R.id.english_result_name))
				.setText(mScoreEntity.getXm());
		((TextView) findViewById(R.id.english_result_numb))
				.setText(mScoreEntity.getZkzh());
		((TextView) findViewById(R.id.english_result_class_avg))
				.setText(mScoreEntity.getBjAverage());
		((TextView) findViewById(R.id.english_result_class_rank))
				.setText(mScoreEntity.getBjRanking());
		((TextView) findViewById(R.id.english_result_school_avg))
				.setText(mScoreEntity.getXxAverage());
		((TextView) findViewById(R.id.english_result_school_rank))
				.setText(mScoreEntity.getXxRanking());

	}

	public void onScoreDetailClick(View view) {
		if (mScoreEntity == null)
			return;
		Intent intent = new Intent(this,
				EnglishScoreQueryResultDetailsActivity.class);
		intent.putExtra("stuName", mScoreEntity.getXm());
		intent.putExtra("schName", mScoreEntity.getXxmc());
		intent.putExtra("examType", mScoreEntity.getType());
		intent.putExtra("stuZkzh", mScoreEntity.getZkzh());
		intent.putExtra("totalScore", mScoreEntity.getTotalScore());
		intent.putExtra("listenScore", mScoreEntity.getListenScore());
		intent.putExtra("readScore", mScoreEntity.getReadScore());
		intent.putExtra("writeScore", mScoreEntity.getWriteScore());
		startActivity(intent);
	}

	public void onSharePkClick(View view) {
		if (((HSESchoolApp) getApplicationContext()).umSocialServiceShare != null) {
			((HSESchoolApp) getApplicationContext()).setUmengShareObj(
					EnglishScoreQueryResultActivity.this, null,
					Configs.UmengShare_App);
			((HSESchoolApp) getApplicationContext()).umSocialServiceShare
					.openShare(EnglishScoreQueryResultActivity.this, false);
		}
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		removeDialogCustom();
		if (result == null)
			return;
		if (result instanceof Error) {
			NoticeDialog noticeDialog = new NoticeDialog(this);
			noticeDialog.show();
			noticeDialog.setMessage(((Error) result).getMessage());
			noticeDialog.setOnCDClickListener(new OnCDClickListener() {

				@Override
				public void onPositiveClick(View view) {
					finish();
				}

				@Override
				public void onNegativeClick(View view) {

				}
			});
			return;
		}
		switch (type) {
		case TaskOrMethod_GetEnglishScore:
			if ("1".equals(((JSONObject) result).optString("result").toString())) {
				Gson gson = new Gson();
				mScoreEntity = gson.fromJson(
						((JSONObject) result).optString("item"),
						EnglishScoreEntity.class);
				initData();
			} else {
				Toast.makeText(this, ((JSONObject) result).optString("msg"),
						Toast.LENGTH_SHORT).show();
				finish();
			}
			break;

		default:
			break;
		}
	}

	class NoticeDialog extends Dialog implements OnClickListener {
		private Button btnPos;
		private TextView tvMessage;

		public NoticeDialog(Context context) {
			super(context, R.style.custom_dialog);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.view_english_score_result_notice_dialog);

			btnPos = (Button) findViewById(R.id.btn_positive);
			btnPos.setOnClickListener(this);
			tvMessage = ((TextView) findViewById(R.id.message));
		}

		public void setMessage(String message) {
			tvMessage.setText(message);
		}

		@Override
		public void dismiss() {
			super.dismiss();
			EnglishScoreQueryResultActivity.this.finish();
		}

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_positive:
				if (listener != null) {
					listener.onPositiveClick(view);
				} else {
					dismiss();
				}
				break;
			}
		}

		private OnCDClickListener listener;

		public void setOnCDClickListener(OnCDClickListener listener) {
			this.listener = listener;
		}

	}

}
