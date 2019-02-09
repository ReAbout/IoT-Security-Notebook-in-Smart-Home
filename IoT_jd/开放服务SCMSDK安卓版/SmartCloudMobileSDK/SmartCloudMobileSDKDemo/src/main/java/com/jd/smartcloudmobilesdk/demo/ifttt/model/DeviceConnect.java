package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.util.List;

/**
 * IFTTT设备参数
 */
public class DeviceConnect {

    private String pro_type;
    private String p_description;
    private Integer type;
    private String device_name;
    private String feed_id;
    private String p_img_url;
    private String product_id;
    private List<DeviceStream> stream;

    /**
     * @return the pro_type
     */
    public String getPro_type() {
        return pro_type;
    }

    /**
     * @param pro_type the pro_type to set
     */
    public void setPro_type(String pro_type) {
        this.pro_type = pro_type;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the device_name
     */
    public String getDevice_name() {
        return device_name;
    }

    /**
     * @param device_name the device_name to set
     */
    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    /**
     * @return the feed_id
     */
    public String getFeed_id() {
        return feed_id;
    }

    /**
     * @param feed_id the feed_id to set
     */
    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    /**
     * @return the p_description
     */
    public String getP_description() {
        return p_description;
    }

    /**
     * @param p_description the p_description to set
     */
    public void setP_description(String p_description) {
        this.p_description = p_description;
    }

    /**
     * @return the p_img_url
     */
    public String getP_img_url() {
        return p_img_url;
    }

    /**
     * @param p_img_url the p_img_url to set
     */
    public void setP_img_url(String p_img_url) {
        this.p_img_url = p_img_url;
    }

    /**
     * @return the product_id
     */
    public String getProduct_id() {
        return product_id;
    }

    /**
     * @param product_id the product_id to set
     */
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    /**
     * @return the stream
     */
    public List<DeviceStream> getStream() {
        return stream;
    }

    /**
     * @param stream the stream to set
     */
    public void setStream(List<DeviceStream> stream) {
        this.stream = stream;
    }

}
