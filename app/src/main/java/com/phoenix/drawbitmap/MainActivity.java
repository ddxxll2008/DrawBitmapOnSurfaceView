package com.phoenix.drawbitmap;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	private BitmapSurfaceView bitmapSurfaceView;
	private double spaceNumber = 0.01;
	private double percent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bitmapSurfaceView = (BitmapSurfaceView) findViewById(R.id.surfaceview);

		//设置SurfaceView背景透明
		bitmapSurfaceView.setZOrderOnTop(true);
		bitmapSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

		percent = 0.01;
		new RandomNumberThread().start();
	}

	public class RandomNumberThread extends Thread {
		@Override
		public void run() {
			while (true) {
				bitmapSurfaceView.setPercent(percent);
				percent = percent + spaceNumber;
				if (percent >= 1) {
					spaceNumber = -0.01;
				} else if (percent <= 0.01){
					spaceNumber = 0.01;
				}
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
