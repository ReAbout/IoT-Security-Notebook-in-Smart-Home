package com.jd.smartcloudmobilesdk.demo.scene;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.scene.model.DeviceDes;
import com.jd.smartcloudmobilesdk.demo.scene.model.DeviceStream;
import com.jd.smartcloudmobilesdk.demo.scene.model.Response;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneDevice;
import com.jd.smartcloudmobilesdk.demo.scene.model.SceneItemModel;
import com.jd.smartcloudmobilesdk.demo.scene.model.Stream;
import com.jd.smartcloudmobilesdk.demo.scene.widget.ArrayWheelAdapter;
import com.jd.smartcloudmobilesdk.demo.scene.widget.WheelView;
import com.jd.smartcloudmobilesdk.demo.utils.DisplayUtils;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.scene.SceneManager;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneAddTaskUI extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Gallery device_g;
    private ListView listView;
    private List<SceneDevice> list;
    private TextView device_name;
    private DesAdapter adapter;
    private RAdapter oAdapter;
    private ResponseOptsAdapter rAdapter;
    private Response response = new Response();
    private Map<Integer, Boolean> map;

    private long time;

    /*设备参数*/
    private List<SceneItemModel> items;
    private String feed_id = "";
    private DecimalFormat format;

    private String image = "";
    private String name = "";
    private Integer position = null; // 触发条件点击位置
    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_add_task);

        map = new HashMap<>();
        items = new ArrayList<>();
        format = new DecimalFormat("##0.000");
        initView();
        getIFTTTDeviceList();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("添加任务");

        device_g = (Gallery) findViewById(R.id.device_g);
        adapter = new DesAdapter(this);
        device_g.setAdapter(adapter);
        device_g.setOnItemSelectedListener(this);

        device_name = (TextView) findViewById(R.id.sc_device_name);
        listView = (ListView) findViewById(R.id.sc_attr_select);
        oAdapter = new RAdapter(this);
        listView.setAdapter(oAdapter);
        listView.setOnItemClickListener(this);

        findViewById(R.id.sat_sure).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                this.finish();
                break;
            case R.id.sat_sure:
                if (items.isEmpty()) {
                    Toast.makeText(this, "请选择任务", Toast.LENGTH_SHORT).show();
                    return;
                }
                finishActivity(items);
                break;
        }
    }

    private void finishActivity(List<SceneItemModel> items) {
        Intent intent = new Intent();
        intent.putExtra("data", (Serializable) items);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 获取IFTTT设备列表
     */
    private void getIFTTTDeviceList() {
        Map<String, Object> map = new HashMap<>();
        map.put("part", "response");

        SceneManager.getIFTTTDeviceList(map, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getIFTTTDeviceList onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        String result = new JSONObject(response).getString("result");
                        String l = new JSONObject(result).getString("list");
                        Type type = new TypeToken<List<SceneDevice>>() {
                        }.getType();
                        Gson gson = new Gson();
                        List<SceneDevice> data = gson.fromJson(l, type);
                        list = getData(data);
                        adapter.setList(list);
                        if (list.size() > 1) {
                            device_g.setSelection(1);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getIFTTTDeviceList onFailure response = " + response);
                toastShort("网络错误");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.dc_opts_plist:
                DeviceDes deviceDes = (DeviceDes) rAdapter.getItem(position);
                if (deviceDes == null) {
                    return;
                }

                if (deviceDes.isStatus()) {
                    deviceDes.setStatus(false);
                    rAdapter.setB(false);
                    for (int j = 0; j < rAdapter.getSectionCount(); j++) {
                        for (int i = 0; i < rAdapter.getCountForSection(j); i++) {
                            DeviceDes dd = (DeviceDes) rAdapter.getItem(j, i);
                            dd.setStatus(false);
                            rAdapter.getItemView(j, i, view, parent).findViewById(R.id.item_choose).setBackgroundResource(0);
                        }
                    }
                } else {
                    for (int j = 0; j < rAdapter.getSectionCount(); j++) {
                        for (int i = 0; i < rAdapter.getCountForSection(j); i++) {
                            DeviceDes dd = (DeviceDes) rAdapter.getItem(j, i);
                            dd.setStatus(false);
                            rAdapter.getItemView(j, i, view, parent).findViewById(R.id.item_choose).setBackgroundResource(0);
                        }
                    }
                    TextView choose = (TextView) view.findViewById(R.id.item_choose);
                    choose.setBackgroundResource(R.mipmap.ico_ok_h);
                    deviceDes.setStatus(true);
                    rAdapter.setB(true);
                }
                if (deviceDes.getKey() != null) {
                    response.setValue(deviceDes.getKey());
                    response.setMode("advance");
                    response.setKeyValue(deviceDes.getValue());
                    if (response.getValue_des() == null) {
                        response.setChoose_value_description(deviceDes.getValue() + response.getSymbol());
                    } else {
                        response.setChoose_value_description(deviceDes.getValue());
                    }
                    oAdapter.notifyDataSetChanged();
                } else if (deviceDes.getId() != null) {
                    response.setMode("common");
                    response.setId(deviceDes.getId());
                    response.setDescription(deviceDes.getDescription());
                    response.setChoose_value_description(deviceDes.getDescription());
                    oAdapter.notifyDataSetChanged();
                }
                rAdapter.setCustomer_choose(false);
                rAdapter.setProgressValue(null);
                rAdapter.notifyDataSetChanged();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                isChooseData(position);
                break;
            case R.id.sc_attr_select:
                DeviceStream data = oAdapter.getItem(position);
                if (!data.getIs_type()) { // 非日期数据
                    response.setValue_type(data.getValue_type());
                    response.setStream_id(data.getStream_id());
                    response.setStream_name(data.getStream_name());
                    response.setSymbol(data.getSymbol());
                    TextView choose_options = (TextView) view.findViewById(R.id.choose_options);
                    TextView item_choose = (TextView) view.findViewById(R.id.item_choose);
                    choose_options.setText("");
                    choose_options.setVisibility(View.GONE);
                    if (rAdapter != null) {
                        rAdapter.getEcho().setText("");
                    }
                    if (map.get(position)) {
                        if (response.getValue() != null || response.getId() != null) {
                            for (int i = 0; i < oAdapter.getCount(); i++) {
                                map.put(i, false);
                                oAdapter.getView(i, view, parent).findViewById(R.id.item_choose).setBackgroundResource(0);
                            }
                            if (rAdapter != null) {
                                rAdapter.setB(true);
                            }
                            map.put(position, true);
                            oAdapter.getView(position, view, parent).setBackgroundResource(R.mipmap.ico_ok_h);
                        } else {
                            response.setChoose_value_description(null);
                            map.put(position, false);
                            item_choose.setBackgroundResource(R.mipmap.ico_goto_g_h);
                        }
                    } else {
                        for (int i = 0; i < oAdapter.getCount(); i++) {
                            map.put(i, false);
                            oAdapter.getView(i, view, parent).findViewById(R.id.item_choose).setBackgroundResource(R.mipmap.ico_goto_g_h);
                        }
                        if (rAdapter != null) {
                            rAdapter.setB(false);
                            // rAdapter.setCustomer_choose(false);
                            rAdapter.setProgressValue(null);
                        }
                        map.put(position, true);
                        response.setValue(null);
                        response.setChoose_value_description(null);
                        item_choose.setBackgroundResource(R.mipmap.ico_ok_h);
                    }
                    showDialog(data.getStream_name(), data, choose_options, item_choose, position);
                    oAdapter.notifyDataSetChanged();
                } else {
                    response.setValue_type(position + "");
                    response.setStream_id(data.getStream_id());
                    response.setStream_name(data.getStream_name());
                    response.setSymbol(data.getSymbol());
                    TextView choose_options = (TextView) view.findViewById(R.id.choose_options);
                    TextView item_choose = (TextView) view.findViewById(R.id.item_choose);
                    choose_options.setText("");
                    choose_options.setVisibility(View.GONE);
                    if (rAdapter != null) {
                        rAdapter.getEcho().setText("");
                    }
                    if (map.get(position)) {
                        if (response.getValue() != null || response.getId() != null) {
                            for (int i = 0; i < oAdapter.getCount(); i++) {
                                map.put(i, false);
                                oAdapter.getView(i, view, parent).findViewById(R.id.item_choose).setBackgroundResource(0);
                            }
                            if (rAdapter != null) {
                                rAdapter.setB(true);
                            }
                            map.put(position, true);
                            oAdapter.getView(position, view, parent).setBackgroundResource(R.mipmap.ico_ok_h);
                        } else {
                            response.setChoose_value_description(null);
                            map.put(position, false);
                            item_choose.setBackgroundResource(R.mipmap.ico_goto_g_h);
                        }
                    } else {
                        for (int i = 0; i < oAdapter.getCount(); i++) {
                            map.put(i, false);
                            oAdapter.getView(i, view, parent).findViewById(R.id.item_choose).setBackgroundResource(R.mipmap.ico_goto_g_h);
                        }
                        if (rAdapter != null) {
                            rAdapter.setB(false);
                            rAdapter.setProgressValue(null);
                        }
                        map.put(position, true);
                        response.setValue(null);
                        response.setChoose_value_description(null);
                        item_choose.setBackgroundResource(R.mipmap.ico_ok_h);
                    }
                    if (data.getCustomtime()) {
                        showTimeDialog();
                    } else {
                        switch (position) {
                            case 0:
                                time = 1;
                                break;
                            case 1:
                                time = 3;
                                break;
                            case 2:
                                time = 5;
                                break;
                        }
                        items.clear();
                        SceneItemModel model = new SceneItemModel();
                        model.setDevice_type("time");
                        model.setDelay(time + "");
                        items.add(model);
                        if (items != null && !items.isEmpty()) {
                            findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#FF4061"));
                        } else {
                            findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#BDC8D5"));
                        }
                    }
                    oAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SceneDevice model = adapter.getItem(position);
        if (model == null) {
            return;
        }
        device_name.setText(model.getDevice_name());

        if (map.size() > 0) {
            map.clear();
        }
        if ("1".equals(model.getProduct_id())) {
            List<DeviceStream> streamList = model.getStream();
            for (int i = 0; i < streamList.size(); i++) {
                if (response.getValue_type() != null && !response.getValue_type().equals("int") && !response.getValue_type().equals("float") && !response.getValue_type().equals("string") && Integer.parseInt(response.getValue_type()) == i) {
                    map.put(i, true);
                } else {
                    map.put(i, false);
                }
            }
            oAdapter.setList(streamList);

            feed_id = "";
            image = "";
            name = "";
        } else {
            feed_id = model.getFeed_id();
            image = model.getP_img_url();
            name = model.getDevice_name();

            response.setFeed_id(model.getFeed_id());
            response.setProduct_id(model.getProduct_id());
            response.setType(model.getType());
            response.setP_img_url(model.getP_img_url());

            device_name.setText(model.getDevice_name());
            response.setDevice_name(model.getDevice_name());
            if (map.size() > 0) {
                map.clear();
            }
            if (model.getStream() == null) {
                return;
            }
            for (int i = 0; i < model.getStream().size(); i++) {
                map.put(i, false);
                this.position = null;
            }
            for (int i = 0; i < model.getStream().size(); i++) {
                for (int j = 0; j < model.getStream().get(i).getDeviceDes().size(); j++) {
                    if (response.getId() != null && response.getId().equals(model.getStream().get(i).getDeviceDes().get(j).getId())) {
                        map.put(i, true);
                        this.position = i;
                    }
                }
            }

            for (int i = 0; i < model.getStream().size(); i++) {
                if (model.getStream().get(i).getValue_des() != null && model.getStream().get(i).getValue_des().size() > 0) {
                    for (int j = 0; j < model.getStream().get(i).getValue_des().size(); j++) {
                        Map<String, String> map1 = null;
                        try {
                            map1 = parseMap(model.getStream().get(i).getValue_des().get(j).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (map1 != null) {
                            for (String k : map1.keySet()) {
                                if (response.getStream_id() != null && response.getStream_id().equals(model.getStream().get(i).getStream_id()) &&
                                        response.getKeyValue() != null && map1.get(k) != null && response.getKeyValue().equals(map1.get(k))) {
                                    map.put(i, true);
                                    this.position = i;
                                }
                            }
                        }
                    }
                } else {
                    if (response.getStream_id() != null && response.getStream_id().equals(model.getStream().get(i).getStream_id()) && !response.getValue_type().equals("string")
                            ) {
                        map.put(i, true);
                        this.position = i;
                    }
                }
            }
            oAdapter.setList(model.getStream());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Map<String, String> parseMap(String jsonStr) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        String str = "";
        int i = 0;
        int j = 0;
        if (jsonStr.contains("/")) {
            i = jsonStr.indexOf("/");
            j = jsonStr.indexOf("=");
            i = i - j - 1;
            str = jsonStr.replace("/", "");
        } else {
            str = jsonStr;
        }
        Map<String, String> map = null;
        try {
            map = gson.fromJson(str, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (map != null) {
            for (String k : map.keySet()) {
                String v = map.get(k);
                if (jsonStr.contains("/")) {
                    v = v.substring(0, i) + "/" + v.substring(i, v.length());
                }
                map.put(k, v);
            }
        }
        return map;
    }

    /**
     * 设备图
     */
    class DesAdapter extends ArrayListAdapter<SceneDevice> {

        public DesAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_device_gallery, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.tv_test);
            SceneDevice data = getItem(position);
            ImageLoader.getInstance().displayImage(data.getP_img_url(), imageView);
            return convertView;
        }
    }

    /**
     * 列表参数
     */
    class RAdapter extends ArrayListAdapter<DeviceStream> {

        public RAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_device_stream, null);
            }
            TextView item_name = (TextView) convertView.findViewById(R.id.item_name);
            TextView item_choose = (TextView) convertView.findViewById(R.id.item_choose);
            TextView choose_options = (TextView) convertView.findViewById(R.id.choose_options);
            DeviceStream data = getItem(position);
            item_name.setText(data.getStream_name());
            if (map.get(position)) {
                item_choose.setBackgroundResource(R.mipmap.ico_ok_h);
                if (SceneAddTaskUI.this.position != null && SceneAddTaskUI.this.position == position && response.getChoose_value_description() != null) {
                    choose_options.setVisibility(View.VISIBLE);
                    response.setKeyValue(response.getChoose_value_description());
                    choose_options.setText(response.getChoose_value_description());
                }
            } else {
                item_choose.setBackgroundResource(R.mipmap.ico_goto_g_h);
                choose_options.setText("");
                choose_options.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    private List<SceneDevice> getData(List<SceneDevice> models) {
        List<DeviceStream> parameterModels = new ArrayList<>();
        DeviceStream parameterModel = new DeviceStream();
        parameterModel.setStream_name("1秒");
        parameterModel.setCustomtime(false);
        parameterModel.setIs_type(true);
        parameterModels.add(parameterModel);
        DeviceStream parameterModel1 = new DeviceStream();
        parameterModel1.setStream_name("3秒");
        parameterModel1.setCustomtime(false);
        parameterModel1.setIs_type(true);
        parameterModels.add(parameterModel1);
        DeviceStream parameterModel2 = new DeviceStream();
        parameterModel2.setStream_name("5秒");
        parameterModel2.setCustomtime(false);
        parameterModel2.setIs_type(true);
        parameterModels.add(parameterModel2);
        DeviceStream parameterModel3 = new DeviceStream();
        parameterModel3.setStream_name("自定义时长");
        parameterModel3.setCustomtime(true);
        parameterModel3.setIs_type(true);
        parameterModels.add(parameterModel3);

        SceneDevice time = new SceneDevice();
        time.setP_img_url("drawable://" + R.mipmap.icon_hourglass_round);
        time.setDevice_name("延时");
        time.setProduct_id("1");
        time.setStream(parameterModels);

        List<SceneDevice> deviceList = new ArrayList<>();
        deviceList.add(time);
        deviceList.addAll(models);
        for (int i = 0; i < 4; i++) {
            map.put(i, false);
        }
        return deviceList;
    }

    private void isChooseData(int position) {
        items.clear();
        if (rAdapter.isCustomer_choose() && rAdapter.getProgressValue() != null) {
            response.setValue(rAdapter.getProgressValue());
            response.setMode("advance");
            rAdapter.getEcho().setVisibility(View.VISIBLE);
            response.setEcho_value(rAdapter.getEcho().getText().toString());
            response.setChoose_value_description(rAdapter.getProgressValue() + rAdapter.getSymbol());
            SceneItemModel si = new SceneItemModel();
            si.setDevice_type("device");
            si.setFeed_id(feed_id);
            si.setImage(image);
            si.setName(response.getDevice_name());
            List<Stream> lstream = new ArrayList<>();
            Stream stream = new Stream();
            stream.setStream_id(response.getStream_id());
            stream.setStream_name(response.getStream_name());
            stream.setCurrent_value(rAdapter.getProgressValue());
            stream.setMaster_flag(rAdapter.getProgressValue());
            stream.setUnits(response.getSymbol());
            lstream.add(stream);
            si.setStreams(lstream);
            items.add(si);
            //	response.setComparison_opt("==");
            oAdapter.notifyDataSetChanged();
        } else if (rAdapter.isB()) {
            SceneItemModel si = new SceneItemModel();
            si.setDevice_type("device");
            si.setFeed_id(feed_id);
            si.setImage(image);
            si.setName(response.getDevice_name());
            List<Stream> lstream = new ArrayList<>();
            Stream stream = new Stream();
            stream.setStream_id(response.getStream_id());
            stream.setStream_name(response.getStream_name());
            stream.setCurrent_value(response.getValue());
            stream.setMaster_flag(response.getKeyValue());
            lstream.add(stream);
            si.setStreams(lstream);
            items.add(si);
            if (response.getKeyValue() == null) {
                items.clear();
                for (int i = 0; i < map.size(); i++) {
                    map.put(i, false);
                }
                oAdapter.notifyDataSetChanged();
            }
        } else {
            rAdapter.getEcho().setVisibility(View.GONE);
            Toast.makeText(SceneAddTaskUI.this, "请选择条件", Toast.LENGTH_SHORT).show();
            rAdapter.getList().get(0).setStatus(false);
            rAdapter.setCustomer_choose(false);
            for (int i = 0; i < map.size(); i++) {
                map.put(i, false);
            }
            this.position = null;
            if (response.getValue() != null) { // 假如点击其他选项未选择参数，清理所有已选择的参数
                response.setValue(null);
                response.setMode(null);
            } else if (response.getId() != null) {
                response.setMode(null);
                response.setId(null);
            }
            rAdapter.notifyDataSetChanged();
            oAdapter.notifyDataSetChanged();
        }

        if (items != null && items.size() > 0) {
            findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#FF4061"));
        } else {
            findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#BDC8D5"));
        }
    }

    /**
     * @param name
     * @param data
     * @param echo 回显所选择的数值
     */
    private void showDialog(String name, final DeviceStream data, TextView echo, TextView item_choose, final int position) {
        dialog = new Dialog(this, R.style.dialogTheme1);
        View view = LayoutInflater.from(this).inflate(R.layout.dc_opts_dialog, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (rAdapter == null) {
            rAdapter = new ResponseOptsAdapter(SceneAddTaskUI.this);
        }
        PinnedHeaderListView plistView = (PinnedHeaderListView) view.findViewById(R.id.dc_opts_plist);
        plistView.setOnItemClickListener(this);
        Button dc_opts_sure = (Button) view.findViewById(R.id.dc_opts_sure);
        dc_opts_sure.setVisibility(View.GONE);

        plistView.setAdapter(rAdapter);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.windowstyle);  // 添加动画
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = DisplayUtils.getDisplayWidth(); // 宽度
        lp.height = DisplayUtils.getDisplayHeight() * 2 / 3;
        window.setAttributes(lp);

        List<DeviceStream> dsList = new ArrayList<>();
        if (data.getDeviceDes() != null && data.getDeviceDes().size() > 0) {
            for (int i = 0; i < data.getDeviceDes().size(); i++) {
                DeviceDes deviceDes = data.getDeviceDes().get(i);
                if (response.getMode() != null && response.getMode().equals("common") && response.getId() != null && deviceDes.getId() != null && response.getId().equals(deviceDes.getId())) {
                    deviceDes.setStatus(true);
                    rAdapter.setB(true);
                } else {
                    deviceDes.setStatus(false);
                    rAdapter.setB(false);
                }
            }
            DeviceStream stream = new DeviceStream();
            stream.setStream_name(name);
            stream.setDeviceDes(data.getDeviceDes());
            stream.setStatus(data.isStatus());
            stream.setUnits(data.getUnits());
            dsList.add(stream);
        }
        DeviceStream stream1 = new DeviceStream();
        stream1.setStream_name(name);
        Map<Integer, Object> mapc = new HashMap<>();
        if (data.getValue_des() != null) {
            for (int i = 0; i < data.getValue_des().size(); i++) {
                Map<String, String> map1 = parseMap(data.getValue_des().get(i).toString());
                for (String k : map1.keySet()) {
                    mapc.put(i, k + "," + map1.get(k));
                }
            }
            List<DeviceDes> list = new ArrayList<>();
            for (Integer key : mapc.keySet()) {
                DeviceDes dd = new DeviceDes();
                String str[] = mapc.get(key).toString().split(",");
                dd.setKey(str[0]);
                dd.setValue(str[1]);
                list.add(dd);
            }

            for (int i = 0; i < list.size(); i++) {
                DeviceDes dd = list.get(i);
                if (response.getMode() != null && response.getMode().equals("advance") && response.getValue() != null && dd.getKey() != null && response.getValue().equals(dd.getKey())) {
                    dd.setStatus(true);
                } else {
                    dd.setStatus(false);
                }
            }
            stream1.setDeviceDes(list);
        } else {
//            response.setValue_des(null);
//            response.setEcho_value(null);
//            if (rAdapter.getProgressValue() == null) {
//                if (isInteger(response.getEcho_value())) {
//                    if (response.getEcho_value() != null) {
//                        rAdapter.setProgressValue(response.getEcho_value());
//                    }
//                    if (response.getComparison_opt() != null) {
//                        stream1.setComparison(response.getComparison_opt());
//                    }
//                }
//            }
            stream1.setValue_des(data.getValue_des());
            stream1.setComparison_opt(data.getComparison_opt());
            stream1.setMax_value(data.getMax_value());
            stream1.setMin_value(data.getMin_value());
            stream1.setSymbol(data.getSymbol());
            stream1.setValue_type(data.getValue_type());
        }
        dsList.add(stream1);
        this.position = position;
        rAdapter.setItem_choose(item_choose);
        rAdapter.setList(dsList);
        rAdapter.setEcho(echo);
        rAdapter.setItem_choose(item_choose);
        rAdapter.setSymbol(data.getSymbol());

        rAdapter.notifyDataSetChanged();
        dialog.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                items.clear();
                if (rAdapter.isCustomer_choose() && rAdapter.getProgressValue() != null) {
                    response.setValue(rAdapter.getProgressValue());
                    response.setMode("advance");
                    rAdapter.getEcho().setVisibility(View.VISIBLE);
                    response.setEcho_value(rAdapter.getEcho().getText().toString());
                    response.setChoose_value_description(rAdapter.getProgressValue() + rAdapter.getSymbol());
                    // response.setComparison_opt("==");
                    SceneItemModel si = new SceneItemModel();
                    si.setDevice_type("device");
                    si.setFeed_id(feed_id);
                    si.setImage(image);
                    si.setName(response.getDevice_name());
                    List<Stream> lstream = new ArrayList<>();
                    Stream stream = new Stream();
                    stream.setStream_id(response.getStream_id());
                    stream.setStream_name(response.getStream_name());
                    stream.setCurrent_value(rAdapter.getProgressValue());
                    stream.setMaster_flag(rAdapter.getProgressValue());
                    stream.setUnits(response.getSymbol());
                    lstream.add(stream);
                    si.setStreams(lstream);
                    items.add(si);
                    oAdapter.notifyDataSetChanged();
                } else if (rAdapter.isB()) {
                    SceneItemModel si = new SceneItemModel();
                    si.setDevice_type("device");
                    si.setFeed_id(feed_id);
                    si.setImage(image);
                    si.setName(response.getDevice_name());
                    List<Stream> lstream = new ArrayList<>();
                    Stream stream = new Stream();
                    stream.setStream_id(response.getStream_id());
                    stream.setStream_name(response.getStream_name());
                    stream.setCurrent_value(response.getValue());
                    stream.setMaster_flag(response.getKeyValue());
                    lstream.add(stream);
                    si.setStreams(lstream);
                    items.add(si);
                    if (response.getKeyValue() == null) {
                        items.clear();
                        for (int i = 0; i < map.size(); i++) {
                            map.put(i, false);
                        }
                        oAdapter.notifyDataSetChanged();
                    }
                } else {
                    rAdapter.getEcho().setVisibility(View.GONE);
                    Toast.makeText(SceneAddTaskUI.this, "请选择条件", Toast.LENGTH_SHORT).show();
                    rAdapter.getList().get(0).setStatus(false);
                    rAdapter.setCustomer_choose(false);
                    for (int i = 0; i < map.size(); i++) {
                        map.put(i, false);
                    }
                    SceneAddTaskUI.this.position = null;
                    if (response.getValue() != null) { // 假如点击其他选项未选择参数，清理所有已选择的参数
                        response.setValue(null);
                        response.setMode(null);
                    } else if (response.getId() != null) {
                        response.setMode(null);
                        response.setId(null);
                    }
                    rAdapter.notifyDataSetChanged();
                    oAdapter.notifyDataSetChanged();
                }

                if (items != null && items.size() > 0) {
                    findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#FF4061"));
                } else {
                    findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#BDC8D5"));
                }
            }
        });

        dc_opts_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                items.clear();
                if (rAdapter.isCustomer_choose() && rAdapter.getProgressValue() != null) {
                    response.setValue(rAdapter.getProgressValue());
                    response.setMode("advance");
                    rAdapter.getEcho().setVisibility(View.VISIBLE);
                    response.setEcho_value(rAdapter.getEcho().getText().toString());
                    response.setChoose_value_description(rAdapter.getProgressValue() + rAdapter.getSymbol());
                    SceneItemModel si = new SceneItemModel();
                    si.setDevice_type("device");
                    si.setFeed_id(feed_id);
                    si.setImage(image);
                    si.setName(response.getDevice_name());
                    List<Stream> lstream = new ArrayList<>();
                    Stream stream = new Stream();
                    stream.setStream_id(response.getStream_id());
                    stream.setStream_name(response.getStream_name());
                    stream.setCurrent_value(rAdapter.getProgressValue());
                    stream.setMaster_flag(rAdapter.getProgressValue());
                    stream.setUnits(response.getSymbol());
                    lstream.add(stream);
                    si.setStreams(lstream);
                    items.add(si);
                    //	response.setComparison_opt("==");
                    oAdapter.notifyDataSetChanged();
                } else if (rAdapter.isB()) {
                    SceneItemModel si = new SceneItemModel();
                    si.setDevice_type("device");
                    si.setFeed_id(feed_id);
                    si.setImage(image);
                    si.setName(response.getDevice_name());
                    List<Stream> lstream = new ArrayList<>();
                    Stream stream = new Stream();
                    stream.setStream_id(response.getStream_id());
                    stream.setStream_name(response.getStream_name());
                    stream.setCurrent_value(response.getValue());
                    stream.setMaster_flag(response.getKeyValue());
                    lstream.add(stream);
                    si.setStreams(lstream);
                    items.add(si);
                    if (response.getKeyValue() == null) {
                        items.clear();
                        for (int i = 0; i < map.size(); i++) {
                            map.put(i, false);
                        }
                        oAdapter.notifyDataSetChanged();
                    }
                } else {
                    rAdapter.getEcho().setVisibility(View.GONE);
                    Toast.makeText(SceneAddTaskUI.this, "请选择条件", Toast.LENGTH_SHORT).show();
                    rAdapter.getList().get(0).setStatus(false);
                    rAdapter.setCustomer_choose(false);
                    for (int i = 0; i < map.size(); i++) {
                        map.put(i, false);
                    }
                    SceneAddTaskUI.this.position = null;
                    if (response.getValue() != null) {//假如点击其他选项未选择参数，清理所有已选择的参数
                        response.setValue(null);
                        response.setMode(null);
                    } else if (response.getId() != null) {
                        response.setMode(null);
                        response.setId(null);
                    }
                    rAdapter.notifyDataSetChanged();
                    oAdapter.notifyDataSetChanged();
                }

                if (items != null && items.size() > 0) {
                    findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#FF4061"));
                } else {
                    findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#BDC8D5"));
                }
            }
        });
        dialog.show();
    }

    /*时间弹窗*/
    private void showTimeDialog() {
        final Dialog dialog = new Dialog(this, R.style.dialogTheme1);
        View view = LayoutInflater.from(this).inflate(R.layout.scene_choose_time, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final WheelView hour = (WheelView) view.findViewById(R.id.sc_choose_hour);
        final WheelView minute = (WheelView) view.findViewById(R.id.sc_choose_minute);
        String[] h = new String[24];
        for (int i = 0; i <= 23; i++) {
            h[i] = i + "";
        }
        ArrayWheelAdapter<String> adapterH = new ArrayWheelAdapter<String>(this, h) {
            @Override
            protected void configureTextView(TextView view) {
                super.configureTextView(view);
                view.setGravity(Gravity.RIGHT);
                view.setPadding(0, 10, 80, 10);
            }
        };
        adapterH.setTextColor(Color.parseColor("#000000")); // #5dc41f
        adapterH.setTextSize(20);
        String[] m = new String[60];
        for (int i = 0; i < 60; i++) {
            m[i] = i + "";
        }
        ArrayWheelAdapter<String> adapterM = new ArrayWheelAdapter<String>(this, m) {
            @Override
            protected void configureTextView(TextView view) {
                super.configureTextView(view);
                view.setGravity(Gravity.LEFT);
                view.setPadding(80, 10, 0, 10);
            }
        };
        adapterM.setTextColor(Color.parseColor("#000000"));
        adapterM.setTextSize(20);
        hour.setViewAdapter(adapterH);
        hour.setBeautyFlag(true);
        hour.setCurrentItem(h.length / 2);

        minute.setViewAdapter(adapterM);
        minute.setBeautyFlag(true);
        minute.setCurrentItem(m.length / 2);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.windowstyle);  //添加动画
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = DisplayUtils.getDisplayWidth(); // 宽度
        //	lp.alpha = 0.7f; // 透明度
        lp.height = DisplayUtils.getDisplayHeight() * 4 / 7;
        window.setAttributes(lp);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                time = hour.getCurrentItem() * 60 * 60 + minute.getCurrentItem() * 60;
                items.clear();
                SceneItemModel model = new SceneItemModel();
                model.setDevice_type("time");
                model.setDelay(time + "");
                model.setImage(image);
                model.setName(name);
                items.add(model);
                if (items != null && items.size() > 0) {
                    findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#FF4061"));
                } else {
                    findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#BDC8D5"));
                }

            }
        });
        dialog.show();

        view.findViewById(R.id.sc_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 0;
                items.clear();
                dialog.dismiss();
                for (int i = 0; i < oAdapter.getCount(); i++) {
                    map.put(i, false);
                }
                if (response.getValue() != null) {//假如点击其他选项未选择参数，清理所有已选择的参数
                    response.setValue(null);
                    response.setMode(null);
                } else if (response.getId() != null) {
                    response.setMode(null);
                    response.setId(null);
                }
                oAdapter.notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.sc_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                time = hour.getCurrentItem() * 60 * 60 + minute.getCurrentItem() * 60;
                items.clear();
                SceneItemModel model = new SceneItemModel();
                model.setDevice_type("time");
                model.setDelay(time + "");
                model.setImage(image);
                model.setName(name);
                items.add(model);
                if (items != null && items.size() > 0) {
                    findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#FF4061"));
                } else {
                    findViewById(R.id.sat_sure).setBackgroundColor(Color.parseColor("#BDC8D5"));
                }

            }
        });
    }
}
