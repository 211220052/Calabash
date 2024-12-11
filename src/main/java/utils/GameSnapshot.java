package utils;

import com.anish.world.Thing;
import com.anish.world.World;
import maze.BattleFieldGenerator;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;
public class GameSnapshot {
    private final List<GlyphColorPair> tileGlyphs = new ArrayList<>();
    private final List<GlyphColorPair> creatureGlyphs = new ArrayList<>();

    public GameSnapshot(World world){
        for (int x = 0; x < BattleFieldGenerator.getDimension() + 2; x++) {
            for (int y = 0; y < BattleFieldGenerator.getDimension() + 2; y++) {
                Thing thing = world.get(x, y);
                if (thing != null) {
                    // 保存 Thing 的信息
                    tileGlyphs.add(new GlyphColorPair(thing.getGlyph(), thing.getColor(), x, y));
                    if (thing.isCapable() && !thing.isFree()) {
                        if (thing.getCreature().ifAlive()) {
                            // 保存 Creature 的信息
                            creatureGlyphs.add(new GlyphColorPair(thing.getCreature().getGlyph(), thing.getCreature().getColor(), x, y));
                        }
                    }
                }
            }
        }
    }

    // 获取保存的信息
    public List<GlyphColorPair> getTileGlyphs() {
        return tileGlyphs;
    }
    public List<GlyphColorPair> getCreatureGlyphs() {
        return creatureGlyphs;
    }


}

