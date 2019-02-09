package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangchangan on 2017/3/9.
 */

public class Script implements Serializable {

    private String id;
    private String version;
    private List<Event> events;
    private List<Action> actions;
    private List<Logic> logic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Logic> getLogic() {
        return logic;
    }

    public void setLogic(List<Logic> logic) {
        this.logic = logic;
    }
}
