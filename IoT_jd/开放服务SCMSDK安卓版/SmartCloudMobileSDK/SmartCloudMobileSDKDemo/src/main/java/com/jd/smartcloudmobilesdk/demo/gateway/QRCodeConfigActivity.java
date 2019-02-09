package com.jd.smartcloudmobilesdk.demo.gateway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.control.HtmlDetailActivity;
import com.jd.smartcloudmobilesdk.demo.utils.ActivityManagerUtil;
import com.jd.smartcloudmobilesdk.gateway.GatewayBindCallback;
import com.jd.smartcloudmobilesdk.gateway.GatewayBindError;
import com.jd.smartcloudmobilesdk.gateway.GatewayDevice;
import com.jd.smartcloudmobilesdk.gateway.GatewayManager;
import com.jd.smartcloudmobilesdk.gateway.GatewayScanDevice;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.Constant;
import com.jd.smartcloudmobilesdk.utils.JLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 网关子设备：扫描二维码配网
 * Created by yangchangan on 2017/6/18.
 */
public class QRCodeConfigActivity extends BaseActivity implements View.OnClickListener {

    private WebView mWebView;

    // 解析设备二维码信息
    private Map<String, String> mQRCodes;
    private GatewayDevice mGatewayDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_config);

        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("添加设备");
        findViewById(R.id.tv_config).setOnClickListener(this);

        ImageView iconView = (ImageView) findViewById(R.id.iv_icon);
        TextView nameView = (TextView) findViewById(R.id.tv_name);
        TextView macView = (TextView) findViewById(R.id.tv_mac);

        mWebView = (WebView) findViewById(R.id.web_view);

        String qrCode = getIntent().getStringExtra("qr_code");
        mQRCodes = GatewayManager.parseQRCode(qrCode);
        if (mQRCodes != null && !mQRCodes.isEmpty()) {
            macView.setText(String.format("设备编码：%s", mQRCodes.get(Constant.KEY_DEVICE_MAC)));
        }

        mGatewayDevice = (GatewayDevice) getIntent().getSerializableExtra("gateway");
        if (mGatewayDevice != null) {
            nameView.setText(mGatewayDevice.getSubDevice().getProduct_name());
            String imgUrl = mGatewayDevice.getSubDevice().getImg_url();
            ImageLoader.getInstance().displayImage(imgUrl, iconView);

            // 获取配置操作说明
            getProductManual(mGatewayDevice.getSubDevice().getProduct_id());
        }
    }

    /**
     * 获取配置操作说明
     */
    private void getProductManual(String product_id) {
        GatewayManager.getProductManual(product_id, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getProductManual onSuccess response = " + response);
                if (CommonUtil.isSuccess(response)) {
                    try {
                        JSONObject result = new JSONObject(response).optJSONObject("result");
                        String content = result.optString("content");
                        if (TextUtils.isEmpty(content)) {
                            return;
                        }

                        if (mWebView != null) {
                            mWebView.loadData(content, "text/html; charset=UTF-8", null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getProductManual onFailure response = " + response);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.tv_config:

                handleConfigClick();
                break;
        }
    }

    private void handleConfigClick() {
        showLoadingDialog();
        GatewayManager.getInstance().addSubDevice(mGatewayDevice, mQRCodes, mBindCallback);
    }

    private GatewayBindCallback mBindCallback = new GatewayBindCallback() {
        @Override
        public void onSuccess(GatewayScanDevice device) {
            dismissLoadingDialog();

            startHtmlDetailActivity(mGatewayDevice.getFeed_id());
        }

        @Override
        public void onError(GatewayScanDevice device, GatewayBindError bindError) {
            dismissLoadingDialog();

            if (bindError != null) {
                toastShort(bindError.getError_info());
            }
        }

        @Override
        public void onFailure(String response) {
            dismissLoadingDialog();

            toastShort("网络错误");
        }
    };

    private void startHtmlDetailActivity(String feed_id) {
        Activity activity = ActivityManagerUtil.getActivity(HtmlDetailActivity.class);
        if (activity != null) {
            activity.finish();
        }

        Intent intent = new Intent(this, HtmlDetailActivity.class);
        intent.putExtra("feed_id", feed_id);
        intent.putExtra("load_cache", false);
        startActivity(intent);
        finish();
    }
}
