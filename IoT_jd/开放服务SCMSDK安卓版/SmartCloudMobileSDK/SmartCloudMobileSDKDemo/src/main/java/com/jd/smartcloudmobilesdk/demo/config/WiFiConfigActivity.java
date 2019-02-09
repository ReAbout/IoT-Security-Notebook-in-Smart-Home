package com.jd.smartcloudmobilesdk.demo.config;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jd.smartcloudmobilesdk.activate.ActivateManager;
import com.jd.smartcloudmobilesdk.activate.BindCallback;
import com.jd.smartcloudmobilesdk.activate.BindResult;
import com.jd.smartcloudmobilesdk.confignet.ConfigNetManager;
import com.jd.smartcloudmobilesdk.confignet.wifi.WiFiConfigCallback;
import com.jd.smartcloudmobilesdk.confignet.wifi.WiFiScanDevice;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.Constant;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * WiFi配网界面：基类（一键配置、SoftAp）
 * Created by yangchangan on 2017/5/17.
 */
public abstract class WiFiConfigActivity extends BaseActivity implements View.OnClickListener {
    protected final int TIMEOUT_PERIOD_CONFIG = 60 * 1000;
    protected final int TIMEOUT_PERIOD_SOFT_AP = 120 * 1000;

    private EditText mWifiSSIDText;
    private EditText mWifiPwdText;
    private EditText mProductUUIDText;

    private TextView mConfigHintView;

    private TextView mTimeView;
    private View mLoadingLayout;

    private ListView mListView;
    private WiFiConfigAdapter mListAdapter;

    protected String mWifiSSID;
    protected String mWifiPwd = "12345678";
    protected String mProductUUID = "32BYAN";
    protected String mDeviceMac;
    protected ProductModel mProductModel;

    // 配网倒计时定时器
    private CountDownTimer mTimeoutTimer;

    // 已绑定设备
    private List<WiFiScanDevice> mBindDeviceList = new ArrayList<>();
    private List<BindResult> mBindResultList = new ArrayList<>();

    private boolean mIsConfiguring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_config);

        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getTitleText());
        titleView.setOnClickListener(this);

        findViewById(R.id.tv_config_desc).setOnClickListener(this);
        findViewById(R.id.tv_start_config).setOnClickListener(this);
        findViewById(R.id.tv_stop_config).setOnClickListener(this);

        mWifiSSIDText = (EditText) findViewById(R.id.et_wifi_ssid);
        mWifiSSIDText.setHint(getWifiSSID());
        mWifiPwdText = (EditText) findViewById(R.id.et_wifi_pwd);
        mWifiPwdText.setHint(getWifiPwd());

        mProductUUIDText = (EditText) findViewById(R.id.et_puid);
        mProductUUIDText.setHint(getProductUUID());

        mConfigHintView = (TextView) findViewById(R.id.tv_config_hint);

        mTimeView = (TextView) findViewById(R.id.tv_time);
        mLoadingLayout = findViewById(R.id.layout_loading);

        mListView = (ListView) findViewById(R.id.list_view);
        mListAdapter = new WiFiConfigAdapter(this);
        mListView.setAdapter(mListAdapter);

        mLogView = (TextView) findViewById(R.id.tv_log);
        mLogScrollView = (ScrollView) findViewById(R.id.sv_log);
    }

    protected String getTitleText() {
        return "WiFi配网";
    }

    protected String getWifiPwd() {
        return mWifiPwd;
    }

    protected String getProductUUID() {
        return mProductUUID;
    }

    private void setConfigParams() {
        mWifiSSID = mWifiSSIDText.getText().toString().trim();
        if (TextUtils.isEmpty(mWifiSSID)) {
            mWifiSSID = mWifiSSIDText.getHint().toString().trim();
        }

        mWifiPwd = mWifiPwdText.getText().toString().trim();
        if (TextUtils.isEmpty(mWifiPwd)) {
            mWifiPwd = mWifiPwdText.getHint().toString().trim();
        }

        // 从产品二维码中解析出productUUID和deviceMac
        EditText qrCodeText = (EditText) findViewById(R.id.et_qr_code);
        String qrCode = qrCodeText.getText().toString();
        Map<String, String> map = ConfigNetManager.parseQRCode(qrCode);

        mDeviceMac = map.get(Constant.KEY_DEVICE_MAC);
        String productUuid = map.get(Constant.KEY_PRODUCT_UUID);
        if (!TextUtils.isEmpty(productUuid)) {
            mProductUUIDText.setText(productUuid);
        }

        mProductUUID = mProductUUIDText.getText().toString().trim();
        if (TextUtils.isEmpty(mProductUUID)) {
            mProductUUID = mProductUUIDText.getHint().toString().trim();
        }
    }

    private void setBindResultList(List<BindResult> bindResultList) {
        if (mListAdapter != null) {
            mListAdapter.setList(bindResultList);
        }
    }

    private void setLoadingVisibility(int visibility) {
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(visibility);
        }
    }

    private void resetResource(boolean isStart) {
        if (mBindResultList == null) {
            mBindResultList = new ArrayList<>();
        }
        mIsConfiguring = isStart;

        if (isStart) {
            mBindDeviceList.clear();
            mBindResultList.clear();
            setBindResultList(mBindResultList);

            setLoadingVisibility(View.VISIBLE);
            mConfigHintView.setText("添加搜索到的设备");
        } else {
            setLoadingVisibility(View.GONE);
            if (mBindResultList.isEmpty()) {
                mConfigHintView.setText("没有搜索到待添加的设备");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopWifiConfig();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.tv_config_desc:

                handleConfigDescClick();
                break;
            case R.id.tv_start_config:

                handleStartConfigClick();
                break;
            case R.id.tv_stop_config:

                handleStopConfigClick();
                break;
            case R.id.tv_title:

                setLogViewVisibility();
                break;
        }
    }

    private void handleConfigDescClick() {
        if (mIsConfiguring) {
            toastShort("正在配网中...");
            return;
        }
        cancelLog();
        setConfigParams();

        getProductDesc(mProductUUID);
    }

    private void handleStartConfigClick() {
        if (mIsConfiguring) {
            toastShort("正在配网中...");
            return;
        }
        cancelLog();
        setConfigParams();

        getProductByProductUuid(mProductUUID);
    }

    private void handleStopConfigClick() {
        stopWifiConfig();
    }

    protected void startWifiConfig() {
        resetResource(true);

        startTimeoutTimer();
        // 子类实现具体业务
    }

    protected void stopWifiConfig() {
        resetResource(false);

        cancelTimeoutTimer();
        // 子类实现具体业务
    }

    /**
     * 获取产品配网说明
     */
    private void getProductDesc(String productUUID) {
        ConfigNetManager.getProductDesc(productUUID, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getProductDesc onSuccess response = " + response);
                appendLog("获取产品配网说明：\n" + response);
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getProductDesc onFailure response = " + response);
                appendLog("获取产品配网说明：\n" + response);
                toastShort("网络错误");
            }
        });
    }

    /**
     * 获取产品信息
     */
    private void getProductByProductUuid(final String productUuid) {
        ConfigNetManager.getProductByProductUUID(productUuid, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getProductByProductUuid onSuccess response = " + response);
                appendLog("获取产品信息：\n" + response);

                if (CommonUtil.isSuccessWithToast(mContext, response)) {
                    try {
                        String result = new JSONObject(response).optString("result");
                        mProductModel = new Gson().fromJson(result, ProductModel.class);
                        if (mProductModel != null) {
                            if (mListAdapter != null) {
                                mListAdapter.setProductModel(mProductModel);
                            }

                            startWifiConfig();
                        } else {
                            toastShort("获取产品信息失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        toastShort("获取产品信息失败");
                    }
                } else {
                    stopWifiConfig();
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getProductByProductUuid onFailure response = " + response);
                appendLog("获取产品信息：\n" + response);
                toastShort("网络错误");
            }
        });
    }

    /**
     * WiFi设备发现回调
     */
    protected WiFiConfigCallback mConfigCallback = new WiFiConfigCallback() {

        @Override
        public void onScanResult(List<WiFiScanDevice> deviceList) {
            if (deviceList == null || deviceList.isEmpty()) {
                return;
            }
            JLog.e(TAG, "onScanResult " + deviceList.toString());

            List<WiFiScanDevice> devices = new ArrayList<>();
            for (WiFiScanDevice device : deviceList) {

                if (!mBindDeviceList.contains(device)) {
                    devices.add(device);
                }
            }

            if (!devices.isEmpty()) {
                appendLog("设备发现：" + deviceList.size());

                // 激活绑定设备
                ActivateManager.getInstance().activateBindDevice(devices, mBindCallback);
            }
        }

        @Override
        public void onConfigFailed(String error) {
            appendLog("设备配网失败：\n" + error);
            stopWifiConfig();
        }
    };

    /**
     * 激活绑定回调
     */
    private BindCallback mBindCallback = new BindCallback() {

        @Override
        public void onSuccess(WiFiScanDevice device, BindResult bindResult) {
            if (device == null || bindResult == null) {
                return;
            }

            if (!mBindDeviceList.contains(device)) {
                mBindDeviceList.add(device);
            }

            if (!mBindResultList.contains(bindResult)) {
                mBindResultList.add(bindResult);
                setBindResultList(mBindResultList);
                appendLog("绑定成功：\n" + bindResult.toString());
            }
        }

        @Override
        public void onError(WiFiScanDevice device, String error) {
            JLog.e(TAG, "bind error device = " + device + " error = " + error);
            appendLog("绑定失败：\n" + error);
        }

        @Override
        public void onFailure(WiFiScanDevice device, String response) {
            JLog.e(TAG, "bind failure device = " + device + " response = " + response);
            appendLog("绑定失败：\n" + response);
        }
    };

    /**
     * 获取超时时间
     */
    protected int getTimeoutPeriod() {
        return TIMEOUT_PERIOD_CONFIG;
    }

    /**
     * 启动超时定时器
     */
    private void startTimeoutTimer() {
        final long millisInFuture = getTimeoutPeriod();
        mTimeoutTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int) (millisUntilFinished / 1000);
                mTimeView.setText(String.format(Locale.getDefault(), "%ds", time));
            }

            @Override
            public void onFinish() {
                stopWifiConfig();
                appendLog("配网超时，停止配网");
            }
        };
        mTimeoutTimer.start();
    }

    /**
     * 取消超时定时器
     */
    private void cancelTimeoutTimer() {
        if (mTimeoutTimer != null) {
            mTimeoutTimer.cancel();
            mTimeoutTimer = null;
        }
    }
}
