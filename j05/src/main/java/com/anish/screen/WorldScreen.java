package com.anish.screen;

import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.TimeUnit;


import com.anish.world.Calabash;
import com.anish.world.Monster;
import com.anish.world.World;

import asciiPanel.AsciiPanel;
import maze.BattleFieldGenerator;

public class WorldScreen implements Screen {


    public WorldScreen() {

        System.out.println("Waiting for Battle");
        World.getInstance().startCreatures();
    }





    @Override
    public void displayOutput(AsciiPanel terminal){
        for (int x = 0; x < BattleFieldGenerator.getDimension() + 2; x++) {
            for (int y = 0; y < BattleFieldGenerator.getDimension() + 2; y++) {
                terminal.write(World.getInstance().get(x, y).getGlyph(), x, y, World.getInstance().get(x, y).getColor());
                if(World.getInstance().get(x, y).isCapable() && !World.getInstance().get(x, y).isFree()
                    &&World.getInstance().get(x, y).getCreature().ifAlive() ){
                    terminal.write(World.getInstance().get(x, y).getCreature().getGlyph(), x, y, World.getInstance().get(x, y).getCreature().getColor());
                    /*if(World.getInstance().get(x, y).getCreature().ifUseSkill()) {
                        for (int dx = -1; dx <= 1; dx++) {
                            for (int dy = -1; dy <= 1; dy++) {
                                if (dx == 0 && dy == 0)
                                    continue;
                                int newX = x + dx;
                                int newY = y + dy;
                                terminal.write('~', newX, newY, World.getInstance().get(x, y).getCreature().getColor());
                            }
                        }
                        World.getInstance().get(x, y).getCreature().useSkill(false);
                    }*/
                }
            }
        }
    }





    @Override
    public Screen respondToUserInput(KeyEvent key) {
        Calabash calabash = World.getInstance().getCalabashes().get(World.getInstance().getCalabashControlled());
        switch (key.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                attack(calabash);
                break;
            case KeyEvent.VK_UP:
                move(calabash,0,-1);
                break;
            case KeyEvent.VK_DOWN:
                move(calabash,0,1);
                break;
            case KeyEvent.VK_LEFT:
                move(calabash,-1,0);
                break;
            case KeyEvent.VK_RIGHT:
                move(calabash,1,0);
                break;
        }
        return this;
    }

    private void attack(Calabash calabash){
        for (int i = 0; i < World.getInstance().getMonsters().size(); i++) {
            if(World.getInstance().getMonsters().get(i).ifAlive()) {
                int deltaX = calabash.getX() - World.getInstance().getMonsters().get(i).getX();
                int deltaY = calabash.getY() - World.getInstance().getMonsters().get(i).getY();
                //计算距离
                if((deltaX + deltaY)<2){
                    World.getInstance().getMonsters().get(i).takeDamage(calabash);
                    break;
                }

            }


        }
    }

    private void move(Calabash calabash, int dx, int dy){
        if(World.getInstance().get(calabash.getX() + dx,calabash.getY() + dy).isCapable()){
            if(World.getInstance().get(calabash.getX() + dx,calabash.getY() + dy).isFree()){
                World.getInstance().removeCreature(calabash.getX(),calabash.getY());
                calabash.moveTo(calabash.getX() + dx,calabash.getY() + dy);
                if(dy == -1)
                    System.out.println("GO UP");
                else if(dy == 1)
                    System.out.println("GO DOWN");
                else if(dx == -1)
                    System.out.println("GO LEFT");
                else if(dx == 1)
                    System.out.println("GO RIGHT");

            }
            else{
                System.out.println("Not Free!");
            }

        }
        else{
            System.out.println("Touching the wall!");
        }
    }




}
