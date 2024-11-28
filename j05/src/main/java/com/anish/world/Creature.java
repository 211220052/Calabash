package com.anish.world;
import maze.Node;

import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Creature extends Thing {

    protected static final int CALABASH = 0;
    protected static final int MONSTER = 1;
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
        this.world.get(getX(), getY()).clearCreature();
        this.world.putCreature(this, xPos, yPos);
    }



    public  void useSkill(boolean ifUse){
        ifUseSkill = ifUse;
    }


    public  void run(){

    }


    protected void makeDecision() {
        // 这里可以调用Minimax算法或其他决策算法来决定移动或攻击
        // Minimax算法示例:
        // Position bestMove = minimax(position, depth, true);
        // moveCreature(bestMove);
        // 或者进行攻击等行为
    }

    // Minimax算法实现...
    private Node minimax() {
        // ...
        return new Node(1,1); // 返回决策位置
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



}
