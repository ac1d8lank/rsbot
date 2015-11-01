package com.ac1d.rsbot.hallo15;

import com.ac1d.rsbot.hallo15.data.HalloArea;
import com.ac1d.rsbot.hallo15.data.HalloObject;
import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.CycleTaskManager;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.stats.StatTrak;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

import java.awt.*;

@Script.Manifest(name = "AcidHalloween2015", description = "Does the event.")
public class AcidHalloween15 extends AcidScript<ClientContext> {
    private HalloManager mManager;

    @Override
    public void start() {
        super.start();
        mManager = new HalloManager(ctx);
    }

    @Override
    public void drawUI(Graphics g) {
        String text = "";
//        text += "Instruction: "+ Instruction.get(ctx);
        text += "  Souls: "+StatTrak.HOURLY.getAverage("soul")+"/hr";
        text += "  Favour: "+StatTrak.HOURLY.getAverage("favour")+"/hr";
        text += "  Areas: "+getAreas();
        if(mManager.currentTask() != null) {
            text += "Task: " + mManager.currentTask().toString();
        }
        g.drawString(text, 0, 20);
    }

    private String getAreas() {
        String s = "";
        for(HalloArea area : HalloArea.values()) {
            if(area.containsPlayer(ctx)) {
                s += area+":";
            }
        }
        return s;
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

}
