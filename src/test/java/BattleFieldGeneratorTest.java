package maze;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BattleFieldGeneratorTest {

    private BattleFieldGenerator generator;

    @Before
    public void setUp() {
        // 使用固定的维度和随机种子来确保测试的可重复性
        int dimension = 10; // 选择一个较小的维度以便测试
        generator = new BattleFieldGenerator(dimension);

    }

    @Test
    public void testGenerate() {
        // 生成战场
        generator.generate();

        // 验证战场维度
        assertEquals(10, BattleFieldGenerator.getDimension());

        // 验证战场是否完全生成（没有未初始化的单元格）
        for (int i = 0; i < BattleFieldGenerator.getDimension(); i++) {
            for (int j = 0; j < BattleFieldGenerator.getDimension(); j++) {
                assertNotEquals(0, BattleFieldGenerator.battleField[i][j]);
            }
        }
    }

    // 你可以添加更多的测试方法来测试 BattleFieldGenerator 类的其他方法
}