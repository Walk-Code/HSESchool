package com.zhuochuang.hsej.store;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.PhotoAlbumDialog;
import com.layout.RatingBarView;
import com.layout.emoji.EmojiUtils;
import com.layout.photoalbum.Bimp;
import com.layout.photoalbum.FileUtils;
import com.layout.photoalbum.PhotoAlbumActivity;
import com.layout.photoview.PhotoViewer;
import com.model.CacheHandler;
import com.model.Configs;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.StaticGridView;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.PublishActivity;
import com.zhuochuang.hsej.R;

/**
 * modify kris
 * @author Administrator
 *
 */
public class StorePublishEvaluateActivity extends BaseActivity{
	private PhotoAlbumDialog mPhotoAlbumDialog;
	private String mCameraPath;
	private StaticGridView mStaticGridView;
	private GridAdapter mAdapter;
	private RatingBarView mBarView; 
	private int mStarNuml;
	private int mUploadIndex = 0;
	
	ArrayList<String> mUpLoadPathList = new ArrayList<String>();
	Handler mHandler;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		setContentView(R.layout.store_publish_evaluate);
		setTitleText(getResources().getString(R.string.stores_publish_evaluate));	
		
		((TextView)findViewById(R.id.tv_details)).setText(getIntent().getStringExtra("goodsName"));
		ImageLoader.getInstance().displayImage(getIntent().getStringExtra("img"), (ImageView)findViewById(R.id.good_image),ImageLoaderConfigs.displayImageOptionsBg);
		mBarView = (RatingBarView) findViewById(R.id.custom_ratingbar);
		mBarView.setOnRatingListener(new RatingBarView.OnRatingListener() {
	            @Override
	            public void onRating(Object bindObject, int RatingScore) {
	            	mStarNuml = RatingScore;
	            }
	    });
		
		findViewById(R.id.commit_evaluate).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mStarNuml == 0){
					Toast.makeText(StorePublishEvaluateActivity.this, R.string.stores_goods_evaluate_mes_grade, Toast.LENGTH_SHORT).show();
					return;
				}
				String content = ((EditText)findViewById(R.id.goods_publish_edit)).getText().toString();
				if(Utils.isTextEmpty(content)){
					Toast.makeText(StorePublishEvaluateActivity.this, R.string.stores_goods_evaluate_mes_review, Toast.LENGTH_SHORT).show();
					return;
				}
				showDialogCustom(DIALOG_CUSTOM);
				if(Bimp.base64Arr != null && Bimp.base64Arr.size() > 0){
					startUpload();
				}
				else{
					startSend();
				}
			}
		});
		initStaticGridView();
	}
	
	private void startSend(){
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_OrdersEvaluateOrders,
				DataLoader.getInstance().setOrdersEvaluateOrdersTypeParams(getIntent().getStringExtra("goodId"),
						((EditText)findViewById(R.id.goods_publish_edit)).getText().toString(), getImgIds(), mStarNuml), 
				StorePublishEvaluateActivity.this);
	}
	
	private void startUpload(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("imgStr", getPublishPath());
		params.put("resourceType", "6");
		mUploadIndex++;
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserUploadPhoto, params, StorePublishEvaluateActivity.this);
	}
	
	private String getPublishPath(){
		if(mUpLoadPathList == null){
			mUpLoadPathList = new ArrayList<String>();
		}
		return Bimp.base64Arr.get(mUploadIndex);
	}
	
	private void initStaticGridView(){
		mStaticGridView = (StaticGridView) findViewById(R.id.gridpic);
		mAdapter = new GridAdapter(this);
		mAdapter.update();
		mStaticGridView.setAdapter(mAdapter);
		mStaticGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				// TODO Auto-generated method stub
				Bimp.imgMaxSize = 8;
				if(Bimp.bmp.size() == Bimp.imgMaxSize){
					HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
					for(int i = 0; i < Bimp.bmp.size(); i++){
						map.put(i, false);
					}
					PhotoViewer viewer = new PhotoViewer(StorePublishEvaluateActivity.this, arg2);
					viewer.setInvertSelectionMap(map);
					viewer.showPhotoViewer(arg1);
				}
				else{
					if (arg2 == Bimp.bmp.size()) {
						if(arg2 == Bimp.imgMaxSize){
							Toast.makeText(StorePublishEvaluateActivity.this, 
									String.format(getResources().getString(R.string.photoalbum_most_pic), Bimp.imgMaxSize + ""), Toast.LENGTH_SHORT).show();
						}
						else{
							showImgDialog();
						}
					}
					else{
						HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
						for(int i = 0; i < Bimp.bmp.size(); i++){
							map.put(i, false);
						}
						PhotoViewer viewer = new PhotoViewer(StorePublishEvaluateActivity.this, arg2);
						viewer.setInvertSelectionMap(map);
						viewer.showPhotoViewer(arg1);
					}
				}
				
				Utils.removeSoftKeyboard(StorePublishEvaluateActivity.this);
			}
		});
		
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Object[] objs = (Object[]) msg.obj;
				switch (msg.what) {
				case EventManager.EventType_Photoalbum:
					try {
					if(objs != null && objs[0] != null && objs[0] instanceof HashMap<?,?>){
						HashMap<Integer, Boolean> map = (HashMap<Integer, Boolean>) objs[0];
						if(map == null || map.size() == 0){
							return;
						}
						List<Bitmap> bmp = new ArrayList<Bitmap>();
						List<String> drr = new ArrayList<String>();
						ArrayList<String> base64 = new ArrayList<String>();
						int tag = 0, tagF = 0;
						for(int i = 0; i < Bimp.bmp.size(); i++){
							if(!map.get(i)){
								tagF++;
								bmp.add(Bimp.bmp.get(i));
								drr.add(Bimp.drr.get(i));
								base64.add(Bimp.base64Arr.get(i));
							}
							else{
								tag++;
							}
						}
						
						if(tag == Bimp.bmp.size()){
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
							mAdapter.update();
						}
						else if(tagF == Bimp.bmp.size()){
							
						}
						else{
							Bimp.bmp = bmp;
							Bimp.drr = drr;
							Bimp.base64Arr = base64;
							Bimp.max = Bimp.bmp.size();
							mAdapter.update();
						}
					}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					break;
				}
			}
		});
	}
	
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private int screenWidth;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		@SuppressWarnings("deprecation")
		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		}

		public void update() {
			loading();
		}

		@Override
		public int getCount() {
			if(Bimp.bmp.size() == Bimp.imgMaxSize){
				return Bimp.bmp.size();
			}
			return (Bimp.bmp.size() + 1);
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.photoalbumitem_published_grid, parent, false);
				int width = (screenWidth - Utils.dipToPixels(StorePublishEvaluateActivity.this, 50)) / 4;
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(width, width);
				convertView.setLayoutParams(lp);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(Bimp.bmp.size() == Bimp.imgMaxSize){
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}
			else{
				if (position == Bimp.bmp.size()) {
					holder.image.setImageBitmap(null);
					holder.image.setBackgroundResource(R.drawable.icon_camera_add);
				}
				else {
					holder.image.setImageBitmap(Bimp.bmp.get(position));
				}
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					mAdapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			// Bimp.base64Arr.clear();
			new Thread(new Runnable() {
			@Override
			public void run() {
			while (true) {
				if (Bimp.max == Bimp.drr.size()) {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
					break;
				}
				else {
					try {
						String path = Bimp.drr.get(Bimp.max);
						Bitmap bm = Bimp.revitionImageSize(path, path.equalsIgnoreCase(mCameraPath));
						Bimp.bmp.add(bm);
						String newStr = path.substring(path.lastIndexOf("/") + 1,path.lastIndexOf("."));
						FileUtils.saveBitmap(bm, "" + newStr);
						Bimp.max += 1;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			}
			}).start();
		}
	}
	
	private void showImgDialog(){
		if(mPhotoAlbumDialog == null){
			mPhotoAlbumDialog = new PhotoAlbumDialog(StorePublishEvaluateActivity.this);
			mPhotoAlbumDialog.setItemSelectListener(new PhotoAlbumDialog.OnItemSelectListener() {
				@Override
				public void onItemClick(int position) {
				switch (position) {
				case R.id.dialog_item1:
					File file = CacheHandler.getCameraImgPath(StorePublishEvaluateActivity.this);
					mCameraPath = file.getAbsolutePath();
					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					startActivityForResult(intentFromCapture, Configs.REQUESTCODE_CAMERA);
					break;
				case R.id.dialog_item2:
					Intent intent = new Intent(StorePublishEvaluateActivity.this, PhotoAlbumActivity.class);
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
	
	public void headerImageClick(View view){
		if(mPhotoAlbumDialog == null){
			mPhotoAlbumDialog = new PhotoAlbumDialog(StorePublishEvaluateActivity.this);
			mPhotoAlbumDialog.setItemSelectListener(new PhotoAlbumDialog.OnItemSelectListener() {
				@Override
				public void onItemClick(int position) {
				switch (position) {
				case R.id.dialog_item1:
					File file = CacheHandler.getCameraImgPath(StorePublishEvaluateActivity.this);
					mCameraPath = file.getAbsolutePath();
					mAdapter.notifyDataSetChanged();
					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					startActivityForResult(intentFromCapture, Configs.REQUESTCODE_CAMERA);
					break;
				case R.id.dialog_item2:
					clearBimp();
					Bimp.imgMaxSize = 8;
					Intent intent = new Intent(StorePublishEvaluateActivity.this, PhotoAlbumActivity.class);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case Configs.REQUESTCODE_IMAGE:
				mAdapter.update();
				break;
			case Configs.REQUESTCODE_CAMERA:
				if (Bimp.drr.size() < 9 && resultCode == -1) {
					Bimp.drr.add(mCameraPath);
				}
				mAdapter.update();
				break;
			}
	    }
	}
	
	private String getImgIds(){
		if(mUpLoadPathList == null || mUpLoadPathList.size() == 0){
			return "";
		}
		
		String imgIds = "";
		for(String img : mUpLoadPathList){
			imgIds += img + ",";
		}
		
		if(imgIds.contains(",")){
			imgIds = imgIds.substring(0, imgIds.length() - 1);
		}
		
		return imgIds;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearBimp();
		EventManager.getInstance().removeHandlerListenner(mHandler);
		((HSESchoolApp)getApplication()).removeActivityStore(this);
	}
	
	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		if(result == null){
			removeDialogCustom();
			return;
		}
		
		if(result instanceof Error){
			removeDialogCustom();
			if(type == TaskType.TaskOrMethod_UserUploadPhoto){
				new AlertDialog.Builder(StorePublishEvaluateActivity.this)
		        .setMessage(R.string.publish_pic_fail)
		        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	mUpLoadPathList.clear();
		            	mUploadIndex = 0;
		            }
		        })
		        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
		            @Override
					public void onClick(DialogInterface dialog, int whichButton) {
		            	showDialogCustom(DIALOG_CUSTOM);
		            	mUploadIndex--;
		            	startUpload();
		            }
		        }).show();
			}
			else{
				mUpLoadPathList.clear();
            	mUploadIndex = 0;
				Toast.makeText(StorePublishEvaluateActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			}
			return;
		}
		
		switch (type) {
		case TaskOrMethod_UserUploadPhoto:
			if(result instanceof JSONObject && ((JSONObject)result).has("item")){
				JSONObject item = ((JSONObject)result).optJSONObject("item");
				if(mUpLoadPathList == null){
					mUpLoadPathList = new ArrayList<String>();
				}
				mUpLoadPathList.add(item.optString("id"));
				if(mUploadIndex == Bimp.base64Arr.size()){
					startSend();
				}
				else{
					startUpload();
				}
			}
			break;
		case TaskOrMethod_OrdersEvaluateOrders:
			removeDialogCustom();
			EventManager.getInstance().sendMessage(EventManager.EventType_EvaluateFinish, new Object());
			Toast.makeText(StorePublishEvaluateActivity.this,getResources().getString(R.string.vote_commit_success), Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					setResult(Activity.RESULT_OK);
					StorePublishEvaluateActivity.this.finish();
				}
			}, 1000);
			break;
		default:
			break;
		}
	}
}
