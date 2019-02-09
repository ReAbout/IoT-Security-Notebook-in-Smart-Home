package com.jd.smartcloudmobilesdk.demo.control;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.utils.PromptDialog;
import com.jd.smartcloudmobilesdk.devicecontrol.DeviceControlManager;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_name;
    private View tv_edit;
    private TextView tv_des;
    private ImageView iv_logo;
    private TextView btn_del;

    private String imageUrl;
    private String name;
    private String feed_id;
    private int main_sub_type;
    private int isShared;
    private int share_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imageUrl = bundle.getString("imageUrl");
            name = bundle.getString("name");
            feed_id = bundle.getString("feed_id");
            isShared = bundle.getInt("isShared");
            main_sub_type = bundle.getInt("main_sub_type");
            share_count = bundle.getInt("share_count");
            initView();
        }
    }

    private void initView() {
        btn_del = (TextView) findViewById(R.id.btn_del);
        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.tv_edit).setOnClickListener(this);
        btn_del.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_logo = (ImageView) findViewById(R.id.iv_icon);

        tv_title.setText(getString(R.string.setting));
        tv_name.setText(name);
        ImageLoader.getInstance().displayImage(imageUrl, iv_logo);

        if (main_sub_type == 2 && isShared != 1) {
            btn_del.setVisibility(View.GONE);
        } else {
            btn_del.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_del:
                delDevice();
                break;
            case R.id.iv_left:
                editResult();
                break;
            case R.id.tv_edit:
                Intent intent = new Intent(SettingActivity.this, EditNameActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("feed_id", feed_id);
                startActivityForResult(intent, HtmlDetailActivity.EDIT_DEVICE);
                break;
        }
    }

    private void delDevice() {

        checkTimerTask(new OnTaskCheckListener() {

            @Override
            public void onTaskCheck(boolean hasTask) {
                if (isFinishing()) {
                    return;
                }
                String tips = "确定要删除设备吗？";
                if (hasTask) {
                    tips = "";
                }
                if (share_count > 0)
                    tips = "设备已分享，是否确认要删除设备";
                if (main_sub_type == 2) {
                    tips = "删除设备后，将无法控制设备";
                } else if (main_sub_type == 1) {
                    if (isShared != 1) {//设备共享
                        tips = "删除设备后，也将取消家人的共享";
                    } else {
                        tips = "删除设备后，该设备下关联的子设备将一并删除";
                    }
                }
                if (!TextUtils.isEmpty(tips)) {
                    final PromptDialog dialog = new PromptDialog(SettingActivity.this,
                            R.style.jdPromptDialog);
                    dialog.msg = tips;
                    dialog.show();

                    dialog.setConfirmListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            dialog.dismiss();
                            unbindDevice(feed_id, "0");
                        }
                    });
                } else {
                    unbindDevice(feed_id, "0");
                }

            }
        });
    }


    public void checkTimerTask(final OnTaskCheckListener listener) {

        DeviceControlManager.getDeviceTimeTaskWithFeedId(feed_id, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                if (SettingActivity.this.isFinishing()) {
                    return;
                }

                try {
                    if (CommonUtil.isSuccessWithToast(mContext, response)) {

                        final JSONObject object = new JSONObject(response);
                        String resultStr = object.optString("result");
                        JSONObject jsonObject = new JSONObject(resultStr);

                        if (jsonObject.optInt("timed_task_count", 0) > 0) {

                            String tips = "您的设备尚有定时任务，建议清除定时任务后再删除设备";
                            final PromptDialog dialog = new PromptDialog(
                                    SettingActivity.this, R.style.jdPromptDialog);
                            dialog.title = tips;
                            dialog.show();
                            dialog.setConfirmText("仍然删除");
                            dialog.setCancelListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setConfirmListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    dialog.dismiss();
                                    listener.onTaskCheck(true);
                                }
                            });

                        } else {
                            listener.onTaskCheck(false);
                        }
                    }
                } catch (Exception e1) {
                    JLog.e(e1);
                }
            }

            @Override
            public void onFailure(String response) {
                listener.onTaskCheck(false);
            }
        });
    }

    public void unbindResult() {
        setResult(HtmlDetailActivity.UNBIND_DEVICE, null);
        this.finish();
    }

    private void unbindDevice(String feed_id, String force) {

        DeviceControlManager.unbindDevice(feed_id, force, new ResponseCallback() {

            @Override
            public void onFinish() {
                super.onFinish();
                btn_del.setText("删除设备");
            }

            @Override
            public void onStart() {
                super.onStart();
                btn_del.setText("解绑中..");
            }

            @Override
            public void onSuccess(String response) {
                if (CommonUtil.isSuccess(response)) {
                    unbindResult();
                } else {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");

                        String error = obj.getString("error");
                        JSONObject errObj = new JSONObject(error);
                        String errorCode = errObj
                                .getString("errorCode");
                        String errorInfo = errObj
                                .getString("errorInfo");
                        if ("2011".equals(errorCode)) {
                            showForceDialog(errorInfo);
                        } else {
                            if (!TextUtils.isEmpty(errorInfo)) {
                                Toast.makeText(SettingActivity.this, errorInfo, Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        JLog.e(e);
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                Toast.makeText(SettingActivity.this, "网络错误", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void editResult() {
        Intent intent = new Intent();
        intent.putExtra("onRename", name);
        setResult(HtmlDetailActivity.EDIT_NORMAL, intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HtmlDetailActivity.EDIT_DEVICE:
                if (data != null) {
                    name = data.getStringExtra("onRename");
                    tv_name.setText(name);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                editResult();
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private interface OnTaskCheckListener {
        void onTaskCheck(boolean hasTask);
    }

    public void showForceDialog(String errorInfo) {
        if (!this.isFinishing()) {
            final PromptDialog dialog = new PromptDialog(this,
                    R.style.jdPromptDialog);
            dialog.title = "提示";
            dialog.msg = "删除设备后，该设备参与的设备互联也会被删除";
            dialog.setConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    unbindDevice(feed_id, "1");
                }
            });
            dialog.setCancelListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    btn_del.setText("删除设备");
                }
            });
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            // dialog.setCancelVisible(View.GONE);
        }
    }
}
