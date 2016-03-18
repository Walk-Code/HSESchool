package com.zhuochuang.hsej;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.layout.CustomListCell;
import com.model.ImageLoaderConfigs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;

/**
 * 符合条件的好友
 * @author Dion
 *
 */
public class FriendsSomeActivity extends ListViewActivity {
	
	private JSONArray mDataArray = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitleText(R.string.conform_friends_title);
		try {
			mDataArray = new JSONArray(getIntent().getStringExtra("data"));
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(mListAdapter != null){
			mListAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void getAdapter() {
		mListAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				if(mDataArray != null && mDataArray.length() > 0){
					return mDataArray.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = LayoutInflater.from(FriendsSomeActivity.this).inflate(R.layout.listcell_some_firends, null);
				}
				
				JSONObject obj = mDataArray.optJSONObject(position);
				
				CustomListCell customListCell = (CustomListCell)convertView.findViewById(R.id.search_firends);
				/*customListCell.getSubText().setTextSize(16.f);
				customListCell.getTitleText().setTextSize(16.f);
				customListCell.getSubText().setTextColor(Color.parseColor("#454545"));
				customListCell.getTitleText().setTextColor(Color.parseColor("#454545"));
				LinearLayout.LayoutParams lp = (LayoutParams) customListCell.getSubText().getLayoutParams();
				lp.topMargin = Utils.dipToPixels(FriendsSomeActivity.this, 3);
				customListCell.getSubText().setLayoutParams(lp);
				customListCell.setTitleView(getResources().getString(R.string.search_friend_xm) + obj.optString("xm"));
				customListCell.setSubTitleView(getResources().getString(R.string.search_friend_nickname) + obj.optString("nickName"));*/
				
				customListCell.setTitleView(obj.optString("nickName"));
				customListCell.setSubTitleView("");
				
				ImageLoader.getInstance().displayImage(
						obj.optString("headImage"), 
						customListCell.getImageView(), 
						ImageLoaderConfigs.displayImageOptionsRound);
				
				return convertView;
			}
		};
		
	}
	
	@Override
	public void OnItemClickListener(AdapterView<?> parent, View view,
			int position, long id) {
		
		if(mDataArray != null && mDataArray.length() > 0 ){
			JSONObject obj = mDataArray.optJSONObject(position);
			if(obj != null ){
				Intent intent = new Intent(FriendsSomeActivity.this, PersonPageActivity.class);
				intent.putExtra("id", obj.optString("id"));
				startActivity(intent);
			}
		}
	}


	@Override
	public void getHeaderView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void getFooterView() {
		// TODO Auto-generated method stub
	}
}
