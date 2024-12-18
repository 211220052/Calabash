package com.anish.world;

import asciiPanel.AsciiPanel;
import com.anish.world.field.*;
import maze.BattleFieldGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class World {
    private static final World instance = new World(30,0,0);
    private final Lock putlock = new ReentrantLock();
    private final Lock removelock = new ReentrantLock();
    public static final int WIDTH = 62;
    public static final int HEIGHT = 62;
    private int dimension;
    private Tile<Thing>[][] tiles;
    public BattleFieldGenerator generator;

    private   List<Monster> monsters;

    private   List<Calabash> calabashes;

    private int calabashControlled;



    private int monsterControlled;



    private World(int dimension, int calabashControlled, int monsterControlled) {
        this.dimension = dimension;
        this.calabashControlled = calabashControlled;
        this.monsterControlled = monsterControlled;

        String inputPath1 = getPath("battleField.txt");
        List<int[]> rows = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputPath1))) {
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

        if (tiles == null) {
            tiles = new Tile[dimension + 2][dimension + 2];
        }

        putBuildings();

    }



    public static World getInstance() {
        assert instance != null;
        return instance;
    }





    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }
    public List<Monster> getMonsters() {
        return monsters;
    }
    public List<Calabash> getCalabashes() {
        return calabashes;
    }

    public int getCalabashControlled() {
        return calabashControlled;
    }

    public int getMonsterControled() {
        return monsterControlled;
    }
    public boolean isCreatureFree(int x, int y) {
        return this.tiles[x][y].getThing().isFree();
    }
    public void putBuildings(){
        for (int i = 0; i < dimension + 2; i++) {
            for (int j = 0; j < dimension + 2; j++) {
                tiles[i][j] = new Tile<>(i, j);
                //在边界上放置墙壁
                if (i == 0 || j == 0 || i == dimension + 1 || j == dimension + 1) {
                    tiles[i][j].setThing(new Wall(this));
                }
                else {
                    if(BattleFieldGenerator.battleField[i - 1][j - 1] == 1){
                        tiles[i][j].setThing(new Land(this));
                    }
                    else if(BattleFieldGenerator.battleField[i - 1][j - 1] == 2){
                        tiles[i][j].setThing(new Stone(this));
                    }
                    else if(BattleFieldGenerator.battleField[i - 1][j - 1] == 3){
                        tiles[i][j].setThing(new Mountain(this));
                    }
                    else if(BattleFieldGenerator.battleField[i - 1][j - 1] == 4){
                        tiles[i][j].setThing(new Swamp(this));
                    }
                    else if(BattleFieldGenerator.battleField[i - 1][j - 1] == 5){
                        tiles[i][j].setThing(new River(this));
                    }

                }

            }

        }

    }

    public void putCreature(Creature creature, int x, int y) {
        putlock.lock();
        try {
            this.tiles[x][y].setCreatureOnThing(creature);

        }
        finally {
            putlock.unlock();
        }

    }

    public void removeCreature(int x, int y) {
        this.tiles[x][y].clearCreatureOnThing();
    }


    public void setCreatures(){
        calabashes = new ArrayList<>(Arrays.asList(
                new Calabash(AsciiPanel.ONE, instance, 0),
                new Calabash(AsciiPanel.TWO, instance, 1),
                new Calabash(AsciiPanel.THREE, instance, 2),
                new Calabash(AsciiPanel.FOUR, instance, 3),
                new Calabash(AsciiPanel.FIVE, instance, 4),
                new Calabash(AsciiPanel.SIX, instance, 5),
                new Calabash(AsciiPanel.SEVEN, instance, 6))) ;
        calabashes.get(calabashControlled).setControlled(true);

        monsters = new ArrayList<>();
        for(int i=0;i<14;i++){
            monsters.add(new Monster(AsciiPanel.powderBlue,instance,i));
        }

        monsters.get(monsterControlled).setControlled(true);
        String inputPath2 = getPath("gameProgress.txt");
        // 写入文件
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath2))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                int identity = Integer.parseInt(parts[1]);
                int health = Integer.parseInt(parts[2]);
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                if ("Calabash".equals(type)) {
                    Calabash calabash = calabashes.get(identity);
                    calabash.setHealth(health);
                    putCreature(calabash, x, y);
                } else if ("Monster".equals(type)) {
                    Monster monster = monsters.get(identity);
                    monster.setHealth(health);
                    putCreature(monster, x, y);
                }
            }
        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }



    public void startCreatures(){
        for (int i = 0; i < 7; i++)
            calabashes.get(i).start();
        for(int i=0;i<14; i++)
            monsters.get(i).start();
        //calabashes.get(0).start();
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
