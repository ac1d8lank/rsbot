package com.ac1d.rsbot.hallo15.data;

import com.ac1d.rsbot.util.rt6.ObjectInteractTask;

public enum HalloObject {
    BUBBLING_WATER  (100061, "Search", "Bubbling water"),
    JETTY_DOWN      (100046, "Embark", "Jetty"),
    JETTY_UP        (100047, "Disembark", "Jetty"),
    MEETING_POINT   (100084, "Guide", "Soul meeting point"),
    SOUL_RIFT       (100058, "Jump in", "Soul Rift"),
    ;

    private final int id;
    private final String action;
    private final String name;

    HalloObject(int id, String action, String option) {
        this.id = id;
        this.action = action;
        this.name = option;
    }

    public static class ObjectTask extends ObjectInteractTask {
        public ObjectTask(HalloObject o) {
            super(o.id, o.action, o.name);
        }
    }
}
