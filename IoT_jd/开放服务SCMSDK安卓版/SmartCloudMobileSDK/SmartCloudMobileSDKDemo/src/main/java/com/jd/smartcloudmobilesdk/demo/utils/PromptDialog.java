package com.jd.smartcloudmobilesdk.demo.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;

/**
 *
 */
public class PromptDialog extends Dialog implements View.OnClickListener {
    public String msg;
    public String title;
    public View bodyView;
    public String hint_cancel, hint_confirm;

    public PromptDialog(Context context, int style) {
        super(context, style);
    }

    public PromptDialog(Context context, int style, View view) {
        super(context, style);
        this.bodyView = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.prompt_dialog);
        this.setCanceledOnTouchOutside(true);
        initLayout();
    }

    private int gravity = -1;

    public void setMsgGravity(int gravity) {
        this.gravity = gravity;
    }

    private TextView title_view;
    private TextView msg_view;
    private TextView cancel;
    private TextView confirm;

    private void initLayout() {
        title_view = (TextView) findViewById(R.id.title);

        if (!TextUtils.isEmpty(title)) {
            title_view.setText(title);
            title_view.setVisibility(View.VISIBLE);
        }

        msg_view = (TextView) findViewById(R.id.msg);
        if (!TextUtils.isEmpty(msg)) {
            msg_view.setText(msg);
            msg_view.setVisibility(View.VISIBLE);
            if (gravity != -1) {
                msg_view.setGravity(gravity);
            }
        }

        if (bodyView != null) {
            ViewGroup custom_view = ((ViewGroup) findViewById(R.id.custom_view));
            custom_view.addView(bodyView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            custom_view.setVisibility(View.VISIBLE);
        }

        cancel = (TextView) findViewById(R.id.cancel);
        if (hint_cancel != null) {
            cancel.setText(hint_cancel);
        }
        cancel.setOnClickListener(this);

        confirm = (TextView) findViewById(R.id.confirm);
        if (hint_confirm != null) {
            confirm.setText(hint_confirm);
        }
        confirm.setOnClickListener(this);
    }

    private View.OnClickListener cancelListener;
    private View.OnClickListener confirmListener;

    public void setCancelListener(
            View.OnClickListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void setCancelVisible(int visible) {
        cancel.setVisibility(visible);
        if (visible == View.INVISIBLE || visible == View.GONE)
            findViewById(R.id.middle_line).setVisibility(View.GONE);
    }

    public void setCancelText(String txt) {
        cancel.setText(txt);
    }

    public void setConfirmText(String txt) {
        confirm.setText(txt);
    }

    public void setConfirmTextColor(int color) {
        confirm.setTextColor(color);
    }

    public void setCancelTextColor(int color) {
        cancel.setTextColor(color);
    }

    public TextView getCancel() {
        return cancel;
    }

    public TextView getConfirm() {
        return confirm;
    }


    public TextView getTitle_view() {
        return title_view;
    }

    public TextView getMsg_view() {
        return msg_view;
    }

    public void setConfirmVisible(int visible) {
        confirm.setVisibility(visible);
        if (visible == View.INVISIBLE || visible == View.GONE)
            findViewById(R.id.middle_line).setVisibility(View.GONE);
    }

    public void setConfirmListener(
            View.OnClickListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.cancel:
                if (cancelListener != null)
                    cancelListener.onClick(arg0);
                else
                    dismiss();
                break;
            case R.id.confirm:
                if (confirmListener != null)
                    confirmListener.onClick(arg0);
                break;
            default:
                break;
        }
    }

}
