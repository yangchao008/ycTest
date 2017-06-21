package com.vnetoo.test.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 内容摘要：
 * 完成日期：2017/4/7
 * 编码作者：杨超 .
 */
public class MyHelper {

    /**
     * 获取进度对话框
     * @param context
     * @param isCancelable
     * @return
     */
    public static ProgressDialog getProgressDialog(Context context,final boolean isCancelable){
        ProgressDialog progressDialog = new ProgressDialog(context){
            @Override
            public void show() {
                super.show();
                setCancelable(isCancelable);
            }
        };
        return progressDialog;
    }

}
