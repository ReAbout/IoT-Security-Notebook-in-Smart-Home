package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Record;
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
public class SceneLogsActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private SceneLogsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_logs);

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText("执行记录");

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new SceneLogsAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void setList(List<Record> recordList) {
        if (mAdapter != null) {
            mAdapter.setList(recordList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        getSceneLogList();
    }

    /**
     * 获取场景列表
     */
    private void getSceneLogList() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("page_size", 30);

        IFTTTManager.getUserLogs(map, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getSceneLogList onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        String result = new JSONObject(response).optString("result");
                        String records = new JSONObject(result).optString("records");

                        Gson gson = new Gson();
                        List<Record> recordList = gson.fromJson(records, new TypeToken<List<Record>>() {
                        }.getType());

                        setList(recordList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getSceneLogList onFailure response = " + response);
            }

            @Override
            public void onFinish() {
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getSceneLogList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JLog.e(TAG, "onItemClick parent = " + parent + "position = " + position);

        Intent intent = new Intent(this, SceneLogDetailActivity.class);
        intent.putExtra("record_id", mAdapter.getItem(position).getRecord_id());
        intent.putExtra("logic_name", mAdapter.getItem(position).getLogic_name());
        startActivity(intent);
    }

}
