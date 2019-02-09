package com.jd.smartcloudmobilesdk.demo.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.jd.smartcloudmobilesdk.devicecontrol.DeviceService;

/**
 * Created by pengmin1 on 2017/3/8.
 */
public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                State state = networkInfo.getState();
                if (state == State.CONNECTED) {
                    // restartService(context);
//                    Toast.makeText(context, "网络已连接", Toast.LENGTH_SHORT).show();
                }
                if (state == State.DISCONNECTED) {
//                    Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {//这个监听网络连接的设置，包括wifi和移动数据 的打开和关闭
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                if (State.CONNECTED == info.getState()) {

                } else if (info.getType() == 1) {
//                    if (State.DISCONNECTING == info.getState())
//                        Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    /**
     * 网络连接重置长连接
     *
     * @param context
     */
    private void restartService(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DeviceService.class);
        intent.setAction(DeviceService.NETWORK_STATUS);
        context.startService(intent);
    }
}

 