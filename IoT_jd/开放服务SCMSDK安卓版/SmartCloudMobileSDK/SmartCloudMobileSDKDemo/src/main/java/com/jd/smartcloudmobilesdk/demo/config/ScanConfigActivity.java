package com.jd.smartcloudmobilesdk.demo.config;

import com.jd.smartcloudmobilesdk.confignet.ConfigNetManager;

/**
 * WiFi设备发现（适用已入网设备，只进行设备发现）
 * Created by yangchangan on 2018/1/16.
 */
public class ScanConfigActivity extends WiFiConfigActivity {

    @Override
    protected String getTitleText() {
        return "设备发现";
    }

    @Override
    protected void startWifiConfig() {
        super.startWifiConfig();

        startScanConfig();
    }

    @Override
    protected void stopWifiConfig() {
        super.stopWifiConfig();

        stopScanConfig();
    }

    /**
     * 开始设备发现配置
     */
    private void startScanConfig() {
        ConfigNetManager.getInstance().startScanConfig(mProductUUID, mConfigCallback);
        appendLog("配置参数：\n" + "productUUID = " + mProductUUID);
    }

    /**
     * 停止设备发现配置
     */
    private void stopScanConfig() {
        ConfigNetManager.getInstance().stopScanConfig();
    }

}
