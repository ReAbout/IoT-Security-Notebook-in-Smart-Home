package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangchangan on 2017/3/21.
 */

public class RecordDetail implements Serializable {

    private String script_id;
    private String logic_id;
    private List<ActionLog> actions;

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

    public List<ActionLog> getActions() {
        return actions;
    }

    public void setActions(List<ActionLog> actions) {
        this.actions = actions;
    }
}
