package com.jd.smartcloudmobilesdk.demo.control;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.control.utils.HtmlTitleMappings;
import com.jd.smartcloudmobilesdk.demo.control.view.HtmlTitleButton;
import com.jd.smartcloudmobilesdk.demo.control.view.PopUpMenuView;
import com.jd.smartcloudmobilesdk.demo.gateway.GatewayListActivity;
import com.jd.smartcloudmobilesdk.demo.utils.PromptDialog;
import com.jd.smartcloudmobilesdk.devicecontrol.DeviceControlManager;
import com.jd.smartcloudmobilesdk.devicecontrol.WVJBResponseCallback;
import com.jd.smartcloudmobilesdk.devicecontrol.model.Result;
import com.jd.smartcloudmobilesdk.gateway.GatewayDevice;
import com.jd.smartcloudmobilesdk.init.AppManager;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengmin1 on 2017/3/8.
 */
public class HtmlDetailActivity extends BaseActivity implements View.OnClickListener, PopUpMenuView.OnPopMenuCallBack {

    private HtmlTitleButton mButton1, mButton2, mButton3, mButton4;
    private HashMap<String, HtmlTitleButton> buttonMaps = new HashMap<>();

    private View web_title;
    private WebView webView;
    private TextView mdd_number;
    private TextView tv_title, onlineSta, device_msg_show;
    private RelativeLayout ll_prompt_msg;

    private boolean isLoadCache;
    private String feed_id;
    private Result mResult;

    //新js add
    private List menusArrayList = new ArrayList<>();
    private List list = new ArrayList<>();
    //    private List iconlist = new ArrayList<>();
//    private List callbacklist = new ArrayList<>();
    private PopUpMenuView menuView;
    private String mainRightCallBack;
    private String p_feed_id;

    private DeviceControlManager deviceControlManager;
    public static final int EDIT_DEVICE = 100;
    public static final int EDIT_NORMAL = 111;
    public static final int UNBIND_DEVICE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            feed_id = bundle.getString("feed_id");
            isLoadCache = bundle.getBoolean("load_cache", true);

            initView();
            initData();
        }
    }

    private void initView() {
        web_title = findViewById(R.id.web_title);
        webView = (WebView) findViewById(R.id.webView);
        mButton1 = (HtmlTitleButton) findViewById(R.id.button1);
        mButton1.setImage(HtmlTitleMappings.KEY_BACK);
        mButton2 = (HtmlTitleButton) findViewById(R.id.button2);
        mButton2.setVisibility(View.GONE);

        mButton3 = (HtmlTitleButton) findViewById(R.id.button3);
        mButton3.setVisibility(View.GONE);

        mButton4 = (HtmlTitleButton) findViewById(R.id.button4);
        mButton4.setNativeViewVisibility(View.VISIBLE);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);

        buttonMaps.put(HtmlTitleMappings.BUTTON1_ID, mButton1);
        buttonMaps.put(HtmlTitleMappings.BUTTON2_ID, mButton2);
        buttonMaps.put(HtmlTitleMappings.BUTTON3_ID, mButton3);
        buttonMaps.put(HtmlTitleMappings.BUTTON4_ID, mButton4);

        mdd_number = (TextView) findViewById(R.id.mdd_number);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setOnClickListener(this);
        onlineSta = (TextView) findViewById(R.id.online_sta);

        ll_prompt_msg = (RelativeLayout) findViewById(R.id.ll_prompt_msg);
        ll_prompt_msg.setOnClickListener(this);
        device_msg_show = (TextView) findViewById(R.id.device_msg_show);

        menuView = new PopUpMenuView(HtmlDetailActivity.this);

        mLogView = (TextView) findViewById(R.id.tv_log);
        mLogScrollView = (ScrollView) findViewById(R.id.sv_log);
    }

    private void initData() {
        deviceControlManager = new DeviceControlManager(HtmlDetailActivity.this, AppManager.getInstance().getUserName());
        deviceControlManager.getDeviceInfo(feed_id, isLoadCache, new DeviceControlManager.OnDeviceDataLoadListener() {
            @Override
            public void onDetailLoad(Result result) {
                if (HtmlDetailActivity.this.isFinishing() || deviceControlManager == null) {
                    return;
                }

                if (result != null) {
                    mResult = result;
                    showTitle(result);

                    // 临时添加测试log
                    setLogText(new Gson().toJson(result));

                    if (result.getH5() != null) {
                        deviceControlManager.initH5Data(webView, new DeviceControlHandler());
                    } else {
                        toastShort("暂不支持该设备");
                    }
                } else {
                    if (!deviceControlManager.isHasApplyCache()) {
                        final PromptDialog dialog = new PromptDialog(HtmlDetailActivity.this, R.style.jdPromptDialog);
                        dialog.title = "获取信息失败，请检查网络";
                        dialog.show();
                        dialog.setCancelVisible(View.GONE);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setConfirmListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                HtmlDetailActivity.this.finish();
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }

            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onSelectItem(int index) {

    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }

        super.onDestroy();
    }

    private class DeviceControlHandler implements DeviceControlManager.OnDeviceControlListener {

        @Override
        public void shareSTH(JSONObject content) {
            if (content != null) {
                String title = content.optString("title");
                String shareUrl = content.optString("shareUrl");
                String imgUrl = content.optString("imgUrl");
                String message = content.optString("message");
                Toast.makeText(HtmlDetailActivity.this, "分享到第三方", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void openUrl(String url) {
            Intent intent = new Intent(HtmlDetailActivity.this, ModelDetailHtml5Activity.class);
            intent.putExtra("url", url);
            intent.putExtra("feed_id", feed_id);
            startActivity(intent);
        }

        @Override
        public void config(String data) {
            /**
             * 隐藏显示按钮
             */
            configData(data);
        }

        @Override
        public void showLoading(boolean show) {
            if (show) {
                showLoadingDialog();
            } else {
                dismissLoadingDialog();
            }
        }

        @Override
        public void addSubDevice() {
            jumpToGatewayList();
        }

        @Override
        public void onRefresh(String p_feed_id1, String feed_id1) {
            feed_id = feed_id1;
            p_feed_id = p_feed_id1;
            initData();
        }

        @Override
        public void configActionBar(String data) {
            configActionBarData(data);
        }

        @Override
        public void finish() {
            HtmlDetailActivity.this.finish();
        }

        @Override
        public void showTitle(boolean show) {
            if (show) {
                web_title.setVisibility(View.VISIBLE);
            } else {
                web_title.setVisibility(View.GONE);
            }
        }

        @Override
        public void setNavigationBarTitle(String data) {
            setNavigationBarTitleData(data);
        }

        @Override
        public void setNavigationBarRightItem(String data) {
            showRightPopMenu(data);
        }

        @Override
        public void jumpNativePage(String data) {
            Intent intent = new Intent(HtmlDetailActivity.this, JsNativeActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }

        @Override
        public void toast(String message) {
            Toast.makeText(HtmlDetailActivity.this, message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void notice(String url) {
            if (mResult.getH5().getUrl().equals(url) || mResult.getH5().getUrl().equals(url + "#")) {
                mButton4.setNativeViewVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageError() {
            configActionBar(null);
        }

        @Override
        public void alert(String data, WVJBResponseCallback jsCallback) {
            jsAlert(data, jsCallback);
        }

        @Override
        public void onLanStatusChange(int status) {
            if (mResult != null && mResult.getDevice() != null) {
                onlineSta.setText(("1".equals(mResult.getDevice().getStatus()) || status == 0) ? "" : "设备不在线");
            }
        }
    }

    /**
     * 显示标题
     */
    private void showTitle(Result result) {
        if (result.getDevice() != null) {
            String t = result.getDevice().getDevice_name();
            if (t.length() > 10) {
                t = t.substring(0, 10) + "...";
            }
            tv_title.setText(t);
        }
    }

    /**
     * title
     *
     * @param data
     */
    private void showRightPopMenu(String data) {

        JSONObject param = null;
        try {
            param = new JSONObject(data);
            if (param == null) {
                return;
            }
            String type = (String) param.get("type");
            String placeholder = (String) param.get("placeholder");
            String mainCallback = (String) param.get("callback");

            if ("icon".equals(type)) {
                mButton4.setImage(placeholder);
            } else if ("text".equals(type)) {
                mButton4.setText(placeholder);
            }
            mainRightCallBack = mainCallback;


        } catch (JSONException e) {
            return;
        }


        if (list.size() > 0) {
            list.clear();
        }
//        if (iconlist.size() > 0) {
//            iconlist.clear();
//        }
//        if (callbacklist.size() > 0) {
//            callbacklist.clear();
//        }
        if (menusArrayList.size() > 0) {
            menusArrayList.clear();
        }

        try {
            JSONArray menusArray = param.optJSONArray("menus");
            if (menusArray != null) {
                for (int i = 0; i < menusArray.length(); i++) {
                    menusArrayList.add(menusArray.get(i));
                }

                int menusLength = menusArray.length();
                for (int i = 0; i < menusLength; i++) {

                    JSONObject obj = menusArray.getJSONObject(i);
                    String text = (String) obj.get("text");
                    String icon = (String) obj.get("icon");
                    String callback = (String) obj.get("callback");

                    if (!TextUtils.isEmpty(text)) {
                        list.add(obj.get("text"));
                    }
//                    if (!TextUtils.isEmpty(icon)) {
//                        iconlist.add(obj.get("icon"));
//                    }
//                    if (!TextUtils.isEmpty(callback)) {
//                        callbacklist.add(obj.get("callback"));
//                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void jsAlert(String data, final WVJBResponseCallback jsCallback) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            final PromptDialog dialog = new PromptDialog(HtmlDetailActivity.this, R.style.jdPromptDialog);
            dialog.title = jsonObject.optString("messageTitle");
            dialog.show();

            if (!TextUtils.isEmpty(jsonObject.optString("messageYes"))) {
                dialog.setConfirmText(jsonObject.optString("messageYes"));
            } else {
                dialog.setConfirmVisible(View.GONE);
            }

            if (!TextUtils.isEmpty(jsonObject.optString("messageNo"))) {
                dialog.setCancelText(jsonObject.optString("messageNo"));
            } else {
                dialog.setCancelVisible(View.GONE);
            }

            dialog.setConfirmListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    jsCallback.callback("1");
                    dialog.dismiss();
                }
            });
            dialog.setCancelListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    jsCallback.callback("0");
                    dialog.dismiss();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * js 新增接口：设置主副标题
     */
    private void setNavigationBarTitleData(String data) {

        JSONObject param;
        try {
            param = new JSONObject(data);
        } catch (JSONException e) {
            return;
        }

        if (param == null) {
            return;
        }

        String mainTitle = param.optString("main");
        String extraTitle = param.optString("extra");

        if (!TextUtils.isEmpty(mainTitle)) {
            if (mainTitle.length() > 10) {
                mainTitle = mainTitle.substring(0, 10) + "...";
            }
        }

        if (tv_title != null) {
            tv_title.setText(mainTitle);
        }

        if (onlineSta != null) {
            onlineSta.setText(extraTitle);
        }

    }

    /**
     * 配置actionbar显示
     *
     * @param data
     */
    private void configActionBarData(String data) {
        if (TextUtils.isEmpty(data)) {
            resetActionBar();
            return;
        }
        JSONObject param;
        try {
            param = new JSONObject(data);
        } catch (JSONException e) {
            return;
        }

        if (param == null) {
            resetActionBar();
            return;
        }
        JSONArray buttonArray = param.optJSONArray("what");
        JSONArray displayArray = param.optJSONArray("display");
        JSONArray callBackNameArray = param.optJSONArray("callBackName");
        if (buttonArray != null && buttonArray.length() > 0
                && displayArray != null && displayArray.length() > 0
                && callBackNameArray != null && callBackNameArray.length() > 0) {
            for (Map.Entry<String, HtmlTitleButton> entry : buttonMaps.entrySet()) {
                entry.getValue().setTag(null);
                entry.getValue().setVisibility(View.GONE);
            }
            for (int i = 0; i < buttonArray.length(); i++) {
                HtmlTitleButton button = buttonMaps.get(buttonArray
                        .optString(i));
                String display = displayArray.optString(i);
                String callBackName = callBackNameArray.optString(i);
                if (button != null) {
                    button.setVisibility(View.VISIBLE);
                    button.setTag(callBackName);
                    if (display.startsWith("drawable")) {
                        button.setImage(display);
                    } else {
                        button.setText(display);
                    }
                }
            }
        } else {
            // 如果没有指定任何一个button，恢复默认
            resetActionBar();
        }
    }

    private void resetActionBar() {
        for (Map.Entry<String, HtmlTitleButton> entry : buttonMaps.entrySet()) {
            entry.getValue().setTag(null);
            entry.getValue().setVisibility(View.GONE);
        }

        mButton1.setVisibility(View.VISIBLE);
        mButton1.setImage(HtmlTitleMappings.KEY_BACK);
        mButton2.setVisibility(View.GONE);
        mButton3.setVisibility(View.GONE);
        mButton4.setVisibility(View.VISIBLE);
        mButton4.setNativeViewVisibility(View.VISIBLE);
    }

    /**
     * 跳转到网关设备列表
     */
    private void jumpToGatewayList() {
        if (mResult == null || mResult.getDevice() == null) {
            return;
        }

        Intent intent = new Intent(HtmlDetailActivity.this, GatewayListActivity.class);
        GatewayDevice gatewayDevice = new GatewayDevice();
        gatewayDevice.setFeed_id(feed_id);
        gatewayDevice.setDevice_id(mResult.getDevice().getDevice_id());
        gatewayDevice.setDevice_name(mResult.getDevice().getDevice_name());
        gatewayDevice.setProduct_uuid(mResult.getProduct().getProduct_uuid());
        intent.putExtra("gateway", gatewayDevice);
        startActivity(intent);
    }

    /**
     * 配置UI的一些展示
     *
     * @param data
     */
    private void configData(String data) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            // 根据关键字来扩展
            if (jsonObject.has("showBack")) {
                if (jsonObject.getBoolean("showBack")) {
                    mButton1.setImage("drawable_back");
                }
                mButton1.setImageViewVisibility(jsonObject.getBoolean("showBack") ? View.VISIBLE : View.GONE);
            }

            if (jsonObject.has("showMore")) {
                mButton4.setNativeViewVisibility(jsonObject
                        .getBoolean("showMore") ? View.VISIBLE
                        : View.GONE);
            }

            if (jsonObject.has("showOnline")) {
                onlineSta.setText((jsonObject.optBoolean("showOnline") ? "设备不在线" : ""));
            }
            if (jsonObject.has("titletext")) {
                if (!TextUtils.isEmpty(jsonObject.optString("titletext"))) {
                    tv_title.setText(jsonObject.optString("titletext"));
                } else {
                    String title = "";
                    if (mResult.getDevice() != null) {
                        title = mResult.getDevice().getDevice_name();
                        if (title.length() > 10) {
                            title = title.substring(0, 10) + "...";
                        }
                    }
                    tv_title.setText(title);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doMore() {
        Intent intent = new Intent(HtmlDetailActivity.this, SettingActivity.class);
        intent.putExtra("imageUrl", mResult.getProduct().getP_img_url());
        intent.putExtra("name", mResult.getDevice().getDevice_name());
        intent.putExtra("feed_id", feed_id);
        intent.putExtra("main_sub_type", mResult.getDevice().getMain_sub_type());
        String isShared = mResult.getShared_info().getIsShared();// 共享字段
        intent.putExtra("isShared", Integer.parseInt(isShared));

        String shareCount = "";
        if (mResult != null && mResult.getShared_info() != null) {
            shareCount = mResult.getShared_info().getShared_count();
        }
        intent.putExtra("share_count", TextUtils.isEmpty(shareCount) ? 0 : Integer.parseInt(shareCount));

        startActivityForResult(intent, EDIT_DEVICE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_DEVICE:

                switch (resultCode) {
                    case EDIT_NORMAL:
                        if (data != null) {
                            String devName = data.getStringExtra("onRename");
                            if (!TextUtils.isEmpty(devName) && !devName.equals(mResult.getDevice().getDevice_name())) {

                                //TODO
                                deviceControlManager.changeDeviceName(devName);
                                if (devName.length() > 10) {
                                    devName = devName.substring(0, 10) + "...";
                                }
                                tv_title.setText(devName);
                            }
                        }
                        break;
                    case UNBIND_DEVICE:
                        HtmlDetailActivity.this.finish();
                        break;
                }
                break;
        }
    }

    private void doBack() {

        if (mResult == null) {
            return;
        }
        // 获取历史列表
        WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
        if (mWebBackForwardList.getCurrentItem() != null) {

            String u = mWebBackForwardList.getCurrentItem().getUrl();// getOriginalUrl();
            JLog.e("GAO",
                    "closeBP" + u + ",size=" + mWebBackForwardList.getSize());

            int ic = mWebBackForwardList.getCurrentIndex();
            if (ic > 0) {
                String u2 = mWebBackForwardList.getItemAtIndex(ic - 1).getUrl();
                JLog.e("GAO", "closeBP2: " + u2 + "$$");

                if (u2.equals(mResult.getH5().getUrl()) || u2.equals(mResult.getH5().getUrl() + "#")) {
                    // 首页
                    JLog.e("GAO", "显示BP首页2: " + u2);
                    // Show
                    // iLeft.setVisibility(View.VISIBLE);
                    mButton4.setNativeViewVisibility(View.VISIBLE);
                }

            }

            JLog.e("GAO", "closeBP1: " + u + "$$");
            if (u.equals(mResult.getH5().getUrl()) || u.equals(mResult.getH5().getUrl() + "#")
                    && !webView.canGoBack()) {
                // 首页
                JLog.e("GAO", "显示BP首页1: " + u);
                if (!TextUtils.isEmpty(p_feed_id)) {
                    feed_id = p_feed_id;
                    p_feed_id = null;
                    initData();
                    return;
                } else {
                    this.finish();
                    return;
                }
            }

            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                this.finish();
            }

        } else {
            JLog.e("GAO", "url异常则关闭退出");
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                if (v.getTag() != null && !TextUtils.isEmpty((CharSequence) v.getTag())) {
                    String action = (String) v.getTag();
                    if ("goBack".equals(action)) {
                        doBack();
                    } else if ("close".equals(action)) {
                        HtmlDetailActivity.this.finish();
                    } else if ("setting".equals(action)) {
                        doMore();
                    } else {
                        webView.loadUrl("javascript:" + action + "();");
                    }
                } else {
                    doBack();
                }
                break;
            case R.id.button2:
            case R.id.button3:
                if (v.getTag() != null && !TextUtils.isEmpty((CharSequence) v.getTag())) {
                    String action = (String) v.getTag();
                    if ("goBack".equals(action)) {
                        doBack();
                    } else if ("close".equals(action)) {
                        HtmlDetailActivity.this.finish();
                    } else if ("setting".equals(action)) {
                        doMore();
                    } else {
                        webView.loadUrl("javascript:" + action + "();");
                    }
                }
                break;
            case R.id.button4:

                if (menusArrayList.size() > 0) {
                    menuView.setList(list);
//                    menuView.setIconList(iconlist);
//                    menuView.setCallBacklist(callbacklist);
                    menuView.setOnPopItemCallBack(this);

                    menuView.initData();
                    menuView.showPopupWindow(mButton4);
                    return;
                } else {

                    if ("goBack".equals(mainRightCallBack)) {
                        doBack();
                        return;
                    } else if ("close".equals(mainRightCallBack)) {
                        HtmlDetailActivity.this.finish();
                        return;
                    } else if ("setting".equals(mainRightCallBack)) {
                        doMore();
                        return;
                    }
                }

                if (v.getTag() != null && !TextUtils.isEmpty((CharSequence) v.getTag())) {
                    String action = (String) v.getTag();
                    if ("goBack".equals(action)) {
                        doBack();
                    } else if ("close".equals(action)) {
                        HtmlDetailActivity.this.finish();
                    } else if ("setting".equals(action)) {
                        doMore();
                    } else {
                        webView.loadUrl("javascript:" + action + "();");
                    }
                } else {
                    doMore();
                }
                break;
            case R.id.tv_title:

                setLogViewVisibility();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mLoadingDialog != null) {
                    dismissLoadingDialog();
                    return true;
                }
                onBack();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onBack() {
        if (HtmlTitleMappings.KEY_BACK.equals(mButton1.getImageKey())) {
            onClick(mButton1);
        } else {
            doBack();
        }
    }

}
