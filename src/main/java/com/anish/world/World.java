package com.anish.world;

import asciiPanel.AsciiPanel;
import com.anish.world.field.*;
import maze.BattleFieldGenerator;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class World {
    private static final World instance = new World(30,0);
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

    public World() {

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.trim().isEmpty()) {
            dimension = 30;
            calabashControlled = 0;
        }
        else {
            String[] parts = input.split("\\s+");
            if (parts.length == 2) {
                try {
                    // 将分割后的字符串转换为整数
                    dimension = Integer.parseInt(parts[0]);
                    calabashControlled = Integer.parseInt(parts[1]);
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
    public World(int dimension, int calabashControlled) {
        this.dimension = dimension;
        this.calabashControlled = calabashControlled;
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




}
