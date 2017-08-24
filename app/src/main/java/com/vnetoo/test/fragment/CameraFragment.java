package com.vnetoo.test.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vnetoo.test.R;

/**
 * 内容摘要：
 * 完成日期：2017/4/1
 * 编码作者：杨超 .
 */
public class CameraFragment extends Fragment implements View.OnClickListener {

    View mContentView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_camera,container,false);

        mContentView.findViewById(R.id.btn_front).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_rear).setOnClickListener(this);
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (view.getId()){
            case R.id.btn_front:
//                android.service.
                intent.putExtra("android.service.extras.CAMERA_FACING", 1);
//                intent.putExtra("camerasensortype", 2);
                startActivityForResult(intent, 2);
                break;
            case R.id.btn_rear:
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

}
