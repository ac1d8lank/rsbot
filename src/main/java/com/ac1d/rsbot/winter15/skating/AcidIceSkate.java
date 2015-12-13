package com.ac1d.rsbot.winter15.skating;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import java.awt.*;

@Script.Manifest(name = "Acid Ice Skate", description = "Ice Skates at Winter Event")
public class AcidIceSkate extends AcidScript<ClientContext> {
    private SkateManager mManager;

    @Override
    public void start() {
        super.start();

        mManager = new SkateManager(ctx);
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    @Override
    public void drawUI(Graphics g) {
        super.drawUI(g);
        if(mManager != null) {
            g.drawString("Task: " + mManager.currentTask() + " SEG: "+ SkateManager.Segment.ofPlayer(ctx) + " LANE: "+ SkateManager.Lane.ofPlayer(ctx), 20, 20);

            for(SkateManager.Lane l : SkateManager.Lane.values()) {
                new SkateManager.Path(ctx, l).debugDraw(ctx, g);
            }
        }
    }

    private void drawTile(ClientContext ctx, Graphics g, Tile t) {
        if(t == null) return;
        ((Graphics2D)g).draw(t.matrix(ctx).bounds());
    }

}
