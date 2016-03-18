package com.layout.photoalbum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.layout.photoalbum.PhotoAlbumChildAdapter.TextCallback;
import com.zhuochuang.hsej.R;

public class PhotoAlbumChildActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	// ArrayList<Entity> dataList;
	List<ImageItem> dataList;
	GridView gridView;
	PhotoAlbumChildAdapter adapter;
	AlbumHelper helper;
	Button bt;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(PhotoAlbumChildActivity.this, 
						String.format(getResources().getString(R.string.photoalbum_most_pic), Bimp.imgMaxSize + ""), Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_photoalbum_child);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);

		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				if (Bimp.act_bool) {
					Bimp.act_bool = false;
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < 9) {
						Bimp.drr.add(list.get(i));
					}
				}
				
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});
		
		findViewById(R.id.pic_cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				initView();
			}
		}, 100);
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new PhotoAlbumChildAdapter(PhotoAlbumChildActivity.this, dataList, mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			@Override
			public void onListen(int count) {
				if(count == 0){
					bt.setText(getResources().getString(R.string.photoalbum_done));
					bt.setEnabled(false);
				}
				else{
					bt.setText(getResources().getString(R.string.photoalbum_done) + "(" + count + ")");
					bt.setEnabled(true);
				}
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				adapter.notifyDataSetChanged();
			}
		});
		
		gridView.setSelection(dataList.size() - 1);
	}
}
