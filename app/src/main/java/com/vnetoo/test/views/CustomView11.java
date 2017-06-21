package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2017/5/25.
 */

public class CustomView11 extends View {

    private Paint mPaint;
    private Path mPath;
    private int mScreenWidth,mScreenHeight;
    private int pageIndex = 0,mBitmapSize;
    private List<Bitmap> mBitmaps;
    float mClipX, fx;
    boolean isLastPage,isNextPage,isMoveToRight;

    public synchronized void setBitmaps(List<Bitmap> bitmaps) {
        if (bitmaps == null || 0 == bitmaps.size())
            throw new IllegalArgumentException("no bitmap to display");
        if (bitmaps.size() < 2)
            throw new IllegalArgumentException("bitmap size too low");

        this.mBitmaps = bitmaps;
        mBitmapSize = mBitmaps.size();
        invalidate();
    }

    public CustomView11(Context context) {
        super(context);
        initRes(context);
    }

    public CustomView11(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRes(context);
    }

    public CustomView11(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mScreenWidth = w;
        mScreenHeight = h;
        initBitmap();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void initBitmap() {
        List<Bitmap> temp = new ArrayList<Bitmap>();
        if (mBitmaps != null){
            for (int i = 0; i < mBitmaps.size(); i++) {
                Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i),mScreenWidth,mScreenHeight,
                        true);
                temp.add(bitmap);
            }
        }
        mBitmaps = temp;
    }

    private void initRes(Context context) {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        mPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBitmaps2(canvas);
    }

    //（从mBitmaps的第一张开始）
    private void drawBitmaps2(Canvas canvas) {
        isLastPage = false;

        if (pageIndex < 0){
            Toast.makeText(getContext(),"第一页",Toast.LENGTH_SHORT).show();
            canvas.drawBitmap(mBitmaps.get(0),0,0,null);
            return;
        }
        pageIndex = pageIndex < 0 ? 0 : pageIndex;
        pageIndex = pageIndex > mBitmapSize ? mBitmapSize : pageIndex;
        int start = pageIndex;
        int end = pageIndex + 2;

        if (end > mBitmapSize){
            isLastPage = true;

            start = mBitmapSize -1;
            end = start + 1;
            Toast.makeText(getContext(),"最后一页",Toast.LENGTH_SHORT).show();
        }

        for (int i = end - 1; i >= start ; i--) {
            canvas.save();

            if (!isLastPage && i == start){
                canvas.clipRect(0,0,mClipX,mScreenHeight);
            }
            canvas.drawBitmap(mBitmaps.get(i),0,0,null);

            canvas.restore();
        }
    }

    /**
     * 绘制位图（从mBitmaps的最后一张开始）
     *
     * @param canvas
     *            Canvas对象
     */
    private void drawBitmaps(Canvas canvas) {
        // 绘制位图前重置isLastPage为false
        isLastPage = false;

        // 限制pageIndex的值范围
        pageIndex = pageIndex < 0 ? 0 : pageIndex;
        pageIndex = pageIndex > mBitmaps.size() ? mBitmaps.size() : pageIndex;

        // 计算数据起始位置
        int start = mBitmaps.size() - 2 - pageIndex;
        int end = mBitmaps.size() - pageIndex;

        /*
         * 如果数据起点位置小于0则表示当前已经到了最后一张图片
         */
        if (start < 0) {
            // 此时设置isLastPage为true
            isLastPage = true;

            // 强制重置起始位置
            start = 0;
            end = 1;
        }

        for (int i = start; i < end; i++) {
            canvas.save();

            /*
             * 仅裁剪位于最顶层的画布区域
             * 如果到了末页则不在执行裁剪
             */
            if (!isLastPage && i == end - 1) {
                canvas.clipRect(0, 0, mClipX, mScreenHeight);
            }
            canvas.drawBitmap(mBitmaps.get(i), 0, 0, null);

            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isNextPage = true;
        switch (event.getAction()& MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                fx = event.getX();

                if (fx < mScreenWidth /2){
                    pageIndex --;
                    isNextPage = false;
                    mClipX = fx;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float xx = event.getX() - fx;
                if (Math.abs(xx) > mScreenWidth / 100){
                    if (xx >0)  isMoveToRight = true;
                    else    isMoveToRight = false;
                    mClipX = event.getX();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 判断是否需要自动滑动
                judgeSlideAuto();
                /*
             * 如果当前页不是最后一页
             * 如果是需要翻下一页
             * 并且上一页已被clip掉
             */
                if (!isLastPage && isNextPage && mClipX <= 0) {
                    pageIndex++;
                    mClipX = mScreenWidth;
                    invalidate();
                }
                break;
        }
        return true;
    }

    private void judgeSlideAuto() {
        if (isMoveToRight){
            if (mClipX > mScreenWidth / 2){
                add();
            }else{
                minus();
            }
        }else{
            if (mClipX < mScreenWidth / 2){
                minus();
            }else {
                add();
            }
        }
    }

    private void minus() {
        while (mClipX > 0){
            mClipX --;
            invalidate();
        }
    }

    private void add() {
        while (mClipX < mScreenWidth){
            mClipX ++;
            invalidate();
        }
    }
}
