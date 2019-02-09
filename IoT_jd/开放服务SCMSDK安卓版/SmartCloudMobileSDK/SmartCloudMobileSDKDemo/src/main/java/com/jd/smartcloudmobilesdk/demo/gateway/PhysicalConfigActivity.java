package com.jd.smartcloudmobilesdk.demo.gateway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.control.HtmlDetailActivity;
import com.jd.smartcloudmobilesdk.demo.utils.ActivityManagerUtil;
import com.jd.smartcloudmobilesdk.gateway.GatewayBindCallback;
import com.jd.smartcloudmobilesdk.gateway.GatewayBindError;
import com.jd.smartcloudmobilesdk.gateway.GatewayDevice;
import com.jd.smartcloudmobilesdk.gateway.GatewayManager;
import com.jd.smartcloudmobilesdk.gateway.GatewayScanCallback;
import com.jd.smartcloudmobilesdk.gateway.GatewayScanDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关子设备：物理按键配网
 * Created by yangchangan on 2017/5/27.
 */
public class PhysicalConfigActivity extends BaseActivity implements View.OnClickListener {

    private TextView mConfigView;
    private ListView mConfigListView;
    private GatewayConfigAdapter mConfigAdapter;
    private List<GatewayScanDevice> mConfigList = new ArrayList<>();

    private TextView mConfiguredView;
    private ListView mConfiguredListView;
    private GatewayConfigAdapter mConfiguredAdapter;
    private List<GatewayScanDevice> mConfiguredList = new ArrayList<>();

    private ProgressBar mProgressBar;
    private TextView mProgressView;

    private GatewayDevice mGatewayDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_config);

        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("添加设备");

        mConfigView = ((TextView) findViewById(R.id.tv_config));
        mConfigListView = (ListView) findViewById(R.id.lv_config);

        mConfiguredView = ((TextView) findViewById(R.id.tv_configured));
        mConfiguredListView = (ListView) findViewById(R.id.lv_configured);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressView = (TextView) findViewById(R.id.tv_progress);
        findViewById(R.id.layout_progress).setOnClickListener(this);

        mGatewayDevice = (GatewayDevice) getIntent().getSerializableExtra("gateway");

        mConfigAdapter = new GatewayConfigAdapter(this, false);
        mConfigListView.setAdapter(mConfigAdapter);

        mConfiguredAdapter = new GatewayConfigAdapter(this, true);
        mConfiguredListView.setAdapter(mConfiguredAdapter);

        if (mGatewayDevice != null) {
            startScan(mGatewayDevice);
        }
    }

    private void refreshView() {
        if (mConfigList == null || mConfigList.isEmpty()) {
            mConfigView.setVisibility(View.GONE);
            mConfigListView.setVisibility(View.GONE);
        } else {
            mConfigView.setVisibility(View.VISIBLE);
            mConfigView.setText("搜索到" + mConfigList.size() + "个可添加的设备");
            mConfigListView.setVisibility(View.VISIBLE);
        }

        if (mConfiguredList == null || mConfiguredList.isEmpty()) {
            mConfiguredView.setVisibility(View.GONE);
            mConfiguredListView.setVisibility(View.GONE);
        } else {
            mConfiguredView.setVisibility(View.VISIBLE);
            mConfiguredView.setText("搜索到" + mConfiguredList.size() + "个已添加的设备");
            mConfiguredListView.setVisibility(View.VISIBLE);
        }

        if (mConfigAdapter != null) {
            mConfigAdapter.setList(mConfigList);
        }

        if (mConfiguredAdapter != null) {
            mConfiguredAdapter.setList(mConfiguredList);
        }
    }

    /**
     * 开始设备发现
     */
    public void startScan(GatewayDevice gatewayDevice) {
        GatewayManager.getInstance().startScan(gatewayDevice, new GatewayScanCallback() {

            @Override
            public void onTimerTick(long millisInFuture, long millisUntilFinished) {

                if (mProgressBar != null) {
                    mProgressBar.setMax((int) millisInFuture);
                    mProgressBar.setProgress((int) (millisInFuture - millisUntilFinished));
                }

                // 设备发现结束
                if (millisUntilFinished == 0) {
                    mProgressView.setText("开始使用");
                    if (mConfigList.isEmpty() && mConfiguredList.isEmpty()) {
                        startTimeoutActivity();
                    }
                }
            }

            @Override
            public void onScanResult(List<GatewayScanDevice> deviceList) {
                if (deviceList == null || deviceList.isEmpty()) {
                    return;
                }

                for (GatewayScanDevice device : deviceList) {
                    if (mConfigList != null && !mConfigList.contains(device)) {
                        mConfigList.add(device);
                    }
                }

                refreshView();
            }
        });
    }

    private void startTimeoutActivity() {
        this.finish();
        startActivity(ConfigTimeoutActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GatewayManager.getInstance().stopScan();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.layout_progress:

                if ("搜索设备".equals(mProgressView.getText())) {
                    return;
                } else if (mConfigList.isEmpty()) {
                    startHtmlDetailActivity(mGatewayDevice.getFeed_id());
                } else {
                    toastShort("您还有未添加的子设备");
                }
                break;
        }
    }

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

    /**
     * 网关子设备配网适配器
     */
    public class GatewayConfigAdapter extends ArrayListAdapter<GatewayScanDevice> {

        private boolean mConfigured;

        public GatewayConfigAdapter(Context context, boolean configured) {
            super(context);
            mConfigured = configured;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null || convertView.getTag() == null) {
                convertView = View.inflate(mContext, R.layout.item_gateway_config, null);
                holder = new ViewHolder();
                holder.configView = findViewById(convertView, R.id.tv_config);
                holder.macView = findViewById(convertView, R.id.tv_mac);
                holder.progressBar = findViewById(convertView, R.id.progress_bar);
                holder.addView = findViewById(convertView, R.id.iv_add);
                holder.actionView = findViewById(convertView, R.id.tv_action);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final GatewayScanDevice subDevice = getItem(position);
            holder.macView.setText(String.format("设备编码：%s", subDevice.getMac()));
            if (mConfigured) {
                holder.configView.setText("已添加设备");
                holder.actionView.setVisibility(View.VISIBLE);
                holder.actionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startHtmlDetailActivity(mGatewayDevice.getFeed_id());
                    }
                });
            } else {
                holder.configView.setText("可添加设备");
                holder.addView.setVisibility(View.VISIBLE);
                holder.addView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleAddViewClick(holder, subDevice);
                    }
                });
            }

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView configView;
        TextView macView;
        ProgressBar progressBar;
        ImageView addView;
        TextView actionView;
    }

    private void handleAddViewClick(final ViewHolder holder, GatewayScanDevice subDevice) {
        holder.addView.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.VISIBLE);
        GatewayManager.getInstance().addSubDevice(mGatewayDevice, subDevice, new GatewayBindCallback() {

            @Override
            public void onSuccess(GatewayScanDevice device) {
                holder.addView.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);

                mConfiguredList.add(device);
                if (mConfigList.contains(device)) {
                    mConfigList.remove(device);
                }
                refreshView();
            }

            @Override
            public void onError(GatewayScanDevice device, GatewayBindError bindError) {
                holder.addView.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);

                if (bindError == null) {
                    return;
                }

                if ("1005".equals(bindError.getError_code())) {
                    startRepeatBindActivity(bindError);
                } else {
                    toastShort(bindError.getError_info());
                }
            }

            @Override
            public void onFailure(String response) {
                holder.addView.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);

                toastShort("网络错误");
            }
        });
    }

    private void startRepeatBindActivity(GatewayBindError bindError) {
        Intent intent = new Intent(this, RepeatBindActivity.class);
        intent.putExtra("bind_error", bindError);
        startActivity(intent);
    }
}
