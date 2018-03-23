package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vnetoo.test.MyApplication;
import com.vnetoo.test.R;

/**
 * Created by Administrator on 2017/5/25.
 */

public class CustomView13 extends View {
    private Paint mPaint;
    private int mScreenWidth, mScreenHeight;
    private Bitmap mBitmap;

    boolean isRotateEnable;

    float mPreRotate;
    private float rotateValue = 0.0f;
    float mCurTotalRotation = 0;
    final int MIN_ROTATION_DEGREE = 25;

    Point mCenterPoint;

    Matrix mMatrix;

    public CustomView13(Context context) {
        super(context);
        initRes(context);
    }

    public CustomView13(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRes(context);
    }

    public CustomView13(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void initRes(Context context) {
        mScreenWidth = MyApplication.sScreenWidth;
        mScreenHeight = MyApplication.sScreenHeight;

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fy3);

        mCenterPoint = new Point();
        mCenterPoint.set(mScreenWidth / 2 ,mScreenHeight /2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMatrix == null)    mMatrix = new Matrix();
        else    mMatrix.reset();

        mMatrix.postRotate(mCurTotalRotation % 360, mCenterPoint.x,mCenterPoint.y);
        canvas.setMatrix(mMatrix);

        int left = (mScreenWidth - mBitmap.getWidth())/ 2;
        int top = (mScreenHeight - mBitmap.getHeight())/ 2;

        canvas.drawBitmap(mBitmap,left,top,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2){
                    mPreRotate = rotation(event);
                    isRotateEnable = true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1){
                    float rotate = rotation(event);
                    rotateValue = rotate - mPreRotate;

                    mCurTotalRotation += rotateValue;
                    mPreRotate = rotate;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                doUp();
                break;
        }

        return true;
    }

    private void doUp() {

        if (isRotateEnable || mCurTotalRotation % 90 != 0) {
            isRotateEnable = false;

            float toDegrees = (int) (mCurTotalRotation / 90) * 90;
            float remainder = mCurTotalRotation % 90;

            if (remainder > 45)
                toDegrees += 90;
            else if (remainder < -45)
                toDegrees -= 90;

            mCurTotalRotation = toDegrees;
            invalidate();
        }
    }

    private float rotation(MotionEvent event) {
        //根据前后两点的x,y坐标，计算正切函数值，再通过正切函数值反查前后两点的旋转角度
        double delta_x = event.getX(1) - event.getX(0);
        double delta_y = event.getY(1) - event.getY(0);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}
