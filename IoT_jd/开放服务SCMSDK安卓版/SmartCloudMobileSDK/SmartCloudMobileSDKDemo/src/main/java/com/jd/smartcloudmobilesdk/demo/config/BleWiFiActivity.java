package com.jd.smartcloudmobilesdk.demo.config;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.activate.ActivateManager;
import com.jd.smartcloudmobilesdk.activate.BindCallback;
import com.jd.smartcloudmobilesdk.activate.BindResult;
import com.jd.smartcloudmobilesdk.confignet.ConfigNetManager;
import com.jd.smartcloudmobilesdk.confignet.ble.base.BleConfigCallback;
import com.jd.smartcloudmobilesdk.confignet.ble.base.BleDevice;
import com.jd.smartcloudmobilesdk.confignet.ble.base.BleScanCallback;
import com.jd.smartcloudmobilesdk.confignet.wifi.WiFiScanDevice;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.utils.JLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Ble+WiFi配网
 * Created by yangchangan on 2017/2/27.
 */
public class BleWiFiActivity extends BaseActivity implements View.OnClickListener {
    private static final int STATUS_BLE_ENABLED = 0;
    private static final int STATUS_BLUETOOTH_NOT_AVAILABLE = 1;
    private static final int STATUS_BLE_NOT_AVAILABLE = 2;
    private static final int STATUS_BLUETOOTH_DISABLED = 3;

    private static final int REQUEST_ENABLE_BT = 1;

    // 配网类型：1：单个 2：批量
    private static final int NET_TYPE_ONE = 1;
    private static final int NET_TYPE_BATCH = 2;

    private ListView mListView;
    private BleListAdapter mListAdapter;
    private List<BleDevice> mBleDeviceList;

    private String mWifiSSID;
    private String mWifiPassword;

    // 测试配网时间
    private int mNetType;
    private long mStartTime;
    private TextView mTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_wifi);

        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText("蓝牙配网");

        findViewById(R.id.tv_batch).setOnClickListener(this);
        findViewById(R.id.tv_stop).setOnClickListener(this);
        findViewById(R.id.tv_start).setOnClickListener(this);

        mTimeView = (TextView) findViewById(R.id.tv_time);
        mLogView = (TextView) findViewById(R.id.tv_log);

        mListView = (ListView) findViewById(R.id.list_view);

        mBleDeviceList = new ArrayList<>();
        if (mListAdapter == null) {
            mListAdapter = new BleListAdapter(this);
            mListView.setAdapter(mListAdapter);
        }
    }

    private void setTimeView(long time) {
        if (mTimeView != null) {
            mTimeView.setText(String.format(Locale.getDefault(), "%.2fs", time / 1000.0f));
        }
    }

    private void clearBleDeviceList() {
        if (mBleDeviceList != null) {
            mBleDeviceList.clear();
        } else {
            mBleDeviceList = new ArrayList<>();
        }

        if (mListAdapter != null) {
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTimeView(0);

        // 清除记录
        clearBleDeviceList();
        checkBluetoothEnable();

        if (Build.VERSION.SDK_INT >= 18) {
            registerReceiver();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT >= 18) {
            unregisterReceiver();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ConfigNetManager.getInstance().stopBleConfig();
    }

    /**
     * 检查蓝牙状态
     */
    private void checkBluetoothEnable() {
        if (Build.VERSION.SDK_INT < 18) {
            return;
        }

        int bleStatus = getBleStatus(this);
        if (bleStatus != STATUS_BLE_ENABLED) {

            // 启动蓝牙
            enableBluetooth();
        }
    }

    /**
     * 获取蓝牙状态
     */
    public int getBleStatus(Context context) {
        // Use this check to determine whether BLE is supported on the device.
        // Then you can selectively disable BLE-related features.
        if (!context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            return STATUS_BLE_NOT_AVAILABLE;
        }

        // Checks if Bluetooth is supported on the device.
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return STATUS_BLUETOOTH_NOT_AVAILABLE;
        }

        if (!adapter.isEnabled()) {
            return STATUS_BLUETOOTH_DISABLED;
        }

        return STATUS_BLE_ENABLED;
    }

    /**
     * 启动蓝牙
     */
    private void enableBluetooth() {
        // 打开蓝牙(方法一)：调用enable，直接打开蓝牙
        // BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        // if (adapter != null) {
        //     adapter.enable();
        // }

        // 打开蓝牙（方法二）：调用系统API，跳转到系统设置界面
        // Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        // startActivity(intent);

        // 打开蓝牙（方法三）
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBleReceiver, intentFilter);
    }

    /**
     * 注销广播接收器
     */
    private void unregisterReceiver() {
        if (mBleReceiver != null) {
            unregisterReceiver(mBleReceiver);
        }
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                // 蓝牙状态改变
                handleBleStateChanged(intent);
            }
        }
    };

    /**
     * 处理蓝牙状态改变
     */
    private void handleBleStateChanged(Intent intent) {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
        JLog.e(TAG, "bleStateChanged state = " + state);
        switch (state) {
            case BluetoothAdapter.STATE_TURNING_ON:

                // 清除记录
                clearBleDeviceList();
                break;
            case BluetoothAdapter.STATE_ON:

                // 蓝牙打开：开始扫描
                startBleScan();
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:

                // 蓝牙关闭：停止扫描
                stopBleScan();
                break;
            case BluetoothAdapter.STATE_OFF:

                // 清除记录
                clearBleDeviceList();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.tv_start:

                clearBleDeviceList();
                startBleScan();
                break;
            case R.id.tv_stop:

                stopBleScan();
                break;
            case R.id.tv_batch:

                if (!mListAdapter.isEmpty()) {
                    mNetType = NET_TYPE_BATCH;
                    showWiFiDialog(null);
                }
                break;
        }
    }

    /**
     * 开始扫描
     */
    private void startBleScan() {
        ConfigNetManager.getInstance().bleStartScan(mBleScanCallback);
    }

    /**
     * 停止扫描
     */
    private void stopBleScan() {
        ConfigNetManager.getInstance().bleStopScan();
    }

    private BleScanCallback mBleScanCallback = new BleScanCallback() {

        @Override
        public void onScanResult(final BleDevice bleDevice) {
            if (bleDevice == null) {
                return;
            }
            // JLog.e(TAG, "发现设备：" + bleDevice.toString());

            if (mBleDeviceList.contains(bleDevice)) {
                int index = mBleDeviceList.indexOf(bleDevice);
                mBleDeviceList.set(index, bleDevice);
            } else {
                mBleDeviceList.add(bleDevice);
            }

            if (mListAdapter != null) {
                mListAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 显示WiFi输入框
     */
    private void showWiFiDialog(final BleDevice bleDevice) {

        View view = View.inflate(this, R.layout.dialog_ble_wifi, null);

        final Dialog dialog = new Dialog(this, R.style.jdPromptDialog);
        dialog.setContentView(view);
        dialog.show();

        final EditText nameText = (EditText) view.findViewById(R.id.tv_name);
        nameText.setHint(getWifiSSID());
        final EditText passwordText = (EditText) view.findViewById(R.id.et_password);
        passwordText.setHint("4006183638");
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                mWifiSSID = "";
                mWifiPassword = "";
            }
        });

        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                mWifiPassword = passwordText.getText().toString();

                mWifiSSID = nameText.getText().toString();
                if (TextUtils.isEmpty(mWifiSSID)) {
                    mWifiSSID = nameText.getHint().toString();
                }

                if (TextUtils.isEmpty(mWifiSSID)) {
                    toastShort("请输入WiFi");
                    return;
                }

                mWifiPassword = passwordText.getText().toString();
                if (TextUtils.isEmpty(mWifiPassword)) {
                    mWifiPassword = passwordText.getHint().toString();
                }
                JLog.e(TAG, "mWifiSSID = *" + mWifiSSID + "* wifiPassword = " + mWifiPassword);

                // 记录开始时间
                setTimeView(0);

                if (mNetType == NET_TYPE_BATCH) {

                    // Ble配网
                    bleMultiConfig(mBleDeviceList);
                } else if (mNetType == NET_TYPE_ONE) {

                    // Ble配网
                    bleSingleConfig(bleDevice);
                }
            }
        });
    }

    /**
     * Ble配网（单个）
     */
    private void bleSingleConfig(BleDevice bleDevice) {
        if (bleDevice == null) {
            return;
        }

        stopBleScan();

        cancelLog();
        mStartTime = System.currentTimeMillis();
        ConfigNetManager.getInstance().bleSingleConfig(mWifiSSID, mWifiPassword, bleDevice, mBleConfigCallback);
    }

    /**
     * Ble配网（批量）
     */
    private void bleMultiConfig(List<BleDevice> bleList) {
        if (bleList == null || bleList.isEmpty()) {
            return;
        }

        stopBleScan();

        cancelLog();
        mStartTime = System.currentTimeMillis();
        ConfigNetManager.getInstance().bleMultiConfig(mWifiSSID, mWifiPassword, bleList, mBleConfigCallback);
    }

    /**
     * Ble配置回调
     */
    private BleConfigCallback mBleConfigCallback = new BleConfigCallback() {

        @Override
        public void onWiFiStatus(String address, final int wifiStatus) {
            if (wifiStatus == 2) {
                setTimeView(System.currentTimeMillis() - mStartTime);
            }

            if (TextUtils.isEmpty(address) || mBleDeviceList == null) {
                return;
            }

            for (int i = 0; i < mBleDeviceList.size(); i++) {
                if (address.endsWith(mBleDeviceList.get(i).getAddress())) {
                    mBleDeviceList.get(i).setWifiStatus(wifiStatus);
                    mListAdapter.notifyDataSetChanged();
                    break;
                }
            }

        }

        @Override
        public void onDebugLog(String log) {
            appendLog(log);
        }

        @Override
        public void onScanResult(List<WiFiScanDevice> deviceList) {
            if (deviceList == null || deviceList.size() == 0) {
                return;
            }

            // 测试：临时停止配网，根据具体业务自行处理
            ConfigNetManager.getInstance().stopBleConfig();

            appendLog("WiFi设备发现 cont = " + deviceList.size());
            for (WiFiScanDevice device : deviceList) {
                appendLog("mac = " + device.getMac() + " feedId = " + device.getFeedid());
            }

            // 激活绑定设备
            ActivateManager.getInstance().activateBindDevice(deviceList, mBindCallback);
        }

        @Override
        public void onConfigFailed(String error) {

        }
    };

    /**
     * 激活绑定回调
     */
    private BindCallback mBindCallback = new BindCallback() {

        @Override
        public void onSuccess(WiFiScanDevice device, BindResult bindResult) {

        }

        @Override
        public void onError(WiFiScanDevice device, String error) {

        }

        @Override
        public void onFailure(WiFiScanDevice device, String response) {

        }
    };

    /**
     * 蓝牙设备列表适配器
     */
    private class BleListAdapter extends BaseAdapter {

        private Context mContext;

        public BleListAdapter(Context context) {

            mContext = context;
        }

        @Override
        public int getCount() {
            return mBleDeviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBleDeviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = View.inflate(mContext, R.layout.item_ble_wifi, null);
                viewHolder = new ViewHolder();
                viewHolder.iconView = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.nameView = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.macView = (TextView) view.findViewById(R.id.tv_mac);
                viewHolder.statusView = (TextView) view.findViewById(R.id.tv_status);
                viewHolder.addView = (TextView) view.findViewById(R.id.tv_add);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final BleDevice bleDevice = mBleDeviceList.get(position);
            viewHolder.nameView.setText(bleDevice.getDevice().getName());

            if (mBleDeviceList.get(position).getChipType() == 1) {

                viewHolder.macView.setVisibility(View.VISIBLE);
                viewHolder.macView.setText(bleDevice.getAddress());

                viewHolder.statusView.setVisibility(View.VISIBLE);
                int wifiStatus = mBleDeviceList.get(position).getWifiStatus();
                viewHolder.statusView.setText("WifiStatus：" + getWifiStatus(wifiStatus));
                viewHolder.statusView.setTextColor(getStatusViewTextColor(wifiStatus));
            } else {
                viewHolder.macView.setVisibility(View.GONE);
                viewHolder.statusView.setVisibility(View.GONE);
            }

            viewHolder.addView.setVisibility(View.VISIBLE);

            if (mBleDeviceList.get(position).getWifiStatus() == 2) {
                viewHolder.addView.setVisibility(View.INVISIBLE);
            }

            view.setBackgroundResource(R.color.white);

            viewHolder.addView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            mNetType = NET_TYPE_ONE;

                            // 显示WiFi输入框，开始配网
                            showWiFiDialog(bleDevice);
                            break;
                    }

                    return false;
                }
            });

            return view;
        }

        /**
         * 0-disconnected
         * 1-connecting
         * 2-connected
         * 3-未扫描到对应的路由
         * 4-因其他原因连接失败
         */
        private String getWifiStatus(int status) {

            String wifiStatus;
            switch (status) {
                case 0:

                    wifiStatus = "未连接";
                    break;
                case 1:

                    wifiStatus = "连接中...";
                    break;
                case 2:

                    wifiStatus = "已连接";
                    break;
                case 3:

                    wifiStatus = "未扫描到对应的路由";
                    break;
                case 4:
                default:

                    wifiStatus = "因其他原因连接失败";
                    break;
            }

            return wifiStatus;
        }

        private int getStatusViewTextColor(int status) {
            int color = 0xFFFF5C5C;
            if (status == 2) {

                // 已完成
                color = 0xFF1d81fc;
            }
            return color;
        }

    }

    private static class ViewHolder {
        ImageView iconView;
        TextView nameView;
        TextView macView;
        TextView statusView;
        TextView addView;
    }

}
