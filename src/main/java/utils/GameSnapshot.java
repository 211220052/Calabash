package utils;

import com.anish.world.Calabash;
import com.anish.world.Monster;
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
        for(int i = 0;i<world.getCalabashes().size();i++){
            Calabash calabash = world.getCalabashes().get(i);
            creatures.add(new TeamIdentityPair(calabash.getTeam(),
                    calabash.getIdentity(),
                    calabash.getHealth(),
                    calabash.getX(),
                    calabash.getY()));
            
        }
        for(int i = 0;i<world.getMonsters().size();i++){
            Monster monster = world.getMonsters().get(i);
            creatures.add(new TeamIdentityPair(monster.getTeam(),
                    monster.getIdentity(),
                    monster.getHealth(),
                    monster.getX(),
                    monster.getY()));

        }
        
    }

    // 获取保存的信息
    public List<TeamIdentityPair> getCreatures() {
        return creatures;
    }


}

