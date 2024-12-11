import asciiPanel.AsciiPanel;
import com.anish.world.Creature;
import com.anish.world.World;
import com.anish.world.Position;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CreatureTest {

    private World world;
    private Creature creature;
    private Creature target;

   @Before
   public void setUp() {
        world = new World(30,0);
        creature = new Creature(AsciiPanel.ONE, 'C', world);
        target = new Creature(AsciiPanel.powderBlue, 'T', world);
        creature.setHealth(100);
        creature.setAttack(10);
        target.setHealth(100);
        world.putCreature(creature,1,1);
        world.putCreature(target,1,2);
    }

    @Test
    public void testConstructor() {
        assert (creature!=null);
        assertEquals(AsciiPanel.ONE, creature.getColor());
        assertEquals('C', creature.getGlyph());
        assertSame(world, creature.getWorld());
    }

    @Test
    public void testGettersAndSetters() {
        creature.setHealth(50);
        assertEquals(50, creature.getHealth());

        creature.setSpeed(2);
        assertEquals(2, creature.getSpeed());

        creature.setAttack(5);
        assertEquals(5, creature.getAttack());

        creature.setVision(3);
        assertEquals(3, creature.getVision());

        creature.setTeam(Creature.MONSTER);
        assertEquals(Creature.MONSTER, creature.getTeam());

        creature.setIdentity(1);
        assertEquals(1, creature.getIdentity());

        Position position = new Position(2, 3);
        creature.setPosition(position);
        assertSame(position, creature.getPosition());
    }

    @Test
    public void testMoveTo() {
        creature.moveTo(2, 1);
        assertEquals(2, creature.getX());
        assertEquals(1, creature.getY());
    }

    @Test
    public void testAttackCreature() {
        creature.attackCreature(target);
        assertTrue(creature.ifUseSkill());
        assertEquals(90, target.getHealth());
    }

    @Test
    public void testTakeDamage() {
        target.takeDamage(creature);
        assertEquals(90, target.getHealth());
    }

    @Test
    public void testIfAlive() {
        assertTrue(creature.ifAlive());
        creature.setHealth(0);
        assertFalse(creature.ifAlive());
    }
    @Test
    public void testPosition(){
       Creature creature1 = new Creature(creature,2,2);
        assertEquals(2,creature1.getPosition().getX());
        assertEquals(2,creature1.getPosition().getY());

    }
}
