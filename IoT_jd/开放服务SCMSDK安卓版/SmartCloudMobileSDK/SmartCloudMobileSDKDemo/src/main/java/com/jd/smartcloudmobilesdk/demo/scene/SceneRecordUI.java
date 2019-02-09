package com.jd.smartcloudmobilesdk.demo.scene;

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
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneRecord;
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
 * 执行记录：JoyLink2.0
 * Created by yangchangan on 2017/6/26.
 */
public class SceneRecordUI extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private SceneRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_scene_record);

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
        mAdapter = new SceneRecordAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void setList(List<SceneRecord> recordList) {
        if (mAdapter != null) {
            mAdapter.setList(recordList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSceneRecordList();
    }

    @Override
    public void onRefresh() {
        getSceneRecordList();
    }

    /**
     * 获取场景执行记录
     */
    private void getSceneRecordList() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("pageSize", 50);

        SceneManager.getSceneRecord(map, new ResponseCallback() {

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getSceneRecordList onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        String result = new JSONObject(response).optString("result");
                        Type type = new TypeToken<List<SceneRecord>>() {
                        }.getType();
                        List<SceneRecord> recordList = new Gson().fromJson(result, type);

                        setList(recordList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getSceneRecordList onFailure response = " + response);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JLog.e(TAG, "onItemClick parent = " + parent + "position = " + position);

        Intent intent = new Intent(this, SceneRecordDetailUI.class);
        intent.putExtra("name", mAdapter.getItem(position).getName());
        intent.putExtra("recordId", mAdapter.getItem(position).getRecordId());
        startActivity(intent);
    }
}
