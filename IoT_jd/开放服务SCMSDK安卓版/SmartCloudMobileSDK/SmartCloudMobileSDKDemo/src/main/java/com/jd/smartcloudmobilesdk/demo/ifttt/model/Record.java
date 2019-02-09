package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2017/3/21.
 */

public class Record implements Serializable {

    private String script_id;
    private String logic_id;
    private String logic_name;
    private String record_id;

    private int status;
    private long start_time;
    private long end_time;

    public String getScript_id() {
        return script_id;
    }

    public void setScript_id(String script_id) {
        this.script_id = script_id;
    }

    public String getLogic_id() {
        return logic_id;
    }

    public void setLogic_id(String logic_id) {
        this.logic_id = logic_id;
    }

    public String getLogic_name() {
        return logic_name;
    }

    public void setLogic_name(String logic_name) {
        this.logic_name = logic_name;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
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
