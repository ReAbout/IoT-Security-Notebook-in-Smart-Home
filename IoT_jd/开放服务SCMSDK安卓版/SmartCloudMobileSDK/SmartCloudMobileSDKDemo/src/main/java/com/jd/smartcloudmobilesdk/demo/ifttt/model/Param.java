package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2017/3/9.
 */

public class Param implements Serializable {

    private String name;
    private String type;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
