package com.ac1d.rsbot.artisan.armour;

import com.ac1d.rsbot.util.rt6.ObjectInteractTask;

public enum ArmourObject {
    CHUTE  (29396, "Deposit-armour", "Chute"),
    SMELTER(29395, "Withdraw-ingots", "Smelter"),
    ANVIL  (4046,  "Smith", "Anvil"),
    ;

    public final ObjectInteractTask task;

    ArmourObject(int id, String action, String option) {
        this.task = new ObjectInteractTask(id, action, option);
    }
}
