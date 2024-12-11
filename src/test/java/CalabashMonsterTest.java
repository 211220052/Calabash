import asciiPanel.AsciiPanel;
import com.anish.world.Calabash;
import com.anish.world.Monster;
import com.anish.world.World;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalabashMonsterTest {

    private Calabash calabash;
    private Monster monster;


    @Before
    public void setUp() {
        // 创建一个 Calabash 对象进行测试
        World world = new World(30,0);
        calabash = new Calabash(AsciiPanel.ONE, world, 1);
        monster = new Monster(AsciiPanel.powderBlue,world,0);
    }

    @Test
    public void testConstructor() {
        // 测试构造函数是否正确初始化了对象属性
        assertEquals(AsciiPanel.ONE, calabash.getColor());
        assertEquals(1000, calabash.getHealth());
        assertEquals(2, calabash.getSpeed());
        assertEquals(50, calabash.getAttack());
        assertEquals(3, calabash.getVision());
        assertEquals(Calabash.CALABASH, calabash.getTeam());
        assertEquals(1, calabash.getIdentity());
        assertFalse(calabash.isControlled());

        assertEquals(AsciiPanel.powderBlue, monster.getColor());
        assertEquals(100, monster.getHealth());
        assertEquals(1, monster.getSpeed());
        assertEquals(1, monster.getAttack());
        assertEquals(4, monster.getVision());
        assertEquals(Monster.MONSTER, monster.getTeam());
        assertEquals(0, monster.getIdentity());
    }

//    @Test
//    public void testRun() {
//        // 测试 run 方法是否按预期工作
//        // 注意：这个测试可能需要一些额外的逻辑来模拟外部世界和 Minimax 算法的行为
//        // 由于 Minimax 算法实现复杂，这里我们不包含它的模拟
//        // 你可以根据需要添加更多的测试方法来测试 run 方法中的不同路径
//    }

    @Test
    public void testSetControlled() {
        // 测试 setControlled 方法是否按预期工作
        calabash.setControlled(true);
        assertTrue(calabash.isControlled());
    }

    // 你可以添加更多的测试方法来测试 Calabash 类的其他方法
}