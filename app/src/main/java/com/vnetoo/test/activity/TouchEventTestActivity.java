package com.vnetoo.test.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.vnetoo.test.utils.MyLog;
import com.vnetoo.test.R;

/**
 * Created by Administrator on 2017/5/17.
 */

public class TouchEventTestActivity extends FragmentActivity implements View.OnClickListener {
    private final String TAG = "TouchEventTestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_touchevent);
        findViewById(R.id.btn_click).setOnClickListener(this);
        findViewById(R.id.btn_click2).setOnClickListener(this);
        findViewById(R.id.btn_click3).setOnClickListener(this);
        findViewById(R.id.btn_click4).setOnClickListener(this);
        findViewById(R.id.btn_click5).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String text ;
        switch (view.getId()){
            case R.id.btn_click:
                text = "点了按钮1";
                break;
            case R.id.btn_click2:
                text = "点了按钮2";
                break;
            case R.id.btn_click3:
                text = "点了按钮3";
                break;
            case R.id.btn_click4:
                text = "点了按钮4";
                break;
            case R.id.btn_click5:
                text = "点了按钮5";
                break;
            default:
                text = "未知";
                break;
        }
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
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
        return super.onTouchEvent(event);
    }
}
