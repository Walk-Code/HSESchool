package com.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
/**
 * 搴曢儴鐨凾abbar
 */
@SuppressLint("ClickableViewAccessibility")
public class TabBarView extends LinearLayout {
	private TabBarAdapter mAdapter;
	private boolean mIsFirstCreate = true;
	private int mCurrentPosition = 1000;
	private int mCurrentPositionTemp = 1000;
	private OnItemSelectedListeners mOnItemSelectedListener;
	private OnItemLongPressListener mOnItemLongPressListener;
	private OnItemSpecialListener mOnItemSpecialListener;
	private int mSpecialItem = -1;

	public interface TabBarAdapter {
		int getCount();

		View getSeletedView(int position, View convertView);

		View getNOSeletedView(int position, View convertView);
	}

	public interface OnItemSelectedListeners {
		void onItemSelected(int position);
	}

	public interface OnItemClicks {
		void onItemClicks(int position);
	}

	public interface OnItemLongPressListener {
		void onItemLongPress(int position);
	}

	public interface OnItemSpecialListener {
		void onItemSpecial(int position);
	}

	public TabBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TabBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setOnItemSelectedListener(
			OnItemSelectedListeners onItemSelectedListener) {
		mOnItemSelectedListener = onItemSelectedListener;
	}

	public void setOnItemLongPressListener(
			OnItemLongPressListener onItemLongPressListener) {
		mOnItemLongPressListener = onItemLongPressListener;
	}

	public void setOnItemSpecialListener(
			OnItemSpecialListener onItemSpecialListener) {
		mOnItemSpecialListener = onItemSpecialListener;
	}

	public void setAdapter(TabBarAdapter adapter) {
		mAdapter = adapter;
		createUI();
	}

	public void setSpecialItem(int specialItem) {
		mSpecialItem = specialItem;
	}

	public void selectItem(int position) {
		if (mCurrentPosition != position) {
			for (int i = 0; i < getChildCount(); i++) {
				if (position == i) {
					View v = getChildAt(position);
					v.setId(position);
					mAdapter.getSeletedView(position, v);
					if (mCurrentPosition != 1000) {
						mAdapter.getNOSeletedView(mCurrentPosition,
								getChildAt(mCurrentPosition));
					}
					if (mOnItemSelectedListener != null) {
						mOnItemSelectedListener.onItemSelected(position);
					}
				} else {
					View v = getChildAt(i);
					v.setId(i);
					mAdapter.getNOSeletedView(i, v);
				}
			}
			mCurrentPosition = position;
		}
	}

	public void notifyChange() {
		mCurrentPositionTemp = mCurrentPosition;
		mCurrentPosition = 1000;
		selectItem(mCurrentPositionTemp);
	}

	public int getCurrentPosition() {
		return mCurrentPosition;
	}

	/**
	 * 鍙栨秷褰撳墠鐨勯�鎷�
	 */
	public void cancleSelected() {
		mAdapter.getNOSeletedView(mCurrentPosition,
				getChildAt(mCurrentPosition));
		mCurrentPosition = 1000;
	}

	private void createUI() {
		if (mIsFirstCreate) {
			setOrientation(LinearLayout.HORIZONTAL);
			for (int position = 0; position < mAdapter.getCount(); position++) {
				View v = mAdapter.getNOSeletedView(position, null);
				addView(v,
						new LinearLayout.LayoutParams(
								android.view.ViewGroup.LayoutParams.MATCH_PARENT,
								android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1));
				v.setId(position);
				v.setOnClickListener(new ItemSelect());
				v.setOnLongClickListener(new LongClick());
			}
			mIsFirstCreate = false;
		}
	}

	class ItemSelect implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = v.getId();
			if (mSpecialItem != -1 && position == mSpecialItem) {
				mOnItemSpecialListener.onItemSpecial(position);
			} else {
				selectItem(position);
			}
		}
	}

	class LongClick implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			int position = v.getId();
			if (mSpecialItem != -1 && position == mSpecialItem) {
				mOnItemLongPressListener.onItemLongPress(position);
			}
			return true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
}
