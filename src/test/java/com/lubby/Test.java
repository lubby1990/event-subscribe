package com.lubby;


import com.lubby.Listener.Listener;
import com.lubby.container.Container;

/**
 * Created by liubin on 2015-12-29.
 */
public class Test {
    public static void main(String[] args) {

        Container container = new Container();
        Listener listenerOne = new ListenerOne();
        Listener listenertwo = new ListenerTwo();

        container.register(EventA.class, listenerOne);
        container.register(EventA.class, listenertwo);

        container.register(EventB.class, listenerOne);
        container.register(EventB.class, listenertwo);

        container.call(new EventA());

        container.call(new EventB());
    }
}
