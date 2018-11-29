package com.zhgame.animalkindom.tools;

public class NetMessage {

    public static String DANGER = "danger";
    public static String SUCCESS = "success";
    public static String WARNING = "warning";
    public static String INFO = "info";

    public static String STATUS_ACCOUNT_ERROR = "login_account_error";
    public static String STATUS_ACCOUNT_INVALID = "account_invalid";
    public static String STATUS_INVALID_OPERATION = "invalid_operation";
    public static String STATUS_LOGIN_STATUS_ERROR = "login_status_error";
    public static String STATUS_OK = "ok";
    public static String LAND_MOVE_WAIT = "land_move_wait";
    public static String NOT_ENOUGH_SATIETY = "not_enough_satiety";
    public static String NOT_ENOUGH_VIGOUR = "not_enough_vigour";
    public static String BAG_FULL = "bag_full";
    public static String UNREAD_MSG = "unread_msg";
    public static String WAIT_FOR_ANSWER = "wait_for_answer";

    private String type;
    private String content;
    private Object data;

    public NetMessage() {

    }

    public NetMessage(String content, String type) {
        this.content = content;
        this.type = type;
    }

    public NetMessage(String content, String type, Object data) {
        this.content = content;
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
