package com.ac1d.rsbot.agility;

import com.ac1d.rsbot.util.*;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

import java.awt.*;

@Script.Manifest(name = "AcidAgility", description = "Agility Trainer")
public class AcidAgility extends AcidScript<ClientContext> {

    private Manager mManager;

    public AcidAgility() {
        mManager = new Manager(ctx);
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

    private class Manager extends CycleTaskManager<ClientContext> {
        private Course mmCourse;

        public Manager(ClientContext ctx) {
            super(ctx);

            Player p = ctx.players.local();
            for(Course c : Course.ALL) {
                if(c.courseArea.contains(p)) {
                    mmCourse = c;
                    break;
                }
            }

            if(mmCourse == null) {
                // We aren't in a course :(
                stop();
                return;
            }

            for(Course.Obstacle o : mmCourse.getObstacles()) {
                addTask(new Course.ObstacleTask(ctx, o));
            }
        }
    }
}
