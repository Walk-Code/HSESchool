package com.zhuochuang.hsej;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.model.CacheHandler;
import com.model.Configs;
import com.model.DataLoader;

public class AdvertiseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_advertise);
		
		String advertise = CacheHandler.getCacheDir(AdvertiseActivity.this) + "/" + Configs.AdvertiseImg;
		File file = new File(advertise);
		if(file.exists()){
			((ImageView)findViewById(R.id.imageview)).setImageURI(Uri.parse(advertise));
		}
		else{
			finish();
		}
		
		long timeLength = 2000;
		String settingsTime  = DataLoader.getInstance().getSettingsKey("residence");
		if(settingsTime != null && settingsTime.length() > 0){
			try {
				timeLength = Long.parseLong(settingsTime);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity(new Intent(AdvertiseActivity.this, MainActivity.class));
				AdvertiseActivity.this.finish();
			}
		}, timeLength);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        }
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try{
			((BitmapDrawable)((ImageView)findViewById(R.id.imageview)).getBackground()).getBitmap().recycle();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		catch (Error error) {
			// TODO: handle exception
		}
	}
}
