package com.layout;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.util.Utils;

public class CourseListDayView extends FrameLayout{

	View mConvertView;
	static final int DealHeight = 40;
	
	public CourseListDayView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initConvertView(context);
	}
	
	public CourseListDayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initConvertView(context);
	}

	private void initConvertView(Context context){
		//mConvertView = ViewGroup.inflate(context, R.layout.view_courselist_day, null);
		//this.addView(mConvertView);
	}
	
	public void setDayData(Context context, JSONArray arr){
		this.removeAllViews();
		if(arr == null || arr.length() == 0){
			initEmptyWeek(context);
			return;
		}
		
		getEmptyLineList(context, arr);
		
		for(int i = 0; i < arr.length(); i++){
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.TOP);
			FrameLayout.LayoutParams lineLp = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(context, 0.5f), Gravity.TOP);
			boolean isSingleKc = false;
			JSONObject obj = arr.optJSONObject(i);
			String djj = obj.optString("djj");
			if(djj.contains(",")){
				String[] split = djj.split(",");
				switch (split.length) {
				case 2:
					lp.height = Utils.dipToPixels(context, DealHeight * 2);
					break;
				case 3:
					lp.height = Utils.dipToPixels(context, DealHeight * 3);
					break;
				case 4:
					lp.height = Utils.dipToPixels(context, DealHeight * 4);
					break;
				default:
					break;
				}
				int firstDjj = Integer.parseInt(split[0]);
				getTopMargin(context, lp, firstDjj);
			}
			else{
				isSingleKc = true;
				lp.height = Utils.dipToPixels(context, DealHeight);
				getTopMargin(context, lp, Integer.parseInt(djj));
			}
			lineLp.topMargin = lp.topMargin + lp.height;
			CourseListView view = new CourseListView(context);
			this.addView(view, lp);
			view.setData(obj, isSingleKc);
			View line = new View(context);
			line.setBackgroundColor(Color.parseColor("#bbbbbb"));
			this.addView(line, lineLp);
		}
	}
	
	private void getTopMargin(Context context, FrameLayout.LayoutParams lp, int firstDjj){
		switch (firstDjj) {
		case 1:
			lp.topMargin = 0;
			break;
		case 2:
			lp.topMargin = Utils.dipToPixels(context, DealHeight);
			break;
		case 3:
			lp.topMargin = Utils.dipToPixels(context, DealHeight * 2);
			break;
		case 4:
			lp.topMargin = Utils.dipToPixels(context, DealHeight * 3);
			break;
		case 5:
			lp.topMargin = Utils.dipToPixels(context, DealHeight * 4);
			break;
		case 6:
			lp.topMargin = Utils.dipToPixels(context, DealHeight * 5);
			break;
		case 7:
			lp.topMargin = Utils.dipToPixels(context, DealHeight * 6);
			break;
		case 8:
			lp.topMargin = Utils.dipToPixels(context, DealHeight * 7);
			break;
		case 9:
			lp.topMargin = Utils.dipToPixels(context, DealHeight * 8);
			break;
		case 10:
			lp.topMargin = Utils.dipToPixels(context, DealHeight * 9);
			break;
		case 11:
			lp.topMargin = Utils.dipToPixels(context, DealHeight * 10);
			break;

		default:
			break;
		}
	}
	
	private boolean getLines(int endDjj){
		/*if(endDjj % 2 == 0){
			return true;
		}
		return false;*/
		return true;
	}
	
	/**
	 * empty kcb this week
	 * @param context
	 */
	private void initEmptyWeek(Context context){
		for(int i = 0; i < 5; i++){
			FrameLayout.LayoutParams lineLp = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(context, 0.5f), Gravity.TOP);
			lineLp.topMargin = Utils.dipToPixels(context, (DealHeight * 2) * (i + 1));
			View line = new View(context);
			line.setBackgroundColor(Color.parseColor("#bbbbbb"));
			this.addView(line, lineLp);
		}
	}
	
	private void getEmptyLineList(Context context, JSONArray arr){
		ArrayList<Integer> allreadyNum = new ArrayList<>();
		
		for(int i = 0; i < arr.length(); i++){
			JSONObject obj = arr.optJSONObject(i);
			String djj = obj.optString("djj");
			if(djj.contains(",")){
				String[] split = djj.split(",");
				for(int j = 0; j < split.length; j++){
					allreadyNum.add(Integer.parseInt(split[j]));
				}
			}
			else{
				allreadyNum.add(Integer.parseInt(djj));
			}
		}
		
		for(int i = 1; i < 11; i++){
			for(int j = 0; j < allreadyNum.size(); j++){
				if(i % 2 == 0 && !allreadyNum.contains(i)){
					FrameLayout.LayoutParams lineLp = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(context, 0.5f), Gravity.TOP);
					lineLp.topMargin = Utils.dipToPixels(context, DealHeight * i);
					View line = new View(context);
					line.setBackgroundColor(Color.parseColor("#bbbbbb"));
					this.addView(line, lineLp);
					continue;
				}
				if(!allreadyNum.contains(i)){
					
				}
			}
		}
	}
}
