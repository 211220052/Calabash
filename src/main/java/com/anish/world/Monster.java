package com.anish.world;
import com.anish.minimax.MinMaxResult;
import com.anish.minimax.Minimax;
import com.anish.minimax.Move;

import java.awt.Color;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Monster extends Creature{


    public Monster(Color color,World world, int i) {
        super(color, (char) 1, world);
        health = 100;
        speed = 1;
        attack = 1;
        vision = 4;
        team = MONSTER;
        identity = i;
        isControlled = false;
    }





    @Override
    public void run() {
        while (this.ifAlive() && !Thread.currentThread().isInterrupted()) {
            if(!isControlled){
                MinMaxResult minMaxResult = Minimax.minimax(this, this.vision, true, 0, MONSTER);
                Move bestMove = minMaxResult.move;
                if(bestMove == null)
                    continue;
                if ("move".equals(bestMove.action)) {
                    this.moveTo(bestMove.creature.getPosition().getX(),bestMove.creature.getPosition().getY());
                    //System.out.println("Monster" + this.identity+" moveTo: "+ this.getX() +","+ this.getY());
                }
                else if ("attack".equals(bestMove.action)) {
                    for(int i =0;i<World.getInstance().getCalabashes().size();i++){
                        if(World.getInstance().getCalabashes().get(i).getIdentity() == bestMove.target.getIdentity()){
                            this.attackCreature(World.getInstance().getCalabashes().get(i));
                            System.out.print("Monster" + this.identity+" is attacking " + "Calabash" + bestMove.target.getIdentity());
                            System.out.println(" Calabash" + bestMove.target.getIdentity() + "'s health:" + World.getInstance().getCalabashes().get(i).health);
                            break;
                        }

                    }
                }

                try {
                    if(world.get(this.getX(),this.getY()).isRough)
                        TimeUnit.SECONDS.sleep(3);
                    else
                        TimeUnit.SECONDS.sleep(2);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Monster" + this.identity + " was interrupted");
                }
            }

        }
        this.world.removeCreature(getX(),getY());
        System.out.println("Calabash" + this.identity + " died");

    }

    /*public void ssuspend() {
        LockSupport.park(); // 挂起当前线程
        System.out.println("Monster" + this.identity + " was Suspended");
    }*/
}

