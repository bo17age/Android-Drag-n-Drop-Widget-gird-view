package com.ksesoft.lee.widgetgridviewlib;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;

/**
 * Created by bo17a on 4/27/2017.
 */

public class WidgetView extends RelativeLayout {
    Context context;
    public int col,row;
    int w_col, h_col;
    public GridLayout layout;
    public RelativeLayout child;

    public WidgetView(Context context, int w_col, int h_col) {
        super(context);
        _init(context,w_col,h_col);
    }

    public WidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context,1,1);
    }

    public WidgetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _init(context,1,1);
    }

    public  void  initView(){
        layout = (GridLayout)this.getParent();
        //ViewGroup.LayoutParams parent_params = layout.getLayoutParams();
        ViewGroup.LayoutParams params = this.getLayoutParams();

        params.width = layout.devidedX*w_col;
        params.height = layout.devidedY*h_col;

        setColumn(col);
        setRow(row);

    }

    public void resizeX(float rx){
        int new_w_col = Math.round((this.getWidth()+rx)/layout.devidedX);
        if(new_w_col != w_col && new_w_col>0){
            freeCurrentCells();
            if(layout.isFreeSpace(col,row,new_w_col,h_col)){
                ViewGroup.LayoutParams params = this.getLayoutParams();
                params.width = new_w_col*layout.devidedX;
                this.setLayoutParams(params);
                w_col = new_w_col;
            }
            occupyCells();
        }
    }
    public void resizeY(float ry){
        int new_h_col = Math.round((this.getHeight()+ry)/layout.devidedY);
        if(new_h_col != h_col && new_h_col>0){
            freeCurrentCells();
            if(layout.isFreeSpace(col,row,w_col,new_h_col)){
                ViewGroup.LayoutParams params = this.getLayoutParams();
                params.height = new_h_col*layout.devidedY;
                this.setLayoutParams(params);
                h_col = new_h_col;
                occupyCells();
            }
            occupyCells();
        }
    }


    //
    public void setColumn(int x) {
        //  free current cells
        freeCurrentCells();

        this.setX( (float)layout.devidedX*x);
        this.col = x;

        //  occupy new cells
        occupyCells();

        Log.d("INFO","setColumn: "+this.getX());
    }

    public void setRow(int y) {
        //  free current cells
        freeCurrentCells();

        this.setY((float)layout.devidedY*y);
        this.row = y;

        //  occupy new cells
        occupyCells();
        Log.d("INFO","setRow: "+this.getY());
    }

    private void freeCurrentCells(){
        //  free current cells
        for(int i=col;i<col+w_col;i++){
            for(int j=row;j<row+h_col;j++){
                layout.occupiedMatrix[i*layout.cols+j] = false;
            }
        }
    }

    private void occupyCells(){
        for(int i=this.col;i<this.col+w_col;i++){
            for(int j=this.row;j<this.row+h_col;j++){
                layout.occupiedMatrix[i*layout.cols+j] = true;
            }
        }
    }

    private View view;
    public void setView(View view){
        this.view = view;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        this.view.setLayoutParams(lp);
        this.child.addView(this.view);
        RelativeLayout.LayoutParams view_params = (RelativeLayout.LayoutParams) this.view.getLayoutParams();
        view_params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.view.setLayoutParams(view_params);

        /*setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // データ準備
                View tv = (View)v;
                if( v instanceof ResizePointView){
                    return false;
                }
                ClipData data = ClipData.newPlainText("test", "drag:" + tv. getId());
                // ドラッグ開始
                HashMap<String, Object> localState = new HashMap<String, Object>();
                localState.put("cols",layout.cols);
                localState.put("rows",layout.rows);
                localState.put("view",v);
                localState.put("viewid",v.getId());
                localState.put("dragShadowBuilder",new MyDragShadowBuilder(v));
                v.startDrag(data, (MyDragShadowBuilder)localState.get("dragShadowBuilder"), localState, 0);

                freeCurrentCells();

                // ドラッグ中を示す印として、青色ボーダーに変更する
                //tv.setBorderColor(0xFF0000FF);
                return true;
            }
        });*/

        setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.d("test", "started 2.");
                if( v instanceof ResizePointView){
                    return false;
                }
                HashMap<String,Object> localState = (HashMap<String,Object>)event.getLocalState();
                View from = (View)localState.get("view");
                View to = v;

                //Log.d("Lee","v.id: "+v.getId()+" d.id: "+((View) event.getLocalState()).getId());
                if(from.getId() != to.getId()){
                    return false;
                }

                String pos = String.format("%s -> %s [%.2f, %.2f] ",
                        ""+from.getId(), ""+to.getId(), event.getX(), event.getY());
                switch(event.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
                        // 自分自身へはドロップしない
                        Log.d("test", pos + "drag started.");

                        ViewGroup.LayoutParams shadowParams = layout.shadow.getLayoutParams();
                        shadowParams.width = from.getWidth();
                        shadowParams.height = from.getHeight();
                        layout.shadow.setLayoutParams(shadowParams);

                        // ドロップを受け付ける印として、黄色ボーダーに変更する
                        //to.setBorderColor(0xFFFFFF00);
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:


                        Log.d("test", pos + "drag ended.");

                        // ボーダー色を元に戻す
                        //to.setBorderColor(0xFF000000);
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        //  give this to layout
                        return false;
                    case DragEvent.ACTION_DROP:
                        //  give this to layout
                    return false;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d("test", pos + "drag entered.");
                        // ドロップ領域に侵入した印として、赤色ボーダーに変更する
                        //to.setBorderColor(0xFFFF0000);
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d("test", pos + "drag exited.");
                        // ボーダー色を元に戻す
                        //to.setBorderColor(0xFFFFFF00);
                        return true;
                }
                // TODO Auto-generated method stub
                return false;
            }
        });
    }

    private void _init(Context context, int w_col, int h_col){
        this.context = context;
        this.w_col = w_col;
        this.h_col = h_col;

        int w = 20;
        int h = 20;

        child = new RelativeLayout(context);
        this.addView(child);
        child.setBackgroundColor(0xFF00FF00);
        RelativeLayout.LayoutParams childParamns = (RelativeLayout.LayoutParams)child.getLayoutParams();
        childParamns.addRule(RelativeLayout.CENTER_IN_PARENT);
        childParamns.setMargins(10,10,10,10);
        //  do we need this?
        // child.setLayoutParams(childParamns);

        //  TOP
        ResizePointView resize_top = new ResizePointView(this.getContext());
        this.addView(resize_top);
        resize_top.getLayoutParams().width = w;
        resize_top.getLayoutParams().height = h;
        resize_top.setPosition(1);

        //  RIGHT
        ResizePointView resize_right = new ResizePointView(this.getContext());
        this.addView(resize_right);
        resize_right.getLayoutParams().width = w;
        resize_right.getLayoutParams().height = h;
        resize_right.setPosition(2);

        //  BOTTOM
        ResizePointView resize_bottom = new ResizePointView(this.getContext());
        this.addView(resize_bottom);
        resize_bottom.getLayoutParams().width = w;
        resize_bottom.getLayoutParams().height = h;
        resize_bottom.setPosition(3);

        //  LEFT
        ResizePointView resize_left = new ResizePointView(this.getContext());
        this.addView(resize_left);
        resize_left.getLayoutParams().width = w;
        resize_left.getLayoutParams().height = h;
        resize_left.setPosition(4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(new Drawable() {
                @Override
                public void draw(Canvas canvas) {
                    int w = getWidth();
                    int h = getHeight();

                    Paint mPaint = new Paint();
                    // mPaint.setColor(0xFFDD0000); // 赤
                    mPaint.setColor(0xFF000000); // 黒
                    mPaint.setStrokeWidth(5);
                    mPaint.setAntiAlias(true);
                    mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(0, 0, w, h, mPaint);
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

    public void startDrag(){
        ClipData data = ClipData.newPlainText("test", "drag:" + this. getId());
        // ドラッグ開始
        HashMap<String, Object> localState = new HashMap<String, Object>();
        localState.put("cols",layout.cols);
        localState.put("rows",layout.rows);
        localState.put("view",this);
        localState.put("viewid",this.getId());
        localState.put("dragShadowBuilder",new MyDragShadowBuilder(this));
        freeCurrentCells();

        super.startDrag(data, (MyDragShadowBuilder)localState.get("dragShadowBuilder"), localState, 0);
    }
    public void selfRemove(){
        freeCurrentCells();
        layout.removeView(this);
    }
}
