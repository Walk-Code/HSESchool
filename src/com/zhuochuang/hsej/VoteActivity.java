package com.zhuochuang.hsej;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.photoview.PhotoViewer;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.ContentAdapter;
import com.util.Utils;

public class VoteActivity extends BaseActivity{

	ListView mListView;
	View mHeaderView;
	ContentAdapter mAdapter;
	JSONObject mVoteObj;
	JSONArray mVoteList;
	ArrayList<String> mIdList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_vote);
		mMainLayout.setBackgroundColor(Color.WHITE);
		setTitleMarginRight(Utils.dipToPixels(VoteActivity.this, 65));
		
		mHeaderView = View.inflate(VoteActivity.this, R.layout.listcell_vote_header, null);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.addHeaderView(mHeaderView, null, false);
		mListView.setAdapter(mAdapter = new ContentAdapter(){

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
					convertView = View.inflate(VoteActivity.this, R.layout.listcell_vote, null);
					LinearLayout.LayoutParams lp = (LayoutParams) convertView.findViewById(R.id.imageview).getLayoutParams();
					lp.height = Utils.realVoteListImageHeight(VoteActivity.this);
					convertView.findViewById(R.id.imageview).setLayoutParams(lp);
				}
				
				JSONObject obj = mVoteList.optJSONObject(position);
				if(obj != null){
					String imageUrl = obj.optString("imageUrl");
					if(Utils.isTextEmpty(imageUrl)){
						convertView.findViewById(R.id.imageview).setVisibility(View.GONE);
					}
					else{
						convertView.findViewById(R.id.imageview).setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(obj.optString("imageUrl"), (ImageView)convertView.findViewById(R.id.imageview), ImageLoaderConfigs.displayImageOptionsBg);
					}
					convertView.findViewById(R.id.imageview).setOnClickListener(new onImageClick(obj.optString("imageUrl")));
					String content = obj.optString("content");
					if(Utils.isTextEmpty(content)){
						convertView.findViewById(R.id.textview_declare).setVisibility(View.GONE);
					}
					else{
						convertView.findViewById(R.id.textview_declare).setVisibility(View.VISIBLE);
						((TextView)convertView.findViewById(R.id.textview_declare)).setText(content);
					}
					
					((TextView)convertView.findViewById(R.id.textview_name)).setText((position + 1) + ": " + obj.optString("titleName"));
					
					
					switch (getIntent().getIntExtra("favoriteStatus", 0)) {
					case 6:
						convertView.findViewById(R.id.group_select).setVisibility(View.GONE);
						convertView.findViewById(R.id.group_roundprogress).setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.textview_commit_percent).setVisibility(View.VISIBLE);
						int almostNumber = mVoteObj.optInt("voteNumber", 0);
						if(almostNumber == 0){
							almostNumber = 100;
						}
						int number = obj.optInt("number", 0);
						
						/*RoundCornerProgressBar progressBar = (RoundCornerProgressBar) convertView.findViewById(R.id.view_roundprogress);
						progressBar.setBackgroundColor(Color.TRANSPARENT);
						progressBar.setProgressColor(Color.parseColor("#dc5151"));
						progressBar.setMax(almostNumber);
						progressBar.setProgress(number);
						//progressBar.postInvalidate();
*/						
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
						
						
						((TextView)convertView.findViewById(R.id.textview_commit_percent)).setText(
								getResources().getString(R.string.vote_commit_num) + obj.optString("percent"));
						break;
					default:
						convertView.findViewById(R.id.group_select).setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.group_select).setOnClickListener(new OnCheckClick(obj));
						convertView.findViewById(R.id.group_roundprogress).setVisibility(View.GONE);
						convertView.findViewById(R.id.textview_commit_percent).setVisibility(View.GONE);
						
						if(mIdList.contains(obj.optString("id"))){
							convertView.findViewById(R.id.ico_select).setBackgroundResource(R.drawable.list_option_tick);
						}
						else{
							convertView.findViewById(R.id.ico_select).setBackgroundResource(R.drawable.list_option_no);
						}
						break;
					}
				}
				return convertView;
			}
		});
		
		showDialogCustom(DIALOG_CUSTOM);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_VoteGetVoteTopic, 
				DataLoader.getInstance().getVoteGetVoteTopicParams(getIntent().getIntExtra("resouceType", 4), 
						getIntent().getStringExtra("id")), VoteActivity.this);
	}
	
	private void setHeaderData(){
		if(mVoteObj == null){
			return;
		}
		setTitleText(mVoteObj.optString("titleName"));
		
		LinearLayout.LayoutParams lp = (LayoutParams) mHeaderView.findViewById(R.id.imageview).getLayoutParams();
		lp.height = Utils.realVoteImageHeight(VoteActivity.this);
		mHeaderView.findViewById(R.id.imageview).setLayoutParams(lp);
		
		String imageUrl = mVoteObj.optString("imageUrl");
		if(Utils.isTextEmpty(imageUrl)){
			mHeaderView.findViewById(R.id.imageview).setVisibility(View.GONE);
		}
		else{
			mHeaderView.findViewById(R.id.imageview).setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(mVoteObj.optString("imageUrl"), (ImageView)mHeaderView.findViewById(R.id.imageview), ImageLoaderConfigs.displayImageOptionsBg);
		}
		
		((TextView)mHeaderView.findViewById(R.id.textview_content)).setText(mVoteObj.optString("content"));
		
		switch (getIntent().getIntExtra("favoriteStatus", 0)) {
		case 6:
			findViewById(R.id.textview_checkvote).setVisibility(View.VISIBLE);
			findViewById(R.id.group_bottom).setVisibility(View.GONE);
			mHeaderView.findViewById(R.id.textview_num).setVisibility(View.VISIBLE);
			((TextView)mHeaderView.findViewById(R.id.textview_num)).setText(String.format(getResources().getString(R.string.vote_join_num), mVoteObj.optString("voteNumber")));
			break;

		default:
			findViewById(R.id.textview_checkvote).setVisibility(View.GONE);
			mHeaderView.findViewById(R.id.textview_num).setVisibility(View.GONE);
			findViewById(R.id.group_bottom).setVisibility(View.VISIBLE);
			break;
		}
		
		if(mVoteObj.has("isEnd") && !mVoteObj.optBoolean("isEnd", false) && !mVoteObj.optBoolean("starting", false)){
			findViewById(R.id.textview_commit).setEnabled(false);
			((TextView)findViewById(R.id.textview_commit)).setText(getResources().getString(R.string.vote_unstart));
		}
		else{
			findViewById(R.id.textview_commit).setEnabled(true);
			((TextView)findViewById(R.id.textview_commit)).setText(getResources().getString(R.string.vote_commit));
		}
	}
	
	class OnCheckClick implements OnClickListener{

		JSONObject obj;
		public OnCheckClick(JSONObject obj){
			this.obj = obj;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mVoteObj == null || obj == null){
				return;
			}
			if(mIdList.contains(obj.optString("id"))){
				mIdList.remove(obj.optString("id"));
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
				return;
			}
			if(mIdList.size() < mVoteObj.optInt("maxNumber", 0)){
				mIdList.add(obj.optString("id"));
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
			}
			else{
				Toast.makeText(VoteActivity.this, String.format(getResources().getString(R.string.survey_option_max), mVoteObj.optInt("maxNumber", 0) +""), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	class onImageClick implements OnClickListener{

		String url;
		public onImageClick(String url){
			this.url = url;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			PhotoViewer photoViewer = new PhotoViewer(VoteActivity.this, 0);
			photoViewer.setSinglePicUrl(url);
			photoViewer.showPhotoViewer(v);
		}
	}
	
	public void onVoteResultClick(View view){
		if(mVoteObj == null){
			return;
		}
		Intent intent = new Intent(VoteActivity.this, VoteResultActivity.class);
		intent.putExtra("data", mVoteObj.toString());
		startActivity(intent);
	}
	
	public void onCommitBtnClick(View view){
		if(mVoteObj == null){
			return;
		}
		if(mIdList.size() < mVoteObj.optInt("minNumber", 1)){
			Toast.makeText(VoteActivity.this, String.format(getResources().getString(R.string.vote_commit_null), mVoteObj.optInt("minNumber", 1) +""), Toast.LENGTH_SHORT).show();
			return;
		}
		String value = "";
		for(String id : mIdList){
			value += id + ",";
		}
		if(!Utils.isTextEmpty(value) && value.contains(",")){
			value = value.substring(0, value.length() - 1);
		}
		showDialogCustom(DIALOG_CUSTOM);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_VoteVoteSubmit, 
				DataLoader.getInstance().getVoteVoteSubmitParams(mVoteObj.optString("id"), value), VoteActivity.this);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		super.taskFinished(type, result, isHistory);
		removeDialogCustom();
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(VoteActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_VoteGetVoteTopic:
			if(result instanceof JSONObject && ((JSONObject)result).has("item")){
				mVoteObj = ((JSONObject)result).optJSONObject("item");
				if(mVoteObj != null && mVoteObj.has("isEnd") && mVoteObj.optBoolean("isEnd", false)){
					getIntent().putExtra("favoriteStatus", 6);
				}
				setHeaderData();
				if(mVoteObj.has("items")){
					mVoteList = mVoteObj.optJSONArray("items");
				}
			}
			if(mAdapter != null){
				mAdapter.notifyDataSetChanged();
			}
			break;
		case TaskOrMethod_VoteVoteSubmit:
			Toast.makeText(VoteActivity.this, getResources().getString(R.string.vote_commit_success), Toast.LENGTH_SHORT).show();
			EventManager.getInstance().sendMessage(EventManager.EventType_VoteChange, getIntent().getStringExtra("id"));
			getIntent().putExtra("favoriteStatus", 6);
			if(result instanceof JSONObject && ((JSONObject)result).has("item")){
				mVoteObj = ((JSONObject)result).optJSONObject("item");
				setHeaderData();
				if(mVoteObj.has("items")){
					mVoteList = mVoteObj.optJSONArray("items");
				}
			}
			if(mAdapter != null){
				mAdapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
	}

}
