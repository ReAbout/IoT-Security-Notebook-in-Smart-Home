package com.jd.smartcloudmobilesdk.demo.config;

import android.view.View;

import com.jd.smartcloudmobilesdk.confignet.ConfigNetManager;
import com.jd.smartcloudmobilesdk.confignet.OneStepNativeModel;

/**
 * 标准一建配置
 * Created by yangchangan on 2017/2/25.
 */
public class OneStepNativeActivity extends WiFiConfigActivity implements View.OnClickListener {

    @Override
    protected String getTitleText() {
        return "标准一键配置";
    }

    @Override
    protected void startWifiConfig() {
        super.startWifiConfig();

        startOneStepConfigNative();
    }

    @Override
    protected void stopWifiConfig() {
        super.stopWifiConfig();

        stopOneStepConfigNative();
    }

    /**
     * 开始标准一键配置
     */
    private void startOneStepConfigNative() {
        OneStepNativeModel model = new OneStepNativeModel(mWifiSSID, mWifiPwd, mProductUUID);
        ConfigNetManager.getInstance().startOneStepConfigNative(model, mConfigCallback);

        appendLog("配置参数：\n" + model.toString());
    }

    /**
     * 停止标准一键配置
     */
    private void stopOneStepConfigNative() {
        ConfigNetManager.getInstance().stopOneStepConfigNative();
    }
}
