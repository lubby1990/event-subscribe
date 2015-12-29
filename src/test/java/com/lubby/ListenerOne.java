package com.lubby;

import com.lubby.Listener.Listener;
import com.lubby.event.Event;

/**
 * Created by liubin on 2015-12-29.
 */
public class ListenerOne implements Listener {
    public void doWork(Event event) {
        System.out.println(event.getEventName() + " send to listener one");
    }
}
