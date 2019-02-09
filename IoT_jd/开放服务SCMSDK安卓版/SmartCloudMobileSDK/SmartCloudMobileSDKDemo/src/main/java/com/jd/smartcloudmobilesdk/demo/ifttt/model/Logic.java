package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;

/**
 * Created by yangchangan on 2017/3/9.
 */

public class Logic implements Serializable {
    /**
     * 逻辑的id，以小写字母l开头，后面以数字形式编号，脚本内部不断累加
     */
    private String id;

    /**
     * 相同id的逻辑的执行步骤序号
     */
    private int index;

    /**
     * 是否启动该条逻辑（注：逻辑链中的每条链的en值必须一致。）
     */
    private int en;

    /**
     * 逻辑的描述性标识，用于提醒用户该逻辑的用途。
     */
    private String notation;

    /**
     * 事件的表达式，index大于0的执行步骤事件表达式可以为空。
     */
    private String events;

    /**
     * 动作的表达式，next为0的执行步骤动作表达式可以为空。
     */
    private String actions;

    /**
     * 事件发生的时间窗口。当事件表达式中有event事件不唯一时，此值不应该为空。单位为ms
     */
    private int drift;

    /**
     * 下一步骤的index值。
     */
    private int next;

    /**
     * 执行下一个步骤的延时时间。
     */
    private int delay;

    private boolean base;

    public boolean isBase() {
        return base;
    }

    public void setBase(boolean base) {
        this.base = base;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getEn() {
        return en;
    }

    public void setEn(int en) {
        this.en = en;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public int getDrift() {
        return drift;
    }

    public void setDrift(int drift) {
        this.drift = drift;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
