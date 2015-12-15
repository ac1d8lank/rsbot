package com.ac1d.rsbot.xmas15.combat;

import com.ac1d.rsbot.util.rt6.ObjectInteractTask;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Constants;

public enum SnowmanType {
    MELEE(Constants.SKILLS_ATTACK, "Melted snowman warrior", 100382, new Tile(3664, 3762, 1), new Tile(3669, 3769, 1)),
    RANGED(Constants.SKILLS_RANGE, "Melted snowman ranger", 100384, new Tile(3659, 3763, 1), new Tile(3664, 3767, 1), new Tile(3667, 3772, 1)),
    MAGIC(Constants.SKILLS_MAGIC, "Melted snowman mage", 100383, new Tile(3661, 3773, 1), new Tile(3671, 3766, 1)),
    ;

    public final int skill;
    public final String meltedName;
    public final int meltedId;
    public final Tile[] locations;

    SnowmanType(int skill, String meltedName, int meltedId, Tile... locations) {
        this.skill = skill;
        this.meltedName = meltedName;
        this.meltedId = meltedId;
        this.locations = locations;
    }
}
