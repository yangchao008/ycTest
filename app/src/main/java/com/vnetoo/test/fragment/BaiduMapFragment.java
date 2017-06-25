package com.vnetoo.test.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;
import com.baidu.mapapi.search.poi.*;
import com.vnetoo.test.MyApplication;
import com.vnetoo.test.R;
import com.vnetoo.test.adapter.PlaceListAdapter;
import com.vnetoo.test.utils.MyHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 内容摘要：
 * 完成日期：2017/4/1
 * 编码作者：杨超 .
 */
public class BaiduMapFragment extends Fragment implements View.OnClickListener {

    View mContentView;
    BaiduMap mBaiduMap;
    MapView mMapView;
    ImageView mSelectImg;
    ProgressBar mLoadBar;
    EditText et_key;
    PopupWindow mSearchResultPopupWindow;
    ProgressDialog mProgressDialog;

    InputMethodManager mInputMethodManager;

    MapFragment mParentFragment;

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
    ListView mListView;
    PlaceListAdapter mAdapter,mSearchResultAdapter;
    List<PoiInfo> mInfoList,mSearchInfoList;
    PoiInfo mCurrentInfo,mCurrentPosition;

    public void initPoiInfo(PoiInfo info){
        if (null != info){
            LatLng la = info.location;

            // 动画跳转
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(la);
            mBaiduMap.animateMapStatus(u);
        }else{
            turnBack();
        }
    }

    public PoiInfo getSelectPoiInfo() {
        PoiInfo poiInfo = null;
        if (mAdapter.getNotifyTip() > -1){
            poiInfo = mAdapter.getItem(mAdapter.getNotifyTip());
        }
        return poiInfo;
    }

    public PoiInfo getCurrentPosition() {
        turnBack();
        return mCurrentPosition;
    }

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
        mProgressDialog = MyHelper.getProgressDialog(getActivity(),false);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_baidumap,container,false);

        initView();

        et_key = (EditText) mContentView.findViewById(R.id.et_key);
        mContentView.findViewById(R.id.btn_ok).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_back).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_search).setOnClickListener(this);

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
        mPoiSearch.setOnGetPoiSearchResultListener(SearchListener);

        // 初始化当前MapView中心屏幕坐标，初始化当前地理坐标
        mCenterPoint = mBaiduMap.getMapStatus().targetScreen;

        mLoactionLatLng = mBaiduMap.getMapStatus().target;

        // 定位
        getCurrentLocation();

        // 地理编码
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);

        // 周边位置列表
        mListView = (ListView)mContentView.findViewById(R.id.place_list);
        mLoadBar = (ProgressBar)mContentView.findViewById(R.id.place_progressBar);
        mListView.setOnItemClickListener(itemClickListener);
        mAdapter = new PlaceListAdapter(getActivity().getLayoutInflater(), mInfoList);
        mSearchResultAdapter = new PlaceListAdapter(getActivity().getLayoutInflater(), mSearchInfoList);
        mListView.setAdapter(mAdapter);

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

    OnGetPoiSearchResultListener SearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult result) {
            mProgressDialog.dismiss();
            if(result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND==result.error){
                Toast.makeText(getActivity(), "未搜索到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            mSearchInfoList.clear();
            mSearchInfoList.addAll(result.getAllPoi());
            mSearchResultAdapter.notifyDataSetChanged();
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        }
    };

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
                mCurrentInfo.name = "[当前位置]";

                if (null == mCurrentPosition)
                    mCurrentPosition = mCurrentInfo;
                mInfoList.clear();
                mInfoList.add(mCurrentInfo);

                // 将周边信息加入表
                if (result.getPoiList() != null) {
                    mInfoList.addAll(result.getPoiList());
                }
                // 通知适配数据已改变
                mAdapter.setNotifyTip(0);
                mAdapter.notifyDataSetChanged();
                mLoadBar.setVisibility(View.GONE);
                if (null == mParentFragment)
                    mParentFragment = (MapFragment)getParentFragment();
                mParentFragment.dismissProgressBarView();

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

    // listView选项点击事件监听器
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub

            // 通知是适配器第position个item被选择了
            mAdapter.setNotifyTip(position);
            mAdapter.notifyDataSetChanged();

            BitmapDescriptor mSelectIco = BitmapDescriptorFactory
                    .fromResource(R.drawable.position_icon);
            mBaiduMap.clear();
            PoiInfo info = mAdapter.getItem(position);
            LatLng la = info.location;

            // 动画跳转
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(la);
            mBaiduMap.animateMapStatus(u);

            // 添加覆盖物
//            OverlayOptions ooA = new MarkerOptions().position(la)
//                    .icon(mSelectIco).anchor(0.5f, 0.5f);
//            mBaiduMap.addOverlay(ooA);

            // 选中项打勾
            mSelectImg.setBackgroundResource(R.drawable.greywhite);
            mSelectImg = (ImageView) view.findViewById(R.id.place_select);
            mSelectImg.setBackgroundResource(R.drawable.selected_icon);

            // Uri mUri = Uri.parse("geo:39.940409,116.355257");
            // Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
            // startActivity(mIntent);

        }

    };

    private void getMyLocation() {
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(MyApplication.sContext); // 实例化LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        setLocationOption();   //设置定位参数
        mLocationClient.start(); // 开始定位
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); // 设置为一般地图

        // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE); //设置为卫星地图
        // mBaiduMap.setTrafficEnabled(true); //开启交通图
    }

    /**
     * 设置定位参数
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                if (null == mParentFragment)
                    mParentFragment = (MapFragment)getParentFragment();
                mParentFragment.closeMap(true);
                break;
            case R.id.btn_back:
                turnBack();
                break;
            case R.id.btn_search:
                searchByKey();
                break;
            default:
                break;
        }
    }

    private void searchByKey() {
        String key = et_key.getText().toString().trim();
        if (TextUtils.isEmpty(key)){
            Toast.makeText(getActivity(),"关键字不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        // 以下两行代码是强制关闭软键盘
        mInputMethodManager.showSoftInput(et_key,InputMethodManager.SHOW_FORCED);
        mInputMethodManager.hideSoftInputFromWindow(et_key.getWindowToken(),0);

        mProgressDialog.setMessage("搜索中");
        mProgressDialog.show();

//        PoiDetailSearchOption detailSearchOption = new PoiDetailSearchOption();
//        detailSearchOption.poiUid(key);
//        mPoiSearch.searchPoiDetail(detailSearchOption);
        PoiNearbySearchOption nearbySearchOption  = new PoiNearbySearchOption();
        nearbySearchOption.keyword(key).location(mLoactionLatLng).pageNum(0).radius(1000000).pageCapacity(20).sortType(PoiSortType.comprehensive);
        mPoiSearch.searchNearby(nearbySearchOption);

        if (null != mSearchResultPopupWindow && mSearchResultPopupWindow.isShowing()){
            return;
        }
        View view =LayoutInflater.from(getActivity()).inflate(R.layout.pop_search_result, null);
        mSearchResultPopupWindow = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView listView = (ListView) view.findViewById(R.id.lv_search_result);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setAdapter(mSearchResultAdapter);

        mSearchResultPopupWindow.setFocusable(true);
        mSearchResultPopupWindow.setTouchable(true);
        mSearchResultPopupWindow.setOutsideTouchable(true);

//        mSearchResultPopupWindow.setWidth(et_key.getWidth());
        mSearchResultPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mSearchResultPopupWindow.showAsDropDown(et_key,0,5);

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            PoiInfo poiInfo = mSearchResultAdapter.getItem(i);

            // 实现动画跳转
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(poiInfo.location);
            mBaiduMap.animateMapStatus(u);

            LatLng currentLatLng = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);

            mBaiduMap.clear();
            // 发起反地理编码检索
            mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                    .location(currentLatLng));

            mSearchResultPopupWindow.dismiss();
        }
    };

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
