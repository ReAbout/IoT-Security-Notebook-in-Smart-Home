package com.jd.smartcloudmobilesdk.demo.gateway;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.gateway.GatewaySubDevice;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 网关子设备列表适配器
 * Created by yangchangan on 2017/5/26.
 */
public class GatewayListAdapter extends ArrayListAdapter<GatewaySubDevice> {

    public GatewayListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gateway_list, null);
            holder = new ViewHolder();
            holder.iconView = findViewById(convertView, R.id.iv_icon);
            holder.nameView = findViewById(convertView, R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GatewaySubDevice subDevice = getItem(position);
        holder.nameView.setText(subDevice.getProduct_name());
        ImageLoader.getInstance().displayImage(subDevice.getImg_url(), holder.iconView);

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconView;
        TextView nameView;
    }
}
