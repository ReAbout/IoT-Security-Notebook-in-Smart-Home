package com.jd.smartcloudmobilesdk.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView Adapter 抽象类
 * Created by yangchangan on 2017/3/8.
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter {
    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mList = new ArrayList<>();

    public ArrayListAdapter(Context context) {
        this(context, null);
    }

    public ArrayListAdapter(Context context, List<T> list) {
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        mList = list;

        notifyDataSetChanged();
    }

    public void setList(T[] array) {
        List<T> list = new ArrayList<>(array.length);
        for (T t : array) {
            list.add(t);
        }
        setList(list);
    }

    public void remove(T object) {
        if (mList != null) {
            mList.remove(object);
        }

        notifyDataSetChanged();
    }

    public void insert(T object, int index) {
        if (mList != null) {
            mList.add(index, object);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected <V> V findViewById(View view, int id) {
        return (V) (view.findViewById(id));
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);

}
