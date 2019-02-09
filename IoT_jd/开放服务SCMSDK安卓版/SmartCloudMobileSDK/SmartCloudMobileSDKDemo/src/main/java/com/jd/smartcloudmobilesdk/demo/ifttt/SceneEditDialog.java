package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;

import java.util.List;

/**
 * 场景编辑对话框
 * Created by yangchangan on 2017/3/27.
 */

public class SceneEditDialog extends Dialog {

    private ListView mListView;
    private ListAdapter mAdapter;
    private OnItemClickListener mListener;

    private Context mContext;
    private List<String> mList;

    public SceneEditDialog(Context context) {
        super(context, R.style.jdPromptDialog1);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_edit_dialog);

        mListView = (ListView) findViewById(R.id.list_view);

        mAdapter = new ListAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
    }

    public void setList(List<String> list) {
        mList = list;

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    /**
     * 列表适配器
     */
    private class ListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList == null ? null : mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = View.inflate(mContext, R.layout.item_scene_edit, null);
                viewHolder = new ViewHolder();
                viewHolder.nameView = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.nameView.setText(mList.get(position));

            return view;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mListener != null) {
                mListener.onItemClick((String) getItem(position));
            }
        }
    }

    static class ViewHolder {
        TextView nameView;
    }


}
