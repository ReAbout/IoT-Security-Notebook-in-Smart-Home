package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangchangan on 2017/3/9.
 */

public class ObjInOut implements Serializable {

    private String name;
    private String type;
    private List<String> range;

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

    public List<String> getRange() {
        return range;
    }

    public void setRange(List<String> range) {
        this.range = range;
    }
}
