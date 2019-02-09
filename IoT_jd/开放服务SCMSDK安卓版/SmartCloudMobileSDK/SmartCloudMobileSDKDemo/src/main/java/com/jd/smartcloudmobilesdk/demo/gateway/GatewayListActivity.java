package com.jd.smartcloudmobilesdk.demo.gateway;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.gateway.GatewayDevice;
import com.jd.smartcloudmobilesdk.gateway.GatewayManager;
import com.jd.smartcloudmobilesdk.gateway.GatewaySubDevice;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 网关子设备列表
 * Created by yangchangan on 2017/5/24.
 */
public class GatewayListActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ListView mListView;
    private GatewayListAdapter mListAdapter;

    private GatewayDevice mGatewayDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_list);

        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("网关设备列表");

        mListView = (ListView) findViewById(R.id.list_view);
        mListAdapter = new GatewayListAdapter(this);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(this);

        mGatewayDevice = (GatewayDevice) getIntent().getSerializableExtra("gateway");
        if (mGatewayDevice != null) {
            getSubDevices(mGatewayDevice.getFeed_id());
        }
    }

    /**
     * 获取子设备列表
     */
    private void getSubDevices(String feed_id) {
        GatewayManager.getSubDevices(feed_id, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getSubDevices onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        JSONObject result = new JSONObject(response).optJSONObject("result");
                        String subDevice = result.optString("sub_device_types");
                        if (TextUtils.isEmpty(subDevice)) {
                            return;
                        }

                        Type type = new TypeToken<List<GatewaySubDevice>>() {
                        }.getType();
                        List<GatewaySubDevice> subDevices = new Gson().fromJson(subDevice, type);

                        mListAdapter.setList(subDevices);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getSubDevices onFailure response = " + response);
                toastShort("网络错误");
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
        final GatewaySubDevice subDevice = mListAdapter.getItem(position);
        if (mGatewayDevice != null) {
            mGatewayDevice.setSubDevice(subDevice);
        }

        int sub_add_type = subDevice.getSub_add_type();
        if (sub_add_type == 1) { // 扫码添加

            Intent intent = new Intent(this, CaptureActivity.class);
            intent.putExtra("gateway", mGatewayDevice);
            startActivity(intent);
            finish();
        } else if (sub_add_type == 2) { // 直接搜索添加

        } else if (sub_add_type == 3) { // 物理按键添加

            Intent intent = new Intent(this, ProductManualActivity.class);
            intent.putExtra("gateway", mGatewayDevice);
            startActivity(intent);
            finish();
        }
    }
}
