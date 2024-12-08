package com.anish.world;
import maze.Node;

import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Creature extends Thing {

    public static final int CALABASH = 0;
    public static final int MONSTER = 1;
    // 添加一个锁对象来同步对生物体的攻击
    private final Lock attackLock = new ReentrantLock();
    private final Lock takeAttackLock = new ReentrantLock();
    protected int health;
    protected int speed;
    protected int attack;
    protected int vision;
    protected int team;
    protected int identity;
    protected boolean isControlled;



    protected boolean ifUseSkill;



    protected Position position;


    public Creature(Color color, char glyph, World world) {
        super(color, glyph, world);
        isControlled = false;
        ifUseSkill = false;

    }

    public Creature(Creature creature) {
        super(creature.getColor(), (char) 2, creature.getWorld());
        health = creature.getHealth();
        speed = creature.getSpeed();
        attack = creature.getAttack();
        vision = creature.getVision();
        team = creature.getTeam();
        identity = creature.getIdentity();
        isControlled = creature.isControlled;


    }

    public Creature(Creature creature, int x, int y) {
        super(creature.getColor(), (char) 2, creature.getWorld());
        health = creature.getHealth();
        speed = creature.getSpeed();
        attack = creature.getAttack();
        vision = creature.getVision();
        team = creature.getTeam();
        identity = creature.getIdentity();
        isControlled = creature.isControlled;
        position = new Position(x,y);


    }


    public void moveTo(int xPos, int yPos) {
        int pxPos = getX(), pyPos = getY();
        this.world.putCreature(this, xPos, yPos);
        //this.world.get(pxPos, pyPos).clearCreature();
        this.world.removeCreature(pxPos, pyPos);
    }



    public  void useSkill(boolean ifUse){
        ifUseSkill = ifUse;
    }


    public  void run(){

    }

    // 攻击生物体
    public void attackCreature(Creature target) {
        attackLock.lock();
        try {
            if (target.ifAlive()) {
                // 模拟攻击效果
                useSkill(true);
                target.takeDamage(this);
            }
        }
        finally {
            attackLock.unlock();
        }

    }

    // 接收伤害
    public  void takeDamage(Creature creature) {
        if (ifAlive()) {
            // 如果血量小于等于0，生物体死亡
            this.setHealth(this.health - creature.getAttack());
        }
    }


    public boolean ifAlive() {
        return this.health > 0;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getVision() {
        return vision;
    }

    public void setVision(int vision) {
        this.vision = vision;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean ifUseSkill() {
        return ifUseSkill;
    }

    public boolean isControlled(){
        return isControlled;
    }


}
