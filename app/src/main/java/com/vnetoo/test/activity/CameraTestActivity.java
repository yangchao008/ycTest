package com.vnetoo.test.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.vnetoo.test.R;

import java.io.IOException;

/**
 * Author: yangchao
 * Date: 2017-12-13 11:03
 * Comment: //TODO
 */
public class CameraTestActivity extends Activity implements SurfaceHolder.Callback{
    Camera myCamera;
    SurfaceView mySurfaceView;
    SurfaceHolder mySurfaceHolder;
    Button myButton1;
    Button myButton2;
    boolean isPreview = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test);

        mySurfaceView = (SurfaceView)findViewById(R.id.mySurfaceView);
        myButton1 = (Button)findViewById(R.id.myButton);
        myButton2 = (Button)findViewById(R.id.myButton2);

        mySurfaceHolder = mySurfaceView.getHolder();
        mySurfaceHolder.addCallback(this);
        mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        myButton1.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                initCamera();
            }
        });
        myButton2.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(myCamera!=null&&isPreview){
                    myCamera.stopPreview();
                    myCamera.release();
                    myCamera = null;
                    isPreview = false;
                }
            }

        });
    }
    public void initCamera() {
        // TODO Auto-generated method stub
        if(!isPreview){
            myCamera = Camera.open();
        }
        if(myCamera !=null && !isPreview){
            try{
                myCamera.setPreviewDisplay(mySurfaceHolder);
                myCamera.startPreview();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            isPreview = true;
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }
}