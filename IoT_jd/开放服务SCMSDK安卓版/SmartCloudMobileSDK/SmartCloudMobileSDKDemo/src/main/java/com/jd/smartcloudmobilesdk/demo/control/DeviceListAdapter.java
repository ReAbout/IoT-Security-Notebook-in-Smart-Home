package com.jd.smartcloudmobilesdk.demo.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.devicecontrol.model.DevDetailModel;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 设备列表适配器
 * Created by yangchangan on 2017/3/17.
 */
public class DeviceListAdapter extends ArrayListAdapter<DevDetailModel> {

    public DeviceListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_device_list, null);
            holder = new ViewHolder();
            holder.iconView = findViewById(convertView, R.id.iv_icon);
            holder.nameView = findViewById(convertView, R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DevDetailModel model = getItem(position);
        holder.nameView.setText(model.getDevice_name());
        ImageLoader.getInstance().displayImage(model.getC_img_url(), holder.iconView);

        return convertView;
    }

    static class ViewHolder {
        ImageView iconView;
        TextView nameView;
    }
}
