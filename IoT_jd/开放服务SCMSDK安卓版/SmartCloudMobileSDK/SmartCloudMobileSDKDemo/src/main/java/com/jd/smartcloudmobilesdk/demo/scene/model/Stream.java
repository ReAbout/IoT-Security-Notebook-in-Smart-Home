package com.jd.smartcloudmobilesdk.demo.scene.model;

import java.io.Serializable;


public class Stream implements Serializable {
    private String current_value;
    private String master_flag;
    private String stream_id;
    private String stream_name;
    private String stream_type;
    private String units;
    private String at;
    private String value_des;
    private String ptype;
    private String tag_id;

    public Stream() {
    }

    public Stream(String current_value, String master_flag, String stream_id,
                  String stream_name, String stream_type, String units) {
        this.current_value = current_value;
        this.master_flag = master_flag;
        this.stream_id = stream_id;
        this.stream_name = stream_name;
        this.stream_type = stream_type;
        this.units = units;
    }

    public String getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(String current_value) {
        this.current_value = current_value;
    }

    public String getMaster_flag() {
        return master_flag;
    }

    public void setMaster_flag(String master_flag) {
        this.master_flag = master_flag;
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

    public String getStream_type() {
        return stream_type;
    }

    public void setStream_type(String stream_type) {
        this.stream_type = stream_type;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getValue_des() {
        return value_des;
    }

    public void setValue_des(String value_des) {
        this.value_des = value_des;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return "Stream{" +
                "current_value='" + current_value + '\'' +
                ", master_flag='" + master_flag + '\'' +
                ", stream_id='" + stream_id + '\'' +
                ", stream_name='" + stream_name + '\'' +
                ", stream_type='" + stream_type + '\'' +
                ", units='" + units + '\'' +
                ", at='" + at + '\'' +
                ", value_des='" + value_des + '\'' +
                ", ptype='" + ptype + '\'' +
                ", tag_id='" + tag_id + '\'' +
                '}';
    }
}

