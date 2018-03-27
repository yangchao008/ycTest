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
import com.vnetoo.test.utils.MyLog;

import java.util.Queue;

import cn.kejin.ximageview.XImageView;

/**
 * Author: yangchao
 * Date: 2017-12-11 10:05
 * Comment: //TODO
 */
public class XImageViewActivity extends FragmentActivity{
    final String TAG = getClass().getName();
    XImageView mXImageView;
    StringBuffer msg = new StringBuffer();

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
        TreeNode a = new TreeNode();
        a.value = "A";
        TreeNode b = new TreeNode();
        b.value = "B";
        TreeNode c = new TreeNode();
        c.value = "C";
        TreeNode d = new TreeNode();
        d.value = "D";
        TreeNode e = new TreeNode();
        e.value = "E";
        TreeNode f = new TreeNode();
        f.value = "F";
        TreeNode g = new TreeNode();
        g.value = "G";

        a.left = b;
        a.right = c;

        b.left = d;
        b.right = e;

        c.left = f;
        c.right = g;

        printPreTree(a);
        MyLog.l(TAG,"printPreTree = " + msg);
        msg = new StringBuffer();
        printMidTree(a);
        MyLog.l(TAG,"printMidTree = " + msg);
        msg = new StringBuffer();
        printAfterTree(a);
        MyLog.l(TAG,"printAfterTree = " + msg);
    }

    public void printPreTree(TreeNode root){
        if (root == null)
            return;
        msg.append( "--" + root.value);
        printPreTree(root.left);
        printPreTree(root.right);

    }

    public void printMidTree(TreeNode root){
        if (root == null)
            return;
        printPreTree(root.left);
        msg.append( "--" + root.value);
        printPreTree(root.right);

    }

    public void printAfterTree(TreeNode root){
        if (root == null)
            return;
        printPreTree(root.left);
        printPreTree(root.right);
        msg.append( "--" + root.value);
    }

    /**
     ** 二叉树节点
     **/
    public class TreeNode{
        TreeNode left;
        TreeNode right;
        String value;
    }
}
