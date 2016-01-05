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
    protected boolean interact(Component obj, String action, String option) {
        if(action == null) {
            return obj.click();
        } else {
            return super.interact(obj, action, option);
        }
    }

    @Override
    protected long getIdleDelayMillis() {
        return 0;
    }

    @Override
    protected Component findEntity(ClientContext ctx) {
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
