package com.layout.qrcode;

import java.util.Collection;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.zhuochuang.hsej.R;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
@SuppressLint("DrawAllocation")
public final class ViewfinderView extends View {
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;
	private static final int MIDDLE_LINE_WIDTH = 6;
	private static final int MIDDLE_LINE_PADDING = 5;
	private static final int SPEEN_DISTANCE = 5;
	private static final int TEXT_SIZE = 14;
	private static final int TEXT_PADDING_TOP = 30;
	
	private static final int CORNER_WIDTH = 7;
	private static final int CORNER_LENGTH = 15;
	
	private static float density;
	private Paint paint;
	private int ScreenRate;
	private int slideTop;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	boolean isFirst;
	
	private Context context;
	private int mWidth;
	private int topIntravel = 0;
	
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		density = context.getResources().getDisplayMetrics().density;
		ScreenRate = (int)(CORNER_LENGTH * density);
		mWidth = context.getResources().getDisplayMetrics().widthPixels;

		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);

		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	public void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		
		if(!isFirst){
			//frame.top = frame.top - topIntravel;
			//frame.bottom = frame.bottom - topIntravel;
			
			isFirst = true;
			slideTop = frame.top - topIntravel;
		}
		
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		
		canvas.drawRect(0, 0, width, frame.top- topIntravel, paint);
		canvas.drawRect(0, frame.top- topIntravel, frame.left, frame.bottom- topIntravel + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top- topIntravel, width, frame.bottom- topIntravel + 1, paint);
		canvas.drawRect(0, frame.bottom- topIntravel + 1, width, height, paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} 
		else {
			//paint.setColor(Color.GREEN);
			//paint.setColor(Color.WHITE);
			paint.setColor(Color.argb(255, 98, 227, 0));
			canvas.drawRect(frame.left, frame.top - topIntravel, frame.left + ScreenRate,
					frame.top - topIntravel + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top - topIntravel, frame.left + CORNER_WIDTH, frame.top - topIntravel
					+ ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top - topIntravel, frame.right,
					frame.top- topIntravel + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top- topIntravel, frame.right, frame.top- topIntravel
					+ ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom- topIntravel - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom- topIntravel, paint);
			canvas.drawRect(frame.left, frame.bottom- topIntravel - ScreenRate,
					frame.left + CORNER_WIDTH, frame.bottom- topIntravel, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom - topIntravel- CORNER_WIDTH,
					frame.right, frame.bottom- topIntravel, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom- topIntravel - ScreenRate,
					frame.right, frame.bottom- topIntravel, paint);
			
			//lines
			slideTop += SPEEN_DISTANCE;
			if(slideTop >= (frame.bottom - topIntravel)){
				slideTop = frame.top - topIntravel;// - topIntravel;
			}
			
			Rect lineRect = new Rect();
			lineRect.left = frame.left;
			lineRect.right = frame.right;
			lineRect.top = slideTop;
			lineRect.bottom = slideTop + 3;
			canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R.drawable.scanning_line))).getBitmap(), null, lineRect, paint);
			
			//canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH/4, frame.right - MIDDLE_LINE_PADDING,slideTop + MIDDLE_LINE_WIDTH/4, paint);

			//canvas.draw
			
			paint.setColor(Color.WHITE);
			paint.setTextSize(TEXT_SIZE * density);
			paint.setAntiAlias(true);
		//	paint.setAlpha(0x40);
			//paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			paint.setTypeface(Typeface.create("System", Typeface.NORMAL));
			
			int margin = mWidth - (int) paint.measureText(getResources().getString(R.string.qrscan_text));
			canvas.drawText(getResources().getString(R.string.qrscan_text), margin / 2, frame.bottom + TEXT_PADDING_TOP *density, paint);
			
			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if(currentPossible.isEmpty()){
				lastPossibleResultPoints = null;
			} 
			else{
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top - topIntravel + point.getY(), 6.0f, paint);
				}
			}
			if(currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top - topIntravel + point.getY(), 3.0f, paint);
				}
			}
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top - topIntravel, frame.right, frame.bottom- topIntravel);
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
