package com.vnetoo.test.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.bm.library.PhotoView;
import com.vnetoo.test.R;

/**
 * Author: yangchao
 * Date: 2017-12-11 10:05
 * Comment: //TODO
 */
public class PhotoViewActivity extends FragmentActivity{
    PhotoView mPhotoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo_view);
        mPhotoView = (PhotoView) findViewById(R.id.photoView);
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fy3);
        mPhotoView.setImageBitmap(srcBitmap);
        mPhotoView.enable();
    }
}
