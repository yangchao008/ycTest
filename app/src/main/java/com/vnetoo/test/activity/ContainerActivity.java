package com.vnetoo.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.vnetoo.test.R;
import com.vnetoo.test.fragment.MapFragment;
import com.vnetoo.test.fragment.NewActivityFragment;
import com.vnetoo.test.fragment.dummy.DummyContent;

/**
 * 内容摘要：
 * 完成日期：2017/3/20
 * 编码作者：杨超 .
 */
public class ContainerActivity extends FragmentActivity implements View.OnClickListener ,NewActivityFragment.OnListFragmentInteractionListener {
    public static final String CLASS_NAME = "class_name";
    public static final String BUNDLE_EXTRA = "bundle_extra";
    public static final String TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        setupViews();

    }

    private void setupViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        String title = getIntent().getStringExtra(TITLE);
        ((TextView)findViewById(R.id.tv_title)).setText(TextUtils.isEmpty(title) ? getString(R.string.title) : title);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

        int position = Integer.parseInt(item.id);
        Intent intent = null;
        switch (position){
            case 1:
                intent = new Intent(this,BasicActivity.class);
                break;
            case 2:
                intent = new Intent(this,BottomNavigationActivity.class);
                break;
            case 3:
                intent = new Intent(this,EmptyActivity.class);
                break;
            case 4:
                intent = new Intent(this,FullscreenActivity.class);
                break;
            case 5:
                intent = new Intent(this,LoginActivity.class);
                break;
            case 6:
                intent = new Intent(this,MyItemListActivity.class);
                break;
            case 7:
                intent = new Intent(this,NavigationDrawerActivity.class);
                break;
            case 8:
                intent = new Intent(this,ScrollingActivity.class);
                break;
            case 9:
                intent = new Intent(this,SettingsActivity.class);
                break;
            case 10:
                intent = new Intent(this,TabbedActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
}
