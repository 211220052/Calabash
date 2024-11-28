package com.anish.world;

import asciiPanel.AsciiPanel;
import com.anish.world.field.*;
import maze.BattleFieldGenerator;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class World {

    private static World instance = new World();;
    private final Lock putlock = new ReentrantLock();
    private final Lock removelock = new ReentrantLock();


    public static final int WIDTH = 62;
    public static final int HEIGHT = 62;




    private final int dimension;
    private Tile<Thing>[][] tiles;


    BattleFieldGenerator generator;




    private   List<Monster> monsters;



    private   List<Calabash> calabashes;


    private int calabashControlled;


    public World() {

        Scanner scanner = new Scanner(System.in);
        dimension = scanner.nextInt();
        generator = new BattleFieldGenerator(dimension);
        generator.generate();
        System.out.println(generator.getSymbolicBattleField());
        System.out.println(generator.getRawBattleField());
        if (tiles == null) {
            tiles = new Tile[dimension + 2][dimension + 2];
        }
        // [0 , dimension-1] --> [0 , dimension + 1]

        putBuildings(dimension);

        this.setCalabashes();
        this.setMonsters();


        calabashControlled = scanner.nextInt();
        calabashes.get(calabashControlled).setControlled(true);

        putCalabashes();
        putMonsters();


    }


    public static World getInstance() {
        return instance;
    }

    public int getDimension() {
        return dimension;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }
    public List<Calabash> getCalabashes() {
        return calabashes;
    }

    public void startCreatures(){
//        for (int i = 0; i < 7; i++)
//            calabashes.get(i).start();
//        for(int i=0;i<14; i++)
//            monsters.get(i).start();
        calabashes.get(0).start();
    }
    public void putBuildings(int dimension){
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
    public BattleFieldGenerator getGenerator() {
        return generator;
    }
    public void putCalabashes() {
        Random rand = new Random();
        int randx;
        int randy;
        for (int i = 0; i < 7; ){
            randx = rand.nextInt(dimension) + 1;
            randy = rand.nextInt(dimension) + 1;
            if(tiles[randx][randy].getThing().isCapable() && isCreatureFree(randx,randy)){
                this.putCreature(calabashes.get(i), randx, randy);
                i++;
            }
        }

    }
    public void putMonsters() {
        Random rand = new Random();
        int randx;
        int randy;
        int i=0;
        while(i<14){
            randx = rand.nextInt(dimension) + 1;
            randy = rand.nextInt(dimension) + 1;
            if(tiles[randx][randy].getThing().isCapable() && isCreatureFree(randx,randy)){
                this.putCreature(monsters.get(i), randx, randy);
                i++;
            }
        }

    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void putCreature(Creature t, int x, int y) {
        putlock.lock();
        try {
            this.tiles[x][y].setCreatureOnThing(t);

        }
        finally {
            putlock.unlock();
        }

    }



    public boolean isCreatureFree(int x, int y) {
        return this.tiles[x][y].getThing().isFree();
    }

    public void removeCreature(int x, int y) {
        //removelock.lock();
        try {
            this.tiles[x][y].clearCreatureOnThing();

        }
        finally {
            //removelock.lock();
        }

    }


    public void setMonsters() {
        this.monsters = new ArrayList<>();
        for(int i=0;i<14;i++){
            monsters.add(new Monster(AsciiPanel.powderBlue,this,i));
        }
    }




    // 其他方法



    public void setCalabashes() {
        this.calabashes = new ArrayList<>(Arrays.asList(
                new Calabash(AsciiPanel.ONE, this, 0),
                new Calabash(AsciiPanel.TWO, this, 1),
                new Calabash(AsciiPanel.THREE, this, 2),
                new Calabash(AsciiPanel.FOUR, this, 3),
                new Calabash(AsciiPanel.FIVE, this, 4),
                new Calabash(AsciiPanel.SIX, this, 5),
                new Calabash(AsciiPanel.SEVEN, this, 6))) ;
    }


    public int getCalabashControlled() {
        return calabashControlled;
    }

}
