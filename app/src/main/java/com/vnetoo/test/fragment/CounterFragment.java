package com.vnetoo.test.fragment;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vnetoo.test.R;
import com.vnetoo.test.activity.ContainerActivity;
import com.vnetoo.test.service.CounterService;
import com.vnetoo.test.utils.MyLog;

/**
 * 内容摘要：
 * 完成日期：2017/3/20
 * 编码作者：杨超 .
 */
public class CounterFragment extends Fragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private ContainerActivity mActivity;

    private View mContentView;
    private TextView mCountTextView;

    private CounterService mCounterService;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ContainerActivity)activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.l(TAG,"onCreate");
        setRetainInstance(true);
        mActivity.bindService(new Intent(mActivity,CounterService.class),mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mCounterService = ((CounterService.MyBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mCounterService = null;
        }
    };

    private BroadcastReceiver mCounterActionReceiver = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            if (CounterService.BROADCAST_COUNTER_ACTION.equals(intent.getAction())){
                long counter = intent.getLongExtra(CounterService.COUNTER_VALUE, 0);
                String text = String.valueOf(counter);
                mCountTextView.setText(text);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyLog.l(TAG,"onCreateView");
        mContentView = inflater.inflate(R.layout.fragment_counter, container,false);
        setupViews();
        return mContentView;
    }

    @Override
    public void onResume() {
        MyLog.l(TAG,"onResume");
        IntentFilter counterActionFilter = new IntentFilter(CounterService.BROADCAST_COUNTER_ACTION);
        mActivity.registerReceiver(mCounterActionReceiver,counterActionFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MyLog.l(TAG, "onPause");
        mActivity.unregisterReceiver(mCounterActionReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.l(TAG,"onDestroy");
        mActivity.unbindService(mConnection);
    }

    private void setupViews() {
        mCountTextView = (TextView) mContentView.findViewById(R.id.tv_count);
        mContentView.findViewById(R.id.btn_start).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                mCounterService.startCounter(0);
                break;
            case R.id.btn_stop:
                mCounterService.stopCounter();
                break;
            default:
                break;
        }
    }

    public interface ICounterService {
        public void startCounter(int initVal);
        public void stopCounter();
    }
}
