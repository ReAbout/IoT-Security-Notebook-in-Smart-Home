package com.jd.smartcloudmobilesdk.demo.gateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.gateway.GatewayBindError;

/**
 * 网关子设备已被其他账号绑定
 * Created by yangchangan on 2017/6/16.
 */
public class RepeatBindActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_bind);

        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("重复配置");

        findViewById(R.id.tv_telephone).setOnClickListener(this);

        initView();
    }

    private void initView() {
        GatewayBindError bindError = (GatewayBindError) getIntent().getExtras().get("bind_error");
        if (bindError == null) {
            return;
        }

        String[] keys = new String[]{"产品名称", "序列号", "绑定用户"};
        String[] values = new String[]{bindError.getProduct_name(), bindError.getFeed_id(), bindError.getError_info()};
        int[] ids = new int[]{R.id.layout_item1, R.id.layout_item2, R.id.layout_item3};
        for (int i = 0; i < 3; i++) {
            View layoutItem = findViewById(ids[i]);
            TextView key = (TextView) layoutItem.findViewById(R.id.tv_name);
            TextView value = (TextView) layoutItem.findViewById(R.id.tv_value);
            key.setText(keys[i]);
            value.setText(values[i]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.tv_telephone:

                callTelephone("4006065522");
                break;
        }
    }

    private void callTelephone(String telephone) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + telephone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
