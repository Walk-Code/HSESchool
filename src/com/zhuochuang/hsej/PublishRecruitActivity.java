package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskType;
import com.util.Utils;
import com.zhuochuang.hsej.adapter.MSpinnerAdapter;
import com.zhuochuang.hsej.entity.RecruitServiceEntity.RecruitItemsEntity;

public class PublishRecruitActivity extends BaseActivity {
	private final int DIALOG_LOADING = 0x1000;
	private final int DATA_CHANGE_CODE = 0x1002;
	private Spinner spJobType;
	protected boolean isEdit = false;
	private RecruitItemsEntity mItemsEntity;
	private int mId;

	private int jobType;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_publish_recruit);
		findViewById(R.id.textview_right_text).setVisibility(View.GONE);

		mId = getIntent().getIntExtra("id", 0);
		if (mId == 0)
			setTitleText(R.string.text_zpfb);
		else
			setTitleText(R.string.text_zpbj);
		initView();
		initData();
	}

	private void initView() {
		spJobType = (Spinner) findViewById(R.id.publish_recruit_job_type);

	}

	private void initData() {
		String[] modes = getResources().getStringArray(
				R.array.activity_publish_recruit_job_type);
		spJobType.setAdapter(new MSpinnerAdapter(this, modes));
		spJobType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				jobType = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		if (mId != 0) {
			loadData();
		}
	}

	private void loadData() {
		HashMap<String, Object> detailsParams = new HashMap<String, Object>();
		detailsParams.put("id", mId);
		showDialogCustom(DIALOG_LOADING);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_RecruitServiceDetails, detailsParams,
				this);
	}

	private void InflaterData() {
		((EditText) findViewById(R.id.publish_recruit_job_desc))
				.setText(mItemsEntity.getName());
		((EditText) findViewById(R.id.publish_recruit_job_number))
				.setText(mItemsEntity.getPeopleNum() + "");
		((EditText) findViewById(R.id.publish_recruit_job_salary))
				.setText(mItemsEntity.getSalary());
		((EditText) findViewById(R.id.publish_recruit_job_requirements))
				.setText(mItemsEntity.getJobRequirements());
		((EditText) findViewById(R.id.publish_recruit_job_content))
				.setText(mItemsEntity.getJobContent());
		((EditText) findViewById(R.id.publish_recruit_job_contact))
				.setText(mItemsEntity.getLinkMan());
		((EditText) findViewById(R.id.publish_recruit_job_contact_phone))
				.setText(mItemsEntity.getPhone());
		((EditText) findViewById(R.id.publish_recruit_job_time))
				.setText(mItemsEntity.getJobTime());
		((EditText) findViewById(R.id.publish_recruit_job_address))
				.setText(mItemsEntity.getJobAddress());
		spJobType.setSelection(mItemsEntity.getJobNature() - 1);
	}

	public void onPublishClick(View view) {
		String jobDesc = ((EditText) findViewById(R.id.publish_recruit_job_desc))
				.getText().toString();
		String jobNumber = ((EditText) findViewById(R.id.publish_recruit_job_number))
				.getText().toString();
		String jobSalary = ((EditText) findViewById(R.id.publish_recruit_job_salary))
				.getText().toString();
		String jobRequire = ((EditText) findViewById(R.id.publish_recruit_job_requirements))
				.getText().toString();
		String jobContent = ((EditText) findViewById(R.id.publish_recruit_job_content))
				.getText().toString();
		String jobContact = ((EditText) findViewById(R.id.publish_recruit_job_contact))
				.getText().toString();
		String jobContactPhone = ((EditText) findViewById(R.id.publish_recruit_job_contact_phone))
				.getText().toString();
		String jobUpTime = ((EditText) findViewById(R.id.publish_recruit_job_time))
				.getText().toString();
		String jobAddress = ((EditText) findViewById(R.id.publish_recruit_job_address))
				.getText().toString();

		Resources mRes=getResources();
		if (TextUtils.isEmpty(jobDesc)) {
			Toast.makeText(this, mRes.getString(R.string.text_hint_job_desc), Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(jobNumber)) {
			Toast.makeText(this, mRes.getString(R.string.text_qsr)+mRes.getString(R.string.text_hint_job_number), Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(jobSalary)) {
			Toast.makeText(this, mRes.getString(R.string.text_hint_job_salary), Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(jobRequire)) {
			Toast.makeText(this, mRes.getString(R.string.text_hint_job_request), Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(jobContent)) {
			Toast.makeText(this, mRes.getString(R.string.text_hint_job_content), Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(jobContact)) {
			Toast.makeText(this, mRes.getString(R.string.text_qsrlxr), Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(jobContactPhone)) {
			Toast.makeText(this, mRes.getString(R.string.text_qsrlxfs), Toast.LENGTH_SHORT).show();
			return;
		}
		if(!Utils.isMobileNO(jobContactPhone)){
			Toast.makeText(this, mRes.getString(R.string.text_qsrzqdsjhm), Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(jobUpTime)) {
			Toast.makeText(this, mRes.getString(R.string.text_hint_job_time), Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(jobAddress)) {
			Toast.makeText(this, mRes.getString(R.string.text_hint_job_address), Toast.LENGTH_SHORT).show();
			return;
		}

		HashMap<String, Object> jobParams = new HashMap<String, Object>();
		if (mId != 0)
			jobParams.put("id", mId);
		jobParams.put("name", jobDesc);
		jobParams.put("jobNature", jobType);
		jobParams.put("peopleNum", jobNumber);
		jobParams.put("jobRequirements", jobRequire);
		jobParams.put("jobContent", jobContent);
		jobParams.put("linkMan", jobContact);
		jobParams.put("phone", jobContactPhone);
		jobParams.put("jobTime", jobUpTime);
		jobParams.put("jobAddress", jobAddress);
		jobParams.put("salary", jobSalary);

		showDialogCustom(DIALOG_LOADING);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_RecruitServiceSend, jobParams, this);
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
		case TaskOrMethod_RecruitServiceSend:
			if ("1".equals(((JSONObject) result).optString("result").toString())) {
				EventManager.getInstance().sendMessage(DATA_CHANGE_CODE, "");
				Utils.removeSoftKeyboard(this);
				Toast.makeText(this,
						getResources().getString(R.string.publish_success),
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(this,
						getResources().getString(R.string.publish_fail),
						Toast.LENGTH_SHORT).show();
			}
			break;
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
}
