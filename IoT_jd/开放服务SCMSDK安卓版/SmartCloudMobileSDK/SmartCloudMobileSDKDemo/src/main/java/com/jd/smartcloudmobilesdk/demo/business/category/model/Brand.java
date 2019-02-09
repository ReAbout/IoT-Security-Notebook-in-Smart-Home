package com.jd.smartcloudmobilesdk.demo.business.category.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2018/6/5.
 */
public class Brand implements Serializable {

    private String cid;
    private String brand_id;
    private String name;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
