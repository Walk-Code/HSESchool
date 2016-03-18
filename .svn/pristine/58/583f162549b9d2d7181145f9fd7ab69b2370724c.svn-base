package com.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class ConsumeTextView extends TextView {
	boolean dontConsumeNonUrlClicks = true;
	boolean linkHit;
	private static TouchableSpan touchableSpan = null;
	private Context context;

	public ConsumeTextView(Context context) {
		super(context);
		this.context = context;
	}

	public ConsumeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public ConsumeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		linkHit = false;
		boolean res = super.onTouchEvent(event);
		if (dontConsumeNonUrlClicks){
			return linkHit;
		}
		return res;
	}

	public void setTextSp(SpannableString sp){
		//CharSequence spanned = Html.fromHtml(sp, emojiGetter, null);
		//desc.setText(spanned, bufferType);
		setText(sp);
	}

	public static class LocalLinkMovementMethod extends LinkMovementMethod{
		static LocalLinkMovementMethod sInstance;
	
		public static LocalLinkMovementMethod getInstance() {
		if (sInstance == null){
			sInstance = new LocalLinkMovementMethod();
		}
		return sInstance;
	}

	@Override
	public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
		int action = event.getAction();
	
		if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_CANCEL) {
			int x = (int) event.getX();
			int y = (int) event.getY();
		
			x -= widget.getTotalPaddingLeft();
			y -= widget.getTotalPaddingTop();
		
			x += widget.getScrollX();
			y += widget.getScrollY();
		
			Layout layout = widget.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line, x);
		
			TouchableSpan[] link = buffer.getSpans(off, off, TouchableSpan.class);
			if(action == MotionEvent.ACTION_CANCEL){
				if(touchableSpan != null){
					touchableSpan.setPressed(false);
					Selection.setSelection(buffer, buffer.getSpanStart(touchableSpan), buffer.getSpanEnd(touchableSpan));
					Selection.removeSelection(buffer);
				}
			}
			if (link.length != 0) {
				if (action == MotionEvent.ACTION_UP) {
					link[0].setPressed(false);
					link[0].onClick(widget);
					Selection.removeSelection(buffer);
					touchableSpan = null;
				} 
				else if (action == MotionEvent.ACTION_DOWN) {
					link[0].setPressed(true);
					Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
					touchableSpan = link[0];
				}
				
				if (widget instanceof ConsumeTextView){
					((ConsumeTextView) widget).linkHit = true;
				}
				return true;
			} 
			else {
				Selection.removeSelection(buffer);
				Touch.onTouchEvent(widget, buffer, event);
				return false;
			}
		}
		return Touch.onTouchEvent(widget, buffer, event);
	   }
	}
}
