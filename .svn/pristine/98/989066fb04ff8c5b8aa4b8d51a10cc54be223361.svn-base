package com.zhuochuang.hsej;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.util.ContentAdapter;

public class VoteResultActivity extends ListViewActivity{

	View mHeaderView;
	JSONObject mVoteObj;
	JSONArray mVoteList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void getHeaderView() {
		// TODO Auto-generated method stub
		setTitleText(R.string.vote_checkresult);
		mMainLayout.setBackgroundColor(Color.WHITE);
		
		mHeaderView = View.inflate(VoteResultActivity.this, R.layout.listcell_vote_header, null);
		mListView.addHeaderView(mHeaderView, null, false);
		mHeaderView.findViewById(R.id.imageview).setVisibility(View.GONE);
		mHeaderView.findViewById(R.id.textview_content).setVisibility(View.GONE);
		mHeaderView.findViewById(R.id.textview_num).setVisibility(View.VISIBLE);
		
		try {
			mVoteObj = new JSONObject(getIntent().getStringExtra("data"));
			if(mVoteObj.has("items")){
				mVoteList = mVoteObj.optJSONArray("items");
			}
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(mVoteObj == null){
			return;
		}
		((TextView)mHeaderView.findViewById(R.id.textview_num)).setText(String.format(getResources().getString(R.string.vote_join_num), mVoteObj.optString("voteNumber")));
	
		if(mListAdapter != null){
			mListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void getAdapter() {
		// TODO Auto-generated method stub
		mListAdapter = new ContentAdapter(){

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(mVoteList != null && mVoteList.length() > 0){
					return mVoteList.length();
				}
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if(convertView == null){
					convertView = View.inflate(VoteResultActivity.this, R.layout.listcell_vote, null);
				}
				
				JSONObject obj = mVoteList.optJSONObject(position);
				if(obj != null){
					((TextView)convertView.findViewById(R.id.textview_name)).setText((position + 1) + ": " + obj.optString("titleName"));
					
					convertView.findViewById(R.id.textview_declare).setVisibility(View.GONE);
					convertView.findViewById(R.id.group_select).setVisibility(View.GONE);
					convertView.findViewById(R.id.group_roundprogress).setVisibility(View.VISIBLE);
					convertView.findViewById(R.id.textview_commit_percent).setVisibility(View.VISIBLE);
					
					int almostNumber = mVoteObj.optInt("voteNumber", 0);
					if(almostNumber == 0){
						almostNumber = 100;
					}
					int number = obj.optInt("number", 0);
					SeekBar seekbar = (SeekBar) convertView.findViewById(R.id.seekbar);
					seekbar.setMax(almostNumber);
					seekbar.setProgress(number);
					seekbar.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View arg0, MotionEvent arg1) {
							// TODO Auto-generated method stub
							return true;
						}
					});
					
					/*RoundCornerProgressBar progressBar = (RoundCornerProgressBar) convertView.findViewById(R.id.view_roundprogress);
					progressBar.setBackgroundColor(Color.TRANSPARENT);
					progressBar.setProgressColor(Color.parseColor("#dc5151"));
					progressBar.setMax(mVoteObj.optInt("voteNumber", 0));
					progressBar.setProgress(obj.optInt("number", 0));*/
			//		progressBar.postInvalidate();
					//((TextView)convertView.findViewById(R.id.textview_commit_percent)).setText(getResources().getString(R.string.vote_commit_num)
					//		+ (position * 20) + getResources().getString(R.string.vote_perecnt));
					((TextView)convertView.findViewById(R.id.textview_commit_percent)).setText(
							getResources().getString(R.string.vote_commit_num) + obj.optString("percent"));
				}
				return convertView;
			}
		};
	}

	@Override
	public void getFooterView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnItemClickListener(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}

}
