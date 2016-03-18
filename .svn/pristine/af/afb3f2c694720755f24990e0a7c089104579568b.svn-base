package com.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;

@SuppressWarnings("rawtypes")
public class AdvertiseLoader extends AsyncTask{

	private Context mContext;
	private boolean isFinish = false;
	
	public AdvertiseLoader(Context context){
		mContext = context;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		try{
			URL url = new URL((String) params[0]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(30 * 1000);
			conn.setReadTimeout(30 * 1000);
			conn.setRequestMethod("GET");
			InputStream inStream = conn.getInputStream();
	        File file = new File(CacheHandler.getCacheDir(mContext), Configs.AdvertiseImg + ".temp");  
	        if(!file.exists()){
	        	file.createNewFile();
	        }
	        OutputStream outStream = new FileOutputStream(file);
	        byte buffer [] = new byte[1024];
	        int len  = 0;
	        while((len = inStream.read(buffer))  != -1){
	        	outStream.write(buffer, 0, len);
	        }
	        outStream.flush();
	        outStream.close();
	        inStream.close();
	        isFinish = true;
		}
		catch (Exception e) {
			// TODO: handle exception
			isFinish = false;
		}
		return params[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		File file = new File(CacheHandler.getCacheDir(mContext), Configs.AdvertiseImg + ".temp");
		if(file.exists()){
			if(isFinish){
				try {
					SharedPreferenceHandler.saveAdvertisePath(mContext, (String)result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				file.renameTo(new File(CacheHandler.getCacheDir(mContext), Configs.AdvertiseImg));
			}
			else{
				file.delete();
			}
		}
	}
}
