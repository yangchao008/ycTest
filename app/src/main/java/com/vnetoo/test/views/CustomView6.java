package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.vnetoo.test.MyApplication;
import com.vnetoo.test.R;

/**
 * Created by Administrator on 2017/5/18.
 */
public class CustomView6 extends View {

    private final String TAG = "CustomView6";
    private static final int MIN_MOVE_DIS = 5;// 最小的移动距离：如果我们手指在屏幕上的移动距离小于此值则不会绘制

    private Bitmap fgBitmap, bgBitmap;// 前景橡皮擦的Bitmap和背景我们底图的Bitmap
    private Paint mPaint;// 橡皮檫路径画笔

    private int screenW, screenH,left,top,bgLeft,bgTop;// 屏幕宽高
    private float preX, preY;// 记录上一个触摸事件的位置坐标

    public CustomView6(Context context) {
        super(context);
        init(context);
    }

    public CustomView6(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 初始化
        init(context);
    }

    public CustomView6(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化
        init(context);
    }

    private void init(Context context) {
        screenW = MyApplication.sScreenWidth;
        screenH = MyApplication.sScreenHeight;

        // 实例化画笔并开启其抗锯齿和抗抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 生成前景图Bitmap
        fgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_02);

        // 获取背景底图Bitmap
        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_01);

        resetPosition();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景
        canvas.drawBitmap(bgBitmap, bgLeft, bgTop, mPaint);
        // 绘制前景
        canvas.drawBitmap(fgBitmap, left, top, mPaint);

        /*
         * 这里要注意canvas和mCanvas是两个不同的画布对象
         * 当我们在屏幕上移动手指绘制路径时会把路径通过mCanvas绘制到fgBitmap上
         * 每当我们手指移动一次均会将路径mPath作为目标图像绘制到mCanvas上，而在上面我们先在mCanvas上绘制了中性灰色
         * 两者会因为DST_IN模式的计算只显示中性灰，但是因为mPath的透明，计算生成的混合图像也会是透明的
         * 所以我们会得到“橡皮擦”的效果
         */
//        canvas.drawPath(mPath, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         * 获取当前事件位置坐标
         */
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 手指接触屏幕重置路径
//                mPath.reset();
//                mPath.moveTo(x, y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:// 手指移动时连接路径
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx >= MIN_MOVE_DIS || dy >= MIN_MOVE_DIS) {
//                    mPath.quadTo(preX, preY, x , y);
                    getPosition(x,y);
                    preX = x;
                    preY = y;
                }
                break;
            case MotionEvent.ACTION_UP:// 手指接触屏幕重置路径
                resetPosition();
                break;
        }

        // 重绘视图
        invalidate();
        return true;
    }

    private void resetPosition() {
        bgLeft = screenW / 2 - fgBitmap.getWidth() / 2;
        bgTop = screenH / 2 - fgBitmap.getHeight() / 2;
        Log.d(TAG,"fgBitmap.getHeight() = " + fgBitmap.getHeight());
        left = bgLeft;
        top = bgTop;
    }

    private void getPosition(float x, float y) {
        float r = 15, r2 = r *2 / 3;

        double rr = Math.sqrt((Math.pow((x - screenW /2),2) + Math.pow((y - screenH /2),2)));
        left =(int)(((x -screenW /2) * r / rr) + screenW /2)- fgBitmap.getWidth() / 2;
        top = (int)(((y -screenH /2) * r / rr) + screenH /2)- fgBitmap.getHeight() / 2;

        bgLeft =(int)(((x -screenW /2) * r2 / rr) + screenW /2)- fgBitmap.getWidth() / 2;
        bgTop = (int)(((y -screenH /2) * r2 / rr) + screenH /2)- fgBitmap.getHeight() / 2;
    }

}
