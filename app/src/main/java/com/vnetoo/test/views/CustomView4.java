package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.vnetoo.test.MyApplication;
import com.vnetoo.test.R;

/**
 * Created by Administrator on 2017/5/18.
 */
public class CustomView4 extends View {

    private final String TAG = "CustomView4";
    Paint mPaint;
    Context mContext;

    public CustomView4(Context context) {
        super(context);
        initPaint();
        mContext = context;
    }

    public CustomView4(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 初始化画笔
        initPaint();
    }

    public CustomView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        Bitmap srcBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.c402);
        Bitmap disBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.c401);
        int x = MyApplication.sScreenWidth /2;
        int y = MyApplication.sScreenHeight /2;

        int left = x - srcBitmap.getWidth() / 2;
        int top = y- srcBitmap.getHeight() / 2;

        canvas.drawColor(Color.WHITE);

        /*
         * 将绘制操作保存到新的图层（更官方的说法应该是离屏缓存）我们将在1/3中学习到Canvas的全部用法这里就先follow me
         */
        int sc = canvas.saveLayer(0, 0, MyApplication.sScreenWidth, MyApplication.sScreenHeight, null, Canvas.ALL_SAVE_FLAG);

        // 先绘制dis目标图
        canvas.drawBitmap(disBitmap, left, top, mPaint);

        // 设置混合模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        // 再绘制src源图
        canvas.drawBitmap(srcBitmap, left, top, mPaint);

        // 还原混合模式
        mPaint.setXfermode(null);

        // 还原画布
        canvas.restoreToCount(sc);

    }

}
