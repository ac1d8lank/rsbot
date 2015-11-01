package com.ac1d.rsbot.util.rt6;

import com.ac1d.rsbot.util.Task;
import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Path;
import org.powerbot.script.rt6.Player;

public class MoveTask extends Task<ClientContext> {

    private Area mFrom;
    private Area mTo;
    private boolean mMoved;

    public MoveTask(Area from, Area to) {
        mFrom = from;
        mTo = to;
    }

    @Override
    public boolean isReady(ClientContext ctx) {
        return (mFrom == null || mFrom.contains(ctx.players.local())) && ctx.movement.findPath(mTo.getRandomTile()).valid();
    }

    @Override
    public void onPoll(ClientContext ctx) throws FailureException {
        if(ctx.players.local().idle()) {
            ctx.movement.findPath(mTo.getRandomTile()).traverse();
            mMoved = true;
        }
    }

    @Override
    public boolean isDone(ClientContext ctx) {
        final Player p = ctx.players.local();
        return mMoved && p.idle() && mTo.contains(p);
    }
}
