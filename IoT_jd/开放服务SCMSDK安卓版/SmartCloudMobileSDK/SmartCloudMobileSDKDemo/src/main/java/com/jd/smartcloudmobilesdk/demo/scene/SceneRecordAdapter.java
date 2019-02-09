package com.jd.smartcloudmobilesdk.demo.scene;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneRecord;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 执行记录适配器
 * Created by yangchangan on 2017/6/26.
 */
public class SceneRecordAdapter extends ArrayListAdapter<SceneRecord> {

    public SceneRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_scene_record, null);
            holder = new ViewHolder();

            holder.nameView = findViewById(convertView, R.id.tv_name);
            holder.timeView = findViewById(convertView, R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SceneRecord record = getItem(position);
        holder.nameView.setText(record.getName());
        holder.timeView.setText(getTime(record.getEndTime()));

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView timeView;
    }

    private String getTime(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            return format.format(dateFormat.parse(time));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
