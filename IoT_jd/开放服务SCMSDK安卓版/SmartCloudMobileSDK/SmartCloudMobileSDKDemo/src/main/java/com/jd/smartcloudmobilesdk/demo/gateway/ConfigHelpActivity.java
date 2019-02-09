package com.jd.smartcloudmobilesdk.demo.gateway;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;

/**
 * 配置帮助
 * Created by yangchangan on 2017/6/1.
 */
public class ConfigHelpActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_help);

        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("配置帮助");

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl("https://devsmart.jd.com/static/wch20150416/wifi_config_help.html");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
        }
    }
}
