import asciiPanel.AsciiPanel;
import com.anish.world.World;
import com.anish.world.field.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldTest {

    private World world;
    private Land land;
    private Mountain mountain;
    private River river;
    private Stone stone;
    private Swamp swamp;
    private Wall wall;
    @Before
    public void setUp() {
        // 创建一个 Calabash 对象进行测试
        world = new World(30,0);
        land = new Land(world);
        mountain = new Mountain(world);
        river = new River(world);
        stone = new Stone(world);
        swamp = new Swamp(world);
        wall = new Wall(world);
    }

    @Test
    public void testConstructor() {
        // 测试构造函数是否正确初始化了Land对象
        assertNotNull(land);
        assertEquals(AsciiPanel.green, land.getColor());
        assertEquals((char) 177, land.getGlyph());
        assertSame(world, land.getWorld());

        assertNotNull(mountain);
        assertEquals(AsciiPanel.brightBlack, mountain.getColor());
        assertEquals((char) 30, mountain.getGlyph());
        assertSame(world, mountain.getWorld());

        assertNotNull(river);
        assertEquals(AsciiPanel.brightBlue, river.getColor());
        assertEquals((char) 247, river.getGlyph());
        assertSame(world, river.getWorld());

        assertNotNull(stone);
        assertEquals(AsciiPanel.brightBlack, stone.getColor());
        assertEquals((char) 35, stone.getGlyph());
        assertSame(world, stone.getWorld());

        assertNotNull(swamp);
        assertEquals(AsciiPanel.yellow, swamp.getColor());
        assertEquals((char) 176, swamp.getGlyph());
        assertSame(world, swamp.getWorld());

        assertNotNull(wall);
        assertEquals(AsciiPanel.cyan, wall.getColor());
        assertEquals((char) 254, wall.getGlyph());
        assertSame(world, wall.getWorld());
    }

    @Test
    public void testCapable() {
        // 测试capable属性是否被正确设置为true
        assertTrue(land.isCapable());
        assertTrue(mountain.isCapable());
        assertTrue(river.isCapable());
        assertFalse(stone.isCapable());
        assertTrue(swamp.isCapable());
        assertFalse(wall.isCapable());
    }

    @Test
    public void testIsRough() {
        // 测试isRough属性是否被正确设置为false
        assertFalse(land.isRough());
        assertTrue(mountain.isRough());
        assertTrue(river.isRough());
        assertTrue(swamp.isRough());

    }


}
