package com.zhuochuang.hsej.store;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskType;
import com.model.data.BaseInfo;
import com.model.data.GroupInfo;
import com.model.data.ProductInfo;
import com.util.Utils;
import com.zhuochuang.hsej.BaseActivity;
import com.zhuochuang.hsej.HSESchoolApp;
import com.zhuochuang.hsej.MainActivity;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.StartupActivity;
import com.zhuochuang.hsej.store.ShopCartListViewAdapter.CheckInterface;
import com.zhuochuang.hsej.store.ShopCartListViewAdapter.ModifyCountInterface;

@SuppressLint("ResourceAsColor")
public class StoreShoppingCarActivity extends BaseActivity {
	private ShopCartListViewAdapter mAdapter;
	private PullToRefreshListView mListView;
	private TextView mTotalPrice;
	private TextView mGoodCount;
	private boolean lock_edit = true;
	private JSONArray mItemsArr;
	private PopupWindow popWindow;
	private List<Map<String, Object>> mDatasCar;
	private List<GroupInfo> mGroups = new ArrayList<GroupInfo>();
	private Map<String, List<ProductInfo>> mChildren = new HashMap<String, List<ProductInfo>>();
	private double totalPrice;// 总价
	private int totalCount;
	private CheckBox mSelectAll;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		((HSESchoolApp) getApplication()).addActivityStore(this);
		mMainLayout.removeAllViews();
		setContentView(R.layout.store_shopping_car);
		initView();
		mListView.startRefresh();
	}

	private void initView() {
		mListView = (PullToRefreshListView) findViewById(R.id.pullToListView_shoppingCar);
		mAdapter = new ShopCartListViewAdapter(mDatasCar, StoreShoppingCarActivity.this);
		mListView.setAdapter(mAdapter);
		mTotalPrice = (TextView) findViewById(R.id.total_price);
		mDatasCar = new ArrayList<Map<String, Object>>();
		mGoodCount = (TextView) findViewById(R.id.but_pay);
		mSelectAll = (CheckBox) findViewById(R.id.select_all);
		mGoodCount.setText(getResources().getString(R.string.goods_pay));
		mTotalPrice.setText(getResources().getString(R.string.money_sigh) + "0.00");
		findViewById(R.id.but_pay).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONArray shoppingCarArr = new JSONArray();
				if (0 == totalCount){
					return;
				}
				for(int i = 0; i < mGroups.size(); i++) {
					GroupInfo info = mGroups.get(i);
					double money = 0;
					int totality = 0;
					JSONObject groupObj = new JSONObject();
					JSONArray goodsArr = new JSONArray();
					try {
						String deliverType = info.getDeliver();
						groupObj.put("shopId", info.getId());
						groupObj.put("shopName", info.getName());
						groupObj.put("deliverFee", info.getDeliverFee());
						groupObj.put("deliver", deliverType);
						double deliverFee = info.getDeliverFee();
						List<ProductInfo> childs = mChildren.get(info.getId());
						for (int j = 0; j < childs.size(); j++) {
							ProductInfo pinfo = childs.get(j);
							if (pinfo != null && pinfo.isChoosed()) {
								if(pinfo.getCount() > pinfo.getInventory()){
									new AlertDialog.Builder(StoreShoppingCarActivity.this) 
									.setCancelable(false)
								 	.setMessage(String.format(getResources().getString(R.string.shopcar_above_inventory_pay), pinfo.getName(), pinfo.getInventory() +""))
								 	.setPositiveButton(getResources().getString(R.string.confirm), null)
								 	.show();
									return;
								}
								JSONObject goodsObj = new JSONObject();
								goodsObj.put("goodsId", pinfo.getId());
								goodsObj.put("goodsName", pinfo.getName());
								goodsObj.put("colour", pinfo.getColor());
								goodsObj.put("spec", pinfo.getSize());
								goodsObj.put("price", pinfo.getPrice());
								goodsObj.put("number", pinfo.getCount());
								goodsObj.put("shoppingCarId", pinfo.getShopCarId());
								goodsObj.put("image", pinfo.getImageUrl());
								goodsArr.put(goodsObj);
								money += (double)pinfo.getPrice() * (double)pinfo.getCount();
								totality += pinfo.getCount();
							}
						}
						
						groupObj.put("goods", goodsArr);
						if(!Utils.isTextEmpty(deliverType) && deliverType.equalsIgnoreCase("1")){
							groupObj.put("money", (double)money);
						}
						else{
							groupObj.put("money", (double)money + (double)deliverFee);
						}
						
						groupObj.put("totality", totality);
						groupObj.put("address", info.getAddress());
						if (0 == money) {
							continue;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					shoppingCarArr.put(groupObj);
				}
				if (0 < shoppingCarArr.length()) {
					Intent intent = new Intent(StoreShoppingCarActivity.this, StoreCheckOrderActivity.class);
					intent.putExtra("ShoppingCar", shoppingCarArr.toString());
					intent.putExtra("money", totalPrice);
					startActivity(intent);
				}
			}
		});

		findViewById(R.id.store_shopping_edit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lock_edit) {
					((TextView) findViewById(R.id.store_shopping_edit)).setText(getResources().getString(R.string.photoalbum_done));
					((TextView) findViewById(R.id.store_shopping_edit)).setTextColor(Color.RED);
					findViewById(R.id.store_count).setVisibility(View.GONE);
					findViewById(R.id.but_pay).setVisibility(View.GONE);
					findViewById(R.id.store_shopping_collection).setVisibility(View.VISIBLE);
					findViewById(R.id.store_shopping_delete).setVisibility(View.VISIBLE);
					lock_edit = false;
				}
				else {
					((TextView) findViewById(R.id.store_shopping_edit)).setText(getResources().getString(R.string.my_homepage_edit));
					((TextView) findViewById(R.id.store_shopping_edit)).setTextColor(Color.BLACK);
					findViewById(R.id.store_count).setVisibility(View.VISIBLE);
					findViewById(R.id.but_pay).setVisibility(View.VISIBLE);
					findViewById(R.id.store_shopping_collection).setVisibility(View.GONE);
					findViewById(R.id.store_shopping_delete).setVisibility(View.GONE);
					lock_edit = true;
				}
			}
		});

		findViewById(R.id.shopping_car_left_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		findViewById(R.id.store_shopping_delete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mDatasCar == null || mDatasCar.size() == 0) {
					new AlertDialog.Builder(StoreShoppingCarActivity.this)
					.setCancelable(false)
					.setMessage(R.string.store_shopping_car_null)
					.setPositiveButton(getResources().getString(R.string.store_shopping_car_go),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								((HSESchoolApp)getApplication()).finishActivityListStore();
								StoreShoppingCarActivity.this.finish();
							}
						})
					.setNegativeButton(getResources().getString(R.string.store_shopping_car_cancle), null).show();
					return;
				}
				doDelete();
			}
		});

		findViewById(R.id.store_shopping_collection).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String productId = "";
				String statues = "";
				for (int i = 0; i < mGroups.size(); i++) {
					GroupInfo group = mGroups.get(i);
					List<ProductInfo> childs = mChildren.get(group.getId());
					for (int j = 0; j < childs.size(); j++) {
						ProductInfo product = childs.get(j);
						if (product.isChoosed()) {
							productId += product.getId() + ",";
							statues += "0" + ",";
						}
					}
				}
				if(Utils.isTextEmpty(productId)){
					return;
				}
				if (productId.endsWith(",")) {
					productId = productId.substring(0, productId.length() - 1);
				}
				if (statues.endsWith(",")) {
					statues = statues.substring(0, statues.length() - 1);
				}
				showDialogCustom(DIALOG_CUSTOM);
				DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_FavoriteApply,
						DataLoader.getInstance().setFavoriteTypeParams("15", productId, statues), StoreShoppingCarActivity.this);
			}
		});

		mSelectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				doCheckAll();
			}
		});

		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				DataLoader.getInstance().startTaskForResult(
						TaskType.TaskOrMethod_ShoppingCarListShoppingCar, null, StoreShoppingCarActivity.this);
			}
		});
	}

	private void doDelete() {
		List<Map<String, Object>> toBeDelete = new ArrayList<>();
		List<GroupInfo> toBeDeleteGroups = new ArrayList<>();
		int sum = 0;
		// int[] id = new int[mDatasCar.size()];

		/**
		 * modify by kris
		 * 
		 */
		String ids = "";

		if (mGroups != null && mGroups.size() > 0) {
			for (int i = 0; i < mGroups.size(); i++) {
				GroupInfo group = mGroups.get(i);
				List<ProductInfo> childs = mChildren.get(group.getId());
				if (group.isChoosed()) {
					toBeDeleteGroups.add(group);
				}
				List<ProductInfo> toBeDeleteProducts = new ArrayList<>();
				for (int j = 0; j < childs.size(); j++) {
					if (childs.get(j).isChoosed()) {
						toBeDeleteProducts.add(childs.get(j));
						ids += childs.get(j).getShopCarId() + ",";
					}
				}
				childs.removeAll(toBeDeleteProducts);
				sum += childs.size();
			}
		}
		mGroups.removeAll(toBeDeleteGroups);
		for (int i = 0; i < mDatasCar.size(); i++) {
			BaseInfo info = (BaseInfo) mDatasCar.get(i).get(
					ShopCartListViewAdapter.SHOPCART_DATA);
			if (info.isChoosed()) {
				toBeDelete.add(mDatasCar.get(i));
			}
		}
		if (ids != null && ids.length() > 0 && ids.contains(",")) {
			ids = ids.substring(0, ids.length() - 1);
		}
		mDatasCar.removeAll(toBeDelete);
		mAdapter.notifyDataSetChanged();
		calculateAll();
		mAdapter.notifyDataSetChanged();
		DataLoader.getInstance().startTaskForResult(
				TaskType.TaskOrMethod_ShoppingCarDelShoppingCar,
				DataLoader.getInstance().setDelShoppingCarTypeParams(ids),
				StoreShoppingCarActivity.this);
		String size = "";
		if (0 != mGroups.size()) {
			size = mGroups.size() + "";
		}
		((TextView) findViewById(R.id.shopping_car)).setText(getResources()
				.getString(R.string.store_shopping_car)
				+ "(" +(mGroups.size() == 0 ? "" : mGroups.size()) + ")");
	}

	// 全选反选
	private void doCheckAll() {
		for (int i = 0; i < mGroups.size(); i++) {
			GroupInfo info = mGroups.get(i);
			if(info != null){
				info.setChoosed(mSelectAll.isChecked());
				List<ProductInfo> childs = mChildren.get(info.getId());
				for (int j = 0; j < childs.size(); j++) {
					childs.get(j).setChoosed(mSelectAll.isChecked());
				}
			}
		}
		mAdapter.notifyDataSetChanged();// 刷新界面
		calculateAll();// 更新总价数量
	}

	// 计数
	private void calculateAll() {
		totalCount = 0;
		totalPrice = 0;
		for (int i = 0; i < mGroups.size(); i++) {
			GroupInfo group = mGroups.get(i);
			List<ProductInfo> childs = mChildren.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				ProductInfo product = childs.get(j);
				if (product.isChoosed()) {
					totalCount++;// += product.getCount();
					totalPrice += (double)product.getCount() * (double)product.getPrice();
				}
			}
		}
		mTotalPrice.setText(getResources().getString(R.string.money_sigh) + (totalPrice == 0 ? 0 : new DecimalFormat("#.00").format(totalPrice)));
		if (0 == totalCount) {
			mGoodCount.setText(getResources().getString(R.string.pay));
		}
		else {
			mGoodCount.setText(getResources().getString(R.string.pay) + "(" + totalCount + ")");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != mAdapter) {
			mListView.startRefresh();
			((TextView) findViewById(R.id.shopping_car)).setText(getResources().getString(R.string.store_shopping_car));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventManager.getInstance().removeHandlerListenner(mHandler);
		((HSESchoolApp) getApplication()).removeActivityStore(this);
	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		super.taskFinished(type, result, isHistory);
		if(mListView != null){
			mListView.complete();
		}
		if(result == null){
			removeDialogCustom();
			return;
		}
		if(result instanceof Error){
			removeDialogCustom();
			Toast.makeText(StoreShoppingCarActivity.this, ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case TaskOrMethod_ShoppingCarListShoppingCar:
			String size = "";
			int length = 0;
			try {
				totalCount = 0;
				totalPrice = 0.0;
				mDatasCar.clear();
				mGroups.clear();
				mChildren.clear();
				mSelectAll.setChecked(false);
				mGoodCount.setText(getResources().getString(R.string.pay_finish));
				mTotalPrice.setText(getResources().getString(R.string.money_sigh) + "0");
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			if(((JSONObject)result).has("items")){
				mItemsArr = ((JSONObject)result).optJSONArray("items");				
				if(mItemsArr != null && mItemsArr.length() > 0){			
					for(int i = 0;i < mItemsArr.length(); i++){
						JSONObject item = mItemsArr.optJSONObject(i);
						if(item != null){
							Map<String, Object> group = new HashMap<String, Object>();
							GroupInfo gininfo = new GroupInfo();
							gininfo.setId(item.optString("shopId"));
							gininfo.setName(item.optString("shopName"));
							gininfo.setDeliver(item.optString("deliver"));
							if(item.has("address")){
								gininfo.setAddress(item.optString("address"));
							}
							else{
								gininfo.setAddress("");
							}
							gininfo.setDeliverFee(item.optDouble("deliverFee", 0));
							
							group.put(ShopCartListViewAdapter.SHOPCART_TYPE, ShopCartListViewAdapter.SHOPCART_FLAG_GROUP);
							group.put(ShopCartListViewAdapter.SHOPCART_DATA, gininfo);
							mDatasCar.add(group);
							mGroups.add(gininfo);							
							JSONArray arr = item.optJSONArray("goods");
							List<ProductInfo> childs = new ArrayList<ProductInfo>();
							for(int j = 0; j < arr.length(); j++){
								length++;
								Map<String,Object> product = new HashMap<String, Object>();
								ProductInfo pininfo = new ProductInfo();
							    JSONObject goodsMsgObj = arr.optJSONObject(j);
							    pininfo.setName(goodsMsgObj.optString("goodsName"));
							    pininfo.setId(goodsMsgObj.optLong("goodsId")+"");						
							    pininfo.setImageUrl(goodsMsgObj.optString("image"));
							    pininfo.setColor(goodsMsgObj.optString("colour"));
							    pininfo.setSize(goodsMsgObj.optString("spec"));
							    pininfo.setPrice(goodsMsgObj.optDouble("price"));
							    pininfo.setMarkPrice(goodsMsgObj.optDouble("money"));
							    pininfo.setCount(goodsMsgObj.optInt("number"));
							    pininfo.setInventory(goodsMsgObj.optInt("inventory"));
							    pininfo.setShopCarId(goodsMsgObj.optInt("shoppingCarId")+"");	
							    product.put(ShopCartListViewAdapter.SHOPCART_PARENT_ID, gininfo.getId());
								product.put(ShopCartListViewAdapter.SHOPCART_PARENT_POSITION, i);
								product.put(ShopCartListViewAdapter.SHOPCART_TYPE, ShopCartListViewAdapter.SHOPCART_FLAG_CHILDREN);
								product.put(ShopCartListViewAdapter.SHOPCART_DATA, pininfo);
							    mDatasCar.add(product);
							    childs.add(pininfo);
						   }
							mChildren.put(mGroups.get(i).getId(), childs);
						}
					}
					mAdapter.setData(mDatasCar);
					mAdapter.setCheckInterface(new CheckInterface() {
						
						@Override
						public void checkGroup(int position, boolean isChecked) {
							Map<String, Object> parent = mDatasCar.get(position);
							String parentId = ((GroupInfo) (parent.get(ShopCartListViewAdapter.SHOPCART_DATA))).getId();
							for (int i = 0; i < mDatasCar.size(); i++) {
								Map<String, Object> map = mDatasCar.get(i);
								String child_parentId = (String) map.get(ShopCartListViewAdapter.SHOPCART_PARENT_ID);
								if (parentId.equals(child_parentId)) {
									ProductInfo pinfo = (ProductInfo) map.get(ShopCartListViewAdapter.SHOPCART_DATA);
									pinfo.setChoosed(isChecked);
								}
							}

							boolean allGroupSameState = true;
							for (int i = 0; i < mGroups.size(); i++) {
								if (isChecked != mGroups.get(i).isChoosed()) {
									allGroupSameState = false;
									break;
								}
							}
							if (allGroupSameState) {
								mSelectAll.setChecked(isChecked);
							} else {
								mSelectAll.setChecked(false);
							}
							mAdapter.notifyDataSetChanged();
							calculateAll();
						}
						
						@Override
						public void checkChild(int position, boolean isChecked) {							
							Map<String, Object> child = mDatasCar.get(position);
							int parentPosition = (int) child.get(ShopCartListViewAdapter.SHOPCART_PARENT_POSITION);
							GroupInfo parent = mGroups.get(parentPosition);
							List<ProductInfo> childs = mChildren.get(parent.getId());
							boolean allChildSameState = true;
							for (int i = 0; i < childs.size(); i++) {
								if (childs.get(i).isChoosed() != isChecked) {
									allChildSameState = false;
									break;
								}
							}
							if (allChildSameState) {
								parent.setChoosed(isChecked);
							} else {
								parent.setChoosed(false);
							}

							boolean allGroupSameState = true;
							boolean firstState = mGroups.get(0).isChoosed();
							for (int i = 0; i < mGroups.size(); i++) {
								if (firstState != mGroups.get(i).isChoosed()) {
									allGroupSameState = false;
									break;
								}
							}
							if (allGroupSameState) {
								mSelectAll.setChecked(firstState);
							} else {
								mSelectAll.setChecked(false);
							}
							mAdapter.notifyDataSetChanged();
							calculateAll();
						}
					});
					
					mAdapter.setModifyCountInterface(new ModifyCountInterface() {
						
						@Override
						public void doIncrease(int position, View showCountView, boolean isChecked) {
							if(mDatasCar == null || mDatasCar.size() == 0){
								return;
							}
							Map<String, Object> map = mDatasCar.get(position);
							if(map == null || map.size() == 0){
								return;
							}
							ProductInfo product = (ProductInfo) map.get(ShopCartListViewAdapter.SHOPCART_DATA);
							int currentCount = product.getCount();
							if (currentCount <= 1) {
								return;
							}
							currentCount--;
							if (currentCount <= 1) {
								currentCount = 1;
							}
							product.setCount(currentCount);
							((TextView)showCountView).setText(currentCount + "");
							mAdapter.notifyDataSetChanged();
							calculateAll();
							DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_ShoppingCarSaveShoppingCar,
									DataLoader.getInstance().setsaveShoppingCarTypeParams(currentCount, product.getColor(), product.getSize(), product.getPrice() +"", product.getId(), true),
									StoreShoppingCarActivity.this);
						}
						
						@Override
						public void doDecrease(int position, View showCountView, boolean isChecked) {
							if(mDatasCar == null || mDatasCar.size() == 0){
								return;
							}
							Map<String, Object> map = mDatasCar.get(position);
							if(map == null || map.size() == 0){
								return;
							}
							ProductInfo product = (ProductInfo) map.get(ShopCartListViewAdapter.SHOPCART_DATA);
							int currentCount = product.getCount();
							int inventory = product.getInventory();
							if(currentCount >= inventory){
								new AlertDialog.Builder(StoreShoppingCarActivity.this) 
								.setCancelable(false)
							 	.setMessage(String.format(getResources().getString(R.string.shopcar_above_inventory), inventory +""))
							 	.setPositiveButton(getResources().getString(R.string.confirm), null)
							 	.show();
								return;
							}
							currentCount++;
							product.setCount(currentCount);
							((TextView)showCountView).setText(currentCount + "");
							mAdapter.notifyDataSetChanged();//刷新界面
							calculateAll();//更新总价数量
							DataLoader.getInstance().startTaskForResult(TaskType.TaskOrMethod_ShoppingCarSaveShoppingCar,
									DataLoader.getInstance().setsaveShoppingCarTypeParams(currentCount, product.getColor(), product.getSize(), product.getPrice() +"", product.getId(), true),
									StoreShoppingCarActivity.this);
						}
					});
										
				}
			}
			if(length > 0){
				size = "(" + length + ")";			
			}
			((TextView)findViewById(R.id.shopping_car)).setText(getResources().getString(R.string.store_shopping_car) + size);
			break;
		case TaskOrMethod_ShoppingCarDelShoppingCar:
			if(result instanceof JSONObject){
				mSelectAll.setChecked(false);
				showDialogToast(getResources().getString(R.string.delete_success));
				EventManager.getInstance().sendMessage(EventManager.EventType_ShoppingCar, ((JSONObject)result).optInt("count"));
			}			
			break;	
		case TaskOrMethod_FavoriteApply:
			removeDialogCustom();
			if(((JSONObject)result).has("result")){	
				showDialogToast(getResources().getString(R.string.collect_success));
			}
			break;			
		default:
			break;
		}
	}

	private void showDialogToast(String content) {
		View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.store_car_hint, null);
		view.findViewById(R.id.store_car_hints).getBackground().setAlpha(200);
		((TextView) view.findViewById(R.id.store_car_hint)).setText(content);
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
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
				}
			}
		}, 800);
	}
}
