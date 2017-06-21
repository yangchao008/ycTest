package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.vnetoo.test.MyApplication;

/**
 * Created by Administrator on 2017/5/25.
 */

public class CustomView10 extends View {

    private int mScreenWidth,mScreenHeight, mTopControlX,mTopControlY,
            mBottomControlX, mBottomControlY, fy,fy2,widthSpace = 20;
    private Paint mPaint;
    private Path mPath;
    private final int space = 50;
    private boolean topIsAdd,bottomIsAdd;


    public CustomView10(Context context) {
        super(context);
        initRes(context);
    }

    public CustomView10(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRes(context);
    }

    public CustomView10(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize();
    }

    private void initSize() {
        mBottomControlX = 0;
        mBottomControlY = space;

        mTopControlX = mScreenWidth / 2;
        mTopControlY = 0;

        fy = 0;
        fy2 = space;
    }

    private void initRes(Context context) {
        mScreenWidth = MyApplication.sScreenWidth;
        mScreenHeight = MyApplication.sScreenHeight;


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0xFFA2D6AE);

        mPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.moveTo(0,fy);

        mPath.cubicTo(mBottomControlX, mBottomControlY, mTopControlX,mTopControlY,mScreenWidth,fy2);
        mPath.lineTo(mScreenWidth,mScreenHeight);
        mPath.lineTo(0,mScreenHeight);
        mPath.close();

        canvas.drawPath(mPath,mPaint);
        if (mBottomControlY + 1 > mScreenHeight){
            initSize();
        }else{

            widthSpace = 5 + (int)Math.random() * 5;
            mTopControlY = mBottomControlY - space;
            fy = mTopControlY + widthSpace / 2;
            mBottomControlY++;
            fy2 = mBottomControlY;

            if (mBottomControlX + widthSpace > mScreenWidth / 2 && bottomIsAdd){
                bottomIsAdd = false;
            }

            if (mBottomControlX - widthSpace < 0 &&!bottomIsAdd){
                bottomIsAdd = true;
            }
            mBottomControlX = bottomIsAdd ? mBottomControlX + widthSpace : mBottomControlX - widthSpace;

            if (mTopControlX + widthSpace > mScreenWidth  && topIsAdd){
                topIsAdd = false;
            }

            if (mTopControlX - widthSpace < mScreenWidth / 2 &&!topIsAdd){
                topIsAdd = true;
            }
            mTopControlX = topIsAdd ? mTopControlX + widthSpace : mTopControlX - widthSpace;
        }
        mPath.reset();
        invalidate();
    }

}
