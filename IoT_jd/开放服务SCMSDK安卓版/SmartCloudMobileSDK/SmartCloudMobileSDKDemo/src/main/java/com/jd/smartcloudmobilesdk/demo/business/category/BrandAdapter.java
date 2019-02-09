package com.jd.smartcloudmobilesdk.demo.business.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.BaseRecyclerAdapter;
import com.jd.smartcloudmobilesdk.demo.business.category.model.Brand;

/**
 * Created by yangchangan on 2018/6/1.
 */
public class BrandAdapter extends BaseRecyclerAdapter<Brand> {

    @Override
    protected RecyclerView.ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayout(parent.getContext(), R.layout.item_brand_list));
    }

    @Override
    protected void onBindVH(RecyclerView.ViewHolder holder, int position, Brand brand) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.nameView.setText(brand.getName());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;

        ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
