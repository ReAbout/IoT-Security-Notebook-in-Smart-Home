package com.jd.smartcloudmobilesdk.demo.scene.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2017/7/3.
 */
public class DeviceDes implements Serializable {
    private Integer id;
    private boolean status;
    private String key;
    private String value;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
