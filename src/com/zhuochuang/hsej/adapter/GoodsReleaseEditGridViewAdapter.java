package com.zhuochuang.hsej.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.layout.photoalbum.Bimp;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.Utils;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.entity.EditImageEntity;

public class GoodsReleaseEditGridViewAdapter extends
		MBaseAdapter<EditImageEntity> {
	public List<EditImageEntity> tempImageEntities;

	public GoodsReleaseEditGridViewAdapter(Context context) {
		super(context);
		tempImageEntities = new ArrayList<EditImageEntity>();
	}

	@Override
	public int getCount() {
		if (tempImageEntities.size() > 7)
			return 8;
		return tempImageEntities.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return tempImageEntities.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.photoalbumitem_published_grid, null);
			@SuppressWarnings("deprecation")
			int width = (((Activity) context).getWindowManager()
					.getDefaultDisplay().getWidth() - Utils.dipToPixels(
					(context), 50)) / 4;
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(width,
					width);
			convertView.setLayoutParams(lp);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == tempImageEntities.size()) {
			ImageLoader.getInstance().displayImage(null, holder.image);
			holder.image.setImageResource(R.drawable.btn_addpicture);
		} else {
			EditImageEntity imageEntity = (EditImageEntity) getItem(position);
			if (imageEntity.getPath().startsWith("http://")) {
				holder.image.setScaleType(ScaleType.CENTER_CROP);
				ImageLoader.getInstance().displayImage(imageEntity.getPath(),
						holder.image);
			} else {
				try {
					Bitmap bm = Bimp.revitionImageSize(
							imageEntity.getPath(),
							imageEntity.getPath().equalsIgnoreCase(
									imageEntity.getPath()));
					holder.image.setImageBitmap(bm);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return convertView;
	}

	public void Update() {
		tempImageEntities = new ArrayList<EditImageEntity>();
		tempImageEntities.addAll(datas);
		for (String path : Bimp.drr) {
			tempImageEntities.add(new EditImageEntity(-1, path, true));
		}
		notifyDataSetChanged();
	}

	public void UpdateHandle() {
		for (int i = 0; i < tempImageEntities.size(); i++) {
			if (!tempImageEntities.get(i).isCheck()) {
				for (int j = 0; j < datas.size(); j++) {
					if (datas.get(j).getPath()
							.equals(tempImageEntities.get(i).getPath()))
						datas.remove(j--);
				}
				tempImageEntities.remove(i--);
			}
		}
		notifyDataSetChanged();
	}

	public class ViewHolder {
		public ImageView image;
	}

}
