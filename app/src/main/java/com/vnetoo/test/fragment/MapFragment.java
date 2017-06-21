package com.vnetoo.test.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.mapapi.search.core.PoiInfo;
import com.vnetoo.test.R;

/**
 * 内容摘要：
 * 完成日期：2017/4/5
 * 编码作者：杨超 .
 */
public class MapFragment extends Fragment {
    View mContentView,mProgressView;
    MyMapFragment mMyMapFragment;
    BaiduMapFragment mBaiduMapFragment;
    String[] mTags = {"MyMapFragment","BaiduMapFragment"};
    int fragmentIndex = 0;

    public int getFragmentIndex() {
        return fragmentIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == getChildFragmentManager().findFragmentByTag(mTags[0]))
            mMyMapFragment = new MyMapFragment();
        else mMyMapFragment = (MyMapFragment) getChildFragmentManager().findFragmentByTag(mTags[0]);
        if (null == getChildFragmentManager().findFragmentByTag(mTags[1]))
            mBaiduMapFragment = new BaiduMapFragment();
        else mBaiduMapFragment = (BaiduMapFragment) getChildFragmentManager().findFragmentByTag(mTags[1]);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_map,container,false);
        if (null == savedInstanceState)
            getChildFragmentManager().beginTransaction().add(R.id.fl_container, mMyMapFragment, mTags[0])
                    .add(R.id.fl_container, mBaiduMapFragment, mTags[1]).hide(mBaiduMapFragment).show(mMyMapFragment)
                    .commit();
        mProgressView = mContentView.findViewById(R.id.llyt_progress);
        return mContentView;
    }

    public void dismissProgressBarView() {
        mProgressView.setVisibility(View.GONE);
    }

    public PoiInfo getCurrentPosition(){
        return mBaiduMapFragment.getCurrentPosition();
    }

    public PoiInfo getSelectPosition(){
        return mBaiduMapFragment.getSelectPoiInfo();
    }

    public void openMap(PoiInfo info) {
        fragmentIndex = 1;
        mBaiduMapFragment.initPoiInfo(info);
        getChildFragmentManager().beginTransaction().hide(mMyMapFragment).show(mBaiduMapFragment).commit();
    }

    public void closeMap(boolean isSetAddress) {
        fragmentIndex = 0;
        getChildFragmentManager().beginTransaction().hide(mBaiduMapFragment).show(mMyMapFragment).commit();
        mMyMapFragment.setAddress(isSetAddress,false);
    }
}
