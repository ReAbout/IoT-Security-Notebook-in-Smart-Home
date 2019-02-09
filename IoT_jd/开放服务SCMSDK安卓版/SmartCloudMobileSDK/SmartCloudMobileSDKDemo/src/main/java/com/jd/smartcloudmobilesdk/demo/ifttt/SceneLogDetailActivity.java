package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.ActionLog;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.RecordDetail;
import com.jd.smartcloudmobilesdk.ifttt.IFTTTManager;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景执行记录界面
 * Created by yangchangan on 2017/3/21.
 */
public class SceneLogDetailActivity extends BaseActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private TextView mTitleView;

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private SceneLogDetailAdapter mAdapter;

    private String mRecordId;
    private String mLogicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_log_detail);

        initView();
        getIntentExtra();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        mTitleView = (TextView) findViewById(R.id.tv_title);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new SceneLogDetailAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    private void getIntentExtra() {
        if (getIntent() != null) {
            mRecordId = getIntent().getStringExtra("record_id");
            mLogicName = getIntent().getStringExtra("logic_name");
        }
    }

    private void setList(List<ActionLog> actionLogList) {
        if (mAdapter != null) {
            mAdapter.setList(actionLogList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mTitleView.setText(mLogicName);
        getSceneLogDetail(mRecordId);
    }

    /**
     * 获取场景列表
     */
    private void getSceneLogDetail(final String record_id) {
        if (TextUtils.isEmpty(record_id)) {
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("record_id", record_id);

        IFTTTManager.getLog(map, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getSceneLogDetail onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        String result = new JSONObject(response).optString("result");

                        Gson gson = new Gson();
                        RecordDetail recordDetail = gson.fromJson(result, new TypeToken<RecordDetail>() {
                        }.getType());

                        if (recordDetail != null) {
                            setList(recordDetail.getActions());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getSceneLogDetail onFailure response = " + response);
            }

            @Override
            public void onFinish() {
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getSceneLogDetail(mRecordId);
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
