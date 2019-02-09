package com.jd.smartcloudmobilesdk.demo.scene;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneDetail;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneStream;
import com.jd.smartcloudmobilesdk.demo.utils.PromptDialog;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.scene.SceneManager;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景编辑适配器
 * Created by yangchangan on 2017/6/26.
 */
public class SceneEditAdapter extends ArrayListAdapter<SceneDetail> {

    private DeleteListener mDeleteListener;

    public SceneEditAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_scene_detail_edit, null);
            holder = new ViewHolder();

            holder.iconView = findViewById(convertView, R.id.iv_icon);
            holder.nameView = findViewById(convertView, R.id.tv_name);
            holder.descView = findViewById(convertView, R.id.tv_desc);
            holder.deleteView = findViewById(convertView, R.id.iv_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SceneDetail detail = getItem(position);
        if ("device".equals(detail.getDevice_type())) {
            ImageLoader.getInstance().displayImage(detail.getImages(), holder.iconView);

            if ("4".equals(detail.getDevice_delete())) {
                holder.nameView.setText("设备已删除");
            } else {
                holder.nameView.setText(detail.getDevice_name());
            }

            holder.descView.setVisibility(View.VISIBLE);
            holder.descView.setText(getDesc(detail.getStreams()));
        } else if ("time".equals(detail.getDevice_type())) {
            holder.iconView.setImageResource(R.mipmap.icon_hourglass_round);
            holder.nameView.setText(getDelayTime(detail.getDelay()));
            holder.descView.setVisibility(View.GONE);
        }

        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        ImageView iconView;
        TextView nameView;
        TextView descView;
        ImageView deleteView;
    }

    private String getDesc(List<SceneStream> streamList) {
        StringBuilder builder = new StringBuilder();
        if (streamList == null || streamList.isEmpty()) {
            return builder.toString();
        }

        for (SceneStream stream : streamList) {
            builder.append("/").append(stream.getStream_name_zh());
            builder.append(":").append(stream.getCurrent_value_zh());
        }

        return builder.substring(1);
    }

    private String getDelayTime(int delay) {
        String delayTime;
        if (delay <= 0) {
            delayTime = "间隔0秒";
        } else if (delay < 60) {
            delayTime = String.format("间隔%d秒", delay);
        } else {
            int minute = delay / 60;
            if (minute < 60) {
                delayTime = String.format("间隔%d分钟", minute);
            } else {
                int hour = minute / 60;
                if (hour > 24) {
                    delayTime = "间隔大于24小时";
                } else {
                    delayTime = String.format("间隔%d小时%d分钟", hour, minute % 60);
                }
            }
        }
        return delayTime;
    }


    /**
     * 删除场景任务提示框
     */
    private void showDeleteDialog(final int position) {
        final PromptDialog dialog = new PromptDialog(mContext, R.style.jdPromptDialog);
        dialog.msg = "确定要删除这个任务？";
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                // 删除场景
                deleteScene(getItem(position).getId(), position);
            }
        });
        dialog.show();
    }

    /**
     * 删除场景
     */
    private void deleteScene(String id, final int position) {
        if (TextUtils.isEmpty(id)) {
            remove(getItem(position));
            doDeleteListener(position);
            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        List<Object> list = new ArrayList<>();
        list.add(new JSONObject(map));

        SceneManager.deleteScene(new JSONArray(list).toString(), new ResponseCallback() {

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "deleteScene onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {

                    remove(getItem(position));
                    doDeleteListener(position);
                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "deleteScene onFailure response = " + response);
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void setDeleteListener(DeleteListener listener) {
        mDeleteListener = listener;
    }

    private void doDeleteListener(int position) {
        if (mDeleteListener != null) {
            mDeleteListener.delete(position);
        }
    }

    public interface DeleteListener {
        void delete(int position);
    }
}
