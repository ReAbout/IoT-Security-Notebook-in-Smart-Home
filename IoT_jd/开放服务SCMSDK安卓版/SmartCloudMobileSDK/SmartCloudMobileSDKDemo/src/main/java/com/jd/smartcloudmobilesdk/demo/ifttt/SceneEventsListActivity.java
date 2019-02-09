package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.DeviceConnect;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.DeviceDes;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.DeviceStream;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Trigger;
import com.jd.smartcloudmobilesdk.demo.utils.DisplayUtils;
import com.jd.smartcloudmobilesdk.ifttt.IFTTTManager;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by pengmin1 on 2017/1/5.
 */
public class SceneEventsListActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ExpandableListView eblv_device;
    private SceneEventTopView topView;
    private ArrayList<DeviceConnect> dclist;
    private EventsAdapter adapter;

    private DeviceOptsAdapter optsAdapter;
    private Trigger trigger;
    private String choicePostions;

    /**
     * 上一个页面传过来是否是修改值
     */
    private boolean modifyHand;
    private String modifyTime;
    private String feedId;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_device_list);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("设置启动条件");
        topView = new SceneEventTopView(this, null);
        eblv_device = (ExpandableListView) findViewById(R.id.eblv_device);
        eblv_device.addHeaderView(topView);
        dclist = new ArrayList<>();
        adapter = new EventsAdapter();
        eblv_device.setAdapter(adapter);

        eblv_device.setGroupIndicator(null);
        eblv_device.setCacheColorHint(Color.TRANSPARENT);
        trigger = new Trigger();
        requestDevice();
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            modifyHand = getIntent().getExtras().getBoolean("modifyHand");
            modifyTime = getIntent().getExtras().getString("modifyTime");
            feedId = getIntent().getExtras().getString("feedId");
            position = getIntent().getExtras().getInt("position", -1);
            topView.isHand = modifyHand;
            topView.mTime = modifyTime;
            topView.showHand();
            topView.showTime();
        }
    }

    private void requestDevice() {
        Map<String, Object> map = new HashMap<>();
        map.put("part", "trigger");
        IFTTTManager.getIFTTTDeviceList(map, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                if (CommonUtil.isSuccess(response)) {
                    try {
                        String result = new JSONObject(response).getString("result");
                        String list = new JSONObject(result).getString("list");
                        Type type = new TypeToken<List<DeviceConnect>>() {
                        }.getType();
                        Gson gson = new Gson();
                        dclist = gson.fromJson(list, type);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        DeviceDes deviceDes = (DeviceDes) optsAdapter.getItem(arg2);
        if (deviceDes == null || TextUtils.isEmpty(choicePostions)) {
            return;
        }

        if (deviceDes.isStatus()) {
            deviceDes.setStatus(false);
            optsAdapter.setB(false);
            for (int j = 0; j < optsAdapter.getSectionCount(); j++) {
                for (int i = 0; i < optsAdapter.getCountForSection(j); i++) {
                    DeviceDes dd = (DeviceDes) optsAdapter.getItem(j, i);
                    dd.setStatus(false);
                    optsAdapter.getItemView(j, i, arg1, arg0).findViewById(R.id.item_choose).setBackgroundResource(0);
                }
            }
        } else {
            for (int j = 0; j < optsAdapter.getSectionCount(); j++) {
                for (int i = 0; i < optsAdapter.getCountForSection(j); i++) {
                    DeviceDes dd = (DeviceDes) optsAdapter.getItem(j, i);
                    dd.setStatus(false);
                    optsAdapter.getItemView(j, i, arg1, arg0).findViewById(R.id.item_choose).setBackgroundResource(0);
                }
            }
            TextView choose = (TextView) arg1.findViewById(R.id.item_choose);
            choose.setBackgroundResource(R.mipmap.ico_ok_h);
            deviceDes.setStatus(true);
            optsAdapter.setB(true);
        }

        if (deviceDes.getKey() != null) {
            if (trigger.getKeyValue() != null) {
                trigger.setKeyValue(null);
            }
            if (trigger.getValue() != null) {
                trigger.setValue(null);
            }
            trigger.setKeyValue(deviceDes.getValue());
            trigger.setValue(deviceDes.getKey());
            trigger.setComparison_opt("==");
            trigger.setMode("advance");
            trigger.setChoose_value_description(deviceDes.getValue());
            adapter.notifyDataSetChanged();
        } else if (deviceDes.getId() != null) {
            trigger.setMode("common");
            trigger.setDescription(deviceDes.getDescription());
            trigger.setId(deviceDes.getId());
            trigger.setChoose_value_description(deviceDes.getDescription());
            adapter.notifyDataSetChanged();
        }
//        trigger.setP_img_url(currentImage);
//        activity.clearData();
//        activity.saveData(trigger, response);
        optsAdapter.setProgressValue(null);
        optsAdapter.setCustomer_choose(false);
        optsAdapter.notifyDataSetChanged();
    }

    private void deviceStreamClick(int groupPosition, int childPosition, TextView echo, TextView item_choose) {
        DeviceStream data = dclist.get(groupPosition).getStream().get(childPosition);
        trigger.setValue_type(data.getValue_type());
        trigger.setStream_id(data.getStream_id());
        trigger.setStream_name(data.getStream_name());
        trigger.setSymbol(data.getSymbol());
        if (groupPosition < dclist.size()) {
            DeviceConnect eeviceConnect = dclist.get(groupPosition);
            trigger.setFeed_id(eeviceConnect.getFeed_id());
            trigger.setProduct_id(eeviceConnect.getProduct_id());
            trigger.setType(eeviceConnect.getType());
            trigger.setP_img_url(eeviceConnect.getP_img_url());
        }

        if (optsAdapter != null) {
            optsAdapter.getEcho().setText("");
        }

        if ((groupPosition + "," + childPosition).equals(choicePostions)) {
            if (trigger.getValue() != null || trigger.getId() != null) {
                if (optsAdapter != null) {
                    optsAdapter.setB(true);
                }
            } else {
                trigger.setChoose_value_description(null);
                item_choose.setBackgroundResource(R.mipmap.ico_goto_g_h);
            }
        } else {
            if (optsAdapter != null) {
                optsAdapter.setB(false);
                optsAdapter.setCustomer_choose(false);
                optsAdapter.setProgressValue(null);
            }
            trigger.setValue(null);
            trigger.setChoose_value_description(null);
            choicePostions = groupPosition + "," + childPosition;
        }

        adapter.notifyDataSetChanged();
        showDialog(data.getStream_name(), data, echo, item_choose);
    }

    /**
     * @param name
     * @param data
     * @param echo        回显所选择的数值  , final int position
     * @param item_choose
     */
    private void showDialog(String name, DeviceStream data, TextView echo, TextView item_choose) {
        final Dialog dialog = new Dialog(this, R.style.dialogTheme1);
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_dc_opts, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (optsAdapter == null) {
            optsAdapter = new DeviceOptsAdapter(this);
        }
        PinnedHeaderListView plistView = (PinnedHeaderListView) view.findViewById(R.id.dc_opts_plist);
        plistView.setOnItemClickListener(this);
        plistView.setAdapter(optsAdapter);
        Button sure = (Button) view.findViewById(R.id.dc_opts_sure);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.windowstyle);  //添加动画
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = DisplayUtils.getDisplayWidth(); // 宽度
        //			        lp.alpha = 0.7f; // 透明度
        lp.height = DisplayUtils.getDisplayHeight() * 3 / 4;
        window.setAttributes(lp);

        List<DeviceStream> dsList = new ArrayList<DeviceStream>();

        if (data.getDeviceDes() != null && data.getDeviceDes().size() > 0) {
            for (int i = 0; i < data.getDeviceDes().size(); i++) {
                DeviceDes deviceDes = data.getDeviceDes().get(i);
                if (trigger.getMode() != null && trigger.getMode().equals("common") && trigger.getId() != null && deviceDes.getId() != null && trigger.getId().equals(deviceDes.getId())) {
                    deviceDes.setStatus(true);
                    optsAdapter.setB(true);
                } else {
                    deviceDes.setStatus(false);
                    optsAdapter.setB(false);
                }
            }
            DeviceStream stream = new DeviceStream();
            stream.setStream_name(name);
            stream.setDeviceDes(data.getDeviceDes());
            stream.setStatus(data.isStatus());
            stream.setSymbol(data.getSymbol());
            dsList.add(stream);
        }

        DeviceStream stream1 = new DeviceStream();
        stream1.setStream_name(name);
        Map<Integer, Object> mapc = new HashMap<Integer, Object>();
        if (data.getValue_des() != null) {
            for (int i = 0; i < data.getValue_des().size(); i++) {
                Map<String, String> map1 = ConvertJson.parseMap(data.getValue_des().get(i).toString());
                for (String k : map1.keySet()) {
                    mapc.put(i, k + "," + map1.get(k));
                }
            }
            List<DeviceDes> list = new ArrayList<DeviceDes>();
            for (Integer key : mapc.keySet()) {
                DeviceDes dd = new DeviceDes();
                String str[] = mapc.get(key).toString().split(",");
                dd.setKey(str[0]);
                dd.setValue(str[1]);
                list.add(dd);
            }
            for (int i = 0; i < list.size(); i++) {
                DeviceDes dd = list.get(i);
                if (trigger.getMode() != null && trigger.getMode().equals("advance") && (trigger.getValue() != null && dd.getKey() != null && trigger.getValue().equals(dd.getKey()))) {
                    dd.setStatus(true);
                    optsAdapter.setB(true);
                } else {
                    dd.setStatus(false);
                    optsAdapter.setB(false);
                }
            }
            stream1.setDeviceDes(list);
            stream1.setValue_des(data.getValue_des());
        } else {
            if (optsAdapter.getProgressValue() == null) {
                if (isInteger(trigger.getEcho_value())) {
                    if (trigger.getEcho_value() != null) {
                        optsAdapter.setProgressValue(trigger.getEcho_value());
                        optsAdapter.setCustomer_choose(true);
                    }
                    if (trigger.getComparison_opt() != null) {
                        optsAdapter.setChoose(trigger.getComparison_opt());
                    }
                }
            }
            stream1.setValue_des(data.getValue_des());
            stream1.setComparison_opt(data.getComparison_opt());
            stream1.setMax_value(data.getMax_value());
            stream1.setMin_value(data.getMin_value());
            stream1.setSymbol(data.getSymbol());
            stream1.setValue_type(data.getValue_type());
        }


        dsList.add(stream1);
        optsAdapter.setList(dsList);
        optsAdapter.setEcho(echo);
        optsAdapter.setItem_choose(item_choose);
        optsAdapter.setSymbol(data.getSymbol());
        optsAdapter.notifyDataSetChanged();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (optsAdapter.isCustomer_choose() && optsAdapter.getProgressValue() != null) {
                    trigger.setValue(optsAdapter.getProgressValue());
                    trigger.setMode("advance");
                    if (trigger.getKeyValue() != null) {
                        trigger.setKeyValue(null);
                    }
                    optsAdapter.getEcho().setVisibility(View.VISIBLE);
                    if (optsAdapter.getChoose() == null) {
                        optsAdapter.setChoose(">");
                    }
                    if (optsAdapter.getChoose().equals(">")) {
                        trigger.setChoose_value_description("大于" + optsAdapter.getProgressValue() + optsAdapter.getSymbol());
                    } else if (optsAdapter.getChoose().equals("==")) {
                        trigger.setChoose_value_description("等于" + optsAdapter.getProgressValue() + optsAdapter.getSymbol());
                    } else if (optsAdapter.getChoose().equals("<")) {
                        trigger.setChoose_value_description("小于" + optsAdapter.getProgressValue() + optsAdapter.getSymbol());
                    }
                    trigger.setComparison_opt(optsAdapter.getChoose());
                    adapter.notifyDataSetChanged();
                } else if (optsAdapter.isB()) {
                } else {
                    optsAdapter.getEcho().setVisibility(View.GONE);
                    optsAdapter.getList().get(0).setStatus(false);
                    choicePostions = null;
                    optsAdapter.setCustomer_choose(false);
                    if (trigger.getValue() != null) {//假如点击其他选项未选择参数，清理所有已选择的参数
                        trigger.setValue(null);
                        trigger.setComparison_opt(null);
                        trigger.setMode(null);
                    } else if (trigger.getId() != null) {
                        trigger.setMode(null);
                        trigger.setId(null);
                    }
                    optsAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stu
                if (optsAdapter.isCustomer_choose() && optsAdapter.getProgressValue() != null) {

                    trigger.setValue(optsAdapter.getProgressValue());
                    trigger.setMode("advance");
                    optsAdapter.getEcho().setVisibility(View.VISIBLE);
                    if (optsAdapter.getChoose() == null) {
                        optsAdapter.setChoose(">");
                    }

                    if (trigger.getKeyValue() != null) {
                        trigger.setKeyValue(null);
                    }
                    if (optsAdapter.getChoose().equals(">")) {
                        trigger.setChoose_value_description("大于" + optsAdapter.getProgressValue() + optsAdapter.getSymbol());
                    } else if (optsAdapter.getChoose().equals("==")) {
                        trigger.setChoose_value_description("等于" + optsAdapter.getProgressValue() + optsAdapter.getSymbol());
                    } else if (optsAdapter.getChoose().equals("<")) {
                        trigger.setChoose_value_description("小于" + optsAdapter.getProgressValue() + optsAdapter.getSymbol());
                    }
                    trigger.setComparison_opt(optsAdapter.getChoose());
                    adapter.notifyDataSetChanged();
                } else if (optsAdapter.isB()) {
                } else {
                    optsAdapter.getEcho().setVisibility(View.GONE);
                    optsAdapter.getList().get(0).setStatus(false);
                    choicePostions = null;
                    optsAdapter.setCustomer_choose(false);
                    if (trigger.getValue() != null) {//假如点击其他选项未选择参数，清理所有已选择的参数
                        trigger.setValue(null);
                        trigger.setComparison_opt(null);
                        trigger.setMode(null);
                    } else if (trigger.getId() != null) {
                        trigger.setMode(null);
                        trigger.setId(null);
                    }
                    optsAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean isNext() {
        // TODO Auto-generated method stub
        if (trigger.getType() == null || trigger.getType().equals("")) {
            return false;
        }
        if (trigger.getFeed_id() == null || trigger.getFeed_id().equals("")) {
            return false;
        }
        if (trigger.getProduct_id() == null || trigger.getProduct_id().equals("")) {
            return false;
        }

        if (trigger.getMode() == null || trigger.getMode().equals("")) {
            return false;
        }
        if (trigger.getMode().equals("advance")) {
            if (trigger.getStream_id() == null || trigger.getStream_id().equals("")) {
                return false;
            }
            if (trigger.getComparison_opt() == null || trigger.getComparison_opt().equals("")) {
                return false;
            }
            if (trigger.getValue() == null || trigger.getValue().equals("")) {
                return false;
            }
        } else if (trigger.getMode().equals("common")) {
            if (trigger.getId() == null || trigger.getId().equals("")) {
                return false;
            }
        }

        if (dclist != null && dclist.isEmpty()) {
            Toast.makeText(this, "请选择触发设备", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isHand", topView.isHand);
        intent.putExtra("mTime", topView.mTime);
        intent.putExtra("trigger", trigger);

        intent.putExtra("modifyHand", modifyHand);
        intent.putExtra("modifyTime", modifyTime);
        intent.putExtra("feedId", feedId);
        /**
         * 是编辑页面
         */
        if (modifyHand || !TextUtils.isEmpty(modifyTime) || !TextUtils.isEmpty(feedId)) {
            intent.putExtra("position", position);
        }
        setResult(CreateSceneActivity.CODE_EVENT, intent);
        finish();
    }

    private class EventsAdapter extends BaseExpandableListAdapter {

        public EventsAdapter() {

        }

        @Override
        public int getGroupCount() {
            return dclist == null ? 0 : dclist.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return dclist.get(groupPosition).getStream() == null ? 0 : dclist.get(groupPosition).getStream().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return dclist.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return dclist.get(groupPosition).getStream().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final ParentViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SceneEventsListActivity.this).inflate(R.layout.scene_event_list_parent_item, null);
                holder = new ParentViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.iv_fold = (ImageView) convertView.findViewById(R.id.iv_fold);
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(SceneEventsListActivity.this, 60)));
                convertView.setTag(holder);
            } else {
                holder = (ParentViewHolder) convertView.getTag();
            }

            holder.tv_name.setText(dclist.get(groupPosition).getDevice_name());

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SceneEventsListActivity.this).inflate(R.layout.scene_event_list_child_item, null);
                holder = new ChildViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
                holder.item_choose = (TextView) convertView.findViewById(R.id.item_choose);
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(SceneEventsListActivity.this, 60)));
                convertView.setTag(holder);
            } else {
                holder = (ChildViewHolder) convertView.getTag();
            }

            if ((groupPosition + "," + childPosition).equals(choicePostions)) {
                holder.item_choose.setBackgroundResource(R.mipmap.hand_checked);
                if (trigger.getChoose_value_description() != null) {
                    holder.tv_des.setVisibility(View.VISIBLE);
                    trigger.setStream_name(dclist.get(groupPosition).getStream().get(childPosition).getStream_name());
                    holder.tv_des.setText(trigger.getChoose_value_description());
                }
            } else {
                holder.item_choose.setBackgroundResource(R.mipmap.icon_arrow);
                holder.tv_des.setText("");
                holder.tv_des.setVisibility(View.GONE);
            }

            holder.tv_name.setText(dclist.get(groupPosition).getStream().get(childPosition).getStream_name());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    choicePostions = groupPosition + "," + childPosition;
                    deviceStreamClick(groupPosition, childPosition, holder.tv_des, holder.item_choose);
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    static class ChildViewHolder {
        TextView tv_name;
        TextView tv_des;
        TextView item_choose;
    }

    static class ParentViewHolder {
        TextView tv_name;
        ImageView iv_fold;
    }
}
