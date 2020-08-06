package com.mgtj.airadio.base.event;

/**
 * Created by wanglei on 2016/12/22.
 */

public class BusProvider {

    private static EventBusImpl bus;

    public static EventBusImpl getEventBus() {
        if (bus == null) {
            synchronized (BusProvider.class) {
                if (bus == null) {
                    bus = new EventBusImpl();
                }
            }
        }
        return bus;
    }

}
