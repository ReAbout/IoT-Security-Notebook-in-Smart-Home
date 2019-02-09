package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangchangan on 2017/3/8.
 */
public class Event implements Serializable {

    /**
     * 事件的id，以小写字母e开头，后面以数字形式编号，脚本内部不断累加
     */
    private String id;

    /**
     * 事件来源jApp的GUID。
     * 当事件来源是signal时，GUID可以为全F，代表不关心事件不源。
     * 当事件来源是local时，GUID可以为空，代表jApp本身。
     */
    private String GUID;

    /**
     * jApp的服务名
     */
    private String service;

    /**
     * jApp的服务成员名
     */
    private String member;

    /**
     * 事件不源类型，可选的有：signal,property,function,local
     */
    private String type;

    /**
     * 当type为property或function时，此值不能为空，代表间隔多长时间调用或查询一下事件结果。单位为ms.
     */
    private int interval;

    /**
     * signal,property,function,local的原型，这部分与服务宣告出来的原型完全一致。
     */
    private Obj obj;

    /**
     * 事件达成的条件，其中通过name,value,operator描述了返回结果准确的达成条件。
     * 当事件来源是function时，在此对像中还包括了函数的输入参数的值。即在此字段，不区分输入输出，
     * 由于在任何情况下对一个原型的描述中其输入输出的参数名都不重复，因此有以下原则：
     * 当对像的name名与输入参数对应时，代表函数的输入参数值。
     * 当对像的name名与返回的结果对应时，代表结果的匹配条件。
     */
    private List<Condition> condition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Obj getObj() {
        return obj;
    }

    public void setObj(Obj obj) {
        this.obj = obj;
    }

    public List<Condition> getCondition() {
        return condition;
    }

    public void setCondition(List<Condition> condition) {
        this.condition = condition;
    }
}
