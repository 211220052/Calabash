package utils;

import com.anish.world.Thing;
import com.anish.world.World;
import maze.BattleFieldGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.awt.Color;
public class GameSnapshot implements Serializable {
    private final List<TeamIdentityPair> creatures = new ArrayList<>();

    public GameSnapshot(World world){
        for (int x = 0; x < BattleFieldGenerator.getDimension() + 2; x++) {
            for (int y = 0; y < BattleFieldGenerator.getDimension() + 2; y++) {
                Thing thing = world.get(x, y);
                if (thing != null && thing.isCapable() && !thing.isFree() && thing.getCreature().ifAlive()) {
                    {
                        creatures.add(new TeamIdentityPair(thing.getCreature().getTeam(),
                                thing.getCreature().getIdentity(),
                                thing.getCreature().getHealth(),
                                thing.getCreature().getX(),
                                thing.getCreature().getY()));
                    }
                }
            }
        }
    }

    // 获取保存的信息
    public List<TeamIdentityPair> getCreatures() {
        return creatures;
    }


}

