package com.ac1d.rsbot.ecto;

import com.ac1d.rsbot.util.AcidScript;
import com.ac1d.rsbot.util.CycleTaskManager;
import com.ac1d.rsbot.util.Random;
import com.ac1d.rsbot.util.TaskManager;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

import java.awt.*;

@Script.Manifest(name = "AcidEcto", description = "Grinds bones at the Ectofuntus")
public class AcidEcto extends AcidScript<ClientContext> {
    private CycleTaskManager<ClientContext> mManager;

    @Override
    public void start() {
        super.start();
        mManager = new CycleTaskManager<>(ctx);

        // Banking
        final BankTask bankTask = new BankTask() {
            @Override
            public boolean isReady(ClientContext ctx) {
                return shouldBank() && super.isReady(ctx);
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return !shouldBank() && super.isDone(ctx);
            }
        };
        bankTask.addTarget(EctoItem.ECTOPHIAL, 0, 1);
        bankTask.addTarget(EctoItem.BONES, 1, 9);
        bankTask.addTarget(EctoItem.EMPTY_POT, 1, 9);
        bankTask.addTarget(EctoItem.EMPTY_BUCKET, 1, 9);
        bankTask.addTarget(EctoItem.BONEMEAL, 0, 9); // TODO: make sure we don't bank these
        bankTask.addTarget(EctoItem.SLIME_BUCKET, 0, 9);
        mManager.addTask(bankTask);

        // Bank to center
        mManager.addTask(new EctoArea.AreaMoveTask(EctoArea.BANK, EctoArea.PORT_CENTER) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return !shouldBank() && super.isReady(ctx);
            }
        });

        // Center to exit (optional)
        mManager.addTask(new EctoArea.AreaMoveTask(EctoArea.PORT_CENTER, EctoArea.PORT_EXIT) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return !shouldBank() && Random.percent(21) && super.isReady(ctx);
            }
        });

        // Exit barrier
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.ENERGY_BARRIER) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return (EctoArea.PORT_CENTER.playerInside(ctx) || EctoArea.PORT_EXIT.playerInside(ctx))
                        && !shouldBank() && super.isReady(ctx);
            }

            @Override
            protected long getInteractDelayMillis() {
                return 10000;
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return EctoArea.PORT_ENTER.playerInside(ctx) && super.isDone(ctx);
            }
        });

        // Go into ectofuntus (optional)
        mManager.addTask(new EctoArea.AreaMoveTask(EctoArea.PORT_EXIT, EctoArea.ECTO) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return !shouldBank() && EctoArea.PORT_ENTER.playerInside(ctx) && Random.percent(14) && super.isReady(ctx);
            }
        });

        // Up stairs
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.GRINDER_UP) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return canGrindBones() && (EctoArea.ECTO.playerInside(ctx) || EctoArea.PORT_ENTER.playerInside(ctx)) && super.isReady(ctx);
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return EctoArea.GRINDER.playerInside(ctx) && super.isDone(ctx);
            }
        });

        // Random grinder walk (optional)
        mManager.addTask(new EctoArea.AreaMoveTask(EctoArea.GRINDER, EctoArea.GRINDER) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return canGrindBones() && Random.percent(13) && super.isReady(ctx);
            }
        });

        // Grind
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.HOPPER) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return canGrindBones() && EctoArea.GRINDER.playerInside(ctx) && super.isReady(ctx);
            }

            @Override
            protected long getInteractDelayMillis() {
                return 3000;
            }

            @Override
            protected long getIdleDelayMillis() {
                return 3000;
            }

            @Override
            public void onMessage(MessageEvent message) {
                if(message.text().contains("somePattern")) {
                    // TODO: detect if there's already bones in here, finish the manual steps
                }
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return !canGrindBones() && super.isDone(ctx);
            }
        });

        // TODO: Ectophial down

        // Walk down from grinder
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.GRINDER_DOWN) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return !canGrindBones() && super.isReady(ctx);
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return EctoArea.T3_DOWN.playerInside(ctx) && super.isDone(ctx);
            }
        });

        // Open trapdoor
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.TRAPDOOR_CLOSED) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return canFillBuckets() && EctoArea.ECTO.playerInside(ctx) && super.isReady(ctx);
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return !ctx.objects.select().id(EctoObject.TRAPDOOR_OPEN.id).isEmpty() && super.isDone(ctx);
            }
        });

        // Go down trapdoor
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.TRAPDOOR_OPEN) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return canFillBuckets() && EctoArea.ECTO.playerInside(ctx) && super.isReady(ctx);
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return EctoArea.T1_UP.playerInside(ctx) && super.isDone(ctx);
            }
        });

        // Walk down stairs
        for(final PoolTier t : PoolTier.values()) {
            mManager.addTask(new EctoArea.AreaMoveTask(t.area, t.downArea) {
                @Override
                public boolean isReady(ClientContext ctx) {
                    return canFillBuckets() && super.isReady(ctx);
                }
            });
            mManager.addTask(new EctoObject.ObjectTask(t.downStairs) {
                @Override
                public boolean isReady(ClientContext ctx) {
                    return canFillBuckets() && t.area.playerInside(ctx) && super.isReady(ctx);
                }

                @Override
                public boolean isDone(ClientContext ctx) {
                    final PoolTier down = t.down();
                    return (down == null || down.area.playerInside(ctx)) && super.isDone(ctx);
                }
            });
        }

        // Fill buckets
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.SLIME_POOL) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return canFillBuckets() && EctoArea.POOL.playerInside(ctx) && super.isReady(ctx);
            }

            @Override
            protected long getInteractDelayMillis() {
                return 3000;
            }

            @Override
            protected long getIdleDelayMillis() {
                return 3000;
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return !canFillBuckets() && super.isDone(ctx);
            }
        });

        // TODO: Ectophial up

        // First flight of stairs from pool
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.POOL_UP) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return !canFillBuckets() && EctoArea.POOL.playerInside(ctx) && super.isReady(ctx);
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return EctoArea.T3_DOWN.playerInside(ctx) && super.isDone(ctx);
            }
        });

        // Walk up stairs
        for(final PoolTier t : PoolTier.values()) {
            mManager.addTask(new EctoArea.AreaMoveTask(t.area, t.upArea) {
                @Override
                public boolean isReady(ClientContext ctx) {
                    return !canFillBuckets() && super.isReady(ctx);
                }
            });
            mManager.addTask(new EctoObject.ObjectTask(t.upStairs) {
                @Override
                public boolean isReady(ClientContext ctx) {
                    return !canFillBuckets() && t.area.playerInside(ctx) && super.isReady(ctx);
                }

                @Override
                public boolean isDone(ClientContext ctx) {
                    final PoolTier up = t.up();
                    return (up == null || up.area.playerInside(ctx)) && super.isDone(ctx);
                }
            });
        }

        // Worship
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.ECTOFUNTUS) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return canWorship() && EctoArea.ECTO.playerInside(ctx) && super.isReady(ctx);
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return !canWorship() && super.isDone(ctx);
            }
        });

        // Walk to barrier (optional)
        mManager.addTask(new EctoArea.AreaMoveTask(EctoArea.ECTO, EctoArea.PORT_ENTER) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return shouldBank() && Random.percent(16) && super.isReady(ctx);
            }
        });

        // Enter barrier
        mManager.addTask(new EctoObject.ObjectTask(EctoObject.ENERGY_BARRIER) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return shouldBank() && EctoArea.PORT_ENTER.playerInside(ctx) && super.isReady(ctx);
            }

            @Override
            public boolean isDone(ClientContext ctx) {
                return EctoArea.PORT_EXIT.playerInside(ctx) && super.isDone(ctx);
            }
        });

        // Walk to center
        mManager.addTask(new EctoArea.AreaMoveTask(EctoArea.PORT_EXIT, EctoArea.PORT_CENTER) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return shouldBank() && super.isReady(ctx);
            }
        });

        // Center to bank
        mManager.addTask(new EctoArea.AreaMoveTask(EctoArea.PORT_CENTER, EctoArea.BANK) {
            @Override
            public boolean isReady(ClientContext ctx) {
                return shouldBank() && super.isReady(ctx);
            }
        });
    }

    private boolean shouldBank() {
        return !canGrindBones() && !canFillBuckets() && !canWorship();
    }

    private boolean canGrindBones() {
        return hasItem(EctoItem.BONES) && hasItem(EctoItem.EMPTY_POT);
    }

    private boolean canFillBuckets() {
        return hasItem(EctoItem.EMPTY_BUCKET) && (hasItem(EctoItem.BONEMEAL) || hasItem(EctoItem.BONES));
    }

    private boolean canWorship() {
        return hasItem(EctoItem.SLIME_BUCKET) && hasItem(EctoItem.BONEMEAL);
    }

    private boolean hasItem(EctoItem item) {
        return !ctx.backpack.select().id(item.id).isEmpty();
    }

    @Override
    public TaskManager<ClientContext> getTaskManager() {
        return mManager;
    }

    @Override
    public void drawUI(Graphics g) {
        final Player p = ctx.players.local();
        String text = "Areas: ";
        for(EctoArea a : EctoArea.values()) {
            if(a.area.contains(p)) {
                text += a.toString()+", ";
            }
        }
        text += "  Player idle: "+ctx.players.local().idle();
        if(mManager.currentTask() != null) {
            text += "  Task: " + mManager.currentTask().toString();
        }
        g.drawString(text, 0, 20);
    }

    private enum PoolTier {
        // TODO: add agility shortcut up/down
        T1(EctoArea.T1, EctoArea.T1_DOWN, EctoObject.T1_DOWN, EctoArea.T1_UP, EctoObject.LADDER),
        T2(EctoArea.T2, EctoArea.T2_DOWN, EctoObject.T2_DOWN, EctoArea.T2_UP, EctoObject.T2_UP),
        T3(EctoArea.T3, EctoArea.T3_DOWN, EctoObject.T3_DOWN, EctoArea.T3_UP, EctoObject.T3_UP),
        ;

        private final EctoArea area;
        private final EctoArea downArea;
        private final EctoObject downStairs;
        private final EctoArea upArea;
        private final EctoObject upStairs;

        PoolTier(EctoArea area, EctoArea downArea, EctoObject downStairs, EctoArea upArea, EctoObject upStairs) {
            this.area = area;
            this.downArea = downArea;
            this.downStairs = downStairs;
            this.upArea = upArea;
            this.upStairs = upStairs;
        }

        public PoolTier up() {
            final int idx = ordinal() - 1;
            if(idx >= 0) {
                return values()[idx];
            }
            return null;
        }

        public PoolTier down() {
            final int idx = ordinal() + 1;
            if(idx < values().length) {
                return values()[idx];
            }
            return null;
        }
    }
}
