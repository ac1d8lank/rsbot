package com.ac1d.rsbot.falconry;

import com.ac1d.rsbot.falconry.data.FalconItem;
import com.ac1d.rsbot.util.Task;
import org.powerbot.script.rt6.Action;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Hud;
import org.powerbot.script.rt6.Item;

public class DropTask extends Task<ClientContext> {

    @Override
    public boolean isReady(ClientContext ctx) {
        return ctx.backpack.select().count() > 26;
    }

    @Override
    public void onPoll(ClientContext ctx) throws FailureException {
        // Open inventory
        if(!ctx.hud.opened(Hud.Window.BACKPACK)) {
            ctx.hud.open(Hud.Window.BACKPACK);
            return;
        }
        // Power drop if we find them in the actionbar
        for(Action a : ctx.combatBar.actions()) {
            if(a.type() == Action.Type.ITEM
                    && FalconItem.shouldDrop(a.id())
                    && ctx.backpack.select().id(a.id()).count() > 0) {
                a.select();
                return;
            }
        }
        // Otherwise, drop manually
        for(Item i : ctx.backpack.items()) {
            if(FalconItem.shouldDrop(i.id())) {
                FalconItem fi = FalconItem.get(i.id());
                i.interact(fi.action, fi.name);
            }
        }
    }

    @Override
    public boolean isDone(ClientContext ctx) {
        return ctx.backpack.select().id(FalconItem.DROP_ITEMS).count() == 0;
    }
}
