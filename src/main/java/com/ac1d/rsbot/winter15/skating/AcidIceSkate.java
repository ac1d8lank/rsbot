package com.ac1d.rsbot.winter15.skating;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import java.awt.*;

@Script.Manifest(name = "Acid Ice Skate", description = "Ice Skates at the 2015 Christmas Event (Live and Let Slide)", properties = "client=6;hidden=false;vip=false;")
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
            g.drawString("Task: " + mManager.currentTask() + " SEG: "+ Segment.ofPlayer(ctx) + " LANE: "+ Lane.ofPlayer(ctx), 20, 20);

            for(Lane l : Lane.values()) {
                new Path(ctx, l).debugDraw(ctx, g);
            }
        }
    }
}
