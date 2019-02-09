package com.jd.smartcloudmobilesdk.demo.business.category.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2018/6/5.
 */
public class Category implements Serializable {

    private String cid;
    private String cname;
    private String img_url;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
