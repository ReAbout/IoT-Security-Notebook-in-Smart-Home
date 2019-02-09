package com.jd.smartcloudmobilesdk.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.smartcloudmobilesdk.demo.utils.ActivityManagerUtil;
import com.jd.smartcloudmobilesdk.demo.utils.LoadingDialog;

/**
 * Activity基类
 * Created by yangchangan on 2017/2/24.
 */
public class BaseActivity extends Activity {
    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;

    // 加载loading
    protected static LoadingDialog mLoadingDialog;

    // 调试log
    protected TextView mLogView;
    protected ScrollView mLogScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        ActivityManagerUtil.pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dismissLoadingDialog();
        ActivityManagerUtil.popActivity(this);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.main_translatex100to0, R.anim.main_translatex0tof100);
    }

    protected void toastShort(String text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            if (!mLoadingDialog.isShowing()) {
                try {
                    mLoadingDialog.show();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            try {
                mLoadingDialog.cancel();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        mLoadingDialog = null;
    }

    protected void cancelLog() {
        if (mLogView != null) {
            mLogView.setText("");
        }
    }

    protected void appendLog(String log) {
        if (mLogView != null) {
            mLogView.append("\n" + log + "\n");
        }
    }

    protected void setLogText(String log) {
        if (mLogView != null) {
            mLogView.setText(log);
        }
    }

    protected void setLogViewVisibility() {
        if (mLogScrollView == null) {
            return;
        }

        if (mLogScrollView.getVisibility() == View.GONE) {
            mLogScrollView.setVisibility(View.VISIBLE);
        } else if (mLogScrollView.getVisibility() == View.VISIBLE) {
            mLogScrollView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取Wifi的SSID
     */
    protected String getWifiSSID() {
        String wifiSSID = "";
        try {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String infoStr = wifiInfo.toString();
            String ssidStr = wifiInfo.getSSID();
            if (infoStr.contains(ssidStr)) {
                wifiSSID = ssidStr;
            } else {
                wifiSSID = ssidStr.replaceAll("\"", "") + "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return wifiSSID;
    }
}
