package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Record;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 场景执行记录适配器
 * Created by yangchangan on 2017/3/8.
 */
public class SceneLogsAdapter extends ArrayListAdapter<Record> {

    private SimpleDateFormat format;

    public SceneLogsAdapter(Context context) {
        super(context);

        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_scene_logs, null);
            holder = new ViewHolder();

            holder.nameView = findViewById(convertView, R.id.tv_name);
            holder.timeView = findViewById(convertView, R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Record record = getItem(position);
        holder.nameView.setText(record.getLogic_name());
        holder.timeView.setText(formTime(record.getEnd_time()));

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView timeView;
    }

    private String formTime(long time) {

        try {
            return format.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
