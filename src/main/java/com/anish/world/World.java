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

    private static final World defualtInstance = new World(30,0,0,true);
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

    private World() {

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.trim().isEmpty()) {
            dimension = 30;
            calabashControlled = 0;
            monsterControlled = 0;
        }
        else {
            String[] parts = input.split("\\s+");
            if (parts.length == 3) {
                try {
                    // 将分割后的字符串转换为整数
                    dimension = Integer.parseInt(parts[0]);
                    calabashControlled = Integer.parseInt(parts[1]);
                    monsterControlled = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        }

        generator = new BattleFieldGenerator(dimension);
        generator.generate();
        //System.out.println(generator.getSymbolicBattleField());
        //System.out.println(generator.getRawBattleField());
        if (tiles == null) {
            tiles = new Tile[dimension + 2][dimension + 2];
        }
        // [0 , dimension-1] --> [0 , dimension + 1]

        putBuildings();

    }
    private World(int dimension, int calabashControlled, int monsterControled) {
        this.dimension = dimension;
        this.calabashControlled = calabashControlled;
        this.monsterControlled = monsterControled;

        generator = new BattleFieldGenerator(dimension);
        generator.generate();
        //System.out.println(generator.getSymbolicBattleField());
        //System.out.println(generator.getRawBattleField());
        if (tiles == null) {
            tiles = new Tile[dimension + 2][dimension + 2];
        }
        // [0 , dimension-1] --> [0 , dimension + 1]
        putBuildings();
    }

    private World(int dimension, int calabashControlled, int monsterControlled,boolean flag) {
        assert flag;
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


        calabashes = new ArrayList<>(Arrays.asList(
                new Calabash(AsciiPanel.ONE, getInstance(), 0),
                new Calabash(AsciiPanel.TWO, getInstance(), 1),
                new Calabash(AsciiPanel.THREE, getInstance(), 2),
                new Calabash(AsciiPanel.FOUR, getInstance(), 3),
                new Calabash(AsciiPanel.FIVE, getInstance(), 4),
                new Calabash(AsciiPanel.SIX, getInstance(), 5),
                new Calabash(AsciiPanel.SEVEN, getInstance(), 6))) ;
        calabashes.get(calabashControlled).setControlled(true);

        monsters = new ArrayList<>();
        for(int i=0;i<14;i++){
            monsters.add(new Monster(AsciiPanel.powderBlue,getInstance(),i));
        }
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
                    //System.out.println(World.getInstance().getCalabashes().get(identity).getX());
                } else if ("Monster".equals(type)) {
                    Monster monster = monsters.get(identity);
                    monster.setHealth(health);
                    putCreature(monster, x, y);
                }
            }
            System.out.println("Data has been loaded from " + inputPath2);
        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }



    }



    public static World getInstance() {
        assert instance != null;
        return instance;
    }

    public static World getDefualtInstance() {
        assert defualtInstance != null;
        return defualtInstance;
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
        addCreatures();
        putCreatures();
    }


    public void addCreatures() {
        calabashes = new ArrayList<>(Arrays.asList(
                new Calabash(AsciiPanel.ONE, getInstance(), 0),
                new Calabash(AsciiPanel.TWO, getInstance(), 1),
                new Calabash(AsciiPanel.THREE, getInstance(), 2),
                new Calabash(AsciiPanel.FOUR, getInstance(), 3),
                new Calabash(AsciiPanel.FIVE, getInstance(), 4),
                new Calabash(AsciiPanel.SIX, getInstance(), 5),
                new Calabash(AsciiPanel.SEVEN, getInstance(), 6))) ;
        calabashes.get(calabashControlled).setControlled(true);

        monsters = new ArrayList<>();
        for(int i=0;i<14;i++){
            monsters.add(new Monster(AsciiPanel.powderBlue,getInstance(),i));
        }
    }

    public void putCreatures() {
        Random rand = new Random();
        int randx,randy;
        for (int i = 0; i <= 20; ){
            randx = rand.nextInt(dimension) + 1;
            randy = rand.nextInt(dimension) + 1;
            if(tiles[randx][randy].getThing().isCapable() && isCreatureFree(randx,randy)){
                if(i<=6)
                    this.putCreature(calabashes.get(i), randx, randy);
                else
                    this.putCreature(monsters.get(i-7), randx, randy);
                i++;

            }
        }
    }





    public void startCreatures(){
        for (int i = 0; i < 7; i++)
            calabashes.get(i).start();
        for(int i=0;i<14; i++)
            monsters.get(i).start();
        //calabashes.get(0).start();
    }

    /*public void suspendCreatures(){
        for (int i = 0; i < 7; i++)
            calabashes.get(i).ssuspend();
        for(int i=0;i<14; i++)
            monsters.get(i).ssuspend();
        //calabashes.get(0).start();
    }*/
    public void continueCreatures(){
        for (int i = 0; i < 7; i++)
            LockSupport.unpark(calabashes.get(i));
        for(int i=0;i<14; i++)
            LockSupport.unpark(monsters.get(i));
        //calabashes.get(0).start();
    }


    public void stopCreatures(){
        for (int i = 0; i < 7; i++)
            calabashes.get(i).interrupt();
        for(int i=0;i<14; i++)
            monsters.get(i).interrupt();

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
