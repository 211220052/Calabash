import asciiPanel.AsciiPanel;
import com.anish.world.*;
import com.anish.world.field.Land;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class TileTest {

    private Tile<Thing> tile;
    private Thing thing;
    private Creature creature;

    @Before
    public void setUp() {
        World world = new World(30, 5);
        tile = new Tile<>();
        thing = new Land(world); 
        creature = new Creature(AsciiPanel.ONE, 'C', world);
    }

    @Test
    public void testConstructor() {
        Tile<Thing> defaultTile = new Tile<>();
        assertEquals(-1, defaultTile.getxPos());
        assertEquals(-1, defaultTile.getyPos());

        Tile<Thing> customTile = new Tile<>(5, 10);
        assertEquals(5, customTile.getxPos());
        assertEquals(10, customTile.getyPos());
    }

    @Test
    public void testSetAndGetThing() {
        tile.setThing(thing);
        assertSame(thing, tile.getThing());
    }

    @Test
    public void testSetCreatureOnThing() {
        tile.setThing(thing);
        tile.setCreatureOnThing(creature);
        assertSame(creature, thing.getCreature()); // 假设Thing类有一个getCreature方法
        assertSame(tile, creature.tile); // 假设Creature类有一个getTile方法
    }

    @Test
    public void testClearCreatureOnThing() {
        tile.setThing(thing);
        tile.setCreatureOnThing(creature);
        tile.clearCreatureOnThing();
        assertNull(thing.getCreature());
        //assertNull(creature.tile);
    }
}