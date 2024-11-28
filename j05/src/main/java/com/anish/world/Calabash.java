package com.anish.world;
import com.anish.minimax.MinMaxResult;
import com.anish.minimax.Minimax;
import com.anish.minimax.Move;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;

public class Calabash extends Creature{


    public Calabash(Color color, World world, int i) {
        super(color, (char) 2, world);
        health = 1000;
        speed = 2;
        attack = 50;
        vision = 4;
        team = CALABASH;
        identity = i;
        isControlled = false;
    }

    public Calabash(Calabash calabash) {
        super(calabash.getColor(), (char) 2, calabash.getWorld());
        health = calabash.getHealth();
        speed = calabash.getSpeed();
        attack = calabash.getAttack();
        vision = calabash.getVision();
        team = CALABASH;
        identity = calabash.getIdentity();
        isControlled = calabash.isControlled;
    }




    @Override
    public void run() {
        Scanner scanner;
        while (this.ifAlive()) {
            if(!isControlled){
                // 生成所有可能的移动
                //List<Move> moves = Minimax.generateMoves(this, CALABASH);
                // 使用Minimax算法找到最佳移动
                MinMaxResult minMaxResult = Minimax.minimax(this, this.vision, true, 0, CALABASH);
                // 获取最佳移动
                Move bestMove = minMaxResult.move;
                // 应用最佳移动
                if(bestMove == null)
                    continue;
                if ("move".equals(bestMove.action)) {
                    world.removeCreature(this.getX(),this.getY());
                    this.moveTo(bestMove.creature.getPosition().getX(),bestMove.creature.getPosition().getY());
                    System.out.println("Calabash" + this.identity+" moveTo: "+ this.getX() +","+ this.getY());
                }
                else if ("attack".equals(bestMove.action)) {
                    for(int i =0;i<World.getInstance().getMonsters().size();i++){
                        if(World.getInstance().getMonsters().get(i).getIdentity() == bestMove.target.getIdentity()){
                            this.attackCreature(World.getInstance().getMonsters().get(i));
                            System.out.print("Calabash" + this.identity+" is attacking " + "Monster" + bestMove.target.getIdentity());
                            System.out.println(" Monster" + bestMove.target.getIdentity() + "'s health:" + World.getInstance().getMonsters().get(i).health);
                            break;
                        }

                    }
                }

                try {
                    if(world.get(this.getX(),this.getY()).isrough)
                            TimeUnit.SECONDS.sleep(2);
                    else
                        TimeUnit.SECONDS.sleep(1);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }


        }

    }


    public boolean isControlled() {
        return isControlled;
    }

    public void setControlled(boolean controlled) {
        isControlled = controlled;
    }
}
