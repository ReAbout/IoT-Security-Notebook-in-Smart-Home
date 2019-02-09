package com.jd.smartcloudmobilesdk.demo.openapi;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.openapi.OpenApiManager;
import com.jd.smartcloudmobilesdk.utils.JLog;

/**
 * Created by yangchangan on 2018/1/25.
 */
public class OpenApiActivity extends BaseActivity implements View.OnClickListener {

    private EditText mMethodText;
    private EditText mParamsText;
    private TextView mResponseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_api);

        initView();

        mMethodText.setText("jingdong.smart.api.device.list");
        mParamsText.setText("");
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("开放API");
        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.tv_commit).setOnClickListener(this);

        mMethodText = (EditText) findViewById(R.id.et_method);
        mParamsText = (EditText) findViewById(R.id.et_params);
        mResponseView = (TextView) findViewById(R.id.tv_response);
    }

    private void openApiPost(String urlMethod, String paramJson) {
        OpenApiManager.post(urlMethod, paramJson, new ResponseCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "onSuccess response = " + response);

                mResponseView.setText(response);
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "onFailure response = " + response);

                mResponseView.setText(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                this.finish();
                break;
            case R.id.tv_commit:

                String urlMethod = mMethodText.getText().toString();
                String paramJson = mParamsText.getText().toString();
                openApiPost(urlMethod, paramJson);
                break;
        }
    }
}
