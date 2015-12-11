package com.ac1d.rsbot.util.rt6;


import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Widget;

public class ComponentInteractTask extends InteractTask<Component> {
    private final int mWidget;
    private final int[] mComponents;

    public ComponentInteractTask(String action, int widget, int... components) {
        super(action, null);
        mWidget = widget;
        mComponents = components;
    }

    @Override
    public boolean isReady(ClientContext ctx) {
        return getEntity(ctx).visible() && super.isReady(ctx);
    }

    @Override
    protected Component getEntity(ClientContext ctx) {
        final Widget w = ctx.widgets.widget(mWidget);
        Component c = null;
        for (int cId : mComponents) {
            if (c == null) {
                c = w.component(cId);
            } else {
                c = c.component(cId);
            }
        }
        return c;
    }
}
