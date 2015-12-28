package com.ac1d.rsbot.artisan.cannon;

public enum CannonObject {
    // Pickup parts
    BASE_DESK(29326),
    STAND_DESK(29310),
    BARREL_DESK(29327),
    FURNACE_DESK(29329),

    // Repair objects
    MOULD_DESK(29357),
    SMELTER(29397),
    ANVIL(24847),
    FURNACE(24887),
    GUNPOWDER(29298),

    // Stage objects
    CANNON_SPOT(24856),
    BASE       (24865), // with Base
    STAND      (24868), // with Stand
    BARREL     (24869), // with Barrel
    CANNON     (24880), // ready to test!

    /*
    Stuck Animations:
    288,
    183,
    177,
     */
    ;

    CannonObject(int id) {
    }
}
