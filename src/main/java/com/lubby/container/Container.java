package com.lubby.container;

import com.lubby.Listener.Listener;
import com.lubby.event.Event;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liubin on 2015-12-29.
 */
public class Container {
    private ConcurrentHashMap<Class<? extends Event>, Set<Listener>> eventListener;
    private ExecutorService threadPool;
    private int THREAD_POOL_SIZE = 2;

    public Container() {
        eventListener = new ConcurrentHashMap<Class<? extends Event>, Set<Listener>>();
        threadPool = new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public Container(int threadPoolSize) {
        this.THREAD_POOL_SIZE = threadPoolSize;
        eventListener = new ConcurrentHashMap<Class<? extends Event>, Set<Listener>>();
        threadPool = new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * Listener regist for event
     *
     * @param eventClass the event's class
     * @param listener   the listener which regist fro event
     */
    public void register(Class<? extends Event> eventClass, Listener listener) {
        Set<Listener> listenerSet = null;
        ReentrantLock lock = new ReentrantLock();

        lock.lock();
        try {
            boolean has = eventListener.containsKey(eventClass);
            if (has) {
                listenerSet = eventListener.get(eventClass);
            } else {
                listenerSet = new HashSet<Listener>();
            }
            listenerSet.add(listener);
            eventListener.put(eventClass, listenerSet);
        } finally {
            lock.unlock();
        }
    }

    /**
     * check if this listener regists for event
     *
     * @param eventClass
     * @param listener
     * @return
     */
    public boolean contains(Class<? extends Event> eventClass, Listener listener) {
        Set<Listener> listenerSet = null;
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        boolean exist = false;

        lock.lock();
        try {
            boolean has = eventListener.containsKey(eventClass);
            if (has) {
                listenerSet = eventListener.get(eventClass);
                exist = listenerSet.contains(listener);
            }
            listenerSet.add(listener);
        } finally {
            lock.unlock();
        }
        return exist;
    }

    public void removeEventListener(Class<? extends Event> eventClass) {
        Set<Listener> listenerSet = null;
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            boolean has = eventListener.containsKey(eventClass);
            if (has) {
                eventListener.remove(eventClass);
            }
        } finally {
            lock.unlock();
        }
    }

    public void unregisterListner(Class<? extends Event> eventClass, Listener listener) {
        Set<Listener> listenerSet = null;
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            boolean has = eventListener.containsKey(eventClass);
            if (has) {
                listenerSet = eventListener.get(eventClass);
                Iterator<Listener> iterator = listenerSet.iterator();
                while (iterator.hasNext()) {
                    if (listener == iterator.next()) {
                        iterator.remove();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void call(Event event) {
        final Event localEvent = event;
        listenerProcess(event);
    }

    public void listenerProcess(Event event) {
        final Event localEvent = event;
        Set<Listener> listenerSet = eventListener.get(event.getClass());
        if (listenerSet == null || listenerSet.size() == 0) {
            return;
        } else {
            Iterator<Listener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                final Listener listener = iterator.next();
                threadPool.execute(new Runnable() {
                    public void run() {
                        listener.doWork(localEvent);
                    }
                });
            }
        }

    }


}
