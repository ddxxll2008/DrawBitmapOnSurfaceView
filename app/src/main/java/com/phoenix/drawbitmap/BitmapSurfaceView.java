package com.phoenix.drawbitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.graphics.Bitmap.createBitmap;

/**
 * 自定义控件，在surfaceview上绘制bitmap
 * Created by dingxiaolei on 17/5/27.
 */

public class BitmapSurfaceView extends SurfaceView implements Runnable {
	private static final String TAG = "BitmapSurfaceView";

	private SurfaceHolder mSurfaceHolder;
	private Canvas mCanvas;
	private boolean isRunning;
	private double percent;

	public BitmapSurfaceView(Context context) {
		super(context);
		init();
	}

	public BitmapSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BitmapSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	//初始化
	private void init() {
		isRunning = true;

		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder surfaceHolder) {
				new Thread(BitmapSurfaceView.this).start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

			}

			@Override
			public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
				isRunning = false;
			}
		});
	}

	@Override
	public void run() {
		while (isRunning) {
			drawBitmap(percent);
		}
	}

	private void drawBitmap(double percent) {
		Log.d(TAG, "drawBitmap: " + percent);
		mCanvas = mSurfaceHolder.lockCanvas();
		if (mCanvas != null) {
			try {
				mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				//获取声音波形原始图片
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sound_wave);
				//放大波形图像
				Matrix matrix = new Matrix();
				float widthScale = (float) getWidth() / (float) bitmap.getWidth();
				float heightScale = (float) getHeight() / (float) bitmap.getHeight();
				matrix.postScale(widthScale, heightScale);
				Bitmap newBitmap = createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight()
						, matrix, true);

				int width = (int) (getWidth() * percent);
//				int height = (int) (getHeight() * percent);
				// 指定图片绘制区域
				Rect src = new Rect(0, 0, width, getHeight());
				// 指定图片在屏幕上显示的区域
				Rect dst = new Rect(0, 0, width, getHeight());
				// 绘制图片
				mCanvas.drawBitmap(newBitmap, src, dst, null);
				Log.d(TAG, "width: " + width);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}
}
