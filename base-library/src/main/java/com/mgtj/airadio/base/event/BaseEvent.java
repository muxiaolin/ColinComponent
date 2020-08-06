package com.mgtj.airadio.base.event;

/**
 * desc:   发送事件
 */
public class BaseEvent implements IBus.IEvent {

    public int eventCode;
    public Object data;
    public Object data1;

    public BaseEvent() {

    }

    public BaseEvent(int eventCode) {
        this.eventCode = eventCode;
    }


    public BaseEvent(int eventCode, Object data) {
        this.eventCode = eventCode;
        this.data = data;
    }

    public BaseEvent(int eventCode, Object data, Object data1) {
        this.eventCode = eventCode;
        this.data = data;
        this.data1 = data1;
    }


    @Override
    public int getTag() {
        return 0;
    }
}
