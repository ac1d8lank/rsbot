package com.ac1d.rsbot.iceskate;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

import java.awt.*;

@Script.Manifest(name = "Acid Ice Skate", description = "Ice Skating")
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
//            g.setColor(Color.BLUE);
//            final Tile playerTile = ctx.players.local().tile();
//            drawTile(ctx, g, playerTile);
//            g.setColor(Color.WHITE);
//            for(int i = 0; i < 5; i++) {
//                final SkateManager.Segment seg = SkateManager.Segment.ofPlayer(ctx);
//                final SkateManager.Lane lane = SkateManager.Lane.ofPlayer(ctx);
//                drawTile(ctx, g, seg.getTile(lane, i + seg.getIndex(playerTile)));
//            }
        }
    }

    private void drawTile(ClientContext ctx, Graphics g, Tile t) {
        if(t == null) return;
        ((Graphics2D)g).draw(t.matrix(ctx).bounds());
    }

}
