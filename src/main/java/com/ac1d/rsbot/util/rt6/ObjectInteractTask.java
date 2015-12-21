package com.ac1d.rsbot.util.rt6;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.awt.*;

public class ObjectInteractTask extends InteractTask<GameObject> {

    private final int mId;

    public ObjectInteractTask(int id, String action, String option) {
        super(action, option);
        mId = id;
    }

    @Override
    protected final GameObject findEntity(ClientContext ctx) {
        return getObject(ctx, mId);
    }

    /** Override this for other selection methods */
    protected GameObject getObject(ClientContext ctx, int id) {
        return ctx.objects.select().id(id).within(16).nearest().poll();
    }

    @Override
    public void onDraw(ClientContext ctx, Graphics2D g) {
        g.setColor(Color.yellow);
        ((Graphics2D)g).draw(getObject(ctx, mId).tile().matrix(ctx).bounds());
    }
}
