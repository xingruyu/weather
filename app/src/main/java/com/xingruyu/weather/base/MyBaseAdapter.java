package com.xingruyu.weather.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 所有adapter得基类
 * Created by WDX on 2016/11/12.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    private List<T> date;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyBaseAdapter(Context mContext,List<T> date) {
        super();
        this.mContext = mContext;
        this.date = date;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return date == null ? 0 : date.size();
    }

    @Override
    public T getItem(int position) {
        if (position < date.size()){
            return date.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, mInflater);
    }

    public abstract View createView(int position, View convertView, ViewGroup parent, LayoutInflater inflater);
}
