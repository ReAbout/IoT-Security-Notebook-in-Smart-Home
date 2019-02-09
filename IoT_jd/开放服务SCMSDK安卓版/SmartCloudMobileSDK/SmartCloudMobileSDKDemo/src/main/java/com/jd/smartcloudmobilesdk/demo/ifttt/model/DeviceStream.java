package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.util.List;

public class DeviceStream {

    private String stream_id;
    private String stream_name;
    private String value_type;
    private String max_value;
    private String min_value;
    private List<Object> value_des;
    private String comparison_opt;
    private String units;
    private String symbol;
    private List<DeviceDes> manu_set;

    private boolean status;
    //回显运算符
    private String comparison;

    /**
     * 自定义时长
     */
    private Boolean customtime = false;
    /**
     * 时间与设备区分
     */
    private Boolean is_type = false;

    public Boolean getCustomtime() {
        return customtime;
    }

    public void setCustomtime(Boolean customtime) {
        this.customtime = customtime;
    }

    public Boolean getIs_type() {
        return is_type;
    }

    public void setIs_type(Boolean is_type) {
        this.is_type = is_type;
    }

    /**
     * @return the comparison
     */
    public String getComparison() {
        return comparison;
    }

    /**
     * @param comparison the comparison to set
     */
    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

    /**
     * @return the symbol
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
     * @return the deviceDes
     */
    public List<DeviceDes> getDeviceDes() {
        return manu_set;
    }

    /**
     * @param deviceDes the deviceDes to set
     */
    public void setDeviceDes(List<DeviceDes> deviceDes) {
        this.manu_set = deviceDes;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public String getValue_type() {
        return value_type;
    }

    public void setValue_type(String value_type) {
        this.value_type = value_type;
    }

    public String getMax_value() {
        return max_value;
    }

    public void setMax_value(String max_value) {
        this.max_value = max_value;
    }

    public String getMin_value() {
        return min_value;
    }

    public void setMin_value(String min_value) {
        this.min_value = min_value;
    }

    public List<Object> getValue_des() {
        return value_des;
    }

    public void setValue_des(List<Object> value_des) {
        this.value_des = value_des;
    }

    public String getComparison_opt() {
        return comparison_opt;
    }

    public void setComparison_opt(String comparison_opt) {
        this.comparison_opt = comparison_opt;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
