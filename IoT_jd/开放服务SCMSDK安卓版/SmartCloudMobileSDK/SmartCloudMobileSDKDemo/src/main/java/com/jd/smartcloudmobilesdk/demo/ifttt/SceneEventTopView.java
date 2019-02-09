package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.utils.TimeUtils;

import java.util.Map;

/**
 * Created by pengmin1 on 2017/1/5.
 */

public class SceneEventTopView extends FrameLayout implements View.OnClickListener {

    private View fl_timer;
    private View fl_hand;
    private LinearLayout ll_timer;//用于显示定时的时间
    private TextView tv_time;

    private View iv_arrow;
    private View iv_hand;
    private Context context;
    public boolean isHand;
    public String mTime;

    public SceneEventTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.scene_event_top_view, this, true);
        fl_timer = view.findViewById(R.id.fl_timer);
        fl_hand = view.findViewById(R.id.fl_hand);
        ll_timer = (LinearLayout) view.findViewById(R.id.ll_timer);
        iv_arrow = view.findViewById(R.id.iv_arrow);
        iv_hand = view.findViewById(R.id.iv_hand);
        tv_time = (TextView) view.findViewById(R.id.tv_time);

        fl_hand.setOnClickListener(this);
        fl_timer.setOnClickListener(this);
        showTime();
    }

    public void showTime() {
        if (TextUtils.isEmpty(mTime)) {
            return;
        }
        ll_timer.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Map<String, String> map = TimeUtils.getShowTitleWithTimeValue(mTime);
        tv_time.setText(map.get(TimeUtils.LOCAL_SHOW_TIME) + "|");
        if (map.containsKey(TimeUtils.LOCAL_SHOW_WEEK)) {
            String[] weeks = map.get(TimeUtils.LOCAL_SHOW_WEEK).split(",");
            for (int i = 0; i < weeks.length; i++) {
                ImageView view = new ImageView(context);
                view.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(weeks[i])));
                ll_timer.addView(view, lp);
            }
        } else {
            TextView textView = new TextView(context);
            textView.setTextColor(context.getResources().getColor(R.color.blue_459efe));
            textView.setTextSize(14);
            textView.setText(map.get(TimeUtils.LOCAL_SHOW_DATE));
            ll_timer.addView(textView, lp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_hand:
                isHand = !isHand;
                showHand();
                break;
            case R.id.fl_timer:
                SceneTimerDialog dialog = new SceneTimerDialog(context);
                dialog.setTime(mTime, new SceneTimerDialog.SureClickListener() {
                    @Override
                    public void sureClick(String time) {
                        mTime = time;
                        showTime();
                    }
                });
                dialog.show();
                break;
        }
    }

    public void showHand() {
        if (!isHand) {
            iv_arrow.setVisibility(View.VISIBLE);
            iv_hand.setVisibility(View.GONE);
        } else {
            iv_arrow.setVisibility(View.GONE);
            iv_hand.setVisibility(View.VISIBLE);
        }
    }
}
