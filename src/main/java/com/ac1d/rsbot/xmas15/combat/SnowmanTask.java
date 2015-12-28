package com.ac1d.rsbot.xmas15.combat;

import com.ac1d.rsbot.util.rt6.InteractTask;
import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.awt.Graphics2D;

public class SnowmanTask extends InteractTask<GameObject> {

    public static final int[] STAGE_IDS = {
            100442, 100443, 100444, 100335, 100446, 100447, 100448, 100449, 100450, 100451, 100452,
            100453, 100454, 100455, 100456, 100457, 100458, 100459, 100460, 100461, 100462, 100463,
            100464, 100465, 100466, 100467, 100468, 100469, 100470, 100471
    };

    private final TypeChecker mChecker;
    private int[] mIds;

    private Filter<? super GameObject> mFilter = new Filter<GameObject>() {
        @Override
        public boolean accept(GameObject gameObject) {
            final Tile objTile = gameObject.tile();
            for(SnowmanType type : SnowmanType.values()) {
                if(!mChecker.typeEnabled(type)) {
                    continue;
                }

                for(Tile tile : type.locations) {
                    if(tile.equals(objTile)) {
                        return true;
                    }
                }
            }
            return false;
        }
    };

    public SnowmanTask(TypeChecker checker, String name, int... ids) {
        super("Build", name);
        mChecker = checker;
        mIds = ids;
    }

    @Override
    protected GameObject findEntity(ClientContext ctx) {
        return ctx.objects.select()
                .select(mFilter)
                .id(mIds)
                .nearest().poll();
    }

    @Override
    public boolean isDone(ClientContext ctx) {
        return !mFilter.accept(getEntity()) || super.isDone(ctx);
    }

    @Override
    protected long getIdleDelayMillis() {
        return 4000;
    }

    @Override
    protected long getInteractDelayMillis() {
        return 6000;
    }

    public interface TypeChecker {
        boolean typeEnabled(SnowmanType type);
    }
}
