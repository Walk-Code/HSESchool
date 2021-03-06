package com.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuochuang.hsej.R;
/**
 * 
 * @author Dion
 *
 */
public class CustomListCell extends RelativeLayout implements OnClickListener{
	
	private ImageView mImageView = null;
	private TextView mTitleView = null;
	private TextView mSubTitleView = null;
	private Button mButton = null;
	private TextView mTextView = null;
	private FrameLayout mFrameLayout = null;
	
	private Context mContext = null;
	private View view = null;
	
	private boolean mShowButton = false;
	private boolean mShowArrow = false;
	
	private boolean isClick = false;
	
	private onButtonListener mButtonListener = null;//回调

	public CustomListCell(Context context) {
		super(context, null);
	}

	public CustomListCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		
		view = inflate(context,R.layout.listcell_custom, this);
		mImageView = (ImageView)view.findViewById(R.id.image_custom);
		mTitleView = (TextView)view.findViewById(R.id.title_custom);
		mSubTitleView = (TextView)view.findViewById(R.id.subtitle_custom);
		
		mButton = (Button)view.findViewById(R.id.button_custom);
		mButton.setOnClickListener(this);
		
		mTextView = (TextView)view.findViewById(R.id.text_custom);
		mFrameLayout = (FrameLayout) view.findViewById(R.id.framelayout_custom);
		
		getAttrs(context, attrs);
	}
	
	/**
	 * 从xml中读取设置的属性
	 * @param context
	 * @param attrs 属性数组
	 */
	 private void getAttrs(Context context, AttributeSet attrs) {
		 TypedArray tArray = context.obtainStyledAttributes(attrs,
	                R.styleable.CustomListCell);
		 
			for(int i = 0; i <tArray.getIndexCount(); i++){
				int index = tArray.getIndex(i);
				switch (index) {
				case R.styleable.CustomListCell_showArrow:
					mShowArrow = tArray.getBoolean(index, false);
					break;
				case R.styleable.CustomListCell_showButton:
					mShowButton = tArray.getBoolean(index, false);
					break;
				default:
					break;
				}
			}
			
			tArray.recycle();
	}

	
	@Override
	public void onClick(View v) {
		
		if(mButtonListener != null){
			mButtonListener.OnButtonListener();
		}
	}
	
	//回调
	public interface onButtonListener{
		public void OnButtonListener();
	}
	
	public void setOnButtonListener (onButtonListener listener){
		this.mButtonListener = listener;
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(!isClick){
			isClick = true;
			post(new Runnable() {
				
				@Override
				public void run() {
					
					if(mShowArrow){
						mFrameLayout.setVisibility(View.VISIBLE);
					}else if(mShowButton){
						mButton.setVisibility(View.VISIBLE);
					}
				}
			});
		}
		
	}
	
	public ImageView getImageView (){
		return mImageView;
	}
	
	public Button getButtonView (){
		if(mButton == null){
			return mButton = (Button)view.findViewById(R.id.button_custom);
		}
		return mButton;
	}
	
	public TextView getTextView (){
		if(mTextView == null){
			return mTextView = (TextView)view.findViewById(R.id.text_custom);
		}
		return mTextView;
	}
	
	public void setTitleView (int resource){
		mTitleView.setText(resource);
		requestLayout();  
	    invalidate(); 
	}
	
	
	public void setTitleView (String text){
		mTitleView.setText(text);
		requestLayout();  
	    invalidate(); 
	}
	
	
	public void setSubTitleView (int resource){
		mSubTitleView.setText(resource);
		requestLayout();  
	    invalidate(); 
	}
	
	
	public void setSubTitleView (String text){
		mSubTitleView.setText(text);
		requestLayout();  
	    invalidate(); 
	}
	
	public TextView getTitleText(){
		return mTitleView;
	}
	
	public TextView getSubText(){
		return mSubTitleView;
	}
}
