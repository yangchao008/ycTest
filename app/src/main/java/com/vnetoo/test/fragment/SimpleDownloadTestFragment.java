package com.vnetoo.test.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vnetoo.test.R;
import com.vnetoo.test.activity.ContainerActivity;
import com.vnetoo.test.utils.MyLog;
import com.vnetoo.test.utils.SpUtils;
import android.app.DownloadManager.Request;


/**
 * 内容摘要：
 * 完成日期：2017/3/24
 * 编码作者：杨超 .
 */
public class SimpleDownloadTestFragment extends Fragment implements View.OnClickListener {

    private ImageLoader mImageLoader;

    private View mContentView;

    private DownloadManager mDownloadManager;

    private ContainerActivity mActivity;

    private long referenceId = -1L;

    final String REFERENCE_ID = "reference_id";

    IntentFilter mFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
            if (referenceId == reference) {
                setProgress();
            }
        }
    };

    private void setProgress(){
        ImageView imageView = (ImageView) mContentView.findViewById(R.id.iv_icon);
        if (referenceId > 0){
            String url = "http://img1.imgtn.bdimg.com/it/u=67109071,1046027022&fm=11&gp=0.jpg";
            mImageLoader.displayImage(url,imageView);
        }else{
            imageView.setImageResource(R.drawable.ic_launcher);
        }
        ((ProgressBar) mContentView.findViewById(R.id.pb)).setProgress(referenceId > 0 ? 100 : 0);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ContainerActivity)activity;
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDownloadManager = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        referenceId = (Long)SpUtils.get(mActivity,REFERENCE_ID,-1L);
        MyLog.l(SimpleDownloadTestFragment.class.getSimpleName(),"referenceId = " + referenceId);
        mActivity.registerReceiver(mReceiver, mFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_downloadmanager,container,false);
        mContentView.findViewById(R.id.btn_download).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_delete).setOnClickListener(this);

        setProgress();
        return mContentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_download:
                download();
                break;
            case R.id.btn_delete:
                if (referenceId < 0){
                    return;
                }
                Uri uri = mDownloadManager.getUriForDownloadedFile(referenceId);
                MyLog.l(SimpleDownloadTestFragment.class.getSimpleName(),"uri = " + uri);
                mDownloadManager.remove(referenceId);
                uri = mDownloadManager.getUriForDownloadedFile(referenceId);
                MyLog.l(SimpleDownloadTestFragment.class.getSimpleName(), "uri2 = " + uri);
                referenceId = -1;
                SpUtils.put(REFERENCE_ID,referenceId < 1 ? -1 : referenceId);
                setProgress();
                break;
        }

    }

    private void download() {
        if (referenceId < 0){
            String url = "http://img1.imgtn.bdimg.com/it/u=67109071,1046027022&fm=11&gp=0.jpg";
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new Request(uri);
            referenceId = mDownloadManager.enqueue(request);
            SpUtils.put(REFERENCE_ID, referenceId < 1 ? -1 : referenceId);
        }
    }
}
