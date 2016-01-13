# event-subscribe
This is a event subscribe.
first you should regist you listeners with subscribve.
when,event could call the listeners where event happens

A event subcribe module. It can register the listener with event. Then call the registered listener to do some thing you want . It is asynchronous. 
How to use :

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
