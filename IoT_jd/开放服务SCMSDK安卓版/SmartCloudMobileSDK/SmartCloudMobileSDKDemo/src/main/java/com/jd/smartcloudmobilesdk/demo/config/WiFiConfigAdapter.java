package com.jd.smartcloudmobilesdk.demo.config;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.smartcloudmobilesdk.activate.BindResult;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 绑定列表适配器
 * Created by yangchangan on 2017/5/18.
 */
public class WiFiConfigAdapter extends ArrayListAdapter<BindResult> {
    private ProductModel mProductModel;

    public WiFiConfigAdapter(Context context) {
        super(context);
    }

    public void setProductModel(ProductModel productModel) {
        this.mProductModel = productModel;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_wifi_config, null);
            holder = new ViewHolder();
            holder.iconView = (ImageView) view.findViewById(R.id.iv_icon);
            holder.nameView = (TextView) view.findViewById(R.id.tv_name);
            holder.macView = (TextView) view.findViewById(R.id.tv_mac);
            holder.actionView = (TextView) view.findViewById(R.id.tv_action);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String imgUrl = mProductModel != null ? mProductModel.getImg_url() : "";
        ImageLoader.getInstance().displayImage(imgUrl, holder.iconView);

        final BindResult bindResult = getItem(position);
        if (!TextUtils.isEmpty(bindResult.getDeviceName())) {
            holder.nameView.setText(bindResult.getDeviceName());
        } else if (mProductModel != null) {
            holder.nameView.setText(mProductModel.getName());
        } else {
            holder.nameView.setText(bindResult.getDeviceMac());
        }

        if (bindResult.getBindStatus() == 1) {
            holder.actionView.setText("使用");
            holder.macView.setText(bindResult.getDeviceMac());
        } else if (bindResult.getBindStatus() == 2) {
            holder.actionView.setText("绑定出错");
            holder.macView.setText("此设备已被其他账户添加");
        } else {
            holder.actionView.setText("绑定出错");
            holder.macView.setText("设备绑定出现异常");
        }

        holder.actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindResult.getBindStatus() == 1) {

                    // 跳转到设备详情页
                    Toast.makeText(mContext, "打开设备详情页", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private static class ViewHolder {
        ImageView iconView;
        TextView nameView;
        TextView macView;
        TextView actionView;
    }
}



