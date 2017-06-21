package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vnetoo.test.MyApplication;

/**
 * Created by Administrator on 2017/5/18.
 */

public class CustomView extends View implements Runnable{

    Paint mPaint;
    Context mContext;

    final float RADIUS = 100;
    float mRadius = RADIUS;

    public CustomView(Context context) {
        super(context);
        initPaint();
        mContext = context;
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 初始化画笔
        initPaint();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        // 初始化画笔
        initPaint();
    }

    // 初始化画笔
    private void initPaint() {
        // 实例化画笔并打开抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        /*
         * 设置画笔样式为描边，圆环嘛……当然不能填充不然就么意思了
         *
         * 画笔样式分三种：
         * 1.Paint.Style.STROKE：描边
         * 2.Paint.Style.FILL_AND_STROKE：描边并填充
         * 3.Paint.Style.FILL：填充
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);

        /*
         * 设置描边的粗细，单位：像素px
         * 注意：当setStrokeWidth(0)的时候描边宽度并不为0而是只占一个像素
         */
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircle(canvas);
        drawCircle2(canvas);
        drawCircle3(canvas);
    }

    private void drawCircle(Canvas canvas) {
        float x = MyApplication.sScreenWidth /2;
//        float y = MyApplication.sScreenHeight /2;
        float y = RADIUS;
        canvas.drawCircle(x,y,mRadius,mPaint);
    }

    private void drawCircle2(Canvas canvas) {
        // 设置画笔颜色为自定义颜色
        mPaint.setColor(Color.argb(255, 255, 128, 103));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        float x = MyApplication.sScreenWidth /2;
//        float y = MyApplication.sScreenHeight /2;
        float y = 3 * RADIUS + 10;
        canvas.drawCircle(x,y,mRadius,mPaint);
    }

    private void drawCircle3(Canvas canvas) {
        // 生成色彩矩阵
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.5F, 0, 0, 0, 0,
                0, 0.5F, 0, 0, 0,
                0, 0, 0.5F, 0, 0,
                0, 0, 0, 1, 0,
        });
        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        float x = MyApplication.sScreenWidth /2;
//        float y = MyApplication.sScreenHeight /2;
        float y = 5 * RADIUS + 20;
        canvas.drawCircle(x,y,mRadius,mPaint);
    }

    @Override
    public void run() {
         /*
         * 确保线程不断执行不断刷新界面
         */
        while (true){
            try {
                if (mRadius > 0){
                    mRadius -= 10;
                    postInvalidate();
                }else {
                    mRadius = RADIUS;
                }
                // 每执行一次暂停40毫秒
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
