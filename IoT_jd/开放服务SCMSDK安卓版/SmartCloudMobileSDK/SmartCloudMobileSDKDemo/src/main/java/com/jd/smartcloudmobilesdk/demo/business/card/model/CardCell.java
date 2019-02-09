package com.jd.smartcloudmobilesdk.demo.business.card.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangchangan on 2018/5/2.
 */
public class CardCell implements Serializable {

    private int card_id;
    private int seq;
    private int cid;
    private int card_type;
    private String feed_id;
    private String scene_id;
    private String card_name;

    // 广域网在线状态
    private String status;

    // 局域网在线状态 1-在线 0-离线
    public String lan_status;

    private String share_from;
    private String create_time;

    // 场景定时
    private boolean show_timer_guide;
    private String next_exe_time;
    private String next_exe_time_express;

    private String device_id;
    private String p_img_url;
    private String product_id;
    private String product_uuid;
    private String ble_protocol;
    private String deviceId_ble;
    private String[] stream_type_list;

    private List<String> c_img_url;
    private List<Stream> snapshot;
    private List<CardDesc> card_desc;
    private List<CardDesc> card_control;
    private List<CardDesc> add_card_desc;
    private List<CardDesc> add_card_control;

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCard_type() {
        return card_type;
    }

    public void setCard_type(int card_type) {
        this.card_type = card_type;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getScene_id() {
        return scene_id;
    }

    public void setScene_id(String scene_id) {
        this.scene_id = scene_id;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLan_status() {
        return lan_status;
    }

    public void setLan_status(String lan_status) {
        this.lan_status = lan_status;
    }

    public String getShare_from() {
        return share_from;
    }

    public void setShare_from(String share_from) {
        this.share_from = share_from;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public boolean isShow_timer_guide() {
        return show_timer_guide;
    }

    public void setShow_timer_guide(boolean show_timer_guide) {
        this.show_timer_guide = show_timer_guide;
    }

    public String getNext_exe_time() {
        return next_exe_time;
    }

    public void setNext_exe_time(String next_exe_time) {
        this.next_exe_time = next_exe_time;
    }

    public String getNext_exe_time_express() {
        return next_exe_time_express;
    }

    public void setNext_exe_time_express(String next_exe_time_express) {
        this.next_exe_time_express = next_exe_time_express;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getP_img_url() {
        return p_img_url;
    }

    public void setP_img_url(String p_img_url) {
        this.p_img_url = p_img_url;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_uuid() {
        return product_uuid;
    }

    public void setProduct_uuid(String product_uuid) {
        this.product_uuid = product_uuid;
    }

    public String getBle_protocol() {
        return ble_protocol;
    }

    public void setBle_protocol(String ble_protocol) {
        this.ble_protocol = ble_protocol;
    }

    public String getDeviceId_ble() {
        return deviceId_ble;
    }

    public void setDeviceId_ble(String deviceId_ble) {
        this.deviceId_ble = deviceId_ble;
    }

    public String[] getStream_type_list() {
        return stream_type_list;
    }

    public void setStream_type_list(String[] stream_type_list) {
        this.stream_type_list = stream_type_list;
    }

    public List<String> getC_img_url() {
        return c_img_url;
    }

    public void setC_img_url(List<String> c_img_url) {
        this.c_img_url = c_img_url;
    }

    public List<Stream> getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(List<Stream> snapshot) {
        this.snapshot = snapshot;
    }

    public List<CardDesc> getCard_desc() {
        return card_desc;
    }

    public void setCard_desc(List<CardDesc> card_desc) {
        this.card_desc = card_desc;
    }

    public List<CardDesc> getCard_control() {
        return card_control;
    }

    public void setCard_control(List<CardDesc> card_control) {
        this.card_control = card_control;
    }

    public List<CardDesc> getAdd_card_desc() {
        return add_card_desc;
    }

    public void setAdd_card_desc(List<CardDesc> add_card_desc) {
        this.add_card_desc = add_card_desc;
    }

    public List<CardDesc> getAdd_card_control() {
        return add_card_control;
    }

    public void setAdd_card_control(List<CardDesc> add_card_control) {
        this.add_card_control = add_card_control;
    }
}
