package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.Utils;
import com.zhuochuang.hsej.entity.RecruitServiceEntity.RecruitItemsEntity;

public class PublishRecruitDetailsActivity extends BaseActivity {
	private RecruitItemsEntity mItemsEntity;
	private final int DIALOG_LOADING = 0x1001;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_publish_recruit_details);
		initView();
		initData();
	}

	private void initView() {
		setTitleText(R.string.text_zpxq);
	}

	private void initData() {
		HashMap<String, Object> detailsParams = new HashMap<String, Object>();
		int id = getIntent().getIntExtra("id", 0);
		if (id == 0)
			return;
		detailsParams.put("id", id);
		showDialogCustom(DIALOG_LOADING);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_RecruitServiceDetails, detailsParams,
				this);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		removeDialogCustom();
		if (result == null)
			return;
		if (result instanceof Error) {
			Toast.makeText(this, ((Error) result).getMessage(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_RecruitServiceDetails:
			if ("1".equals(((JSONObject) result).optString("result").toString())) {
				Gson gson = new Gson();
				mItemsEntity = gson.fromJson(
						((JSONObject) result).optString("item"),
						RecruitItemsEntity.class);
				InflaterData();
			} else {
				Toast.makeText(this,
						getResources().getString(R.string.load_fail),
						Toast.LENGTH_SHORT).show();
				finish();
			}
			break;

		default:
			break;
		}
	}

	private void InflaterData() {// 填充数据
		if ("1".equals(mItemsEntity.getStatus())) {
			((TextView) findViewById(R.id.recruit_details_title)).setText(Html
					.fromHtml(mItemsEntity.getName()
							+ "<font color='#DC4C4B'><small>("
							+ getResources().getString(
									R.string.activitysecendhand_lost_tab_going)
							+ ")</small></font>"));
		} else {
			((TextView) findViewById(R.id.recruit_details_title))
					.setText(Html.fromHtml(mItemsEntity.getName()
							+ "<font color='#DC4C4B'><small>("
							+ getResources()
									.getString(
											R.string.activitysecendhand_lost_tab_complete)
							+ ")</small></font>"));
		}
		String[] naString = getResources().getStringArray(
				R.array.activity_publish_recruit_job_type);
		((TextView) findViewById(R.id.recruit_details_type))
				.setText(mItemsEntity.getJobNature() == 1 ? naString[0]
						: naString[1]);
		((TextView) findViewById(R.id.recruit_details_number))
				.setText(mItemsEntity.getPeopleNum() + "人");
		((TextView) findViewById(R.id.recruit_details_contact))
				.setText(mItemsEntity.getLinkMan());
		((TextView) findViewById(R.id.recruit_details_phone))
				.setText(mItemsEntity.getPhone());
		((TextView) findViewById(R.id.recruit_details_salary))
				.setText(mItemsEntity.getSalary());
		((TextView) findViewById(R.id.recruit_details_address))
				.setText(mItemsEntity.getJobAddress());
		((TextView) findViewById(R.id.recruit_details_time))
				.setText(mItemsEntity.getJobTime());
		((TextView) findViewById(R.id.recruit_details_publish_time))
				.setText(Utils.getAlmostTime(mItemsEntity.getCreateDate()));
		((TextView) findViewById(R.id.recruit_details_content))
				.setText(mItemsEntity.getJobContent());
		((TextView) findViewById(R.id.recruit_details_require))
				.setText(mItemsEntity.getJobRequirements());

	}
}
