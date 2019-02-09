package com.jd.smartcloudmobilesdk.demo.scene.model;

import java.io.Serializable;
import java.util.List;

/**
 * 场景列表数据模型
 * Created by yangchangan on 2017/6/27.
 */
public class SceneListModel implements Serializable {

    // 场景id
    private String id;

    // 场景名称
    private String name;

    // 场景状态
    private String status;

    // 场景开始时间
    private String startTime;

    // 场景结束时间
    private String endTime;

    private List<String> images;

    // 定时任务，下一次执行场景时间
    private String next_exe_time;
    private String next_exe_time_express;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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
}
