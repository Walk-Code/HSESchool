package com.layout;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.model.ImageLoaderConfigs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.Utils;
import com.zhuochuang.hsej.R;

public class ListViewCell {
	
	private Context mContext;
	private View mViewGroup;
	private int mCurrentStyle;
	private int mCellType;
	private JSONArray mJSArr;
	private JSONObject mJSObj;
	
	public static final int FragmentSchoolyard = 100001, MyMessage = 100002;
	
	public ListViewCell(Context context, int style){
		mContext = context;
		mCurrentStyle = style;
		
		switch (style) {
		case FragmentSchoolyard:
			mViewGroup = View.inflate(mContext, R.layout.listcell_myschoolyard, null);
			break;
		case MyMessage:
			mViewGroup = View.inflate(mContext, R.layout.listcell_my_message, null);
			break;
		default:
				break;
		}
		
		mViewGroup.setTag(this);
	}

	public View getView(){
		return mViewGroup;
	}
	
	public void setData(Object... object){
		
		switch (mCurrentStyle) {
		case FragmentSchoolyard:
			
			break;
		case MyMessage:
            mJSObj = (JSONObject) object[1];
			
			if(mJSObj != null){
				ImageView userImage = (ImageView) mViewGroup.findViewById(R.id.user_image);
				RelativeLayout interactionLayout = (RelativeLayout)mViewGroup.findViewById(R.id.interaction_layout);
				TextView userName = (TextView) mViewGroup.findViewById(R.id.user);
				TextView oprationType = (TextView) mViewGroup.findViewById(R.id.opration_type);
				
				interactionLayout.setVisibility(View.GONE);
				mViewGroup.findViewById(R.id.content).setVisibility(View.GONE);
				mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.GONE);
				
				((TextView) mViewGroup.findViewById(R.id.red_point)).setVisibility(mJSObj.optString("status").equalsIgnoreCase("10") ? View.GONE : View.VISIBLE);
				((TextView) mViewGroup.findViewById(R.id.date)).setText(Utils.friendlyTime(mContext, mJSObj.optLong("createDate")));
				
				final int msgType = Integer.parseInt(mJSObj.optString("fromResource"));
				
				userName.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (msgType) {
						case 11:
						default:
							break;
						}
					}
				});
				mViewGroup.findViewById(R.id.layout_head).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (msgType) {
						case 11:
							break;

						default:
							break;
						}
					}
				});
				
				switch (msgType) {
				case 0://系统
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					break;
				case 1://1用户
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					break;
				case 2://社团
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					
					break;
				case 3://活动
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					break;
				case 4://资讯
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					break;
				case 5://服务
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					break;
				case 6://评论
					ImageLoader.getInstance().displayImage("http://cdnq.duitang.com/uploads/item/201505/22/20150522173721_sMHBi.thumb.700_0.jpeg", 
							userImage, ImageLoaderConfigs.displayImageOptionsRound);
					
				    interactionLayout.setVisibility(View.VISIBLE);
				    JSONObject res = mJSObj.optJSONObject("resource");
				    if(res != null){
				    	userName.setText(res.optString("name"));
				    }
	                oprationType.setText(mContext.getString(R.string.message_commented_you));
	                mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
	                ((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					break;
				case 7://图片墙
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					break;
				case 8://兴趣组
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					break;
				case 9://帖子
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					break;
				case 10://消息
					mViewGroup.findViewById(R.id.sys_msg_icon).setVisibility(View.VISIBLE);
					((TextView) mViewGroup.findViewById(R.id.content)).setText(mJSObj.optString("content"));
					mViewGroup.findViewById(R.id.content).setVisibility(View.VISIBLE);
					break;
					default:
						break;
				}
			}
			break;
		default:
			break;
		}
	}
}
