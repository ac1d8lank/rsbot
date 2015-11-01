package com.ac1d.rsbot.util.stats;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class StatTrak {
    public static StatTrak HOURLY = new StatTrak(60 * 60 * 1000);

    private HashMap<String, Queue<Event>> eventQueues = new HashMap<>();
    private Queue<Event> tempQueue = new LinkedList<>();
    private int interval;

    private StatTrak(int interval) {
        this.interval = interval;
    }

    public synchronized void addEvent(String name) {
        addEvent(name, 1);
    }

    public synchronized void addEvent(String name, int count) {
        final Queue<Event> queue = getOrCreateQueue(name);
        purge(queue);
        queue.add(new Event(count));
    }

    public synchronized int getCount(String name) {
        final Queue<Event> events = getOrCreateQueue(name);
        purge(events);
        return count(events);
    }

    public synchronized int getAverage(String name) {
        final Queue<Event> events = getOrCreateQueue(name);
        purge(events);

        final Event oldest = events.peek();
        if(oldest == null) {
            return 0;
        }

        final long now = System.currentTimeMillis();

        return (int) ((count(events) * interval) / (now - oldest.timestamp));
    }

    private synchronized int count(Queue<Event> events) {
        int total = 0;
        for(Event e : events) {
            total += e.count;
        }
        return total;
    }

    private synchronized Queue<Event> getOrCreateQueue(String name) {
        final Queue<Event> queue;
        if(!eventQueues.containsKey(name)) {
            queue = new LinkedList<>();
            eventQueues.put(name, queue);
        } else {
            queue = eventQueues.get(name);
        }
        return queue;
    }

    private synchronized void purge(Queue<Event> events) {
        final long now = System.currentTimeMillis();
        if(events != null) {
            while (events.size() > 1 && now - events.peek().timestamp >= interval) {
                events.poll();
            }
        }
    }

    private static class Event {
        public final long timestamp = System.currentTimeMillis();
        public final int count ;

        public Event(int count) {
            this.count = count;
        }
    }
}
