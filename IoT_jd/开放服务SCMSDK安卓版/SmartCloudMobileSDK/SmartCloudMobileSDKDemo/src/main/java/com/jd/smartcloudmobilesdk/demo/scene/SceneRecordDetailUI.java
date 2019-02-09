package com.jd.smartcloudmobilesdk.demo.scene;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneRecordDetail;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.scene.SceneManager;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行记录详情：JoyLink2.0
 * Created by yangchangan on 2017/6/26.
 */
public class SceneRecordDetailUI extends BaseActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private SceneRecordDetailAdapter mAdapter;

    private String mRecordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_scene_record_detail);

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText("记录详情");

        if (getIntent() != null) {
            titleView.setText(getIntent().getStringExtra("name"));
            mRecordId = getIntent().getStringExtra("recordId");
        }

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new SceneRecordDetailAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    private void setList(List<SceneRecordDetail> detailList) {
        if (mAdapter != null) {
            mAdapter.setList(detailList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSceneRecordDetail();
    }

    @Override
    public void onRefresh() {
        getSceneRecordDetail();
    }

    /**
     * 获取场景执行记录详情
     */
    private void getSceneRecordDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("recordId", mRecordId);

        SceneManager.getSceneRecordDetail(map, new ResponseCallback() {

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getSceneRecordDetail onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        String result = new JSONObject(response).optString("result");
                        String items = new JSONObject(result).getString("items");

                        Type type = new TypeToken<List<SceneRecordDetail>>() {
                        }.getType();
                        List<SceneRecordDetail> detailList = new Gson().fromJson(items, type);

                        setList(detailList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getSceneRecordDetail onFailure response = " + response);
                toastShort("网络错误");
            }

            @Override
            public void onFinish() {
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
        }
    }
}
