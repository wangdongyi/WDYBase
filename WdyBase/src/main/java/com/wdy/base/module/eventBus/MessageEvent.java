package com.wdy.base.module.eventBus;

/**
 * 作者：王东一
 * 创建时间：2017/8/24.
 */

public class MessageEvent {
    private String message;
    private Object object;

    public MessageEvent(String message) {
        this.message = message;
    }
    public MessageEvent(String message, Object object) {
        this.message = message;
        this.object=object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
