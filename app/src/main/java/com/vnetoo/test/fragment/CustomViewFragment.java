package com.vnetoo.test.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vnetoo.test.R;
import com.vnetoo.test.views.CustomView;
import com.vnetoo.test.views.CustomView11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class CustomViewFragment extends Fragment {
    View mContentView;
    final static String LAYOUT_ID = "layoutId";

    public static Bundle getBundle(int layoutId){
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID,layoutId);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getActivity().getIntent().getIntExtra(LAYOUT_ID,R.layout.fragment_customview);
        mContentView = inflater.inflate(layoutId,container,false);

        if (R.layout.fragment_customview11==layoutId) {
            List<Bitmap> list = new ArrayList<Bitmap>();
            list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fy));
            list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fy2));
            list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fy3));
            list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fy4));
            list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fy5));
            ((CustomView11) mContentView.findViewById(R.id.customView11)).setBitmaps(list);
        }
        return mContentView;
    }
}
