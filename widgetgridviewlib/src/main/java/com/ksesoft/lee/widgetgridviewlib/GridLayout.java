package com.ksesoft.lee.widgetgridviewlib;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bo17a on 5/1/2017.
 */

public class GridLayout extends RelativeLayout {
    public int cols, rows;
    int devidedX,devidedY;
    Context context;

    Boolean[] occupiedMatrix;

    private void _init(Context context){
        this.context = context;
    }
    public GridLayout(Context context) {
        super(context);
        _init(context);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _init(context);
    }

    ArrayList<WidgetView> widgets = new ArrayList<WidgetView>();
    boolean _PostInit = false;
    public void AddWidget(WidgetView widget,int col, int row){
        widget.col = col;
        widget.row = row;

        if(_PostInit){
            addView(widget);
            widget.initView();
        }else{
            widgets.add(widget);
        }
    }

    ShadowView shadow;
    GridDrawable bg;
    public void init(int c, int r){
        this.cols = c;
        this.rows = r;
        this.setPadding(0,0,0,0);

        occupiedMatrix = new Boolean[this.cols * this.rows];
        for(int i=0;i<occupiedMatrix.length;i++){
            occupiedMatrix[i] = false;
        }
        this.post(new Runnable() {
            @Override
            public void run() {
            devidedX = getWidth()/cols;
            devidedY = getHeight()/rows;

            bg = new GridDrawable(cols,rows, getWidth(), getHeight());
            shadow = new ShadowView(context);

            shadow.setVisibility(View.INVISIBLE);
            addView(shadow);
            shadow.setX(0);
            shadow.setY(0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(bg);
            }else{
                setBackgroundDrawable(bg);
            }

            //  init all children
            //for(int index=0; index<getChildCount(); ++index) {
            for(int index=0; index<widgets.size(); ++index) {
                WidgetView childView = widgets.get(index);

                //  init a view
                if (childView instanceof WidgetView){
                    WidgetView widget = (WidgetView)childView;
                    addView(widget);
                    widget.initView();
                }
            }

            _PostInit = true;
            }
        });

        setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.d("test", "started 1.");
                int row,col;
                float newX,newY;
                WidgetView from;

                switch(event.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        HashMap<String,Object> localState = (HashMap<String,Object>) event.getLocalState();
                        from = (WidgetView)localState.get("view");
                        col = Math.round((event.getX()-from.getWidth()/2)/devidedX);
                        newX = col*devidedX;

                        row = Math.round((event.getY())/devidedY);
                        newY = row*devidedY;

                        if(col <0 || col+from.w_col>cols){
                            return true;
                        }
                        if(row <0 || row+from.h_col>rows){
                            return true;
                        }

                        if(isFreeSpace(col,row, from.w_col,from.h_col)){
                            shadow.current_col = col;
                            shadow.current_row = row;
                            shadow.setX(newX);
                            shadow.setY(newY);
                            shadow.setVisibility(View.VISIBLE);
                        }

                        Log.d("test", "ACTION_DRAG_LOCATION_2. X: "+ event.getX()+" Y: "+ event.getY()); // 量が多いのでログは出さない
                        return true;

                    case DragEvent.ACTION_DROP:
                        HashMap<String,Object> localState2 = (HashMap<String,Object>)event.getLocalState();
                        from = (WidgetView)localState2.get("view");
                        float oldX = from.getX();
                        newX = event.getX();

                        Log.d("test","From: "+oldX+ " To: "+newX);

                        col = Math.round((event.getX()-from.getWidth()/2)/devidedX);
                        row = Math.round((event.getY()-from.getHeight()/2)/devidedY);

                        //from.setX( event.getX()-from.getWidth()/2);
                        //from.setY(event.getY()-from.getHeight()/2);
                        from.setColumn(shadow.current_col);
                        from.setRow(shadow.current_row);

                        shadow.setVisibility(View.INVISIBLE);
                        Log.d("test", "ACTION_DROP.");
                        return true;
                };
                return false;
            }
        });
    }

    public boolean isFreeSpace(int col_num, int row_num, int w_col, int h_col){
        for(int i=col_num;i<col_num+w_col;i++){
            for(int j=row_num;j<row_num+h_col;j++){
                if(occupiedMatrix[i*this.cols+j]){
                    return false;
                }
            }
        }

        return true;
    }
}
