package com.ac1d.rsbot.util;

import com.ac1d.rsbot.util.imported.Lodestone;
import org.powerbot.script.ClientContext;

public class LodestoneTask<C extends ClientContext> extends Task<C> {
    private final Lodestone mLode;

    public LodestoneTask(Lodestone lode) {
        mLode = lode;
    }

    @Override
    public boolean isReady(C ctx) {
        return false;
    }

    @Override
    public void onPoll(C ctx) throws FailureException {

    }

    @Override
    public boolean isDone(C ctx) {
        return false;
    }
}
