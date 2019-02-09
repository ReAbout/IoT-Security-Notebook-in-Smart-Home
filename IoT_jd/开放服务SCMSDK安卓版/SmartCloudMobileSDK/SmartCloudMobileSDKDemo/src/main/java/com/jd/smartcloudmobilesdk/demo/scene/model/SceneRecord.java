package com.jd.smartcloudmobilesdk.demo.scene.model;

import java.io.Serializable;

/**
 * 场景执行记录数据模型
 * Created by yangchangan on 2017/6/27.
 */
public class SceneRecord implements Serializable {

    private String recordId;
    private String name;
    private String status;
    private String startTime;
    private String endTime;
    private String exe_type;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getExe_type() {
        return exe_type;
    }

    public void setExe_type(String exe_type) {
        this.exe_type = exe_type;
    }
}
