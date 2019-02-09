package com.jd.smartcloudmobilesdk.demo.scene;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneDetail;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneStream;
import com.jd.smartcloudmobilesdk.demo.utils.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 场景详情适配器
 * Created by yangchangan on 2017/6/26.
 */
public class SceneDetailAdapter extends ArrayListAdapter<SceneDetail> {

    // 布局类型
    private final int VIEW_TYPE_COUNT = 3;
    private final int VIEW_TYPE_NONE = 0;
    private final int VIEW_TYPE_DEVICE = 1;
    private final int VIEW_TYPE_TIME = 2;

    private boolean mIsStart;

    public SceneDetailAdapter(Context context) {
        super(context);
    }

    public void setIsStart(boolean isStart) {
        mIsStart = isStart;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (getList() == null || getList().isEmpty()) {
            return VIEW_TYPE_NONE;
        }

        if ("device".equals(getList().get(position).getDevice_type())) {
            return VIEW_TYPE_DEVICE;
        } else if ("time".equals(getList().get(position).getDevice_type())) {
            return VIEW_TYPE_TIME;
        }

        return VIEW_TYPE_NONE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int nViewType = getItemViewType(position);

        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_scene_detail, null);

            if (nViewType == VIEW_TYPE_DEVICE) {
                holder = new DeviceViewHolder(convertView);
            } else if (nViewType == VIEW_TYPE_TIME) {
                holder = new TimeViewHolder(convertView);
            } else {
                holder = new ViewHolder();
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setViewHolder(position);

        return convertView;
    }

    private class ViewHolder {
        protected void setViewHolder(int position) {
        }
    }

    private class DeviceViewHolder extends ViewHolder {
        RoundedImageView iconBgView;
        ImageView iconView;
        TextView nameView;
        TextView descView;
        TextView statusView;

        DeviceViewHolder(View convertView) {
            ViewStub viewStub = findViewById(convertView, R.id.vs_detail_device);
            View view = viewStub.inflate();

            this.iconBgView = findViewById(view, R.id.iv_icon_bg);
            this.iconView = findViewById(view, R.id.iv_icon);
            this.nameView = findViewById(view, R.id.tv_name);
            this.descView = findViewById(view, R.id.tv_desc);
            this.statusView = findViewById(view, R.id.tv_status);
        }

        @Override
        protected void setViewHolder(int position) {
            SceneDetail detail = getItem(position);
            if (detail == null) {
                return;
            }

            ImageLoader.getInstance().displayImage(detail.getImages(), this.iconView);
            if ("4".equals(detail.getDevice_delete())) {
                this.nameView.setText("设备已删除");
            } else {
                this.nameView.setText(detail.getDevice_name());
            }
            this.descView.setText(getDesc(detail.getStreams()));

            if (mIsStart) {
                setStatusView(-1);
            } else {
                setStatusView(detail.getStatus());
            }
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

        private void setStatusView(int status) {
            switch (status) {
                case -1: // 初始化状态
                case 0:

                    setStatusView(0xffececec, "待执行", R.mipmap.icon_status_wait);
                    break;
                case 1: // 完成

                    setStatusView(0xfffe365b, "已完成", R.mipmap.icon_status_succeed);
                    break;
                case 2: // 待执行

                    setStatusView(0xffececec, "待执行", R.mipmap.icon_status_wait);
                    break;
                case 3: // 离线

                    setStatusView(0xfffe365b, "设备离线", R.mipmap.icon_status_error);
                    break;
                case 4: // 设备已删除

                    setStatusView(0xfffe365b, "已删除", R.mipmap.icon_status_error);
                    break;
                case 5: // 网络异常

                    setStatusView(0xfffe365b, "网络异常", R.mipmap.icon_status_error);
                    break;
                case 6: // 执行失败

                    setStatusView(0xfffe365b, "执行失败", R.mipmap.icon_status_error);
                    break;
                case 7: // 待执行

                    setStatusView(0xffececec, "待执行", R.mipmap.icon_status_wait);
                    break;
                case 8: // 执行失败

                    setStatusView(0xfffe365b, "网络异常", R.mipmap.icon_status_error);
                    break;
            }

        }

        private void setStatusView(int color, String text, int id) {
            this.iconBgView.setBorderColor(color);

            this.statusView.setText(text);
            Drawable drawable = ContextCompat.getDrawable(mContext, id);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            this.statusView.setCompoundDrawables(drawable, null, null, null);
            this.statusView.setCompoundDrawablePadding(10);
        }
    }

    private class TimeViewHolder extends ViewHolder {
        ImageView iconView;
        TextView timeView;

        TimeViewHolder(View convertView) {
            ViewStub viewStub = findViewById(convertView, R.id.vs_detail_time);
            View view = viewStub.inflate();

            this.iconView = findViewById(view, R.id.iv_icon);
            this.timeView = findViewById(view, R.id.tv_time);
        }

        @Override
        protected void setViewHolder(int position) {
            SceneDetail detail = getItem(position);
            if (detail == null) {
                return;
            }

            this.timeView.setText(getDelayTime(detail.getDelay()));
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
    }
}
