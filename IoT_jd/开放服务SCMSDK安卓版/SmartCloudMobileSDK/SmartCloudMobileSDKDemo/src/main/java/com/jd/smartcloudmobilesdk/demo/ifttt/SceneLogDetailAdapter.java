package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.ActionLog;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 场景列表适配器
 * Created by yangchangan on 2017/3/8.
 */
public class SceneLogDetailAdapter extends ArrayListAdapter<ActionLog> {

    private SimpleDateFormat format;

    public SceneLogDetailAdapter(Context context) {
        super(context);

        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_scene_log_detail, null);
            holder = new ViewHolder();

            holder.nameView = findViewById(convertView, R.id.tv_name);
            holder.timeView = findViewById(convertView, R.id.tv_time);
            holder.statusView = findViewById(convertView, R.id.tv_status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ActionLog actionLog = getItem(position);
        holder.nameView.setText(actionLog.getAction_id());
        holder.timeView.setText(formTime(actionLog.getEnd_time()));
        holder.statusView.setText(String.format("status：%d", actionLog.getStatus()));

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView timeView;
        TextView statusView;
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
