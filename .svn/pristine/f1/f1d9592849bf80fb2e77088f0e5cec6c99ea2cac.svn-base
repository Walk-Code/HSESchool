package com.zhuochuang.hsej;

import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.model.DataLoader;
import com.model.TaskType;
import com.zhuochuang.hsej.adapter.CommOrganizStructAdapter;
import com.zhuochuang.hsej.entity.CommunityStructEntity;

/**
 * 社团组织架构
 * 
 * @author Administrator
 * 
 */
public class CommunityOrganizStructeActivity extends BaseActivity implements
		OnClickListener {
	private int commId;
	private TextView btnRight;
	private CommOrganizStructAdapter mStructAdapter;
	private PullToRefreshListView mListView;
	private String previousYear;// 往届年份
	private String currentYear = "";// 当前年份
	
	private Button btnSubmit;

	private CommunityStructEntity mStructEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_organiz_structe);

		setTitleText(R.string.text_tv_btn_organize);
		Calendar calendar = Calendar.getInstance();
		currentYear = String.valueOf(calendar.get(Calendar.YEAR));
		btnRight = (TextView) findViewById(R.id.textview_right_text);
		btnRight.setVisibility(View.GONE);
		btnRight.setText(String.format(
				getResources().getString(R.string.text_previous), currentYear));
		btnRight.setTextSize(16);
		btnRight.setOnClickListener(this);
		btnSubmit=(Button) findViewById(R.id.apply_btn_submit);
		mListView = (PullToRefreshListView) findViewById(R.id.puulto_listview);
		initView();
	}

	private void initView() {
		commId = Integer.parseInt(getIntent().getStringExtra("comm_id"));
		mStructAdapter = new CommOrganizStructAdapter(this);
		mListView.setAdapter(mStructAdapter);
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadData();
			}
		});
		mListView.startRefresh();
		btnSubmit.setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						HashMap<String, Object> structParams = new HashMap<String, Object>();
						structParams.put("communityId", commId);
						showDialogCustom(100);
						DataLoader
								.getInstance()
								.startTaskForResult(
										TaskType.TaskOrMethod_CommunityGetUserStructure,
										structParams,
										CommunityOrganizStructeActivity.this);
					}
				});

	}

	private void loadData() {
		HashMap<String, Object> commParams = new HashMap<String, Object>();
		commParams.put("periodNum", currentYear);
		commParams.put("communityId", commId);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_CommunityGetStructure, commParams,
				CommunityOrganizStructeActivity.this);
	}

	@Override
	public void onClick(View v) {
		if (TextUtils.isEmpty(previousYear))
			return;
		Intent intent = new Intent(this, CommunityQueryPreviousActivity.class);
		intent.putExtra("current_year", currentYear);
		intent.putExtra("previous_year", previousYear);
		startActivityForResult(intent, 100);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		mListView.complete();
		removeDialogCustom();
		if (result == null)
			return;
		if (result instanceof Error) {
			Toast.makeText(this, ((Error) result).getMessage(),
					Toast.LENGTH_SHORT).show();
			btnSubmit.setText(R.string.text_zwzwsq);
			btnSubmit.setClickable(false);
			btnSubmit.setBackgroundResource(R.drawable.btn_gray_n);
			return;
		}
		switch (type) {
		case TaskOrMethod_CommunityGetStructure:
			if ("1".equals(((JSONObject) result).optString("result"))) {
				btnRight.setVisibility(View.VISIBLE);
				previousYear = ((JSONObject) result).optString("previous");
				mStructAdapter.setData(((JSONObject) result)
						.optJSONArray("items"));
				if("1".equalsIgnoreCase(((JSONObject) result).optString("applyStructure"))){
					btnSubmit.setText(R.string.text_zwsq);
				}else{
					btnSubmit.setText(R.string.text_cksq);
				}
			}
			break;
		case TaskOrMethod_CommunityGetUserStructure:
			if ("1".equals(((JSONObject) result).optString("result"))) {
				Gson gson = new Gson();
				mStructEntity = gson.fromJson(result.toString(),
						CommunityStructEntity.class);
				if (mStructEntity.getStructures() == null
						|| mStructEntity.getStructures().size() == 0) {
					Toast.makeText(
							this,
							getResources().getString(R.string.text_gstmyfsqdzw),
							Toast.LENGTH_SHORT).show();
					return;
				}
				mStructEntity.setCommid(commId + "");
				Intent intent = new Intent(
						CommunityOrganizStructeActivity.this,
						CommunityApplyPositionActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("struct_entity", mStructEntity);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				Toast.makeText(this, ((JSONObject) result).optString("msg"),
						Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			currentYear = data.getStringExtra("currentYear");
			btnRight.setText(String.format(
					getResources().getString(R.string.text_previous), currentYear));
			loadData();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

}
