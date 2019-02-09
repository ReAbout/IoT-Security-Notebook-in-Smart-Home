package com.jd.smartcloudmobilesdk.demo.scene;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneListModel;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.scene.SceneManager;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 场景列表：JoyLink2.0
 * Created by yangchangan on 2017/6/26.
 */
public class SceneListUI extends BaseActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private Button mAddButton;
    private ScrollView mScrollView;

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private SceneListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_scene_list);

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText("场景模式");
        TextView rightView = (TextView) findViewById(R.id.tv_right);
        rightView.setVisibility(View.VISIBLE);
        rightView.setText("执行记录");
        rightView.setOnClickListener(this);

        findViewById(R.id.layout_add).setOnClickListener(this);
        mAddButton = (Button) findViewById(R.id.btn_add);
        mAddButton.setOnClickListener(this);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new SceneListAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    private void refreshView(List<SceneListModel> sceneList) {
        if (sceneList != null && !sceneList.isEmpty()) {
            setList(sceneList);

            mScrollView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAddButton.setVisibility(View.VISIBLE);
        } else {
            mScrollView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mAddButton.setVisibility(View.GONE);
        }
    }

    private void setList(List<SceneListModel> sceneList) {
        if (mAdapter != null) {
            mAdapter.setList(sceneList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSceneList();
    }

    @Override
    public void onRefresh() {
        getSceneList();
    }

    /**
     * 获取场景列表
     */
    private void getSceneList() {
        SceneManager.getSceneList(null, new ResponseCallback() {

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getSceneList onSuccess response = " + response);
                if (CommonUtil.isSuccessWithToast(mContext, response)) {
                    try {
                        String result = new JSONObject(response).optString("result");
                        Type type = new TypeToken<List<SceneListModel>>() {
                        }.getType();
                        List<SceneListModel> detailList = new Gson().fromJson(result, type);

                        refreshView(detailList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getSceneList onFailure response = " + response);
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
            case R.id.tv_right:

                startActivity(SceneRecordUI.class);
                break;
            case R.id.btn_add:
            case R.id.layout_add:

                startActivity(CreateSceneUI.class);
                break;
        }
    }
}
