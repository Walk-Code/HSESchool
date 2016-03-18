package com.zhuochuang.hsej.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.layout.PullToRefreshListView;
import com.layout.PullToRefreshListView.OnRefreshListener;
import com.model.DataLoader;
import com.model.EventManager;
import com.model.TaskListener;
import com.model.TaskType;
import com.util.Utils;
import com.zhuochuang.hsej.GoodsReleaseActivity;
import com.zhuochuang.hsej.PublishRecruitActivity;
import com.zhuochuang.hsej.PublishRecruitDetailsActivity;
import com.zhuochuang.hsej.R;
import com.zhuochuang.hsej.SecondHandandLostActivity;
import com.zhuochuang.hsej.SecondHandandLostDetailsActivity;
import com.zhuochuang.hsej.adapter.RecruitServiceAdapter;
import com.zhuochuang.hsej.adapter.SHLBaseAdapter.OnEFClickListener;
import com.zhuochuang.hsej.adapter.SecondHandandLostAdapter;
import com.zhuochuang.hsej.entity.RecruitServiceEntity;
import com.zhuochuang.hsej.entity.SecondHandEntity;

/**
 * requestCode===1 二手买卖 2 失物招领 3 招聘服务
 * 
 * @author Administrator
 * 
 */
public class SecondHandandLostFragment extends Fragment implements
		TaskListener, OnRefreshListener, OnItemClickListener, OnEFClickListener {

	private PullToRefreshListView mListView;
	private int status;
	private int requestType;
	private boolean isMyself;
	private SecondHandEntity mSecondHandEntity;// 失物招领跟二手实体对象
	private RecruitServiceEntity mRecruitServiceEntity;// 招聘服务对象
	private SecondHandandLostAdapter mHandandLostAdapter;
	private RecruitServiceAdapter mRecruitServiceAdapter;
	private LinearLayout delLayout;
	private Button btnDelete;
	private List<Long> delIds;
	private final int DATA_CHANGE_CODE = 0x1002;
	private final int EDIT_CODE = 0x1003;
	private final int EDIT_FINISH_CODE = 0x1004;

	public SecondHandandLostFragment(int status, int type, boolean isMyself) {
		this.status = status;
		this.requestType = type;
		this.isMyself = isMyself;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_second_handand_lost_going,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		mListView = (PullToRefreshListView) getView().findViewById(
				R.id.puulto_listview);
		delLayout = (LinearLayout) getView().findViewById(
				R.id.linear_delete_layout);
		btnDelete = (Button) getView().findViewById(R.id.btn_delete);
	}

	private void initData() {
		delIds = new ArrayList<Long>();
		mListView.setOnRefreshListener(this);
		mListView.startRefresh();
		if (!isRecruit()) {// 失物、二手服务
			mHandandLostAdapter = new SecondHandandLostAdapter(getActivity(),
					isMyself ? false : status == 1 ? true : false,
					isMyself ? false : status == 1 ? false : false, isMyself,
					requestType);
			mListView.setAdapter(mHandandLostAdapter);
			mHandandLostAdapter.setOnEFClickListener(this);
		} else {// 招聘服务
			mListView.setDivider(getResources().getDrawable(
					R.drawable.listview_divider_color));
			mRecruitServiceAdapter = new RecruitServiceAdapter(getActivity(),
					isMyself ? false : status == 1 ? true : false,
					isMyself ? false : status == 1 ? false : false, isMyself);
			mListView.setAdapter(mRecruitServiceAdapter);
			mRecruitServiceAdapter.setOnEFClickListener(this);
		}
		mListView.setOnItemClickListener(this);
		EventManager.getInstance().setHandlerListenner(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (mSecondHandEntity != null) {
					switch (msg.what) {
					case EDIT_CODE:
						if (isMyself) {
							mHandandLostAdapter.setEdit(true);
							delLayout.setVisibility(View.VISIBLE);
						}
						break;
					case EDIT_FINISH_CODE:
						mHandandLostAdapter.setEdit(false);
						delLayout.setVisibility(View.GONE);
						break;
					case DATA_CHANGE_CODE:
						mListView.startRefresh();
						break;
					}
				} else if (mRecruitServiceEntity != null) {
					switch (msg.what) {
					case EDIT_CODE:
						if (isMyself) {
							mRecruitServiceAdapter.setEdit(true);
							delLayout.setVisibility(View.VISIBLE);
						}
						break;
					case EDIT_FINISH_CODE:
						mRecruitServiceAdapter.setEdit(false);
						delLayout.setVisibility(View.GONE);
						break;
					case DATA_CHANGE_CODE:
						mListView.startRefresh();
						break;
					}
				}
			}
		});

	}

	private void initListener() {
		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DeleteGoods();
			}
		});
	}

	@Override
	public void onRefresh() {
		if (isMyself) {
			if (!DataLoader.getInstance().isLogin()) {
				return;
			}
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pageNo", 1);
		params.put("pageSize", 20);
		params.put("isMyself", isMyself);
		params.put("status", status);
		if (!isRecruit()) {
			params.put("type", requestType);
			DataLoader.getInstance().startTaskForResult(
					TaskType.TaskOrMethod_SecondHandandLost, params, this);
		} else {
			DataLoader.getInstance().startTaskForResult(
					TaskType.TaskOrMethod_RecruitServiceList, params, this);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		if (!isRecruit()) {
			intent = new Intent(getActivity(),
					SecondHandandLostDetailsActivity.class);
			intent.putExtra("goodsid",
					mSecondHandEntity.getItems().get(position - 1).getId() + "");
			intent.putExtra("nativeCode", requestType);
		} else {
			intent = new Intent(getActivity(),
					PublishRecruitDetailsActivity.class);
			intent.putExtra("id",
					mRecruitServiceEntity.getItems().get(position - 1).getId());
		}
		startActivity(intent);
	}

	private void DeleteGoods() {// 删除
		String ids = "";
		for (int i = 0; i < delIds.size(); i++)
			ids += delIds.get(i) + ",";
		if (ids.contains(","))
			ids = ids.substring(0, ids.length() - 1);
		if (TextUtils.isEmpty(ids)) {
			Toast.makeText(getActivity(), R.string.delete_notice,
					Toast.LENGTH_SHORT).show();
			return;
		}
		final HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		new AlertDialog.Builder(getActivity())
				.setMessage(R.string.text_delete_info)
				.setNegativeButton(R.string.my_homepage_save,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (!isRecruit()) {
									DataLoader
											.getInstance()
											.startTaskForResult(
													TaskType.TaskOrMethod_SecondHandandLostDelete,
													params,
													SecondHandandLostFragment.this);
								} else {
									DataLoader
											.getInstance()
											.startTaskForResult(
													TaskType.TaskOrMethod_RecruitServiceDelete,
													params,
													SecondHandandLostFragment.this);
								}
							}
						}).setPositiveButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create().show();

	}

	@Override
	public void onFinishClick(View view, final int position) {
		new AlertDialog.Builder(getActivity())
				.setMessage(R.string.text_trans_finish)
				.setNegativeButton(R.string.my_homepage_save,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								HashMap<String, Object> params = new HashMap<String, Object>();
								if (!isRecruit()) {
									params.put("id", mSecondHandEntity
											.getItems().get(position).getId());
									DataLoader
											.getInstance()
											.startTaskForResult(
													TaskType.TaskOrMethod_SecondHandandLostFinish,
													params,
													SecondHandandLostFragment.this);
								} else {
									params.put("id", mRecruitServiceEntity
											.getItems().get(position).getId());
									DataLoader
											.getInstance()
											.startTaskForResult(
													TaskType.TaskOrMethod_RecruitServiceFinish,
													params,
													SecondHandandLostFragment.this);
								}
							}
						}).setPositiveButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create().show();
	}

	@Override
	public void onEditClick(View view, int position) {
		Intent intent;
		switch (requestType) {
		case 1:
		case 2:
			intent = new Intent(getActivity(), GoodsReleaseActivity.class);
			intent.putExtra("type", requestType);
			Bundle bundle = new Bundle();
			bundle.putSerializable("second_handand_entity", mSecondHandEntity
					.getItems().get(position));
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(getActivity(), PublishRecruitActivity.class);
			intent.putExtra("id", mRecruitServiceEntity.getItems()
					.get(position).getId());
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onChecked(boolean isChecked, int position) {
		long id;
		if (!isRecruit()) {
			id = Long.parseLong(mSecondHandEntity.getItems().get(position)
					.getId()
					+ "");
		} else {
			id = Long.parseLong(mRecruitServiceEntity.getItems().get(position)
					.getId()
					+ "");
		}
		if (isChecked)
			delIds.add(id);
		else
			delIds.remove(id);

	}

	@Override
	public void taskStarted(TaskType type) {

	}

	@Override
	public void taskFinished(TaskType type, Object result, boolean isHistory) {
		if (result == null)
			return;
		if (result instanceof Error) {
			Toast.makeText(getActivity(), ((Error) result).getMessage(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		Gson gson;
		switch (type) {
		case TaskOrMethod_SecondHandandLost:
			mListView.complete();
			gson = new Gson();
			mSecondHandEntity = gson.fromJson(result.toString(),
					SecondHandEntity.class);
			mHandandLostAdapter.setData(mSecondHandEntity.getItems());
			SecondHandandLostActivity.Instance.setTabTitleText(
					mSecondHandEntity.getTotalBegin(),
					mSecondHandEntity.getTotalEnd());
			break;
		case TaskOrMethod_SecondHandandLostDelete:
			if (result instanceof JSONObject) {
				Utils.removeSoftKeyboard(getActivity());
				EventManager.getInstance().sendMessage(DATA_CHANGE_CODE, "");
				Toast.makeText(getActivity(),
						getResources().getString(R.string.delete_ok),
						Toast.LENGTH_SHORT).show();
				mListView.startRefresh();
			} else {
				Toast.makeText(getActivity(), R.string.delete_fail,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case TaskOrMethod_RecruitServiceFinish:
		case TaskOrMethod_SecondHandandLostFinish:
			if (result instanceof JSONObject) {
				EventManager.getInstance().sendMessage(DATA_CHANGE_CODE, "");
			}
			break;
		case TaskOrMethod_RecruitServiceList:
			mListView.complete();
			if (result instanceof JSONObject) {
				if ("1".equals(((JSONObject) result).optString("result")
						.toString())) {
					gson = new Gson();
					mRecruitServiceEntity = gson.fromJson(result.toString(),
							RecruitServiceEntity.class);
					SecondHandandLostActivity.Instance.setTabTitleText(
							mRecruitServiceEntity.getTotalBegin(),
							mRecruitServiceEntity.getTotalEnd());
					mRecruitServiceAdapter.setData(mRecruitServiceEntity
							.getItems());
				}
			} else {
				Toast.makeText(getActivity(),
						R.string.activity_common_service_get_fail,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case TaskOrMethod_RecruitServiceDelete:
			if (result instanceof JSONObject) {
				Utils.removeSoftKeyboard(getActivity());
				EventManager.getInstance().sendMessage(DATA_CHANGE_CODE, "");
				Toast.makeText(getActivity(),
						getResources().getString(R.string.delete_ok),
						Toast.LENGTH_SHORT).show();
				mListView.startRefresh();
			} else {
				Toast.makeText(getActivity(), R.string.delete_fail,
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (isMyself) {
			mListView.startRefresh();
		}
	}

	@Override
	public void taskIsCanceled(TaskType type) {

	}

	private boolean isRecruit() {// 是否是招聘页面
		if (requestType == 1 || requestType == 2)
			return false;
		return true;
	}
}
