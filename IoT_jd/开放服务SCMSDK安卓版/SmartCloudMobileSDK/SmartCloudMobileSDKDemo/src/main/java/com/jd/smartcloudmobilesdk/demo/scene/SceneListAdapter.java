package com.jd.smartcloudmobilesdk.demo.scene;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneListModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 场景列表适配器
 * Created by yangchangan on 2017/6/26.
 */
public class SceneListAdapter extends ArrayListAdapter<SceneListModel> {

    public SceneListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_scene_list2, null);
            holder = new ViewHolder();

            holder.headLayout = findViewById(convertView, R.id.layout_head);
            holder.nameView = findViewById(convertView, R.id.tv_name);
            holder.numView = findViewById(convertView, R.id.tv_num);
            holder.timeMarkView = findViewById(convertView, R.id.tv_time_mark);
            holder.timeExeView = findViewById(convertView, R.id.tv_time_exe);
            holder.containerLayout = findViewById(convertView, R.id.layout_container);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SceneListModel model = getItem(position);
        holder.nameView.setText(model.getName());
        holder.numView.setText(String.format("%d个设备", model.getImages().size()));

        if (TextUtils.isEmpty(model.getNext_exe_time())) {
            holder.timeMarkView.setVisibility(View.GONE);
            holder.timeExeView.setText("");
        } else {
            holder.timeMarkView.setVisibility(View.VISIBLE);

            String timeExpress = getTimeExpress(model.getNext_exe_time(), model.getNext_exe_time_express());
            holder.timeExeView.setText(timeExpress);
        }

        holder.headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SceneDetailUI.class);
                intent.putExtra("scene_id", model.getId());
                intent.putExtra("scene_name", model.getName());
                mContext.startActivity(intent);
            }
        });
        addImages(holder.containerLayout, model.getImages());

        return convertView;
    }

    static class ViewHolder {
        View headLayout;
        TextView nameView;
        TextView numView;
        TextView timeMarkView;
        TextView timeExeView;
        LinearLayout containerLayout;
    }

    private void addImages(LinearLayout containerLayout, List<String> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        if (containerLayout.getChildCount() > 0) {
            containerLayout.removeAllViews();
        }

        for (int i = 0; i < images.size(); i++) {
            View view = View.inflate(mContext, R.layout.round_image, null);
            RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout);
            ImageView roundedImageView = (ImageView) view.findViewById(R.id.iv_icon);
            ImageLoader.getInstance().displayImage(images.get(i), roundedImageView);
            containerLayout.addView(layout);
        }
    }

    /**
     * 获取场景时间表达式
     */
    private String getTimeExpress(String exeTime, String exeTimeExpress) {

        String timeExpress = "";
        if (TextUtils.isEmpty(exeTime) || TextUtils.isEmpty(exeTimeExpress)) {
            return timeExpress;
        }

        try {
            String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            calendar.setTime(format.parse(exeTime));
            int week = calendar.get(Calendar.DAY_OF_WEEK);

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String time = formatter.format(calendar.getTime());

            if (!exeTimeExpress.endsWith("*")) {

                // 仅一次
                timeExpress = String.format("%s场景执行", time);
            } else {

                // 重复
                timeExpress = String.format("周%s %s场景执行", weeks[week - 1], time);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeExpress;
    }
}
