package com.anish.screen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.TimeUnit;


import com.anish.world.Calabash;
import com.anish.world.Monster;
import com.anish.world.Thing;
import com.anish.world.World;

import asciiPanel.AsciiPanel;
import maze.BattleFieldGenerator;

import javax.swing.*;

public class WorldScreen extends JPanel implements Screen {

    public WorldScreen() {
        System.out.println("Waiting for Battle");

    }
    @Override
    public void displayOutput(AsciiPanel terminal){
        for (int x = 0; x < BattleFieldGenerator.getDimension() + 2; x++) {
            for (int y = 0; y < BattleFieldGenerator.getDimension() + 2; y++) {
                Thing thing = World.getInstance().get(x, y);
                if(thing != null){
                    terminal.write(thing.getGlyph(), x, y, thing.getColor());
                    if(thing.isCapable() && !thing.isFree()){
                        if(thing.getCreature().ifAlive()){
                            terminal.write(thing.getCreature().getGlyph(), x, y, thing.getCreature().getColor());
                            if(thing.getCreature().ifUseSkill()){
                                //playUp(terminal,x,y);
                                thing.getCreature().useSkill(false);
                            }
                        }
                        else{
                            World.getInstance().removeCreature(x, y);
                        }


                    }
                }else {
                    System.out.println("thing == null:"+ x + " " + y);
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

    private void playUp(AsciiPanel terminal, int x, int y){
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0)
                    continue;
                int newX = x + dx;
                int newY = y + dy;
                terminal.write('~', newX, newY, World.getInstance().get(x, y).getCreature().getColor());
            }
        }
    }

    private void attack(Calabash calabash){
        for (int i = 0; i < World.getInstance().getMonsters().size(); i++) {
            if(World.getInstance().getMonsters().get(i).ifAlive()) {
                int deltaX = calabash.getX() - World.getInstance().getMonsters().get(i).getX();
                int deltaY = calabash.getY() - World.getInstance().getMonsters().get(i).getY();
                //计算距离
                if(Math.sqrt(deltaX * deltaX + deltaY * deltaY) < 3){
                    World.getInstance().getMonsters().get(i).takeDamage(calabash);
                    System.out.print("Calabash" + calabash.getIdentity()+" is attacking " + "Monster" + World.getInstance().getMonsters().get(i).getIdentity());
                    System.out.println(" Monster" + World.getInstance().getMonsters().get(i).getIdentity() + "'s health:" + World.getInstance().getMonsters().get(i).getHealth());
                    break;
                }

            }


        }
    }

    private void move(Calabash calabash, int dx, int dy){
        if(World.getInstance().get(calabash.getX() + dx,calabash.getY() + dy).isCapable()){
            if(World.getInstance().get(calabash.getX() + dx,calabash.getY() + dy).isFree()){
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
