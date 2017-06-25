package com.vnetoo.test.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.vnetoo.test.R;

import java.util.List;

/**
 * 内容摘要：
 * 完成日期：2017/4/1
 * 编码作者：杨超 .
 */
public class PlaceListAdapter extends BaseAdapter {

    List<PoiInfo> mList;
    LayoutInflater mInflater;
    int notifyTip ;

    private class MyViewHolder {
        TextView placeName;
        TextView placeAddree;
        ImageView placeSelected;
    }

    public PlaceListAdapter(LayoutInflater mInflater , List<PoiInfo> mList) {
        super();
        this.mList = mList;
        this.mInflater = mInflater;
        notifyTip = -1 ;
    }

    /**
     * 获得第几个item被选择
     */
    public int getNotifyTip() {
        return notifyTip;
    }

    /**
     * 设置第几个item被选择
     * @param notifyTip
     */
    public void setNotifyTip(int notifyTip) {
        this.notifyTip = notifyTip;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public PoiInfo getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        MyViewHolder holder;
        if (convertView == null) {
            System.out.println("----aa-");
            convertView = mInflater.inflate(R.layout.listitem_place, parent, false);
            holder = new MyViewHolder();
            holder.placeName = (TextView) convertView
                    .findViewById(R.id.place_name);
            holder.placeAddree = (TextView) convertView
                    .findViewById(R.id.place_adress);
            holder.placeSelected = (ImageView) convertView
                    .findViewById(R.id.place_select);
            holder.placeName.setText(mList.get(position).name);
            holder.placeAddree.setText(mList.get(position).address);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.placeName.setText(mList.get(position).name);
        holder.placeAddree.setText(mList.get(position).address);
        //根据重新加载的时候第position条item是否是当前所选择的，选择加载不同的图片
        if(notifyTip == position ){
            holder.placeSelected.setBackgroundResource(R.drawable.selected_icon);
        }
        else {
            holder.placeSelected.setBackgroundResource(Color.TRANSPARENT);
        }

        return convertView;
    }


// class MyItemClickListener implements OnClickListener {
//
// ImageView mImg;
// public MyItemClickListener(ImageView mImg) {
// this.mImg = mImg;
// }
// @Override
// public void onClick(View v) {
// // TODO Auto-generated method stub
// mImg.setBackgroundResource(R.drawable.position_icon);
// }
//
// }


}