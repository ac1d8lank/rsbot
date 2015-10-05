package com.ac1d.rsbot.util;

import org.powerbot.script.ClientContext;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;

import java.awt.*;

public abstract class AcidScript<C extends ClientContext> extends PollingScript<C> implements PaintListener {

    private Task mCurrentTask;
    private long mPollCount = 0;

    protected String state;

    protected static final Font SANS = new Font("Comic Sans MS", Font.BOLD, 20);

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
            state = "Skipping tick";
            return;
        }

        if(mCurrentTask == null || mCurrentTask.isDone()) {
            mCurrentTask = getTaskManager().nextTask();
            if(mCurrentTask == null) {
                // Still nothing, so wait until next poll
                return;
            }
            mCurrentTask.reset();
        }
        state = mCurrentTask.tick();
    }

    @Override
    public void repaint(Graphics g) {
        if(g instanceof Graphics2D) {
            drawUI((Graphics2D) g);
        }
    }

    public void drawUI(Graphics2D g) {
        // STUB
    }

    public abstract TaskManager getTaskManager();
}
