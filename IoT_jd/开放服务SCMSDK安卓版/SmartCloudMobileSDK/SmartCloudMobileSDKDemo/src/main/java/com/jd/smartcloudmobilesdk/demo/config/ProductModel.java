package com.jd.smartcloudmobilesdk.demo.config;

import java.io.Serializable;

/**
 * 产品数据模型
 * Created by yangchangan on 2017/8/15.
 */
public class ProductModel implements Serializable {

    private String name;
    private String img_url;
    private String description;
    private int config_type;
    private String device_type;

    private int newdesc;
    private int main_sub_type;
    private boolean is_support;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getConfig_type() {
        return config_type;
    }

    public void setConfig_type(int config_type) {
        this.config_type = config_type;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public int getNewdesc() {
        return newdesc;
    }

    public void setNewdesc(int newdesc) {
        this.newdesc = newdesc;
    }

    public int getMain_sub_type() {
        return main_sub_type;
    }

    public void setMain_sub_type(int main_sub_type) {
        this.main_sub_type = main_sub_type;
    }

    public boolean is_support() {
        return is_support;
    }

    public void setIs_support(boolean is_support) {
        this.is_support = is_support;
    }
}
