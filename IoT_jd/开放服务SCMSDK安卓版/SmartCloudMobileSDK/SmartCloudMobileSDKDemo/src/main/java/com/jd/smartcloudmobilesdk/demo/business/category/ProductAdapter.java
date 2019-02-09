package com.jd.smartcloudmobilesdk.demo.business.category;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.BaseRecyclerAdapter;
import com.jd.smartcloudmobilesdk.demo.business.category.model.Product;
import com.jd.smartcloudmobilesdk.demo.utils.DisplayUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by yangchangan on 2018/6/1.
 */
public class ProductAdapter extends BaseRecyclerAdapter<Product> implements View.OnClickListener {

    @Override
    protected RecyclerView.ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayout(parent.getContext(), R.layout.item_product_list));
    }

    @Override
    protected void onBindVH(RecyclerView.ViewHolder holder, int position, Product product) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.nameView.setText(product.getName());
            ImageLoader.getInstance().displayImage(product.getImg_url(), viewHolder.iconView);

            String[] models = product.getProduct_models();
            if (models == null || models.length == 0) {
                viewHolder.modelLayout.setVisibility(View.GONE);
            } else {
                viewHolder.modelLayout.setVisibility(View.VISIBLE);
                viewHolder.modelView.setText(models[0]);

                if (models.length > 1) {
                    viewHolder.moreView.setVisibility(View.VISIBLE);
                    viewHolder.modelLayout.setTag(product);
                    viewHolder.modelLayout.setOnClickListener(this);
                } else {
                    viewHolder.moreView.setVisibility(View.GONE);
                }
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconView;
        private TextView nameView;

        private View modelLayout;
        private View moreView;
        private TextView modelView;


        ViewHolder(View itemView) {
            super(itemView);
            iconView = (ImageView) itemView.findViewById(R.id.iv_icon);
            nameView = (TextView) itemView.findViewById(R.id.tv_name);

            modelLayout = itemView.findViewById(R.id.layout_model);
            moreView = itemView.findViewById(R.id.iv_more);
            modelView = (TextView) itemView.findViewById(R.id.tv_model);
        }
    }

    @Override
    public void onClick(View v) {
        View moreView = v.findViewById(R.id.iv_more);
        if (moreView.getVisibility() == View.VISIBLE) {
            Product product = (Product) v.getTag();
            showMoreDialog(v.getContext(), product);
        }
    }

    private void showMoreDialog(Context context, Product product) {
        View view = View.inflate(context, R.layout.dialog_product_model, null);
        View closeView = view.findViewById(R.id.iv_close);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = (Dialog) v.getTag();
                if (dialog != null) {
                    dialog.cancel();
                }
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        textView.setText(product.getName());
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(new ProductModelAdapter(context, product.getProduct_models(), listView));

        float h = DisplayUtils.getDisplayHeight();
        Dialog dialog = new Dialog(context, R.style.dialogTheme1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.addContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.windowstyle);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = DisplayUtils.getDisplayWidth();
            params.height = (int) (h * 0.6f);
            window.setAttributes(params);
        }
        listView.setTag(dialog);
        closeView.setTag(dialog);
        dialog.show();

    }

    private class ProductModelAdapter extends BaseAdapter implements View.OnClickListener {
        private Context mContext;
        private String[] mProductModels;
        private ListView listView;

        public ProductModelAdapter(Context context, String[] models, ListView listView) {
            mContext = context;
            mProductModels = models;
            this.listView = listView;
        }

        public void notifyDataSetChanged(String[] models) {
            mProductModels = models;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mProductModels == null ? 0 : mProductModels.length;
        }

        @Override
        public String getItem(int position) {
            return mProductModels[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.dialog_product_item, null);
                holder.allView = convertView.findViewById(R.id.tv_all);
                holder.modelView = (TextView) convertView.findViewById(R.id.tv_model);
                holder.modelLayout = convertView.findViewById(R.id.layout_model);
                holder.modelLayout.setOnClickListener(this);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            if (position == 0) {
                holder.allView.setVisibility(View.VISIBLE);
            } else {
                holder.allView.setVisibility(View.GONE);
            }
            holder.modelView.setText(getItem(position));
            return convertView;
        }

        @Override
        public void onClick(View v) {
            Dialog dialog = (Dialog) listView.getTag();
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        private class ViewHolder {
            View allView;
            View modelLayout;
            TextView modelView;
        }
    }
}
