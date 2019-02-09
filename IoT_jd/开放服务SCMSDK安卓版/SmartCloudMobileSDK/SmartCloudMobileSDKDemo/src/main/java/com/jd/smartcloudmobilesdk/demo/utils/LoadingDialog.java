package com.jd.smartcloudmobilesdk.demo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.jd.smartcloudmobilesdk.demo.R;

public class LoadingDialog extends ProgressDialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadingView mLoadView = new LoadingView(getContext());
        mLoadView.setDrawableResId(R.mipmap.loading);
        setContentView(mLoadView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.dimAmount = 0.0f;
        getWindow().setAttributes(lp);

        setCancelable(false);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
