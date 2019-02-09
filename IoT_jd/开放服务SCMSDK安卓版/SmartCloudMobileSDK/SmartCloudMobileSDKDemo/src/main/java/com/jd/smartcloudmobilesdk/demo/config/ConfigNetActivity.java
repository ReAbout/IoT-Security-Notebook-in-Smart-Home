package com.jd.smartcloudmobilesdk.demo.config;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.confignet.ConfigNetManager;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.utils.Constant;
import com.jd.smartcloudmobilesdk.utils.JLog;

import java.util.Map;

/**
 * 设备入网
 * Created by yangchangan on 2017/2/24.
 */
public class ConfigNetActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_net);

        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("设备入网");

        findViewById(R.id.tv_scan_config).setOnClickListener(this);
        findViewById(R.id.tv_config_cloud).setOnClickListener(this);
        findViewById(R.id.tv_config_native).setOnClickListener(this);
        findViewById(R.id.tv_config_softap).setOnClickListener(this);
        findViewById(R.id.tv_config_ble).setOnClickListener(this);

        findViewById(R.id.tv_parse).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.tv_scan_config:

                startActivity(ScanConfigActivity.class);
                break;
            case R.id.tv_config_cloud:

                startActivity(OneStepCloudActivity.class);
                break;
            case R.id.tv_config_native:

                startActivity(OneStepNativeActivity.class);
                break;
            case R.id.tv_config_softap:

                startActivity(SoftApActivity.class);
                break;
            case R.id.tv_config_ble:

                startActivity(BleWiFiActivity.class);
                break;
            case R.id.tv_parse:

                parseQRCode();
                break;
        }
    }

    /**
     * 解析设备二维码信息
     */
    private void parseQRCode() {
        EditText qrCodeText = (EditText) findViewById(R.id.et_qr_code);
        TextView qrCodeView = (TextView) findViewById(R.id.tv_qr_code);

        String qrCode = qrCodeText.getText().toString();
        Map<String, String> map = ConfigNetManager.parseQRCode(qrCode);

        qrCodeView.setText(map.toString());

        // 取值举例
        String product_uuid = map.get(Constant.KEY_PRODUCT_UUID);
        JLog.e(TAG, "product_uuid = " + product_uuid);
    }
}
