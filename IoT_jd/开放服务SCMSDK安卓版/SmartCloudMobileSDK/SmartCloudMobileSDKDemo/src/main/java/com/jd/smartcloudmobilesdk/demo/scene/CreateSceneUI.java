package com.jd.smartcloudmobilesdk.demo.scene;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneDetail;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneItemModel;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneStream;
import com.jd.smartcloudmobilesdk.demo.scene.model.Stream;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.scene.SceneManager;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建场景：JoyLink2.0
 * Created by yangchangan on 2017/6/29.
 */
public class CreateSceneUI extends BaseActivity implements View.OnClickListener {
    private final int CREATE_SCENE = 1;

    private TextView mNameView;

    private ListView mListView;
    private CreateSceneAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_create_scene);

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText("新建场景");
        TextView rightView = (TextView) findViewById(R.id.tv_right);
        rightView.setVisibility(View.VISIBLE);
        rightView.setText("保存");
        rightView.setOnClickListener(this);

        findViewById(R.id.layout_edit_name).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);
        mNameView = (TextView) findViewById(R.id.tv_edit_name);

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new CreateSceneAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATE_SCENE:

                if (data != null && data.getSerializableExtra("data") != null) {
                    List<SceneItemModel> items = (List<SceneItemModel>) data.getSerializableExtra("data");

                    List<SceneDetail> detailList = mAdapter.getList();
                    if (detailList == null) {
                        detailList = new ArrayList<>();
                    }
                    detailList.addAll(getDetailList(items));
                    if (mAdapter != null) {
                        mAdapter.setList(detailList);
                    }
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

                this.finish();
                break;
            case R.id.tv_right:

                createScene();
                break;

            case R.id.iv_add:

                Intent intent = new Intent(this, SceneAddTaskUI.class);
                startActivityForResult(intent, CREATE_SCENE);
                break;
            case R.id.layout_edit_name:

                SceneRenameDialog dialog = new SceneRenameDialog(this);
                dialog.setName(mNameView.getText().toString());
                dialog.setRenameListener(new SceneRenameDialog.RenameListener() {
                    @Override
                    public void onRename(String name) {
                        mNameView.setText(name);
                    }
                });
                dialog.show();
                break;
        }
    }

    /**
     * 创建场景
     */
    private void createScene() {
        final String sceneName = mNameView.getText().toString().trim();
        if (TextUtils.isEmpty(sceneName)) {
            toastShort("请输入场景名称");
            return;
        }

        List<SceneDetail> detailList = mAdapter.getList();
        if (detailList == null || detailList.isEmpty()) {
            toastShort("请添加任务");
            return;
        }

        if ("time".equals(detailList.get(detailList.size() - 1).getDevice_type())) {
            toastShort("时间不能设置成最后一个任务");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("name", sceneName);
        map.put("items", getSceneItems(detailList));

        SceneManager.createScene(map, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "createScene onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    toastShort("创建成功");
                    CreateSceneUI.this.finish();
                } else {
                    toastShort("创建失败");
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "createScene onFailure response = " + response);
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
