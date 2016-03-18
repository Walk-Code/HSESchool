package com.zhuochuang.hsej;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.JsonUtil;
import com.util.Utils;
import com.zhuochuang.hsej.adapter.MSpinnerAdapter;
import com.zhuochuang.hsej.entity.CommonServiceEntity;
import com.zhuochuang.hsej.entity.CommonServiceEntity.CommItemsEntity;
import com.zhuochuang.hsej.entity.CommonServiceEntity.CommItemsEntity.ItemEntity;
import com.zhuochuang.hsej.entity.PayResultEntity;
import com.zhuochuang.hsej.view.FlowLayout;

/**
 * 公共报名服务
 * 
 * @author Administrator
 * 
 */
public class CommonServiceSignUpActivity extends BaseActivity implements
		OnClickListener {
	private CommonServiceEntity mServiceEntity;
	private int DATA_LODING = 0x1000;
	private final int REQUEST_CODE = 0x1001;

	private LinearLayout viewLayout;

	private PayResultEntity mPayResultEntity;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_common_service_sign_up);
		mServiceEntity = (CommonServiceEntity) getIntent().getExtras()
				.getSerializable("comm_entity");
		initData();
	}

	private void initData() {
		setTitleText(mServiceEntity.getName());
		if (!mServiceEntity.isApplyStatus()) {//没有报名
			findViewById(R.id.layout_not_applyStatus).setVisibility(
					View.VISIBLE);
			findViewById(R.id.layout_is_applyStatus).setVisibility(View.GONE);
			viewLayout = (LinearLayout) findViewById(R.id.viewContainer);
			findViewById(R.id.btn_submit).setOnClickListener(this);
			initView();
		} else {//已报名
			findViewById(R.id.layout_not_applyStatus).setVisibility(View.GONE);
			findViewById(R.id.layout_is_applyStatus)
					.setVisibility(View.VISIBLE);
			TextView textView = (TextView) findViewById(R.id.tv_sign_up_info);
			Drawable drawable;
			if (mServiceEntity.isIsfree()) {  //免费
				textView.setText(R.string.text_signup_success);
				drawable = getResources().getDrawable(R.drawable.icon_succeed);
				findViewById(R.id.linear_give_money_layout).setVisibility(
						View.GONE);
			} else {  //不免费
				if (mServiceEntity.isPayment()) {  //已付费
					textView.setText(R.string.text_signup_success);
					drawable = getResources().getDrawable(
							R.drawable.icon_succeed);

					findViewById(R.id.linear_give_money_layout).setVisibility(
							View.GONE);
				} else {//没有付费
					textView.setText(String
							.format(getResources().getString(
									R.string.text_signup_info),
									String.format("%.2f",
											mServiceEntity.getCost())));
					drawable = getResources().getDrawable(R.drawable.icon_wait);
					findViewById(R.id.linear_give_money_layout).setVisibility(
							View.VISIBLE);
				}
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				textView.setCompoundDrawables(drawable, null, null, null);
			}

			findViewById(R.id.btn_look_signup).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									CommonServiceSignUpActivity.this,
									CommonServiceResultActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("comm_entity",
									mServiceEntity);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
			findViewById(R.id.btn_give_money).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							HashMap<String, Object> payParams = new HashMap<String, Object>();
							payParams.put("resourceType", 18);
							payParams.put("resourceId", mServiceEntity.getId());
							payParams.put("ecardMoney",
									mServiceEntity.getCost());
							showDialogCustom(100);
							DataLoader
									.getInstance()
									.startTaskForResult(
											TaskType.TaskOrMethod_CommonGetPayUrl,
											payParams,
											CommonServiceSignUpActivity.this);
						}
					});
		}

	}

	private void initView() {
		LinearLayout layout;
		ViewSort();// 排序
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				dip2px(this, 42));
		params.setMargins(0, 0, 0, dip2px(this, 10));
		for (CommItemsEntity itemsEntity : mServiceEntity.getItems()) {
			layout = (LinearLayout) View.inflate(this,
					R.layout.view_layout_linearlayout, null);
			TextView textView = new TextView(this);
			textView.setTextColor(Color.BLACK);
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setLayoutParams(new RadioGroup.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, dip2px(this, 30)));
			switch (itemsEntity.getType()) {
			case 0:
				RadioGroup radioGroup = new RadioGroup(this);
				radioGroup.setId(itemsEntity.getId());
				RadioButton radioButton;
				textView.setText(itemsEntity.getName());
				layout.addView(textView);
				for (ItemEntity mItemEntity : itemsEntity.getItems()) {
					radioButton = (RadioButton) View.inflate(this,
							R.layout.view_layout_radiobutton, null);
					radioButton.setId((itemsEntity.getId() + itemsEntity
							.getType()) * 10 + mItemEntity.getId());
					radioButton.setText(mItemEntity.getName());
					radioButton.setLayoutParams(new RadioGroup.LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT, dip2px(this, 35)));
					radioGroup.addView(radioButton);
				}
				radioGroup.check((itemsEntity.getId() + itemsEntity.getType())
						* 10 + itemsEntity.getItems().get(0).getId());
				layout.addView(radioGroup);
				break;
			case 1:
				textView.setText(itemsEntity.getName());
				FlowLayout fLinearLayout = (FlowLayout) View.inflate(this,
						R.layout.view_layout_flowlayout, null);
				layout.addView(textView);
				CheckBox checkBox;
				for (ItemEntity mItemEntity : itemsEntity.getItems()) {
					checkBox = (CheckBox) View.inflate(this,
							R.layout.view_layout_checkbox, null);
					checkBox.setId((itemsEntity.getId() + itemsEntity.getType())
							* 10 + mItemEntity.getId());
					checkBox.setText(mItemEntity.getName());
					checkBox.setLayoutParams(new RadioGroup.LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT, dip2px(this, 35)));
					fLinearLayout.addView(checkBox);
				}
				layout.addView(fLinearLayout);
				break;
			case 2:
				params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, dip2px(
						this, 42 * itemsEntity.getRow()));
				params.setMargins(0, 0, 0, dip2px(this, 10));
				EditText editText = (EditText) View.inflate(this,
						R.layout.view_layout_edittext, null);
				if (itemsEntity.getRow() > 1) {
					editText.setGravity(Gravity.TOP | Gravity.LEFT);
					editText.setPadding(dip2px(this, 10), dip2px(this, 10),
							dip2px(this, 10), dip2px(this, 10));
				} else {
					editText.setPadding(dip2px(this, 10), dip2px(this, 0),
							dip2px(this, 10), dip2px(this, 0));
				}
				editText.setHint(itemsEntity.getName());
				editText.setId(itemsEntity.getId());
				editText.setLines(itemsEntity.getRow());
				editText.setLayoutParams(params);
				layout.addView(editText);
				break;
			case 3:
				textView.setText(itemsEntity.getName());
				layout.addView(textView);
				Spinner spinner = (Spinner) View.inflate(this,
						R.layout.view_layout_spinner, null);
				spinner.setId(itemsEntity.getId());
				LayoutParams params2 = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						dip2px(this, 42));
				spinner.setLayoutParams(params2);
				String[] selectData = new String[itemsEntity.getItems().size()];
				for (int i = 0; i < selectData.length; i++) {
					selectData[i] = itemsEntity.getItems().get(i).getName();
				}
				spinner.setAdapter(new MSpinnerAdapter(this, selectData));
				layout.addView(spinner);
				break;
			default:
				break;
			}
			viewLayout.addView(layout);
		}
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
	public void onClick(View view) {
		HashMap<String, Object> submitParams = new HashMap<String, Object>();
		HashMap<String, Object> itemMap;
		List<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		for (CommItemsEntity itemsEntity : mServiceEntity.getItems()) {
			itemMap = new HashMap<String, Object>();
			switch (itemsEntity.getType()) {
			case 0:
				RadioGroup radioGroup = (RadioGroup) findViewById(itemsEntity
						.getId());
				itemMap.put("id", itemsEntity.getId());
				itemMap.put("value",
						(radioGroup.getCheckedRadioButtonId() - ((itemsEntity
								.getId() + itemsEntity.getType()) * 10)));
				break;
			case 1:
				CheckBox checkBox;
				String value = "";
				for (ItemEntity mEntity : itemsEntity.getItems()) {
					checkBox = (CheckBox) findViewById((itemsEntity.getId() + itemsEntity
							.getType()) * 10 + mEntity.getId());
					if (checkBox.isChecked()) {
						value += mEntity.getId() + ",";
					}
				}
				if (itemsEntity.isRequired()) {
					if (TextUtils.isEmpty(value)) {
						Toast.makeText(CommonServiceSignUpActivity.this,
								R.string.input_incomplete_info,
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				if (value.contains(",")) {
					value = value.substring(0, value.length() - 1);
				}
				itemMap.put("id", itemsEntity.getId());
				itemMap.put("value", value);
				break;
			case 2:
				EditText editText = (EditText) findViewById(itemsEntity.getId());
				itemMap.put("id", itemsEntity.getId());
				if (itemsEntity.isRequired()) {
					if (TextUtils.isEmpty(editText.getText().toString())) {
						Toast.makeText(CommonServiceSignUpActivity.this,
								R.string.input_incomplete_info,
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				itemMap.put("value", editText.getText().toString());
				break;
			case 3:
				Spinner spinner = (Spinner) findViewById(itemsEntity.getId());
				itemMap.put("id", itemsEntity.getId());
				itemMap.put(
						"value",
						itemsEntity.getItems()
								.get((int) spinner.getSelectedItemId()).getId());
				break;
			default:
				break;
			}
			params.add(itemMap);
		}
		submitParams.put("id", mServiceEntity.getId());
		submitParams.put("items", params);
		HashMap<String, Object> mParams = new HashMap<String, Object>();
		mParams.put("items", JsonUtil.toJson(submitParams));
		System.out.println("submit========data:"
				+ JsonUtil.toJson(submitParams));
		showDialogCustom(DATA_LODING);
		Utils.removeSoftKeyboard(CommonServiceSignUpActivity.this);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_CommonServerSubmit, mParams, this);
	}

	private void ViewSort() {
		for (int i = 0; i < mServiceEntity.getItems().size() - 1; i++) {
			for (int j = i + 1; j < mServiceEntity.getItems().size(); j++) {
				CommItemsEntity temp;
				if (mServiceEntity.getItems().get(i).getSequence() > mServiceEntity
						.getItems().get(j).getSequence()) {
					temp = mServiceEntity.getItems().get(j);
					mServiceEntity.getItems().set(j,
							mServiceEntity.getItems().get(i));
					mServiceEntity.getItems().set(i, temp);
				}
			}
		}
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		removeDialogCustom();
		if (result == null) {
			return;
		}
		if (result instanceof Error) {
			Toast.makeText(CommonServiceSignUpActivity.this,
					((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_CommonServerSubmit:
			if ("1".equals(((JSONObject) result).optString("result"))) {
				mServiceEntity = null;
				mServiceEntity = new Gson().fromJson(
						((JSONObject) result).optString("item"),
						CommonServiceEntity.class);
				initData();
			} else {
				Toast.makeText(this, R.string.activity_common_service_get_fail,
						Toast.LENGTH_SHORT).show();
				return;
			}
			break;

		case TaskOrMethod_CommonGetPayUrl:
			Gson gson = new Gson();
			mPayResultEntity = gson.fromJson(result.toString(),
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

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			mServiceEntity.setPayment(true);
			initData();
		}
	}

}