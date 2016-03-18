package com.zhuochuang.hsej;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.DataLoader;
import com.model.TaskType;
import com.zhuochuang.hsej.adapter.OneCardSolutionAdapter;
import com.zhuochuang.hsej.entity.OneCardSolutionEntity;
import com.zhuochuang.hsej.entity.PayResultEntity;
import com.zhuochuang.hsej.view.CustomDialog;
import com.zhuochuang.hsej.view.CustomDialog.OnCDClickListener;
import com.zhuochuang.hsej.view.FlowRadioGroup;

public class OneCardSolutionActivity extends BaseActivity implements
		OnCheckedChangeListener, OnClickListener {
	private TextView btnRepLoss;
	private RadioGroup btnGroup;
	private final int REQUEST_CODE = 0x1001;
	private ListView mListView;
	private OneCardSolutionAdapter mCardSolutionAdapter;
	private int code = 0;
	private List<OneCardSolutionEntity> mAllEntities;// 全部
	private List<OneCardSolutionEntity> mRechargeEntities;// 充值
	private List<OneCardSolutionEntity> mMealsEntities;// 餐费
	private List<OneCardSolutionEntity> mElectroEntities;// 用电
	private CustomDialog mCustomDialog;
	private String cardNo;
	private int ReAmount = 50;// 充值金额
	private PayResultEntity mPayResultEntity;
	private LinearLayout mainLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
	}

	private void initView() {
		setContentView(R.layout.activity_one_card_solution);
		setTitleText(R.string.activity_one_card_solution_title);
		btnRepLoss = ((TextView) findViewById(R.id.textview_right_text));
		btnGroup = (RadioGroup) findViewById(R.id.rg_records_of_consumption);
		mListView = (ListView) findViewById(R.id.puulto_listview);
		btnGroup.setOnCheckedChangeListener(this);
		btnRepLoss.setText(R.string.activity_one_card_solution_right_text);
		btnRepLoss.setTextSize(14);

		mCardSolutionAdapter = new OneCardSolutionAdapter(this);
		mListView.setAdapter(mCardSolutionAdapter);

		btnRepLoss.setOnClickListener(this);
		showDialogCustom(101);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_OneCardSolutionGetCard, null, this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rd_all_record:
			if (mAllEntities == null) {
				code = 0;
				loadData();
			} else {
				mCardSolutionAdapter.setData(mAllEntities);
			}
			break;
		case R.id.rd_recharge_record:
			if (mRechargeEntities == null) {
				code = 100;
				loadData();
			} else {
				mCardSolutionAdapter.setData(mRechargeEntities);
			}
			break;
		case R.id.rd_meals_record:
			if (mMealsEntities == null) {
				code = 210;
				loadData();
			} else {
				mCardSolutionAdapter.setData(mMealsEntities);
			}
			break;
		case R.id.rd_electro_record:
			if (mElectroEntities == null) {
				code = 216;
				loadData();
			} else {
				mCardSolutionAdapter.setData(mElectroEntities);
			}
			break;
		}
	}

	private void InflaterData(JSONObject optJSONObject) {
		mainLayout.setVisibility(View.VISIBLE);
		try {
			((TextView) findViewById(R.id.card_end_date))
					.setText(getResources().getString(
							R.string.card_end_time,
							new SimpleDateFormat("yyyy.MM.dd").format(new Date(
									System.currentTimeMillis()))));
			cardNo = optJSONObject.getString("cardNO");
			((TextView) findViewById(R.id.card_no)).setText(String.format(
					getResources().getString(R.string.card_no_text), cardNo));
			((TextView) findViewById(R.id.card_give_money)).setText(String
					.format(getResources().getString(
							R.string.card_give_money_text),
							optJSONObject.getString("subcard")));
			((TextView) findViewById(R.id.card_amount))
					.setText(String.format("%.2f",
							Double.parseDouble(optJSONObject.getString("card"))));
			loadData();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void loadData() {
		showDialogCustom(100);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pageNo", 1);
		params.put("pageSize", 20);
		params.put("code", code);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_OneCardSolutionList, params, this);

	}

	private void loadPayData() {
		HashMap<String, Object> payParams = new HashMap<String, Object>();
		payParams.put("sign", mPayResultEntity.getItems().getSign());
		payParams.put("pay_type", mPayResultEntity.getItems().getPay_type());
		payParams.put("request_xml", mPayResultEntity.getItems()
				.getRequest_xml());
		showDialogCustom(100);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_GetWay, payParams, this);
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
		case TaskOrMethod_OneCardSolutionGetCard:
			if ("1".equalsIgnoreCase(((JSONObject) result).optString("result"))) {
				InflaterData(((JSONObject) result).optJSONObject("item"));
			}else if ("2".equalsIgnoreCase(((JSONObject) result).optString("result"))) {
				NoticeDialog noticeDialog = new NoticeDialog(this);
				noticeDialog.show();
				noticeDialog.setMessage(((JSONObject) result).optString("msg"));
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
			break;
		case TaskOrMethod_OneCardSolutionList:
			Type type2 = new TypeToken<List<OneCardSolutionEntity>>() {
			}.getType();
			Gson gson = new Gson();
			if (code == 0) {
				mAllEntities = gson.fromJson(
						((JSONObject) result).optString("items"), type2);
				mCardSolutionAdapter.setData(mAllEntities);
			} else if (code == 100) {
				mRechargeEntities = gson.fromJson(
						((JSONObject) result).optString("items"), type2);
				mCardSolutionAdapter.setData(mRechargeEntities);
			} else if (code == 210) {
				mMealsEntities = gson.fromJson(
						((JSONObject) result).optString("items"), type2);
				mCardSolutionAdapter.setData(mMealsEntities);
			} else if (code == 216) {
				mElectroEntities = gson.fromJson(
						((JSONObject) result).optString("items"), type2);
				mCardSolutionAdapter.setData(mElectroEntities);
			}
			break;
		case TaskOrMethod_OneCardSolutionReportLoss:
			if (mCustomDialog.isShowing())
				mCustomDialog.dismiss();
			if ("1".equalsIgnoreCase(((JSONObject) result).optString("result"))) {
				Toast.makeText(OneCardSolutionActivity.this,
						R.string.text_report_loss_success, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(OneCardSolutionActivity.this,
						R.string.text_report_loss_fail, Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case TaskOrMethod_CommonGetPayUrl:
			Gson gson2 = new Gson();
			mPayResultEntity = gson2.fromJson(result.toString(),
					PayResultEntity.class);
			if (mPayResultEntity.getResult() == 1) {
				loadPayData();
			} else {
				Toast.makeText(this, R.string.text_get_pay_fail,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case TaskOrMethod_GetWay:
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra("result", result.toString());
			startActivityForResult(intent, REQUEST_CODE);
			break;
		default:
			break;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textview_right_text:
			mCustomDialog = new CustomDialog(this, getResources().getString(
					R.string.activity_one_card_solution_right_text),
					getResources().getString(R.string.text_report_loss),
					getResources().getString(R.string.store_close));
			mCustomDialog.setOnCDClickListener(new OnCDClickListener() {
				@Override
				public void onPositiveClick(View view) {
					showDialogCustom(102);
					DataLoader.getInstance().startTaskForResult(
							TaskType.TaskOrMethod_OneCardSolutionReportLoss,
							null, OneCardSolutionActivity.this);
				}

				@Override
				public void onNegativeClick(View view) {
					mCustomDialog.dismiss();
				}
			});
			mCustomDialog.show();
			TextView textView = new TextView(this);
			textView.setText(String.format(
					getResources().getString(R.string.text_report_loss_notice),
					cardNo));
			textView.setTextSize(16);
			textView.setTextColor(Color.rgb(99, 99, 99));
			mCustomDialog.setMContentView(textView);
			break;

		case R.id.btn_online_recharge:
			mCustomDialog = new CustomDialog(this, getResources().getString(
					R.string.text_online_recharge), getResources().getString(
					R.string.text_recharge), getResources().getString(
					R.string.store_close));
			mCustomDialog.setOnCDClickListener(new OnCDClickListener() {
				@Override
				public void onPositiveClick(View view) {
					HashMap<String, Object> payParams = new HashMap<String, Object>();
					payParams.put("resourceType", 1);
					payParams.put("ecardMoney", ReAmount);
					showDialogCustom(100);
					DataLoader.getInstance().startTaskForResult(
							TaskType.TaskOrMethod_CommonGetPayUrl, payParams,
							OneCardSolutionActivity.this);
				}

				@Override
				public void onNegativeClick(View view) {
					mCustomDialog.dismiss();
				}
			});
			mCustomDialog.show();
			View view = View.inflate(this, R.layout.view_online_recharge, null);
			FlowRadioGroup fRadioGroup = (FlowRadioGroup) view
					.findViewById(R.id.flow_radiogroup);
			fRadioGroup
					.setOnCheckedChangeListener(new com.zhuochuang.hsej.view.FlowRadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(FlowRadioGroup group,
								int checkedId) {
							switch (checkedId) {
							case R.id.rd_fifty:
								ReAmount = 50;
								break;
							case R.id.rd_one_hundred:
								ReAmount = 100;
								break;
							case R.id.rd_two_hundred:
								ReAmount = 200;
								break;
							case R.id.rd_five_hundred:
								ReAmount = 500;
								break;
							default:
								ReAmount = 50;
								break;
							}
						}
					});
			mCustomDialog.setMContentView(view);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
			mCustomDialog.dismiss();
			DataLoader.getInstance().startTaskForResult(
					TaskType.TaskOrMethod_OneCardSolutionGetCard, null, this);
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
			OneCardSolutionActivity.this.finish();
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
