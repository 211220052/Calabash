package com.anish.world;
import com.anish.minimax.MinMaxResult;
import com.anish.minimax.Minimax;
import com.anish.minimax.Move;

import java.awt.Color;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Calabash extends Creature{


    public Calabash(Color color, World world, int i) {
        super(color, (char) 2, world);
        health = 1000;
        speed = 2;
        attack = 50;
        vision = 3;
        team = CALABASH;
        identity = i;
        isControlled = false;
    }
    
    @Override
    public void run() {
        while (this.ifAlive() && !Thread.currentThread().isInterrupted()) {
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
                    this.moveTo(bestMove.creature.getPosition().getX(),bestMove.creature.getPosition().getY());
                    //System.out.println("Calabash" + this.identity+" moveTo: "+ this.getX() +","+ this.getY());
                }
                else if ("attack".equals(bestMove.action)) {
                    for(int i =0;i<World.getInstance().getMonsters().size();i++){
                        if(World.getInstance().getMonsters().get(i).getIdentity() == bestMove.target.getIdentity()){
                            this.attackCreature(World.getInstance().getMonsters().get(i));
                            //System.out.print("Calabash" + this.identity+" is attacking " + "Monster" + bestMove.target.getIdentity());
                            //System.out.println(" Monster" + bestMove.target.getIdentity() + "'s health:" + World.getInstance().getMonsters().get(i).health);
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
                    // 恢复中断状态
                    Thread.currentThread().interrupt();
                    // 处理中断逻辑
                    System.out.println("Calabash" + this.identity + " was interrupted");
                }


            }


        }
        this.world.removeCreature(getX(),getY());


    }



    public void setControlled(boolean controlled) {
        isControlled = controlled;
    }

    /*public void ssuspend() {
        LockSupport.park(); // 挂起当前线程
        System.out.println("Calabash" + this.identity + " was Suspended");
    }*/
}
