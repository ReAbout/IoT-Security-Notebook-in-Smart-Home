package com.jd.smartcloudmobilesdk.demo.gateway;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.gateway.GatewayDevice;

/**
 * 模拟扫码页面
 * Created by yangchangan on 2017/6/18.
 */
public class CaptureActivity extends BaseActivity implements OnClickListener {

    private EditText mQRCodeText;

    private GatewayDevice mGatewayDevice;
    private String qrCode = "http://smart.jd.com/download?g=WUU0RD%5ADS1AwMDAwMDEwMDE2X%7AE3MkJEM0JFN0MwOA%3D%3D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("扫码");
        findViewById(R.id.tv_start).setOnClickListener(this);

        mQRCodeText = (EditText) findViewById(R.id.et_qr_code);
        mQRCodeText.setHint(qrCode);

        mGatewayDevice = (GatewayDevice) getIntent().getSerializableExtra("gateway");
    }

    private String getQrCode() {
        String qrCode = mQRCodeText.getText().toString().trim();
        if (TextUtils.isEmpty(qrCode)) {
            qrCode = mQRCodeText.getHint().toString().trim();
        }

        return qrCode;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.tv_start:

                Intent intent = new Intent(this, QRCodeConfigActivity.class);
                intent.putExtra("gateway", mGatewayDevice);
                intent.putExtra("qr_code", getQrCode());
                startActivity(intent);
                finish();
                break;
        }
    }
}
