package com.jd.smartcloudmobilesdk.demo.authorize;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.authorize.AuthorizeCallback;
import com.jd.smartcloudmobilesdk.authorize.AuthorizeFragment;
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
public class AuthorizeFragmentActivity extends BaseActivity implements View.OnClickListener {

    private String mState;
    private String mAppKey;
    private String mRedirectUri;

    private String mLoginType;
    private String mPhoneNumber;
    private String mScreenMatch;

    private AuthorizeFragment mAuthorizeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize_fragment);

        findViewById(R.id.iv_left).setOnClickListener(this);
        getExtraData();

        authorize();
    }

    private void getExtraData() {
        Intent intent = getIntent();
        if (intent != null) {
            mAppKey = intent.getStringExtra("appKey");
            mRedirectUri = intent.getStringExtra("redirectUri");
            mState = intent.getStringExtra("state");

            mLoginType = intent.getStringExtra("loginType");
            mPhoneNumber = intent.getStringExtra("phoneNumber");
            mScreenMatch = intent.getStringExtra("screenMatch");
        }
    }

    private void authorize() {
        mAuthorizeFragment = AuthorizeFragment.newInstance();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, mAuthorizeFragment);
        transaction.commit();

        if ("0".equals(mLoginType)) {
            ((TextView) findViewById(R.id.tv_title)).setText("账号授权-自定义样式");
            mAuthorizeFragment.authorize(mAppKey, mRedirectUri, mState, mCallback);
        } else if ("1".equals(mLoginType)) {
            ((TextView) findViewById(R.id.tv_title)).setText("短信授权-自定义样式");
            mAuthorizeFragment.smsAuthorize(mAppKey, mRedirectUri, mState, mPhoneNumber, mScreenMatch, mCallback);
        }

        mAuthorizeFragment.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                // JLog.e(TAG, "WebChromeClient onReceivedTitle title = " + title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // JLog.e(TAG, "WebChromeClient onProgressChanged newProgress = " + newProgress);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mAuthorizeFragment.goBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:

                if (!mAuthorizeFragment.goBack()) {
                    finish();
                }
                break;
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
