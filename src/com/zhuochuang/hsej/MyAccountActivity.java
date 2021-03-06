package com.zhuochuang.hsej;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.layout.PhotoAlbumDialog;
import com.layout.photoalbum.Bimp;
import com.layout.photoalbum.FileUtils;
import com.layout.photoalbum.PhotoAlbumActivity;
import com.model.CacheHandler;
import com.model.Configs;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.SharedPreferenceHandler;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhuochuang.hsej.store.StoreShoppingAddressActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyAccountActivity extends BaseActivity{

	JSONObject mUserInfoObj;
	
	PhotoAlbumDialog mPhotoAlbumDialog;
	String mCameraPath;
	String mBase64Str;
	String mHeadImageId;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_myaccount);
		setTitleText(R.string.myaccount_title);
		
		showDialogCustom(DIALOG_CUSTOM);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserGetUser, null, MyAccountActivity.this);
	}
	
	private void startUpload(){
		showDialogCustom(DIALOG_CUSTOM);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("imgStr", mBase64Str);
		params.put("resourceType", "30");
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserUploadPhoto, params, MyAccountActivity.this);
	}
	
	private void setUserMsg(){
		if(mUserInfoObj == null){
			return;
		}
		ImageLoader.getInstance().displayImage(mUserInfoObj.optString("headImage"), 
			     ((ImageView)findViewById(R.id.imageview_avator)), ImageLoaderConfigs.displayImageOptionsRound);
		((TextView)findViewById(R.id.textview_xm)).setText(mUserInfoObj.optString("xm"));
		((TextView)findViewById(R.id.textview_nickname)).setText(mUserInfoObj.optString("nickName"));
		((TextView)findViewById(R.id.textview_xb)).setText(mUserInfoObj.optString("xb").equalsIgnoreCase("0") ? 
				getResources().getString(R.string.user_xb_nv) : getResources().getString(R.string.user_xb_nan));
		
		((TextView)findViewById(R.id.textview_honor)).setText(mUserInfoObj.optString("honoraryTitle"));
		((TextView)findViewById(R.id.textview_birth)).setText(mUserInfoObj.optString("csrq"));
		((TextView)findViewById(R.id.textview_xh)).setText(mUserInfoObj.optString("xh"));
		((TextView)findViewById(R.id.textview_class)).setText(mUserInfoObj.optString("bjmc"));
		
		((TextView)findViewById(R.id.textview_phone)).setText(mUserInfoObj.optString("phone"));
	}
	
	public void onMyAccountClick(View view){
		Intent intent = null;
		switch (view.getId()) {
		case R.id.group_avator:
			if(mPhotoAlbumDialog == null){
				mPhotoAlbumDialog = new PhotoAlbumDialog(MyAccountActivity.this);
				mPhotoAlbumDialog.setItemSelectListener(new PhotoAlbumDialog.OnItemSelectListener() {
				@Override
				public void onItemClick(int position) {
				switch (position) {
				case R.id.dialog_item1:
					File file = CacheHandler.getCameraImgPath(MyAccountActivity.this);
					mCameraPath = file.getAbsolutePath();
					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					startActivityForResult(intentFromCapture, Configs.REQUESTCODE_CAMERA);
					break;
				case R.id.dialog_item2:
					clearBimp();
					Bimp.imgMaxSize = 1;
					Intent intent = new Intent(MyAccountActivity.this, PhotoAlbumActivity.class);
					startActivityForResult(intent, Configs.REQUESTCODE_IMAGE);
					break;
				case R.id.dialog_item3:
					mPhotoAlbumDialog.cancel();
					break;
				}
				}
				});
			}
			mPhotoAlbumDialog.show();
			return;
		case R.id.group_nickname:
			intent = new Intent(MyAccountActivity.this, MyAccountChangeActivity.class);
			intent.putExtra("hint", getResources().getString(R.string.login_register_nickname));
			intent.putExtra("type", Configs.AccountChangeType_NickName);
			startActivityForResult(intent, Configs.AccountChangeType_NickName);
			break;
		case R.id.group_honor:
			startActivity(new Intent(MyAccountActivity.this, MyHonorActivity.class));
			break;
		case R.id.group_consigneesaddress:
			intent = new Intent(MyAccountActivity.this, StoreShoppingAddressActivity.class);
			intent.putExtra("fromAccount", true);
			startActivity(intent);
			break;
		case R.id.group_photoalbum:
			intent = new Intent(MyAccountActivity.this, AlbumActivity.class);
			intent.putExtra("isFriend", false);
			intent.putExtra("isDeleteOp", true);
			startActivity(intent);
			break;
		case R.id.group_phone:
			if(mUserInfoObj == null){
				return;
			}
			intent = new Intent(MyAccountActivity.this, PhoneBindingActivity.class);
			intent.putExtra("phone", mUserInfoObj.optString("phone"));
			intent.putExtra("isBangding", mUserInfoObj.optBoolean("isBangding", false));
			startActivityForResult(intent, Configs.REQUESTCODE_BindingPhone);
			break;
		case R.id.group_introduce:
			intent = new Intent(MyAccountActivity.this, MyAccountChangeActivity.class);
			intent.putExtra("hint", getResources().getString(R.string.myaccount_hint10));
			intent.putExtra("content", mUserInfoObj.optBoolean("isBangding", false));
			intent.putExtra("type", Configs.AccountChangeType_Introduce);
			startActivityForResult(intent, Configs.AccountChangeType_Introduce);
			break;
		case R.id.group_specialty:
			intent = new Intent(MyAccountActivity.this, MyAccountChangeActivity.class);
			intent.putExtra("hint", getResources().getString(R.string.myaccount_hint11));
			intent.putExtra("content", mUserInfoObj.optBoolean("isBangding", false));
			intent.putExtra("type", Configs.AccountChangeType_Specialty);
			startActivityForResult(intent, Configs.AccountChangeType_Specialty);
			break;
		case R.id.group_declaration:
			intent = new Intent(MyAccountActivity.this, MyAccountChangeActivity.class);
			intent.putExtra("hint", getResources().getString(R.string.myaccount_hint12));
			intent.putExtra("content", mUserInfoObj.optBoolean("isBangding", false));
			intent.putExtra("type", Configs.AccountChangeType_Declaration);
			startActivityForResult(intent, Configs.AccountChangeType_Declaration);
			break;

		default:
			break;
		}
	}
	
	private void clearBimp(){
		if(Bimp.drr.size() != 0){
			Bimp.drr.clear();
			Bimp.drr = new ArrayList<String>();
		}
		if(Bimp.bmp.size() != 0){
			Bimp.bmp.clear();
			Bimp.bmp = new ArrayList<Bitmap>();
		}
		if(Bimp.base64Arr.size() != 0){
			Bimp.base64Arr.clear();
			Bimp.base64Arr = new ArrayList<String>();
		}
		Bimp.max = 0;
		Bimp.imgMaxSize = 9;
		FileUtils.deleteDir();
	}
	
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		 // 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 180);
		intent.putExtra("outputY", 180);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Configs.REQUESTCODE_CropResult);
	}
	
	/**
	 * 保存裁剪之后的图片数据 ,   base64编码 
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();  
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 - 100)压缩文件  
         //   findViewById(R.id.user_avator_circle).setVisibility(View.VISIBLE);
         //   ((CircleImageView)findViewById(R.id.user_avator_circle)).setImageBitmap(photo); 
            byte[] bytes = stream.toByteArray();  
            mBase64Str = Base64.encodeToString(bytes, Base64.DEFAULT);
            startUpload();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case Configs.REQUESTCODE_IMAGE:
				try {
					File file = new File(Bimp.drr.get(0));
					startPhotoZoom(Uri.fromFile(file));
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case Configs.REQUESTCODE_CAMERA:
				File tempFile = new File(mCameraPath);
				if(tempFile != null){
					startPhotoZoom(Uri.fromFile(tempFile));
				}
				break;
			case Configs.REQUESTCODE_CropResult:
				if(data != null) {
					getImageToView(data);
				}
				break;
			case Configs.REQUESTCODE_BindingPhone:
				if(data == null) {
					return;
				}
				String phone = data.getStringExtra("phone");
				try {
					mUserInfoObj.put("phone", phone);
					mUserInfoObj.put("isBangding", true);
				} 
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((TextView)findViewById(R.id.phone_text)).setText(phone);
				((TextView)findViewById(R.id.textview_binding_phone)).setText(mUserInfoObj.optBoolean("isBangding", false)? 
						getResources().getString(R.string.my_homepage_changephone) : getResources().getString(R.string.my_homepage_bindingphone));
				
				showDialogCustom(DIALOG_CUSTOM);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserGetUser, null, MyAccountActivity.this);
				break;
			case Configs.AccountChangeType_NickName:
			case Configs.AccountChangeType_Introduce:
			case Configs.AccountChangeType_Specialty:
			case Configs.AccountChangeType_Declaration:
				showDialogCustom(DIALOG_CUSTOM);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserGetUser, null, MyAccountActivity.this);
				break;
			}
	    }
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		if(result == null){
			removeDialogCustom();
			return;
		}
		
		if(result instanceof Error){
			removeDialogCustom();
			Toast.makeText(MyAccountActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserGetUser:
			removeDialogCustom();
			if(result instanceof JSONObject && ((JSONObject)result).has("item")){
				mUserInfoObj = ((JSONObject)result).optJSONObject("item");
				try {
					SharedPreferenceHandler.saveUserInfo(MyAccountActivity.this, mUserInfoObj.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			setUserMsg();
			break;
		case TaskOrMethod_UserUploadPhoto:
			if(result instanceof JSONObject){
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserGetUser, null, MyAccountActivity.this);
			}
			break;
		default:
			break;
		}
	}
}
