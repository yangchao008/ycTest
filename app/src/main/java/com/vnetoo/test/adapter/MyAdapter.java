package com.vnetoo.test.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.vnetoo.test.MyApplication;
import com.vnetoo.test.R;
import com.vnetoo.test.bean.BaseBean;

import java.util.List;

/**
 * 内容摘要：
 * 完成日期：2017/3/20
 * 编码作者：杨超 .
 */
public class MyAdapter extends BaseAdapter{

    List<BaseBean> mListData;

    public void setListData(List<BaseBean> mListData) {
        this.mListData = mListData;
    }

    @Override
    public int getCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public BaseBean getItem(int i) {
        return mListData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = new TextView(MyApplication.sContext);
            holder.tv_title= (TextView)view;
            holder.tv_title.setTextColor(MyApplication.sContext.getResources()
                    .getColor(R.color.text_black3));
            holder.tv_title.setTextSize(15);
            holder.tv_title.setPadding(20,20,0,20);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        BaseBean item = getItem(i);
        holder.tv_title.setText(item.name);
        return view;
    }

    class ViewHolder{
        TextView tv_title;
    }
}
