
package com.jd.smartcloudmobilesdk.demo.scene.model;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {

    private String value_type;
    private String comparison_opt;
    private String product_id;
    private String value;
    private String stream_id;
    private Integer type;
    private String feed_id;
    private String mode;
    private String dev_status;
    private String p_img_url;
    private String device_name;
    private String stream_name;
    private List<Object> value_des;

    private Integer id;
    private String description;
    private String symbol;

    /**
     * @return the symbol 单位
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the keyValue
     */
    public String getKeyValue() {
        return keyValue;
    }

    /**
     * @param keyValue the keyValue to set
     */
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    private String keyValue; // 离散型value

    /**
     * 触发高级模式 回显值
     */
    private String choose_value_description;

    /**
     * @return the choose_value_description
     */
    public String getChoose_value_description() {
        return choose_value_description;
    }

    /**
     * @param choose_value_description the choose_value_description to set
     */
    public void setChoose_value_description(String choose_value_description) {
        this.choose_value_description = choose_value_description;
    }

    /**
     * 高级模式String类型值回显
     */
    private String echo_value;

    /**
     * @return the echo_value
     */
    public String getEcho_value() {
        return echo_value;
    }

    /**
     * @param echo_value the echo_value to set
     */
    public void setEcho_value(String echo_value) {
        this.echo_value = echo_value;
    }

    /**
     * @return the value_des
     */
    public List<Object> getValue_des() {
        return value_des;
    }

    /**
     * @param value_des the value_des to set
     */
    public void setValue_des(List<Object> value_des) {
        this.value_des = value_des;
    }

    /**
     * @return the stream_name
     */
    public String getStream_name() {
        return stream_name;
    }

    /**
     * @param stream_name the stream_name to set
     */
    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
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
     * @return the value_type
     */
    public String getValue_type() {
        return value_type;
    }

    /**
     * @param value_type the value_type to set
     */
    public void setValue_type(String value_type) {
        this.value_type = value_type;
    }

    /**
     * @return the comparison_opt
     */
    public String getComparison_opt() {
        return comparison_opt;
    }

    /**
     * @param comparison_opt the comparison_opt to set
     */
    public void setComparison_opt(String comparison_opt) {
        this.comparison_opt = comparison_opt;
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
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the stream_id
     */
    public String getStream_id() {
        return stream_id;
    }

    /**
     * @param stream_id the stream_id to set
     */
    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
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
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the dev_status
     */
    public String getDev_status() {
        return dev_status;
    }

    /**
     * @param dev_status the dev_status to set
     */
    public void setDev_status(String dev_status) {
        this.dev_status = dev_status;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
