package com.vnetoo.test.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

import com.baidu.platform.comapi.map.B;
import com.bm.library.PhotoView;
import com.vnetoo.test.R;

import cn.kejin.ximageview.XImageView;

/**
 * Author: yangchao
 * Date: 2017-12-11 10:05
 * Comment: //TODO
 */
public class XImageViewActivity extends FragmentActivity{
    XImageView mXImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ximageview);
        mXImageView = (XImageView) findViewById(R.id.xImageView);
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fy3);
        mXImageView.setImage(srcBitmap);
        mXImageView.setActionListener(new XImageView.OnActionListener() {
            @Override
            public void onSingleTapped(XImageView view, MotionEvent event, boolean onImage) {

            }

            @Override
            public boolean onDoubleTapped(XImageView view, MotionEvent event) {
                return false;
            }

            @Override
            public void onLongPressed(XImageView view, MotionEvent event) {

            }

            @Override
            public void onSetImageStart(XImageView view) {

            }

            @Override
            public void onSetImageFinished(XImageView view, boolean success, Rect image) {

            }
        });
    }
}
