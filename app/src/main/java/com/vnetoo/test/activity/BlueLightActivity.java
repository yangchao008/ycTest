package com.vnetoo.test.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.vnetoo.test.R;

/**
 * Author: yangchao
 * Date: 2018-02-08 15:13
 * Comment: //TODO
 */
public class BlueLightActivity extends Activity{

    private WindowManager.LayoutParams params;
    private WindowManager mWindowManager;
    private View mOverlayView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_light);

        initBlueLightLayer();
        ((SeekBar)findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateLight(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateLight(int progress) {
        mOverlayView.setBackgroundColor(getColor2(progress));
        mWindowManager.updateViewLayout(mOverlayView, params);
    }

    /**
     * 过滤蓝光
     * @param blueFilterPercent 蓝光过滤比例[10-80]
     * */
    public int getColor2(int blueFilterPercent){
        int realFilter = blueFilterPercent;
        if (realFilter < 10){
            realFilter = 10;
        }
        else if (realFilter > 80){
            realFilter = 80;
        }
        int a = (int) (realFilter / 80f * 180);
        int r = (int) (200 - (realFilter / 80f) * 190);
        int g = (int) (180 - ( realFilter / 80f) * 170);
        int b = (int) (60 - realFilter / 80f * 60);
        return Color.argb(a, r, g, b);
    }

    private void initBlueLightLayer() {
        //init about blue filter params

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT);

        // An alpha value to apply to this entire window.
        // An alpha of 1.0 means fully opaque and 0.0 means fully transparent
        params.alpha = 0.1F;

        // When FLAG_DIM_BEHIND is set, this is the amount of dimming to apply.
        // Range is from 1.0 for completely opaque to 0.0 for no dim.
        //params.dimAmount = 0.5F;
        params.dimAmount = 0F;

        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(this);
        mOverlayView = inflater.inflate(R.layout.fiter_layout, null);

        mWindowManager.addView(mOverlayView,params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mOverlayView);
        mWindowManager = null;
    }
}
