package com.zhuochuang.hsej;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.layout.photoview.PhotoViewer;
import com.model.DataLoader;
import com.model.TaskType;
import com.util.AutoGallery;
import com.util.PageGuide;
import com.util.Utils;
import com.zhuochuang.hsej.adapter.SecondHandandLostDetailsAdapter;
import com.zhuochuang.hsej.entity.SecondHandEntity.ItemsEntity;

public class SecondHandandLostDetailsActivity extends BaseActivity {

	private int nativeCode = 1;
	private AutoGallery mAutoGallery;
	private PageGuide mPageGuide;
	private ItemsEntity mGoodsEntity;
	private final int DIALOG_LODING = 0x100;
	private PhotoViewer mPhotoViewer;
	private String[] mPhotos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_handand_lost_details);
		initView();
		initData();
	}

	private void initView() {// 初始化UI
		findViewById(R.id.navbar_base).setVisibility(View.GONE);
		mAutoGallery = (AutoGallery) findViewById(R.id.gallery_banner);
		mPageGuide = (PageGuide) findViewById(R.id.pageguide);
	}

	private void initData() {// 初始化并加载数据
		Intent intent = getIntent();
		String goodsId = intent.getStringExtra("goodsid");
		nativeCode = intent.getIntExtra("nativeCode", 1);
		if (nativeCode == 1) {
			findViewById(R.id.shld_layout_price).setVisibility(View.VISIBLE);
			findViewById(R.id.shld_layout_trans_mode).setVisibility(
					View.VISIBLE);
		} else {
			findViewById(R.id.shld_layout_price).setVisibility(View.GONE);
			findViewById(R.id.shld_layout_trans_mode).setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(goodsId)) {
			Toast.makeText(this, R.string.notice_id_not_null,
					Toast.LENGTH_SHORT).show();
			finish();
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", goodsId);
		showDialogCustom(DIALOG_LODING);
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_SecondHandandLostDetails, params, this);
	}

	private void InflaterData() {// 数据加载完成后填充数据
		if (mGoodsEntity.getImages().size() > 1) {
			mPageGuide.setParams(mGoodsEntity.getImages().size(),
					Utils.dipToPixels(this, 7), Utils.dipToPixels(this, 7));
		}
		mAutoGallery.setAdapter(new SecondHandandLostDetailsAdapter(this,
				mGoodsEntity.getImages()));
		mAutoGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (mGoodsEntity.getImages().size() > 1)
					mPageGuide.setSelect(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		if (mGoodsEntity.getImages().size() > 0) {
			mPhotos = new String[mGoodsEntity.getImages().size()];
			for (int i = 0; i < mPhotos.length; i++) {
				mPhotos[i] = mGoodsEntity.getImages().get(i).getPath();
			}
		}
		mAutoGallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mPhotoViewer = new PhotoViewer(
						SecondHandandLostDetailsActivity.this, position);
				mPhotoViewer.setNewfeedPicArr(mPhotos);
				mPhotoViewer.showPhotoViewer(view);
			}
		});
		if (nativeCode == 1) {
			((TextView) findViewById(R.id.shld_tv_price)).setText(String
					.format("%.2f", mGoodsEntity.getPrice()));
			String[] modeNames = getResources().getStringArray(
					R.array.goods_release_transaction_mode);
			if (mGoodsEntity.getTradingType() == 1) {
				((TextView) findViewById(R.id.shld_tv_trans_mode))
						.setText(modeNames[0]);
			} else {
				((TextView) findViewById(R.id.shld_tv_trans_mode))
						.setText(modeNames[1]);
			}
		}

		if ("1".equals(mGoodsEntity.getStatus())) {
			((TextView) findViewById(R.id.shld_tv_title)).setText(Html
					.fromHtml(mGoodsEntity.getName()
							+ "<font color='#DC4C4B'><small>("
							+ getResources().getString(
									R.string.activitysecendhand_lost_tab_going)
							+ ")</small></font>"));
		} else {
			((TextView) findViewById(R.id.shld_tv_title))
					.setText(Html.fromHtml(mGoodsEntity.getName()
							+ "<font color='#DC4C4B'><small>("
							+ getResources()
									.getString(
											R.string.activitysecendhand_lost_tab_complete)
							+ ")</small></font>"));
		}

		((TextView) findViewById(R.id.shld_tv_contact)).setText(mGoodsEntity
				.getLinkMan());
		((TextView) findViewById(R.id.shld_tv_contact_phone))
				.setText(mGoodsEntity.getPhone());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m");
		((TextView) findViewById(R.id.shld_tv_publish_time)).setText(dateFormat
				.format(new Date(mGoodsEntity.getCreateDate())));
		if (nativeCode == 1) {
			((TextView) findViewById(R.id.shld_tv_name))
					.setText(R.string.second_handand_details_name);
		} else {
			((TextView) findViewById(R.id.shld_tv_name))
					.setText(R.string.lost_details_name);
		}

		((TextView) findViewById(R.id.shld_tv_desc)).setText(mGoodsEntity
				.getContent());
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.shld_btn_back:
			finish();
			break;
		}
	}

	@Override
	public void taskStarted(TaskType type) {

	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		removeDialogCustom();
		if (result == null)
			return;
		if (result instanceof Error) {
			Toast.makeText(SecondHandandLostDetailsActivity.this,
					((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		if (result instanceof JSONObject) {
			if ("1".equals(((JSONObject) result).optString("result").toString())) {
				Gson gson = new Gson();
				mGoodsEntity = gson.fromJson(((JSONObject) result)
						.optJSONObject("item").toString(), ItemsEntity.class);
				InflaterData();
			}
		}
	}

	@Override
	public void taskIsCanceled(TaskType type) {

	}
}
