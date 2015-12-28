package com.ac1d.rsbot.artisan.armour;

import com.ac1d.rsbot.util.rt6.ObjectInteractTask;
import org.powerbot.script.rt6.ClientContext;

public enum ArmourObject {
    CHUTE  (29396, "Deposit-armour", "Chute"),
    SMELTER(29395, "Withdraw-ingots", "Smelter"),
    ANVIL  (4046,  "Smith", "Anvil"),
    ;

    public final ObjectInteractTask task;

    ArmourObject(int id, String action, String option) {
        this.task = new ObjectInteractTask(id, action, option) {
            @Override
            public boolean isDone(ClientContext ctx) {
                switch(ArmourObject.this) {
                    case ANVIL:
                        if(ArmourManager.isInteracting(ctx)) {
                            return true;
                        }
                        break;
                }
                return super.isDone(ctx);
            }
        };
    }
}
