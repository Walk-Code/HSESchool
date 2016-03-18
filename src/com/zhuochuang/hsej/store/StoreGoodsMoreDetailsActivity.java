package com.zhuochuang.hsej.store;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.photoview.PhotoViewer;
import com.model.Configs;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.model.TaskType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.AutoGallery;
import com.util.ContentAdapter;
import com.util.PageGuide;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.ContactListActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.LoginActivity;
import com.zhuochuang.hsej.PostDetailsActivity;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.store.StickyScrollView.OnScrollChangedListener;
import com.zhuochuang.hsej.store.StorePopupWindowToShopCar.OnPopupWindowsClickListener;

public class StoreGoodsMoreDetailsActivity extends BaseActivity {
	private AutoGallery mAutoGallery;
	private PageGuide mPageGuide;
	private StorePopupWindowToShopCar mPopupWindowToShopCar;
	private JSONArray mImagesArr;
	private JSONArray mCommentArr;
	private JSONObject mItems;
	private LinearLayout mCommentLayout;
	private CustomEvaluateLayout[] mCustomEvaluateLayout;
	private PopupWindow popWindow;
	private ContentAdapter mAdapter;
	private Handler mHandler;
	private boolean lock;
	private String mColor;
	private String mSize;
	public static boolean isBuy = false;
	private int mCollect;
	private int shopCarCount = 0;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((HSESchoolApp)getApplication()).addActivityStore(this);
		mMainLayout.removeAllViews();
		setContentView(R.layout.store_goods_details);
		showDialogCustom(DIALOG_CUSTOM);
		init();

		// 商品ID
		DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_GoodsGoodsData, DataLoader.getInstance().getGoodsDataTypeParams(
						getIntent().getStringExtra("shopId")), this);
		EventManager.getInstance().setHandlerListenner(mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Object[] obj = (Object[]) msg.obj;
				switch (msg.what) {
				case EventManager.EventType_UpdateUI:
					if (obj != null && obj.length > 1) {
						mColor = (String) obj[1];
						mSize = (String) obj[2];
						((TextView) findViewById(R.id.goods_choose)).setText(getResources().getString(R.string.goods_choose)
								+ ": " + (String) obj[1] + " " + mSize);

						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (null != popWindow && popWindow.isShowing()) {
									popWindow.dismiss();
								}
							}
						}, 1000);
					}
					if (isBuy) {
						isBuy = false;
						showDialogCustom(DIALOG_CUSTOM);
						DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_ShoppingCarSaveShoppingCar,
							DataLoader.getInstance().setsaveShoppingCarTypeParams(
											1, mColor, mSize, mItems.optString("price"), mItems.optString("id"), false),
							StoreGoodsMoreDetailsActivity.this);
					}
					break;
				default:
					break;
				}
			}
		});
	}

	private void init() {
		mAutoGallery = (AutoGallery) findViewById(R.id.gallery_banner);
		mPageGuide = (PageGuide) findViewById(R.id.pageguide);
		mCommentLayout = (LinearLayout) findViewById(R.id.store_comment_layout);
		
		// findViewById(R.id.viewGroups).getBackground().setAlpha(0);
		mAutoGallery.setAdapter(mAdapter = new ContentAdapter() {
			@Override
			public int getCount() {
				if (mImagesArr == null || mImagesArr.length() == 0) {
					return 0;
				}
				return Integer.MAX_VALUE;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// 判断并获取图片的链接数
				if (position >= mImagesArr.length()) {
					position = position % mImagesArr.length();
				}
				if (convertView == null) {
					convertView = LayoutInflater.from(
							StoreGoodsMoreDetailsActivity.this).inflate(
							R.layout.store_page_item, null);
					convertView.setLayoutParams(new Gallery.LayoutParams(
							Utils.getScreenWidth(StoreGoodsMoreDetailsActivity.this),
							Utils.getScreenHeight(StoreGoodsMoreDetailsActivity.this) / 3 * 2));

				}
				JSONObject obj = mImagesArr.optJSONObject(position);
				ImageLoader.getInstance().displayImage(obj.optString("path"),
						((ImageView) convertView.findViewById(R.id.images)),
						ImageLoaderConfigs.displayImageOptionsBg);
				return convertView;
			}
		});

		mAutoGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= mImagesArr.length()) {
					position = position % mImagesArr.length();
				}
				mPageGuide.setSelect(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		mAutoGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(mImagesArr == null || mImagesArr.length() == 0){
					return;
				}
				if (position >= mImagesArr.length()) {
					position = position % mImagesArr.length();
				}
				PhotoViewer photoViewer = new PhotoViewer(StoreGoodsMoreDetailsActivity.this, position);
				photoViewer.setPathArr(mImagesArr);
				photoViewer.showPhotoViewer(view);
			}
		});

		findViewById(R.id.details_back).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						StoreGoodsMoreDetailsActivity.this.finish();

					}
				});

		findViewById(R.id.details_store_car).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (DataLoader.getInstance().isLogin()) {
							Intent intent = new Intent(
									StoreGoodsMoreDetailsActivity.this,
									StoreShoppingCarActivity.class);
							startActivity(intent);
						} else {
							StoreGoodsMoreDetailsActivity.this
									.startActivity(new Intent(
											StoreGoodsMoreDetailsActivity.this,
											LoginActivity.class));
							return;
						}
					}
				});

		findViewById(R.id.details_share).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO action share
						if (mItems == null
								|| ((HSESchoolApp) getApplicationContext()).umSocialServiceShare == null) {
							return;
						}
						if (mItems != null
								&& ((HSESchoolApp) getApplicationContext()).umSocialServiceShare != null) {
							((HSESchoolApp) getApplicationContext())
									.setUmengShareObj(
											StoreGoodsMoreDetailsActivity.this,
											mItems,
											Configs.UmengShare_Infomation);
							((HSESchoolApp) getApplicationContext()).umSocialServiceShare
									.openShare(
											StoreGoodsMoreDetailsActivity.this,
											false);
							((HSESchoolApp) getApplicationContext())
									.setOnFavoriteClick(new OnClickListener() {

										@Override
										public void onClick(View v) {
											String myId = DataLoader
													.getInstance()
													.getHeaderUsetId();
											if (myId == null
													|| myId.length() == 0) {
												Toast.makeText(
														StoreGoodsMoreDetailsActivity.this,
														getResources()
																.getString(
																		R.string.login_notify),
														Toast.LENGTH_SHORT)
														.show();
												startActivity(new Intent(
														StoreGoodsMoreDetailsActivity.this,
														LoginActivity.class));
												overridePendingTransition(
														R.anim.push_bottom_in,
														R.anim.push_bottom_out);
												return;
											}

											if (mItems.has("favoriteStatus")
													&& mItems.optString(
															"favoriteStatus")
															.equalsIgnoreCase(
																	"0")) {
												Toast.makeText(
														StoreGoodsMoreDetailsActivity.this,
														getResources()
																.getString(
																		R.string.favorite_already),
														Toast.LENGTH_SHORT)
														.show();
												return;
											}

											showDialogCustom(DIALOG_CUSTOM);
											HashMap<String, Object> params = new HashMap<String, Object>();
											params.put("resourceType", "15");
											params.put("resourceIds",
													mItems.optString("id"));
											params.put("statuses", "0");
											DataLoader
													.getInstance()
													.startTaskForResult(
															TaskType.TaskOrMethod_FavoriteApply,
															params,
															StoreGoodsMoreDetailsActivity.this);
										}
									});
							((HSESchoolApp) getApplicationContext())
									.setOnFriendClick(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											String myId = DataLoader
													.getInstance()
													.getHeaderUsetId();
											if (myId == null
													|| myId.length() == 0) {
												Toast.makeText(
														StoreGoodsMoreDetailsActivity.this,
														getResources()
																.getString(
																		R.string.login_notify),
														Toast.LENGTH_SHORT)
														.show();
												startActivity(new Intent(
														StoreGoodsMoreDetailsActivity.this,
														LoginActivity.class));
												overridePendingTransition(
														R.anim.push_bottom_in,
														R.anim.push_bottom_out);
												return;
											}
											startActivity(new Intent(
													StoreGoodsMoreDetailsActivity.this,
													ContactListActivity.class));
										}
									});
						}
					}
				});


		findViewById(R.id.details_go_store).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mItems.optString("shopId") != null) {
							Intent intent = new Intent(
									StoreGoodsMoreDetailsActivity.this,
									StorePersonActivity.class);
							intent.putExtra("shopId",
									mItems.optString("shopId"));
							startActivity(intent);
						} else {
							Toast.makeText(
									StoreGoodsMoreDetailsActivity.this,
									getResources().getString(
											R.string.connection_fail),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		// 购物车
		mPopupWindowToShopCar = new StorePopupWindowToShopCar(
				StoreGoodsMoreDetailsActivity.this);
		mPopupWindowToShopCar.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
				// mAutoGallery.setDuration(4000);
			}
		});

		mPopupWindowToShopCar.setOnPopupWindowOnClickListener(new OnPopupWindowsClickListener() {
			@Override
			public void onPopupWindowItemClick(int position) {
				// ((TextView)findViewById(R.id.goods_choose)).setText(mItems.optString("colour").split(",")[position]+" "+(mItems.optString("spec").split(","))[position]);

			}
		});

		findViewById(R.id.details_add_car).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 停止广告轮播
						if (DataLoader.getInstance().isLogin()) {
							if (0 == ((TextView) findViewById(R.id.goods_choose))
									.getText().toString().length()) {
								backgroundAlpha(0.8f);
								if (mPopupWindowToShopCar != null) {
									mPopupWindowToShopCar
											.showAtLocation(
													findViewById(R.id.parent_layout),
													Gravity.BOTTOM
															| Gravity.CENTER_HORIZONTAL,
													0, 0);
								}
							} else {
								if (TextUtils.isEmpty(mColor)
										|| TextUtils.isEmpty(mSize)) {
									isBuy = true;
									mPopupWindowToShopCar
											.showAtLocation(
													findViewById(R.id.parent_layout),
													Gravity.BOTTOM
															| Gravity.CENTER_HORIZONTAL,
													0, 0);
									return;
								}
								showDialogCustom(DIALOG_CUSTOM);
								DataLoader
										.getInstance()
										.startTaskForResult(
												TaskType.TaskOrMethod_ShoppingCarSaveShoppingCar,
												DataLoader
														.getInstance()
														.setsaveShoppingCarTypeParams(
																1,
																mColor,
																mSize,
																mItems.optString("price"),
																mItems.optString("id"), false),
												StoreGoodsMoreDetailsActivity.this);
								// showCarNotifyDialog();
								/*EventManager
										.getInstance()
										.sendMessage(
												EventManager.getInstance().EventType_UpdateUI);*/
							}
						} else {
							StoreGoodsMoreDetailsActivity.this
									.startActivity(new Intent(
											StoreGoodsMoreDetailsActivity.this,
											LoginActivity.class));
							overridePendingTransition(
											R.anim.push_bottom_in,
											R.anim.push_bottom_out);
							return;
						}
					}
				});

		findViewById(R.id.store_check_evaluate).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mItems != null) {
							Intent intent = new Intent(StoreGoodsMoreDetailsActivity.this, StoreGoodsCommentListActivity.class);
							intent.putExtra("goodsId", mItems.optString("id"));
							//intent.putExtra("test", mCommentArr.toString());
							startActivity(intent);
						}
					}
				});

		findViewById(R.id.goods_collection).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!DataLoader.getInstance().isLogin()) {
					startActivity(new Intent(StoreGoodsMoreDetailsActivity.this, LoginActivity.class));
					overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
					return;
				}
				showDialogCustom(DIALOG_CUSTOM);
				if (mItems != null && mItems.has("favoriteStatus") && mItems.optString("favoriteStatus").equalsIgnoreCase("0")) {
					mCollect = 1;
				}
				else {
					mCollect = 0;
				}
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_FavoriteApply,
					DataLoader.getInstance().setFavoriteTypeParams("15", mItems.optString("id"), mCollect +""), StoreGoodsMoreDetailsActivity.this);
			}
		});

		findViewById(R.id.store_goods_choose).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindowToShopCar.showAtLocation(findViewById(R.id.parent_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});

		if (DataLoader.getInstance().isLogin() && 0 != getIntent().getIntExtra("shoppingCarCount", 0)) {
			findViewById(R.id.details_goods_count).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.details_goods_count)).setText(getIntent().getIntExtra("shoppingCarCount", 0) + "");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventManager.getInstance().removeHandlerListenner(mHandler);
		((HSESchoolApp)getApplication()).removeActivityStore(this);
	}

	private void showCarNotifyDialog() {
		View view = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.store_car_hint, null);
		view.findViewById(R.id.store_car_hints).getBackground().setAlpha(200);
		popWindow = new PopupWindow(view, Utils.getScreenWidth(this) / 3 * 2,
				Utils.getScreenHeight(this) / 5, true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOutsideTouchable(true);
		popWindow.showAtLocation(view.findViewById(R.id.store_car_hints),
				Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (null != popWindow && popWindow.isShowing()) {
					popWindow.dismiss();
				}
			}
		}, 1000);
	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		if (result == null) {
			removeDialogCustom();
			return;
		}

		if (result instanceof Error) {
			removeDialogCustom();
			Toast.makeText(this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}

		switch (type) {
		case TaskOrMethod_GoodsGoodsData:
			if (result instanceof JSONObject) {
				if (((JSONObject) result).has("imageList")) {
					mImagesArr = ((JSONObject) result).optJSONArray("imageList");
					if (mImagesArr != null && mImagesArr.length() > 0) {
						mPageGuide.setParams(mImagesArr.length(), Utils.dipToPixels(StoreGoodsMoreDetailsActivity.this, 7),
								Utils.dipToPixels(StoreGoodsMoreDetailsActivity.this, 7));
						mAutoGallery.setSelection(mImagesArr.length() * 1000000);
						mAutoGallery.setLength(mImagesArr.length());
						mAutoGallery.setDuration(4000);
						mAutoGallery.setAutoScroll();

						if (mAutoGallery != null && mAutoGallery.getAdapter() != null) {
							((ContentAdapter) mAutoGallery.getAdapter()).notifyDataSetChanged();
						}
					}
					if (mAutoGallery != null && mAutoGallery.getAdapter() != null) {
						((ContentAdapter) mAutoGallery.getAdapter()).notifyDataSetChanged();
					}
				}
				if (((JSONObject) result).has("items")) {
					mItems = ((JSONObject) result).optJSONObject("items");
					((HSESchoolApp) getApplicationContext()).setUmengShareParams(getIntent().getIntExtra("resouceType", 15) + "",
									getIntent().getStringExtra("shopId"), "");
					((TextView) findViewById(R.id.details_goods)).setText(mItems.optString("name"));
					((TextView) findViewById(R.id.details_price)).setText(getResources().getString(
									R.string.money_sigh) + new DecimalFormat("#.00").format(mItems.optDouble("price")));
					if (0 != mItems.optDouble("money")) {
						((TextView) findViewById(R.id.details_mark)).setText(getResources().getString(R.string.money_sigh)
										+ new DecimalFormat("#.00").format(mItems.optDouble("money")));
						((TextView) findViewById(R.id.details_mark)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					}
					/*
					 * if(mItems.optDouble("price")){ ((TextView)
					 * findViewById(R.
					 * id.details_mark)).setText(getResources().getString
					 * (R.string.money_sigh)+new
					 * DecimalFormat("#.00").format(mItems.optDouble("price")));
					 * }
					 */
					((TextView) findViewById(R.id.detail_stock)).setText(getResources().getString(R.string.goods_already_sold)+ mItems.optString("sales") + getResources().getString(R.string.goods_inventory2));
					WebView content = (WebView) findViewById(R.id.goods_show);
					content.loadDataWithBaseURL("", mItems.optString("content"), "text/html", "UTF-8", "");
					if (mItems != null && (0 < (mItems.optString("colour").length())) && (0 < (mItems.optString("spec").length()))) {
						mPopupWindowToShopCar.changeData(Arrays.asList(mItems.optString("colour").split(",")), Arrays.asList((mItems.optString("spec").split(","))),
								mImagesArr.optJSONObject(0).optString("path"),
								mItems.optString("price"), mItems.optString("shopId"), mItems.optInt("inventory"));
						if(mItems.optInt("inventory") <= 0){
							findViewById(R.id.details_add_car).setBackgroundColor(Color.parseColor("#9f9f9f"));
							findViewById(R.id.details_add_car).setEnabled(false);
						}
					}
					else {
						mPopupWindowToShopCar.changeData(null, null, mImagesArr.optJSONObject(0).optString("path"), mItems.optString("price"),
								mItems.optString("shopId"), mItems.optInt("inventory"));
					}

					if(mItems.optString("favoriteStatus").equalsIgnoreCase("0")) {
						findViewById(R.id.views).setBackgroundResource(R.drawable.store_icon_collect_pre);
					}
					else {
						findViewById(R.id.views).setBackgroundResource(R.drawable.store_icon_collect);
					}
					if(mItems.has("shop")){
						JSONObject shopObj = mItems.optJSONObject("shop");
						if(shopObj != null && shopObj.has("deliver")){
							String deliver = shopObj.optString("deliver");
							if(!Utils.isTextEmpty(deliver) && deliver.equalsIgnoreCase("1")){
								findViewById(R.id.group_deliver_0).setVisibility(View.GONE);
								findViewById(R.id.group_deliver_1).setVisibility(View.VISIBLE);
							}
							else if(!Utils.isTextEmpty(deliver) && deliver.equalsIgnoreCase("0")){
								findViewById(R.id.group_deliver_1).setVisibility(View.GONE);
								findViewById(R.id.group_deliver_0).setVisibility(View.VISIBLE);
							}
							else{
								findViewById(R.id.group_deliver_1).setVisibility(View.VISIBLE);
								findViewById(R.id.group_deliver_0).setVisibility(View.VISIBLE);
							}
						}
					}
				}
				if(((JSONObject) result).has("comment")) {
					((TextView) findViewById(R.id.goods_evaluate)).setText(getResources().getString(R.string.stores_goods_evaluate) + "(" + ((JSONObject)result).optString("total") + ")");
					mCommentArr = ((JSONObject) result).optJSONArray("comment");
					if (mCommentArr.length() == 0) {
						/*((TextView) findViewById(R.id.store_comtent_null)).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (!DataLoader.getInstance().isLogin()) {
									StoreGoodsMoreDetailsActivity.this.startActivity(new Intent(
													StoreGoodsMoreDetailsActivity.this,LoginActivity.class));
									overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
									return;
								}
							}
						});*/
						findViewById(R.id.store_check_evaluate).setVisibility(View.GONE);
					}
					else {
						findViewById(R.id.goods_comment_layout).setVisibility(View.VISIBLE);
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1);
						mCustomEvaluateLayout = new CustomEvaluateLayout[mCommentArr.length()];
						mCommentLayout.removeAllViews();
						JSONObject obj = mCommentArr.optJSONObject(0);
						mCustomEvaluateLayout[0] = new CustomEvaluateLayout(getApplicationContext());
						mCustomEvaluateLayout[0].setClient(obj.optString("nickName"));
						mCustomEvaluateLayout[0].setComtent(obj.optString("content"));
						mCustomEvaluateLayout[0].setComment_time(obj.optLong("createDate"));
						mCustomEvaluateLayout[0].setEvaluateStar(obj.optInt("evaluate"));
						mCommentLayout.addView(mCustomEvaluateLayout[0], layoutParams);
						mCustomEvaluateLayout[0].setPicList(mCommentArr.optJSONObject(0).optJSONArray("images"));
					}
				}
			}
			findViewById(R.id.details_scrollview).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_boom).setVisibility(View.VISIBLE);
			removeDialogCustom();
			if(DataLoader.getInstance().isLogin()){
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_ShoppingCarListShoppingCar, null, this);
			}
			break;

		case TaskOrMethod_ShoppingCarSaveShoppingCar:
			if (((JSONObject) result).has("result")) {
				int count = ((JSONObject) result).optInt("count", 0);
				if (count > 0) {
					findViewById(R.id.details_goods_count).setVisibility(
							View.VISIBLE);
					((TextView) findViewById(R.id.details_goods_count)).setText(count +"");
				}
				else {
					findViewById(R.id.details_goods_count).setVisibility(View.GONE);
				}
				showCarNotifyDialog();
				DataLoader.getInstance().startTaskForResult(
						TaskType.TaskOrMethod_ShoppingCarListShoppingCar, null, this);
			}
			else{
				removeDialogCustom();
			}
			break;
		case TaskOrMethod_ShoppingCarListShoppingCar:
			removeDialogCustom();
			if (((JSONObject) result).has("result")) {
				try {
					JSONArray jsonArray = ((JSONObject) result).optJSONArray("items");
					shopCarCount=0;
					if(jsonArray != null && jsonArray.length() > 0){
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.optJSONObject(i);
							JSONArray array = object.getJSONArray("goods");
							shopCarCount += array.length();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					shopCarCount=0;
				}
			}
			if (shopCarCount > 0) {
				findViewById(R.id.details_goods_count).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.details_goods_count)).setText(shopCarCount + "");
			}
			else {
				findViewById(R.id.details_goods_count).setVisibility(View.GONE);
			}
			EventManager.getInstance().sendMessage(EventManager.getInstance().EventType_ShoppingCar, shopCarCount);
			break;
		case TaskOrMethod_FavoriteApply:
			removeDialogCustom();
			try {
				if(mItems != null && mItems.has("favoriteStatus") && mItems.optString("favoriteStatus").equalsIgnoreCase("0")){
					mItems.put("favoriteStatus", "1");
					findViewById(R.id.views).setBackgroundResource(R.drawable.store_icon_collect);
				}
				else{
					mItems.put("favoriteStatus", "0");
					findViewById(R.id.views).setBackgroundResource(R.drawable.store_icon_collect_pre);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(DataLoader.getInstance().isLogin()){
			DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_ShoppingCarListShoppingCar, null, this);
		}
	}
}
