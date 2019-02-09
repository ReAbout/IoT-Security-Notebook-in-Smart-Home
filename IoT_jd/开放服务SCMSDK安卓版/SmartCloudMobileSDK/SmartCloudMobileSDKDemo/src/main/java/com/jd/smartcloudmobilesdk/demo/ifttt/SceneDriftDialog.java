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
import android.widget.Toast;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.ifttt.widget.ArrayWheelAdapter;
import com.jd.smartcloudmobilesdk.demo.ifttt.widget.WheelView;

/**
 * Created by pengmin1 on 2017/1/3.
 */

public class SceneDriftDialog extends Dialog implements View.OnClickListener {

    public static final int CONDITION_ALL = 0;//满足所有条件
    public static final int CONDITION_ONE = 1;//满足一个条件

    private Context context;
    private View ll_content1;
    private View iv_close;
    private View fl_drift;
    private TextView tv_time;
    private TextView tv_all_condition;
    private TextView tv_one_condition;
    private View tv_sure1;

    private View ll_content2;
    private View iv_back;
    private WheelView wv_timing_minute;
    private WheelView wv_timing_sec;
    private View tv_sure2;
    private int drift;
    private int conditionType;
    private SureClickListener sureClickListener;

    public interface SureClickListener {
        void sureClick(int drift, int conditionType);
    }

    public void setSureClickListener(SureClickListener sureClickListener) {
        this.sureClickListener = sureClickListener;
    }

    public void setDrift(int drift, int conditionType) {
        this.drift = drift;
        this.conditionType = conditionType;
    }

    public SceneDriftDialog(Context context) {
        super(context, R.style.jdPromptDialog1);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.scene_drift_setting);
        this.setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        ll_content1 = findViewById(R.id.ll_content1);
        iv_close = findViewById(R.id.iv_close);
        fl_drift = findViewById(R.id.fl_drift);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_all_condition = (TextView) findViewById(R.id.tv_all_condition);
        tv_one_condition = (TextView) findViewById(R.id.tv_one_condition);
        tv_sure1 = findViewById(R.id.tv_sure1);
        ll_content2 = findViewById(R.id.ll_content2);
        iv_back = findViewById(R.id.iv_back);
        wv_timing_minute = (WheelView) findViewById(R.id.wv_timing_minute);
        wv_timing_sec = (WheelView) findViewById(R.id.wv_timing_sec);
        tv_sure2 = findViewById(R.id.tv_sure2);
        initTimingWheelView();


        fl_drift.setOnClickListener(this);
        tv_all_condition.setOnClickListener(this);
        tv_one_condition.setOnClickListener(this);
        tv_sure1.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_sure2.setOnClickListener(this);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.DialogBottom);

        wv_timing_minute.setCurrentItem(drift / 60);
        wv_timing_sec.setCurrentItem(drift % 60);
        changeConditionState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_drift:
                timeChoiceAnim();
                break;
            case R.id.tv_all_condition:
                if (conditionType != CONDITION_ALL) {
                    conditionType = CONDITION_ALL;
                    changeConditionState();
                }
                break;
            case R.id.tv_one_condition:
                if (conditionType != CONDITION_ONE) {
                    conditionType = CONDITION_ONE;
                    changeConditionState();
                }
                break;
            case R.id.tv_sure1:
                drift = wv_timing_minute.getCurrentItem() * 60 + wv_timing_sec.getCurrentItem();
                SceneDriftDialog.this.dismiss();

                if (sureClickListener != null) {
                    sureClickListener.sureClick(drift, conditionType);
                }
                break;
            case R.id.iv_back:
                timeChoiceBackAnim();
                break;
            case R.id.tv_sure2:
                timeChoiceSure();
                break;
            case R.id.iv_close:
                SceneDriftDialog.this.dismiss();
                break;
        }
    }

    private void changeConditionState() {
        switch (conditionType) {
            case CONDITION_ALL:
                tv_all_condition.setBackgroundResource(R.mipmap.trigger_checked);
                tv_all_condition.setTextColor(context.getResources().getColor(R.color.blue_459efe));
                tv_one_condition.setBackgroundResource(R.mipmap.trigger_notchecked);
                tv_one_condition.setTextColor(context.getResources().getColor(R.color.midnightgary));
                break;
            case CONDITION_ONE:
                tv_all_condition.setBackgroundResource(R.mipmap.trigger_notchecked);
                tv_all_condition.setTextColor(context.getResources().getColor(R.color.midnightgary));
                tv_one_condition.setBackgroundResource(R.mipmap.trigger_checked);
                tv_one_condition.setTextColor(context.getResources().getColor(R.color.blue_459efe));
                break;
        }
    }

    /**
     * 时间选择点击确定按钮
     */
    private void timeChoiceSure() {
        int min = wv_timing_minute.getCurrentItem();
        int sec = wv_timing_sec.getCurrentItem();
        if (0 == min && 0 == sec) {
            Toast.makeText(context, "间隔时间不能为0", Toast.LENGTH_SHORT).show();
        } else {
            timeChoiceBackAnim();
            if (min == 0) {
                tv_time.setText(sec + "秒");
            } else if (sec == 0) {
                tv_time.setText(min + "分");
            } else {
                tv_time.setText(min + "分" + sec + "秒");
            }
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
