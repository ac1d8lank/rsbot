package com.ac1d.rsbot.winter15.cooking;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

import java.awt.*;

@Script.Manifest(name = "AcidXmasCooking", description = "Cooks food at 2015 Christmas Event (For Your Ice Only)", properties = "client=6;hidden=false;vip=false;")
public class AcidXmasCooking extends AcidScript<ClientContext> {
    private CookingManager mManager;

    @Override
    public void start() {
        super.start();
        mManager = new CookingManager(ctx);
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }


    @Override
    public void onDraw(Graphics2D g) {
        super.onDraw(g);
        if(mManager != null) {
            g.drawString("Task: " + mManager.currentTask() + " INST: " + CookingManager.Food.getInstruction(ctx), 20, 20);
        }
    }

}
