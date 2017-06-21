package com.vnetoo.test.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.vnetoo.test.utils.MyLog;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MyViewGroup2 extends LinearLayout{

    private final String TAG = "MyViewGroup3";

    public MyViewGroup2(Context context) {
        super(context);
    }

    public MyViewGroup2(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public MyViewGroup2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
    }

    /***
     * dispatchTouchEvent()返回true，后续事件（ACTION_MOVE、ACTION_UP）会再传递，
     * 如果返回false，dispatchTouchEvent()就接收不到ACTION_UP、ACTION_MOVE。
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                MyLog.l(TAG,"dispatchTouchEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                MyLog.l(TAG,"dispatchTouchEvent--ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                MyLog.l(TAG,"dispatchTouchEvent--ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * ViewGroup提供的方法，默认返回false，返回true表示拦截
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                MyLog.l(TAG,"onInterceptTouchEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                MyLog.l(TAG,"onInterceptTouchEvent--ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                MyLog.l(TAG,"onInterceptTouchEvent--ACTION_UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 默认返回false，表示不消费这事件。
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                MyLog.l(TAG,"onTouchEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                MyLog.l(TAG,"onTouchEvent--ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                MyLog.l(TAG,"onTouchEvent--ACTION_UP");
                break;
        }
        return true;
    }
}
