package com.vnetoo.test.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.vnetoo.test.R;

/**
 * 内容摘要：
 * 完成日期：2017/4/1
 * 编码作者：杨超 .
 */
public class MyMapFragment extends Fragment implements View.OnClickListener {

    View mContentView;
    MapFragment mParentFragment;
    PoiInfo mPoiInfo;
//    MapView mMapView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_mymap,container,false);

        mContentView.findViewById(R.id.btn_open).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_location).setOnClickListener(this);
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_location:
                setAddress(true,true);
                break;
            case R.id.btn_open:
                if (null == mParentFragment)
                    mParentFragment = (MapFragment)getParentFragment();
                mParentFragment.openMap(mPoiInfo);
                break;
            default:
                break;
        }
    }

    public void setAddress(boolean isSetAddress,boolean isCurrentPosition) {
        if (!isSetAddress)  return;
        if (null == mParentFragment)
            mParentFragment = (MapFragment)getParentFragment();
        mPoiInfo = isCurrentPosition ? mParentFragment.getCurrentPosition() : mParentFragment.getSelectPosition();
        if (null != mPoiInfo && !TextUtils.isEmpty(mPoiInfo.address)){
            ((TextView)mContentView.findViewById(R.id.tv_location)).setText(mPoiInfo.address + mPoiInfo.name);
        }
    }
}
