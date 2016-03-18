package com.zhuochuang.hsej;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.CircleImageView;
import com.layout.PhotoAlbumDialog;
import com.layout.photoalbum.Bimp;
import com.layout.photoalbum.FileUtils;
import com.layout.photoalbum.PhotoAlbumActivity;
import com.model.CacheHandler;
import com.model.Configs;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.SharedPreferenceHandler;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;
/**
 * 
 * @author Administrator
 *modified dion
 */
@SuppressLint("HandlerLeak")
public class MyPageActivity extends BaseActivity implements OnClickListener{
	private GridView mAlbumListView;
	private ContentAdapter mAlbumAdapter;
	private JSONArray mAlbumArrMost;
	private JSONArray mAlbumArr;
	private boolean mIsEdit = false;
	private JSONObject mUserInfo;
	
	private PhotoAlbumDialog mPhotoAlbumDialog;
	private String mCameraPath;
	private String mBase64Str;
	private String mHeadImageId;
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mUserInfo = DataLoader.getInstance().getUserInfoObj();
		if(mUserInfo != null){
			setContentView(R.layout.activity_my_personal_page);
			initView();
		}
		loadUserData();
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Object[] objs = (Object[]) msg.obj;
				switch (msg.what) {
				case EventManager.EventType_ReloadAlbum:
					if(mAlbumArrMost != null){
						Utils.removeIndex(mAlbumArrMost, (Integer)objs[0]);
					}
					if(mAlbumArrMost != null && mAlbumArrMost.length() > 0){
						mAlbumArr = new JSONArray();
						for(int i = 0; i < mAlbumArrMost.length(); i++){
							if(i < 4){
								mAlbumArr.put(mAlbumArrMost.optJSONObject(i));
							}
							else{
								break;
							}
						}
					}
					mAlbumAdapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
			}
		});
	}
	
	public void loadUserData(){
		showDialogCustom(DIALOG_CUSTOM);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserGetUser, null, this);
	}
	
	//我的主页
	protected void initView(){
		setTitleText(R.string.my_homepage_me);
		findViewById(R.id.textview_right_text).setVisibility(View.VISIBLE);
		setEdit(false);
		
		ImageLoader.getInstance().displayImage(mUserInfo.optString("headImage"), 
			     ((ImageView)findViewById(R.id.user_avator)), ImageLoaderConfigs.displayImageOptionsRound);
		findViewById(R.id.user_avator_circle).setVisibility(View.GONE);
		
		((TextView)findViewById(R.id.user_name)).setText(String.format(getString(R.string.my_homepage_nickname), mUserInfo.optString("nickName")));
		((TextView)findViewById(R.id.user_gender)).setText(String.format(getString(R.string.my_homepage_gender), mUserInfo.optString("xb").equalsIgnoreCase("0") ? 
				getResources().getString(R.string.user_xb_nv) : getResources().getString(R.string.user_xb_nan)));
		((TextView)findViewById(R.id.user_birth)).setText(String.format(getString(R.string.my_homepage_birth), mUserInfo.optString("csrq")));
		((TextView)findViewById(R.id.user_xh)).setText(String.format(getString(R.string.my_homepage_xh), mUserInfo.optString("xh")));
		((TextView)findViewById(R.id.user_class)).setText(mUserInfo.optString("bjmc"));
		((EditText)findViewById(R.id.introduction_text)).setText(mUserInfo.optString("selfIntroduction"));
		((EditText)findViewById(R.id.specialty_text)).setText(mUserInfo.optString("speciality"));
		((EditText)findViewById(R.id.declaration_text)).setText(mUserInfo.optString("declaration"));
		((EditText)findViewById(R.id.nick_name_text)).setText(mUserInfo.optString("nickName"));
		((EditText)findViewById(R.id.phone_text)).setText(mUserInfo.optString("phone"));
		
		//85 + 25 + 10*(count - 1)
		final int iconWidth = (getResources().getDisplayMetrics().widthPixels - Utils.dipToPixels(this, 140))/4;
		mAlbumListView = (GridView) findViewById(R.id.photo_album);
		mAlbumListView.setEnabled(false);
		mAlbumListView.setAdapter(mAlbumAdapter = new ContentAdapter() {
				
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = View.inflate(MyPageActivity.this, R.layout.listcell_index_album, null);
				}
				
				ImageView image = (ImageView)convertView.findViewById(R.id.image);
				ViewGroup.LayoutParams params = image.getLayoutParams();
				params.width = iconWidth;
				params.height = iconWidth;
				
				JSONObject obj = mAlbumArr.optJSONObject(position);
				if(obj != null){
					ImageLoader.getInstance().displayImage(obj.optString("path"), 
							image, ImageLoaderConfigs.displayImageOptionsBg);
				}
				return convertView;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mAlbumArr == null ? 0 : mAlbumArr.length();
			}
		});
		
		RelativeLayout.LayoutParams lps = (LayoutParams) findViewById(R.id.photo_album_layout).getLayoutParams();
		lps.height = iconWidth;
		findViewById(R.id.photo_album_layout).setLayoutParams(lps);
		findViewById(R.id.photo_album_layout).setOnClickListener(this);
	}
	
	public void onBindingPhoneClick(View view){
		if(mUserInfo == null){
			return;
		}
		Intent intent = new Intent(MyPageActivity.this, PhoneBindingActivity.class);
		intent.putExtra("phone", mUserInfo.optString("phone"));
		intent.putExtra("isBangding", mUserInfo.optBoolean("isBangding", false));
		startActivityForResult(intent, Configs.REQUESTCODE_BindingPhone);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.photo_album_layout:
			Intent intent = new Intent(MyPageActivity.this, AlbumActivity.class);
			intent.putExtra("isFriend", false);
			intent.putExtra("isDeleteOp", true);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void onRightBtnClick(View v){
		if(mIsEdit){
			if(mBase64Str != null && mBase64Str.length() > 0){
				startUpload();
			}
			else{
				saveUserInfo();
			}
		}else {
			setEdit(mIsEdit ? false : true);
		}
		
	}
	
	private void startUpload(){
		showDialogCustom(DIALOG_CUSTOM);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("imgStr", mBase64Str);
		params.put("resourceType", "1");
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserUploadPhoto, params, MyPageActivity.this);
	}
	
	public void saveUserInfo(){
		String selfIntroduction = ((EditText)findViewById(R.id.introduction_text)).getText().toString().trim();
		String speciality = ((EditText)findViewById(R.id.specialty_text)).getText().toString().trim();
		String declaration = ((EditText)findViewById(R.id.declaration_text)).getText().toString().trim();
		String nickName = ((EditText)findViewById(R.id.nick_name_text)).getText().toString().trim();
		String phone = ((EditText)findViewById(R.id.phone_text)).getText().toString().trim();
		
//		if(seflIntroduction.equalsIgnoreCase("")){
//			Toast.makeText(this, getString(R.string.personal_page_tips1), Toast.LENGTH_SHORT).show();
//        	return;
//		}
//        if(speciality.equalsIgnoreCase("")){
//        	Toast.makeText(this, getString(R.string.personal_page_tips2), Toast.LENGTH_SHORT).show();
//        	return;
//		}
//        if(declaration.equalsIgnoreCase("")){
//        	Toast.makeText(this, getString(R.string.personal_page_tips3), Toast.LENGTH_SHORT).show();
//        	return;
//        }
        if(nickName == null || nickName.length() == 0 || nickName.replaceAll(" ", "").length() == 0){
        	Toast.makeText(this, getString(R.string.personal_page_nick_name), Toast.LENGTH_SHORT).show();
        	return;
        }
        
        if(phone != null && phone.length() > 0){
        	if(!Utils.isMobileNO(phone)){
        		Toast.makeText(this, getResources().getString(R.string.login_register_phone_valid), Toast.LENGTH_SHORT).show();
				return;
        	}
        }
        
		//showDialogCustom(DIALOG_CUSTOM);
		HashMap<String, Object> params = new HashMap<String, Object>();
		//params.put("seflIntroduction", seflIntroduction);
		params.put("selfIntroduction", selfIntroduction);
		params.put("speciality", speciality);
		params.put("declaration", declaration);
		params.put("nickName", nickName);
		params.put("phone", phone);
		params.put("isBangding", mUserInfo.optBoolean("isBangding", false));
		if(mHeadImageId != null && mHeadImageId.length() > 0){
			params.put("headImageId", mHeadImageId);
		}
		else{
			showDialogCustom(DIALOG_CUSTOM);
		}
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserSetProfile, params, MyPageActivity.this);
	}
	
	protected void setEdit(boolean isEdit){
		mIsEdit = isEdit;
		((EditText)findViewById(R.id.introduction_text)).setEnabled(isEdit);
		((EditText)findViewById(R.id.specialty_text)).setEnabled(isEdit);
		((EditText)findViewById(R.id.declaration_text)).setEnabled(isEdit);
		((EditText)findViewById(R.id.nick_name_text)).setEnabled(isEdit);
	//	((EditText)findViewById(R.id.phone_text)).setEnabled(isEdit);
		((ImageView)findViewById(R.id.user_avator)).setClickable(isEdit);
		
		//findViewById(R.id.textview_binding_phone).setVisibility(isEdit? View.VISIBLE : View.GONE);
		((TextView)findViewById(R.id.textview_binding_phone)).setText(mUserInfo.optBoolean("isBangding", false)? 
				getResources().getString(R.string.my_homepage_changephone) : getResources().getString(R.string.my_homepage_bindingphone));
		
		((TextView) findViewById(R.id.textview_right_text)).setText(getString(isEdit ? R.string.my_homepage_save : R.string.my_homepage_edit));
	}
	
	public void headerImageClick(View view){
		if(mPhotoAlbumDialog == null){
			mPhotoAlbumDialog = new PhotoAlbumDialog(MyPageActivity.this);
			mPhotoAlbumDialog.setItemSelectListener(new PhotoAlbumDialog.OnItemSelectListener() {
				@Override
				public void onItemClick(int position) {
				switch (position) {
				case R.id.dialog_item1:
					File file = CacheHandler.getCameraImgPath(MyPageActivity.this);
					mCameraPath = file.getAbsolutePath();
					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					startActivityForResult(intentFromCapture, Configs.REQUESTCODE_CAMERA);
					break;
				case R.id.dialog_item2:
					clearBimp();
					Bimp.imgMaxSize = 1;
					Intent intent = new Intent(MyPageActivity.this, PhotoAlbumActivity.class);
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
            findViewById(R.id.user_avator_circle).setVisibility(View.VISIBLE);
            ((CircleImageView)findViewById(R.id.user_avator_circle)).setImageBitmap(photo); 
            
            byte[] bytes = stream.toByteArray();  
            mBase64Str = Base64.encodeToString(bytes, Base64.DEFAULT);
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
				if (data != null) {
					getImageToView(data);
				}
				break;
			case Configs.REQUESTCODE_BindingPhone:
				if(data != null) {
					String phone = data.getStringExtra("phone");
					try {
						mUserInfo.put("phone", phone);
						mUserInfo.put("isBangding", true);
					} 
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					((TextView)findViewById(R.id.phone_text)).setText(phone);
					((TextView)findViewById(R.id.textview_binding_phone)).setText(mUserInfo.optBoolean("isBangding", false)? 
							getResources().getString(R.string.my_homepage_changephone) : getResources().getString(R.string.my_homepage_bindingphone));
					
					//loadUserData();
				}
				break;
			}
	    }
		super.onActivityResult(requestCode, resultCode, data);
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		clearBimp();
		EventManager.getInstance().removeHandlerListenner(mHandler);
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
			Toast.makeText(this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserGetUser:
			if(result instanceof JSONObject && ((JSONObject)result).has("item")){
				mUserInfo = ((JSONObject)result).optJSONObject("item");
				try {
					SharedPreferenceHandler.saveUserInfo(MyPageActivity.this, mUserInfo.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(mUserInfo != null){
					initView();
				}
			}
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserGetPhoto, null, this);
			break;
		case TaskOrMethod_UserGetPhoto:
			removeDialogCustom();
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				mAlbumArrMost = ((JSONObject)result).optJSONArray("items");
				if(mAlbumArrMost != null && mAlbumArrMost.length() > 0){
					mAlbumArr = new JSONArray();
					for(int i = 0; i < mAlbumArrMost.length(); i++){
						if(i < 4){
							mAlbumArr.put(mAlbumArrMost.optJSONObject(i));
						}
						else{
							break;
						}
					}
				}
				mAlbumAdapter.notifyDataSetChanged();
			}
			break;
		case TaskOrMethod_UserSetProfile:
			removeDialogCustom();
			if(result instanceof JSONObject){
				setEdit(mIsEdit ? false : true);
				String seflIntroduction = ((EditText)findViewById(R.id.introduction_text)).getText().toString().trim();
				String speciality = ((EditText)findViewById(R.id.specialty_text)).getText().toString().trim();
				String declaration = ((EditText)findViewById(R.id.declaration_text)).getText().toString().trim();
				String nickName = ((EditText)findViewById(R.id.nick_name_text)).getText().toString().trim();
				String phone = ((EditText)findViewById(R.id.phone_text)).getText().toString().trim();
				try {
					if(mUserInfo != null){
						mUserInfo.put("selfIntroduction", seflIntroduction);
						mUserInfo.put("speciality", speciality);
						mUserInfo.put("declaration", declaration);
						mUserInfo.put("nickName", nickName);
						mUserInfo.put("phone", phone);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			new AlertDialog.Builder(MyPageActivity.this)
	        .setMessage(R.string.my_homepage_save_success)
	        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
	            @Override
				public void onClick(DialogInterface dialog, int whichButton) {
	            	loadUserData();
	            }
	        }).show();
			break;
			
		case TaskOrMethod_UserUploadPhoto:
			removeDialogCustom();
			if(result instanceof JSONObject && ((JSONObject)result).has("item")){
				try {
					mHeadImageId = ((JSONObject)result).optJSONObject("item").optString("id");
				} catch (Exception e) {
					// TODO: handle exception
				}
				saveUserInfo();
			}
			break;
		default:
			break;
		}
	}

}
