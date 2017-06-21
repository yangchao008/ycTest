package com.vnetoo.test.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.vnetoo.test.R;
import com.vnetoo.test.fragment.MapFragment;

/**
 * 内容摘要：
 * 完成日期：2017/3/20
 * 编码作者：杨超 .
 */
public class ContainerActivity extends FragmentActivity{
    public static final String CLASS_NAME = "class_name";
    public static final String BUNDLE_EXTRA = "bundle_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        setupViews();

    }

    private void setupViews() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (null == fragment){
            fragment = Fragment.instantiate(this,getIntent().getStringExtra(CLASS_NAME),getIntent()
                    .getBundleExtra(BUNDLE_EXTRA));
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment,getClass()
                    .getSimpleName()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (null != fragment && fragment instanceof MapFragment){
            int index = ((MapFragment)fragment).getFragmentIndex();
            if (1 == index){
                ((MapFragment)fragment).closeMap(false);
            }else   super.onBackPressed();
        }else   super.onBackPressed();
    }
}
