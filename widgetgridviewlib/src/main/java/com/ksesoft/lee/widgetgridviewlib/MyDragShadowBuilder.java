package com.ksesoft.lee.widgetgridviewlib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.RelativeLayout;

public class MyDragShadowBuilder extends DragShadowBuilder {
	Paint mPaint;

	public MyDragShadowBuilder(View view){
		super(view);

		// 描画準備
		mPaint = new Paint();
		// mPaint.setColor(0xFFDD0000); // 赤
		// mPaint.setColor(0xFF000000); // 黒
		mPaint.setColor(0xFF00FFFF); // 青緑
		mPaint.setStrokeWidth(5);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
	}

	@Override
	public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
		super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
        //shadowSize.set( getView().getWidth()+10,10);
		//this.shadowTouchPoint = shadowTouchPoint;
		shadowTouchPoint.y = 0;
	}

	@Override
	public void onDrawShadow(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDrawShadow(canvas);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);
	}
}
