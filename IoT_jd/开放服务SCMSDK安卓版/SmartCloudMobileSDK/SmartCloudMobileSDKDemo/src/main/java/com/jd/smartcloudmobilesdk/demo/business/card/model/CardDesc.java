package com.jd.smartcloudmobilesdk.demo.business.card.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by yangchangan on 2018/5/2.
 */
public class CardDesc implements Serializable {

    private String unit;
    private String stream_id;
    private String stream_text;
    private String current_value;
    private List<Map<String, String>> options;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStream_text() {
        return stream_text;
    }

    public void setStream_text(String stream_text) {
        this.stream_text = stream_text;
    }

    public String getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(String current_value) {
        this.current_value = current_value;
    }

    public List<Map<String, String>> getOptions() {
        return options;
    }

    public void setOptions(List<Map<String, String>> options) {
        this.options = options;
    }
}
