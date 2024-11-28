package com.anish.world;
import com.anish.minimax.MinMaxResult;
import com.anish.minimax.Minimax;
import com.anish.minimax.Move;
import maze.BattleFieldGenerator;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;

public class Monster extends Creature{


    public Monster(Color color,World world, int i) {
        super(color, (char) 1, world);
        health = 100;
        speed = 1;
        attack = 1;
        vision = 2;
        team = MONSTER;
        identity = i;
    }

    public Monster(Monster monster) {
        super(monster.getColor(), (char) 1, monster.getWorld());
        health = monster.getHealth();
        speed = monster.getSpeed();
        attack = monster.getAttack();
        vision = monster.getVision();
        team = MONSTER;
        identity = monster.getIdentity();

    }




    @Override
    public void run() {
        while (this.ifAlive()) {
            // 生成所有可能的移动
            //List<Move> moves = Minimax.generateMoves(this, MONSTER);
            // 使用Minimax算法找到最佳移动
            MinMaxResult minMaxResult = Minimax.minimax(this, this.vision, true, 0, MONSTER);
            // 获取最佳移动
            Move bestMove = minMaxResult.move;
            // 应用最佳移动
            if(bestMove == null)
                continue;
            if ("move".equals(bestMove.action)) {
                world.removeCreature(this.getX(),this.getY());
                this.moveTo(bestMove.creature.getPosition().getX(),bestMove.creature.getPosition().getY());
                System.out.println("Monster" + this.identity+" moveTo: "+ this.getX() +","+ this.getY());
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

