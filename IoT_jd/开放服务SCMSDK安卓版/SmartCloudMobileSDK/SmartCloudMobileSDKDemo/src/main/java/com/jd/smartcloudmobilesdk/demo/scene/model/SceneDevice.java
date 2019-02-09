package com.jd.smartcloudmobilesdk.demo.scene.model;

import java.io.Serializable;
import java.util.List;

/**
 * 支持场景设备的数据模型
 * Created by yangchangan on 2017/7/3.
 */
public class SceneDevice implements Serializable {
    private int type;
    private String pro_type;
    private String product_id;
    private String p_img_url;
    private String p_description;
    private String feed_id;
    private String device_name;
    private List<DeviceStream> stream;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPro_type() {
        return pro_type;
    }

    public void setPro_type(String pro_type) {
        this.pro_type = pro_type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getP_img_url() {
        return p_img_url;
    }

    public void setP_img_url(String p_img_url) {
        this.p_img_url = p_img_url;
    }

    public String getP_description() {
        return p_description;
    }

    public void setP_description(String p_description) {
        this.p_description = p_description;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public List<DeviceStream> getStream() {
        return stream;
    }

    public void setStream(List<DeviceStream> stream) {
        this.stream = stream;
    }
}
