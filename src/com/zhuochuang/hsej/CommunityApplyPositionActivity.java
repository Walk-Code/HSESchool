package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.Utils;
import com.zhuochuang.hsej.adapter.StructSpinnerAdapter;
import com.zhuochuang.hsej.entity.CommunityStructEntity;

/**
 * 申请职位
 * 
 * @author Administrator
 * 
 */
public class CommunityApplyPositionActivity extends BaseActivity {
	private Spinner firstSpinner;
	private Spinner secondSpinner;
	private CommunityStructEntity mStructEntity;
	private StructSpinnerAdapter mSpinnerAdapter1;
	private StructSpinnerAdapter mSpinnerAdapter2;
	private int firstID;
	private int secondID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_apply_position);
		setTitleText(R.string.text_zwsq);
		initView();
	}

	private void initView() {
		mStructEntity = (CommunityStructEntity) getIntent().getExtras()
				.getSerializable("struct_entity");
		JSONObject object = DataLoader.getInstance().getUserInfoObj();
		((EditText) findViewById(R.id.apply_et_schid)).setText(object
				.optString("xh"));
		((EditText) findViewById(R.id.apply_et_name)).setText(object
				.optString("xm"));
		((EditText) findViewById(R.id.apply_et_classid)).setText(object
				.optString("bjmc"));
		if (!TextUtils.isEmpty(mStructEntity.getApplyUser().getPhone()))
			((EditText) findViewById(R.id.apply_et_phone))
					.setText(mStructEntity.getApplyUser().getPhone());
		else
			((EditText) findViewById(R.id.apply_et_phone)).setText(object
					.optString("phone"));
		((EditText) findViewById(R.id.apply_et_sex)).setText("0"
				.equalsIgnoreCase(object.optString("xb")) ? "女" : "男");
		((EditText) findViewById(R.id.apply_et_evaluate)).setText(mStructEntity
				.getApplyUser().getSelfEvaluation());
		((EditText) findViewById(R.id.apply_et_hobby)).setText(mStructEntity
				.getApplyUser().getHobby());
		firstSpinner = (Spinner) findViewById(R.id.apply_spinner_first);
		secondSpinner = (Spinner) findViewById(R.id.apply_spinner_second);

		if (mStructEntity.getApplyUser().getApplyStructure() != 3
				&& mStructEntity.getApplyUser().getApplyStructure() != 0) {
			findViewById(R.id.apply_et_phone).setEnabled(false);
			findViewById(R.id.apply_et_evaluate).setEnabled(false);
			findViewById(R.id.apply_et_hobby).setEnabled(false);
			firstSpinner.setEnabled(false);
			secondSpinner.setEnabled(false);
		}
		mSpinnerAdapter1 = new StructSpinnerAdapter(this);
		firstSpinner.setAdapter(mSpinnerAdapter1);
		firstSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				firstID = mStructEntity.getStructures().get(position).getId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		mSpinnerAdapter2 = new StructSpinnerAdapter(this);

		secondSpinner.setAdapter(mSpinnerAdapter2);
		secondSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				secondID = mStructEntity.getStructures().get(position).getId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		mSpinnerAdapter1.setData(mStructEntity.getStructures());
		mSpinnerAdapter2.setData(mStructEntity.getStructures());
		for (int i = 0; i < mStructEntity.getStructures().size(); i++) {
			if (mStructEntity.getStructures().get(i).getId() == mStructEntity
					.getApplyUser().getFirst())
				firstSpinner.setSelection(i);
			if (mStructEntity.getStructures().get(i).getId() == mStructEntity
					.getApplyUser().getSecond())
				secondSpinner.setSelection(i);
		}
		Button btnApply = (Button) findViewById(R.id.apply_btn_submit);
		btnApply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startApply();
			}
		});
		String[] applyStatus = getResources().getStringArray(
				R.array.apply_status);
		btnApply.setClickable(false);
		btnApply.setBackgroundResource(R.drawable.btn_gray_n);
		switch (mStructEntity.getApplyUser().getApplyStructure()) {
		case 2:
			btnApply.setText(applyStatus[0]);
			break;
		case 3:
		case 0:
			btnApply.setText(applyStatus[1]);
			btnApply.setClickable(true);
			btnApply.setBackgroundResource(R.drawable.btn_red_button_selector);
			break;
		case 4:
			btnApply.setText(applyStatus[2]);
			break;
		case 5:
			btnApply.setText(applyStatus[3]);
			break;
		}
	}

	private void startApply() {
		String phone = ((EditText) findViewById(R.id.apply_et_phone)).getText()
				.toString();
		String selfEv = ((EditText) findViewById(R.id.apply_et_evaluate))
				.getText().toString();
		String hobby = ((EditText) findViewById(R.id.apply_et_hobby)).getText()
				.toString();
		String[] notice = getResources().getStringArray(R.array.apply_notice);
		if (!Utils.isMobileNO(phone)) {
			Toast.makeText(CommunityApplyPositionActivity.this, notice[0],
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(selfEv)) {
			Toast.makeText(CommunityApplyPositionActivity.this, notice[1],
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(hobby)) {
			Toast.makeText(CommunityApplyPositionActivity.this, notice[2],
					Toast.LENGTH_SHORT).show();
			return;
		}
		HashMap<String, Object> applyParams = new HashMap<String, Object>();
		applyParams.put("type", 2);
		applyParams.put("phone", phone);
		applyParams.put("communityId", mStructEntity.getCommid());
		applyParams.put("first", firstID);
		applyParams.put("second", secondID);
		applyParams.put("selfEvaluation", selfEv);
		applyParams.put("hobby", hobby);
		showDialogCustom(100);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_ApplyCommunity, applyParams,
				CommunityApplyPositionActivity.this);
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
		case TaskOrMethod_CommunityGetUserStructure:
			Gson gson = new Gson();
			mStructEntity = gson.fromJson(result.toString(),
					CommunityStructEntity.class);
			break;
		case TaskOrMethod_ApplyCommunity:
			if ("1".equalsIgnoreCase(((JSONObject) result).optString("result"))) {
				Toast.makeText(this,
						getResources().getString(R.string.text_apply_success),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this,
						getResources().getString(R.string.text_apply_fail),
						Toast.LENGTH_SHORT).show();
			}
			finish();
			break;
		default:
			break;
		}
	}
}
