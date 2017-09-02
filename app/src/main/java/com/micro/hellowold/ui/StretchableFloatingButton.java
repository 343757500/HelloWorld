package com.micro.hellowold.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by zsp on 2017/8/31.
 */

public class StretchableFloatingButton extends ViewGroup{
    private boolean isIncreate=true;
    private RectF rectF;
    private int width;
    private int heigth;
    private int center;
    private int x;
    Paint paint=new Paint();

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1://递减状态
                    x-=30;
                    if (x>=center+30){
                        handler.sendEmptyMessageDelayed(1,1);
                    }else{
                        //动画结束，恢复默认状态
                        x=center;
                        setEnabled(true);
                    }
                    break;
                case 2://递增状态
                    x+=30;
                    if (x<width-center){
                        handler.sendEmptyMessageDelayed(2,1);

                    }else{
                        x=width-center;
                        setEnabled(true);
                    }

                    break;
            }


            invalidate();


        }
    };


    public StretchableFloatingButton(Context context) {
        super(context);
    }

    public StretchableFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchableFloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (width==0) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            heigth = MeasureSpec.getSize(heightMeasureSpec);

            //获取圆的半径
            center = heigth/2;

            //获取矩形右边的x坐标
            x = width - center;

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

        //画左边圆
        canvas.drawCircle(center,center,center,paint);

        //画矩形
        rectF = new RectF(center,0,x,heigth);
        canvas.drawRect(rectF,paint);

        //画右边圆
        canvas.drawCircle(x,center,center,paint);
        super.dispatchDraw(canvas);
    }



    private void startDecreate(){
        setEnabled(false);
        isIncreate = false;
        handler.sendEmptyMessageDelayed(1,40);
    }

    private void startIncreate(){
        setEnabled(false);
        isIncreate=true;
        handler.sendEmptyMessageDelayed(2,40);
    }


    public void onStartOnclick(){
        if (isIncreate){
            startDecreate();
        }else {

            startIncreate();
        }
    }

}
