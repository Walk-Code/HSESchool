package com.zhuochuang.hsej;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.layout.PhotoAlbumDialog;
import com.layout.emoji.EmojiFragment;
import com.layout.emoji.EmojiUtils;
import com.layout.photoalbum.Bimp;
import com.layout.photoalbum.FileUtils;
import com.layout.photoalbum.PhotoAlbumActivity;
import com.layout.photoview.PhotoViewer;
import com.model.CacheHandler;
import com.model.Configs;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskType;
import com.util.StaticGridView;
import com.util.Utils;

@SuppressLint({ "HandlerLeak", "UseSparseArrays" })
public class PublishActivity extends BaseActivity{

	private StaticGridView mStaticGridView;
	private GridAdapter mAdapter;
	private PhotoAlbumDialog mPhotoAlbumDialog;
	private Handler mHandler;
	
	private String mCameraPath;
	private String mHobbyGroupId;
	private String mUpLoadPath;
	private ArrayList<String> mUpLoadPathList = new ArrayList<String>();
	private int mUploadIndex = 0;
	private boolean isNeedEmoji = false;
	
	private View mEmojiView;
	private LinearLayout mEmojiLayout;
	private RelativeLayout mEmojiRelayout;
	private EmojiFragment mEmojiFragment;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_publish);
		setTitleText(R.string.hobbygroup_publish);
		findViewById(R.id.textview_publish).setVisibility(View.VISIBLE);
		
		mHobbyGroupId = getIntent().getStringExtra("hobbyGroupId");
		
		initStaticGridView();
		initializeEmojiView();
	}
	
	private void initializeEmojiView(){
		mEmojiView = findViewById(R.id.public_emojiview);
		mEmojiLayout = (LinearLayout) findViewById(R.id.public_emoji_layout);
		mEmojiRelayout = (RelativeLayout) findViewById(R.id.public_emojiview_relayout);
		mEmojiView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mEmojiLayout.isShown()){
					mEmojiView.setBackgroundResource(R.drawable.btn_expression);
					mEmojiLayout.setVisibility(View.GONE);
					InputMethodManager inputManager = (InputMethodManager) ((EditText) findViewById(R.id.edittext_content)).getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
				    inputManager.showSoftInput(findViewById(R.id.edittext_content), 0);
				}
				else{
					((EditText) findViewById(R.id.edittext_content)).requestFocus();
					mEmojiView.setBackgroundResource(R.drawable.btn_keyboard);
					Utils.removeSoftKeyboard(PublishActivity.this);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mEmojiLayout.setVisibility(View.VISIBLE);
						}
					}, 50);
				}
			}
		});
		
		findViewById(R.id.edittext_content).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(mEmojiLayout.isShown()){
					mEmojiLayout.setVisibility(View.GONE);
					mEmojiView.setBackgroundResource(R.drawable.btn_expression);
				}
				return false;
			}
		});
		
		findViewById(R.id.edittext_content).setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				isNeedEmoji = hasFocus;
				mEmojiLayout.setVisibility(View.GONE);
				mEmojiView.setBackgroundResource(R.drawable.btn_expression);
				if(hasFocus){
					mEmojiRelayout.setVisibility(View.VISIBLE);
				}
			}
		});
		
		mEmojiFragment = (EmojiFragment) getSupportFragmentManager().findFragmentById(R.id.emoji_fragment);
		mEmojiFragment.setEditTextHolder((EditText) findViewById(R.id.edittext_content));
		
		mMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
		    @Override
		    public void onGlobalLayout(){
	    	if(mEmojiRelayout != null){
	    		mEmojiRelayout.setVisibility(View.GONE);
	    	}
		    if(!isNeedEmoji) return;
	        int heightDiff = mMainLayout.getRootView().getHeight()- mMainLayout.getHeight();
	        if(heightDiff >100){
	        	mEmojiRelayout.setVisibility(View.VISIBLE);
	        }
	        else{
	        	if(mEmojiLayout.isShown()){
	        		mEmojiRelayout.setVisibility(View.VISIBLE);
	        	}
	        	else{
	        		mEmojiRelayout.setVisibility(View.GONE);
	        	}
	       }
	     }
		});
	}
	
	public void onPublishClick(View view){
		String title = ((EditText)findViewById(R.id.edittext_title)).getText().toString();
		if(title == null || title.length() == 0 || title.replaceAll(" ", "").length() == 0){
			Toast.makeText(PublishActivity.this, getResources().getString(R.string.publish_title_null), Toast.LENGTH_SHORT).show();
			return;
		}
		
		String content = ((EditText)findViewById(R.id.edittext_content)).getText().toString();
		if(content == null || content.length() == 0 || content.replaceAll(" ", "").length() == 0){
			Toast.makeText(PublishActivity.this, getResources().getString(R.string.publish_content_null), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(title.length() > 50){
			Toast.makeText(PublishActivity.this, getResources().getString(R.string.publish_title_max), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(content.length() > 2500){
			Toast.makeText(PublishActivity.this, getResources().getString(R.string.publish_content_max), Toast.LENGTH_SHORT).show();
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
	
	private void startSend(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("hobbyGroupId", mHobbyGroupId);
		params.put("title", ((EditText)findViewById(R.id.edittext_title)).getText().toString());
		//params.put("content", ((EditText)findViewById(R.id.edittext_content)).getText().toString());
		params.put("content", EmojiUtils.convertToUnicode(((EditText)findViewById(R.id.edittext_content)).getText().toString()));
		params.put("imgIds", getImgIds());
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_HobbygroupSendPost, params, PublishActivity.this);
	}
	
	private void startUpload(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("imgStr", getPublishPath());
		params.put("resourceType", "9");
		mUploadIndex++;
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserUploadPhoto, params, PublishActivity.this);
	}
	
	private String getPublishPath(){
		if(mUpLoadPathList == null){
			mUpLoadPathList = new ArrayList<String>();
		}
		return Bimp.base64Arr.get(mUploadIndex);
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
	
	private void initStaticGridView(){
		mStaticGridView = (StaticGridView) findViewById(R.id.public_gridView);
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
					PhotoViewer viewer = new PhotoViewer(PublishActivity.this, arg2);
					viewer.setInvertSelectionMap(map);
					viewer.showPhotoViewer(arg1);
				}
				else{
					if (arg2 == Bimp.bmp.size()) {
						if(arg2 == Bimp.imgMaxSize){
							Toast.makeText(PublishActivity.this, 
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
						PhotoViewer viewer = new PhotoViewer(PublishActivity.this, arg2);
						viewer.setInvertSelectionMap(map);
						viewer.showPhotoViewer(arg1);
					}
				}
				
				Utils.removeSoftKeyboard(PublishActivity.this);
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
				int width = (screenWidth - Utils.dipToPixels(PublishActivity.this, 50)) / 4;
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
					holder.image.setBackgroundResource(R.drawable.btn_addpicture);
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
			mPhotoAlbumDialog = new PhotoAlbumDialog(PublishActivity.this);
			mPhotoAlbumDialog.setItemSelectListener(new PhotoAlbumDialog.OnItemSelectListener() {
				@Override
				public void onItemClick(int position) {
				switch (position) {
				case R.id.dialog_item1:
					File file = CacheHandler.getCameraImgPath(PublishActivity.this);
					mCameraPath = file.getAbsolutePath();
					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					startActivityForResult(intentFromCapture, Configs.REQUESTCODE_CAMERA);
					break;
				case R.id.dialog_item2:
					Intent intent = new Intent(PublishActivity.this, PhotoAlbumActivity.class);
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
		// TODO Auto-generated method stub
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
			if(type == TaskType.TaskOrMethod_UserUploadPhoto){
				new AlertDialog.Builder(PublishActivity.this)
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
				Toast.makeText(PublishActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			}
			return;
		}
		
		switch (type) {
		case TaskOrMethod_HobbygroupSendPost:
			removeDialogCustom();
			if(result instanceof JSONObject){
				Utils.removeSoftKeyboard(PublishActivity.this);
				Toast.makeText(PublishActivity.this, getResources().getString(R.string.publish_success), Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setResult(Activity.RESULT_OK);
						PublishActivity.this.finish();
					}
				}, 800);
			}
			break;
			
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
		default:
			break;
		}
	}
}
