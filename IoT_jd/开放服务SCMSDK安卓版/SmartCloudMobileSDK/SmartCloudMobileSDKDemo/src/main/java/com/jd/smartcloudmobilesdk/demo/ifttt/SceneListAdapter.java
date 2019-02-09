package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Event;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Script;
import com.jd.smartcloudmobilesdk.demo.utils.DisplayUtils;
import com.jd.smartcloudmobilesdk.ifttt.IFTTTManager;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景列表适配器
 * Created by yangchangan on 2017/3/8.
 */
public class SceneListAdapter extends ArrayListAdapter<Script> {

    private LinearLayout.LayoutParams params;

    public SceneListAdapter(Context context) {
        super(context);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = DisplayUtils.dip2px(context, 10);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_scene_list, null);
            holder = new ViewHolder();

            holder.rootLayout = findViewById(convertView, R.id.layout_root);
            holder.nameView = findViewById(convertView, R.id.tv_name);
            holder.executeView = findViewById(convertView, R.id.tv_execute);
            holder.eventLayout = findViewById(convertView, R.id.layout_event);
            holder.actionLayout = findViewById(convertView, R.id.layout_action);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 设置背景
        if (position % 3 == 0) {
            holder.rootLayout.setBackgroundResource(R.mipmap.img_scene_blue);
        } else if (position % 3 == 1) {
            holder.rootLayout.setBackgroundResource(R.mipmap.img_scene_green);
        } else {
            holder.rootLayout.setBackgroundResource(R.mipmap.img_scene_purple);
        }

        final Script script = getItem(position);

        holder.eventLayout.removeAllViews();
        if (script.getEvents() != null && !script.getEvents().isEmpty()) {
            for (int i = 0; i < script.getEvents().size(); i++) {
                SceneStreamItem item = new SceneStreamItem(mContext, null);
                holder.eventLayout.addView(item, params);
            }
        }

        holder.actionLayout.removeAllViews();
        if (script.getActions() != null && !script.getActions().isEmpty()) {
            for (int i = 0; i < script.getActions().size(); i++) {
                SceneStreamItem item = new SceneStreamItem(mContext, null);
                holder.actionLayout.addView(item, params);
            }
        }

        if (script.getLogic() != null && !script.getLogic().isEmpty()) {
            holder.nameView.setText(script.getLogic().get(0).getNotation());
        }

        // 是否手动执行(判断scenarioId是否为空)
        final String scenarioId = getScenarioId(script);
        if (TextUtils.isEmpty(scenarioId)) {
            holder.executeView.setVisibility(View.GONE);
        } else {
            holder.executeView.setVisibility(View.VISIBLE);
            holder.executeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scenarioActivated(scenarioId);
                }
            });
        }

        return convertView;
    }

    private String getScenarioId(Script script) {
        if (script == null) {
            return "";
        }

        List<Event> eventList = script.getEvents();
        if (eventList == null || eventList.isEmpty()) {
            return "";
        }

        for (Event event : eventList) {
            if ("scenarioActivated".equals(event.getMember())) {
                if (event.getCondition() != null && !event.getCondition().isEmpty()) {
                    return event.getCondition().get(0).getValue();
                }
            }
        }

        return "";
    }

    /**
     * 手动执行场景
     */
    private void scenarioActivated(String scenario_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("scenario_id", scenario_id);
        IFTTTManager.scenarioActive(map, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                ((BaseActivity) mContext).showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "scenarioActivated onSuccess response = " + response);
                if (CommonUtil.isSuccessWithToast(mContext, response)) {
                    Toast.makeText(mContext, "手动执行接口调用成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "scenarioActivated onFailure response = " + response);
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ((BaseActivity) mContext).dismissLoadingDialog();
            }
        });
    }

    static class ViewHolder {
        View rootLayout;
        TextView nameView;
        TextView executeView;
        LinearLayout eventLayout;
        LinearLayout actionLayout;
    }
}
