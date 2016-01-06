package com.ac1d.rsbot.util.stats;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public abstract class StatTrak {
    public static TotalTrak TOTAL = new TotalTrak();
    public static TimeTrak HOURLY = new TimeTrak(60 * 60 * 1000);

    private static final StatTrak[] ALL = {TOTAL, HOURLY};


    protected abstract void add(String name, int count);
    protected void add(String name) {
        add(name, 1);
    }


    public static void addEvent(String name) {
        addEvent(name, 1);
    }

    public static void addEvent(String name, int count) {
        for(StatTrak st : ALL) {
            st.add(name, count);
        }

    }

    // Implementations:

    public static class TotalTrak extends StatTrak {
        private HashMap<String, Integer> counts = new HashMap<>();

        @Override
        public void add(String name, int count) {
            counts.put(name, getTotal(name) + count);
        }

        public int getTotal(String name) {
            return counts.containsKey(name) ? counts.get(name) : 0;
        }
    }

    public static class TimeTrak extends StatTrak {
        private HashMap<String, Queue<Event>> eventQueues = new HashMap<>();
        private int interval;

        private TimeTrak(int interval) {
            this.interval = interval;
        }

        @Override
        public synchronized void add(String name, int count) {
            final Queue<Event> queue = getOrCreateQueue(name);
            purge(queue);
            queue.add(new Event(count));
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

        private synchronized long count(Queue<Event> events) {
            long total = 0;
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
    }

    private static class Event {
        public final long timestamp = System.currentTimeMillis();
        public final int count ;

        public Event(int count) {
            this.count = count;
        }
    }
}
