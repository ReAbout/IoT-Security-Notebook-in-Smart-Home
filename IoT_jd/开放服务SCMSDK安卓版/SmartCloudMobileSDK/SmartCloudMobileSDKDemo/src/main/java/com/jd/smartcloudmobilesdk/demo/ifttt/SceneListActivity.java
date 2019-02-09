package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Script;
import com.jd.smartcloudmobilesdk.demo.utils.PromptDialog;
import com.jd.smartcloudmobilesdk.ifttt.IFTTTManager;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景列表界面
 * Created by yangchangan on 2017/3/8.
 */
public class SceneListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private View mNothingLayout;

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private SceneListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_list);

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText("智能场景");
        ImageView rightView = (ImageView) findViewById(R.id.iv_right);
        rightView.setVisibility(View.VISIBLE);
        rightView.setOnClickListener(this);

        mNothingLayout = findViewById(R.id.layout_nothing);
        findViewById(R.id.tv_setting).setOnClickListener(this);
        findViewById(R.id.tv_logs).setOnClickListener(this);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new SceneListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
    }

    private void updateScentView() {
        if (mAdapter == null || mAdapter.isEmpty()) {
            mNothingLayout.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mSwipeLayout.setVisibility(View.GONE);
        } else {
            mNothingLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mSwipeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setList(List<Script> scriptList) {
        if (mAdapter != null) {
            mAdapter.setList(scriptList);
        }
        updateScentView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getSceneList();
    }

    /**
     * 获取场景列表
     */
    private void getSceneList() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("page_size", 50);

        IFTTTManager.getIFTTTList(map, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getSceneList onSuccess response = " + response);
                if (CommonUtil.isSuccessWithToast(mContext, response)) {
                    try {
                        String result = new JSONObject(response).optString("result");
                        String scripts = new JSONObject(result).optString("scripts");

                        Gson gson = new Gson();
                        List<Script> scriptList = gson.fromJson(scripts, new TypeToken<List<Script>>() {
                        }.getType());

                        setList(scriptList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getSceneList onFailure response = " + response);
            }

            @Override
            public void onFinish() {
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getSceneList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.iv_right:
            case R.id.tv_setting:

                startActivity(CreateSceneActivity.class);
                break;
            case R.id.tv_logs:

                startActivity(SceneLogsActivity.class);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JLog.e(TAG, "onItemClick parent = " + parent + "position = " + position);

        Intent intent = new Intent(this, CreateSceneActivity.class);
        intent.putExtra("script", mAdapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        JLog.e(TAG, "onItemLongClick parent = " + parent + "position = " + position);

        final PromptDialog dialog = new PromptDialog(this, R.style.jdPromptDialog);
        dialog.msg = "确定删除场景？";
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                removeScript(position);
            }
        });
        dialog.show();

        return true;
    }

    /**
     * 删除场景
     */
    private void removeScript(int position) {
        final Script script = mAdapter.getItem(position);
        String scriptId = script.getId();

        final Map<String, Object> map = new HashMap<>();
        map.put("script_id", scriptId);
        IFTTTManager.removeScript(map, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "removeScript onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    if (mAdapter != null) {
                        mAdapter.remove(script);
                    }
                    updateScentView();
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "removeScript onFailure response = " + response);
            }
        });
    }
}
