package com.zhuochuang.hsej;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.layout.emoji.EmojiUtils;
import com.model.Configs;
import com.model.CrashHandler;
import com.model.DataLoader;
import com.model.ImageLoaderConfigs;
import com.model.SharedPreferenceHandler;
import com.model.Task;
import com.model.TaskListener;
import com.model.TaskType;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class HSESchoolApp extends Application implements TaskListener{

	public UMSocialService umSocialServiceShare = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
	public CustomPlatform myCustomPlatformFavorite;
	public CustomPlatform myCustomPlatformFriend;
	public CustomPlatform myCustomPlatformReport;
	public CustomPlatform myCustomPlatformEmail;
	private List<Activity> activitys = new LinkedList<Activity>();
	private List<Activity> activitysStore = new LinkedList<Activity>();
	
	public JSONObject mShareObj;
	public String mResourceType, mResourceId, mToUserId;
	private OnClickListener mOnClickListener;
	private OnClickListener mOnClickListenerFriend;
	private OnClickListener mOnClickListenerReport;
	
	public boolean isLoginFailed = false;
	
	public int mType = 0;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Task.setApplicationContext(getApplicationContext());
		DataLoader.setApplicationContext(getApplicationContext());
		ImageLoaderConfigs.initImageLoader(getApplicationContext());
		EmojiUtils.setContext(getApplicationContext());
		CrashHandler.getInstance().init(getApplicationContext());
		
		/*AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		try {
			audiomanage.unloadSoundEffects();
		} catch (Exception e) {
			// TODO: handle exception
		} catch (Error e) {
			// TODO: handle exception
		}*/
		
		//initUmentShare();
		
		PushAgent pushAgent = PushAgent.getInstance(this);
		//pushAgent.enable();
		/**
		 * 该Handler是在IntentService中被调用，故
		 * 1. 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * 2. IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
		 * 	      或者可以直接启动Service
		 * */
		UmengMessageHandler messageHandler = new UmengMessageHandler(){
			@Override
			public void dealWithCustomMessage(final Context context, final UMessage msg) {
				new Handler(getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
					}
				});
			}
			
			@Override
			public Notification getNotification(Context context,
					UMessage msg) {
				switch (msg.builder_id) {
				case 1:
					NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
					RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
					myNotificationView.setTextViewText(R.id.notification_title, msg.title);
					myNotificationView.setTextViewText(R.id.notification_text, msg.text);
					myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
					myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
					builder.setContent(myNotificationView);
					Notification mNotification = builder.build();
					//由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
					mNotification.contentView = myNotificationView;
					return mNotification;
				default:
					//默认为0，若填写的builder_id并不存在，也使用默认。
					return super.getNotification(context, msg);
				}
			}
		};
		pushAgent.setMessageHandler(messageHandler);
		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * */
		final UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
			}

			@Override
			public void launchApp(Context arg0, UMessage arg1) {
				// TODO Auto-generated method stub
				super.launchApp(arg0, arg1);
				try {
					JSONObject obj = arg1.getRaw();
					//System.out.println("===999===" + obj.toString());
					SharedPreferenceHandler.savePushMsg(arg0, obj.toString());
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(isActivityStartAndExit(MainActivity.class)){
					DataLoader.getInstance().pushApplication(arg0);
				}
			}
		};
		pushAgent.setNotificationClickHandler(notificationClickHandler);
		
		myCustomPlatformFavorite = new CustomPlatform(getResources().getString(R.string.umeng_favorite), R.drawable.umeng_socialize_collection_on);
		myCustomPlatformFriend = new CustomPlatform(getResources().getString(R.string.umeng_friend), R.drawable.umeng_socialize_friend_on);
		myCustomPlatformReport = new CustomPlatform(getResources().getString(R.string.umeng_report), R.drawable.icon_report);
		myCustomPlatformEmail = new CustomPlatform(getResources().getString(R.string.umeng_email), R.drawable.umeng_socialize_gmail_on);
		// 平台点击事件处理	
		myCustomPlatformFavorite.mClickListener = new SocializeListeners.OnSnsPlatformClickListener() {
			@Override
			public void onClick(Context arg0, SocializeEntity arg1, SnsPostListener arg2) {
				// TODO Auto-generated method stub
				mOnClickListener.onClick(null);
			}
		};
		// 平台点击事件处理	
		myCustomPlatformFriend.mClickListener = new SocializeListeners.OnSnsPlatformClickListener() {
			@Override
			public void onClick(Context arg0, SocializeEntity arg1, SnsPostListener arg2) {
				// TODO Auto-generated method stub
				mOnClickListenerFriend.onClick(null);
			}
		};
		// 平台点击事件处理	
		myCustomPlatformReport.mClickListener = new SocializeListeners.OnSnsPlatformClickListener() {
			@Override
			public void onClick(Context arg0, SocializeEntity arg1, SnsPostListener arg2) {
				// TODO Auto-generated method stub
				mOnClickListenerReport.onClick(null);
			}
		};
		// 平台点击事件处理	
		myCustomPlatformEmail.mClickListener = new SocializeListeners.OnSnsPlatformClickListener() {
			@Override
			public void onClick(Context arg0, SocializeEntity arg1, SnsPostListener arg2) {
				// TODO Auto-generated method stub
				String name = "";
				String content = "";
				if(mType != Configs.UmengShare_App){
					if(mShareObj == null){
						return;
					}
					name = mShareObj.optString("name");
					content = mShareObj.has("description")? mShareObj.optString("description") : mShareObj.optString("introduction");
				}
				else{
					name = getResources().getString(R.string.app_name);
					content = getResources().getString(R.string.umeng_share_pk_app);
				}
				try {
					Intent email = new Intent(android.content.Intent.ACTION_SEND);
			        email.setType("plain/text");
			        //邮件主题
			        email.putExtra(android.content.Intent.EXTRA_SUBJECT, name);
			        //邮件内容
			        email.putExtra(android.content.Intent.EXTRA_TEXT, content + "\n" + getShareTargetUrl());  
			        email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        arg0.startActivity(Intent.createChooser(email, HSESchoolApp.this.getResources().getString(R.string.umeng_email_share)));
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(HSESchoolApp.this, HSESchoolApp.this.getResources().getString(R.string.umeng_email_share_null), Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
	
	public void setOnFavoriteClick(OnClickListener listener){
		mOnClickListener = listener;
	}
	
	public void setOnFriendClick(OnClickListener listener){
		mOnClickListenerFriend = listener;
	}
	
	public void setOnReportClick(OnClickListener listener){
		mOnClickListenerReport = listener;
	}
	
	private void initUmentShare(int type){
		umSocialServiceShare = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
		SocializeConfig config = umSocialServiceShare.getConfig();
	//	config.getPlatformMap().clear();
		
		Map<String, SnsPlatform> list = config.getPlatformMap();
		if(list.containsKey(myCustomPlatformReport.mShowWord)){
			list.remove(myCustomPlatformReport.mShowWord);
		}
		if(list.containsKey(myCustomPlatformEmail.mShowWord)){
			list.remove(myCustomPlatformEmail.mShowWord);
		}
		if(list.containsKey(myCustomPlatformFavorite.mShowWord)){
			list.remove(myCustomPlatformFavorite.mShowWord);
		}
		if(list.containsKey(myCustomPlatformFriend.mShowWord)){
			list.remove(myCustomPlatformFriend.mShowWord);
		}
		
		switch (type) {
		case Configs.UmengShare_Infomation:
			config.addCustomPlatform(myCustomPlatformFavorite);
			config.addCustomPlatform(myCustomPlatformFriend);
			config.removePlatform(SHARE_MEDIA.TENCENT, SHARE_MEDIA.QZONE, SHARE_MEDIA.EMAIL);
			config.addCustomPlatform(myCustomPlatformEmail);
			config.setPlatformOrder(myCustomPlatformFavorite.mShowWord, myCustomPlatformFriend.mShowWord, 
					SHARE_MEDIA.SINA.toString(), SHARE_MEDIA.QQ.toString(), SHARE_MEDIA.WEIXIN_CIRCLE.toString(),
					SHARE_MEDIA.WEIXIN.toString(), SHARE_MEDIA.SMS.toString(), myCustomPlatformEmail.mShowWord);
			break;
		case Configs.UmengShare_Post:
			config.addCustomPlatform(myCustomPlatformFavorite);
			config.addCustomPlatform(myCustomPlatformFriend);
			config.removePlatform(SHARE_MEDIA.TENCENT, SHARE_MEDIA.QZONE, SHARE_MEDIA.EMAIL);
			config.addCustomPlatform(myCustomPlatformReport);
			config.setPlatformOrder(myCustomPlatformFavorite.mShowWord, myCustomPlatformFriend.mShowWord, 
					SHARE_MEDIA.SINA.toString(), SHARE_MEDIA.QQ.toString(), SHARE_MEDIA.WEIXIN_CIRCLE.toString(),
					SHARE_MEDIA.WEIXIN.toString(), SHARE_MEDIA.SMS.toString(), myCustomPlatformReport.mShowWord);
			break;
		case Configs.UmengShare_App:
			config.removePlatform(SHARE_MEDIA.TENCENT, SHARE_MEDIA.QZONE, SHARE_MEDIA.EMAIL);
			config.addCustomPlatform(myCustomPlatformEmail);
			config.setPlatformOrder(SHARE_MEDIA.SINA.toString(), SHARE_MEDIA.QQ.toString(), SHARE_MEDIA.WEIXIN_CIRCLE.toString(),
					SHARE_MEDIA.WEIXIN.toString(), SHARE_MEDIA.SMS.toString(), myCustomPlatformEmail.mShowWord);
			break;

		default:
			break;
		}
	}
	
	private void initUmentSharePlatform(Context context, int type){
		switch (type) {
		case Configs.UmengShare_Infomation:
		case Configs.UmengShare_Post:
			if(mShareObj == null){
				return;
			}
			break;
		case Configs.UmengShare_App:
			break;

		default:
			break;
		}
		SocializeConfig config = umSocialServiceShare.getConfig();
		config.setSsoHandler(new SinaSsoHandler());
		config.setSsoHandler(new TencentWBSsoHandler());
		config.setSinaCallbackUrl("http://sns.whalecloud.com/sina2/callback");
		umSocialServiceShare.registerListener(mSnsPostListener);
		
		//添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, getResources().getString(R.string.weixin_app_id), getResources().getString(R.string.weixin_app_secret));
		wxHandler.addToSocialSDK();
		//添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, getResources().getString(R.string.weixin_app_id), getResources().getString(R.string.weixin_app_secret));
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		//参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "1104712799", "XLcPkc0uJPbEJz1a");
		qqSsoHandler.addToSocialSDK();
		
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setTargetUrl(getShareTargetUrl());
		if(type != Configs.UmengShare_App){
			weixinContent.setTitle(mShareObj.optString("name"));
			weixinContent.setShareContent(getShareContent(mShareObj, false));
			if(mShareObj.has("image")){
				weixinContent.setShareImage(new UMImage(context, mShareObj.optString("image")));
			}
			else{
				weixinContent.setShareImage(new UMImage(context, R.drawable.icon_shareapp));
			}
		}
		else{
			weixinContent.setTitle(getResources().getString(R.string.app_name));
			weixinContent.setShareContent(getResources().getString(R.string.umeng_share_pk_app));
			weixinContent.setShareImage(new UMImage(context, R.drawable.icon_shareapp));
		}
		umSocialServiceShare.setShareMedia(weixinContent);

		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setTargetUrl(getShareTargetUrl());
		if(type != Configs.UmengShare_App){
			circleMedia.setTitle(mShareObj.optString("name"));
			circleMedia.setShareContent(getShareContent(mShareObj, false));
			if(mShareObj.has("image")){
				circleMedia.setShareImage(new UMImage(context, mShareObj.optString("image")));
			}
			else{
				circleMedia.setShareImage(new UMImage(context, R.drawable.icon_shareapp));
			}
		}
		else{
			weixinContent.setTitle(getResources().getString(R.string.app_name));
			weixinContent.setShareContent(getResources().getString(R.string.umeng_share_pk_app));
			weixinContent.setShareImage(new UMImage(context, R.drawable.icon_shareapp));
		}
		umSocialServiceShare.setShareMedia(circleMedia);
		
		SinaShareContent sina = new SinaShareContent();
		if(type != Configs.UmengShare_App){
			sina.setShareContent(String.format(getResources().getString(R.string.umeng_sina_share_title), mShareObj.optString("name"))
			+ getShareContent(mShareObj, true)
			+ getShareTargetUrl());
			if(mShareObj.has("image")){
				sina.setShareImage(new UMImage(context, mShareObj.optString("image")));
			}
			else{
				sina.setShareImage(new UMImage(context, R.drawable.icon_shareapp));
			}
		}
		else{
			sina.setShareContent(getResources().getString(R.string.umeng_share_pk_app)
					+ getShareTargetUrl());
			sina.setShareImage(new UMImage(context, R.drawable.icon_shareapp));
		}
		umSocialServiceShare.setShareMedia(sina);
		
		QQShareContent qqshare = new QQShareContent();
		qqshare.setTargetUrl(getShareTargetUrl());
		if(type != Configs.UmengShare_App){
			qqshare.setTitle(mShareObj.optString("name"));
			qqshare.setShareContent(getShareContent(mShareObj, false));
			if(mShareObj.has("image")){
				qqshare.setShareImage(new UMImage(context, mShareObj.optString("image")));
			}
			else{
				qqshare.setShareImage(new UMImage(context, R.drawable.icon_shareapp));
			}
		}
		else{
			qqshare.setTitle(getResources().getString(R.string.app_name));
			qqshare.setShareContent(getResources().getString(R.string.umeng_share_pk_app));
			qqshare.setShareImage(new UMImage(context, R.drawable.icon_shareapp));
		}
		umSocialServiceShare.setShareMedia(qqshare);

		SmsShareContent sms = new SmsShareContent();
		if(type != Configs.UmengShare_App){
			sms.setShareContent(mShareObj.optString("name") + "\n\n" + getShareContent(mShareObj, false) + "\n" + getShareTargetUrl());
		}
		else{
			sms.setShareContent(getResources().getString(R.string.app_name) + "\n\n" + getResources().getString(R.string.umeng_share_pk_app) + "\n" + getShareTargetUrl());
		}
		umSocialServiceShare.setShareMedia(sms);
	}
	
	public void setUmengShareParams(String resourceType, String resourceId, String toUserId){
		mResourceType = resourceType;
		mResourceId = resourceId;
		mToUserId = toUserId;
	}
	
	public void setShareToUserid(String toUserId){
		mToUserId = toUserId;
	}
	
	public void setUmengShareObj(Context context, JSONObject obj, int type){
		mShareObj = obj;
		mType = type;
		initUmentShare(type);
		initUmentSharePlatform(context, type);
	}
	
	private String getShareTargetUrl(){
		String url = "";
		switch (mType) {
		case Configs.UmengShare_App:
			JSONObject obj = DataLoader.getInstance().getSettingConfig();
			if(obj != null && obj.has("android_download_url")){
				url = obj.optString("android_download_url");
			}
			break;

		default:
			url = Configs.ShareTargetUrl + "resourceType=" + mResourceType +"&resourceId=" + mResourceId;
			break;
		}
		return url;
	}
	
	private String getShareContent(JSONObject obj, boolean isSinaShare){
		String content = mShareObj.has("description")? mShareObj.optString("description") : 
						(mShareObj.has("introduction")? mShareObj.optString("introduction") : 
						(mShareObj.has("content")? mShareObj.optString("content") : 
							(mShareObj.has("intro")? mShareObj.optString("intro") : "")));
		if(content.length() > 200){
			content = content.substring(0, 199) + "...";
		}
		
		if(isSinaShare && content.length() > 90){
			content = content.substring(0, 89) + "...";
		}
		return content;
	}
	
	private SnsPostListener mSnsPostListener = new SnsPostListener() {
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
			// TODO Auto-generated method stub
			if(arg1 != 200){
				return;
			}
			String share ="";
			switch (arg0) {
			case SINA:
			case TENCENT:
				share = "weibo";
				shareOutstation();
				break;
			case WEIXIN:
			case WEIXIN_CIRCLE:
				share = "weixin";
				break;
			case EMAIL:
				share = "email";
				shareOutstation();
				break;
			case SMS:
				share = "sms";
				shareOutstation();
				break;
			}
			
			/**
			 * operation	是	integer	4:站内分享，5：站外分享
				resourceType		String	资源类型，站内分享时必须提供该字段
				resourceId		Long	资源id，站内分享时必须提供该字段
				toUserId		long	目标用户id，站内分享时必须提供该字段
			 */
			
		}
	};
	
	/**
	 * 站内分享
	 */
	public void shareInstation(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("operation", 4);
		params.put("resourceType", mResourceType);
		params.put("resourceId", mResourceId);
		params.put("toUserId", mToUserId);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserShare, params, HSESchoolApp.this);
	}
	
	/**
	 * 站外分享
	 */
	public void shareOutstation(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("operation", 5);
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_UserShare, params, HSESchoolApp.this);
	}
	
	/**
	* 退出指定名字的activity
	*/
	public void removeActivityByName(String name) {
		for(int i = 0; i < activitys.size(); i ++){
			Activity activity = activitys.get(i);
			
			if (null == activity) {
				break;
			}

			String activityName = activity.getComponentName().getClassName().toString();
			if (TextUtils.equals(name, activityName)) {
				 if (activity != null) {
					   activity.finish();
				 }
			}
		}
	}
	
	public void addActivity(Activity activity) {
		if(activitys == null){
			activitys = new LinkedList<Activity>();
		}
		
        if(activitys != null && activitys.size() > 0){
            if(!activitys.contains(activity)){
                activitys.add(activity);
            }
        }
        else{
            activitys.add(activity);
        }
    }
	
	public void removeActivity(Activity activity){
		if(activitys != null && activitys.size() > 0){
            if(activitys.contains(activity)){
                activitys.remove(activity);
            }
        }
	}
	
	public void finishActivityList() {
        if(activitys != null && activitys.size() > 0){
            for(Activity activity : activitys) {
                activity.finish();
            }
        }
    }
	
    public void exit() {
        if(activitys != null && activitys.size() > 0){
            for(Activity activity : activitys) {
                activity.finish();
            }
        }
        System.exit(0);
    }
    
    /**
     * store
     */
    public void addActivityStore(Activity activity) {
		if(activitysStore == null){
			activitysStore = new LinkedList<Activity>();
		}
		
        if(activitysStore != null && activitysStore.size() > 0){
            if(!activitysStore.contains(activity)){
            	activitysStore.add(activity);
            }
        }
        else{
        	activitysStore.add(activity);
        }
    }
	
	public void removeActivityStore(Activity activity){
		if(activitysStore != null && activitysStore.size() > 0){
            if(activitysStore.contains(activity)){
            	activitysStore.remove(activity);
            }
        }
	}
	
	public void finishActivityListStore() {
        if(activitysStore != null && activitysStore.size() > 0){
            for(Activity activity : activitysStore) {
                activity.finish();
            }
        }
    }
    
    /**
     * 程序是否在前台运行
     * 
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        for(RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
               return true;
            }
        }
        return false;
    }

    /**
     * 判断系统中是否存在某个Activity？它是否已经启动？
     * @param cls
     * @return
     */
	protected boolean isActivityStartAndExit(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		ComponentName cmpName = intent.resolveActivity(getPackageManager());
		if (cmpName != null) { // 说明系统中存在这个activity
			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
			for (RunningTaskInfo taskInfo : taskInfoList) {
				if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
					return true;
				}
			}
		}
		return false;
	}
    
	@Override
	public void taskStarted(TaskType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskIsCanceled(TaskType type) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 4-6level share pk
	 */
}
