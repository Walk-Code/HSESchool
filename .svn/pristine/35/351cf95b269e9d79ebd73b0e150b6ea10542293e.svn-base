package com.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.util.MD5;

public class SharedPreferenceHandler {

	final static String STRING_SAVESetting = "Setting";
	final static String STRING_SAVEDictionary = "Dictionary";
	final static String STRING_SAVEDictionaryParamsTime = "DictionaryParamsTime";
	final static String STRING_SAVELoginResult = "LoginResult";
	final static String STRING_SAVEUserInfo = "UserInfo";
	final static String STRING_SAVEUserId = "UserId";
	final static String STRING_SAVEToken = "Token";
	final static String STRING_SAVEAdvertisePath = "AdvertisePath";
	final static String STRING_SAVESessionId = "SessionId";
	final static String STRING_SAVEUmengRegistrar = "UmengRegistrar";
	final static String STRING_SAVEUmengPush = "UmengPush";
	final static String STRING_SAVEMsgNotify = "MsgNotify";
	final static String STRING_SAVELoginParams = "LoginParams";
	
	final static String STRING_PublishProtocol = "PublishProtocol";
	
	public static void saveSetting(Context context, String setting) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVESetting, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("setting", setting);
		editor.commit();
	}
	
	public static String getSetting(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVESetting, Context.MODE_PRIVATE);
		return settings.getString("setting", null);
	}
	
	/**
	 * 单个的更新时间
	 * @param context
	 * @param updateTime
	 * @param dictionaryParams
	 */
	public static void saveDictionaryParamsTime(Context context, long updateTime, String dictionaryParams){
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEDictionaryParamsTime, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putLong("updateTime_" + dictionaryParams, updateTime);
		editor.commit();
	}
	
	public static long getDictionaryParamsTime(Context context, String dictionaryParams) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEDictionaryParamsTime, Context.MODE_PRIVATE);
		return settings.getLong("updateTime_" + dictionaryParams, 0);
	}
	
	public static void saveDictionary(Context context, String dictionary){
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEDictionary, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("dictionary", dictionary);
		editor.commit();
	}
	
	public static String getDictionary(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEDictionary, Context.MODE_PRIVATE);
		return settings.getString("dictionary", null);
	}
	
	public static void saveLoginResult(Context context, String loginResult) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVELoginResult, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("loginResult", loginResult);
		editor.commit();
	}
	
	public static String getLoginResult(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVELoginResult, Context.MODE_PRIVATE);
		return settings.getString("loginResult", null);
	}
	
	public static void saveUserInfo(Context context, String userinfo) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEUserInfo, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("userinfo", userinfo);
		editor.commit();
	}
	
	public static String getUserInfo(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEUserInfo, Context.MODE_PRIVATE);
		return settings.getString("userinfo", null);
	}

	/**
	 * 保存header XPSUserId
	 * @param context
	 * @param userId
	 * @throws Exception
	 */
	public static void saveXPSUserId(Context context, String userId) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEUserId, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("userId", userId);
		editor.commit();
	}
	
	public static String getXPSUserId(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEUserId, Context.MODE_PRIVATE);
		return settings.getString("userId", "");
	}
	
	public static void saveXPSToken(Context context, String token) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEToken, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("token", token);
		editor.commit();
	}
	
	public static String getXPSToken(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEToken, Context.MODE_PRIVATE);
		return settings.getString("token", "");
	}
	
	public static void removeAllSharedPreference(Context context) throws Exception{
		saveLoginResult(context, null);
		saveUserInfo(context, null);
		saveXPSUserId(context, null);
		saveXPSToken(context, null);
		saveLoginParams(context, null);
	}
	
	/**
	 * 保存广告图advertise path
	 * @param context
	 * @param advertisePath
	 * @throws Exception
	 */
	public static void saveAdvertisePath(Context context, String advertisePath) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEAdvertisePath, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("advertisePath", advertisePath == null? null : MD5.getStringMD5String(advertisePath));
		editor.commit();
	}
	
	public static String getAdvertisePath(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEAdvertisePath, Context.MODE_PRIVATE);
		return settings.getString("advertisePath", null);
	}
	
	/**
	 * 共用Cookie，让客户端访问服务器保持Session进行通信。
	 * @param context
	 * @param sessionId
	 * @throws Exception
	 */
	public static void saveSessionId(Context context, String sessionId) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVESessionId, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("sessionId", sessionId);
		editor.commit();
	}
	
	public static String getSessionId(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVESessionId, Context.MODE_PRIVATE);
		return settings.getString("sessionId", null);
	}
	
	/**
	 * UmengRegistrar
	 * @param context
	 * @param device_token
	 * @throws Exception
	 */
	public static void saveUmengRegistrar(Context context, String device_token) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEUmengRegistrar, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("device_token", device_token);
		editor.commit();
	}

	public static String getUmengRegistrar(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEUmengRegistrar, Context.MODE_PRIVATE);
		
		String device_token = settings.getString("device_token", "");
		return device_token;
	}
	
	/**
	 * 接收到的推送obj
	 * @param context
	 * @param pushObj
	 * @throws Exception
	 */
	public static void savePushMsg(Context context, String pushObj) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEUmengPush, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("pushObj", pushObj);
		editor.commit();
	}

	public static String getPushMsg(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEUmengPush, Context.MODE_PRIVATE);
		
		String push = settings.getString("pushObj", null);
		return push;
	}
	
	/**
	 * 消息未读数量
	 * @param context
	 * @param msgNotify
	 * @throws Exception
	 */
	public static void saveMsgNotify(Context context, String msgNotify) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEMsgNotify, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("MsgNotify", msgNotify);
		editor.commit();
	}
	
	public static String getMsgNotify(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEMsgNotify, Context.MODE_PRIVATE);
		return settings.getString("MsgNotify", null);
	}
	
	/**
	 * 消息未读数量
	 * @param context
	 * @param msgNotify
	 * @throws Exception
	 */
	public static void saveLoginParams(Context context, LoginParamsItem item) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEMsgNotify, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		String account = null;
		String password = null;
		int type = 1;
		if(item != null){
			account = item.userAccount;
			password = item.userPassword;
			type = item.userType;
		}
		editor.putString("account", account);
		editor.putString("password", password);
		editor.putInt("type", type);
		editor.commit();
	}
	
	public static LoginParamsItem getLoginParamsItem(Context context) throws Exception{
		SharedPreferences settings = context.getSharedPreferences(STRING_SAVEMsgNotify, Context.MODE_PRIVATE);
		LoginParamsItem item = new LoginParamsItem();
		item.userAccount = settings.getString("account", null);
		item.userPassword = settings.getString("password", null);
		item.userType = settings.getInt("type", 1);
		if(item.userAccount == null || item.userPassword == null){
			return null;
		}
		
		return item;
	}
	
	/**
	 * publish protocol
	 * @param context
	 * @param id
	 * @throws Exception
	 */
	public static void savePublishProtocol(Context context, String id){
		SharedPreferences settings = context.getSharedPreferences(STRING_PublishProtocol, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putBoolean("protocol_" + id, true);
		editor.commit();
	}
	
	public static boolean getPublishProtocol(Context context, String id){
		SharedPreferences settings = context.getSharedPreferences(STRING_PublishProtocol, Context.MODE_PRIVATE);
		return settings.getBoolean("protocol_" + id, false);
	}
}