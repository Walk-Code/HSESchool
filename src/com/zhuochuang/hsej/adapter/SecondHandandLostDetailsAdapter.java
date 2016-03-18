package com.zhuochuang.hsej.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

import com.model.ImageLoaderConfigs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.entity.SecondHandEntity.ItemsEntity.ImagesEntity;

public class SecondHandandLostDetailsAdapter extends MBaseAdapter<ImagesEntity> {

	public SecondHandandLostDetailsAdapter(Context context,
			List<ImagesEntity> datas) {
		super(context, datas);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImagesEntity mEntity = (ImagesEntity) getItem(position);
		ImageView imageView = null;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.activity_second_handand_lost_details_head_item,
					null);
			convertView.setLayoutParams(new Gallery.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			imageView = (ImageView) convertView.findViewById(R.id.imageview);
		}
		if (imageView != null)
			ImageLoader.getInstance().displayImage(mEntity.getPath(),
					imageView, ImageLoaderConfigs.displayImageOptionsBg);
		return convertView;
	}
}
