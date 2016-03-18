package com.zhuochuang.hsej;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.Utils;

public class MyCardActivity extends BaseActivity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_mycard);
		setTitleText(R.string.mycenter_cell9);
		
		JSONObject userObj = DataLoader.getInstance().getUserInfoObj();
		if(userObj == null){
			return;
		}
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int useWidth = screenWidth - Utils.dipToPixels(MyCardActivity.this, 60);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(useWidth, useWidth / 3);
		lp1.gravity = Gravity.CENTER_HORIZONTAL;
		
		if(userObj.has("barcode")){
			findViewById(R.id.group_identity).setVisibility(View.VISIBLE);
			findViewById(R.id.imageview_identity).setLayoutParams(lp1);
			ImageLoader.getInstance().displayImage(DataLoader.getInstance().getUsetInfoKey("barcode"), 
					(ImageView)findViewById(R.id.imageview_identity), ImageLoaderConfigs.displayImageOptions);
		}
		if(userObj.has("barcodeByXh")){
			findViewById(R.id.group_xh).setVisibility(View.VISIBLE);
			findViewById(R.id.imageview_xh).setLayoutParams(lp1);
			ImageLoader.getInstance().displayImage(DataLoader.getInstance().getUsetInfoKey("barcodeByXh"), 
					(ImageView)findViewById(R.id.imageview_xh), ImageLoaderConfigs.displayImageOptions);
		}
		if(userObj.has("barcodeByZkzh")){
			findViewById(R.id.group_zkzh).setVisibility(View.VISIBLE);
			findViewById(R.id.imageview_zkzh).setLayoutParams(lp1);
			ImageLoader.getInstance().displayImage(DataLoader.getInstance().getUsetInfoKey("barcodeByZkzh"), 
					(ImageView)findViewById(R.id.imageview_zkzh), ImageLoaderConfigs.displayImageOptions);
		}
	}
	
	public void onQrCodeClick(View view){
		/*String url = null;
		switch (view.getId()) {
		case R.id.imageview_identity:
			url = DataLoader.getInstance().getUsetInfoKey("barcode");
			break;
		case R.id.imageview_xh:
			url = DataLoader.getInstance().getUsetInfoKey("barcodeByXh");
			break;
		case R.id.imageview_zkzh:
			url = DataLoader.getInstance().getUsetInfoKey("barcodeByZkzh");
			break;

		default:
			break;
		}
		if(url != null){
			PhotoViewer photoViewer = new PhotoViewer(MyCardActivity.this, 0);
			photoViewer.setSinglePicUrl(url);
			photoViewer.showPhotoViewer(view);
		}*/
	}
}
