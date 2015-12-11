package com.ac1d.rsbot.impcooking;

import com.ac1d.rsbot.iceskate.SkateManager;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

import java.awt.*;

@Script.Manifest(name = "Acid Imp Cooking", description = "Cooks at winter event")
public class AcidImpCooking extends AcidScript<ClientContext> {
    private CookingManager mManager;

    @Override
    public void start() {
        super.start();
        mManager = new CookingManager(ctx);
    }

    @Override
    public TaskManager getTaskManager() {
        return mManager;
    }


    @Override
    public void drawUI(Graphics g) {
        super.drawUI(g);
        g.drawString("Task: "+mManager.currentTask()+" INST: "+CookingManager.Food.getInstruction(ctx), 20, 20);
    }

}
