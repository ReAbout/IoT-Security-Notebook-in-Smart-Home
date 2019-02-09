package com.jd.smartcloudmobilesdk.demo.control;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;

public class JsNativeActivity extends Activity implements View.OnClickListener {

    private TextView tv_des;

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_native);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            data = bundle.getString("data");
            initView();
        }
    }

    private void initView(){
        ((TextView)findViewById(R.id.tv_title)).setText("本地页面");
        findViewById(R.id.iv_left).setOnClickListener(this);
        tv_des = (TextView) findViewById(R.id.tv_des);
        tv_des.setText(data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_left:
                JsNativeActivity.this.finish();
                break;
        }
    }
}
