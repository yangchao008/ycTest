package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vnetoo.test.R;

/**
 * Created by Administrator on 2017/5/25.
 */

public class CustomView9 extends View {

    private final int WIDTH = 19,HEIGHT = 19;
    private final int COUNT = (WIDTH + 1 ) *(HEIGHT + 1);
    private Bitmap mBitmap;
    private float[] verts;

    public CustomView9(Context context) {
        super(context);
        initRes(context);
    }

    public CustomView9(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRes(context);
    }

    public CustomView9(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(context);
    }

    private void initRes(Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
        verts = new float[COUNT * 2];
        float fx,fy;
        float mBitmapWidth = mBitmap.getWidth();
        float mBitmapHeight = mBitmap.getHeight();
        int index = 0;
        for (int i = 0; i <= HEIGHT; i++) {
            fy = mBitmapHeight * i / HEIGHT;
            for (int j = 0; j <= WIDTH; j++) {
                fx = mBitmapWidth * j / WIDTH + i * mBitmapHeight / HEIGHT;
                verts[2 * index + 0] = fx;
                verts[2 * index + 1] = fy;
                index++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmapMesh(mBitmap,WIDTH,HEIGHT,verts,0,null,0,null);
    }

}
