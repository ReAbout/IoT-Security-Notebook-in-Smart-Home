package com.jd.smartcloudmobilesdk.demo.business.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.business.BusinessManager;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.BaseRecyclerAdapter;
import com.jd.smartcloudmobilesdk.demo.business.category.model.Brand;
import com.jd.smartcloudmobilesdk.demo.business.category.model.Product;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品或品牌信息
 * Created by yangchangan on 2018/6/4.
 */
public class ProductBrandActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private View mHintView;

    private BrandAdapter mBrandAdapter;
    private RecyclerView mBrandRecyclerView;

    private ProductAdapter mProductAdapter;
    private RecyclerView mProductRecyclerView;

    private String cid;
    private String cname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brand);

        cid = getIntent().getStringExtra("cid");
        cname = getIntent().getStringExtra("cname");

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(cname);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mHintView = findViewById(R.id.tv_hint);

        mBrandRecyclerView = (RecyclerView) findViewById(R.id.rv_brand);
        mProductRecyclerView = (RecyclerView) findViewById(R.id.rv_product);

        mBrandAdapter = new BrandAdapter();
        mBrandAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                Brand brand = (Brand) data;
                Intent intent = new Intent(mContext, ProductListActivity.class);
                intent.putExtra("cid", brand.getCid());
                intent.putExtra("brand_id", brand.getBrand_id());
                intent.putExtra("name", brand.getName());
                startActivity(intent);
            }
        });
        mBrandRecyclerView.setAdapter(mBrandAdapter);
        mBrandRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mProductAdapter = new ProductAdapter();
        mProductRecyclerView.setAdapter(mProductAdapter);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductOrBrand();
    }

    @Override
    public void onRefresh() {
        getProductOrBrand();
    }

    private void getProductOrBrand() {
        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("page", 1);
        params.put("pageSize", 60);
        BusinessManager.getProductOrBrand(params, new ResponseCallback() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "onSuccess response = " + response);
                if (CommonUtil.isSuccessWithToast(mContext, response)) {
                    try {
                        JSONObject resultObj = new JSONObject(response).optJSONObject("result");
                        String type = resultObj.optString("type");
                        if (TextUtils.isEmpty(type)) {
                            mHintView.setVisibility(View.VISIBLE);
                            mBrandRecyclerView.setVisibility(View.GONE);
                            mProductRecyclerView.setVisibility(View.GONE);
                        } else if ("brand".equals(type)) {
                            String brand = resultObj.optString("brand");
                            List<Brand> brandList = new Gson().fromJson(brand, new TypeToken<List<Brand>>() {
                            }.getType());
                            mBrandAdapter.setList(brandList);

                            mHintView.setVisibility(View.GONE);
                            mBrandRecyclerView.setVisibility(View.VISIBLE);
                            mProductRecyclerView.setVisibility(View.GONE);
                        } else if ("product".equals(type)) {
                            String product = resultObj.optString("product");
                            List<Product> productList = new Gson().fromJson(product, new TypeToken<List<Product>>() {
                            }.getType());
                            mProductAdapter.setList(productList);

                            mHintView.setVisibility(View.GONE);
                            mBrandRecyclerView.setVisibility(View.GONE);
                            mProductRecyclerView.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "onFailure response = " + response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:

                this.finish();
                break;
        }
    }
}
