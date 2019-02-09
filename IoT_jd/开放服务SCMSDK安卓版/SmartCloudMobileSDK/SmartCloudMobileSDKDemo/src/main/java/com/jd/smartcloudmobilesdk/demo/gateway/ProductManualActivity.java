package com.jd.smartcloudmobilesdk.demo.gateway;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.gateway.GatewayDevice;
import com.jd.smartcloudmobilesdk.gateway.GatewayManager;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 产品配置操作说明
 * Created by yangchangan on 2017/5/26.
 */
public class ProductManualActivity extends BaseActivity implements View.OnClickListener {

    private WebView mWebView;
    private GatewayDevice mGatewayDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manual);

        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("添加设备");
        findViewById(R.id.tv_scan).setOnClickListener(this);

        // 子设备产品图标：自由定制是否展示
        ImageView iconView = (ImageView) findViewById(R.id.iv_icon);
        TextView nameView = (TextView) findViewById(R.id.tv_name);
        mWebView = (WebView) findViewById(R.id.web_view);

        mGatewayDevice = (GatewayDevice) getIntent().getSerializableExtra("gateway");
        if (mGatewayDevice != null) {
            nameView.setText(mGatewayDevice.getSubDevice().getProduct_name());
            String imgUrl = mGatewayDevice.getSubDevice().getImg_url();
            ImageLoader.getInstance().displayImage(imgUrl, iconView);

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
            case R.id.tv_scan:

                Intent intent = new Intent(this, PhysicalConfigActivity.class);
                intent.putExtra("gateway", mGatewayDevice);
                startActivity(intent);
                break;
        }
    }
}
