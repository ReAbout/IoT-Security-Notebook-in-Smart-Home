package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Action;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Param;

/**
 * 场景执行任务适配器
 * Created by yangchangan on 2017/3/10.
 */
public class SceneActionAdapter extends ArrayListAdapter<Action> {

    public SceneActionAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.scene_event_action_item, null);
            holder = new ViewHolder();

            holder.lineView = findViewById(convertView, R.id.view_line);
            holder.nameView = findViewById(convertView, R.id.tv_name);
            holder.timeView = findViewById(convertView, R.id.tv_time);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (getCount() > 1) {
            holder.lineView.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.lineView.setBackgroundResource(R.mipmap.scene_mark_up);
            } else if (position == getCount() - 1) {
                holder.lineView.setBackgroundResource(R.mipmap.scene_mark_down);
            } else {
                holder.lineView.setBackgroundResource(R.mipmap.scene_mark_middle);
            }
        } else {
            holder.lineView.setVisibility(View.GONE);
        }

        final Action action = getItem(position);
        if (action.getDelayValue() > 0) {
            holder.nameView.setText("时间间隔");
            holder.timeView.setText(action.getDelayValue() / 1000 + "秒");
        } else {
            holder.nameView.setText(action.getService());
            if (action.getParam() != null) {
                Param param = action.getParam().get(0);
                holder.timeView.setText(param.getName() + " " + param.getValue());
            }
        }

        return convertView;
    }

    private class ViewHolder {
        View lineView;
        TextView timeView;
        TextView nameView;
    }
}
