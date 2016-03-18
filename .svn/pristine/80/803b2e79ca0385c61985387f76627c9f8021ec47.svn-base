package com.layout.photoalbum;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

public class Bimp {
	public static int max = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();	
	public static List<String> drr = new ArrayList<String>();
	public static ArrayList<String> base64Arr = new ArrayList<String>();
	public static int imgMaxSize = 9;//最多选择张数
	

	public static Bitmap revitionImageSize(String path, boolean isCamera) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				
				if(isCamera){
					//int degree = Utils.readPictureDegree(file.getAbsolutePath()); 
					//System.out.println("=====55=====" + degree);
					//if(degree != 0){
						Matrix m = new Matrix();  
		                int width = bitmap.getWidth();  
		                int height = bitmap.getHeight();  
		           //     m.setRotate(90);
		                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
					//}
				}
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();  
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
				String base64 = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
				base64Arr.add(base64);
				break;
			}
			i += 1;
		}
		
		return bitmap;
	}
	
	public static String getBase64(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		String base64 = null;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();  
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
				base64 = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
				break;
			}
			i += 1;
		}
		//if(base64 != null && base64.length() > 0){
			//base64 = base64.replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r", "");
		//}
		return base64;
	}
}
