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

    private boolean mRunning = false;

    @Override
    public void start() {
        mRunning = true;
    }

    @Override
    public void suspend() {
        mRunning = false;
    }

    @Override
    public void resume() {
        mRunning = true;
    }

    @Override
    public void stop() {
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
        if(manager == null) {
            return;
        }

        if(mCurrentTask == null) {
            mCurrentTask = getNextTask(manager);
            if(mCurrentTask == null) {
                return;
            }
            mCurrentTask.onStart(ctx);
        }

        if(mCurrentTask.isDone(ctx)) {
            handleTaskSuccess();
        } else {
            try {
                mCurrentTask.onPoll(ctx);
            } catch (Task.FailureException e) {
                handleTaskFailure();
            }
        }
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        final TaskManager<C> manager = getTaskManager();
        if(manager != null) {
            manager.onMessage(messageEvent);
        }
    }

    private void handleTaskSuccess() {
        getTaskManager().onTaskSuccess(mCurrentTask);
        mCurrentTask.onFinish(true);
        mCurrentTask = null;
    }

    private void handleTaskFailure() {
        getTaskManager().onTaskFail(mCurrentTask);
        mCurrentTask.onFinish(false);
        mCurrentTask = null;
    }

    @Override
    public final void repaint(Graphics g) {
        if(!mRunning) {
            return;
        }
        drawUI(g);
        if(mCurrentTask != null) {
            mCurrentTask.debugDraw(ctx, g);
        }
    }

    public void drawUI(Graphics g) {
        // TODO: base UI here
    }

    public abstract TaskManager<C> getTaskManager();

    public Task<C> getNextTask(TaskManager<C> manager) {
        // TODO: skip tasks if they are unfinished, but don't get caught in a loop if all are invalid
        return manager.nextTask();
    }
}
