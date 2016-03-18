package com.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.layout.emoji.EmojiFragment;
import com.util.Utils;
import com.zhuochuang.hsej.R;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class CustomCommentView extends LinearLayout{

	private View mViewGroup;
	private EditText mEditView;
	
	private View mEmojiView;
	private LinearLayout mEmojiLayout;
	private EmojiFragment mEmojiFragment;
	private OnClickListener mListener;
	
	public CustomCommentView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomCommentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		initCustomView(context);
	}
	
	private void initCustomView(final Context context){
		mViewGroup = LayoutInflater.from(context).inflate(R.layout.custom_commentview, null);
		addView(mViewGroup);
		
		mViewGroup.findViewById(R.id.custom_sendview).setEnabled(false);
		mEditView = (EditText) mViewGroup.findViewById(R.id.custom_edittext);
		mEditView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String content = mEditView.getText().toString();
				if(content == null || content.length() == 0 || content.replaceAll(" ", "").length() == 0){
					((TextView)mViewGroup.findViewById(R.id.custom_sendview)).setTextColor(Color.argb(255, 54, 54, 54));
					mViewGroup.findViewById(R.id.custom_sendview).setBackgroundResource(R.drawable.send_bg_gray);
					mViewGroup.findViewById(R.id.custom_sendview).setEnabled(false);
				}
				else{
					((TextView)mViewGroup.findViewById(R.id.custom_sendview)).setTextColor(Color.argb(255, 241, 241, 241));
					mViewGroup.findViewById(R.id.custom_sendview).setBackgroundResource(R.drawable.btn_red_selector);
					mViewGroup.findViewById(R.id.custom_sendview).setEnabled(true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mEditView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(mEmojiLayout.isShown()){
					mEmojiLayout.setVisibility(View.GONE);
					mEmojiView.setBackgroundResource(R.drawable.btn_expression);
				}
				return false;
			}
		});
		
		mEditView.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					mEmojiLayout.setVisibility(View.GONE);
					mEmojiView.setBackgroundResource(R.drawable.btn_expression);
				}
			}
		});
		
		mEmojiLayout = (LinearLayout) mViewGroup.findViewById(R.id.custom_emoji_layout);
		mEmojiView = mViewGroup.findViewById(R.id.custom_emojiview);
		mEmojiView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mEmojiLayout.isShown()){
					mEmojiView.setBackgroundResource(R.drawable.btn_expression);
					mEmojiLayout.setVisibility(View.GONE);
					InputMethodManager inputManager = (InputMethodManager) mEditView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				    inputManager.showSoftInput(mEditView, 0);
				}
				else{
					mEditView.requestFocus();
					mEmojiView.setBackgroundResource(R.drawable.btn_keyboard);
					Utils.removeSoftKeyboard((Activity) context);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mEmojiLayout.setVisibility(View.VISIBLE);
						}
					}, 50);
				}
			}
		});
		
		mEmojiFragment = (EmojiFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.emoji_fragment);
		mEmojiFragment.setEditTextHolder(mEditView);
	}

	public void requestFouce(){
		if(mEditView != null){
			mEditView.requestFocus();
			InputMethodManager inputManager = (InputMethodManager) mEditView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		    inputManager.showSoftInput(mEditView, 0);
		    
		    if(mEmojiLayout.isShown()){
				mEmojiLayout.setVisibility(View.GONE);
				mEmojiView.setBackgroundResource(R.drawable.btn_expression);
			}
		}
	}
	
	public void hideKeyBoard(Context context){
		if(mEmojiLayout != null && mEmojiLayout.isShown()){
			mEmojiView.setBackgroundResource(R.drawable.btn_expression);
			mEmojiLayout.setVisibility(View.GONE);
		}

		Utils.removeSoftKeyboard((Activity) context);
	}
	
	public void resetCustomEditview(Context context){
		if(mEditView != null){
			setEditViewHint(getResources().getString(R.string.customcomment_hint));
			setEditViewText("");
		}
		Utils.removeSoftKeyboard((Activity) context);
	}
	
	public void setEditViewHint(String hint){
		if(mEditView != null){
			mEditView.setHint(hint);
		}
	}
	
	public void setEditViewText(String text){
		if(mEditView != null){
			mEditView.setText(text);
		}
	}
	
	public EditText getEditView(){
		return mEditView;
	}
	
	public void setOnSendListener(OnClickListener listener){
		mListener = listener;
		mViewGroup.findViewById(R.id.custom_sendview).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mListener.onClick(arg0);
			}
		});
	}
}
