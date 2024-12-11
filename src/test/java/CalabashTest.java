import com.anish.world.Calabash;
import com.anish.world.World;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class CalabashTest {

    private Calabash calabash;


    @Before
    public void setUp() {
        // 创建一个 Calabash 对象进行测试
        World world = new World(30,0);
        calabash = new Calabash(Color.RED, world, 1);
    }

    @Test
    public void testConstructor() {
        // 测试构造函数是否正确初始化了对象属性
        assertEquals(Color.RED, calabash.getColor());
        assertEquals(1000, calabash.getHealth());
        assertEquals(2, calabash.getSpeed());
        assertEquals(50, calabash.getAttack());
        assertEquals(3, calabash.getVision());
        assertEquals(Calabash.CALABASH, calabash.getTeam());
        assertEquals(1, calabash.getIdentity());
        assertFalse(calabash.isControlled());
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