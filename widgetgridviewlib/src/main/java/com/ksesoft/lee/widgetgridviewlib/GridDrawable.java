package com.ksesoft.lee.widgetgridviewlib;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

/**
 * Created by bo17a on 4/26/2017.
 */

public final class GridDrawable extends Drawable {
    private ColorFilter cf;
    private int cols;
    private int rows;
    private int width;
    private int height;
    private int devidedX;
    private int devidedY;

    public GridDrawable(int cols, int rows, int width, int height){
        this.rows = rows;
        this.cols = cols;
        this.width = width;
        this.height = height;

        this.devidedX = width/cols;
        this.devidedY = height/rows;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint rectanglePaint = new Paint();
        rectanglePaint.setARGB(80, 80, 40, 0);
        rectanglePaint.setStrokeWidth(2);
        rectanglePaint.setStyle(Paint.Style.FILL);

        for(int i=0;i<=this.cols;i++){
            android.graphics.Path path = new android.graphics.Path();
            path.setLastPoint(devidedX*i, 0);
            path.lineTo(devidedX*i, height);
            canvas.drawLine(i*devidedX,0,i*devidedX, height,rectanglePaint);
        }
        for(int i=0;i<=this.rows;i++){
            android.graphics.Path path = new android.graphics.Path();
            path.setLastPoint(devidedY*i, 0);
            path.lineTo(devidedY*i, height);
            canvas.drawLine(0,i*devidedY, width, i*devidedY,rectanglePaint);
        }
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.cf = colorFilter;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
