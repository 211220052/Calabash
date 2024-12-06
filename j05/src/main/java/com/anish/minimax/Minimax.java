package com.anish.minimax;


import com.anish.world.Creature;

import com.anish.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Minimax {
    private static final Random random = new Random();
    private static final int CALABASH = 0;
    private static final int MONSTER = 1;
    public static MinMaxResult minimax(Creature creature, int depth, boolean maximizingPlayer, int attackTimes, int team) {
        if (depth == 0 || creature.getHealth() <= 0) {
            return new MinMaxResult(evaluate(creature, attackTimes, team), null);
        }

        if (maximizingPlayer) {
            int inc_attack;
            double maxEval = Integer.MIN_VALUE;
            Move tempMove = null;
            int bestMoveIndex = -1;
            List<Move> moves = generateMoves(creature, team);
            for (int i = 0; i < moves.size(); i++) {
                if("attack".equals(moves.get(i).action))
                    inc_attack = 1;
                else
                    inc_attack = 0;

                MinMaxResult result = minimax(moves.get(i).creature, depth - 1, false, attackTimes + inc_attack, team);
                if (result.eval > maxEval) {
                    maxEval = result.eval;
                    tempMove = moves.get(i);
                }
                else if (result.eval == maxEval) {
                    // 当 result.eval == maxEval 时，随机选择是否更新 tempMove
                    if (random.nextBoolean()) {
                        tempMove = moves.get(i);
                    }
                }
            }
            return new MinMaxResult(maxEval, tempMove);
        } else {
            int inc_attack;
            double minEval = Integer.MAX_VALUE;
            Move tempMove = null;
            int bestMoveIndex = -1;
            List<Move> moves = generateMoves(creature, team);
            for (int i = 0; i < moves.size(); i++) {
                if("attack".equals(moves.get(i).action))
                    inc_attack = 1;
                else
                    inc_attack = 0;
                MinMaxResult result = minimax(moves.get(i).creature, depth - 1, true, attackTimes + inc_attack, team);
                if (result.eval < minEval) {
                    minEval = result.eval;
                    tempMove = moves.get(i);
                }
                else if (result.eval == minEval) {
                    // 当 result.eval == minEval 时，随机选择是否更新 tempMove
                    if (random.nextBoolean()) {
                        tempMove = moves.get(i);
                    }
                }
            }
            return new MinMaxResult(minEval, tempMove);
        }
    }

    private static double evaluate(Creature creature, int attackTimes, int team) {
        double totalScore = 0;

        totalScore = totalScore + attackTimes * 100;
        if(World.getInstance().get(creature.getPosition().getX(),creature.getPosition().getY()).isRough())
            totalScore = totalScore - 10;


        float deltaX, deltaY;

        /*if(team == CALABASH){
            for (int i = 0; i < World.getInstance().getMonsters().size(); i++) {
                if(World.getInstance().getMonsters().get(i).ifAlive()) {
                    deltaX = creature.getPosition().getX() - World.getInstance().getMonsters().get(i).getX();
                    deltaY = creature.getPosition().getY() - World.getInstance().getMonsters().get(i).getY();
                    //计算距离
                    totalScore = totalScore - (deltaX + deltaY);
                }


            }
        }
        else if(team == MONSTER){
            for (int i = 0; i < World.getInstance().getCalabashes().size(); i++) {
                if(World.getInstance().getCalabashes().get(i).ifAlive()) {
                    deltaX = creature.getPosition().getX() - World.getInstance().getCalabashes().get(i).getX();
                    deltaY = creature.getPosition().getY() - World.getInstance().getCalabashes().get(i).getY();
                    //计算距离
                    totalScore = totalScore - (deltaX + deltaY);
                }


            }

        }*/

        return totalScore;
    }

    public static List<Move> generateMoves(Creature creature, int team) {
        // 生成可能的移动和攻击动作
        List<Move> moves = new ArrayList<>();

        int newX , newY;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if(dx == 0 && dy == 0)
                    continue;
                newX = creature.getPosition().getX()+dx;
                newY = creature.getPosition().getY()+dy;

                if(World.getInstance().get(newX,newY).isCapable() && World.getInstance().get(newX,newY).isFree()){
                    Creature newCreature = new Creature(creature, newX, newY);
                    moves.add(new Move(newCreature, "move"));
                }
            }
        }
        // 加入生物体作为目标
        if(team == CALABASH){
            double deltaX, deltaY;
            for (int i = 0; i < World.getInstance().getMonsters().size(); i++) {
                if(!World.getInstance().getMonsters().get(i).ifAlive())
                    continue;
                deltaX = creature.getPosition().getX() - World.getInstance().getMonsters().get(i).getX();
                deltaY = creature.getPosition().getY() - World.getInstance().getMonsters().get(i).getY();
                //计算距离
                if(Math.sqrt(deltaX * deltaX + deltaY * deltaY) < 3){
                    Creature target = new Creature(World.getInstance().getMonsters().get(i));
                    moves.add(new Move(creature, "attack", target));
                }
            }

        }
        else if(team == MONSTER){
            double deltaX, deltaY;
            for (int i = 0; i < World.getInstance().getCalabashes().size(); i++) {
                if(!World.getInstance().getCalabashes().get(i).ifAlive())
                    continue;
                deltaX = creature.getPosition().getX() - World.getInstance().getCalabashes().get(i).getX();
                deltaY = creature.getPosition().getY() - World.getInstance().getCalabashes().get(i).getY();
                //计算距离
                if(Math.sqrt(deltaX * deltaX + deltaY * deltaY) < 2){
                    Creature target = new Creature(World.getInstance().getCalabashes().get(i));
                    moves.add(new Move(creature, "attack", target));
                }
            }
        }
        return moves;
    }


}



