package com.ac1d.rsbot.falconry.data;

import org.powerbot.script.Identifiable;

public enum FalconItem implements Identifiable {
    GLOVE_FALCON(10024),
    GLOVE_EMPTY(10023),

    BONES(526, "Bury", "Bones"),
    SPOTTED_KEBBIT_FUR(10125, "Drop", "Spotted kebbit fur"),
    // TODO: get IDs of the other furs
    ;

    public final int id;
    public final String name;
    public final String action;

    public static final FalconItem[] DROP_ITEMS = {BONES, SPOTTED_KEBBIT_FUR};

    FalconItem(int id) {
        this(id, null, null);
    }

    FalconItem(int id, String action, String name) {
        this.id = id;
        this.action = action;
        this.name = name;
    }

    public static FalconItem get(int id) {
        for(FalconItem fi : values()) {
            if(id == fi.id) {
                return fi;
            }
        }
        return null;
    }

    public static boolean shouldDrop(int id) {
        for(FalconItem item : DROP_ITEMS) {
            if(id == item.id) return true;
        }
        return false;
    }

    @Override
    public int id() {
        return id;
    }
}
