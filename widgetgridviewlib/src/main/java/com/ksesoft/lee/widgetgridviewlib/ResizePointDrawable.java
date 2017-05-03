package com.ksesoft.lee.widgetgridviewlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bo17a on 4/27/2017.
 */

public class ResizePointDrawable extends Drawable {
    ColorFilter cf;

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setARGB(100,100,100,100);
        canvas.drawCircle(10,10,10,paint);
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

