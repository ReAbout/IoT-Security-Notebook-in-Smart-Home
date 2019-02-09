package com.jd.smartcloudmobilesdk.demo.gateway;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;

/**
 * 配网超时
 * Created by yangchangan on 2017/6/1.
 */
public class ConfigTimeoutActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_timeout);

        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("配置超时");

        findViewById(R.id.btn_config_help).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.btn_config_help:

                startActivity(ConfigHelpActivity.class);
                break;
        }
    }
}
