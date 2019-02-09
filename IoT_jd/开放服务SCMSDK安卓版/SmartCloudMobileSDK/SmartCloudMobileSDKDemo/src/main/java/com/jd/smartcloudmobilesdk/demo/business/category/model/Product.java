package com.jd.smartcloudmobilesdk.demo.business.category.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2018/6/5.
 */
public class Product implements Serializable {

    private int id;
    private String cid;
    private String name;
    private String img_url;
    private String product_uuid;
    private String[] product_models;
    private int config_type;
    private String brand_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

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

    public String getProduct_uuid() {
        return product_uuid;
    }

    public void setProduct_uuid(String product_uuid) {
        this.product_uuid = product_uuid;
    }

    public String[] getProduct_models() {
        return product_models;
    }

    public void setProduct_models(String[] product_models) {
        this.product_models = product_models;
    }

    public int getConfig_type() {
        return config_type;
    }

    public void setConfig_type(int config_type) {
        this.config_type = config_type;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }
}
