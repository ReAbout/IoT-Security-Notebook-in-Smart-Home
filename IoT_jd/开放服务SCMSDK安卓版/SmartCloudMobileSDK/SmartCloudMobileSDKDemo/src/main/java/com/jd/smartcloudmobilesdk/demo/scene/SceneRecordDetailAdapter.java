package com.jd.smartcloudmobilesdk.demo.scene;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneRecordDetail;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneStream;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * 执行记录详情适配器
 * Created by yangchangan on 2017/6/26.
 */
public class SceneRecordDetailAdapter extends ArrayListAdapter<SceneRecordDetail> {

    public SceneRecordDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_scene_record_detail, null);
            holder = new ViewHolder();

            holder.timeView = findViewById(convertView, R.id.tv_time);
            holder.nameView = findViewById(convertView, R.id.tv_name);
            holder.descView = findViewById(convertView, R.id.tv_desc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SceneRecordDetail recordDetail = getItem(position);
        holder.timeView.setText(getTime(recordDetail.getStartTime()));
        holder.nameView.setText(getName(recordDetail.getDevice_name()));
        holder.descView.setText(getDesc(recordDetail.getStatus(), recordDetail.getStreams()));

        return convertView;
    }

    static class ViewHolder {
        TextView timeView;
        TextView nameView;
        TextView descView;
    }

    private String getName(String name) {
        return TextUtils.isEmpty(name) ? "设备已删除" : name;
    }

    private String getTime(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            return dateFormat.format(format.parse(time));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getDesc(int status, List<SceneStream> streams) {
        StringBuilder builder = new StringBuilder();
        if (streams != null && !streams.isEmpty()) {
            for (SceneStream stream : streams) {
                builder.append(stream.getStream_name_zh()).append(" ");
                builder.append(stream.getCurrent_value_zh()).append(" ");
            }
        }
        builder.append(getStatus(status));

        return builder.toString();
    }

    private String getStatus(int status) {
        String statusStr;
        switch (status) {
            case 1:
                statusStr = "已执行";
                break;
            case 2:
                statusStr = "未执行";
                break;
            case 3:
                statusStr = "设备离线";
                break;
            case 7:
                statusStr = "未执行";
                break;
            default:
                statusStr = "设备离线";
                break;
        }
        return statusStr;
    }
}
