package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.ifttt.widget.ArrayWheelAdapter;
import com.jd.smartcloudmobilesdk.demo.ifttt.widget.WheelView;
import com.jd.smartcloudmobilesdk.demo.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengmin1 on 2017/1/4.
 */

public class SceneTimerDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private View iv_close;
    private WheelView wv_timing_hour;
    private WheelView wv_timing_minute;
    private View tv_sure;

    private View ll_days;
    private View v_monday;
    private View v_tuesday;
    private View v_wednesday;
    private View v_thursday;
    private View v_friday;
    private View v_saturday;
    private View v_sunday;
    private View v_repeat;//
    private View tv_once;//仅执行一次该智能场景
    private String time;
    private SureClickListener sureClickListener;

    public interface SureClickListener {
        public void sureClick(String time);
    }

    public SceneTimerDialog(Context context) {
        super(context, R.style.jdPromptDialog1);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.scene_timer_set);
        this.setCanceledOnTouchOutside(false);
        initView();
        initData();
    }

    public void setTime(String time, SureClickListener sureClickListener) {
        this.time = time;
        this.sureClickListener = sureClickListener;
    }

    private void initView() {
        iv_close = findViewById(R.id.iv_close);
        wv_timing_hour = (WheelView) findViewById(R.id.wv_timing_hour);
        wv_timing_minute = (WheelView) findViewById(R.id.wv_timing_minute);
        tv_sure = findViewById(R.id.tv_sure);

        ll_days = findViewById(R.id.ll_days);
        v_monday = findViewById(R.id.v_monday);
        v_tuesday = findViewById(R.id.v_tuesday);
        v_wednesday = findViewById(R.id.v_wednesday);
        v_thursday = findViewById(R.id.v_thursday);
        v_friday = findViewById(R.id.v_friday);
        v_saturday = findViewById(R.id.v_saturday);
        v_sunday = findViewById(R.id.v_sunday);
        v_repeat = findViewById(R.id.v_repeat);
        tv_once = findViewById(R.id.tv_once);

        tv_sure.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        v_repeat.setOnClickListener(this);
        v_monday.setOnClickListener(this);
        v_tuesday.setOnClickListener(this);
        v_wednesday.setOnClickListener(this);
        v_thursday.setOnClickListener(this);
        v_friday.setOnClickListener(this);
        v_saturday.setOnClickListener(this);
        v_sunday.setOnClickListener(this);

        initTimingWheelView();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.DialogBottom);

        wv_timing_minute.setCurrentItem(0);
        wv_timing_hour.setCurrentItem(0);
    }

    private void initData() {
        if (TextUtils.isEmpty(time)) {
            tv_once.setVisibility(View.GONE);
            ll_days.setVisibility(View.VISIBLE);
            v_repeat.setSelected(true);
            v_monday.setSelected(true);
            v_tuesday.setSelected(true);
            v_wednesday.setSelected(true);
            v_thursday.setSelected(true);
            v_friday.setSelected(true);
            v_saturday.setSelected(true);
            v_sunday.setSelected(true);
        } else {
            Map<String, String> map = TimeUtils.getShowTitleWithTimeValue(time);
            if (map != null) {
                wv_timing_minute.setCurrentItem(Integer.parseInt(map.get(TimeUtils.LOCAL_TIME_MINUTE)));
                wv_timing_hour.setCurrentItem(Integer.parseInt(map.get(TimeUtils.LOCAL_TIME_HOUR)));
                if (map.containsKey(TimeUtils.LOCAL_SHOW_WEEK)) {
                    tv_once.setVisibility(View.GONE);
                    ll_days.setVisibility(View.VISIBLE);
                    v_repeat.setSelected(true);
                    String[] weeks = map.get(TimeUtils.LOCAL_SHOW_WEEK).split(",");
                    for (int i = 0; i < weeks.length; i++) {
                        if (R.mipmap.everyday == Integer.parseInt(weeks[i])) {
                            v_monday.setSelected(true);
                            v_tuesday.setSelected(true);
                            v_wednesday.setSelected(true);
                            v_thursday.setSelected(true);
                            v_friday.setSelected(true);
                            v_saturday.setSelected(true);
                            v_sunday.setSelected(true);
                        }
                        if (R.mipmap.weekday == Integer.parseInt(weeks[i])) {
                            v_monday.setSelected(true);
                            v_tuesday.setSelected(true);
                            v_wednesday.setSelected(true);
                            v_thursday.setSelected(true);
                            v_friday.setSelected(true);
                        }
                        if (R.mipmap.weekend == Integer.parseInt(weeks[i])) {
                            v_saturday.setSelected(true);
                            v_sunday.setSelected(true);
                        }

                        if (R.mipmap.monday == Integer.parseInt(weeks[i])) {
                            v_monday.setSelected(true);
                        }
                        if (R.mipmap.tuesday == Integer.parseInt(weeks[i])) {
                            v_tuesday.setSelected(true);
                        }
                        if (R.mipmap.wednesday == Integer.parseInt(weeks[i])) {
                            v_wednesday.setSelected(true);
                        }
                        if (R.mipmap.thursday == Integer.parseInt(weeks[i])) {
                            v_thursday.setSelected(true);
                        }
                        if (R.mipmap.friday == Integer.parseInt(weeks[i])) {
                            v_friday.setSelected(true);
                        }
                        if (R.mipmap.saturday == Integer.parseInt(weeks[i])) {
                            v_saturday.setSelected(true);
                        }
                        if (R.mipmap.sunday == Integer.parseInt(weeks[i])) {
                            v_sunday.setSelected(true);
                        }
                    }
                } else {
                    tv_once.setVisibility(View.VISIBLE);
                    ll_days.setVisibility(View.GONE);
                    v_repeat.setSelected(false);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_repeat:
                if (v_repeat.isSelected()) {
                    tv_once.setVisibility(View.VISIBLE);
                    ll_days.setVisibility(View.GONE);
                } else {
                    tv_once.setVisibility(View.GONE);
                    ll_days.setVisibility(View.VISIBLE);
                    v_monday.setSelected(true);
                    v_tuesday.setSelected(true);
                    v_wednesday.setSelected(true);
                    v_thursday.setSelected(true);
                    v_friday.setSelected(true);
                    v_saturday.setSelected(true);
                    v_sunday.setSelected(true);
                }
                v_repeat.setSelected(!v_repeat.isSelected());
                break;
            case R.id.v_monday:
            case R.id.v_tuesday:
            case R.id.v_wednesday:
            case R.id.v_thursday:
            case R.id.v_friday:
            case R.id.v_saturday:
            case R.id.v_sunday:
                v.setSelected(!v.isSelected());
                break;
            case R.id.iv_close:
                SceneTimerDialog.this.dismiss();
                break;
            case R.id.tv_sure:
                sureClick();
                break;
        }
    }

    private void sureClick() {
        ArrayList<String> weekArray = new ArrayList<>();
        if (v_repeat.isSelected()) {
            if (v_sunday.isSelected()) {
                weekArray.add("0");
            }
            if (v_monday.isSelected()) {
                weekArray.add("1");
            }
            if (v_tuesday.isSelected()) {
                weekArray.add("2");
            }
            if (v_wednesday.isSelected()) {
                weekArray.add("3");
            }
            if (v_thursday.isSelected()) {
                weekArray.add("4");
            }
            if (v_friday.isSelected()) {
                weekArray.add("5");
            }
            if (v_saturday.isSelected()) {
                weekArray.add("6");
            }
        }
        HashMap<String, String> timeMap = TimeUtils.generateLocalTimeWithTime(wv_timing_hour.getCurrentItem() + ":" + wv_timing_minute.getCurrentItem(), weekArray);
        if (sureClickListener != null) {
            sureClickListener.sureClick(timeMap.get(TimeUtils.LOCAL_TIME_VALUE));
        }
        SceneTimerDialog.this.dismiss();
    }

    /**
     * 初始化定时时间选择器
     */
    private void initTimingWheelView() {
        String[] h = new String[24];
        for (int i = 0; i < 24; i++) {
            h[i] = i < 10 ? "0" + i : i + "";
        }

        String[] m = new String[60];
        for (int i = 0; i < 60; i++) {
            m[i] = i < 10 ? "0" + i : i + "";
        }

        ArrayWheelAdapter<String> adapterH = new ArrayWheelAdapter<String>(context, h) {
            @Override
            protected void configureTextView(TextView view) {
                super.configureTextView(view);
                view.setGravity(Gravity.CENTER);
                view.setPadding(50, 10, 0, 10);
            }
        };

        adapterH.setTextSize(20);
        adapterH.setTextColor(R.color.text_timer);

        wv_timing_hour.setViewAdapter(adapterH);
        wv_timing_hour.setBeautyFlag(true);
        wv_timing_hour.setCyclic(true);
        wv_timing_hour.setBeautyResources(R.mipmap.wheel_center2, R.color.text_timer, R.color.normal_color);

        ArrayWheelAdapter<String> adapterM = new ArrayWheelAdapter<String>(context, m) {
            @Override
            protected void configureTextView(TextView view) {
                super.configureTextView(view);
                view.setGravity(Gravity.CENTER);
                view.setPadding(0, 10, 80, 10);
            }
        };

        adapterM.setTextSize(20);
        adapterH.setTextColor(R.color.text_timer);

        wv_timing_minute.setViewAdapter(adapterM);
        wv_timing_minute.setBeautyFlag(true);
        wv_timing_minute.setCyclic(true);
        wv_timing_minute.setBeautyResources(R.mipmap.wheel_center2, R.color.text_timer, R.color.normal_color);
    }
}
