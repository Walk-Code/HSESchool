package com.model;

import java.io.File;
import java.util.Date;

import android.content.Context;
import android.os.Environment;

import com.zhuochuang.hsej.R;

public class CacheHandler {
	
	/**
	 * 获取存储的路径， 如果sdcard不存在， 则存储在手机内存空间
	 */
	
	public static File getCacheDir(Context context){
		File sdDir = null; 
        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            sdDir = Environment.getExternalStorageDirectory();
        }
        else{
        	sdDir = context.getCacheDir();
        }
		File cacheDir = new File(sdDir, context.getResources().getString(R.string.app_dirsname));
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
		return cacheDir;
	}

	/**
	 * 图片缓存目录 
	 */
	public static File getImageCacheDir(Context context){
		File cacheDir = new File(getCacheDir(context), "image");
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
		return cacheDir;
	}
	
	public static File getCameraDir(Context context){
		File cacheDir = new File(getCacheDir(context), "camera");
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
		return cacheDir;
	}
	
	public static File getCameraImgPath(Context context){
		File file = new File(getCameraDir(context), new Date().getTime() + ".jpg");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}
}
