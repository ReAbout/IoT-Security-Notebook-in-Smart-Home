package com.jd.smartcloudmobilesdk.demo.scene.model;

import java.io.Serializable;
import java.util.List;

/**
 * 场景详情数据模型
 * Created by yangchangan on 2017/6/27.
 */
public class SceneDetail implements Serializable {

    private String id;
    private int delay;
    private int status;
    private String images;
    private String feed_id;
    private String device_name;
    private String device_type;
    private String device_delete;
    private List<SceneStream> streams;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
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

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_delete() {
        return device_delete;
    }

    public void setDevice_delete(String device_delete) {
        this.device_delete = device_delete;
    }

    public List<SceneStream> getStreams() {
        return streams;
    }

    public void setStreams(List<SceneStream> streams) {
        this.streams = streams;
    }
}
