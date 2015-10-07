package com.ac1d.rsbot.util;

import com.ac1d.rsbot.agility.AcidAgility;
import org.powerbot.script.ClientContext;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;

import java.awt.*;

public abstract class AcidScript<C extends ClientContext> extends PollingScript<C> implements PaintListener {
    /*
     * Might want to make this a stack?
     * If override returns a RETRY, would we want to retry both? Or are we okay with knocking it out
     * If we knock it out, make sure to reset any kind of retry counter
     */
    private Task<C> mNextTickTask;

    private long mPollCount = 0;

    protected String status;
    private boolean mRunning = false;

    @Override
    public void start() {
        status = "Starting";
        mRunning = true;
    }

    @Override
    public void suspend() {
        status = "Suspended";
        mRunning = false;
    }

    @Override
    public void resume() {
        status = "Resuming";
        mRunning = true;
    }

    @Override
    public void stop() {
        status = "Stopping";
        mRunning = false;
    }

    @Override
    public void poll() {
        mPollCount++;
        if(Random.percent(95) && (mPollCount % 2 == 0 || Random.percent(5))) {
            // Usually skip every other poll
            return;
        }

        Task<C> currentTask;
        //TODO: see if the manager has an override first
        if(mNextTickTask != null) {
            currentTask = mNextTickTask;
            mNextTickTask = null;
        } else {
            currentTask = nextTask();
        }

        //TODO only check each task once (break after complete cycle)
        while(mRunning && currentTask != null) {
            if(currentTask.onCooldown()) {
                currentTask = nextTask();
                continue;
            }

            final Task.TickResult result = currentTask.tick(ctx);

            switch(result.getState()) {
                case DONE:
                    status = result.getDescription();
                    currentTask = null;
                    break;
                case SKIP:
                    currentTask = nextTask();
                    break;
                case RETRY:
                    mNextTickTask = currentTask;
                    currentTask = null;
                    break;
            }
        }
    }

    private Task<C> nextTask() {
        return getTaskManager().nextTask();
    }

    @Override
    public void repaint(Graphics g) {
        if(!mRunning) {
            return;
        }
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
        if(status != null) {
            g.drawString("Status: "+ status, 5, 45);
        }
    }

    public abstract TaskManager<C> getTaskManager();
}
