package com.jd.smartcloudmobilesdk.demo.scene.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2017/6/27.
 */
public class SceneStream implements Serializable {

    private String stream_id;
    private String stream_name;
    private String stream_name_zh;
    private String current_value;
    private String current_value_zh;
    private String real_value_zh;

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public String getStream_name_zh() {
        return stream_name_zh;
    }

    public void setStream_name_zh(String stream_name_zh) {
        this.stream_name_zh = stream_name_zh;
    }

    public String getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(String current_value) {
        this.current_value = current_value;
    }

    public String getCurrent_value_zh() {
        return current_value_zh;
    }

    public void setCurrent_value_zh(String current_value_zh) {
        this.current_value_zh = current_value_zh;
    }

    public String getReal_value_zh() {
        return real_value_zh;
    }

    public void setReal_value_zh(String real_value_zh) {
        this.real_value_zh = real_value_zh;
    }
}
