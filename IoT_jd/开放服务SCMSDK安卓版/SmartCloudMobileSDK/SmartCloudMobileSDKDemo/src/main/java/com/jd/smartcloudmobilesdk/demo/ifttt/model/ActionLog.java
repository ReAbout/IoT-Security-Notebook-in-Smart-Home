package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2017/3/21.
 */

public class ActionLog implements Serializable {

    private String action_id;

    private int status;
    private long start_time;
    private long end_time;

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }
}
