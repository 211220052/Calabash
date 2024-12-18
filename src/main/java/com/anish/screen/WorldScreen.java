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
                if(x == 14 && y == 19){
                    System.out.println(" ");
                }
                Thing thing = World.getInstance().get(x, y);
                if(thing != null){
                    terminal.write(thing.getGlyph(), x, y, thing.getColor());
                    if(thing.isCapable() && !thing.isFree()){
                        if(thing.getCreature().ifAlive()){
                            terminal.write(thing.getCreature().getGlyph(), x, y, thing.getCreature().getColor());
                            if(thing.getCreature().ifUseSkill()){
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
    public Screen respondToUserAInput(String str) {
        Calabash calabash = World.getInstance().getCalabashes().get(World.getInstance().getCalabashControlled());
        switch (str) {
            case "attack":
                attack(calabash);
                break;
            case "up":
                move(calabash,0,-1);
                break;
            case "down":
                move(calabash,0,1);
                break;
            case "left":
                move(calabash,-1,0);
                break;
            case "right":
                move(calabash,1,0);
                break;
        }
        return this;
    }

    @Override
    public Screen respondToUserBInput(String str) {
        Monster monster = World.getInstance().getMonsters().get(World.getInstance().getMonsterControled());
        switch (str) {
            case "attack":
                attack(monster);
                break;
            case "up":
                move(monster,0,-1);
                break;
            case "down":
                move(monster,0,1);
                break;
            case "left":
                move(monster,-1,0);
                break;
            case "right":
                move(monster,1,0);
                break;
        }
        return this;
    }

    private void attack(Calabash calabash){
        for (int i = 0; i < World.getInstance().getMonsters().size(); i++) {
            Monster monster = World.getInstance().getMonsters().get(i);
            if(monster.ifAlive()) {
                int deltaX = calabash.getX() - monster.getX();
                int deltaY = calabash.getY() - monster.getY();
                //计算距离
                if(Math.sqrt(deltaX * deltaX + deltaY * deltaY) < 3){
                    monster.takeDamage(calabash);
                    System.out.print("Calabash" + calabash.getIdentity()+" is attacking " + "Monster" + monster.getIdentity());
                    System.out.println(" Monster" + monster.getIdentity() + "'s health:" + monster.getHealth());
                    break;
                }
            }
        }
    }
    private void attack(Monster monster){
        for (int i = 0; i < World.getInstance().getCalabashes().size(); i++) {
            Calabash calabash = World.getInstance().getCalabashes().get(i);
            if(calabash.ifAlive()) {
                int deltaX = monster.getX() - calabash.getX();
                int deltaY = monster.getY() - calabash.getY();
                //计算距离
                if(Math.sqrt(deltaX * deltaX + deltaY * deltaY) < 3){
                    calabash.takeDamage(monster);
                    System.out.print("Monster" + monster.getIdentity()+" is attacking " + "Calabash" + calabash.getIdentity());
                    System.out.println(" Monster" + calabash.getIdentity() + "'s health:" + calabash.getHealth());
                    break;
                }
            }
        }
    }

    private void move(Calabash calabash, int dx, int dy){
        Thing thing = World.getInstance().get(calabash.getX() + dx,calabash.getY() + dy);
        if(thing.isCapable()){
            if(thing.isFree())
                calabash.moveTo(calabash.getX() + dx,calabash.getY() + dy);
            else
                System.out.println("Not Free!");
        }
        else
            System.out.println("Touching the wall!");
    }

    private void move(Monster monster, int dx, int dy){
        Thing thing = World.getInstance().get(monster.getX() + dx,monster.getY() + dy);
        if(thing.isCapable()){
            if(thing.isFree())
                monster.moveTo(monster.getX() + dx,monster.getY() + dy);
            else
                System.out.println("Not Free!");
        }
        else
            System.out.println("Touching the wall!");
    }




}
