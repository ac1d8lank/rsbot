package com.ac1d.rsbot.agility;

import com.ac1d.rsbot.util.*;
import org.powerbot.script.Area;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

import java.awt.*;

@Script.Manifest(name = "AcidAgility", description = "Agility Trainer")
public class AcidAgility extends AcidScript<ClientContext> {

    /** Acid on the eyes */
    protected static final Font SANS = new Font("Comic Sans MS", Font.BOLD, 20);

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
        g.fillRect(0, 0, 400, 70);

        g.setColor(Color.black);
        g.setFont(SANS);
        g.drawString("AcidScript", 5, 25);
        if(state != null) {
            g.drawString("State: "+ state, 5, 45);
        }
        if(state != null && mManager.mmCourse != null) {
            g.drawString("Course: "+ mManager.mmCourse.name, 5, 65);
        }
    }

    private class Manager extends CycleTaskManager<ClientContext> {
        private Course mmCourse;

        public Manager(ClientContext ctx) {
            super(ctx);

            Player p = ctx.players.local();
            for(Course c : Course.ALL) {
                for(Area a : c.courseAreas) {
                    if (a.contains(p)) {
                        mmCourse = c;
                        break;
                    }
                }
            }

            if(mmCourse == null) {
                //TODO: make sure this works
                //TODO: pop up a dialog explaining where to start the script
                // We aren't in a course :(
                stop();
                return;
            }

            for(Course.Action a : mmCourse.getActions()) {
                addTask(new Course.CourseTask(ctx, a));
            }
        }
    }
}
