import com.anish.world.Calabash;
import com.anish.world.Monster;
import com.anish.world.World;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class WorldTest {
    private World world;
    private Calabash calabash;
    private Monster monster;
    @Before
    public void setUp() {
        world = new World(30,0);
        calabash = new Calabash(new Color(204, 0, 0), world, 0);
        monster = new Monster(new Color(176, 224, 230), world, 0);
    }

    @Test
    public void testGet() {
        world.putCreature(calabash, 1, 1);
        world.putCreature(monster, 2, 2);
        assertEquals(calabash, world.get(1, 1).getCreature());
        assertEquals(monster, world.get(2, 2).getCreature());
    }

    @Test
    public void testPutCreature() {
        world.putCreature(calabash, 1, 1);
        assertEquals(calabash, world.get(1, 1).getCreature());
    }

    @Test
    public void testRemoveCreature() {
        world.putCreature(calabash, 1, 1);
        world.removeCreature(1, 1);
        assertNull(world.get(1, 1).getCreature());
    }

    @Test
    public void testAddCreatures() {
        // Test adding creatures to the world
        world.addCreatures();

        // Verify that the correct number of creatures has been added
        assertEquals(7, world.getCalabashes().size());
        assertEquals(14, world.getMonsters().size());
    }
}