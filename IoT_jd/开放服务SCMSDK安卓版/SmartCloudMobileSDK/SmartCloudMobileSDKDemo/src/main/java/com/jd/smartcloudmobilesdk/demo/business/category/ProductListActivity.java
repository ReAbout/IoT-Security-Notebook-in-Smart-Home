package com.jd.smartcloudmobilesdk.demo.business.category;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.business.BusinessManager;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.business.category.model.Product;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangchangan on 2018/6/4.
 */
public class ProductListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private View mHintView;

    private ProductAdapter mProductAdapter;
    private RecyclerView mProductRecyclerView;

    private String cid;
    private String brand_id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        cid = getIntent().getStringExtra("cid");
        brand_id = getIntent().getStringExtra("brand_id");
        name = getIntent().getStringExtra("name");

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(name);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        mHintView = findViewById(R.id.tv_hint);
        mProductRecyclerView = (RecyclerView) findViewById(R.id.rv_product);

        mProductAdapter = new ProductAdapter();
        mProductRecyclerView.setAdapter(mProductAdapter);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductList();
    }

    @Override
    public void onRefresh() {
        getProductList();
    }

    private void getProductList() {
        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("brand_id", brand_id);
        params.put("page", 1);
        params.put("pageSize", 60);
        BusinessManager.getProductInfos(params, new ResponseCallback() {

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
                        String result = new JSONObject(response).optString("result");
                        List<Product> productList = new Gson().fromJson(result, new TypeToken<List<Product>>() {
                        }.getType());

                        if (productList == null || productList.isEmpty()) {
                            mHintView.setVisibility(View.VISIBLE);
                            mProductRecyclerView.setVisibility(View.GONE);
                        } else {
                            mProductAdapter.setList(productList);

                            mHintView.setVisibility(View.GONE);
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
