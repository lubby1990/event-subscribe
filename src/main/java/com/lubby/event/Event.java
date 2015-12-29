package com.lubby.event;

/**
 * Created by liubin on 2015-12-29.
 */
public abstract class Event {
    private String eventName;

    public Event() {
    }

    public Event(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
