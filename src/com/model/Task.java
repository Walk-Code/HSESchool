package com.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.android.agoo.client.BaseRegistrar;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.util.Utils;
import com.zhuochuang.hsej.R;

@SuppressLint("SimpleDateFormat")
public class Task extends AsyncTask<String, Void, String> {

	private static Context context;
	private Object mTaskResult = null;
	private TaskListener mTaskListener = null;
	public TaskType taskType = null;
	private HashMap<String, Object> mTaskParams = null;
	public ArrayList<Task> taskContainer;
	private boolean mIsPostType = true;

	public Task(TaskType type, TaskListener listener,
			HashMap<String, Object> params) {
		taskType = type;
		mTaskListener = listener;
		mTaskParams = params;
	}

	public static void setApplicationContext(Context contexts) {
		context = contexts;
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if (mTaskListener != null) {
			mTaskListener.taskIsCanceled(taskType);
		}
		mTaskListener = null;
		if (taskContainer != null) {
			taskContainer.remove(this);
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (mTaskListener != null) {
			mTaskListener.taskFinished(taskType, mTaskResult, false);
		}
		if (taskContainer != null) {
			taskContainer.remove(this);
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (mTaskListener != null) {
			mTaskListener.taskStarted(taskType);
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	/**
	 * User-Agent：<手机/终端型号>/<软件版本号>
	 * (<终端操作系统>;<终端操作系统版本号>;<屏幕尺寸>;<是否破解>;<运营商>;<联网方式>) 如“iPhone
	 * 3GS/1(ios;3.1.2;320x480;s;46000;wifi)” 是否破解：用一个字母表示，s表示没有破 解，p表示破解；
	 * 运营商：因于手机平台不同可能是MCC
	 * +MNC（数字）或运营商英文名称，此部分因为也涉及海外运营商，因此此部分内容为不确认和枚举，但具体某个运营商的名称是固定的；
	 * 联网方式：wifi、mobile
	 */

	@SuppressWarnings("incomplete-switch")
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		if (!Utils.isInternetAvaiable(context)) {
			Error error = new Error(context.getResources().getString(
					R.string.internet_avaiable_false));
			mTaskResult = error;
			return null;
		}

		try {

			String strUrl = getMethodUrl(taskType, mIsPostType ? null
					: mTaskParams);
			System.out.println("strUrl====" + strUrl);

			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(Configs.TimeOut * 1000);
			con.setReadTimeout(Configs.TimeOut * 1000);
			con.setRequestProperty("User-Agent", Utils.getUserAgent(context));

			String userId = SharedPreferenceHandler.getXPSUserId(context);
			if (userId != null && userId.length() > 0) {
				con.setRequestProperty("XPS-UserId", userId);
			}
			String token = SharedPreferenceHandler.getXPSToken(context);
			if (token != null && token.length() > 0) {
				con.setRequestProperty("XPS-Token", token);
			}

			String pushId = SharedPreferenceHandler.getUmengRegistrar(context);
			if (pushId == null || pushId.length() == 0) {
				pushId = BaseRegistrar.getRegistrationId(context);
				System.out.println("===99=pushId==" + pushId);
				SharedPreferenceHandler.saveUmengRegistrar(context, pushId);
			}
			con.setRequestProperty("XPS-PUSHID", pushId);
			if (mIsPostType) {
				con.setDoOutput(true);
				con.setDoInput(true);
				con.setRequestMethod("POST");
				con.setUseCaches(false);
				con.setInstanceFollowRedirects(true);
				con.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");

				if (mTaskParams != null && mTaskParams.size() > 0) {
					if (taskType == TaskType.TaskOrMethod_OrdersSaveOrders) {
						PrintWriter printWriter = new PrintWriter(
								con.getOutputStream());
						printWriter.print(mTaskParams.get("SubmitOrder"));
						printWriter.flush();
						printWriter.close();
					} else {
						DataOutputStream out = new DataOutputStream(
								con.getOutputStream());
						String paramStr = "";
						for (String key : mTaskParams.keySet()) {
							String paramValue = null;
							Object objectValue = mTaskParams.get(key);
							if (objectValue instanceof String) {
								paramValue = (String) objectValue;
								/*
								 * if(taskType ==
								 * TaskType.TaskOrMethod_OrdersSaveOrders){
								 * PrintWriter printWriter = new
								 * PrintWriter(con.getOutputStream());
								 * printWriter.print(paramValue);
								 * printWriter.flush(); printWriter.close();
								 * //mTaskParams.remove(""); continue; }
								 */
								try {
									paramValue = URLEncoder.encode(paramValue,
											"UTF-8");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								paramStr += String.format("%s=%s&", key,
										paramValue);

							} else {
								paramStr += String.format("%s=%s&", key,
										objectValue);
							}
						}
						out.writeBytes(paramStr);
						out.flush();
						out.close();
					}
				}
			}
			InputStream inputStream = con.getInputStream();
			mTaskResult = parseJSON(inputStream);

			/*
			 * if(sessionId == null || sessionId.length() == 0){ String
			 * cookieVal =con.getHeaderField("Set-Cookie"); // 获取session if
			 * (cookieVal != null) { System.out.println("=====999===");
			 * SharedPreferenceHandler.saveSessionId(context,
			 * cookieVal.substring(0, cookieVal.indexOf(";"))); } }
			 */
			try {
				if (mTaskResult instanceof JSONObject
						&& ((JSONObject) mTaskResult).has("result")
						&& ((JSONObject) mTaskResult).optString("result")
								.equalsIgnoreCase("0")) {
					Error error = new Error(
							((JSONObject) mTaskResult).optString("msg"));
					mTaskResult = error;
					return null;
				}
			} catch (Exception e) {
			}

			switch (taskType) {
			case TaskOrMethod_SettingsGet:
				if (mTaskResult instanceof JSONObject) {
					SharedPreferenceHandler.saveSetting(context,
							((JSONObject) mTaskResult).toString());
					try {
						boolean isNull = false;
						if (Long.parseLong(DataLoader.getInstance()
								.getSettingKey("area")) < SharedPreferenceHandler
								.getDictionaryParamsTime(context, "area")) {
							SharedPreferenceHandler.saveDictionary(context,
									null);
							isNull = true;
						}
						if (!isNull
								&& Long.parseLong(DataLoader.getInstance()
										.getSettingKey("interest")) < SharedPreferenceHandler
										.getDictionaryParamsTime(context,
												"interest")) {
							SharedPreferenceHandler.saveDictionary(context,
									null);
							isNull = true;
						}
						if (!isNull
								&& Long.parseLong(DataLoader.getInstance()
										.getSettingKey("grade")) < SharedPreferenceHandler
										.getDictionaryParamsTime(context,
												"grade")) {
							SharedPreferenceHandler.saveDictionary(context,
									null);
							isNull = true;
						}
						if (!isNull
								&& Long.parseLong(DataLoader.getInstance()
										.getSettingKey("group")) < SharedPreferenceHandler
										.getDictionaryParamsTime(context,
												"group")) {
							SharedPreferenceHandler.saveDictionary(context,
									null);
							isNull = true;
						}
					} catch (Exception e) {
					}
				}
				break;
			case TaskOrMethod_UserLogout:
				SharedPreferenceHandler.removeAllSharedPreference(context);
				break;
			case TaskOrMethod_UserLogin:
				if (mTaskResult instanceof JSONObject) {
					SharedPreferenceHandler.saveLoginResult(context,
							((JSONObject) mTaskResult).toString());
					if (((JSONObject) mTaskResult).has("item")) {
						SharedPreferenceHandler.saveUserInfo(context,
								((JSONObject) mTaskResult)
										.optJSONObject("item").toString());
					}

					SharedPreferenceHandler.saveXPSUserId(context,
							con.getHeaderField("XPS-UserId"));
					SharedPreferenceHandler.saveXPSToken(context,
							con.getHeaderField("XPS-UserToken"));

					LoginParamsItem item = new LoginParamsItem();
					item.userAccount = (String) mTaskParams.get("account");
					item.userPassword = (String) mTaskParams.get("password");
					item.userType = (Integer) mTaskParams.get("sign");
					SharedPreferenceHandler.saveLoginParams(context, item);
				}
				break;
			case TaskOrMethod_SettingGetDictionary:
				if (mTaskResult instanceof JSONObject) {
					try {
						SharedPreferenceHandler.saveDictionary(context,
								((JSONObject) mTaskResult).toString());

						SharedPreferenceHandler.saveDictionaryParamsTime(
								context, Long.parseLong(DataLoader
										.getInstance().getSettingKey("area")),
								"area");
						SharedPreferenceHandler
								.saveDictionaryParamsTime(context, Long
										.parseLong(DataLoader.getInstance()
												.getSettingKey("interest")),
										"interest");
						SharedPreferenceHandler.saveDictionaryParamsTime(
								context, Long.parseLong(DataLoader
										.getInstance().getSettingKey("grade")),
								"grade");
						SharedPreferenceHandler.saveDictionaryParamsTime(
								context, Long.parseLong(DataLoader
										.getInstance().getSettingKey("group")),
								"group");
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				break;
			case TaskOrMethod_UserUntreated:
				if (mTaskResult instanceof JSONObject) {
					SharedPreferenceHandler.saveMsgNotify(context,
							((JSONObject) mTaskResult).toString());
				}
				break;
			}
			inputStream.close();
		} catch (Exception e) {
			Error error = null;
			String msg = e.getMessage();
			if (msg != null && msg.length() > 0
					&& msg.contains(Configs.ServerUrl)) {
				error = new Error(context.getResources().getString(
						R.string.connection_fail));
			} else {
				error = new Error(e.getMessage());
			}
			mTaskResult = error;
		}
		return null;
	}

	private static String getMethodUrl(TaskType type,
			HashMap<String, Object> params) {
		String methodName = null;
		switch (type) {
		case TaskOrMethod_SettingsGet:
			methodName = "settings/get";
			break;
		case TaskOrMethod_UserGetUser:
			methodName = "user/getUser";
			break;
		case TaskOrMethod_UserLogin:
			methodName = "user/login";
			break;
		case TaskOrMethod_UserLogout:
			methodName = "user/logout";
			break;
		case TaskOrMethod_UserRegister:
			methodName = "user/register";
			break;
		case TaskOrMethod_UserGetPhoto:
			methodName = "user/getPhoto";
			break;
		case TaskOrMethod_UserCheckName:
			methodName = "user/checkName";
			break;
		case TaskOrMethod_UserGetCode:
			methodName = "user/getCode";
			break;
		case TaskOrMethod_UserCheckCode:
			methodName = "user/checkCode";
			break;
		case TaskOrMethod_UserResetPwd:
			methodName = "user/resetPwd";
			break;
		case TaskOrMethod_UserSetProfile:
			methodName = "user/setProfile";
			break;
		case TaskOrMethod_UserSetProperties:
			methodName = "user/setProperties";
			break;
		case TaskOrMethod_UserListIntegrals:
			methodName = "user/listIntegrals";
			break;
		case TaskOrMethod_UserShare:
			methodName = "user/share";
			break;
		case TaskOrMethod_UserUntreated:
			methodName = "user/untreated";
			break;
		case TaskOrMethod_UserUpdatePwd:
			methodName = "user/updatePwd";
			break;
		case TaskOrMethod_UserBangdingPhone:
			methodName = "user/bangdingPhone";
			break;

		case TaskOrMethod_MycampusSearch:
			methodName = "mycampus/search";
			break;
		case TaskOrMethod_MycampusMoreServers:
			methodName = "mycampus/moreServers";
			break;
		case TaskOrMethod_MycampusMoreInformations:
			methodName = "mycampus/moreInformations";
			break;
		case TaskOrMethod_MycampusMoreActivities:
			methodName = "mycampus/moreActivities";

			break;
		case TaskOrMethod_CampusListTypes:
			methodName = "campus/listTypes";
			break;
		case TaskOrMethod_CampusListServers:
			methodName = "campus/listServers";
			break;
		case TaskOrMethod_CampusFavorites:
			methodName = "campus/favorites";
			break;
		case TaskOrMethod_ActivityListActivities:
			methodName = "activity/listActivities";
			break;
		case TaskOrMethod_ActivityGetActivity:
			methodName = "activity/getActivity";
			break;
		case TaskOrMethod_MycampusGet:
			methodName = "mycampus/get";
			break;
		case TaskOrMethod_HobbygroupListTypes:
			methodName = "hobbygroup/listTypes";
			break;
		case TaskOrMethod_HobbygroupListHobbyGroups:
			methodName = "hobbygroup/listHobbyGroups";
			break;
		case TaskOrMethod_HobbygroupGetHobbyGroup:
			methodName = "hobbygroup/getHobbyGroup";
			break;
		case TaskOrMethod_HobbygroupListPosts:
			methodName = "hobbygroup/listPosts";
			break;
		case TaskOrMethod_HobbygroupDeletePosts:
			methodName = "hobbygroup/deletePosts";
			break;
		case TaskOrMethod_HobbygroupRepliedPosts:
			methodName = "hobbygroup/repliedPosts";
			break;
		case TaskOrMethod_InformationListInformations:
			methodName = "information/listInformations";
			break;
		case TaskOrMethod_InformationGetInformation:
			methodName = "information/getInformation";
			break;
		case TaskOrMethod_DiscussListDiscussions:
			methodName = "discuss/listDiscussions";
			break;
		case TaskOrMethod_DiscussSendDiscussion:
			methodName = "discuss/sendDiscussion";
			break;
		case TaskOrMethod_DiscussDeleteDiscussion:
			methodName = "discuss/deleteDiscussion";
			break;
		case TaskOrMethod_CommunityListCommunities:
			methodName = "community/listCommunities";
			break;
		case TaskOrMethod_CommunityGetCommunity:
			methodName = "community/getCommunity";
			break;
		case TaskOrMethod_FavoriteApply:
			methodName = "favorite/apply";
			break;
		case TaskOrMethod_FavoriteList:
			methodName = "favorite/list";
			break;
		case TaskOrMethod_HobbygroupSendPost:
			methodName = "hobbygroup/sendPost";
			break;
		case TaskOrMethod_hobbygroupGetPost:
			methodName = "hobbygroup/getPost";
			break;

		case TaskOrMethod_CampusMlhsTypes:
			methodName = "campus/mlhs/types";
			break;
		case TaskOrMethod_MessageClassifiedMsgs:
			methodName = "message/classifiedMsgs";
			break;
		case TaskOrMethod_MessageListAdminMsgs:
			methodName = "message/listAdminMsgs";
			break;
		case TaskOrMethod_MessageListChat:
			methodName = "message/listChat";
			break;
		case TaskOrMethod_MessageSendMsg:
			methodName = "message/sendMsg";
			break;
		case TaskOrMethod_MessageUpdateMsgStatus:
			methodName = "message/updateMsgStatus";
			break;
		case TaskOrMethod_UserListFriends:
			methodName = "user/listFriends";
			break;
		case TaskOrMethod_UserListQuasiFriends:
			methodName = "user/listQuasiFriends";
			break;
		case TaskOrMethod_InformationListTypes:
			methodName = "information/listTypes";
			break;
		case TaskOrMethod_UserApprove:
			methodName = "user/approve";
			break;
		case TaskOrMethod_UserApply:
			methodName = "user/apply";
			break;
		case TaskOrMethod_UserUploadPhoto:
			methodName = "user/uploadPhoto";
			break;
		case TaskOrMethod_UserDeletePhoto:
			methodName = "user/deletePhoto";
			break;
		case TaskOrMethod_MessageListMsgs:
			methodName = "message/listMsgs";
			break;
		case TaskOrMethod_SettingGetDictionary:
			methodName = "setting/getDictionary";
			break;
		case TaskOrMethod_SurveyGetTopic:
			methodName = "survey/getTopic";
			break;
		case TaskOrMethod_SurveySubmit:
			methodName = "survey/submit";
			break;

		case TaskOrMethod_EnrolProgress:
			methodName = "enrol/progress";
			break;
		case TaskOrMethod_EnrolListqa:
			methodName = "enrol/listqa";
			break;
		case TaskOrMethod_CourseListCourse:
			methodName = "course/listCourse";
			break;
		case TaskOrMethod_ReportSaveReport:
			methodName = "report/saveReport";
			break;

		// vote
		case TaskOrMethod_VoteGetVoteTopic:
			methodName = "vote/getVoteTopic";
			break;
		case TaskOrMethod_VoteVoteSubmit:
			methodName = "vote/voteSubmit";
			break;

		// //store
		case TaskOrMethod_MallHomeHome:
			methodName = "mallHome/home";
			break;
		case TaskOrMethod_GoodsTypeListGoodsType:
			methodName = "goodsType/listGoodsType";
			break;
		case TaskOrMethod_ShopShopData:
			methodName = "shop/shopData";
			break;
		case TaskOrMethod_GoodsListGoods:
			methodName = "goods/listGoods";
			break;
		case TaskOrMethod_GoodsGoodsData:
			methodName = "goods/goodsData";
			break;
		case TaskOrMethod_ShoppingCarSaveShoppingCar:
			methodName = "shoppingCar/saveShoppingCar";
			break;
		case TaskOrMethod_ShoppingCarListShoppingCar:
			methodName = "shoppingCar/listShoppingCar";
			break;
		case TaskOrMethod_OrdersSaveOrders:
			methodName = "orders/saveOrders";
			break;
		case TaskOrMethod_UserAddressListUserAddress:
			methodName = "userAddress/listUserAddress";
			break;
		case TaskOrMethod_OrdersListOrders:
			methodName = "orders/listOrders";
			break;
		case TaskOrMethod_UserAddressSaveUserAddress:
			methodName = "userAddress/saveUserAddress";
			break;
		case TaskOrMethod_UserAddressUpdateUserAddress:
			methodName = "userAddress/updateUserAddress";
			break;
		case TaskOrMethod_UserAddressDelUserAddress:
			methodName = "userAddress/delUserAddress";
			break;
		case TaskOrMethod_ShopListShop:
			methodName = "shop/listShop";
			break;
		case TaskOrMethod_HotRecommendlistGoods:
			methodName = "hotRecommend/listGoods";
			break;
		case TaskOrMethod_ShoppingCarDelShoppingCar:
			methodName = "shoppingCar/delShoppingCar";
			break;
		case TaskOrMethod_Shop_ListShopType:
			methodName = "shop/listShopType";
			break;
		case TaskOrMethod_Goods_GoodsEvaluate:
			methodName = "goods/goodsEvaluate";
			break;
		case TaskOrMethod_SecondHandandLost:
			methodName = "secondhandandlost/listSecond";
			break;
		case TaskOrMethod_SecondHandandLostDelete:
			methodName = "secondhandandlost/deleteSecond";
			break;
		case TaskOrMethod_SecondHandandLostPublish:
			methodName = "secondhandandlost/sendSecond";
			break;
		case TaskOrMethod_SecondHandandLostFinish:
			methodName = "secondhandandlost/finishSecond";
			break;
		case TaskOrMethod_SecondHandandLostDetails:
			methodName = "secondhandandlost/getSecond";
			break;
		case TaskOrMethod_RecruitServiceList:
			methodName = "recruit/listRecruit";
			break;
		case TaskOrMethod_RecruitServiceSend:
			methodName = "recruit/sendRecruit";
			break;
		case TaskOrMethod_RecruitServiceDelete:
			methodName = "recruit/deleteRecruit";
			break;
		case TaskOrMethod_RecruitServiceFinish:
			methodName = "recruit/finishRecruit";
			break;
		case TaskOrMethod_RecruitServiceDetails:
			methodName = "recruit/getRecruit";
			break;
		case TaskOrMethod_GetEnglishScore:
			methodName = "thirdparty/getCET";
			break;
		case TaskOrMethod_CommonServerInfo:
			methodName = "common/getCommonServer";
			break;
		case TaskOrMethod_CommonServerSubmit:
			methodName = "common/submitCommonServer";
			break;
		case TaskOrMethod_GetWay:
			methodName = "getway";
			break;
		case TaskOrMethod_OneCardSolutionGetCard:
			methodName = "card/getCard";
			break;
		case TaskOrMethod_OneCardSolutionList:
			methodName = "card/getConsumeOrSave";
			break;
		case TaskOrMethod_OneCardSolutionReportLoss:
			methodName = "card/custLoss";
			break;
		case TaskOrMethod_OrdersGetPayUrl:
			methodName = "orders/getPayUrl";
			break;
		case TaskOrMethod_OrdersEvaluateOrders:
			methodName = "orders/evaluateOrders";
			break;
		case TaskOrMethod_OrdersUpdateOrderStatus:
			methodName = "orders/updateOrderStatus";
			break;

		case TaskOrMethod_CommonGetPayUrl:
			methodName = "common/getPayUrl";
			break;
		case TaskOrMethod_ApplyCommunity:
			methodName = "community/applyCommunity";
			break;
		case TaskOrMethod_CommunityGetStructure:
			methodName = "community/getStructure";
			break;
		case TaskOrMethod_CommunityGetUserStructure:
			methodName = "community/getUserCommunity";
			break;
		case TaskOrMethod_AfterSaleService:
			methodName = "shopInfo/show";
			break;
		default:
			break;
		}
		String url = null;
		if (type == TaskType.TaskOrMethod_GetWay) {
			url = String.format("%s%s", Configs.PayUrl, methodName);
		} else {
			url = String.format("%s%s.do", Configs.ServerUrl, methodName);
		}

		if (methodName == null || methodName.length() == 0 || params == null
				|| params.size() == 0) {
			return url;
		}

		String paramStr = "";
		for (String key : params.keySet()) {
			String paramValue = null;
			Object objectValue = params.get(key);
			if (objectValue instanceof String) {
				paramValue = (String) objectValue;
				try {
					paramValue = URLEncoder.encode(paramValue, "utf-8");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				paramStr += String.format("%s=%s&", key, paramValue);
			} else {
				paramStr += String.format("%s=%s&", key, params.get(key));
			}

		}
		return String.format("%s?%s", url, paramStr);
	}

	public static Object parseJSON(InputStream inputStream) {
		Object obj = null;
		BufferedReader bufferReader = null;
		String bf = null;
		StringBuffer sb = new StringBuffer();
		try {
			bufferReader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"));
			while ((bf = bufferReader.readLine()) != null) {
				sb.append(bf);
			}
			String sbStr = sb.toString();
			if (sbStr != null) {
				if (sbStr.startsWith("[")) {
					obj = new JSONArray(sbStr);
				} else if (sbStr.startsWith("{")) {
					obj = new JSONObject(sbStr);
				} else if (sbStr.startsWith("<")) {
					obj = sbStr;
				} else {
					Error error = new Error(sbStr);
					obj = error;
				}
			}
			System.out.println("result_sbstring===" + sbStr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Error error = new Error(e.getMessage());
			obj = error;
		} catch (Error error) {
			// TODO: handle exception
		}
		return obj;
	}

}
