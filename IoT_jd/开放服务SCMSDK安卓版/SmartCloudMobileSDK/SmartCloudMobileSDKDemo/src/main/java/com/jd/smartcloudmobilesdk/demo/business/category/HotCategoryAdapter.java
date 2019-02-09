package com.jd.smartcloudmobilesdk.demo.business.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.BaseRecyclerAdapter;
import com.jd.smartcloudmobilesdk.demo.business.category.model.Category;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by yangchangan on 2018/6/1.
 */
public class HotCategoryAdapter extends BaseRecyclerAdapter<Category> {

    @Override
    protected RecyclerView.ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayout(parent.getContext(), R.layout.item_category_hot));
    }

    @Override
    protected void onBindVH(RecyclerView.ViewHolder holder, int position, Category category) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.nameView.setText(category.getCname());
            ImageLoader.getInstance().displayImage(category.getImg_url(), viewHolder.iconView);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconView;
        private TextView nameView;

        ViewHolder(View itemView) {
            super(itemView);
            iconView = (ImageView) itemView.findViewById(R.id.iv_icon);
            nameView = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

}
