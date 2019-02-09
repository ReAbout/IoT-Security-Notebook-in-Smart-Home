package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.ifttt.widget.ArrayWheelAdapter;
import com.jd.smartcloudmobilesdk.demo.ifttt.widget.WheelView;

/**
 * Created by pengmin1 on 2017/1/3.
 */

public class SceneDelayDialog extends Dialog implements View.OnClickListener {


    private Context context;
    private View ll_content1;
    private View iv_close;
    private View fl_drift;
    private TextView tv_time;
    private View tv_sure1;

    private View ll_content2;
    private View iv_back;
    private WheelView wv_timing_minute;
    private WheelView wv_timing_sec;
    private View tv_sure2;
    private DelayClickListener delayClickListener;

    public void setDelayClickListener(DelayClickListener delayClickListener) {
        this.delayClickListener = delayClickListener;
    }

    public interface DelayClickListener {
        void delay(int delay);
    }

    public SceneDelayDialog(Context context) {
        super(context, R.style.jdPromptDialog1);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.scene_delay_setting);
        this.setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        ll_content1 = findViewById(R.id.ll_content1);
        iv_close = findViewById(R.id.iv_close);
        fl_drift = findViewById(R.id.fl_drift);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_sure1 = findViewById(R.id.tv_sure1);
        ll_content2 = findViewById(R.id.ll_content2);
        iv_back = findViewById(R.id.iv_back);
        wv_timing_minute = (WheelView) findViewById(R.id.wv_timing_minute);
        wv_timing_sec = (WheelView) findViewById(R.id.wv_timing_sec);
        tv_sure2 = findViewById(R.id.tv_sure2);
        initTimingWheelView();


        fl_drift.setOnClickListener(this);
        tv_sure1.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_sure2.setOnClickListener(this);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.DialogBottom);

        wv_timing_minute.setCurrentItem(0);
        wv_timing_sec.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_drift:
                timeChoiceAnim();
                break;
            case R.id.tv_sure1:
                if (wv_timing_minute.getCurrentItem() * 60 + wv_timing_sec.getCurrentItem() > 0 && delayClickListener != null) {
                    delayClickListener.delay(wv_timing_minute.getCurrentItem() * 60 + wv_timing_sec.getCurrentItem());
                }
                SceneDelayDialog.this.dismiss();
                break;
            case R.id.iv_back:
                timeChoiceBackAnim();
                break;
            case R.id.tv_sure2:
                timeChoiceSure();
                break;
            case R.id.iv_close:
                SceneDelayDialog.this.dismiss();
                break;
        }
    }

    /**
     * 时间选择点击确定按钮
     */
    private void timeChoiceSure() {
        int min = wv_timing_minute.getCurrentItem();
        int sec = wv_timing_sec.getCurrentItem();
        timeChoiceBackAnim();
        if (min == 0) {
            tv_time.setText(sec + "秒");
        } else if (sec == 0) {
            tv_time.setText(min + "分");
        } else {
            tv_time.setText(min + "分" + sec + "秒");
        }
    }

    private void timeChoiceBackAnim() {
        Animation leftIn = AnimationUtils.loadAnimation(context, R.anim.main_translatexf100to0);
        Animation rightOut = AnimationUtils.loadAnimation(context, R.anim.main_translatex0to100);
        ll_content2.setAnimation(rightOut);
        ll_content1.setAnimation(leftIn);
        ll_content1.setVisibility(View.VISIBLE);
        leftIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_content2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void timeChoiceAnim() {
        Animation leftOut = AnimationUtils.loadAnimation(context, R.anim.main_translatex0tof100);
        Animation rightIn = AnimationUtils.loadAnimation(context, R.anim.main_translatex100to0);
        ll_content1.setAnimation(leftOut);
        ll_content2.setAnimation(rightIn);
        ll_content2.setVisibility(View.VISIBLE);
        leftOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_content1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    /**
     * 初始化定时时间选择器
     */
    private void initTimingWheelView() {
        String[] s = new String[60];
        String[] m = new String[60];
        for (int i = 0; i < 60; i++) {
            m[i] = i < 10 ? "0" + i : i + "";
            s[i] = i < 10 ? "0" + i : i + "";
        }

        ArrayWheelAdapter<String> adapterM = new ArrayWheelAdapter<String>(context, m) {
            @Override
            protected void configureTextView(TextView view) {
                super.configureTextView(view);
                view.setGravity(Gravity.CENTER);
                view.setPadding(50, 10, 0, 10);
            }
        };

        adapterM.setTextSize(20);
        wv_timing_minute.setViewAdapter(adapterM);
        wv_timing_minute.setBeautyFlag(true);
        wv_timing_minute.setCyclic(true);
        wv_timing_minute.setBeautyResources(R.mipmap.wheel_center2, R.color.text_timer, R.color.normal_color);

        ArrayWheelAdapter<String> adapterS = new ArrayWheelAdapter<String>(context, s) {
            @Override
            protected void configureTextView(TextView view) {
                super.configureTextView(view);
                view.setGravity(Gravity.CENTER);
                view.setPadding(0, 10, 80, 10);
            }
        };

        adapterS.setTextSize(20);
        adapterS.setTextColor(R.color.text_timer);
        wv_timing_sec.setViewAdapter(adapterS);
        wv_timing_sec.setBeautyFlag(true);
        wv_timing_sec.setCyclic(true);
        wv_timing_sec.setBeautyResources(R.mipmap.wheel_center2, R.color.text_timer, R.color.normal_color);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
