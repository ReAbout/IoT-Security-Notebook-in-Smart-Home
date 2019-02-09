package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Action;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Event;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Logic;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Script;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Trigger;
import com.jd.smartcloudmobilesdk.ifttt.IFTTTManager;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jd.smartcloudmobilesdk.demo.R.id.tv_action;
import static com.jd.smartcloudmobilesdk.demo.R.id.tv_event;
import static com.jd.smartcloudmobilesdk.demo.R.id.tv_name;


/**
 * 创建场景
 * Created by yangchangan on 2017/3/9.
 */
public class CreateSceneActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static final int CODE_EVENT = 111;
    public static final int CODE_ACTION = 222;

    private TextView mNameView;

    private TextView mStatusView;
    private ImageView mExecuteView;

    private TextView mEventView;
    private ListView mEventListView;

    private TextView mActionView;
    private ListView mActionListView;

    private SceneEventAdapter mEventAdapter;
    private SceneActionAdapter mActionAdapter;

    private Script mScript;
    private List<Event> mEventList;
    private List<Action> mActionList;

    private int mDrift = 5;
    private int mConditionType = SceneDriftDialog.CONDITION_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scene);

        initView();
        getIntentExtras();

        setEventList();
        setActionList();
        updateView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mScript == null || TextUtils.isEmpty(mScript.getId())) {
            mExecuteView.setVisibility(View.GONE);
            mStatusView.setVisibility(View.GONE);
        } else {
            mExecuteView.setVisibility(View.VISIBLE);
            mStatusView.setVisibility(View.VISIBLE);

            // checkStatus(mScript.getId(), mScript.getLogic().get(0).getId());
            if (mScript.getLogic().get(0).getEn() == 1) {
                mExecuteView.setSelected(true);
            } else {
                mExecuteView.setSelected(false);
            }
        }
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        findViewById(R.id.iv_modify).setOnClickListener(this);

        findViewById(R.id.iv_event_add).setOnClickListener(this);
        findViewById(R.id.iv_event_setting).setOnClickListener(this);
        findViewById(R.id.iv_action_add).setOnClickListener(this);
        findViewById(R.id.iv_action_setting).setOnClickListener(this);

        mNameView = (TextView) findViewById(tv_name);

        mStatusView = (TextView) findViewById(R.id.tv_status);
        mExecuteView = (ImageView) findViewById(R.id.iv_execute);
        mExecuteView.setOnClickListener(this);

        mEventView = (TextView) findViewById(tv_event);
        mEventView.setOnClickListener(this);
        mActionView = (TextView) findViewById(tv_action);
        mActionView.setOnClickListener(this);

        mEventListView = (ListView) findViewById(R.id.lv_event);
        mActionListView = (ListView) findViewById(R.id.lv_action);

        mEventAdapter = new SceneEventAdapter(this);
        mEventListView.setAdapter(mEventAdapter);
        mEventListView.setOnItemClickListener(this);
        mEventListView.setOnItemLongClickListener(this);

        mActionAdapter = new SceneActionAdapter(this);
        mActionListView.setAdapter(mActionAdapter);
        mActionListView.setOnItemClickListener(this);
        mActionListView.setOnItemLongClickListener(this);
    }

    private void setEventList() {
        if (mEventAdapter != null) {
            mEventAdapter.setList(mEventList);
        }
        updateView();
    }

    private void setActionList() {
        if (mActionAdapter != null) {
            mActionAdapter.setList(mActionList);
        }
        updateView();
    }

    private void setStatusView(int script_stat, int logic_stat) {
        StringBuffer buffer = new StringBuffer();
        if (script_stat == 0) {
            buffer.append("script：不存在   ");
        } else if (script_stat == 1) {
            buffer.append("script：存在    ");
        }

        if (logic_stat == 0) {
            buffer.append("logic：不存在");
        } else if (logic_stat == 1) {
            buffer.append("logic：未启动");
        } else if (logic_stat == 2) {
            buffer.append("logic：已启动");
        }

        mStatusView.setText(buffer.toString());
    }

    private void updateView() {
        if (mEventAdapter == null || mEventAdapter.isEmpty()) {
            mEventView.setVisibility(View.VISIBLE);
            mEventListView.setVisibility(View.GONE);
        } else {
            mEventView.setVisibility(View.GONE);
            mEventListView.setVisibility(View.VISIBLE);
        }

        if (mActionAdapter == null || mActionAdapter.isEmpty()) {
            mActionView.setVisibility(View.VISIBLE);
            mActionListView.setVisibility(View.GONE);
        } else {
            mActionView.setVisibility(View.GONE);
            mActionListView.setVisibility(View.VISIBLE);
        }
    }

    private void getIntentExtras() {
        mEventList = new ArrayList<>();
        mActionList = new ArrayList<>();

        if (getIntent().getExtras() == null) {
            return;
        }

        mScript = (Script) getIntent().getExtras().getSerializable("script");
        if (mScript == null) {
            return;
        }

        if (mScript.getLogic() != null && !mScript.getLogic().isEmpty()) {
            mNameView.setText(mScript.getLogic().get(0).getNotation());
        }

        if (mScript.getEvents() != null && !mScript.getEvents().isEmpty()) {
            mEventList.addAll(mScript.getEvents());
        }

        if (mScript.getActions() != null && !mScript.getActions().isEmpty()) {

            ArrayMap<String, Action> actionMap = new ArrayMap<>();
            List<Action> actionList = mScript.getActions();
            for (Action action : actionList) {
                actionMap.put(action.getId(), action);
            }

            List<Logic> logicList = mScript.getLogic();
            for (Logic logic : logicList) {
                if (logic.getDelay() > 0) {
                    Action action = new Action();
                    action.setDelayValue(logic.getDelay());
                    mActionList.add(action);
                }

                String[] actions = logic.getActions().split("&&");
                if (actions.length > 1) {
                    for (String action : actions) {
                        if (actionMap.get(action.trim()) != null) {
                            mActionList.add(actionMap.get(action.trim()));
                        }
                    }
                } else {
                    if (actionMap.get(logic.getActions()) != null) {
                        mActionList.add(actionMap.get(logic.getActions()));
                    }
                }
            }
        }
    }

    /**
     * 检查脚本的状态
     */
    private void checkStatus(String script_id, String logic_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("script_id", script_id);
        map.put("logic_id", logic_id);
        IFTTTManager.checkStatus(map, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "checkStatus onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        JSONObject result = new JSONObject(response).optJSONObject("result");
                        int script_stat = result.getInt("script_stat");
                        int logic_stat = result.getInt("logic_stat");
                        setStatusView(script_stat, logic_stat);

                        // 开启开关
                        if (logic_stat == 2) {
                            mExecuteView.setSelected(true);
                        } else {
                            mExecuteView.setSelected(false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "stopScript onFailure response = " + response);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_EVENT:
                if (data != null && data.getExtras() != null) {

                    SceneUtils.getInstance().initEventId(mEventList);

                    /**
                     * 新的值
                     */
                    boolean isHand = data.getExtras().getBoolean("isHand");
                    String mTime = data.getExtras().getString("mTime");
                    String feedId = data.getExtras().getString("feedId");

                    /**
                     * 原来的值
                     */
                    boolean modifyHand = data.getExtras().getBoolean("modifyHand");
                    String modifyTime = data.getExtras().getString("modifyTime");

                    /**
                     * 区分是新增还是修改
                     */
                    int position = data.getExtras().getInt("position");
                    Trigger trigger = (Trigger) data.getExtras().getSerializable("trigger");

                    /**
                     * 返回的是手动
                     */
                    if (isHand) {
                        if (position >= 0 && modifyHand && mEventList.size() > position) {
                            mEventList.remove(position);
                            mEventList.add(position, SceneUtils.getInstance().getManualEvent());
                        } else {
                            mEventList.add(SceneUtils.getInstance().getManualEvent());
                        }
                    }

                    /**
                     * 返回的是定时
                     */
                    if (!TextUtils.isEmpty(mTime)) {
                        if (position >= 0 && !TextUtils.isEmpty(modifyTime) && mEventList.size() > position) {
                            mEventList.remove(position);
                            mEventList.add(position, SceneUtils.getInstance().getTimerEvent(mTime));
                        } else {
                            mEventList.add(SceneUtils.getInstance().getTimerEvent(mTime));
                        }
                    }

                    /**
                     * 返回的是设备
                     */
                    if (trigger != null && !TextUtils.isEmpty(trigger.getFeed_id())
                            && !TextUtils.isEmpty(trigger.getStream_id())
                            && !TextUtils.isEmpty(trigger.getValue_type())
                            && !TextUtils.isEmpty(trigger.getValue())
                            && !TextUtils.isEmpty(trigger.getComparison_opt())) {
                        String type;
                        if ("int".equals(trigger.getValue_type())) {
                            type = "i";
                        } else if ("float".equals(trigger.getValue_type())) {
                            type = "f";
                        } else {
                            type = "s";
                        }
                        Event event = SceneUtils.getInstance().getDeviceEvent(trigger.getFeed_id(),
                                trigger.getStream_id(), type, trigger.getValue(), trigger.getComparison_opt());
                        if (position >= 0 && !TextUtils.isEmpty(feedId) && mEventList.size() > position) {
                            mEventList.remove(position);
                            mEventList.add(position, event);
                        } else {
                            mEventList.add(event);
                        }
                    }

                    setEventList();
                }
                break;
            case CODE_ACTION:
                if (data != null && data.getExtras() != null) {
//                    SceneUtils.getInstance().initActionId(mActionList);

                    Trigger trigger = (Trigger) data.getExtras().getSerializable("trigger");
                    int position = data.getExtras().getInt("position");

                    if (trigger != null && !TextUtils.isEmpty(trigger.getFeed_id())
                            && !TextUtils.isEmpty(trigger.getStream_id())
                            && !TextUtils.isEmpty(trigger.getValue_type())
                            && !TextUtils.isEmpty(trigger.getValue())
                            && !TextUtils.isEmpty(trigger.getComparison_opt())) {

                        String type;
                        if ("int".equals(trigger.getValue_type())) {
                            type = "i";
                        } else if ("float".equals(trigger.getValue_type())) {
                            type = "f";
                        } else {
                            type = "s";
                        }
                        Action deviceModel = SceneUtils.getInstance().getAction(trigger.getFeed_id(),
                                trigger.getStream_id(), type, trigger.getValue());
                        if (position >= 0 && mActionList.size() > position) {
                            mActionList.remove(position);
                            mActionList.add(position, deviceModel);
                        } else {
                            mActionList.add(deviceModel);
                        }
                    }

                    setActionList();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:

                finish();
                break;
            case R.id.tv_save:

                handleSaveClick();
                break;
            case R.id.iv_execute:

                handleExecuteClick();
                break;
            case R.id.iv_modify:

                SceneModifyNameDialog dialog = new SceneModifyNameDialog(CreateSceneActivity.this);
                dialog.setName(mNameView.getText().toString());
                dialog.setChangeNameListener(new SceneModifyNameDialog.ChangeNameListener() {
                    @Override
                    public void changeName(String name) {
                        mNameView.setText(name);
                    }
                });
                dialog.show();
                break;
            case tv_event:
            case R.id.iv_event_add:

                Intent eventIntent = new Intent(CreateSceneActivity.this, SceneEventsListActivity.class);
                startActivityForResult(eventIntent, CODE_EVENT);
                break;
            case R.id.iv_event_setting:

                SceneDriftDialog driftDialog = new SceneDriftDialog(CreateSceneActivity.this);
                driftDialog.setDrift(mDrift, mConditionType);
                driftDialog.setSureClickListener(new SceneDriftDialog.SureClickListener() {
                    @Override
                    public void sureClick(int drift, int conditionType) {
                        mDrift = drift;
                        mConditionType = conditionType;
                    }
                });
                driftDialog.show();
                break;
            case tv_action:
            case R.id.iv_action_add:

                Intent actionIntent = new Intent(CreateSceneActivity.this, SceneActionsListActivity.class);
                startActivityForResult(actionIntent, CODE_ACTION);
                break;
            case R.id.iv_action_setting:

                SceneDelayDialog delayDialog = new SceneDelayDialog(CreateSceneActivity.this);
                delayDialog.setDelayClickListener(new SceneDelayDialog.DelayClickListener() {
                    @Override
                    public void delay(int delay) {
                        if (delay > 0) {
                            Action action = new Action();
                            action.setDelayValue(delay * 1000);
                            mActionList.add(action);

                            if (mActionList.size() > 0) {
                                mActionListView.setVisibility(View.VISIBLE);
                                mActionView.setVisibility(View.GONE);
                            }
                            mActionAdapter.setList(mActionList);
                        }
                    }
                });
                delayDialog.show();
                break;
        }
    }

    /**
     * 保存场景（创建、更新）
     */
    private void handleSaveClick() {
        if (mEventList.isEmpty() || mActionList.isEmpty()) {
            toastShort("请完善信息");
            return;
        }

        boolean flag = false;
        for (int i = 0; i < mActionList.size(); i++) {
            if (mActionList.get(i).getDelayValue() == 0) {
                flag = true;
            }
        }
        if (!flag) {
            toastShort("请完善信息");
            return;
        }

        String force;
        Script script2;
        if (mScript != null && !TextUtils.isEmpty(mScript.getId())) {
            int logicEn = mExecuteView.isSelected() ? 1 : 0;
            script2 = SceneUtils.getInstance().getScript(mDrift, mConditionType, logicEn,
                    mNameView.getText().toString(), mEventList, mActionList, mScript.getId());
            force = "true";
        } else {
            script2 = SceneUtils.getInstance().getScript(mDrift, mConditionType, 1,
                    mNameView.getText().toString(), mEventList, mActionList, null);
            force = "false";
        }

        String script = new Gson().toJson(script2);
        JLog.e(TAG, "createScript script = " + script);

        // 创建场景（脚本）
        createScript(script, force);
    }

    private void handleExecuteClick() {
        if (mScript == null) {
            return;
        }

        if (mExecuteView.isSelected()) {
            stopScript(mScript.getId());
        } else {
            startScript(mScript.getId());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JLog.e(TAG, "onItemClick parent = " + parent + "position = " + position);

        if (parent == mEventListView) {
            Intent intent = new Intent(CreateSceneActivity.this, SceneEventsListActivity.class);
            intent.putExtra("position", position);
            Event event = mEventAdapter.getItem(position);

            if ("local".equals(event.getType())) {
                if (event.getCondition() != null && !event.getCondition().isEmpty()) {
                    if ("time".equals(event.getCondition().get(0).getName())) {
                        intent.putExtra("modifyTime", event.getCondition().get(0).getValue());
                    }
                }

            } else if ("signal".equals(event.getType())) {
                intent.putExtra("modifyHand", true);
            } else if ("stream_id".equals(event.getType())) {
                intent.putExtra("feedId", event.getGUID());
            }

            startActivityForResult(intent, CODE_EVENT);
        } else if (parent == mActionListView) {
            Intent intent = new Intent(CreateSceneActivity.this, SceneActionsListActivity.class);
            intent.putExtra("position", position);
            startActivityForResult(intent, CODE_ACTION);
        }
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        JLog.e(TAG, "onItemLongClick parent = " + parent + "position = " + position);

        final List<String> list = new ArrayList<>();
        list.add("删除");
        final SceneEditDialog dialog = new SceneEditDialog(this);
        dialog.show();
        dialog.setList(list);
        dialog.setOnItemClickListener(new SceneEditDialog.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                handleItemClick(parent, position);
            }
        });

        return true;
    }

    private void handleItemClick(AdapterView<?> parent, final int position) {
        if (parent == mEventListView) {

            // 本地删除
            if (mEventList != null) {
                mEventList.remove(position);
                setEventList();
            }
        } else if (parent == mActionListView) {

            // 本地删除
            if (mActionList != null) {
                mActionList.remove(position);
                setActionList();
            }
        }
    }

    /**
     * 创建/修改脚本
     *
     * @param script 脚本
     * @param force  true/false 新建或修改脚本
     */
    private void createScript(String script, String force) {
        Map<String, Object> map = new HashMap<>();
        map.put("script", script);
        map.put("force", force);
        IFTTTManager.createScript(map, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "createScript onSuccess response = " + response);
                if (CommonUtil.isSuccessWithToast(CreateSceneActivity.this, response)) {
                    toastShort("场景创建成功");
                    CreateSceneActivity.this.finish();
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "createScript onSuccess response = " + response);
                toastShort("场景创建失败");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }


    /**
     * 启动场景（脚本）
     *
     * @param script_id 脚本id
     */
    private void startScript(String script_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("script_id", script_id);
        IFTTTManager.startScript(map, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "startScript onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    toastShort("启动场景脚本");
                    mExecuteView.setSelected(true);
                    mScript.getLogic().get(0).setEn(1);
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "startScript onFailure response = " + response);
                toastShort("网络异常");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 停止场景（脚本）
     *
     * @param script_id 脚本id
     */
    private void stopScript(String script_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("script_id", script_id);
        IFTTTManager.stopScript(map, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "stopScript onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    toastShort("停止场景脚本");
                    mScript.getLogic().get(0).setEn(0);
                    mExecuteView.setSelected(false);
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "stopScript onFailure response = " + response);
                toastShort("网络异常");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }
}
