import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
import utils.GamePlaybacker;
import utils.GameRecorder;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Main extends JFrame implements KeyListener {

    static boolean GAMELOOP = true;
    static final int PLAYING = 0;
    //static final int SUSPEND = 1;
    static final int END = 2;
    static final int REPLAYING = 4;
    static int gameState;
    private final AsciiPanel terminal;
    private Screen screen;

    private GameRecorder recorder;
    private GamePlaybacker playbacker;


    public Main() {
        super();
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT, AsciiFont.CP437_16x16);
        add(terminal);
        pack();
        screen = new WorldScreen();
        World.getInstance().setCreatures();
        //World.getInstance().startCreatures();

        addKeyListener(this);
        // 设置窗口位置居中
        setLocationRelativeTo(null);

        initButton();

        requestFocusInWindow();
        setFocusable(true);

        repaint();

    }
    public static void main(String[] args) throws InterruptedException {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);

        while (GAMELOOP){
            if(gameState == PLAYING){
                app.requestFocusInWindow();
                app.setFocusable(true);
                app.repaint();
                TimeUnit.MICROSECONDS.sleep(500);
            }
            /*else if(gameState == SUSPEND){
                app.requestFocusInWindow();
                app.setFocusable(true);
            }*/
            else if(gameState == REPLAYING){
                app.requestFocusInWindow();
                app.setFocusable(true);
                app.playback();
                TimeUnit.MICROSECONDS.sleep(500);
            }
            else if(gameState == END){
                GAMELOOP = false;
            }
        }

    }
    private void initButton() {
        // 添加按钮
        JButton startGameButton = new JButton("开始游戏");
        //JButton suspendGameButton = new JButton("暂停游戏");
        JButton stopGameButton = new JButton("结束游戏");

        JButton saveMapButton = new JButton("保存地图");
        JButton saveProgressButton = new JButton("保存进度");
        JButton recordGameButton = new JButton("录制游戏");

        JButton loadMapButton = new JButton("加载地图");
        JButton loadProgressButton = new JButton("加载进度");
        JButton playbackGameButton = new JButton("回放游戏");

        // 添加监听器
        startGameButton.addActionListener(e -> {
            World.getInstance().startCreatures();
            gameState = PLAYING;
        });
        /*suspendGameButton.addActionListener(e -> {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            if ("暂停游戏".equals(buttonText)) {
                button.setText("继续游戏");
                World.getInstance().suspendCreatures();
                gameState = SUSPEND;
            } else if ("继续游戏".equals(buttonText)) {
                button.setText("暂停游戏");
                World.getInstance().continueCreatures();
                gameState = PLAYING;
            }
        });*/
        stopGameButton.addActionListener(e -> {
            World.getInstance().stopCreatures();
            gameState = END;
        });

        saveMapButton.addActionListener(new SaveMapListener());
        saveProgressButton.addActionListener(new SaveProgressListener());
        recordGameButton.addActionListener(new RecordGameListener());
        loadMapButton.addActionListener(new LoadMapListener());
        loadProgressButton.addActionListener(new LoadProgressListener());
        playbackGameButton.addActionListener(new PlaybackGameListener());

        // 设置按钮的预设尺寸
        Dimension buttonSize = new Dimension(150, 50);
        startGameButton.setPreferredSize(buttonSize);
        //suspendGameButton.setPreferredSize(buttonSize);
        stopGameButton.setPreferredSize(buttonSize);

        saveMapButton.setPreferredSize(buttonSize);
        saveProgressButton.setPreferredSize(buttonSize);
        recordGameButton.setPreferredSize(buttonSize);
        loadMapButton.setPreferredSize(buttonSize);
        loadProgressButton.setPreferredSize(buttonSize);
        playbackGameButton.setPreferredSize(buttonSize);

        // 创建两个面板来分别放置按钮
        JPanel startGamePanel = new JPanel(new FlowLayout());
        JPanel otherButtonsPanel = new JPanel(new GridLayout(2, 3)); // 2行3列

        // 将“开始游戏”按钮添加到单独的面板
        startGamePanel.add(startGameButton);
        //startGamePanel.add(suspendGameButton);
        startGamePanel.add(stopGameButton);

        otherButtonsPanel.add(saveMapButton);
        otherButtonsPanel.add(saveProgressButton);
        otherButtonsPanel.add(recordGameButton);
        otherButtonsPanel.add(loadMapButton);
        otherButtonsPanel.add(loadProgressButton);
        otherButtonsPanel.add(playbackGameButton);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(startGamePanel, BorderLayout.NORTH);
        buttonPanel.add(otherButtonsPanel, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void repaint() {
        //terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
        requestFocusInWindow();
        setFocusable(true);
    }

    public void playback() {
        //terminal.clear();
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

    // ActionListener实现类
    static class SaveMapListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Save Map clicked");
            String outputPath = getPath("battleField.txt");
            // 写入文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {

                for (int[] row : BattleFieldGenerator.battleField) {
                    for (int value : row) {
                        // 将元素写入文件，后跟一个空格
                        writer.write(value + " ");
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

    static class SaveProgressListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 处理保存进度动作
            System.out.println("Save Precess clicked");
            String outputPath = getPath("gameProgress.txt");
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
                System.out.println("Data has been saved to " + outputPath);
            } catch (IOException _e) {
                System.err.println("An error occurred while writing to the file: " + _e.getMessage());
            }
        }

    }

    class RecordGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            System.out.println("Record Game clicked");
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            if ("录制游戏".equals(buttonText)) {
                button.setText("结束录制");
                System.out.println("开始录制");
                recorder = new GameRecorder();
                recorder.start();
            } else if ("结束录制".equals(buttonText)) {
                button.setText("录制游戏");
                recorder.stopRecording();
                String outputPath = getPath("gameVideo.txt");
                // 写入文件
                recorder.saveRecord(outputPath);
                System.out.println("录制结束");
            }

        }
    }

    static class LoadMapListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 实现加载地图的逻辑
            System.out.println("加载地图");
            String inputPath = getPath("battleField.txt");
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
            World.getInstance().putBuildings();
        }
    }

    static class LoadProgressListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 实现加载进度的逻辑
            System.out.println("Load Precess clicked");
            String inputPath = getPath("gameProgress.txt");
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
            System.out.println("Playback Game clicked");
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            if ("回放游戏".equals(buttonText)) {
                gameState = REPLAYING;
                terminal.clear();
                //World.getInstance().suspendCreatures();
                World.getInstance().stopCreatures();
                button.setText("结束回放");
                System.out.println("开始回放");
                String inputPath = getPath("gameVideo.txt");
                playbacker = new GamePlaybacker(terminal, inputPath, button);
                playbacker.start();
            } else if ("结束回放".equals(buttonText)) {
                playbacker.stopPlaybacking();
                //World.getInstance().continueCreatures();
                gameState = PLAYING;
                button.setText("回放游戏");
                System.out.println("回放结束");
            }
        }
    }

    private static String getPath(String s){
        String resourcePath = "data" + File.separator + s;

        // 获取资源文件的输出路径，这里假设是在项目的 resources 目录下
        String needPath = Paths.get("src", "main", "resources", resourcePath).toString();

        // 确保输出目录存在
        try {
            Files.createDirectories(Paths.get(needPath).getParent());
        } catch (IOException ex) {
            System.err.println("Error creating directories: " + ex.getMessage());
            return "-1";
        }

        return needPath;
    }


}

