package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vnetoo.test.MyApplication;
import com.vnetoo.test.R;

/**
 * Created by Administrator on 2017/5/25.
 */

public class CustomView7 extends View {

    private Paint mPaint,mShaderPaint;
    private Context mContext;
    private Bitmap mSrcBitmap;
    private int mScreenWith,mScreenHeight,mLeft,mTop;

    private PorterDuffXfermode mXfermode;// 图形混合模式


    public CustomView7(Context context) {
        super(context);
        initRes(context);
    }

    public CustomView7(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRes(context);
    }

    public CustomView7(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(context);
    }

    private void initRes(Context context) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 实例化混合模式
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SCREEN);

        mSrcBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.girl);
        mScreenWith = MyApplication.sScreenWidth;
        mScreenHeight = MyApplication.sScreenHeight;

        mLeft = mScreenWith / 2 - mSrcBitmap.getWidth() / 2 ;
        mTop = mScreenHeight / 2 - mSrcBitmap.getHeight() / 2;

        mShaderPaint.setShader(new RadialGradient(mScreenWith / 2,mSrcBitmap.getHeight()*3 / 2
                ,mSrcBitmap.getHeight()*3 / 4, Color.TRANSPARENT,Color.BLACK, Shader.TileMode.CLAMP));
//        new LinearGradient()
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mSrcBitmap,mLeft,0,mPaint);
        mPaint.setColorFilter(new ColorMatrixColorFilter(new float[] { 0.8587F, 0.2940F, -0.0927F, 0,
                6.79F, 0.0821F, 0.9145F, 0.0634F, 0, 6.79F, 0.2019F, 0.1097F, 0.7483F, 0, 6.79F, 0,
                0, 0, 1, 0 }));

        mTop = mSrcBitmap.getHeight();
        // 新建图层
        int sc = canvas.saveLayer(mLeft, mTop, mLeft + mSrcBitmap.getWidth(), mTop + mSrcBitmap.getHeight(), null, Canvas.ALL_SAVE_FLAG);

        // 绘制混合颜色
        canvas.drawColor(0xcc1c093e);

        // 设置混合模式
        mPaint.setXfermode(mXfermode);

        // 绘制位图
        canvas.drawBitmap(mSrcBitmap, mLeft, mTop, mPaint);

        // 还原混合模式
        mPaint.setXfermode(null);

        // 还原画布
        canvas.restoreToCount(sc);

        canvas.drawRect(mLeft, mTop, mLeft + mSrcBitmap.getWidth(), mTop + mSrcBitmap.getHeight(),mShaderPaint);
    }
}
