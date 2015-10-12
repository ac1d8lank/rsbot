package com.ac1d.rsbot.ecto;

import com.ac1d.rsbot.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Withdraws/deposits items from bank until inventory has a certain count
 */
public class BankTask extends Task<ClientContext> {

    private final HashMap<Integer, Range> mTargets = new HashMap<>();

    private final Set<Integer> mTempSet = new HashSet<>();

    public void addTarget(EctoItem item, int count) {
        mTargets.put(item.id, new Range(count));
    }

    public void addTarget(EctoItem item, int min, int max) {
        mTargets.put(item.id, new Range(min, max));
    }

    @Override
    public boolean isReady(ClientContext ctx) {
        return EctoArea.BANK.area.contains(ctx.players.local());
    }

    @Override
    public void onPoll(ClientContext ctx) throws FailureException {
        int item = getFirstMismatch(ctx);
        if(item == -1) {
            if(ctx.bank.opened()) {
                ctx.bank.close();
            }
            return;
        } else if(!ctx.bank.opened()) {
            ctx.bank.open();
            return;
        }

        final int invCount = ctx.backpack.select().id(item).count();
        Range target = mTargets.get(item);
        if(target == null) {
            // This isn't an item we want in our inventory, remove it
            target = new Range(0);
        }

        // TODO: check bank count, fail if we can't hit min

        if(invCount > target.max) {
            ctx.bank.deposit(item, invCount - target.max);
        }
        if(invCount < target.max) {
            System.out.println("Withdrawing "+(target.max - invCount)+"x #"+item);
            ctx.bank.withdraw(item, target.max - invCount);
        }
    }

    @Override
    public boolean isDone(ClientContext ctx) {
        return getFirstMismatch(ctx) == -1 && !ctx.bank.opened();
    }

    private int getFirstMismatch(ClientContext ctx) {
        // Get all the ids to act upon
        mTempSet.clear();
        mTempSet.addAll(mTargets.keySet());
        Item[] backpackItems = ctx.backpack.items();
        for(Item i : backpackItems) {
            if(i.valid()) {
                mTempSet.add(i.id());
            }
        }

        for(Integer i : mTempSet) {
            final int invCount = ctx.backpack.select().id(i).count();
            if(!mTargets.get(i).contains(invCount)) {
                return i;
            }
        }
        return -1;
    }

    private static class Range {
        public final int min;
        public final int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public Range(int count) {
            this.min = count;
            this.max = count;
        }

        public boolean contains(int value) {
            return value >= min && value <= max;
        }
    }
}
