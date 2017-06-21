package com.vnetoo.test.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.vnetoo.test.utils.MyLog;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MyView2 extends Button{

    private final String TAG = "MyView2";

    public MyView2(Context context) {
        super(context);
    }

    public MyView2(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public MyView2(Context context, AttributeSet attrs, int defStyleAttr) {
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
     * 默认返回true，表示消费了这个事件。
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
        return false;
    }
}
