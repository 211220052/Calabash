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
        // Initialize the World with a specific dimension and number of controlled calabashes
        //world = World.getInstance();
        world = new World(30,0);

        // Create a Calabash and a Monster for testing
        calabash = new Calabash(new Color(204, 0, 0), world, 0);
        monster = new Monster(new Color(176, 224, 230), world, 0);
    }

    @Test
    public void testGet() {
        // Assuming that the World has a method to place creatures at specific positions
        // Place the calabash and monster at specific positions
        world.putCreature(calabash, 1, 1);
        world.putCreature(monster, 2, 2);

        // Test getting the creature at the specified positions
        assertEquals(calabash, world.get(1, 1).getCreature());
        assertEquals(monster, world.get(2, 2).getCreature());
    }

    @Test
    public void testPutCreature() {
        // Test putting a creature on the world
        world.putCreature(calabash, 1, 1);

        // Verify that the creature is on the world at the specified position
        assertEquals(calabash, world.get(1, 1).getCreature());
    }

    @Test
    public void testRemoveCreature() {
        // Place a creature on the world
        world.putCreature(calabash, 1, 1);

        // Remove the creature from the world
        world.removeCreature(1, 1);

        // Verify that the creature is no longer on the world at the specified position
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



    // Add more tests to cover different scenarios and edge cases
}