package com.zhuochuang.hsej;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.model.DataLoader;
import com.model.EventManager;
import com.util.TabBarView;
import com.util.TabBarView.OnItemSelectedListeners;
import com.zhuochuang.hsej.adapter.SecondHandandLostTabAdapter;
import com.zhuochuang.hsej.fragment.SecondHandandLostFragment;

public class SecondHandandLostActivity extends BaseActivity implements
		OnClickListener {

	private FragmentManager mManager;
	private FragmentTransaction mTransaction;
	private SecondHandandLostFragment mContent;
	private TabBarView mTabBarView;
	private List<SecondHandandLostFragment> mFragments;
	private int nativeCode = 1;
	private TextView btnABRight;
	private List<String> mTabNames;
	private boolean isEdit = false;
	private final int EDIT_CODE = 0x1003;
	private final int EDIT_FINISH_CODE = 0x1004;
	private static SecondHandandLostTabAdapter mTabAdapter;
	private static int mCurrentTab = 0;
	private static int toTab;
	public static SecondHandandLostActivity Instance;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_second_handand_lost);
		Instance = this;
		initView();
		initData();
	}

	private void initView() {
		mTabBarView = (TabBarView) findViewById(R.id.layout_tabbar);
		btnABRight = (TextView) findViewById(R.id.textview_right_text);
		btnABRight.setTextSize(16);
	}

	private void initData() {
		nativeCode = getIntent().getIntExtra("type", 1);
		if (nativeCode == 1) {
			setTitleText(R.string.activitysecendhand_lost_title);
		} else if (nativeCode == 2) {
			setTitleText(R.string.activitysecondhand_lost_swzl_title);
		} else {
			setTitleText(R.string.text_zpfw);
		}
		mManager = getSupportFragmentManager();
		mFragments = new ArrayList<SecondHandandLostFragment>();
		mTabNames = new ArrayList<String>();

		mFragments.add(new SecondHandandLostFragment(1, nativeCode, false));
		mFragments.add(new SecondHandandLostFragment(2, nativeCode, false));
		mFragments.add(new SecondHandandLostFragment(2, nativeCode, true));

		Resources mRes = getResources();
		mTabNames.add(mRes
				.getString(R.string.activitysecendhand_lost_tab_going));
		mTabNames.add(mRes
				.getString(R.string.activitysecendhand_lost_tab_complete));
		mTabNames.add(mRes
				.getString(R.string.activitysecendhand_lost_tab_mypublish));

		mTabAdapter = new SecondHandandLostTabAdapter(getApplicationContext(),
				mTabNames);
		mTabBarView.setAdapter(mTabAdapter);
		mTabBarView.setOnItemSelectedListener(new OnItemSelectedListeners() {
			@Override
			public void onItemSelected(int position) {

				toTab = position;
				if (position == 2) {
					if (!DataLoader.getInstance().isLogin()) {
						Intent intent = new Intent(
								SecondHandandLostActivity.this,
								LoginActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.push_bottom_in,
								R.anim.push_bottom_out);
						return;
					}

				}
				mCurrentTab = position;
				switchFragment(mFragments.get(position));
				ChangeTabNameColor(position);

			}
		});
		mTabBarView.selectItem(0);
	}

	private void switchFragment(SecondHandandLostFragment fragment) {
		mTransaction = mManager.beginTransaction();
		if (fragment == null)
			return;
		if (mContent == null) {
			mTransaction.add(R.id.mainLayout, fragment, fragment.getTag());
		} else {
			if (fragment.isAdded()) {
				mTransaction.hide(mContent).show(fragment);
			} else {
				mTransaction.hide(mContent).add(R.id.mainLayout, fragment,
						fragment.getTag());
			}
		}
		mContent = fragment;
		mTransaction.commitAllowingStateLoss();
	}

	private void ChangeTabNameColor(int position) {// tab的文本及颜色
		if (position == 0 || position == 1) {
			btnABRight.setText(R.string.activitysecendhand_lost_title_right);
			btnABRight.setTextColor(Color.rgb(227, 89, 84));
			btnABRight.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent;
					if (nativeCode == 1 || nativeCode == 2) {
						if (!DataLoader.getInstance().isLogin()) {
							intent = new Intent(SecondHandandLostActivity.this,
									LoginActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.push_bottom_in,
									R.anim.push_bottom_out);
							return;
						}
						intent = new Intent(SecondHandandLostActivity.this,
								GoodsReleaseActivity.class);
						intent.putExtra("type", nativeCode);
					} else {
						if (!DataLoader.getInstance().isLogin()) {
							intent = new Intent(SecondHandandLostActivity.this,
									LoginActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.push_bottom_in,
									R.anim.push_bottom_out);
							return;
						}
						intent = new Intent(SecondHandandLostActivity.this,
								PublishRecruitActivity.class);
					}
					startActivity(intent);
				}
			});
		} else {
			InitBtnAbRightTextColor();
			btnABRight.setOnClickListener(this);
		}
		View view;
		TextView textView;
		for (int i = 0; i < mTabNames.size(); i++) {
			view = mTabBarView.getChildAt(i);
			textView = (TextView) view.findViewById(R.id.textview);
			if (i == position) {
				textView.setTextColor(Color.rgb(227, 89, 84));
			} else {
				textView.setTextColor(Color.rgb(85, 85, 85));
			}
		}
	}

	/**
	 * 改变ActionBar右边按钮的状态
	 */
	private void ChangeBtnABRightState() {
		if (isEdit) {// 编辑状态
			EventManager.getInstance().sendMessage(EDIT_FINISH_CODE, "");
			isEdit = false;
		} else {// 非编辑状态
			EventManager.getInstance().sendMessage(EDIT_CODE, "");
			isEdit = true;
		}
		InitBtnAbRightTextColor();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (toTab == 2) {
			if (DataLoader.getInstance().isLogin()) {
				switchFragment(mFragments.get(2));
			} else {
				mTabBarView.selectItem(mCurrentTab);
			}
		}
	}

	private void InitBtnAbRightTextColor() {// 初始化按钮文字跟颜色
		if (isEdit) {// 编辑状态
			btnABRight.setText(R.string.enter_school_content_text1);
			btnABRight.setTextColor(Color.rgb(227, 89, 84));
		} else {// 非编辑状态
			btnABRight.setText(R.string.delete);
			btnABRight.setTextColor(Color.BLACK);
		}
	}

	public void setTabTitleText(int goingSize, int finishSize) {
		Resources mRes = getResources();
		String formatStrGoing = String.format(
				mRes.getString(R.string.text_tab_num), goingSize);
		String formatStrFinish = String.format(
				mRes.getString(R.string.text_tab_num), finishSize);
		mTabNames.set(0,
				mRes.getString(R.string.activitysecendhand_lost_tab_going)
						+ formatStrGoing);
		mTabNames.set(1,
				mRes.getString(R.string.activitysecendhand_lost_tab_complete)
						+ formatStrFinish);
		mTabBarView.notifyChange();
	}

	@Override
	public void onClick(View v) {
		ChangeBtnABRightState();
	}
}
