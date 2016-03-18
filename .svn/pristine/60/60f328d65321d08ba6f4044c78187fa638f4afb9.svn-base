package com.layout.photoview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.layout.photoalbum.Bimp;
import com.layout.photoview.PhotoViewAttacher.OnViewTapListener;
import com.model.EventManager;
import com.model.ImageLoaderConfigs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.util.PageGuide;
import com.util.Utils;
import com.zhuochuang.hsej.R;

@SuppressLint("InflateParams")
public class PhotoViewer {

	private Context mContext;
	private PopupWindow mPopupWindow;
	private FrameLayout mPopupLayout;
	private ViewPager mViewPager;
	private PageGuide mPageGuide;
	private SamplePagerAdapter mSamplePagerAdapter;

	private ArrayList<Object> mMixtureObj;
	private HashMap<Integer, Boolean> mMap = null;
	private String[] mNewfeedPicArr;
	private String mSinglePicUrl;

	private JSONArray mPathArr;

	private int mPageMax;
	private int mPageTag;
	private boolean mIsDeleteOp = false;

	public PhotoViewer(Context context, int pageTag) {
		mContext = context;
		mPageTag = pageTag;
	}

	public void setmIsDeleteOp(boolean isNeedDelete) {
		mIsDeleteOp = isNeedDelete;
	}

	public void setInvertSelectionMap(HashMap<Integer, Boolean> map) {
		mMap = map;
	}

	public void setNewfeedPicArr(String[] picArr) {
		mNewfeedPicArr = picArr;
	}

	public void setSinglePicUrl(String url) {
		mSinglePicUrl = url;
	}

	public void setMixtureObj(ArrayList<Object> obj) {
		mMixtureObj = obj;
	}

	public void setPathArr(JSONArray arr) {
		mPathArr = arr;
	}

	public void notifyDataChange() {
		if (mPathArr != null && mPathArr.length() == 0) {
			dismiss();
		} else {
			mPageMax = mPathArr.length();
			mViewPager
					.setAdapter(mSamplePagerAdapter = new SamplePagerAdapter());
			((TextView) mPopupLayout.findViewById(R.id.textview_pageguide))
					.setText(((mPageTag == 0 || mPageTag == 1) ? 1 : mPageTag)
							+ "/" + mPageMax);
			mViewPager.setCurrentItem(mPageTag);
		}
	}

	public String getPathId() {
		if (mPathArr != null && mPathArr.length() > 0) {
			JSONObject obj = mPathArr.optJSONObject(mPageTag);
			if (obj != null && obj.has("id")) {
				return obj.optString("id");
			}
		}
		return null;
	}

	public Integer getCurrentPage() {
		return mPageTag;
	}

	private void initViewContainer(Context context) {
		mPopupLayout = (FrameLayout) LayoutInflater.from(context).inflate(
				R.layout.popupwin_photoviewer, null);
		if (mIsDeleteOp) {
			mPopupLayout.findViewById(R.id.navbar_base).setVisibility(
					View.VISIBLE);
			mPopupLayout.findViewById(R.id.ico_postedit).setVisibility(
					View.VISIBLE);
			mPopupLayout.findViewById(R.id.ico).setVisibility(View.VISIBLE);
		}
		mPageGuide = (PageGuide) mPopupLayout
				.findViewById(R.id.photoview_pageguide);
		mPageGuide.setRes(R.drawable.pageguid_p, R.drawable.pageguid_white);
		if (mPathArr != null && mPathArr.length() > 0) {
			mPageMax = mPathArr.length();
			mPageGuide.setParams(mPathArr.length(),
					Utils.dipToPixels(context, 8),
					Utils.dipToPixels(context, 8));
		} else if (mSinglePicUrl != null && mSinglePicUrl.length() > 0) {
			mPageMax = 1;
			mPageGuide.setParams(1, Utils.dipToPixels(context, 8),
					Utils.dipToPixels(context, 8));
		} else if (mNewfeedPicArr != null && mNewfeedPicArr.length > 0) {
			mPageMax = mNewfeedPicArr.length;
			mPageGuide.setParams(mNewfeedPicArr.length,
					Utils.dipToPixels(context, 8),
					Utils.dipToPixels(context, 8));
		} else if (mMixtureObj != null && mMixtureObj.size() > 0) {
			mPageMax = mMixtureObj.size();
			mPageGuide.setParams(mMixtureObj.size(),
					Utils.dipToPixels(context, 8),
					Utils.dipToPixels(context, 8));
		} else if (Bimp.bmp != null && Bimp.bmp.size() > 0) {
			mPageMax = Bimp.bmp.size();
			mPageGuide.setParams(Bimp.bmp.size(),
					Utils.dipToPixels(context, 8),
					Utils.dipToPixels(context, 8));
		}
		mPageGuide.setVisibility(View.GONE);
		mPageGuide.setSelect(mPageTag);
		((TextView) mPopupLayout.findViewById(R.id.textview_pageguide))
				.setText((mPageTag + 1) + "/" + mPageMax);

		mViewPager = (ViewPager) mPopupLayout
				.findViewById(R.id.photoview_viewpager);
		mViewPager.setPageMargin(Utils.dipToPixels(context, 5));
		mViewPager.setAdapter(mSamplePagerAdapter = new SamplePagerAdapter());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				mPageTag = arg0;
				((TextView) mPopupLayout.findViewById(R.id.textview_pageguide))
						.setText((mPageTag + 1) + "/" + mPageMax);

				mPageGuide.setSelect(arg0);
				mPageTag = arg0;
				if (mMap != null && mMap.size() > 0) {
					if (mMap.get(mPageTag)) {
						mPopupLayout.findViewById(R.id.photoview_colorview)
								.setBackgroundResource(
										R.drawable.list_option_no);
					} else {
						mPopupLayout.findViewById(R.id.photoview_colorview)
								.setBackgroundResource(
										R.drawable.list_option_tick);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		if (mMap != null && mMap.size() > 0) {
			mPopupLayout.findViewById(R.id.photoview_colorview).setVisibility(
					View.VISIBLE);
		} else {
			mPopupLayout.findViewById(R.id.photoview_colorview).setVisibility(
					View.GONE);
		}
		if (mMap != null && mMap.size() > 0) {
			if (mMap.get(mPageTag)) {
				mPopupLayout.findViewById(R.id.photoview_colorview)
						.setBackgroundResource(R.drawable.list_option_no);
			} else {
				mPopupLayout.findViewById(R.id.photoview_colorview)
						.setBackgroundResource(R.drawable.list_option_tick);
			}
		}

		mPopupLayout.findViewById(R.id.photoview_colorview).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (mMap == null || mMap.size() == 0) {
							return;
						}
						if (mMap.get(mPageTag)) {
							mMap.put(mPageTag, false);
							mPopupLayout.findViewById(R.id.photoview_colorview)
									.setBackgroundResource(
											R.drawable.list_option_tick);
						} else {
							mMap.put(mPageTag, true);
							mPopupLayout.findViewById(R.id.photoview_colorview)
									.setBackgroundResource(
											R.drawable.list_option_no);
						}
					}
				});

		mViewPager.setCurrentItem(mPageTag);

		FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) mPopupLayout
				.findViewById(R.id.navbar_base).getLayoutParams();
		lp.topMargin = Utils.dipToPixels(mContext, 25);
		lp.height = Utils.dipToPixels(mContext, 50);
		mPopupLayout.findViewById(R.id.navbar_base).setLayoutParams(lp);
		// mPopupLayout.findViewById(R.id.navbar_base).setPadding(0,
		// Utils.dipToPixels(mContext, 25), 0, 0);
		mPopupLayout.findViewById(R.id.group_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
	}

	public void showPhotoViewer(View v) {
		initViewContainer(mContext);
		mPopupWindow = new PopupWindow(mPopupLayout, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		// mPopupWindow.setAnimationStyle(R.style.PhotoShowAnimation);
		mPopupWindow.setAnimationStyle(R.style.FadeShowAnimation);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0,
				0, 0)));
		mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		mPopupWindow.update();

		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				if (mMap != null && mMap.size() > 0) {
					EventManager.getInstance().sendMessage(
							EventManager.EventType_Photoalbum, mMap);
				}
			}
		});
	}

	private void dismiss() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mPathArr != null && mPathArr.length() > 0) {
				return mPathArr.length();
			} else if (mSinglePicUrl != null && mSinglePicUrl.length() > 0) {
				return 1;
			} else if (mNewfeedPicArr != null && mNewfeedPicArr.length > 0) {
				return mNewfeedPicArr.length;
			} else if (mMixtureObj != null && mMixtureObj.size() > 0) {
				return mMixtureObj.size();
			} else if (Bimp.bmp != null && Bimp.bmp.size() > 0) {
				return Bimp.bmp.size();
			}
			return 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			FrameLayout layout = (FrameLayout) LayoutInflater.from(
					container.getContext()).inflate(R.layout.photoview_cell,
					null);
			PhotoView photoView = (PhotoView) layout
					.findViewById(R.id.photoview);

			/*
			 * Uri uri = Uri.parse(CacheHandler.getCacheDir(mContext) + "/" +
			 * "advertise.jpg"); photoView.setImageURI(uri);
			 */

			if (mPathArr != null && mPathArr.length() > 0) {
				JSONObject obj = mPathArr.optJSONObject(position);
				if (obj != null) {
					ImageLoader.getInstance().displayImage(
							obj.optString("path"), photoView,
							ImageLoaderConfigs.displayImageOptionsBg);
				}
			} else if (mNewfeedPicArr != null && mNewfeedPicArr.length > 0) {
				ImageLoader.getInstance().displayImage(
						mNewfeedPicArr[position], photoView,
						ImageLoaderConfigs.displayImageOptionsBg);
			} else if (mMixtureObj != null && mMixtureObj.size() > 0) {
				if (mMixtureObj.get(position) instanceof ArrayList<?>) {
					photoView
							.setImageBitmap((Bitmap) ((ArrayList<Object>) mMixtureObj
									.get(position)).get(0));
				} else if (mMixtureObj.get(position) instanceof String) {
					if (((String) mMixtureObj.get(position))
							.startsWith("http://")) {
						ImageLoader.getInstance().displayImage(
								(String) mMixtureObj.get(position), photoView,
								ImageLoaderConfigs.displayImageOptionsBg);
					} else {
						try {
							Bitmap bm = Bimp.revitionImageSize((String) mMixtureObj.get(position), false);
							photoView.setImageBitmap(bm);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else if (mSinglePicUrl != null && mSinglePicUrl.length() > 0) {
				ImageLoader.getInstance().displayImage(mSinglePicUrl,
						photoView, ImageLoaderConfigs.displayImageOptionsBg);
			} else if (Bimp.bmp != null && Bimp.bmp.size() > 0) {
				photoView.setImageBitmap(Bimp.bmp.get(position));
			}
			// Now just add PhotoView to ViewPager and return it
			if (mSinglePicUrl != null && mSinglePicUrl.length() > 0) {
				ImageLoader.getInstance().displayImage(mSinglePicUrl,
						photoView, ImageLoaderConfigs.displayImageOptionsBg);
			}
			container.addView(layout, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			photoView.setOnViewTapListener(new OnViewTapListener() {
				@Override
				public void onViewTap(View view, float x, float y) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});

			if (mIsDeleteOp) {
				FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) photoView
						.getLayoutParams();
				lp.topMargin = Utils.dipToPixels(mContext, 50);
				photoView.setLayoutParams(lp);
			}

			return layout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
}
