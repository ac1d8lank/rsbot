package com.ac1d.rsbot.util;

import com.ac1d.rsbot.util.stats.SkillTrak;
import org.powerbot.script.*;
import org.powerbot.script.ClientContext;
import org.powerbot.script.rt6.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class AcidScript<C extends ClientContext> extends PollingScript<C> implements PaintListener, MessageListener, MouseListener {

    private Task<C> mCurrentTask;

    private boolean mRunning = false;

    public AcidScript() {
        Script.Manifest mani = getClass().getAnnotation(Script.Manifest.class);
        if(mani != null) {
            AcidGUI.setTitle(mani.name());
        }
    }

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
        if(ctx instanceof org.powerbot.script.rt6.ClientContext) {
            SkillTrak.onPoll((org.powerbot.script.rt6.ClientContext)ctx);
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
        if(!(g instanceof Graphics2D)) return;
        final Graphics2D g2d = (Graphics2D) g;
        if(!mRunning) {
            return;
        }
        onGUI();
        onDraw(g2d);
        if(mCurrentTask != null) {
            mCurrentTask.onDraw(ctx, g2d);
        }
    }

    // Why, AWT?
    @Override
    public void mouseClicked(MouseEvent e) {
        AcidGUI.onMouseEvent(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        AcidGUI.onMouseEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        AcidGUI.onMouseEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        AcidGUI.onMouseEvent(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        AcidGUI.onMouseEvent(e);
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
