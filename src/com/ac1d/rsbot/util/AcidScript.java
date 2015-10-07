package com.ac1d.rsbot.util;

import com.ac1d.rsbot.agility.AcidAgility;
import org.powerbot.script.ClientContext;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;

import java.awt.*;

public abstract class AcidScript<C extends ClientContext> extends PollingScript<C> implements PaintListener {

    private Task<C> mCurrentTask;
    private long mPollCount = 0;

    protected String state;

    @Override
    public void start() {
        state = "Starting";
    }

    @Override
    public void suspend() {
        state = "Suspended";
    }

    @Override
    public void resume() {
        state = "Resuming";
    }

    @Override
    public void stop() {
        state = "Stopping";
    }

    @Override
    public void poll() {
        mPollCount++;
        if(Random.percent(95) && (mPollCount % 2 == 0 || Random.percent(5))) {
            // Usually skip every other poll
            return;
        }

        //TODO only check each task once (break after complete cycle)
        do {
            while(mCurrentTask == null || mCurrentTask.isDone() || mCurrentTask.onCooldown()) {
                // Ready a task for the next poll.
                mCurrentTask = getTaskManager().nextTask();
                if(mCurrentTask == null) {
                    // Still nothing, so wait until next poll
                    return;
                }
                mCurrentTask.reset();
            }
            state = mCurrentTask.tick(ctx);
        } while(mCurrentTask.wasSkipped() || mCurrentTask.onCooldown());
    }

    @Override
    public void repaint(Graphics g) {
        if(g instanceof Graphics2D) {
            drawUI((Graphics2D) g);
        }
    }

    public void drawUI(Graphics2D g) {
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 400, 70);

        g.setColor(Color.black);
        g.setFont(AcidAgility.SANS);
        g.drawString("AcidScript", 5, 25);
        if(state != null) {
            g.drawString("State: "+ state, 5, 45);
        }
    }

    public abstract TaskManager<C> getTaskManager();
}
