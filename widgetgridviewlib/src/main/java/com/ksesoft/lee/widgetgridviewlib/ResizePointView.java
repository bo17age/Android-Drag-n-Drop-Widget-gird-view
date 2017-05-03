package com.ksesoft.lee.widgetgridviewlib;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by bo17a on 4/27/2017.
 */

public class ResizePointView extends View {

    public ResizePointView(Context context) {
        super(context);
        _init();
    }

    public ResizePointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init();
    }

    public ResizePointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init();
    }

    private void _init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground( new ResizePointDrawable());
        }else{
            setBackgroundDrawable( new ResizePointDrawable());
        }
    }

    private int myPosition = 0;
    public void setPosition(int p){
        myPosition = p;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)this.getLayoutParams();
        switch (p){
            case 1:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case 2:
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            case 3:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case 4:
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup.LayoutParams params;
        WidgetView parent = (WidgetView)this.getParent();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (myPosition){
                    case 1:
                        parent.startDrag();
                        break;
                    case 4:
                        parent.selfRemove();
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                switch (myPosition){
                    case 2:
                        params =  parent.getLayoutParams();
                        parent.resizeX(event.getX());
                        //parent.setLayoutParams(params);

                        //  TODO: calc new width to set col for parent view
                        break;
                    case 3:
                        params = parent.getLayoutParams();
                        parent.resizeY(event.getY());
                        //parent.setLayoutParams(params);
                        break;
                }
                // do something
                break;
            case MotionEvent.ACTION_UP:
                //do something
                break;
        }
        return true;
    }


}
