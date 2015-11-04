package com.ac1d.rsbot.falconry;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.TaskManager;
import com.ac1d.rsbot.util.stats.StatTrak;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

import java.awt.Graphics;

@Script.Manifest(name = "AcidFalconry", description = "Hunts Falcons")
public class AcidFalconry extends AcidScript<ClientContext>{

    private FalconManager mManager;

    @Override
    public void start() {
        super.start();

        mManager = new FalconManager(ctx);
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        super.messaged(messageEvent);
        if(messageEvent.source() == null || messageEvent.source().isEmpty()) {
            if(messageEvent.text().contains("The falcon successfully")) {
                StatTrak.addEvent("kebbit");
            }
        }
    }

    @Override
    public void drawUI(Graphics g) {
        super.drawUI(g);
        g.drawString("Caught: "+StatTrak.TOTAL.getTotal("kebbit")+", "+StatTrak.HOURLY.getAverage("kebbit") +" k/hr", 20, 20);
    }
}
