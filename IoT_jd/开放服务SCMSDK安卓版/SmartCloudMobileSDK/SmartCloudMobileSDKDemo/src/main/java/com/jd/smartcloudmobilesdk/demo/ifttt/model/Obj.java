package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangchangan on 2017/3/9.
 */

public class Obj implements Serializable {

    private String name;
    private String type;
    private String access;
    private List<String> range;

    private List<ObjInOut> in;
    private List<ObjInOut> out;

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

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public List<String> getRange() {
        return range;
    }

    public void setRange(List<String> range) {
        this.range = range;
    }

    public List<ObjInOut> getIn() {
        return in;
    }

    public void setIn(List<ObjInOut> in) {
        this.in = in;
    }

    public List<ObjInOut> getOut() {
        return out;
    }

    public void setOut(List<ObjInOut> out) {
        this.out = out;
    }
}
