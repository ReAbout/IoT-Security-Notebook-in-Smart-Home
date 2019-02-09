package com.jd.smartcloudmobilesdk.demo.business.card.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2018/5/2.
 */
public class Stream implements Serializable {

    private String stream_id;
    private String current_value;

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(String current_value) {
        this.current_value = current_value;
    }

}
