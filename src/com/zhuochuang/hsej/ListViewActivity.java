package com.zhuochuang.hsej;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.util.ContentAdapter;

public abstract class ListViewActivity extends BaseActivity{
	
	ListView mListView;
	ContentAdapter mListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.listview);
		init();
	}
	
	public void init(){
		mListView = (ListView) findViewById(R.id.listview);
		getHeaderView();
		getAdapter();
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				OnItemClickListener(parent, view, position, id);
			}
		});
		getFooterView();
	}
	
	public abstract void getHeaderView();
	public abstract void getAdapter();
	public abstract void getFooterView();
	public abstract void OnItemClickListener(AdapterView<?> parent, View view, int position, long id);
}
