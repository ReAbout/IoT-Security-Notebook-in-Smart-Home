package com.jd.smartcloudmobilesdk.demo.config;

import android.view.View;

import com.jd.smartcloudmobilesdk.confignet.ConfigNetManager;
import com.jd.smartcloudmobilesdk.confignet.SoftApModel;

/**
 * SoftAp配置
 * Created by yangchangan on 2017/2/25.
 */
public class SoftApActivity extends WiFiConfigActivity implements View.OnClickListener {

    @Override
    protected String getTitleText() {
        return "SoftAp";
    }

    @Override
    protected int getTimeoutPeriod() {
        return TIMEOUT_PERIOD_SOFT_AP;
    }

    @Override
    protected void startWifiConfig() {
        super.startWifiConfig();

        startSoftApConfig();
    }

    @Override
    protected void stopWifiConfig() {
        super.stopWifiConfig();

        stopSoftApConfig();
    }

    /**
     * 开始SoftAp配置
     */
    private void startSoftApConfig() {
        SoftApModel model = new SoftApModel(mWifiSSID, mWifiPwd, mProductUUID);
        ConfigNetManager.getInstance().startSoftApConfig(model, mConfigCallback);

        appendLog("配置参数：\n" + model.toString());
    }

    /**
     * 停止SoftAp配置
     */
    private void stopSoftApConfig() {
        ConfigNetManager.getInstance().stopSoftApConfig();
    }

}
