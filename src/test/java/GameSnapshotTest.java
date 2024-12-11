import com.anish.world.Thing;
import com.anish.world.World;
import maze.BattleFieldGenerator;
import org.junit.Before;
import org.junit.Test;
import utils.GameSnapshot;

import static org.junit.Assert.*;

import java.awt.Color;

public class GameSnapshotTest {

    private World world;
    private GameSnapshot gameSnapshot;

    private BattleFieldGenerator generator;

    @Before
    public void setUp() {
        // 创建一个模拟的World对象
        world = new World(); // 假设World类有一个无参构造函数
        generator = new BattleFieldGenerator(30);
        world.putBuildings();
        // 创建GameSnapshot对象
        gameSnapshot = new GameSnapshot(world);
    }

    @Test
    public void testGameSnapshotCreation() {
        // 验证tileGlyphs是否包含正确数量的元素
        assertEquals((BattleFieldGenerator.getDimension() + 2) * (BattleFieldGenerator.getDimension() + 2), gameSnapshot.getTileGlyphs().size());
        // 验证creatureGlyphs是否为空，因为没有生物体被创建
        assertTrue(gameSnapshot.getCreatureGlyphs().isEmpty());
    }

    @Test
    public void testTileGlyphsContent() {
        // 验证tileGlyphs中的内容是否正确
        for (int x = 0; x < BattleFieldGenerator.getDimension() + 2; x++) {
            for (int y = 0; y < BattleFieldGenerator.getDimension() + 2; y++) {
                Thing thing = world.get(x, y);
                GlyphColorPair pair = gameSnapshot.getTileGlyphs().get(x * (BattleFieldGenerator.getDimension() + 2) + y);
                assertEquals(thing.getGlyph(), pair.getGlyph());
                assertEquals(thing.getColor(), pair.getColor());
                assertEquals(x, pair.getX());
                assertEquals(y, pair.getY());
            }
        }
    }
}

// 假设的GlyphColorPair类，用于测试
class GlyphColorPair {
    private char glyph;
    private Color color;
    private int x;
    private int y;

    public GlyphColorPair(char glyph, Color color, int x, int y) {
        this.glyph = glyph;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public char getGlyph() {
        return glyph;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
