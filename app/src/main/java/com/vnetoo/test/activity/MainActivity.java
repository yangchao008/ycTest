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

    private final String[] mListData = {"test", "计数器（Service,广播）", "Download Manager","百度地图","百度地图2222","Android中触摸事件传递",
            "自定义View"};
    private final int[] mListId = {TEST,COUNTER,DOWNLOAD_MANAGER,BAIDU_MAP,BAIDU_MAP_22,TOUCH_EVENT_TEST,CUSTOM_VIEW};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
    }

    private void setupViews() {
        mAdapter = new MyAdapter();
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
        switch ((int)id){
            case TEST:
//                startActivity(new Intent(this, com.vnetoo.mymodule.MainActivity.class));
                return;
            case COUNTER:
                intent.putExtra(ContainerActivity.CLASS_NAME, CounterFragment.class.getName());
                break;
            case DOWNLOAD_MANAGER:
                intent.putExtra(ContainerActivity.CLASS_NAME, SimpleDownloadTestFragment.class.getName());
                break;
            case BAIDU_MAP:
                intent.putExtra(ContainerActivity.CLASS_NAME, MapFragment.class.getName());
                break;
            case BAIDU_MAP_22:
                intent.putExtra(ContainerActivity.CLASS_NAME, BaiduMapFragment2.class.getName());
                break;
            case TOUCH_EVENT_TEST:
                startActivity(new Intent(this, TouchEventTestActivity.class));
                return;
            case CUSTOM_VIEW:
                intent.putExtra(ContainerActivity.CLASS_NAME, MyCustomViewFragment.class.getName());
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
