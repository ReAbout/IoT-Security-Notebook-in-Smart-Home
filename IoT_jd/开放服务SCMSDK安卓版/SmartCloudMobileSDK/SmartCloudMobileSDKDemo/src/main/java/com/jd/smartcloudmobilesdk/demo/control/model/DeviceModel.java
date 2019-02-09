package com.jd.smartcloudmobilesdk.demo.control.model;

import com.google.gson.annotations.Expose;
import com.jd.smartcloudmobilesdk.devicecontrol.model.DevDetailModel;

import java.io.Serializable;
import java.util.List;

public class DeviceModel implements Serializable {

    /**
     * 类型名称
     */
    @Expose
    private String type_name;

    /**
     * 类型
     */
    @Expose
    private String pro_type;

    /**
     * 设备列表
     */
    @Expose
    private List<DevDetailModel> list;

    /**
     * 类型描述
     */
    @Expose
    private String type_desc;

    /**
     * list数量
     */
    @Expose
    private String count;

    /**
     * 图片地址
     */
    @Expose
    private String c_img_url;

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getPro_type() {
        return pro_type;
    }

    public void setPro_type(String pro_type) {
        this.pro_type = pro_type;
    }

    public List<DevDetailModel> getList() {
        return list;
    }

    public void setList(List<DevDetailModel> list) {
        this.list = list;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getC_img_url() {
        return c_img_url;
    }

    public void setC_img_url(String c_img_url) {
        this.c_img_url = c_img_url;
    }
}
