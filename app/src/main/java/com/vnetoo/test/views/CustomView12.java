package com.vnetoo.test.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.vnetoo.test.R;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by Administrator on 2017/5/25.
 */

public class CustomView12 extends View {

    private final float BOUNDARY_RADIUS = 9F / 20F;//边界圆的半径
    private final float BACKGROUND_RADIUS = 1F / 3F;//进度背景圆的半径
    private final float ARC_DEVIATE = 1F / 6F;//进度圆的距离边界的距离
    private final float BOUNDARY_WIDTH = 1F / 20F;//边界圆的线条宽度
    private final int DEFAULT_WIDTH = 60;//默认宽度
    private int mWidth,mProgress;
    private float mBoundaryRadius,mBackgroundRadius;

    private int mBoundaryColor;//边界线颜色
    private int mBackgroundColor;//进度圆的背景色
    private int mProgressColor;//进度圆的进度色

    private Paint mBoundaryPaint,mBackgroundPaint,mProgressPaint;

    public CustomView12(Context context) {
        super(context);
        initRes(context);
    }

    public CustomView12(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        initRes(context);
    }

    public CustomView12(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        initRes(context);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android",
                "layout_width");
        if (height.equals(String.valueOf(LinearLayout.LayoutParams.MATCH_PARENT)) ||
                height.equals(String.valueOf(LinearLayout.LayoutParams.WRAP_CONTENT))){
            mWidth = DEFAULT_WIDTH;
        }else {
            int[] systemAttrs = {android.R.attr.layout_width};
            TypedArray typedArray = context.obtainStyledAttributes(attrs,systemAttrs);
            mWidth = typedArray.getDimensionPixelOffset(0,DEFAULT_WIDTH);
            typedArray.recycle();
        }
        obtainAttributes(context, attrs);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView12);

        mBoundaryColor = typedArray.getColor(R.styleable.CustomView12_yc_boundary_color,
                Color.parseColor("#aaaaaa"));
        mBackgroundColor = typedArray.getColor(R.styleable.CustomView12_yc_background_color,
                Color.parseColor("#d6d6d6"));
        mProgressColor = typedArray.getColor(R.styleable.CustomView12_yc_progress_color,
                Color.parseColor("#f88944"));
        mProgress = typedArray.getInteger(R.styleable.CustomView12_yc_progress,0);
        if (mProgress > 100){
            mProgress = 100;
        }else if (mProgress < 0){
            mProgress = 0;
        }
    }

    private void initRes(Context context) {

        mBoundaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mBoundaryPaint.setStyle(Paint.Style.STROKE);
        mBoundaryPaint.setColor(mBoundaryColor);
        mBoundaryPaint.setStrokeWidth(BOUNDARY_WIDTH * mWidth);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackgroundPaint.setColor(mBackgroundColor);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mProgressPaint.setColor(mProgressColor);

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(mWidth, mWidth);
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBoundaryRadius = mWidth * BOUNDARY_RADIUS;
        mBackgroundRadius = mWidth * BACKGROUND_RADIUS;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth /2, mWidth / 2,mBoundaryRadius,mBoundaryPaint);

        canvas.drawCircle(mWidth /2, mWidth / 2,mBackgroundRadius,mBackgroundPaint);

        if (mProgress > 0){
            canvas.save();
            float sweepAngle = mProgress <= 50 ? 180 : 360;
            float deviate = ARC_DEVIATE * mWidth;
            RectF rectF = new RectF(deviate,deviate,mWidth-deviate,mWidth-deviate);
            canvas.drawArc(rectF,0,sweepAngle,true,mProgressPaint);
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (mProgress < 100){
                    mProgress += 50;
                }else {
                    mProgress = 0;
                }
                invalidate();
                break;
        }
        return true;
    }
}
