package com.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.util.Utils;
import com.zhuochuang.hsej.ActivityDetailsActivity;
import com.zhuochuang.hsej.CommonServiceActivity;
import com.zhuochuang.hsej.CommunitydetailsActivity;
import com.zhuochuang.hsej.CourseListActivity;
import com.zhuochuang.hsej.EnglishScoreQueryResultActivity;
import com.zhuochuang.hsej.EnterSchoolActivity;
import com.zhuochuang.hsej.FeedbackActivity;
import com.zhuochuang.hsej.HobbyGroupListActivity;
import com.zhuochuang.hsej.InformationActivity;
import com.zhuochuang.hsej.InformationDetailsActivity;
import com.zhuochuang.hsej.LoginActivity;
import com.zhuochuang.hsej.OneCardSolutionActivity;
import com.zhuochuang.hsej.PersonPageActivity;
import com.zhuochuang.hsej.PostDetailsActivity;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.SecondHandandLostActivity;
import com.zhuochuang.hsej.SurveyTopicActivity;
import com.zhuochuang.hsej.SurveyTopicResultActivity;
import com.zhuochuang.hsej.SystemAnnouncementActivity;
import com.zhuochuang.hsej.VoteActivity;
import com.zhuochuang.hsej.WebViewActivity;

public class DataLoader {

	private static DataLoader dataLoader;
	private static Context mContext;
	private ArrayList<Task> mTaskArray;
	public JSONArray mPropertyArr;
	public JSONObject mPlatforVersion;
	public int mCurrentPropertyIndex = 0;

	public DataLoader() {
		mTaskArray = new ArrayList<Task>();
	}

	public static DataLoader getInstance() {
		if (dataLoader == null) {
			dataLoader = new DataLoader();
		}
		return dataLoader;
	}

	public static void setApplicationContext(Context contexts) {
		mContext = contexts;
	}

	@SuppressLint("NewApi")
	public void startTaskForResult(TaskType type,
			HashMap<String, Object> params, TaskListener listener) {
		try {
			Task task = new Task(type, listener, params);
			task.taskContainer = mTaskArray;
			mTaskArray.add(task);
			if (Utils.getAndroidSDKVersion() >= 11) {
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				task.execute();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void closeTaskWithType(TaskType type) {
		for (Task task : mTaskArray) {
			if (task.taskType == type) {
				task.cancel(true);
			}
		}
	}

	public void closeAllTasks() {
		for (Task task : mTaskArray) {
			task.cancel(true);
		}
	}

	public boolean isLogin() {
		return getUserInfoObj() != null;
	}

	public JSONObject getLoginResultObj() {
		try {
			String loginResult = SharedPreferenceHandler
					.getLoginResult(mContext);
			if (loginResult == null || loginResult.length() == 0) {
				return null;
			}
			return new JSONObject(loginResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getUserInfoObj() {
		String userinfo = null;
		try {
			userinfo = SharedPreferenceHandler.getUserInfo(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (userinfo != null && userinfo.length() > 0) {
			try {
				return new JSONObject(userinfo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getUsetInfoKey(String key) {
		JSONObject obj = getUserInfoObj();
		if (obj != null && obj.has(key)) {
			return obj.optString(key);
		}
		return "";
	}

	public void updateUsetInfoKey(String key, String value) {
		String userinfo = null;
		try {
			userinfo = SharedPreferenceHandler.getUserInfo(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (userinfo != null && userinfo.length() > 0) {
			JSONObject obj = null;
			try {
				obj = new JSONObject(userinfo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (obj != null && obj.has(key)) {
				try {
					obj.put(key, value);
					SharedPreferenceHandler.saveUserInfo(mContext,
							obj.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public String getHeaderUsetId() {
		try {
			return SharedPreferenceHandler.getXPSUserId(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public JSONObject getSettingObj() {
		String setting = null;
		try {
			setting = SharedPreferenceHandler.getSetting(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (setting != null && setting.length() > 0) {
			try {
				return new JSONObject(setting);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getSettingKey(String key) {
		JSONObject obj = getSettingObj();
		if (obj != null && obj.has(key)) {
			return obj.optString(key);
		}
		return "";
	}

	/**
	 * { "result": "1", "area": 1434521997000, "interest": 1433830791000,
	 * "settings": { "residence": 8000, "minVersion": "0.9", "currentVersion":
	 * "1.0", "upgradeUrl": "http://hseschool.app360.cn/install", "intro":
	 * "华商E家APP正式发布：\r\n1、全新UI", "launchImage":
	 * "http://hseschool.app360.cn:80/uploads/2015/07/801bca79-097b-4a8a-b1d5-d29ed79cc206.jpg"
	 * }, "grade": 1438164111000, "aboutUrl":
	 * "http://hseschool.app360.cn:80/app/about/about", "group": 1438164102000 }
	 * */
	public String getSettingsKey(String key) {
		JSONObject obj = getSettingObj();
		if (obj != null && obj.has("settings")) {
			JSONObject settings = obj.optJSONObject("settings");
			try {
				return settings.optString(key);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return "";
	}

	/**
	 * setting/get config
	 * 
	 * @return
	 */
	public JSONObject getSettingConfig() {
		JSONObject obj = getSettingObj();
		if (obj != null && obj.has("config")) {
			return obj.optJSONObject("config");
		}
		return null;
	}

	/**
	 * 0 系统 1 用户 2 社团 3 活动 4 资讯 5 服务 6 评论 7 图片墙 8 兴趣组 9 帖子 10 消息
	 * 
	 * @param context
	 * @param resource
	 * @param obj
	 */
	public void openResource(Context context, int resource, JSONObject obj) {
		if (obj == null) {
			return;
		}
		Intent intent = null;
		switch (resource) {
		case 0:
			intent = new Intent(context, SystemAnnouncementActivity.class);
			break;
		case 1:
			intent = new Intent(context, PersonPageActivity.class);
			intent.putExtra("data", obj.toString());
			break;
		case 2:
			intent = new Intent(context, CommunitydetailsActivity.class);
			intent.putExtra("id", obj.optString("id"));
			break;
		case 3:
			if (obj.has("isSurveyTopic") && obj.optBoolean("isSurveyTopic")) {
				if (!isLogin()) {
					context.startActivity(new Intent(context,
							LoginActivity.class));
					((Activity) context).overridePendingTransition(
							R.anim.push_bottom_in, R.anim.push_bottom_out);
					return;
				}
				if (obj.has("favoriteStatus")
						&& obj.optString("favoriteStatus")
								.equalsIgnoreCase("6")) {
					intent = new Intent(context,
							SurveyTopicResultActivity.class);
				} else {
					intent = new Intent(context, SurveyTopicActivity.class);
				}
				intent.putExtra("resouceType", 3);
			} else if (obj.has("isVoteTopic") && obj.optBoolean("isVoteTopic")) {
				if (!isLogin()) {
					context.startActivity(new Intent(context,
							LoginActivity.class));
					((Activity) context).overridePendingTransition(
							R.anim.push_bottom_in, R.anim.push_bottom_out);
					return;
				}
				intent = new Intent(context, VoteActivity.class);
				intent.putExtra("favoriteStatus",
						obj.optInt("favoriteStatus", 0));
				intent.putExtra("resouceType", 3);
			} else {
				intent = new Intent(context, ActivityDetailsActivity.class);
			}
			intent.putExtra("id", obj.optString("id"));
			break;
		case 4:
			if (obj.has("isSurveyTopic") && obj.optBoolean("isSurveyTopic")) {
				if (!isLogin()) {
					context.startActivity(new Intent(context,
							LoginActivity.class));
					((Activity) context).overridePendingTransition(
							R.anim.push_bottom_in, R.anim.push_bottom_out);
					return;
				}
				if (obj.has("favoriteStatus")
						&& obj.optString("favoriteStatus")
								.equalsIgnoreCase("6")) {
					intent = new Intent(context,
							SurveyTopicResultActivity.class);
				} else {
					intent = new Intent(context, SurveyTopicActivity.class);
				}
			} else if (obj.has("isVoteTopic") && obj.optBoolean("isVoteTopic")) {
				if (!isLogin()) {
					context.startActivity(new Intent(context,
							LoginActivity.class));
					((Activity) context).overridePendingTransition(
							R.anim.push_bottom_in, R.anim.push_bottom_out);
					return;
				}
				intent = new Intent(context, VoteActivity.class);
				intent.putExtra("favoriteStatus",
						obj.optInt("favoriteStatus", 0));
			} else {
				intent = new Intent(context, InformationDetailsActivity.class);
			}
			intent.putExtra("id", obj.optString("id"));
			intent.putExtra("resouceType", 4);
			break;
		case 5:
			openNativeOrThirdWeb(context, obj, false);
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			intent = new Intent(context, HobbyGroupListActivity.class);
			intent.putExtra("data", obj.toString());
			break;
		case 9:
			intent = new Intent(context, PostDetailsActivity.class);
			intent.putExtra("id", obj.optString("id"));
			intent.putExtra("resouceType", 9);
			break;
		case 10:
			break;
		default:
			break;
		}
		if (intent != null) {
			context.startActivity(intent);
		}
	}

	/**
	 * 打开我的消息
	 * 
	 * @param context
	 * @param resource
	 * @param obj
	 */
	public void openResourceMsg(Context context, int resource, JSONObject obj) {
		if (obj == null) {
			return;
		}
		Intent intent = null;
		switch (resource) {
		case 0:
			/*
			 * intent = new Intent(context, SystemAnnouncementActivity.class);
			 * //intent.putExtra("id", obj.optString("id"));
			 * intent.putExtra("resouceType", 0);
			 */
			intent = new Intent(context, SystemAnnouncementActivity.class);
			break;
		case 1:
			intent = new Intent(context, FeedbackActivity.class);
			intent.putExtra("data", obj.toString());
			break;
		case 2:
			intent = new Intent(context, CommunitydetailsActivity.class);
			intent.putExtra("id", obj.optString("id"));
			break;
		case 3:
			intent = new Intent(context, ActivityDetailsActivity.class);
			intent.putExtra("id", obj.optString("id"));
			break;
		case 4:
			intent = new Intent(context, InformationDetailsActivity.class);
			intent.putExtra("id", obj.optString("id"));
			intent.putExtra("resouceType", 4);
			break;
		case 5:
			openNativeOrThirdWeb(context, obj, false);
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			intent = new Intent(context, HobbyGroupListActivity.class);
			intent.putExtra("data", obj.toString());
			break;
		case 9:
			intent = new Intent(context, PostDetailsActivity.class);
			intent.putExtra("id", obj.optString("id"));
			intent.putExtra("resouceType", 9);
			break;
		case 10:
			break;
		default:
			break;
		}
		if (intent != null) {
			context.startActivity(intent);
		}
	}

	public void openNativeOrThirdWeb(Context context, JSONObject obj,
			boolean isPush) {
		if (obj == null)
			return;

		Intent intent = null;

		switch (obj.optInt("type")) {
		case 1:
			switch (DefineHandler.getNativeType(obj.optString("nativeCode"))) {
			case Type_RXBL:
				if (!isLogin()) {
					needLogin(context, isPush);
					return;
				}
				intent = new Intent(context, EnterSchoolActivity.class);
				break;
			case Type_KCB:
				if (!isLogin()) {
					needLogin(context, isPush);
					return;
				}
				intent = new Intent(context, CourseListActivity.class);
				break;
			case Type_ESSC:
				intent = new Intent(context, SecondHandandLostActivity.class);
				intent.putExtra("type", 1);
				break;
			case Type_SWZL:
				intent = new Intent(context, SecondHandandLostActivity.class);
				intent.putExtra("type", 2);
				break;
			case Type_ZPFW:
				intent = new Intent(context, SecondHandandLostActivity.class);
				intent.putExtra("type", 3);
				break;
			case Type_SLJ:
				if (!isLogin()) {
					needLogin(context, isPush);
					return;
				}
				intent = new Intent(context,EnglishScoreQueryResultActivity.class);
				break;
			case Type_YKT:
				if (!isLogin()) {
					needLogin(context, isPush);
					return;
				}
				intent = new Intent(context, OneCardSolutionActivity.class);
				break;
			case Type_TYFW:
				intent = new Intent(context, CommonServiceActivity.class);
				intent.putExtra("id", obj.optString("targetId"));
				break;
			default:
				intent = new Intent(context, WebViewActivity.class);
				intent.putExtra("url", obj.optString("targetUrl"));
				if (isAppThirdpartyNeedLogin(context,
						obj.optString("targetUrl"), isPush)) {
					return;
				}
				break;
			}
			break;
		case 2:
			intent = new Intent(context, WebViewActivity.class);
			intent.putExtra("url", obj.optString("targetUrl"));
			if (isAppThirdpartyNeedLogin(context, obj.optString("targetUrl"),
					isPush)) {
				return;
			}
			break;
		case 3:
			intent = new Intent(context, InformationActivity.class);
			intent.putExtra("type", 3);
			intent.putExtra("typeId", obj.optString("nativeCode"));
			intent.putExtra("name", obj.optString("name"));
			break;
		case 4:
			intent = new Intent(context, InformationActivity.class);
			intent.putExtra("type", 4);
			intent.putExtra("typeId", obj.optString("nativeCode"));
			intent.putExtra("name", obj.optString("name"));
			break;
		default:
			intent = new Intent(context, WebViewActivity.class);
			intent.putExtra("url", obj.optString("targetUrl"));
			if (isAppThirdpartyNeedLogin(context, obj.optString("targetUrl"),
					isPush)) {
				return;
			}
			break;
		}

		if (intent != null) {
			if (isPush) {
				try {
					SharedPreferenceHandler.savePushMsg(context, null);
					intent.putExtra("isPush", true);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
				}
			} else {
				context.startActivity(intent);
			}
		}
	}

	/**
	 * native 使用前先登录
	 * 
	 * @param context
	 * @param isPush
	 */
	private void needLogin(Context context, boolean isPush) {
		Intent intent = new Intent(context, LoginActivity.class);
		if (isPush) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
		try {
			((Activity) context).overridePendingTransition(
					R.anim.push_bottom_in, R.anim.push_bottom_out);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 第三方网址带token， 判断需不需要登录
	 * 
	 * @param url
	 * @return needlogin?
	 */
	private boolean isAppThirdpartyNeedLogin(Context context, String url,
			boolean isPush) {
		if (url != null && url.length() > 0 && url.contains("/app/thirdparty/")
				&& url.contains("needLogin=1") && !isLogin()) {
			needLogin(context, isPush);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param obj
	 * @return 0未加入，1已加入，2审核中
	 */
	public int getStatuType(JSONObject obj) {
		if (obj == null || !obj.has("favoriteStatus")) {
			return 0;
		}

		int statu = 0;
		int favoriteStatus = 0;

		try {
			favoriteStatus = obj.optInt("favoriteStatus");
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

		switch (favoriteStatus) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			break;
		case 10:
			break;
		case 11:
			break;
		default:
			break;
		}

		return statu;
	}

	/**
	 * umeng push intent
	 * 
	 * @param context
	 */
	public void pushApplication(Context context) {
		try {
			String objStr = SharedPreferenceHandler.getPushMsg(context);
			if (objStr == null || objStr.length() == 0
					|| objStr.equalsIgnoreCase("null")) {
				return;
			}

			JSONObject obj = new JSONObject(objStr);
			if (obj == null || !obj.has("extra")) {
				SharedPreferenceHandler.savePushMsg(context, null);
				return;
			}

			JSONObject extra = obj.optJSONObject("extra");
			if (extra == null || !extra.has("resourceType")) {
				SharedPreferenceHandler.savePushMsg(context, null);
				return;
			}

			Intent intent = null;
			switch (extra.optInt("resourceType")) {
			case 0:
				intent = new Intent(context, SystemAnnouncementActivity.class);
				break;
			case 1:
				intent = new Intent(context, FeedbackActivity.class);
				intent.putExtra("data", extra.toString());
				break;
			case 2:
				intent = new Intent(context, CommunitydetailsActivity.class);
				intent.putExtra("id", extra.optString("id"));
				break;
			case 3:
				if (extra.has("isSurveyTopic")
						&& extra.optBoolean("isSurveyTopic")) {
					if (!isLogin()) {
						SharedPreferenceHandler.savePushMsg(context, null);
						intent = new Intent(context, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
						return;
					}
					if (extra.has("favoriteStatus")
							&& extra.optString("favoriteStatus")
									.equalsIgnoreCase("6")) {
						intent = new Intent(context,
								SurveyTopicResultActivity.class);
					} else {
						intent = new Intent(context, SurveyTopicActivity.class);
					}
					intent.putExtra("resouceType", 3);
				} else if (obj.has("isVoteTopic")
						&& obj.optBoolean("isVoteTopic")) {
					if (!isLogin()) {
						SharedPreferenceHandler.savePushMsg(context, null);
						intent = new Intent(context, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
						return;
					}
					intent = new Intent(context, VoteActivity.class);
					intent.putExtra("favoriteStatus",
							obj.optInt("favoriteStatus", 0));
					intent.putExtra("resouceType", 3);
				} else {
					intent = new Intent(context, ActivityDetailsActivity.class);
				}
				intent.putExtra("id", extra.optString("id"));
				break;
			case 4:
				if (extra.has("isSurveyTopic")
						&& extra.optBoolean("isSurveyTopic")) {
					if (!isLogin()) {
						SharedPreferenceHandler.savePushMsg(context, null);
						intent = new Intent(context, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
						return;
					}
					if (extra.has("favoriteStatus")
							&& extra.optString("favoriteStatus")
									.equalsIgnoreCase("6")) {
						intent = new Intent(context,
								SurveyTopicResultActivity.class);
					} else {
						intent = new Intent(context, SurveyTopicActivity.class);
					}
				} else if (extra.has("isVoteTopic")
						&& extra.optBoolean("isVoteTopic")) {
					if (!isLogin()) {
						SharedPreferenceHandler.savePushMsg(context, null);
						intent = new Intent(context, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
						return;
					}
					intent = new Intent(context, VoteActivity.class);
					intent.putExtra("favoriteStatus",
							extra.optInt("favoriteStatus", 0));
				} else {
					intent = new Intent(context,
							InformationDetailsActivity.class);
				}
				intent.putExtra("id", extra.optString("id"));
				intent.putExtra("resouceType", 4);
				break;
			case 5:
				openNativeOrThirdWeb(context, extra, true);
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				intent = new Intent(context, HobbyGroupListActivity.class);
				intent.putExtra("data", extra.toString());
				break;
			case 9:
				intent = new Intent(context, PostDetailsActivity.class);
				intent.putExtra("id", extra.optString("id"));
				intent.putExtra("resouceType", 9);
				break;
			case 10:
				break;
			default:
				break;
			}
			if (intent != null) {
				SharedPreferenceHandler.savePushMsg(context, null);
				intent.putExtra("isPush", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
		}
	}

	public JSONArray joinJSONArray(JSONArray old, JSONArray news) {
		if (old == null && news == null) {
			return null;
		}
		if (old == null) {
			return news;
		}
		if (news == null) {
			return old;
		}
		StringBuffer buffer = new StringBuffer();
		try {
			int len = old.length();
			for (int i = 0; i < len; i++) {
				JSONObject obj1 = (JSONObject) old.get(i);
				if (i == len - 1)
					buffer.append(obj1.toString());
				else
					buffer.append(obj1.toString()).append(",");
			}
			len = news.length();
			if (len > 0)
				buffer.append(",");
			for (int i = 0; i < len; i++) {
				JSONObject obj1 = (JSONObject) news.get(i);
				if (i == len - 1)
					buffer.append(obj1.toString());
				else
					buffer.append(obj1.toString()).append(",");
			}
			buffer.insert(0, "[").append("]");
			return new JSONArray(buffer.toString());
		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}
		return null;
	}

	public JSONArray insertStacktop(JSONArray old, JSONObject obj) {
		if (obj == null) {
			return old;
		}
		if (old == null) {
			old = new JSONArray();
			old.put(obj);
			return old;
		}
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append(obj.toString()).append(",");
			int len = old.length();
			for (int i = 0; i < len; i++) {
				JSONObject obj1 = (JSONObject) old.get(i);
				if (i == len - 1)
					buffer.append(obj1.toString());
				else
					buffer.append(obj1.toString()).append(",");
			}
			buffer.insert(0, "[").append("]");
			return new JSONArray(buffer.toString());
		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public HashMap<String, String> getUrlParamsMap(String url){
		if(url == null || url.length() == 0){
			return null;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		
		try {
			List<NameValuePair> parameters = URLEncodedUtils.parse(new URI(url), "UTF-8");
			for(NameValuePair p : parameters){
				map.put(p.getName(), p.getValue());
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return map;
	}

	public HashMap<String, Object> getVoteGetVoteTopicParams(int resourceType,
			String resourceId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("resourceType", resourceType);
		params.put("resourceId", resourceId);
		return params;
	}

	public HashMap<String, Object> getVoteVoteSubmitParams(String id,
			String value) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("topicId", id);
		params.put("value", value);
		return params;
	}

	// /store
	public HashMap<String, Object> getGoodsTypeListGoodsTypeParams(String id) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return params;
	}

	public HashMap<String, Object> getShopShopDataTypeParams(String id) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return params;
	}

	public HashMap<String, Object> setsaveShoppingCarTypeParams(int number, String colour, String spec, String price, String goodsId, boolean isShopcar) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("number", number);
		params.put("colour", colour);
		params.put("spec", spec);
		params.put("price", price);
		params.put("goodsId", goodsId);
		if(isShopcar){
			params.put("tag", 1);
		}
		return params;
	}

	public HashMap<String, Object> setupdateUserAddressTypeParams(int id,
			String name, String phone, String area, String address, int mark) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("name", name);
		params.put("phone", phone);
		params.put("area", area);
		params.put("address", address);
		params.put("mark", mark);
		return params;
	}

	public HashMap<String, Object> getGoodsDataTypeParams(String shopId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", shopId);
		return params;
	}

	public HashMap<String, Object> setsaveOrders(String data) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("SubmitOrder", data);
		return params;
	}

	public HashMap<String, Object> getListOrdersTypeParams(String status,String name) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("name", name);
		return params;
	}
	
	public HashMap<String, Object> getListOrdersTypeParams(String status,String name, int pageNo, int pageSize) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("name", name);
		params.put("pageNo", pageNo);
		params.put("pageSize", pageSize);
		return params;
	}
	
	public HashMap<String, Object> setSaveUserAddressTypeParams(String name,
			String phone, String address, String mark) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("phone", phone);
		params.put("address", address);
		params.put("mark", mark);
		return params;
	}

	public HashMap<String, Object> setUpdateUserAddressTypeParams(String id,
			String name, String phone, String area, String address, int mark) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("name", name);
		params.put("phone", phone);
		params.put("area", area);
		params.put("address", address);
		params.put("mark", mark);
		return params;
	}

	public HashMap<String, Object> setDelUserAddressTypeParams(String id) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return params;
	}

	public HashMap<String, Object> setGoodsListGoodsTypeParams(String name,
			String shopId, String parentType, String subType, String sort, int pageNo) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("shopId", shopId);
		params.put("parentType", parentType);
		params.put("subType", subType);
		params.put("sort", sort);
		params.put("pageNo", pageNo);
		return params;
	}
	
	public HashMap<String, Object> setListGoodTypeParams(String id) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return params;
	}
	
	public HashMap<String, Object> setDelShoppingCarTypeParams(String id) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return params;
	}

	public HashMap<String, Object> setGoodsEvaluateTypeParams(String id, int pageNum) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("pageNo", pageNum);
		params.put("pageSize", 20);
		return params;
	}

	public HashMap<String, Object> setGetWayTypeParams(String sign,
			String request_xml, String pay_type) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("sign", sign);
		params.put("request_xml", request_xml);
		params.put("pay_type", pay_type);
		return params;
	}

	public HashMap<String, Object> setShopListTypeParams(String name) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("name", name);
		return params;
	}
	public HashMap<String, Object> setOrderNumTypeParams(String orderNum) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("orderNum", orderNum);
		return params;
	}
	public HashMap<String, Object> setUploadPhotoTypeParams(String imgStr,String resourceType) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("imgStr", imgStr);
		params.put("resourceType",resourceType);
		return params;
	}
	public HashMap<String, Object> setOrdersEvaluateOrdersTypeParams(String id,String content,String imgIds,int evaluate) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("content",content);
		params.put("imgIds",imgIds);
		params.put("evaluate",evaluate);
		return params;
	}
	public HashMap<String, Object> setFavoriteTypeParams(String resourceType, String resourceIds, String statuses) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("resourceType", resourceType);
		params.put("resourceIds",resourceIds);
		params.put("statuses",statuses);
		return params;
	}
	public HashMap<String, Object> setUpdateOrderStatusTypeParams(String means,String orderNum,String status) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("means", means);
		params.put("orderNum",orderNum);
		params.put("status",status);
		return params;
	}
	
	public HashMap<String, Object> setAfterSaleServiceParams(String shopId) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("shopId", shopId);
		return params;
	}
}
