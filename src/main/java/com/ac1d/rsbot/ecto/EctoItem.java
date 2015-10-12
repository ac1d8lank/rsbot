package com.ac1d.rsbot.ecto;

public enum EctoItem {
    ECTOPHIAL   (4251),
    EMPTY_BUCKET(1925),
    SLIME_BUCKET(4286),
    EMPTY_POT   (1931),
    BONEMEAL    (4255),
    BONES       (526),
    ;

    public final int id;

    EctoItem(int id) {
        this.id = id;
    }
}
