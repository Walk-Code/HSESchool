package com.zhuochuang.hsej;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.BottomListDialog;
import com.layout.DeleteDialog;
import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.layout.PullToRefreshListView.OnRemoreListener;
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
/**
 * 相册
 * @author sam
 *modify kris
 */
public class AlbumActivity extends BaseActivity{
	private PullToRefreshListView mListView;
	private ListViewAdapter mListViewAdapter;
	private JSONArray mPhotoArr, mShowPhotoArr;
	private BottomListDialog mBottomListDialog;
	private String mCameraPath;
	private String mFriendId = null;
	private int mPageNo = 1;
	private boolean mIsReadMore = false;
	private PhotoViewer mPhotoViewer;
	private DeleteDialog mDeleteDialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_album);
		
		mFriendId = getIntent().getStringExtra("friendId");
		initView();
	}
	
	protected void initView(){
		((TextView)findViewById(R.id.textview_title)).setText(getString(R.string.album_title));
		
		findViewById(R.id.ico_camera).setVisibility(mFriendId != null ? View.GONE : View.VISIBLE);
		
		mListView = (PullToRefreshListView) findViewById(R.id.pullto_listview);
		mListView.setAdapter(mListViewAdapter = new ListViewAdapter());
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				mPageNo = 1;
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("pageNo", mPageNo);
				params.put("pageSize", 50);
				if(mFriendId != null){
					params.put("otherId", mFriendId);
				}
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserGetPhoto, params, AlbumActivity.this);
			}
		});
		
		mListView.setRemoreable(false);
		mListView.setOnRemoreListener(new OnRemoreListener() {
			
			@Override
			public void onRemore() {
				// TODO Auto-generated method stub
				mPageNo++;
				mIsReadMore = true;
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("pageNo", mPageNo);
				params.put("pageSize", 50);
				if(mFriendId != null){
					params.put("otherId", mFriendId);
				}
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserGetPhoto, params, AlbumActivity.this);
			}
		});
		
		mListView.startRefresh();
	}
	
	public void onCameraClick(View v){
		if(mBottomListDialog == null){
			ArrayList<String> itemText = new ArrayList<String>();
			itemText.add(getString(R.string.album_take_picture));
			itemText.add(getString(R.string.album_select_album));
			mBottomListDialog = new BottomListDialog(AlbumActivity.this,itemText);
			mBottomListDialog.show();
			mBottomListDialog.setItemSelectListener(new BottomListDialog.OnItemSelectListener() {
				@Override
				public void onItemClick(int position) {
					//Intent intent = new Intent(AlbumActivity.this, PublishActivity.class);
					switch (position) {
					case 0://拍照
						File file = CacheHandler.getCameraImgPath(AlbumActivity.this);
						mCameraPath = file.getAbsolutePath();
						Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
						startActivityForResult(intentFromCapture, Configs.REQUESTCODE_CAMERA);
						break;
					case 1://从相册选择
						cleanBimp();
						Intent intent = new Intent(AlbumActivity.this, PhotoAlbumActivity.class);
						startActivityForResult(intent, Configs.REQUESTCODE_IMAGE);
						break;

					default:
						break;
					}
				}
			});
		}else {
			if(!mBottomListDialog.isShowing()){
				mBottomListDialog.show();
			}
		}
		
	}

	 class ListViewAdapter extends ContentAdapter {
		 
		@Override
		public int getCount() {
			return mShowPhotoArr == null ? 0 : mShowPhotoArr.length();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = View.inflate(AlbumActivity.this, R.layout.listcell_adbum, null);
				viewHolder.dateTitle = (TextView) convertView.findViewById(R.id.date_title);
				viewHolder.gridview = (StaticGridView) convertView.findViewById(R.id.gridView);
				viewHolder.adapter = new ItemAdapter();
				viewHolder.gridview.setAdapter(viewHolder.adapter);
				viewHolder.gridview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int index, long arg3) {
						// TODO Auto-generated method stub
						try {
							JSONObject item = mShowPhotoArr.optJSONObject(position).optJSONArray("items").optJSONObject(index);
							if(item != null){
								int pageIndex = 0;
								for(int i = 0; i < mPhotoArr.length(); i++){
									JSONObject obj = mPhotoArr.optJSONObject(i);
									if(obj != null && obj.has("id") && obj.optString("id").equalsIgnoreCase(item.optString("id"))){
										pageIndex = i;
										break;
									}
								}
								mPhotoViewer = new PhotoViewer(AlbumActivity.this, pageIndex);
								mPhotoViewer.setmIsDeleteOp(getIntent().getBooleanExtra("isDeleteOp", false));
								mPhotoViewer.setPathArr(mPhotoArr);
								mPhotoViewer.showPhotoViewer(arg1);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
				convertView.setTag(viewHolder);
			}
			else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			JSONObject item = mShowPhotoArr.optJSONObject(position);
			if(item != null && item.has("items") && item.has("createDate")){
				try {
					String createDateString = Utils.getAlmostTime(AlbumActivity.this, Long.parseLong(item.optString("createDate")));
					
					viewHolder.dateTitle.setText(createDateString.substring(0, 11));
				} catch (Exception e) {
					// TODO: handle exception
				}
				viewHolder.adapter.setData(item.optJSONArray("items"));
				viewHolder.adapter.notifyDataSetChanged();
			}
			
			return convertView;
		}
		
		class ViewHolder{
			StaticGridView gridview;
			ItemAdapter adapter;
			TextView dateTitle;
		}
		
		class ItemAdapter extends ContentAdapter{
			JSONArray array;
			
			public void setData(JSONArray array){
				this.array = array;
			}
			
			@Override
			public int getCount() {
				return array == null ? 0 : array.length();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if(convertView == null){
					FrameLayout layout = new FrameLayout(AlbumActivity.this);
					
					ImageView image = new ImageView(AlbumActivity.this);
					int width = (getResources().getDisplayMetrics().widthPixels - Utils.dipToPixels(AlbumActivity.this, 12)) / 4;
					image.setLayoutParams(new AbsListView.LayoutParams(width, width));
					image.setScaleType(ScaleType.CENTER_CROP);
					layout.addView(image);
					
					ImageView clickView = new ImageView(AlbumActivity.this);
					clickView.setBackgroundColor(R.drawable.masklayer_bg);
					clickView.setLayoutParams(new AbsListView.LayoutParams(width, width));
					//layout.addView(clickView);
					
					convertView = layout;
					convertView.setTag(layout);
				}
				
				JSONObject obj = array.optJSONObject(position);
				
				if(obj != null){
					ImageLoader.getInstance().displayImage(obj.optString("path"), 
							(ImageView)((FrameLayout)convertView.getTag()).getChildAt(0), ImageLoaderConfigs.displayImageOptionsBg);
				}
				
				/*convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});*/
				
				return convertView;
			}
		}
		
	}
	 
	 public void onPostEditClick(View view){
			String pathId = mPhotoViewer.getPathId();
			if(pathId == null || pathId.length() == 0){
				return;
			}
			
			if(mDeleteDialog == null){
				mDeleteDialog = new DeleteDialog(AlbumActivity.this);
				mDeleteDialog.setItemSelectListener(new DeleteDialog.OnItemSelectListener() {
					@Override
					public void onItemClick(int position) {
					switch (position) {
					case R.id.dialog_item1:
						showDialogCustom(DIALOG_CUSTOM);
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("id", mPhotoViewer.getPathId());
						DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserDeletePhoto, params, AlbumActivity.this);
						break;
					case R.id.dialog_item2:
						mDeleteDialog.cancel();
						break;
					}
					}
				});
			}
			mDeleteDialog.show();
	 }
	 
	 private void cleanBimp(){
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
		cleanBimp();
	}

	 private JSONArray dealTiemData(JSONArray arr){
		 JSONArray newArr = new JSONArray();
		 
		 if(arr != null && arr.length() > 0){
			 String lastDay = null;
			 JSONArray tempArr = null;
			 
			 for(int i = 0; i < arr.length(); i++){
				 JSONObject currentObj = arr.optJSONObject(i);
				 if(currentObj != null){
					 boolean sameDay = Utils.isSameDay(this, currentObj.optString("createDate"), lastDay);
					 lastDay = currentObj.optString("createDate");
					 
					 if(!sameDay){
						 tempArr = new JSONArray();
						 JSONObject tempObj = new JSONObject();
						 try {
							 tempObj.put("items", tempArr);
							 tempObj.put("createDate", lastDay);
						} catch (Exception e) {
							// TODO: handle exception
						}
						 newArr.put(tempObj);
					 }
					 tempArr.put(currentObj);
				 }
			 }
		 }
		 return newArr;
	 }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case Configs.REQUESTCODE_IMAGE:
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							String base64 = ""; 
							for(String path : Bimp.drr){
								base64 =  Bimp.getBase64(path);
								if(base64 != null && base64.length() > 0){
									showDialogCustom(DIALOG_CUSTOM);
									HashMap<String, Object> params = new HashMap<String, Object>();
									params.put("imgStr", base64);
									params.put("resourceType", "1");
									DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserUploadPhoto, params, AlbumActivity.this);
									base64 = "";
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							removeDialogCustom();
						}
					}
				}, 150);
				break;
			case Configs.REQUESTCODE_CAMERA:
				try {
					String base64 = Bimp.getBase64(mCameraPath);
					if(base64 != null && base64.length() > 0){
						showDialogCustom(DIALOG_CUSTOM);
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("imgStr", base64);
						params.put("resourceType", "1");
						DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserUploadPhoto, params, AlbumActivity.this);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
	    }
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);

		removeDialogCustom();
		
		if(mListView != null){
			mListView.onRefreshComplete();
		}
	       
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}

		switch (type) {
		case TaskOrMethod_UserGetPhoto:
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				JSONArray temp = ((JSONObject)result).optJSONArray("items");
				if(temp != null && temp.length() > 49){
					mListView.setRemoreable(true);
				}
				else{
					mListView.setRemoreable(false);
				}
				if(mIsReadMore){
					mIsReadMore = false;
					mPhotoArr = DataLoader.getInstance().joinJSONArray(mPhotoArr, temp);
				}
				else{
					mPhotoArr = temp;
				}
				mShowPhotoArr = dealTiemData(mPhotoArr);
				mListViewAdapter.notifyDataSetChanged();
			}
			else{
				mListView.setRemoreable(false);
			}
			break;
		case TaskOrMethod_UserUploadPhoto:
			if(result instanceof JSONObject && ((JSONObject)result).has("item")){
				JSONObject item = ((JSONObject)result).optJSONObject("item");
				mPhotoArr = DataLoader.getInstance().insertStacktop(mPhotoArr, item);///.put(item);
				mShowPhotoArr = dealTiemData(mPhotoArr);
				mListViewAdapter.notifyDataSetChanged();
			}
			break;
			
		case TaskOrMethod_UserDeletePhoto:
			if(mPhotoViewer != null){
				EventManager.getInstance().sendMessage(EventManager.EventType_ReloadAlbum, mPhotoViewer.getCurrentPage());
				Utils.removeIndex(mPhotoArr, mPhotoViewer.getCurrentPage());
				mShowPhotoArr = dealTiemData(mPhotoArr);
				mListViewAdapter.notifyDataSetChanged();
				
				mPhotoViewer.setPathArr(mPhotoArr);
				mPhotoViewer.notifyDataChange();
				
			}
			
			break;
		default:
			break;
		}

	}
}

