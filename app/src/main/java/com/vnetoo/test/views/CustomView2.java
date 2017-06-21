package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vnetoo.test.MyApplication;
import com.vnetoo.test.R;

/**
 * Created by Administrator on 2017/5/18.
 */

public class CustomView2 extends View {

    Paint mPaint;
    Context mContext;

    public CustomView2(Context context) {
        super(context);
        initPaint();
        mContext = context;
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 初始化画笔
        initPaint();
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        // 初始化画笔
        initPaint();
    }

    // 初始化画笔
    private void initPaint() {
        // 实例化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.a);
        int x = MyApplication.sScreenWidth /2;
        int y = MyApplication.sScreenHeight /2;

        int left = x,top = 0;
        drawBitmap(canvas, bitmap,0,0,null);
        drawBitmap2(canvas, bitmap,left,top);

        top = bitmap.getHeight();
        drawBitmap3(canvas, bitmap,0,top);
        drawBitmap4(canvas, bitmap,left,top);

        top = 2 * bitmap.getHeight();
        drawBitmap5(canvas, bitmap,0,top);
        drawBitmap6(canvas, bitmap,left,top);

        top = 3 * bitmap.getHeight();
        drawBitmap7(canvas, bitmap,0,top);
        drawBitmap8(canvas, bitmap,left,top);

    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap,int left ,int top,ColorMatrix colorMatrix) {
        if (colorMatrix != null)
            mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap,left,top,mPaint);
    }

    private void drawBitmap2(Canvas canvas, Bitmap bitmap,int left ,int top) {
        // 生成色彩矩阵
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.5F, 0, 0, 0, 0,
                0, 0.5F, 0, 0, 0,
                0, 0, 0.5F, 0, 0,
                0, 0, 0, 1, 0,
        });
        drawBitmap(canvas,bitmap,left,top,colorMatrix);
    }

    private void drawBitmap3(Canvas canvas, Bitmap bitmap,int left ,int top) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.33F, 0.59F, 0.11F, 0, 0,
                0.33F, 0.59F, 0.11F, 0, 0,
                0.33F, 0.59F, 0.11F, 0, 0,
                0, 0, 0, 1, 0,
        });
        drawBitmap(canvas,bitmap,left,top,colorMatrix);
    }

    private void drawBitmap4(Canvas canvas, Bitmap bitmap,int left ,int top) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                -1, 0, 0, 1, 1,
                0, -1, 0, 1, 1,
                0, 0, -1, 1, 1,
                0, 0, 0, 1, 0,
        });
        drawBitmap(canvas,bitmap,left,top,colorMatrix);
    }

    private void drawBitmap5(Canvas canvas, Bitmap bitmap,int left ,int top) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0, 0, 1, 0, 0,
                0, 1, 0, 0, 0,
                1, 0, 0, 0, 0,
                0, 0, 0, 1, 0,
        });
        drawBitmap(canvas,bitmap,left,top,colorMatrix);
    }

    private void drawBitmap6(Canvas canvas, Bitmap bitmap,int left ,int top) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.393F, 0.769F, 0.189F, 0, 0,
                0.349F, 0.686F, 0.168F, 0, 0,
                0.272F, 0.534F, 0.131F, 0, 0,
                0, 0, 0, 1, 0,
        });
        drawBitmap(canvas,bitmap,left,top,colorMatrix);
    }

    private void drawBitmap7(Canvas canvas, Bitmap bitmap,int left ,int top) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1.5F, 1.5F, 1.5F, 0, -1,
                1.5F, 1.5F, 1.5F, 0, -1,
                1.5F, 1.5F, 1.5F, 0, -1,
                0, 0, 0, 1, 0,
        });
        drawBitmap(canvas,bitmap,left,top,colorMatrix);
    }

    private void drawBitmap8(Canvas canvas, Bitmap bitmap,int left ,int top) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1.438F, -0.122F, -0.016F, 0, -0.03F,
                -0.062F, 1.378F, -0.016F, 0, 0.05F,
                -0.062F, -0.122F, 1.483F, 0, -0.02F,
                0, 0, 0, 1, 0,
        });
        drawBitmap(canvas,bitmap,left,top,colorMatrix);
    }

}
