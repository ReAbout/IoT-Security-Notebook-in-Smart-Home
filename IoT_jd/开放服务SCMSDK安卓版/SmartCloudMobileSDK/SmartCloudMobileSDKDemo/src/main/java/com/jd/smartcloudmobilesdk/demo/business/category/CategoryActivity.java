package com.jd.smartcloudmobilesdk.demo.business.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.business.BusinessManager;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.BaseRecyclerAdapter;
import com.jd.smartcloudmobilesdk.demo.business.category.model.Category;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 三级类目 品类列表：根据业务自行实现下拉刷新和上拉加载
 * Created by yangchangan on 2018/5/31.
 */
public class CategoryActivity extends BaseActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, BaseRecyclerAdapter.OnItemClickListener<Category> {

    private SwipeRefreshLayout mSwipeLayout;

    private CategoryAdapter mCategoryAdapter;
    private HotCategoryAdapter mHotCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText("设备品类");

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        View header = View.inflate(this, R.layout.layout_category_header, null);
        RecyclerView hotRecyclerView = (RecyclerView) header.findViewById(R.id.recycler_view);
        hotRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mHotCategoryAdapter = new HotCategoryAdapter();
        mHotCategoryAdapter.setOnItemClickListener(this);
        hotRecyclerView.setAdapter(mHotCategoryAdapter);

        mCategoryAdapter = new CategoryAdapter();
        mCategoryAdapter.setHeaderView(header);
        mCategoryAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mCategoryAdapter);
    }

    protected void onResume() {
        super.onResume();
        getCategoryList();
    }

    @Override
    public void onRefresh() {
        getCategoryList();
    }

    private void getCategoryList() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("pageSize", 90);
        BusinessManager.getPopularCategoryList(params, new ResponseCallback() {

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
                        Gson gson = new Gson();
                        JSONObject resultObj = new JSONObject(response).optJSONObject("result");
                        String category_list = resultObj.optString("cate_list");
                        List<Category> categoryList = gson.fromJson(category_list, new TypeToken<List<Category>>() {
                        }.getType());
                        if (mCategoryAdapter != null) {
                            mCategoryAdapter.setList(categoryList);
                        }

                        String pop_cate_list = resultObj.optString("pop_cate_list");
                        List<Category> hotCategoryList = gson.fromJson(pop_cate_list, new TypeToken<List<Category>>() {
                        }.getType());
                        if (mHotCategoryAdapter != null) {
                            mHotCategoryAdapter.setList(hotCategoryList);
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

    @Override
    public void onItemClick(int position, Category category) {
        if (category != null) {
            Intent intent = new Intent(mContext, ProductBrandActivity.class);
            intent.putExtra("cid", category.getCid());
            intent.putExtra("cname", category.getCname());
            startActivity(intent);
        }
    }
}
