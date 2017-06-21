package com.vnetoo.test.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Administrator on 2017/5/25.
 */

public class CustomView8 extends View {

    private final float STROKE_WIDTH = 1F / 256F, // 描边宽度占比
            SPACE = 1F / 64F,// 大圆小圆线段两端间隔占比
            LINE_LENGTH = 3F / 32F, // 线段长度占比
            CIRCLE_LARGER_RADIUS = 3F / 32F,// 大圆半径
            CIRCLE_SMALL_RADIUS = 5F / 64F,// 小圆半径
            ARC_RADIU = 1F / 8F,// 弧半径
            ARC_TEXT_RADIU = 5F / 32F;// 弧围绕文字半径

    private int size;// 控件边长
    private float strokeWidth;// 描边宽度
    private float ccX, ccY;// 中心圆圆心坐标
    private float largeCircleRadius, smallCircleRadius,lineLength,space,textOffY;// 大圆半径
    private Context mContext;

    private Paint strokePaint,textPaint;// 描边画笔

    public CustomView8(Context context) {
        super(context);
        initRes(context);
    }

    public CustomView8(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRes(context);
    }

    public CustomView8(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(context);
    }

    private void initRes(Context context) {
        mContext = context;

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);

        /*
         * 初始化文字画笔
         */
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(18);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    private void calculation() {
        largeCircleRadius = CIRCLE_LARGER_RADIUS * size;
        smallCircleRadius = CIRCLE_SMALL_RADIUS * size;
        lineLength = LINE_LENGTH * size;
        strokeWidth = STROKE_WIDTH * size;
        // 计算大圆小圆线段两端间隔
        space = size * SPACE;
        textOffY = (textPaint.ascent() + textPaint.descent()) / 2;

        ccX = size / 2;
        ccY = size / 2 + largeCircleRadius;

        strokePaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        size = w;
        calculation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 强制长宽一致
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        canvas.drawColor(0xFFF29B76);

        drawCircleAndText(canvas, ccX, ccY, largeCircleRadius,"yangchao",0);

        drawLeftTop(canvas);
        drawRightTop(canvas);
        drawLeftBottom(canvas);
        drawBottom(canvas);
        drawRightBottom(canvas);

    }

    private void drawCircleAndText(Canvas canvas, float cx, float cy, float radius,String text,float degrees) {
        canvas.drawCircle(cx, cy, radius, strokePaint);

        canvas.save();
        canvas.translate(cx,cy);
        canvas.rotate(-degrees);
        canvas.drawText(text,0, - textOffY,textPaint);

        canvas.restore();
    }

    private void drawBottom(Canvas canvas) {
        canvas.save();
        canvas.translate(ccX,ccY);

        canvas.drawLine(0, largeCircleRadius + space,0 , 2 * largeCircleRadius + space
                ,strokePaint);
        drawCircleAndText(canvas, 0, 2 * largeCircleRadius + 2 * space + smallCircleRadius, smallCircleRadius,"yangchao",0);

        canvas.restore();
    }

    private void drawRightBottom(Canvas canvas) {
        canvas.save();
        canvas.translate(ccX,ccY);
        int degrees = 100;
        canvas.rotate(degrees);

        canvas.drawLine(0,-largeCircleRadius - space,0 , -2 * largeCircleRadius - space
                ,strokePaint);
        drawCircleAndText(canvas, 0, - 2 * largeCircleRadius - 2 * space - smallCircleRadius, smallCircleRadius,"yangchao",degrees);

        canvas.restore();
    }

    private void drawLeftBottom(Canvas canvas) {
        canvas.save();
        canvas.translate(ccX,ccY);
        int degrees = -100;
        canvas.rotate(degrees);

        canvas.drawLine(0,-largeCircleRadius - space,0 , -2 * largeCircleRadius - space
                ,strokePaint);
        drawCircleAndText(canvas, 0, - 2 * largeCircleRadius - 2 * space - smallCircleRadius, smallCircleRadius,"yangchao",degrees);

        canvas.restore();
    }

    private void drawRightTop(Canvas canvas) {
        canvas.save();
        canvas.translate(ccX,ccY);
        int degrees = 30;
        canvas.rotate(degrees);

        canvas.drawLine(0,-largeCircleRadius, 0, - 2 * largeCircleRadius,strokePaint);
        float cy = - 3 * lineLength;
        drawCircleAndText(canvas, 0, cy, largeCircleRadius,"yangchao",degrees);
        drawArc(canvas,cy);

        canvas.restore();
    }

    private void drawArc(Canvas canvas, float cy) {
        canvas.save();
        canvas.translate(0,cy);
        float arcRadius = ARC_RADIU * size;
        RectF rectF = new RectF();
        rectF.set(- arcRadius, - arcRadius,arcRadius,arcRadius);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x55EC6941);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF,-45,-135,true,paint);

        canvas.drawArc(rectF,-45,-135,false,strokePaint);

        float arcTextRadius = ARC_TEXT_RADIU * size;
        float angle = 135 / 4;
        String text = "chao";
        for (int i = 0; i < 5; i++) {
            canvas.save();
            canvas.rotate(45 -  angle * i);
            canvas.drawText(text,0,-arcTextRadius ,textPaint);
            canvas.restore();
        }

        canvas.restore();
    }

    private void drawLeftTop(Canvas canvas) {
        canvas.save();
        canvas.translate(ccX,ccY);
        int degrees = -30;
        canvas.rotate(degrees);

        canvas.drawLine(0, -largeCircleRadius,0, -2 * lineLength,strokePaint);
        drawCircleAndText(canvas, 0, - 3* lineLength, largeCircleRadius,"yangchao",degrees);
        canvas.drawLine(0,- 4* lineLength,0,- 5* lineLength,strokePaint);
        drawCircleAndText(canvas, 0, -6 * lineLength, largeCircleRadius,"yangchao",degrees);

        canvas.restore();
    }
}
