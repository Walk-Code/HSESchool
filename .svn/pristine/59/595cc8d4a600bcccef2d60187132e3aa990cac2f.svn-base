package com.zhuochuang.hsej.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.util.Utils;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.entity.RecruitServiceEntity.RecruitItemsEntity;
import com.zhuochuang.hsej.view.SecondHandandLostRecruitItem;

public class RecruitServiceAdapter extends SHLBaseAdapter<RecruitItemsEntity> {
	private boolean isEdit = false;
	private boolean isMySelf = false;

	public RecruitServiceAdapter(Context context, boolean isGoing,
			boolean isComplete, boolean isMySelf) {
		super(context);
		this.context = context;
		this.isMySelf = isMySelf;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RecruitItemsEntity mItemsEntity = (RecruitItemsEntity) getItem(position);
		final SecondHandandLostRecruitItem recruitItem;
		if (convertView == null)
			convertView = new SecondHandandLostRecruitItem(context);
		recruitItem = (SecondHandandLostRecruitItem) convertView;
		if ("1".equals(mItemsEntity.getStatus()) && isMySelf) {
			recruitItem.shlrTvGoing.setVisibility(View.VISIBLE);
		} else {
			recruitItem.shlrTvGoing.setVisibility(View.GONE);
		}
		recruitItem.shlrTitle.setText(mItemsEntity.getName());
		String[] jobType = context.getResources().getStringArray(
				R.array.activity_publish_recruit_job_type);
		recruitItem.shlrStatus.setText(String.format(context.getResources()
				.getString(R.string.text_job_num), mItemsEntity.getPeopleNum())
				+ (mItemsEntity.getJobNature() == 1 ? jobType[0] : jobType[1]));
		recruitItem.shlrContent.setText(mItemsEntity.getJobContent());
		recruitItem.shlrAddress.setText(mItemsEntity.getUserName());
		recruitItem.shlrDate.setText(Utils.getAlmostTime(mItemsEntity
				.getCreateDate()));
		if (isMySelf) {
			if ("1".equalsIgnoreCase(mItemsEntity.getStatus()) && !isEdit)
				recruitItem.shlrEditLayout.setVisibility(View.VISIBLE);
			else
				recruitItem.shlrEditLayout.setVisibility(View.GONE);
		} else {
			recruitItem.shlrEditLayout.setVisibility(View.GONE);
		}
		if (isEdit) {
			recruitItem.shlrCBox.setVisibility(View.VISIBLE);
			recruitItem.shlrCBox.setChecked(mItemsEntity.isSelect());
		} else {
			recruitItem.shlrCBox.setVisibility(View.GONE);
		}
		recruitItem.shlrCBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (mClickListener != null) {
							mClickListener.onChecked(isChecked, position);
						}
					}
				});
		recruitItem.shlrBtnFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mClickListener != null) {
					mClickListener.onFinishClick(recruitItem.shlrBtnFinish,
							position);
				}
			}
		});
		recruitItem.shlrBtnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mClickListener != null) {
					mClickListener.onEditClick(recruitItem.shlrBtnEdit,
							position);
				}
			}
		});
		return convertView;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
		notifyDataSetChanged();
	}
}
