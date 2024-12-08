import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import com.anish.world.Calabash;
import com.anish.world.Monster;
import com.anish.world.World;
import com.anish.screen.Screen;
import com.anish.screen.WorldScreen;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import maze.BattleFieldGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class Main extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;

    public Main() {
        super();
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT, AsciiFont.CP437_16x16);
        add(terminal);
        pack();
        screen = new WorldScreen();
        World.getInstance().setCreatures();

        addKeyListener(this);
        // 设置窗口位置居中
        setLocationRelativeTo(null);

        initButton();

        // 确保terminal获取焦点
        requestFocusInWindow();
        setFocusable(true);

        repaint();
    }

    private void initButton(){
        // 添加按钮
        JButton startGameButton = new JButton("开始游戏");
        JButton saveMapButton = new JButton("保存地图");
        JButton saveProgressButton = new JButton("保存进度");
        JButton recordGameButton = new JButton("录制游戏");
        JButton loadMapButton = new JButton("加载地图");
        JButton loadProgressButton = new JButton("加载进度");
        JButton playbackGameButton = new JButton("回放游戏");

        startGameButton.addActionListener(new startGameListener());
        saveMapButton.addActionListener(new SaveMapListener());
        saveProgressButton.addActionListener(new SaveProgressListener());
        recordGameButton.addActionListener(new RecordGameListener());
        loadMapButton.addActionListener(new LoadMapListener());
        loadProgressButton.addActionListener(new LoadProgressListener());
        playbackGameButton.addActionListener(new PlaybackGameListener());

        // 设置按钮的预设尺寸
        startGameButton.setPreferredSize(new Dimension(150, 50));
        saveMapButton.setPreferredSize(new Dimension(150, 50));
        saveProgressButton.setPreferredSize(new Dimension(150, 50));
        recordGameButton.setPreferredSize(new Dimension(150, 50));
        loadMapButton.setPreferredSize(new Dimension(150, 50));
        loadProgressButton.setPreferredSize(new Dimension(150, 50));
        playbackGameButton.setPreferredSize(new Dimension(150, 50));
        // 设置按钮的布局
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(startGameButton);
        buttonPanel.add(saveMapButton);
        buttonPanel.add(saveProgressButton);
        buttonPanel.add(recordGameButton);
        buttonPanel.add(loadMapButton);
        buttonPanel.add(loadProgressButton);
        buttonPanel.add(playbackGameButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }


    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
        requestFocusInWindow();
        setFocusable(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }



    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        while(true){
            app.requestFocusInWindow();
            app.setFocusable(true);
            app.repaint();
            TimeUnit.MICROSECONDS.sleep(500);
        }
    }
}

// ActionListener实现类

class startGameListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        World.getInstance().startCreatures();
    }
}
class SaveMapListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // 处理保存地图动作
        System.out.println("Save Map clicked");
        String resourcePath = "data" + File.separator + "battleField.txt";

        // 获取资源文件的输出路径，这里假设是在项目的 resources 目录下
        String outputPath = Paths.get("src", "main", "resources", resourcePath).toString();

        // 确保输出目录存在
        try {
            Files.createDirectories(Paths.get(outputPath).getParent());
        } catch (IOException ex) {
            System.err.println("Error creating directories: " + ex.getMessage());
            return;
        }

        // 写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {

            for (int[] row : BattleFieldGenerator.battleField) {
                for (int value : row) {
                    // 将元素写入文件，后跟一个空格
                    writer.write(Integer.toString(value) + " ");
                }
                // 写入一个换行符以分隔行
                writer.newLine();
            }
            System.out.println("battleField has been written to " + outputPath);
        } catch (IOException _e) {
            System.err.println("An error occurred while writing to the file: " + _e.getMessage());
        }
    }
}

class SaveProgressListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // 处理保存进度动作
        System.out.println("Save Precess clicked");
        String resourcePath = "data" + File.separator + "gameProgress.txt";
        String outputPath = Paths.get("src", "main", "resources", resourcePath).toString();

        // 确保输出目录存在
        try {
            Files.createDirectories(Paths.get(outputPath).getParent());
        } catch (IOException ex) {
            System.err.println("Error creating directories: " + ex.getMessage());
            return;
        }

        // 写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // 保存 Calabash 列表
            for (Calabash calabash : World.getInstance().getCalabashes()) {
                writer.write("Calabash," + calabash.getIdentity() + "," + calabash.getHealth() + "," + calabash.getX() + "," + calabash.getY());
                writer.newLine();
            }
            // 保存 Monster 列表
            for (Monster monster : World.getInstance().getMonsters()) {
                writer.write("Monster," + monster.getIdentity() + "," + monster.getHealth() + "," + monster.getX()+ "," + monster.getY());
                writer.newLine();
            }
            System.out.println("Data has been saved to " + resourcePath);
        } catch (IOException _e) {
            System.err.println("An error occurred while writing to the file: " + _e.getMessage());
        }
    }

}

class RecordGameListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // 处理记录游戏动作
        System.out.println("Record Game clicked");
    }
}

class LoadMapListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // 实现加载地图的逻辑
        System.out.println("加载地图");
        String resourcePath = "data" + File.separator + "battleField.txt";

        String inputPath = Paths.get("src", "main", "resources", resourcePath).toString();

        try {
            Files.createDirectories(Paths.get(inputPath).getParent());
        } catch (IOException ex) {
            System.err.println("Error creating directories: " + ex.getMessage());
            return;
        }
        List<int[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while (true) {
                try {
                    if ((line = reader.readLine()) == null) break;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                // 使用正则表达式分割每一行的数字
                String[] parts = line.trim().split("\\s+");
                int[] row = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    row[i] = Integer.parseInt(parts[i]);
                }
                rows.add(row);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        // 将列表转换为数组
        int[][] map = new int[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            map[i] = rows.get(i);
        }


        BattleFieldGenerator.battleField = map;
        BattleFieldGenerator.setDimension(map.length);

        World.getInstance().putBuildings(BattleFieldGenerator.getDimension());
    }
}

class LoadProgressListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // 实现加载进度的逻辑
        System.out.println("Load Precess clicked");
        String resourcePath = "data" + File.separator + "gameProgress.txt";
        String inputPath = Paths.get("src", "main", "resources", resourcePath).toString();

        // 确保输出目录存在
        try {
            Files.createDirectories(Paths.get(inputPath).getParent());
        } catch (IOException ex) {
            System.err.println("Error creating directories: " + ex.getMessage());
            return;
        }

        // 写入文件
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                int identity = Integer.parseInt(parts[1]);
                int health = Integer.parseInt(parts[2]);
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);

                if ("Calabash".equals(type)) {
                    Calabash calabash = World.getInstance().getCalabashes().get(identity);
                    calabash.setHealth(health);
                    calabash.moveTo(x, y);
                    //System.out.println(World.getInstance().getCalabashes().get(identity).getX());

                } else if ("Monster".equals(type)) {
                    Monster monster = World.getInstance().getMonsters().get(identity);
                    monster.setHealth(health);
                    monster.moveTo(x,y);
                }
            }
            System.out.println("Data has been loaded from " + inputPath);

        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

}

class PlaybackGameListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

        // 实现游戏回放的逻辑
        System.out.println("回放游戏");
    }
}

