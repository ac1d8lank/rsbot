package com.ac1d.rsbot.iceskate;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
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
            final Player p = ctx.players.local();
            g.drawString("Task: " + mManager.currentTask() + " RINK?: " + SkateManager.RINK.contains(p) + " INNER?: " + SkateManager.INNER.contains(p) + " PENG?: " + mManager.mHitPenguin, 20, 20);
        }
    }

}
