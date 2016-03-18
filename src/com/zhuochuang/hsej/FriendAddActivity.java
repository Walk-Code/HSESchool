package com.zhuochuang.hsej;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.model.DataLoader;
import com.model.EventManager;
import com.model.SharedPreferenceHandler;
import com.model.TaskType;
/**
 * 添加好友
 * @author Dion
 *
 */
public class FriendAddActivity extends BaseActivity{
	
	private String [] mSearchClassesArray;//搜索条件的类别：地区，年级
	private String [] mSearchTextArray;//列表中间的hint Text
	private TextView mConditionText = null;//搜索条件选择
	private Button mCommitButton = null;
	private int mLayer = 0;
	private Handler mHandler;
	private JSONObject mDataObj = null;
	private JSONObject mProfessionsObj = null;
	private HashMap<String, Object> mParams = new HashMap<String, Object>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//标题，添加好友
		setTitleText(R.string.add_friends_title);
		
		setContentView(R.layout.activity_add_firends);

		String dictionary = null;
		try {
			dictionary = SharedPreferenceHandler.getDictionary(FriendAddActivity.this);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(dictionary == null || dictionary.length() == 0){
			showDialogCustom(DIALOG_CUSTOM);
			HashMap<String, Object> params = new HashMap<String, Object>();
//			params.put("area", true);
//			params.put("unit", true);
//			params.put("dwh", 2);
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_SettingGetDictionary, params, FriendAddActivity.this);
		}
		else{
			try {
				mDataObj = new JSONObject(dictionary);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		initView();
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				mLayer = 0;
				Object[] objs = (Object[]) msg.obj;
				switch (msg.what) {
				case EventManager.EventType_AreaGroup:
					JSONObject obj = null;
					try {
						obj = new JSONObject((String)objs[1]);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if(obj != null){
						if(obj.has("name")){
							mConditionText.setText(obj.optString("name"));
						}else{
							mConditionText.setText(obj.optString("title"));
						}
					}else{
						//条件选择为不限
						mConditionText.setText((String)objs[1]);
					}
					//地区code
					if((Integer)objs[0]  == 0){
						if(obj != null && obj.has("code")){
							mParams.put("cityCode", obj.optString("code"));
						}else{
							//条件为不限
							if(mParams.containsKey("cityCode")){
								mParams.remove("cityCode");
							}
						}
					}
					//年级code
					if((Integer)objs[0]  == 1 ){
						if((Boolean)objs[2]){
							mParams.put("grade", objs[1]);
						}else{
							if(mParams.containsKey("grade")){
								mParams.remove("grade");
							}
						}
					}
					//专业code
					if((Integer)objs[0]  == 2){
						
						if(obj != null && obj.has("code")){
							mParams.put("professionCode", obj.optString("code"));
							mProfessionsObj = obj;
							//专业重新选择后，班级选项恢复原状
							((TextView)(findViewById(R.id.option_classes).findViewById(R.id.search_firends_text))).setText(mSearchTextArray[3]);
						}else{
							//专业选择不限，班级也自动选 不限
							((TextView)(findViewById(R.id.option_classes).findViewById(R.id.search_firends_text))).setText(R.string.unlimited_setting_add_friend);
							mProfessionsObj = null;
							if(mParams.containsKey("professionCode")){
								mParams.remove("professionCode");
							}
							if(mParams.containsKey("classCode")){
								mParams.remove("classCode");
							}
						}
					}
					//班级code
					if((Integer)objs[0] == 3){
						if(obj != null && obj.has("code")){
							mParams.put("classCode", obj.optString("code"));
						}else{
							if(mParams.containsKey("classCode")){
								mParams.remove("classCode");
							}
						}
					}
					// 兴趣code
					if((Integer)objs[0]  == 4){
						if(obj != null && obj.has("code")){
							mParams.put("hobby", obj.optString("title"));
						}else{
							if(mParams.containsKey("hobby")){
								mParams.remove("hobby");
							}
						}
					}
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void initView(){
		
		findViewById(R.id.option_area).setVisibility(View.GONE);
		
		mSearchClassesArray = getResources().getStringArray(R.array.search_list_classes);
		mSearchTextArray = getResources().getStringArray(R.array.search_list_text);
		
		//地区
		((TextView)(findViewById(R.id.option_area).findViewById(R.id.search_firends_classes))).setText(mSearchClassesArray[0]);
		((TextView)(findViewById(R.id.option_area).findViewById(R.id.search_firends_text))).setText(mSearchTextArray[0]);
		findViewById(R.id.option_area).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onOptionsClick(v);
			}
		});
		//年级
		((TextView)(findViewById(R.id.option_grade).findViewById(R.id.search_firends_classes))).setText(mSearchClassesArray[1]);
		((TextView)(findViewById(R.id.option_grade).findViewById(R.id.search_firends_text))).setText(mSearchTextArray[1]);
		findViewById(R.id.option_grade).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onOptionsClick(v);
			}
		});
		//专业
		((TextView)(findViewById(R.id.option_professions).findViewById(R.id.search_firends_classes))).setText(mSearchClassesArray[2]);
		((TextView)(findViewById(R.id.option_professions).findViewById(R.id.search_firends_text))).setText(mSearchTextArray[2]);
		findViewById(R.id.option_professions).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onOptionsClick(v);
			}
		});

		//班级
		((TextView)(findViewById(R.id.option_classes).findViewById(R.id.search_firends_classes))).setText(mSearchClassesArray[3]);
		((TextView)(findViewById(R.id.option_classes).findViewById(R.id.search_firends_text))).setText(mSearchTextArray[3]);
		findViewById(R.id.option_classes).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onOptionsClick(v);
			}
		});
		
		//兴趣
		((TextView)(findViewById(R.id.option_interest).findViewById(R.id.search_firends_classes))).setText(mSearchClassesArray[4]);
		((TextView)(findViewById(R.id.option_interest).findViewById(R.id.search_firends_text))).setText(mSearchTextArray[4]);
		
		/*//真实姓名
		((TextView)findViewById(R.id.textview_realname)).setText(mSearchClassesArray[5]);
		((EditText)findViewById(R.id.editview_realname)).setHint(mSearchTextArray[5]);*/

		//提交Button
		mCommitButton = (Button)findViewById(R.id.search_commit);
		mCommitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialogCustom(DIALOG_CUSTOM);
				String key = ((EditText)findViewById(R.id.sercher_add_firends)).getText().toString();
				if(!TextUtils.isEmpty(key)){
					mParams.put("nickName", key);
					mParams.put("xm", key);
				}
				else if(mParams.containsKey("nickName")){
					mParams.remove("nickName");
					mParams.remove("xm");
				}
				
				mParams.put("pageSize", 1000);
				
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserListQuasiFriends, mParams, FriendAddActivity.this);
			}
		});
	}
	
	public void onOptionsClick(View view){
		if(mDataObj == null){
			return;
		}
		Intent intent = new Intent(FriendAddActivity.this, FriendAddSettingActivity.class);
		mConditionText = (TextView)view.findViewById(R.id.search_firends_text);
		JSONArray array = null;
		mLayer = 0;
		switch (view.getId()) {
		case R.id.option_area:
			array = mDataObj.optJSONArray("area");
			intent.putExtra("name", mSearchTextArray[0]);
			intent.putExtra("layer", getLayer(array));
			intent.putExtra("index", 0);
			break;
		case R.id.option_grade:
			array = mDataObj.optJSONArray("grade");
			intent.putExtra("name", mSearchTextArray[1]);
			intent.putExtra("layer", 0);
			intent.putExtra("index", 1);
			break;
		case R.id.option_professions:
			array = mDataObj.optJSONArray("group").optJSONObject(0).optJSONArray("item");
			intent.putExtra("name", mSearchTextArray[2]);
			intent.putExtra("layer", getLayer(array)-1);
			intent.putExtra("index", 2);
			break;
		case R.id.option_classes:
			if(mProfessionsObj != null){
				array = mProfessionsObj.optJSONArray("item");
			}else{
				Toast.makeText(FriendAddActivity.this, getResources().getString(R.string.select_professions), Toast.LENGTH_SHORT).show();
			}
			intent.putExtra("name", mSearchTextArray[3]);
			intent.putExtra("layer", getLayer(array));
			intent.putExtra("index", 3);
			break;
		case R.id.option_interest:
			array = mDataObj.optJSONArray("interest");
			intent.putExtra("name", mSearchTextArray[4]);
			intent.putExtra("layer", getLayer(array));
			intent.putExtra("index", 4);
			break;
		default:
			break;
		}
		if(array != null){
			intent.putExtra("array", array.toString());
			startActivity(intent);
		}
	}
	
	protected int getLayer(JSONArray array) {
		if(array != null &&array.length() >0 && array.optJSONObject(0).has("item")){
			mLayer++;
			JSONArray a = array.optJSONObject(0).optJSONArray("item");
			getLayer(a);
		}
		return mLayer;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {  
	        View v = getCurrentFocus();  
	        if (isShouldHideInput(v, ev)) {  
	        	
	            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	            if (imm != null && v != null) {  
	                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
	            }  
	        }  
	        return super.dispatchTouchEvent(ev);  
	    }  
	    // 保留其他控件Touch事件
	    if (getWindow().superDispatchTouchEvent(ev)) {  
	        return true;  
	    }  
	    return onTouchEvent(ev); 
	}
	
    private  boolean isShouldHideInput(View v, MotionEvent event) {  
        if (v != null && (v instanceof EditText)) {  
            int[] leftTop = { 0, 0 };  
            //获取输入框当前的location位置  
            v.getLocationInWindow(leftTop);  
            int left = leftTop[0];  
            int top = leftTop[1];  
            int bottom = top + v.getHeight();  
            int right = left + v.getWidth();  
            if (event.getX() > left && event.getX() < right  
                    && event.getY() > top && event.getY() < bottom) {  
                // 点击的是输入框区域，保留点击EditText的事件  
                return false;  
            } else {  
                return true;  
            }  
        }  
        return false;  
    }  
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventManager.getInstance().removeHandlerListenner(mHandler);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		removeDialogCustom();
		if(result == null){
			return;
		}
		
		if(result instanceof Error){
			Toast.makeText(FriendAddActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch (type) {
		case TaskOrMethod_SettingGetDictionary:
			if(result instanceof JSONObject){
				mDataObj = (JSONObject)result;
			}
			break;
		case TaskOrMethod_UserListQuasiFriends:
			if(result instanceof JSONObject && ((JSONObject)result).has("items")){
				JSONArray items = ((JSONObject)result).optJSONArray("items");
				if(items != null && items.length() >0){
					Intent intent = new Intent(FriendAddActivity.this, FriendsSomeActivity.class);
					intent.putExtra("data", items.toString());
					startActivity(intent);
				}else{
					Toast.makeText(FriendAddActivity.this, getResources().getString(R.string.no_friend), Toast.LENGTH_SHORT).show();
				}
				
			}
			break;
		default:
			break;
		}
	}
}
