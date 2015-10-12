package com.ac1d.rsbot.util;

import org.powerbot.script.*;

import java.awt.*;

public abstract class AcidScript<C extends ClientContext> extends PollingScript<C> implements PaintListener, MessageListener {

    /** Acid on the eyes */
    public static final Font SANS = new Font("Comic Sans MS", Font.BOLD, 20);

    /*
     * Might want to make this a stack?
     * If override returns a RETRY, would we want to retry both? Or are we okay with knocking it out
     * If we knock it out, make sure to reset any kind of retry counter
     */
    private Task<C> mCurrentTask;

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
    public final void poll() {
        mPollCount++;
        if(Random.percent(95) && (mPollCount % 2 == 0 || Random.percent(5))) {
            // Usually skip every other poll
            return;
        }

        final TaskManager<C> manager = getTaskManager();

        if(mCurrentTask == null) {
            mCurrentTask = manager.nextTask();
            if(mCurrentTask == null) {
                return;
            }
            if(!mCurrentTask.isReady(ctx)) {
                handleTaskFailure();
                return;
            }
            mCurrentTask.onStart(ctx);
        }

        try {
            mCurrentTask.onPoll(ctx);
        } catch(Task.FailureException e) {
            handleTaskFailure();
            return;
        }

        if(mCurrentTask.isDone(ctx)) {
            handleTaskSuccess();
        }
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        if(mCurrentTask != null) {
            mCurrentTask.onMessage(messageEvent);
        }
    }

    private void handleTaskSuccess() {
        getTaskManager().onTaskSuccess(mCurrentTask);
        mCurrentTask.onFinish();
        mCurrentTask = null;
    }

    private void handleTaskFailure() {
        getTaskManager().onTaskFail(mCurrentTask);
        mCurrentTask.onFinish();
        mCurrentTask = null;
    }

    @Override
    public final void repaint(Graphics g) {
        if(!mRunning) {
            return;
        }
        drawUI(g);
    }

    public void drawUI(Graphics g) {
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 420, 70);

        g.setColor(Color.black);
        g.setFont(SANS);
        g.drawString("AcidScript", 5, 25);
        if(status != null) {
            g.drawString("Status: "+ status, 5, 45);
        }
    }

    public abstract TaskManager<C> getTaskManager();
}
