package com.ac1d.rsbot.util;

import org.powerbot.script.*;

import java.awt.*;

public abstract class AcidScript<C extends ClientContext> extends PollingScript<C> implements PaintListener, MessageListener {

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
        if(!(g instanceof Graphics2D)) return;
        final Graphics2D g2d = (Graphics2D) g;
        if(!mRunning) {
            return;
        }
        onGUI();
        onDraw(g2d);
        if(mCurrentTask != null) {
            mCurrentTask.debugDraw(ctx, g2d);
        }
    }

    public void onGUI() {
    }

    public void onDraw(Graphics2D g2d) {
        AcidGUI.draw(g2d);
    }

    public abstract TaskManager<C> getTaskManager();

    public Task<C> getNextTask(TaskManager<C> manager) {
        // TODO: skip tasks if they are unfinished, but don't get caught in a loop if all are invalid
        return manager.nextTask();
    }
}
