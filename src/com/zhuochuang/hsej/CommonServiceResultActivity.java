package com.zhuochuang.hsej;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhuochuang.hsej.adapter.MSpinnerAdapter;
import com.zhuochuang.hsej.entity.CommonServiceEntity;
import com.zhuochuang.hsej.entity.CommonServiceEntity.CommItemsEntity;
import com.zhuochuang.hsej.entity.CommonServiceEntity.CommItemsEntity.ItemEntity;
import com.zhuochuang.hsej.view.FlowLayout;

public class CommonServiceResultActivity extends BaseActivity {
	CommonServiceEntity mServiceEntity;
	private LinearLayout viewLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_common_service_result);
		mServiceEntity = (CommonServiceEntity) getIntent().getExtras()
				.getSerializable("comm_entity");
		setTitleText(mServiceEntity.getName());
		viewLayout = (LinearLayout) findViewById(R.id.mian_layout);
		initData();
	}

	private void initData() {
		LinearLayout layout;
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
					radioButton.setChecked(mItemEntity.isChecked());
					radioButton.setClickable(false);
					radioGroup.addView(radioButton);
				}
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
					checkBox.setEnabled(false);
					checkBox.setChecked(mItemEntity.isChecked());
					fLinearLayout.addView(checkBox);
				}
				layout.addView(fLinearLayout);
				break;
			case 2:
				params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, dip2px(
						this, 42 * itemsEntity.getRow()));
				textView.setText(itemsEntity.getName());
				layout.addView(textView);
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
				editText.setText(itemsEntity.getResult());
				editText.setHint(itemsEntity.getName());
				editText.setId(itemsEntity.getId());
				editText.setLines(itemsEntity.getRow());
				editText.setLayoutParams(params);
				editText.setEnabled(false);
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
				for (int i = 0; i < itemsEntity.getItems().size(); i++) {
					if (itemsEntity.getItems().get(i).isChecked())
						spinner.setSelection(i);
				}
				spinner.setEnabled(false);
				layout.addView(spinner);
				break;
			default:
				break;
			}
			viewLayout.addView(layout);
		}
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
