package com.jd.smartcloudmobilesdk.demo.scene.model;

import java.io.Serializable;
import java.util.List;

/**
 * 执行记录详情数据模型
 * Created by yangchangan on 2017/6/27.
 */
public class SceneRecordDetail implements Serializable {

    private String feed_id;
    private int status;
    private String startTime;
    private String device_name;
    private String device_delete;
    private List<SceneStream> streams;

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

    public String getDevice_delete() {
        return device_delete;
    }

    public void setDevice_delete(String device_delete) {
        this.device_delete = device_delete;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SceneStream> getStreams() {
        return streams;
    }

    public void setStreams(List<SceneStream> streams) {
        this.streams = streams;
    }
}
