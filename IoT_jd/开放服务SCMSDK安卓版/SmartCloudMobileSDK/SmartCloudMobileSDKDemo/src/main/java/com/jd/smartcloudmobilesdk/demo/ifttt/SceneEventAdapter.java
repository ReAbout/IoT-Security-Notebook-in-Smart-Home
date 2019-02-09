package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Condition;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Event;
import com.jd.smartcloudmobilesdk.demo.utils.TimeUtils;

import java.util.Map;

/**
 * 场景事件适配器
 * Created by yangchangan on 2017/3/10.
 */
public class SceneEventAdapter extends ArrayListAdapter<Event> {

    public SceneEventAdapter(Context context) {
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
            holder.timerLayout = findViewById(convertView, R.id.layout_timer);
            holder.arrowView = findViewById(convertView, R.id.iv_arrow);
            holder.handView = findViewById(convertView, R.id.iv_hand);

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

        final Event event = getItem(position);
        if ("stream_id".equals(event.getType())) {

            //设备触发
            holder.nameView.setText(event.getService());
            if (event.getCondition() != null && !event.getCondition().isEmpty()) {
                Condition condition = event.getCondition().get(0);
                holder.timeView.setText(condition.getName() + condition.getOperator() + condition.getValue());
            }

            holder.timeView.setVisibility(View.VISIBLE);
            holder.timerLayout.setVisibility(View.GONE);
            holder.arrowView.setVisibility(View.VISIBLE);
            holder.handView.setVisibility(View.GONE);
        } else if ("signal".equals(event.getType())) {

            // 手动
            holder.nameView.setText("手动点击执行");

            holder.timeView.setVisibility(View.GONE);
            holder.timerLayout.setVisibility(View.GONE);
            holder.arrowView.setVisibility(View.GONE);
            holder.handView.setVisibility(View.VISIBLE);
        } else if ("local".equals(event.getType())) {

            // 定时
            holder.nameView.setText("定时启动");

            holder.timeView.setVisibility(View.VISIBLE);
            holder.timerLayout.setVisibility(View.VISIBLE);
            holder.arrowView.setVisibility(View.VISIBLE);
            holder.handView.setVisibility(View.GONE);

            if (event.getCondition() != null && !event.getCondition().isEmpty()) {
                if ("time".equals(event.getCondition().get(0).getName())) {
                    String value = event.getCondition().get(0).getValue();
                    Map<String, String> map = TimeUtils.getShowTitleWithTimeValue(value);
                    holder.timeView.setText(map.get(TimeUtils.LOCAL_SHOW_TIME) + "|");
                    holder.timerLayout.removeAllViews();
                    if (map.containsKey(TimeUtils.LOCAL_SHOW_WEEK)) {
                        String[] weeks = map.get(TimeUtils.LOCAL_SHOW_WEEK).split(",");
                        for (int i = 0; i < weeks.length; i++) {
                            ImageView view = new ImageView(mContext);
                            view.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), Integer.parseInt(weeks[i])));
                            holder.timerLayout.addView(view);
                        }
                    } else {
                        TextView textView = new TextView(mContext);
                        textView.setTextColor(0xff459efe);
                        textView.setTextSize(14);
                        textView.setText(map.get(TimeUtils.LOCAL_SHOW_DATE));
                        holder.timerLayout.addView(textView);
                    }
                }
            }
        }

        return convertView;
    }

    private class ViewHolder {
        View lineView;
        TextView nameView;
        TextView timeView;
        LinearLayout timerLayout;
        View arrowView;
        View handView;
    }
}
