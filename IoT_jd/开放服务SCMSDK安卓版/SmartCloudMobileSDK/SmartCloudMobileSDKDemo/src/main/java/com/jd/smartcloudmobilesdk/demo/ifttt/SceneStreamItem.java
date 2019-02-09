package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;

/**
 * Created by yangchangan on 2017/3/9.
 */
public class SceneStreamItem extends FrameLayout {

    public SceneStreamItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.scene_stream_item, this);
        TextView nameView = (TextView) view.findViewById(R.id.tv_name);
        nameView.setText("设备名");
    }

}
