package com.jd.smartcloudmobilesdk.demo.config;

import android.view.View;

import com.jd.smartcloudmobilesdk.confignet.ConfigNetManager;
import com.jd.smartcloudmobilesdk.confignet.OneStepCloudModel;

/**
 * 一键配置
 * Created by yngchangan on 2017/2/25.
 */
public class OneStepCloudActivity extends WiFiConfigActivity implements View.OnClickListener {

    @Override
    protected String getTitleText() {
        return "一键配置";
    }

    @Override
    protected void startWifiConfig() {
        super.startWifiConfig();

        startOneStepConfigCloud();
    }

    @Override
    protected void stopWifiConfig() {
        super.stopWifiConfig();

        stopOneStepConfigCloud();
    }

    /**
     * 开始一键配置
     */
    private void startOneStepConfigCloud() {
        int configType = mProductModel != null ? mProductModel.getConfig_type() : 0;
        OneStepCloudModel model = new OneStepCloudModel(mWifiSSID, mWifiPwd, mProductUUID, configType, mDeviceMac);
        ConfigNetManager.getInstance().startOneStepConfigCloud(model, mConfigCallback);

        appendLog("配置参数：\n" + model.toString());
    }

    /**
     * 停止一键配置
     */
    private void stopOneStepConfigCloud() {
        ConfigNetManager.getInstance().stopOneStepConfigCloud();
    }

}
