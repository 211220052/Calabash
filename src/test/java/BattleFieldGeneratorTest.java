import maze.BattleFieldGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BattleFieldGeneratorTest {

    private BattleFieldGenerator generator;

    @Before
    public void setUp() {
        int dimension = 10;
        generator = new BattleFieldGenerator(dimension);
    }

    @Test
    public void testGenerate() {
        generator.generate();
        assertEquals(10, BattleFieldGenerator.getDimension());
        // 验证战场是否完全生成（没有未初始化的单元格）
        for (int i = 0; i < BattleFieldGenerator.getDimension(); i++) {
            for (int j = 0; j < BattleFieldGenerator.getDimension(); j++) {
                assertNotEquals(0, BattleFieldGenerator.battleField[i][j]);
            }
        }
    }

}