package com.jd.smartcloudmobilesdk.demo.control.utils;


import com.jd.smartcloudmobilesdk.demo.R;

import java.util.HashMap;

/**
 * Created by pengmin1 on 2017/3/8.
 */
public class HtmlTitleMappings {

    public static final String KEY_BACK = "drawable_back";
    public static final String KEY_SETTING = "drawable_setting";
    public static final String KEY_MORE = "drawable_more";
    public static final String KEY_ADD = "drawable_add";
    public static final String KEY_CLOSE = "drawable_close";

    public static final String BUTTON1_ID = "button1";
    public static final String BUTTON2_ID = "button2";
    public static final String BUTTON3_ID = "button3";
    public static final String BUTTON4_ID = "button4";

    private static HashMap<String, Integer> mMapping = new HashMap<>();

    static {
        mMapping.put(KEY_BACK, R.mipmap.icon_back);
        mMapping.put(KEY_SETTING, R.mipmap.ico_setting_03);
        mMapping.put(KEY_CLOSE, R.mipmap.web_close);
        mMapping.put(KEY_MORE, R.mipmap.title_more_icon);
        mMapping.put(KEY_ADD, R.mipmap.icon_add);
    }

    /***
     * see {@link HtmlTitleMappings} constant {@link KEY_BACK} and so on
     * @param key
     * @return
     */
    public static int getDrawable(String key) {
        if (mMapping.containsKey(key)) {
            return mMapping.get(key);
        } else {
            return 0;
        }
    }
}
