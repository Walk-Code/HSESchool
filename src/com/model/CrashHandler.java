package com.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.zhuochuang.hsej.R;

/**
 * 程序崩溃异常收集处理�?
 * 在LHSESchoolApp.java中实例，
 * 并在AndroidManifest.xml中配置android:name=".HSESchoolApp"
 * 如果在LHSESchoolApp.java里面实例了， 那么logcat里是不会打印出错误信息的，只能在生成指定的文件里查看�?
 * 
 * @category filePath定义crash文件生成路径�?
 * @category infos 收集crash错误信息�?
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {
	
	private Thread.UncaughtExceptionHandler mDefaultHandler; 
	private static CrashHandler crashHandler = new CrashHandler();
	private Context mContext;
	private Map<String, String> infos = new HashMap<String, String>(); 
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private String filePath ;
	
	private CrashHandler(){}
	
	public static CrashHandler getInstance() {
		return crashHandler;
	}

	public void init(Context context) {
		mContext = context;
		filePath = getCacheDir(mContext) + mContext.getResources().getString(R.string.crash_file_path) + "/";
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	private File getCacheDir(Context context){
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

	private boolean handleException(Throwable e) {
		if (e == null) {
			return false;
		}
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, mContext.getString(R.string.crash_toast_message), Toast.LENGTH_LONG).show();
				
				Looper.loop();
				}
			}.start();
		collectDeviceInfo(mContext);
		saveCrashInfoFile(e);
		return true;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}
		else {
			
			try {
				Thread.sleep(800);
			} 
			catch (InterruptedException e) {
				Log.e("CrashHandler", "error : ", e);
			}
			System.exit(1);
		}
	}
	
	public void collectDeviceInfo(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (packageInfo != null) {
				String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
				String versionCode = packageInfo.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			
			Log.e("CrashHandler", "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d("CrashHandler", field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e("CrashHandler", "an error occured when collect crash info", e);
			}
		}
	}
	
	/**
	 * 
	 * 生成crash文件 
	 */
	private String saveCrashInfoFile(Throwable ex) {
		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			stringBuffer.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		stringBuffer.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = filePath ;
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
				fileOutputStream.write(stringBuffer.toString().getBytes());
				fileOutputStream.close();
			}
			return fileName;
		}catch (Exception e) {
			Log.e("CrashHandler", mContext.getString(R.string.crash_write_error), e);
		}
		return null;
	}
}
