package com.jd.smartcloudmobilesdk.demo.authorize;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.authorize.AuthorizeCallback;
import com.jd.smartcloudmobilesdk.authorize.AuthorizeManager;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.Config;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.net.NetManager;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangchangan on 2018/9/8.
 */
public class AuthorizeActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitleView;
    private EditText mPhoneNumText;
    private EditText mScreenRatio;
    private EditText mPageStyle;

    private boolean isAuthorizeCustomStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        findViewById(R.id.iv_left).setOnClickListener(this);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTitleView.setText("授权登录-固定样式");
        mTitleView.setOnClickListener(this);

        findViewById(R.id.tv_account).setOnClickListener(this);
        findViewById(R.id.tv_sms).setOnClickListener(this);

        mPhoneNumText = (EditText) findViewById(R.id.et_phone_num);
        mScreenRatio = (EditText) findViewById(R.id.et_screen_ratio);
        mPageStyle = (EditText) findViewById(R.id.et_page_style);
    }

    private String getPhoneNumber() {
        String phoneNum = mPhoneNumText.getText().toString();
        if (TextUtils.isEmpty(phoneNum)) {
            phoneNum = mPhoneNumText.getHint().toString();
        }
        return phoneNum;
    }

    private String getScreenMatch() {
        String screenRatio = mScreenRatio.getText().toString();
        if (TextUtils.isEmpty(screenRatio)) {
            screenRatio = mScreenRatio.getHint().toString();
        }
        String pageStyle = mPageStyle.getText().toString();
        if (TextUtils.isEmpty(pageStyle)) {
            pageStyle = mPageStyle.getHint().toString();
        }
        return screenRatio + pageStyle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                finish();
                break;
            case R.id.tv_title:

                setAuthorizeStyle();
                break;
            case R.id.tv_account:

                authorize();
                break;
            case R.id.tv_sms:

                smsAuthorize(getPhoneNumber(), getScreenMatch());
                break;
            default:
                break;
        }
    }

    private void setAuthorizeStyle() {
        isAuthorizeCustomStyle = !isAuthorizeCustomStyle;
        if (isAuthorizeCustomStyle) {
            mTitleView.setText("授权登录-自定义样式");
        } else {
            mTitleView.setText("授权登录-固定样式");
        }
    }

    private void authorize() {
        if (isAuthorizeCustomStyle) {
            // 京东账号授权 方式二：开发者可自定义授权页面的样式(标题)
            Intent intent = new Intent(this, AuthorizeFragmentActivity.class);
            intent.putExtra("appKey", Config.appKey);
            intent.putExtra("redirectUri", Config.redirectUri);
            intent.putExtra("state", Config.state);
            intent.putExtra("loginType", "0");
            startActivity(intent);
        } else {
            // 京东账号授权 方式一：授权页面在SDK内实现，不可定制页面样式
            AuthorizeManager manager = AuthorizeManager.getInstance();
            manager.authorize(Config.appKey, Config.redirectUri, Config.state, mCallback);
        }
    }

    private void smsAuthorize(String phoneNumber, String screenMatch) {
        if (isAuthorizeCustomStyle) {
            // 短信验证授权 方式二：开发者可自定义授权页面的样式(标题)
            Intent intent = new Intent(this, AuthorizeFragmentActivity.class);
            intent.putExtra("appKey", Config.appKey);
            intent.putExtra("redirectUri", Config.redirectUri);
            intent.putExtra("state", Config.state);
            intent.putExtra("loginType", "1");
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("screenMatch", screenMatch);
            startActivity(intent);
        } else {
            // 短信验证授权 方式一：授权页面在SDK内实现，不可定制页面样式
            AuthorizeManager manager = AuthorizeManager.getInstance();
            manager.smsAuthorize(Config.appKey, Config.redirectUri, Config.state, phoneNumber, screenMatch, mCallback);
        }
    }

    /**
     * 注册token到SDK
     */
    private void registerAccessToken(String accessToken) {
        AuthorizeManager.getInstance().registerAccessToken(accessToken);
    }

    private AuthorizeCallback mCallback = new AuthorizeCallback() {
        @Override
        public void onResponse(String code, String state) {
            JLog.e(TAG, "authorize code = " + code + " state = " + state);
            getAccessToken(code, state);
        }
    };

    /**
     * 获取token，此处访问开发者服务器，切记替换成自己的网络请求库！
     */
    private void getAccessToken(String code, String state) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("state", state);

        // 切记替换成自己的网络请求库（仅为演示流程）
        NetManager.get(Config.redirectUri, map, new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "getAccessToken onSuccess response = " + response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject dataObject = responseObject.optJSONObject("data");
                    if (dataObject != null) {
                        String accessToken = dataObject.optString("access_token");
                        if (!TextUtils.isEmpty(accessToken)) {
                            toastShort("授权成功");

                            registerAccessToken(accessToken);
                        } else {
                            toastShort("授权失败");
                        }
                    } else {
                        toastShort("授权失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShort("授权失败");
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "getAccessToken onFailure response = " + response);
                toastShort("授权失败");
            }
        });
    }
}
