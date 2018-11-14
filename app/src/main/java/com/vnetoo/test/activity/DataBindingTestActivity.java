package com.vnetoo.test.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vnetoo.test.databinding.DataBindingTestBinding;

/**
 * Date: 2018/11/10 9:55
 * Author: hansyang
 * Description:
 */
public class DataBindingTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingTestBinding binding = DataBindingTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
