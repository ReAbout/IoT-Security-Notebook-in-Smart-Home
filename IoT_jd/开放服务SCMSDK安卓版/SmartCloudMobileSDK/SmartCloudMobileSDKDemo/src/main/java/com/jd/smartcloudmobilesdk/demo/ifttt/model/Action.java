package com.jd.smartcloudmobilesdk.demo.ifttt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangchangan on 2017/3/9.
 */

public class Action implements Serializable {

    /**
     * 动作的id，以小写字母a开头，后面以数字形式编号，脚本内部不断累加
     */
    private String id;

    /**
     * 动作执行jApp的GUID。
     * 当GUID为空时，代表需要联动引擎查询能提供该服务的jApp,并将其做为执行动作的对像。
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
     * 动作类型，可选的有：property,function
     */
    private String type;

    /**
     * property,function的原型，这部分与服务宣告出来的原型完全一致
     */
    private Obj obj;

    /**
     * 对像的输入参数列表。
     * 由于在任何情况下对一个原型的描述中其输入输出的参数名都不重复，因此有以下原则：
     * 当对像的name名与输入参数对应时，代表函数的输入参数值。
     * 当对像的name名与property的name对应时，代表要设置的属性值。
     */
    private List<Param> param;

    /**
     * 显示时间间隔
     */
    private transient int delayValue;

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

    public Obj getObj() {
        return obj;
    }

    public void setObj(Obj obj) {
        this.obj = obj;
    }

    public List<Param> getParam() {
        return param;
    }

    public void setParam(List<Param> param) {
        this.param = param;
    }

    public int getDelayValue() {
        return delayValue;
    }

    public void setDelayValue(int delayValue) {
        this.delayValue = delayValue;
    }
}
