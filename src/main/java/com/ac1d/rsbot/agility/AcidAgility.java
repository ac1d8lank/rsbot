package com.ac1d.rsbot.agility;

import com.ac1d.rsbot.util.*;
import org.powerbot.script.Area;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

import java.awt.*;

@Script.Manifest(name = "AcidAgility", description = "Agility Trainer", properties = "client=6")
public class AcidAgility extends AcidScript<ClientContext> {

    private Course mmCourse;
    private Manager mManager;

    public AcidAgility() {
        mManager = new Manager(ctx);
    }

    @Override
    public void start() {
        super.start();

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
            //TODO: pop up a dialog explaining where to start the script
            // We aren't in a course :(
            ctx.controller.stop();
            return;
        }

        for(Task<ClientContext> a : mmCourse.getActions()) {
            mManager.addTask(a);
        }
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    @Override
    public void drawUI(Graphics2D g) {
        if(mmCourse != null) {
            g.drawString("Course: "+ mmCourse.name, 5, 65);
        }
    }

    private class Manager extends CycleTaskManager<ClientContext> {
        private Task<ClientContext> mmRestTask;

        public Manager(ClientContext ctx) {
            super(ctx);

            mmRestTask = new RestHealTask();
        }

        @Override
        public Task<ClientContext> nextTask() {
            //TODO base this threshold off highest course damage
            final int hp = PlayerUtils.getHealthPercent(ctx);
            if(hp != -1 && hp <= 40) {
                return mmRestTask;
            }
            return super.nextTask();
        }
    }
}
