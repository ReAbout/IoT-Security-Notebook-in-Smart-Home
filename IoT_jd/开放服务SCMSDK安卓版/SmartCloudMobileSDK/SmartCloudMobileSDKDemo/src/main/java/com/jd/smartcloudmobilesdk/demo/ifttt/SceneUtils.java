package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.jd.smartcloudmobilesdk.demo.ifttt.model.Action;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Condition;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Event;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Logic;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Obj;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.ObjInOut;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Param;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.Script;
import com.jd.smartcloudmobilesdk.init.JDSmartSDK;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengmin1 on 2017/1/12.
 */

public class SceneUtils {

    private String TAG = "SceneUtils";

    public static final String SERVICE_MANUAL = "com.joylink.ui"; // 手动
    public static final String SERVICE_TIMER = "com.joylink.local"; // 定时
    public static final String SERVICE_DEVICE = "com.joylink.puid"; // 设备

    private static final String UI_SIGNAL_BUTTON = "scenarioActivated";

    /**
     * 三种event type
     */
    private static final String TYPE_SIGNAL = "signal";
    private static final String TYPE_LOCAL = "local";
    private static final String TYPE_STREAM = "stream_id";
    private static final String TYPE_PROPERTY = "property";

    private int eventId;
    private int actionId;
    private int logicId;

    private static SceneUtils instance;

    public static SceneUtils getInstance() {
        if (instance == null) {
            instance = new SceneUtils();
        }
        return instance;
    }

    public void initEventId(List<Event> eventList) {
        if (eventList != null && !eventList.isEmpty()) {
            int temp = 0;
            int tempId;
            for (Event event : eventList) {
                tempId = Integer.parseInt(event.getId().substring(1));
                if (temp < tempId) {
                    temp = tempId;
                }
            }
            eventId = temp;
        }
    }

    public void initActionId(List<Action> actionList) {
        if (actionList != null && !actionList.isEmpty()) {
            int temp = 0;
            int tempId;
            for (Action action : actionList) {
                tempId = Integer.parseInt(action.getId().substring(1));
                if (temp < tempId) {
                    temp = tempId;
                }
            }
            actionId = temp;
        }
    }

    public Event getManualEvent(String buttonId) {
        /**
         * 设置obj
         */
        Obj obj = new Obj();
        obj.setName(UI_SIGNAL_BUTTON);
        ObjInOut objInOut = new ObjInOut();
        objInOut.setName("id");
        objInOut.setType("s");

        List<ObjInOut> in = new ArrayList<>();
        in.add(objInOut);
        obj.setIn(in);

        /**
         * 设置condition
         */
        Condition condition = new Condition();
        condition.setName("id");
        condition.setType("s");
        condition.setOperator("==");
        condition.setValue(buttonId);
        List<Condition> conditionModels = new ArrayList<>();
        conditionModels.add(condition);

        /**
         * 设置Event
         */
        Event event = new Event();
        event.setId(getEventId());
        event.setService(SERVICE_MANUAL);
        event.setType(TYPE_SIGNAL);
        event.setGUID("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        event.setMember(UI_SIGNAL_BUTTON);
        event.setObj(obj);
        event.setCondition(conditionModels);

        return event;
    }

    /**
     * 获取手动执行时的event
     */
    public Event getManualEvent() {
        return getManualEvent(getUniqueScenarioId());
    }

    /**
     * 获取定时启动的Event
     *
     * @param time 定时的时间
     */
    public Event getTimerEvent(String time) {
        /**
         * 设置obj
         */
        Obj obj = new Obj();
        obj.setName("time");
        ObjInOut objInOut = new ObjInOut();
        objInOut.setName("time");
        objInOut.setType("s");
        List<ObjInOut> in = new ArrayList<>();
        in.add(objInOut);
        obj.setIn(in);

        /**
         * 设置condition
         */
        Condition condition = new Condition();
        condition.setName("time");
        condition.setType("s");
        condition.setValue(time);
        condition.setOperator("==");
        ArrayList<Condition> conditions = new ArrayList<>();
        conditions.add(condition);

        /**
         * 设置Event
         */
        Event event = new Event();
        event.setId(getEventId());
        event.setService(SERVICE_TIMER);
        event.setType(TYPE_LOCAL);
        event.setGUID("");
        event.setMember("time");
        event.setObj(obj);
        event.setCondition(conditions);

        return event;
    }

    /**
     * 获取设备Event
     *
     * @param feedId
     * @param name     eg.PM值(key)
     * @param type     i代表int，f代表float等
     * @param value    eg.PM值(value)
     * @param operator eg.大于等等
     * @return
     */
    public Event getDeviceEvent(String feedId, String name, String type, String value, String operator) {
        /**
         * 设置obj
         */
        Obj obj = new Obj();
        obj.setName(name);
        obj.setType(type);
        obj.setAccess("rw");
        ObjInOut objInOut = new ObjInOut();
        objInOut.setName(name);
        objInOut.setType(type);
        List<ObjInOut> in = new ArrayList<>();
        in.add(objInOut);
        obj.setIn(in);

        /**
         * 设置condition
         */
        Condition condition = new Condition();
        condition.setName(name);
        condition.setType(type);
        condition.setValue(value);
        condition.setOperator(operator);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);

        /**
         * 设置Event
         */
        Event event = new Event();
        event.setId(getEventId());
        event.setService(SERVICE_DEVICE);
        event.setType(TYPE_STREAM);
        event.setGUID(feedId);
        event.setMember("");
        event.setObj(obj);
        event.setCondition(conditions);

        return event;
    }

    /**
     * 获取设备Action
     *
     * @param feedId
     * @param name   eg.PM值(key)
     * @param type   i代表int，f代表float等
     * @param value  eg.PM值(value)
     * @return
     */
    public Action getAction(String feedId, String name, String type, String value) {

        /**
         * 设置obj
         */
        Obj obj = new Obj();
        obj.setName(name);
        obj.setType(type);
        obj.setAccess("rw");

        /**
         * 设置param
         */
        List<Param> params = new ArrayList<>();
        Param param = new Param();
        param.setName(name);
        param.setType(type);
        param.setValue(value);
        params.add(param);

        /**
         * 设置actionModel
         */
        Action action = new Action();
        action.setId(getActionId());
        action.setGUID(feedId);
        action.setType(TYPE_STREAM);
        action.setService(SERVICE_DEVICE);
        action.setMember(name);
        action.setObj(obj);
        action.setParam(params);
        return action;
    }

    /**
     * 组装脚本
     *
     * @param drift         间隔时间
     * @param conditionType 条件类型，目前有"满足所有条件"和“满足一个条件”
     * @param logicEn       脚本开启状态
     * @param name          场景名字
     * @param eventList     event列表
     * @param actionList    action列表，ps包括间隔时间
     * @param scriptId      脚本id，新建的时候传null
     * @return
     */
    public Script getScript(int drift, int conditionType, int logicEn, String name,
                            List<Event> eventList, List<Action> actionList, String scriptId) {
        try {
            List<Event> eventCopyList = new ArrayList<>(eventList);
            List<Action> actionCopyList = new ArrayList<>(actionList);

            /**
             * 设置events string
             */
            drift *= 1000;
            String events = "";
            for (int i = 0; i < eventCopyList.size(); i++) {
                if (0 == i) {
                    events = eventCopyList.get(i).getId();
                } else {
                    if (conditionType == SceneDriftDialog.CONDITION_ONE) {
                        events = events + "||" + eventCopyList.get(i).getId();
                    } else {
                        events = events + "&&" + eventCopyList.get(i).getId();
                    }
                }
            }

            /**
             * 设置logics
             */
            List<Logic> logicList = new ArrayList<>();
            List<Action> actionDelayList = new ArrayList<>();
            actionDelayList.addAll(actionCopyList);

            /**
             * actions的key数量代表logic数组的长度，value中list用来组装logic中的actions字符串
             */
            Map<Integer, List<Action>> actions = new LinkedHashMap<>();
            int delay = 0;
            int categoryId = 0;
            for (int j = 0; j < actionCopyList.size(); j++) {
                if ((actionCopyList.get(j).getDelayValue() > 0
                        && TextUtils.isEmpty(actionCopyList.get(j).getId()))
                        || (j == 0 && actionCopyList.get(j).getDelayValue() == 0)) {
                    categoryId++;
                    ArrayList<Action> temp = new ArrayList<>();
                    actions.put(categoryId, temp);

                    if (actionCopyList.get(j).getDelayValue() > 0) {
                        delay = actionCopyList.get(j).getDelayValue();
                        actionDelayList.remove(actionCopyList.get(j));
                        continue;
                    }
                }
                actionCopyList.get(j).setDelayValue(delay);
                actions.get(categoryId).add(actionCopyList.get(j));
            }

            Iterator iterator = actions.entrySet().iterator();
            int size = actions.entrySet().size();
            int index = 0;
            String logicId = getLogicId();
            while (iterator.hasNext()) {

                Map.Entry entry = (Map.Entry) iterator.next();
                List<Action> val = (List<Action>) entry.getValue();
                if (val.size() > 0) {

                    // 组装logic
                    Logic logic = new Logic();
                    logic.setDrift(drift);
                    logic.setId(logicId);
                    logic.setEn(logicEn);
                    logic.setNotation(name);
                    logic.setBase(true);
                    String actionStr = "";
                    for (int i = 0; i < val.size(); i++) {
                        if (0 == i) {
                            actionStr = val.get(i).getId();
                        } else {
                            actionStr = actionStr + "&&" + val.get(i).getId();

                        }
                    }
                    logic.setEvents(events);
                    logic.setActions(actionStr);
                    logic.setDelay(val.get(0).getDelayValue());
                    logic.setIndex(index);
                    logicList.add(logic);
                    if (index < size - 1) {
                        index++;
                    } else {
                        index = 0;
                    }
                    logic.setNext(index);
                }
            }

            Script script = new Script();
            if (!TextUtils.isEmpty(scriptId)) {
                script.setId(scriptId);
            } else {
                script.setId(getUniqueScriptId());
            }
            script.setEvents(eventCopyList);
            script.setActions(actionDelayList);
            script.setLogic(logicList);

            return script;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEventId() {
        return getEventOrActionId(++eventId, "e");
    }

    public String getActionId() {
        return getEventOrActionId(++actionId, "a");
    }

    public String getLogicId() {
        return getEventOrActionId(++logicId, "l");
    }

    /**
     * 获取唯一的脚本id
     * 每一个ifttt的脚本都有一个id编号，该id为32个字节，0-9字节是脚本生成者的唯一标识符的后十位， 10-19字节是1970年到现在的秒数，
     * 20-29字节是微妙数，30-31字节由0-99的随机数字符化生成。其中，唯一标识符分2类，对于IOS设备是UUID；对于Android设备是IMEI。
     */
    public String getUniqueScriptId() {
        StringBuffer buffer = new StringBuffer();

        long second = System.currentTimeMillis() / 1000;
        long microsecond = System.currentTimeMillis() * 1000;
        int random = (int) (Math.random() * 100);

        buffer.append(subMyString(getIMEI(), 10));
        buffer.append(subMyString(second + "", 10));
        buffer.append(subMyString(microsecond + "", 10));
        buffer.append(subMyString(random + "", 2));

        return buffer.toString();
    }

    private String getEventOrActionId(int id, String idStr) {
        int length = Integer.toString(id).length();
        for (int i = 0; i < 7 - length; i++) {
            idStr += "0";
        }
        idStr += id;
        return idStr;
    }

    /**
     * @param string1 要截取的string
     * @param length  截取的位数，不足补0
     * @return
     */
    private String subMyString(String string1, int length) {
        if (!TextUtils.isEmpty(string1)) {
            if (string1.length() > length) {
                return string1.substring(string1.length() - length, string1.length());
            } else if (string1.length() == length) {
                return string1;
            } else {
                String sub = "";
                for (int i = length; i > string1.length(); i--) {
                    sub += "0";
                }
                return sub + string1;
            }
        } else {
            String sub = "";
            for (int i = length; i > 0; i--) {
                sub += "0";
            }
            return sub;
        }
    }

    private String getIMEI() {
        String ret = "";
        TelephonyManager telephonyManager = (TelephonyManager) JDSmartSDK.getInstance()
                .getContext().getSystemService(Context.TELEPHONY_SERVICE);
        ret = telephonyManager.getDeviceId();
        if (ret == null) {
            return "";
        } else {
            return ret;
        }
    }

    /**
     * 生成一个全局唯一的 场景id （手动执行）用于event里面condition中的value值
     */
    public String getUniqueScenarioId() {

        StringBuffer buffer = new StringBuffer("scenario");

        long second = System.currentTimeMillis() / 1000;
        long microsecond = System.currentTimeMillis() * 1000;
        int random = (int) (Math.random() * 100);

        buffer.append(subMyString(second + "", 10));
        buffer.append(subMyString(microsecond + "", 10));
        buffer.append(subMyString(random + "", 2));

        return buffer.toString();
    }

}
