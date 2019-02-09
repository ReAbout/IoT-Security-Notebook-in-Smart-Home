package com.jd.smartcloudmobilesdk.demo.control;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.smartcloudmobilesdk.devicecontrol.DeviceControlManager;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;

public class EditNameActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_right;
    private EditText et_device_name;
    private String name;
    private String feed_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            feed_id = bundle.getString("feed_id");
            initView();
        }
        initView();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        et_device_name = (EditText) findViewById(R.id.et_device_name);

        et_device_name.setText(name);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(getString(R.string.save));
        tv_title.setText(getString(R.string.edit_name));

        findViewById(R.id.tv_clean).setOnClickListener(this);
        findViewById(R.id.iv_left).setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                EditNameActivity.this.finish();
                break;
            case R.id.tv_right:
                if (TextUtils.isEmpty(et_device_name.getText().toString().trim())) {
                    Toast.makeText(EditNameActivity.this, getString(R.string.edit_name_hint), Toast.LENGTH_SHORT).show();
                } else {
                    DeviceControlManager.editDeviceName(et_device_name.getText().toString(), feed_id, new ResponseCallback() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            showLoadingDialog();
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            dismissLoadingDialog();
                        }

                        @Override
                        public void onSuccess(String response) {
                            if (CommonUtil.isSuccess(response)) {
                                name = et_device_name.getText().toString();
                                Intent intent = new Intent();
                                intent.putExtra("onRename", name);
                                setResult(HtmlDetailActivity.EDIT_DEVICE, intent);
                                EditNameActivity.this.finish();
                            }
                        }

                        @Override
                        public void onFailure(String response) {

                        }
                    });
                }
                break;
            case R.id.tv_clean:
                et_device_name.setText("");
                break;
        }
    }
}
