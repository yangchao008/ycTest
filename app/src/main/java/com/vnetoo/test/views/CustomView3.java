package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vnetoo.test.MyApplication;
import com.vnetoo.test.R;
import com.vnetoo.test.utils.MyLog;

/**
 * Created by Administrator on 2017/5/18.
 */

public class CustomView3 extends View {

    private final String TAG = "CustomView3";
    Paint mPaint,mPaint2,mPaint3;
    Context mContext;
    boolean isClick;

    public CustomView3(Context context) {
        super(context);
        initPaint();
        mContext = context;
    }

    public CustomView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 初始化画笔
        initPaint();
    }

    public CustomView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        // 初始化画笔
        initPaint();
    }

    // 初始化画笔
    private void initPaint() {
        // 实例化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 设置颜色过滤
        mPaint3.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN));

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                 /*
                 * 判断控件是否被点击过
                 */
                if (isClick) {
                    // 如果已经被点击了则点击时设置颜色过滤为空还原本色
                    mPaint.setColorFilter(null);
                    isClick = false;
                } else {
                    // 如果未被点击则点击时设置颜色过滤后为黄色
                    mPaint.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0X00FFFF00));
                    isClick = true;
                }
                // 记得重绘
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.b);
        Bitmap bitmap2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.a);
        int x = MyApplication.sScreenWidth /2;
        int y = MyApplication.sScreenHeight /2;

        int left = x / 2 - bitmap.getWidth() / 2,top = (y - bitmap.getHeight()) / 2;
        drawBitmap(canvas, bitmap,left,top,mPaint);

        left = x * 3 / 2 - bitmap.getWidth() / 2;
        top = (y - bitmap.getHeight())/ 2;
        drawBitmap(canvas, bitmap,left,top,mPaint2);

        left = x  - bitmap2.getWidth() / 2;
        top = y;
        canvas.drawBitmap(bitmap2,left,top,mPaint3);

    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap,int left ,int top,Paint paint) {
        canvas.drawBitmap(bitmap,left,top,paint);
    }

    /**
     * 默认返回true，表示消费了这个事件。
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                MyLog.l(TAG,"onTouchEvent--ACTION_DOWN");
                mPaint2.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0X00FFFF00));
                break;
            case MotionEvent.ACTION_MOVE:
                MyLog.l(TAG,"onTouchEvent--ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                MyLog.l(TAG,"onTouchEvent--ACTION_UP");
                mPaint2.setColorFilter(null);
                break;
        }
        // 记得重绘
        invalidate();
        return super.onTouchEvent(event);
    }

}
