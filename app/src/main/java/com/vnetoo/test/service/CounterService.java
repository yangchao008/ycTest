package com.vnetoo.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import com.vnetoo.test.fragment.CounterFragment;
import com.vnetoo.test.utils.MyLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 内容摘要：
 * 完成日期：2017/3/20
 * 编码作者：杨超 .
 */
public class CounterService extends Service implements CounterFragment.ICounterService{
    private final String TAG = getClass().getSimpleName();

    public final static String BROADCAST_COUNTER_ACTION = "shy.luo.broadcast.COUNTER_ACTION";
    public final static String COUNTER_VALUE = "shy.luo.broadcast.counter.value";

    private Timer mTimer;

    private long mSecond = 0;

    private boolean isStop;

    Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == 1 && !isStop) {
                mSecond ++;
                Intent intent = new Intent(BROADCAST_COUNTER_ACTION);
                intent.putExtra(COUNTER_VALUE, mSecond);
                sendBroadcast(intent);
            }
        }
    };

    @Override
    public void startCounter(int initVal) {
        isStop = false;
        mSecond = initVal;
        if (mTimer == null) {
            mTimer = new Timer();
//            timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    // 需要做的事:发送消息
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                    if (mSecond < 0) {
                        System.gc();
                    }
                }
            }, 1000, 1000);
        }
    }

    public void stopCounter() {
        isStop = true;
    }

    public class MyBinder extends Binder {
        public CounterService getService(){
            return CounterService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.l(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.l(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        MyLog.l(TAG,"onStart");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        MyLog.l(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        MyLog.l(TAG,"onRebind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.l(TAG, "onDestroy");
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
            mSecond = -1;
        }
    }
}
