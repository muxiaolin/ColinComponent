package com.mgtj.airadio.base.event;

/**
 *
 */

public interface IBus {

    void register(Object object);

    void registerSticky(Object object);

    void unregister(Object object);

    void post(Object event);

    void post(IEvent event);

    void postSticky(IEvent event);


    interface IEvent {
        int getTag();
    }

}
