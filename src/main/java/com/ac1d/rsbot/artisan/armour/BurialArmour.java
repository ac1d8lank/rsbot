package com.ac1d.rsbot.artisan.armour;

import org.powerbot.script.rt6.ClientContext;

public class BurialArmour {

    private static int FIRST_PIECE_ID = 20572;
    private static int FIRST_INGOT_ID = 20632;

    public enum Metal {
        IRON,
        STEEL,
        MITHRIL,
        ADAMANT,
        RUNITE,
        ;
    }

    public enum Piece {
        HELM      (1640, 11, "Helm"),
        CHESTPLATE(1640, 21, "Chestplate"),
        GAUNTLETS (1640, 31, "Gauntlets"),
        BOOTS     (1640, 40, "Boots"),
        ;

        public final int widget;
        public final int component;
        public final String instruction;

        Piece(int widget, int component, String instruction) {
            this.widget = widget;
            this.component = component;
            this.instruction = instruction;
        }
    }

    public enum Tier {
        MINER,
        WARRIOR,
        SMITH,
        ;
    }

    public static final int[] ALL_INGOTS = new int[Metal.values().length * Tier.values().length];
    static {
        int i = 0;
        for(Tier t : Tier.values()) {
            for(Metal m : Metal.values()) {
                ALL_INGOTS[i++] = getIngotId(m, t);
            }
        }
    }

    public static final int[] ALL_ITEMS = new int[Tier.values().length * Piece.values().length * Metal.values().length];
    static {
        int i = 0;
        for(Tier t : Tier.values()) {
            for(Piece p : Piece.values()) {
                for (Metal m : Metal.values()) {
                    ALL_ITEMS[i++] = getItemId(t, p, m);
                }
            }
        }
    }

    public static int getIngotId(Metal metal, Tier tier) {
        int id = FIRST_INGOT_ID;
        id += tier.ordinal() * Metal.values().length;
        id += metal.ordinal();
        return id;
    }

    public static int getItemId(Tier tier, Piece piece, Metal metal) {
        int id = FIRST_PIECE_ID;
        // FIXME: figure out why these numbers diverge. Is it just tier 3 maybe?
        if(tier == Tier.SMITH && metal == Metal.ADAMANT) {
            switch(piece) {
                case HELM:
                    return 20615;
                case CHESTPLATE:
                    return 20625;
                case GAUNTLETS:
                    return 20630;
                case BOOTS:
                    return 20620;
            }
        }
        id += tier.ordinal() * (Metal.values().length + Piece.values().length);
        id += piece.ordinal() * Metal.values().length;
        id += metal.ordinal();
        return id;
    }

    public static Piece getInstruction(ClientContext ctx) {
        final String text = ctx.widgets.component(1073, 1).text();
        for(Piece p : Piece.values()) {
            if(p.instruction.equals(text)) {
                return p;
            }
        }
        return null;
    }

    public static Piece getSelected(ClientContext ctx) {
        for(Piece p : Piece.values()) {
            if(ctx.widgets.component(p.widget, p.component).visible()) {
                return p;
            }
        }
        return null;
    }
}
