package com.zhuochuang.hsej.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.util.Utils;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.entity.OneCardSolutionEntity;

public class OneCardSolutionAdapter extends MBaseAdapter<OneCardSolutionEntity> {
	protected int isSave;
	protected int code;

	public OneCardSolutionAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OneCardSolutionEntity mCardSolutionEntity = (OneCardSolutionEntity) getItem(position);
		ViewHolder vHolder;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.activity_one_card_solution_list_item, null);
			vHolder = new ViewHolder();
			vHolder.listName = (TextView) convertView
					.findViewById(R.id.list_name);
			vHolder.listAmount = (TextView) convertView
					.findViewById(R.id.list_amount);
			vHolder.listDate = (TextView) convertView
					.findViewById(R.id.list_date);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}
		vHolder.listName.setText(mCardSolutionEntity.getDescribe());
		int color;
		if (mCardSolutionEntity.getOpFare() > 0) {
			color = Color.rgb(221, 81, 79);
		} else {
			color = Color.rgb(1, 153, 1);
		}
		vHolder.listAmount.setTextColor(color);
		vHolder.listAmount.setText(String.format(context.getResources()
				.getString(R.string.list_amount_text), mCardSolutionEntity
				.getOpFare()));
		vHolder.listDate.setText(Utils.getAlmostTime(mCardSolutionEntity
				.getCreateDate()));
		return convertView;
	}

	class ViewHolder {
		public TextView listName;
		public TextView listAmount;
		public TextView listDate;
	}

}
