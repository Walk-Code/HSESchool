package com.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.zhuochuang.hsej.R;

@SuppressLint("DrawAllocation")
public class CollapsibleTextView extends LinearLayout implements OnClickListener{

	/** default text show max lines */
	private static final int COLLAPSIBLE_DEFAULT_MAX_LINE_COUNT = 5;
	
	private static final int COLLAPSIBLE_STATE_NONE = 0;
	private static final int COLLAPSIBLE_STATE_SHRINKUP = 1;
	private static final int COLLAPSIBLE_STATE_SPREAD = 2;
	
	private TextView desc;
	private TextView descOp;
	
	private String shrinkup;
	private String spread;
	private int mState;
	private boolean flag;
	
	private OnLayoutChange mOnLayoutChange;
	
	public CollapsibleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		shrinkup = context.getString(R.string.desc_shrinkup);
		spread = context.getString(R.string.desc_spread);
		View view = inflate(context, R.layout.collapsible_textview, this);
		view.setPadding(0, -1, 0, 0);
		desc = (TextView) view.findViewById(R.id.desc_tv);
		descOp = (TextView) view.findViewById(R.id.desc_op_tv);
		descOp.setOnClickListener(this);
	}
	
	public CollapsibleTextView(Context context) {
		this(context, null);
	}
	
	public interface OnLayoutChange{
		public void onLayoutChange(int state);
	}
	
	public void setOnLayoutChange(OnLayoutChange onLayoutChange){
		mOnLayoutChange = onLayoutChange;
	}
	
	public TextView getTextView(){
		return desc;
	}
	
	public final void setText(String str, BufferType bufferType) {
		//desc.setText(EmojiUtils.getEmojiCharSequence(str), bufferType);
		desc.setText(str, bufferType);
		mState = COLLAPSIBLE_STATE_SPREAD;
		flag = false;
		requestLayout();
	}
	
	@Override
	public void onClick(View v) {
		flag = false;
		requestLayout();
		if(mOnLayoutChange != null){
			mOnLayoutChange.onLayoutChange(mState);
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!flag) {
		    flag = true;
		    if(desc.getLineCount() < COLLAPSIBLE_DEFAULT_MAX_LINE_COUNT) {
		        mState = COLLAPSIBLE_STATE_NONE;
		        descOp.setVisibility(View.GONE);
		    }
		    else{
		    	post(new InnerRunnable());
		    }
		}
	}
	
	class InnerRunnable implements Runnable {
		@Override
		public void run() {
		    if (mState == COLLAPSIBLE_STATE_SPREAD) {
		        desc.setMaxLines(COLLAPSIBLE_DEFAULT_MAX_LINE_COUNT);
		        desc.setEllipsize(TruncateAt.END);
		        descOp.setVisibility(View.VISIBLE);
		        descOp.setText(spread);
		        mState = COLLAPSIBLE_STATE_SHRINKUP;
		    }
		    else if (mState == COLLAPSIBLE_STATE_SHRINKUP) {
		        desc.setMaxLines(Integer.MAX_VALUE);
		        descOp.setVisibility(View.VISIBLE);
		        descOp.setText(shrinkup);
		        mState = COLLAPSIBLE_STATE_SPREAD;
		    }
		}
	}
	
	public void expandText(){
		if (mState == COLLAPSIBLE_STATE_SPREAD) {
			desc.setMaxLines(COLLAPSIBLE_DEFAULT_MAX_LINE_COUNT);
	        descOp.setVisibility(View.VISIBLE);
	        descOp.setText(spread);
	        mState = COLLAPSIBLE_STATE_SHRINKUP;
	    }
	}
}