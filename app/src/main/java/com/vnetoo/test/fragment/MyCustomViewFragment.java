package com.vnetoo.test.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vnetoo.test.R;
import com.vnetoo.test.activity.ContainerActivity;
import com.vnetoo.test.adapter.MyAdapter;
import com.vnetoo.test.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class MyCustomViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    View mContentView;

    MyAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.activity_main,container,false);
        setupViews();
        return mContentView;
    }

    private void setupViews() {
        mAdapter = new MyAdapter();
        ListView listView = (ListView) mContentView.findViewById(R.id.lv_main);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        List<String> list = new ArrayList<String>();
        list.add("自定义view");
        list.add("自定义view2--ColorMatrix");
        list.add("自定义view3--LightingColorFilter和PorterDuffColorFilter");
        list.add("自定义view4--PorterDuffXfermode - 抠图");
        list.add("自定义view5-PorterDuffXfermode-橡皮擦效果");
        list.add("自定义view6-模仿qq效果");
        list.add("自定义view7-倒影效果");
        list.add("自定义view8-多圆图");
        list.add("自定义view9-drawBitmapMesh错切图");
        list.add("自定义view10-drawPath杯子中水消匿的效果");
        list.add("自定义view11-翻页效果尝试实现一");
        list.add("自定义view12-圆形进度");
//        list.add("自定义view5");
//        list.add("自定义view6");
        List<BaseBean> data = new ArrayList<BaseBean>();
        for (int i = 0; i < list.size(); i++) {
            BaseBean baseBean = new BaseBean();
            baseBean.id = i;
            baseBean.name = list.get(i);
            data.add(baseBean);
        }

        mAdapter.setListData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(),ContainerActivity.class);
        intent.putExtra(ContainerActivity.CLASS_NAME, CustomViewFragment.class.getName());
        switch (i){
            case 0:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview));
                break;
            case 1:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview2));
                break;
            case 2:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview3));
                break;
            case 3:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview4));
                break;
            case 4:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview5));
                break;
            case 5:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview6));
                break;
            case 6:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview7));
                break;
            case 7:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview8));
                break;
            case 8:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview9));
                break;
            case 9:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview10));
                break;
            case 10:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview11));
                break;
            case 11:
                intent.putExtras(CustomViewFragment.getBundle(R.layout.fragment_customview12));
                break;
            default:
                break;
        }
        startActivity(intent);
    }


}
