package com.ac1d.rsbot.agility;

import com.ac1d.rsbot.util.*;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

import java.awt.*;

@Script.Manifest(name = "AcidAgilitye", description = "Agility Trainer")
public class AcidAgility extends AcidScript<ClientContext> {

    private Course.Manager mManager;

    public AcidAgility() {
        mManager = new Course.Manager(ctx);
        mManager.setCourse(Course.BURTHORPE);
    }

    @Override
    public TaskManager getTaskManager() {
        return mManager;
    }

    @Override
    public void drawUI(Graphics2D g) {
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 400, 50);

        g.setColor(Color.black);
        g.setFont(SANS);
        g.drawString("AcidScript", 5, 25);
        if(state != null) {
            g.drawString("State: "+ state, 5, 45);
        }
    }
}
