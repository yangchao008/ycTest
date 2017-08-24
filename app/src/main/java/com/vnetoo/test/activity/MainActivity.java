package com.vnetoo.test.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.vnetoo.test.R;
import com.vnetoo.test.adapter.MyAdapter;
import com.vnetoo.test.bean.BaseBean;
import com.vnetoo.test.fragment.*;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private MyAdapter mAdapter;

    public static final int TEST = 0;
    public static final int COUNTER = 1;
    public static final int DOWNLOAD_MANAGER = 2;
    public static final int BAIDU_MAP = 3;
    public static final int BAIDU_MAP_22 = 4;
    public static final int TOUCH_EVENT_TEST = 5;
    public static final int CUSTOM_VIEW = 6;
    public static final int OPEN_CAMERA = 7;

    private final String[] mListData = {"new activity", "计数器（Service,广播）", "Download Manager","百度地图定位","百度地图点移动","Android中触摸事件传递",
            "自定义View","打开前后摄像头"};
    private final int[] mListId = {TEST,COUNTER,DOWNLOAD_MANAGER,BAIDU_MAP,BAIDU_MAP_22,TOUCH_EVENT_TEST,CUSTOM_VIEW,OPEN_CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
    }

    private void setupViews() {
        mAdapter = new MyAdapter();
        findViewById(R.id.btn_back).setVisibility(View.GONE);
        ListView listView = (ListView) findViewById(R.id.lv_main);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        initData();
    }

    private void initData() {
        List<BaseBean> data = new ArrayList<BaseBean>();
        for (int i = 0; i < mListData.length; i++) {
            BaseBean baseBean = new BaseBean();
            baseBean.id = mListId[i];
            baseBean.name = mListData[i];
            data.add(baseBean);
        }

        mAdapter.setListData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this,ContainerActivity.class);
        String className = "",title = "";
        switch ((int)id){
            case TEST:
                className = NewActivityFragment.class.getName();
                title = "new activity";
                break;
            case COUNTER:
                className = CounterFragment.class.getName();
                title = "计数器";
                break;
            case DOWNLOAD_MANAGER:
                className = SimpleDownloadTestFragment.class.getName();
                title = "下载器";
                break;
            case BAIDU_MAP:
                className = MapFragment.class.getName();
                title = "地图定位";
                break;
            case BAIDU_MAP_22:
                className = BaiduMapFragment2.class.getName();
                title = "地图上点移动";
                break;
            case TOUCH_EVENT_TEST:
                startActivity(new Intent(this, TouchEventTestActivity.class));
                return;
            case CUSTOM_VIEW:
                className = MyCustomViewFragment.class.getName();
                title = "自定义View";
                break;
            case OPEN_CAMERA:
                className = CameraFragment.class.getName();
                title = mListData[OPEN_CAMERA];
                break;
            default:
                break;
        }
        intent.putExtra(ContainerActivity.TITLE, title);
        intent.putExtra(ContainerActivity.CLASS_NAME, className);
        startActivity(intent);
    }
}
