package com.ksesoft.lee.widgetgridviewlib;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bo17a on 4/27/2017.
 */

public class ShadowView extends View {
    public int current_col, current_row;

    public ShadowView(Context context) {
        super(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(new Drawable() {
                @Override
                public void draw(Canvas canvas) {
                    Paint paint = new Paint();
                    paint.setARGB(30,30,30,30);
                    canvas.drawRect(0,0, canvas.getWidth(), canvas.getHeight(), paint);
                }

                @Override
                public void setAlpha(int i) {

                }

                @Override
                public void setColorFilter(ColorFilter colorFilter) {

                }

                @Override
                public int getOpacity() {
                    return PixelFormat.OPAQUE;
                }
            });
        }else{
            this.setBackgroundDrawable(new Drawable() {
                @Override
                public void draw(Canvas canvas) {
                    Paint paint = new Paint();
                    paint.setARGB(30,30,30,30);
                    canvas.drawRect(0,0, canvas.getWidth(), canvas.getHeight(), paint);
                }

                @Override
                public void setAlpha(int i) {

                }

                @Override
                public void setColorFilter(ColorFilter colorFilter) {

                }

                @Override
                public int getOpacity() {
                    return PixelFormat.OPAQUE;
                }
            });
        }
    }

    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
