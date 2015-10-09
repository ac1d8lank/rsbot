package com.ac1d.rsbot.util.rt6;

import com.ac1d.rsbot.util.Task;
import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Path;

public class MoveTask extends Task<ClientContext> {

    private Area mFrom;
    private Area mTo;

    public MoveTask(Area from, Area to) {
        mFrom = from;
        mTo = to;
    }

    @Override
    public boolean isReady(ClientContext ctx) {
        return mFrom.contains(ctx.players.local());
    }

    @Override
    public void onPoll(ClientContext ctx) throws FailureException {
        if(ctx.players.local().idle()) {
            ctx.movement.findPath(mTo.getRandomTile()).traverse();
        }
    }

    @Override
    public boolean isDone(ClientContext ctx) {
        return mTo.contains(ctx.players.local());
    }
}
