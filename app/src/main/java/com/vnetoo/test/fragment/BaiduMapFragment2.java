package com.vnetoo.test.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.vnetoo.test.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 内容摘要：
 * 完成日期：2017/4/1
 * 编码作者：杨超 .
 */
public class BaiduMapFragment2 extends Fragment implements View.OnClickListener {

    View mContentView;
    BaiduMap mBaiduMap;
    MapView mMapView;
    TextView tv_count;
    ImageView mSelectImg;
    ProgressBar mLoadBar;

    InputMethodManager mInputMethodManager;

    // 定位相关声明
    public LocationClient mLocationClient;
    //自定义图标
    BitmapDescriptor mCurrentMarker;
    boolean isFirstLoc = true;// 是否首次定位

    // 当前经纬度
    double mLantitude;
    double mLongtitude;
    LatLng mLoactionLatLng;

    // MapView中央对于的屏幕坐标
    Point mCenterPoint = null;

    // 地理编码
    GeoCoder mGeoCoder = null;

    PoiSearch mPoiSearch;

    // 位置列表
    List<PoiInfo> mInfoList,mSearchInfoList;
    PoiInfo mCurrentInfo,mCurrentPosition;

    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData data = new MyLocationData.Builder()//
                    // .direction(mCurrentX)//
                    .accuracy(location.getRadius())//
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            mBaiduMap.setMyLocationData(data);
            // 设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, false, null);
            mBaiduMap.setMyLocationConfigeration(config);

            mLantitude = location.getLatitude();
            mLongtitude = location.getLongitude();

            LatLng currentLatLng = new LatLng(mLantitude, mLongtitude);
            mLoactionLatLng = new LatLng(mLantitude, mLongtitude);

            // 是否第一次定位
            if (isFirstLoc) {
                isFirstLoc = false;

                // 实现动画跳转
                MapStatusUpdate u = MapStatusUpdateFactory
                        .newLatLng(currentLatLng);
                mBaiduMap.animateMapStatus(u);

                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                        .location(currentLatLng));
                return;
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_baidumap2,container,false);

        initView();

        tv_count = (TextView) mContentView.findViewById(R.id.tv_count);
        mContentView.findViewById(R.id.btn_start).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_back).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_add).setOnClickListener(this);

        return mContentView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mMapView.onPause();
    }

    // 三个状态实现地图生命周期管理
    @Override
    public void onDestroy() {
        //退出时销毁定位
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        // TODO Auto-generated method stub
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
        mGeoCoder.destroy();
        mPoiSearch.destroy();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        // TODO Auto-generated method stub
        // 初始化地图
        mMapView = (MapView) mContentView.findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMapTouchListener(touchListener);

        // 初始化POI信息列表
        mInfoList = new ArrayList();
        mSearchInfoList = new ArrayList();
        mPoiSearch = PoiSearch.newInstance();

        // 初始化当前MapView中心屏幕坐标，初始化当前地理坐标
        mCenterPoint = mBaiduMap.getMapStatus().targetScreen;

        mLoactionLatLng = mBaiduMap.getMapStatus().target;

        // 定位
        getCurrentLocation();

        // 地理编码
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);

        // 周边位置列表
        mLoadBar = (ProgressBar)mContentView.findViewById(R.id.place_progressBar);

        mSelectImg = new ImageView(getActivity());
    }

    /**
     * 开始定位
     */
    private void getCurrentLocation() {
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    // 地理编码监听器
    OnGetGeoCoderResultListener GeoListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            // 没有检索到结果
            }
            // 获取地理编码结果
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            // 没有找到检索结果
            }
            // 获取反向地理编码结果
            else {
            // 当前位置信息
                mCurrentInfo = new PoiInfo();
                mCurrentInfo.address = result.getAddress();
                mCurrentInfo.location = result.getLocation();
                mCurrentInfo.name = "[位置]";

                if (null == mCurrentPosition)
                    mCurrentPosition = mCurrentInfo;
                mInfoList.clear();
                mInfoList.add(mCurrentInfo);

                // 将周边信息加入表
                if (result.getPoiList() != null) {
                    mInfoList.addAll(result.getPoiList());
                }
                // 通知适配数据已改变
                mLoadBar.setVisibility(View.GONE);
            }
        }
    };

    // 地图触摸事件监听器
    BaiduMap.OnMapTouchListener touchListener = new BaiduMap.OnMapTouchListener() {
        @Override
        public void onTouch(MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_UP) {

                // 初始化当前MapView中心屏幕坐标，初始化当前地理坐标
                mCenterPoint = mBaiduMap.getMapStatus().targetScreen;

                if (mCenterPoint == null) {
                    return;
                }
                // 获取当前MapView中心屏幕坐标对应的地理坐标
                LatLng currentLatLng;
                currentLatLng = mBaiduMap.getProjection().fromScreenLocation(
                        mCenterPoint);
                System.out.println("----" + mCenterPoint.x);
                System.out.println("----" + currentLatLng.latitude);
                // 发起反地理编码检索
                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                        .location(currentLatLng));
                mLoadBar.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                moveNumber = 2;
                tv_count.setText(String.valueOf(moveNumber));

                Button button = (Button)view;
                if (button.getText().equals("停止")){
                    if (mSubscription != null)
                        mSubscription.unsubscribe();

                    isStopMove = true;
                    button.setText("重新开始");
                }else {
                    change = CHANGE;
                    isStopMove = false;
                    startMove();
                    button.setText("停止");
                }
                break;
            case R.id.btn_back:
                turnBack();
                break;
            case R.id.btn_add:
                moveNumber = getMoveNumber();
                tv_count.setText(String.valueOf(moveNumber));
                break;
            default:
                break;
        }
    }

    private int getMoveNumber() {

        return moveNumber += 2;
    }


    boolean isStopMove;
    double change;
    final double CHANGE = 0.00001;
    int moveNumber = 2;
    Subscription mSubscription;
    private void startMove2() {
         mSubscription = rx.Observable.interval(0, 50, TimeUnit.MILLISECONDS)//每1秒执行1次，第一次立即执行
                .map(new Func1<Long, List<LatLng>>() {
                    @Override
                    public List<LatLng> call(Long latLng) {
                        List<LatLng> latLngs = new ArrayList<LatLng>();
                        latLngs.clear();
                        for (int i = 0; i < moveNumber; i++) {
                            LatLng latLng1= new LatLng(mLantitude + change, mLongtitude+ 0.001 * (i + 1));
                            latLngs.add(latLng1);
                        }
                        return latLngs;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Observer<List<LatLng>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<LatLng> latLngs) {
                        if (latLngs != null && latLngs.size() > 0){
                            // 动画跳转
                            if (CHANGE == change){
                                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLngs.get(1));
                                mBaiduMap.animateMapStatus(u);
                            }

                            // 添加覆盖物
                            BitmapDescriptor mSelectIco = BitmapDescriptorFactory
                                    .fromResource(R.drawable.position);
                            mBaiduMap.clear();
                            for (int i = 0; i < latLngs.size(); i++) {
                                OverlayOptions ooA = new MarkerOptions().position(latLngs.get(i))
                                        .icon(mSelectIco).anchor(0.5f, 0.5f);
                                mBaiduMap.addOverlay(ooA);
                            }

                            change +=CHANGE;
                        }
                    }
                });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    List<LatLng> list = (List<LatLng>) msg.obj;
                    if (list != null && list.size() > 0){
                        // 动画跳转
                        if (CHANGE == change){
                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(list.get(moveNumber/2));
                            mBaiduMap.animateMapStatus(u);
                        }

                        // 添加覆盖物
                        BitmapDescriptor mSelectIco = BitmapDescriptorFactory
                                .fromResource(R.drawable.position);
                        mBaiduMap.clear();

                        for (int i = 0; i < list.size(); i++) {
                            OverlayOptions ooA = new MarkerOptions().position(list.get(i))
                                    .icon(mSelectIco).anchor(0.5f, 0.5f);
                            mBaiduMap.addOverlay(ooA);
                        }
                        change +=CHANGE;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void startMove() {
        /*
         * 开线程
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                 * 确保线程不断执行不断刷新界面
                 */
                while (!isStopMove) {
                    try {
                        List<LatLng> latLngs = new ArrayList<LatLng>();
                        latLngs.clear();
                        for (int i = 0; i < moveNumber; i++) {
                            LatLng latLng= new LatLng(mLantitude + change, mLongtitude+ 0.001 * (i + 1));
                            latLngs.add(latLng);
                        }

                        Message message = new Message();
                        message.what = 1;
                        message.obj = latLngs;
                        mHandler.sendMessage(message);
                        // 每执行一次暂停40毫秒
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void turnBack() {
        // 实现动画跳转
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLoactionLatLng);
        mBaiduMap.animateMapStatus(u);

        mBaiduMap.clear();
        // 发起反地理编码检索
        mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                .location(mLoactionLatLng));
    }
}
