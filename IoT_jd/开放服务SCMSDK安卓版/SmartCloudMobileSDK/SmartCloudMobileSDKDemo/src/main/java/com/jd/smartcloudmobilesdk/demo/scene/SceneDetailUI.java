package com.jd.smartcloudmobilesdk.demo.scene;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneDetail;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneItemModel;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneStream;
import com.jd.smartcloudmobilesdk.demo.scene.model.Stream;
import com.jd.smartcloudmobilesdk.demo.utils.PromptDialog;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.scene.SceneManager;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 场景详情页：JoyLink2.0
 * Created by yangchangan on 2017/6/27.
 */
public class SceneDetailUI extends BaseActivity implements View.OnClickListener {
    private final int SCENE_ADD_TASK = 2;

    private TextView mTitleView;
    private TextView mRightView;

    private Button mActionButton;

    private ScrollView mEditScrollView;
    private TextView mEditNameView;

    private ListView mEditListView;
    private SceneEditAdapter mEditAdapter;

    private ListView mDetailListView;
    private SceneDetailAdapter mDetailAdapter;

    private boolean mIsStart = true; // 初始化标记

    private boolean mIsEditing;
    private boolean mIsExecuting;
    private Timer mTimer;

    private String mSceneId;
    private String mSceneName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_scene_detail);
        getIntentExtras();
        initView();

        getSceneDetail();
    }

    private void getIntentExtras() {
        if (getIntent() != null) {
            mSceneId = getIntent().getStringExtra("scene_id");
            mSceneName = getIntent().getStringExtra("scene_name");
        }
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTitleView.setText(mSceneName);

        mRightView = (TextView) findViewById(R.id.tv_right);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setText("编辑");
        mRightView.setOnClickListener(this);

        mActionButton = (Button) findViewById(R.id.btn_action);
        mActionButton.setOnClickListener(this);

        findViewById(R.id.layout_edit_name).setOnClickListener(this);
        findViewById(R.id.iv_edit_add).setOnClickListener(this);
        mEditScrollView = (ScrollView) findViewById(R.id.sv_edit);
        mEditNameView = (TextView) findViewById(R.id.tv_edit_name);
        mEditNameView.setText(mSceneName);

        mEditListView = (ListView) findViewById(R.id.lv_edit);
        mEditAdapter = new SceneEditAdapter(this);
        mEditAdapter.setDeleteListener(new SceneEditAdapter.DeleteListener() {
            @Override
            public void delete(int position) {
                if (mDetailAdapter != null) {
                    mDetailAdapter.setList(mEditAdapter.getList());
                }
            }
        });
        mEditListView.setAdapter(mEditAdapter);

        mDetailListView = (ListView) findViewById(R.id.lv_detail);
        mDetailAdapter = new SceneDetailAdapter(this);
        mDetailListView.setAdapter(mDetailAdapter);
    }

    private void setIsEditing(boolean isEditing) {
        mIsEditing = isEditing;
        refreshView();
    }

    private void setIsExecuting(boolean isExecuting) {
        mIsExecuting = isExecuting;
        refreshView();
    }

    private void refreshView() {
        refreshView(null);
    }

    private void refreshView(List<SceneDetail> detailList) {
        if (mIsEditing) {
            mEditScrollView.setVisibility(View.VISIBLE);
            mDetailListView.setVisibility(View.GONE);

            mTitleView.setText("编辑场景");
            mRightView.setText("保存");
            mActionButton.setText("删除场景");
        } else {
            mEditScrollView.setVisibility(View.GONE);
            mDetailListView.setVisibility(View.VISIBLE);

            mEditNameView.setText(mSceneName);
            mTitleView.setText(mSceneName);
            mRightView.setText("编辑");

            if (mIsExecuting) {
                mRightView.setVisibility(View.GONE);
                mActionButton.setText("停止执行");
            } else {
                mRightView.setVisibility(View.VISIBLE);
                mActionButton.setText("执行场景");
            }
        }

        if (mDetailAdapter != null && detailList != null) {
            mDetailAdapter.setIsStart(mIsStart);
            mDetailAdapter.setList(detailList);
        }

        if (mEditAdapter != null && detailList != null) {
            mEditAdapter.setList(detailList);
        }
    }

    /**
     * 获取场景详情
     */
    private void getSceneDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mSceneId);

        SceneManager.getSceneDetail(map, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getSceneDetail onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        if (mIsStart && mIsExecuting) {
                            mIsStart = false;
                        }

                        String result = new JSONObject(response).optString("result");
                        String items = new JSONObject(result).optString("items");
                        int status = new JSONObject(result).optInt("status", -1);
                        setSceneStatus(status);

                        Type type = new TypeToken<List<SceneDetail>>() {
                        }.getType();
                        List<SceneDetail> detailList = new Gson().fromJson(items, type);

                        refreshView(detailList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getSceneDetail onFailure response = " + response);
            }
        });
    }

    private void setSceneStatus(int status) {
        switch (status) {
            case 2:  // 执行中

                setIsExecuting(true);
                break;
            default:

                cancelTimer();
                setIsExecuting(false);
                break;
        }
    }

    /**
     * 启动定时器
     */
    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSceneDetail();
                    }
                });
            }
        }, 0, 2000);
    }

    /**
     * 取消定时器
     */
    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 取消定时器
        cancelTimer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCENE_ADD_TASK:
                if (data != null && data.getSerializableExtra("data") != null) {
                    List<SceneItemModel> items = (List<SceneItemModel>) data.getSerializableExtra("data");

                    List<SceneDetail> detailList = mEditAdapter.getList();
                    if (detailList == null) {
                        detailList = new ArrayList<>();
                    }
                    detailList.addAll(getDetailList(items));
                    refreshView(detailList);
                }
                break;
        }
    }

    private List<SceneDetail> getDetailList(List<SceneItemModel> items) {
        List<SceneDetail> detailList = new ArrayList<>();
        if (items == null || items.isEmpty()) {
            return detailList;
        }

        for (SceneItemModel model : items) {
            SceneDetail detail = new SceneDetail();
            detail.setDevice_type(model.getDevice_type());
            detail.setFeed_id(model.getFeed_id());
            detail.setDevice_name(model.getName());
            detail.setImages(model.getImage());
            detail.setDevice_delete(detail.getDevice_delete());
            if (!TextUtils.isEmpty(model.getDelay())) {
                detail.setDelay(Integer.parseInt(model.getDelay()));
            }

            List<Stream> streams = model.getStreams();
            if (streams != null) {
                List<SceneStream> streamList = new ArrayList<>();
                for (Stream stream : streams) {
                    SceneStream sceneStream = new SceneStream();
                    sceneStream.setStream_id(stream.getStream_id());
                    sceneStream.setCurrent_value(stream.getCurrent_value());
                    sceneStream.setStream_name(stream.getStream_name());
                    sceneStream.setStream_name_zh(stream.getStream_name());
                    String symbol = stream.getUnits() == null ? "" : stream.getUnits();
                    sceneStream.setCurrent_value_zh(stream.getMaster_flag() + symbol);

                    streamList.add(sceneStream);
                }
                detail.setStreams(streamList);
            }
            detailList.add(detail);
        }

        return detailList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                if (mIsEditing) {
                    setIsEditing(false);
                } else {
                    this.finish();
                }
                break;
            case R.id.tv_right:

                if (mIsEditing) {
                    updateScene();
                } else {
                    setIsEditing(true);
                }
                break;
            case R.id.btn_action:

                if (mIsEditing) {

                    showDeleteDialog();
                } else if (mIsExecuting) {

                    // 停止场景
                    cancelTimer();
                    stopScene();
                } else {

                    // 执行场景
                    executeScene();
                }
                break;
            case R.id.iv_edit_add:

                Intent intent = new Intent(this, SceneAddTaskUI.class);
                startActivityForResult(intent, SCENE_ADD_TASK);
                break;
            case R.id.layout_edit_name:

                SceneRenameDialog dialog = new SceneRenameDialog(this);
                dialog.setName(mSceneName);
                dialog.setRenameListener(new SceneRenameDialog.RenameListener() {
                    @Override
                    public void onRename(String name) {
                        mEditNameView.setText(name);
                    }
                });
                dialog.show();
                break;
        }
    }

    /**
     * 删除场景提示框
     */
    private void showDeleteDialog() {
        final PromptDialog dialog = new PromptDialog(this, R.style.jdPromptDialog);
        dialog.msg = "确认删除整个场景？";
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                // 删除场景
                deleteScene();
            }
        });
        dialog.show();
    }

    /**
     * 执行场景
     */
    private void executeScene() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mSceneId);

        SceneManager.executeScene(map, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "executeScene onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    setIsExecuting(true);
                    startTimer();
                } else {
                    try {
                        String errorCode = "";
                        String error = new JSONObject(response).optString("error");
                        if (!TextUtils.isEmpty(error) && !"null".equals(error)) {
                            errorCode = new JSONObject(error).optString("errorCode");
                        }

                        if ("400".equals(errorCode)) {
                            startTimer();
                        } else {
                            setIsExecuting(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        setIsExecuting(false);
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "executeScene onFailure response = " + response);

                setIsExecuting(false);
                toastShort("执行场景失败，请检查网络！");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 停止执行场景
     */
    private void stopScene() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mSceneId);

        SceneManager.stopScene(map, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "stopScene onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        String result = new JSONObject(response).optString("result");
                        String status = new JSONObject(result).optString("sceneStatus");

                        if ("2".equals(status)) {
                            setIsExecuting(true);
                        } else {
                            setIsExecuting(false);
                        }
                        getSceneDetail();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "stopScene onFailure response = " + response);
                toastShort("网络错误");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 删除场景
     */
    private void deleteScene() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mSceneId);

        List<Object> list = new ArrayList<>();
        list.add(new JSONObject(map));

        SceneManager.deleteScene(new JSONArray(list).toString(), new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "deleteScene onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    toastShort("删除成功");
                    SceneDetailUI.this.finish();
                } else {
                    toastShort("删除失败");
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "deleteScene onFailure response = " + response);
                toastShort("网络错误");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 更新场景
     */
    private void updateScene() {
        final String sceneName = mEditNameView.getText().toString().trim();
        if (TextUtils.isEmpty(sceneName)) {
            toastShort("请输入场景名称");
            return;
        }

        List<SceneDetail> detailList = mEditAdapter.getList();
        if (detailList == null || detailList.isEmpty()) {
            toastShort("请添加任务");
            return;
        }

        if ("time".equals(detailList.get(detailList.size() - 1).getDevice_type())) {
            toastShort("时间不能设置成最后一个任务");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", mSceneId);
        map.put("name", sceneName);
        map.put("items", getSceneItems(detailList));

        SceneManager.updateScene(map, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "updateScene onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    mSceneName = sceneName;
                    setIsEditing(false);
                    getSceneDetail();
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "updateScene onFailure response = " + response);
                toastShort("网络错误");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }

    private JSONArray getSceneItems(List<SceneDetail> detailList) {
        if (detailList == null || detailList.isEmpty()) {
            return new JSONArray();
        }

        List<Object> itemList = new ArrayList<>();
        for (SceneDetail detail : detailList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("device_type", detail.getDevice_type());
            if (!TextUtils.isEmpty(detail.getId())) {
                itemMap.put("id", detail.getId());
            }

            if ("time".equals(detail.getDevice_type())) {
                itemMap.put("delay", detail.getDelay());
            } else if ("device".equals(detail.getDevice_type())) {
                itemMap.put("feed_id", detail.getFeed_id());

                List<Object> streamList = new ArrayList<>();
                for (SceneStream stream : detail.getStreams()) {
                    Map<String, Object> streamMap = new HashMap<>();
                    streamMap.put("stream_name", stream.getStream_name());
                    streamMap.put("stream_id", stream.getStream_id());
                    streamMap.put("current_value", stream.getCurrent_value());
                    streamList.add(new JSONObject(streamMap));
                }
                itemMap.put("streams", new JSONArray(streamList));

            }
            itemList.add(new JSONObject(itemMap));
        }

        return new JSONArray(itemList);
    }
}
