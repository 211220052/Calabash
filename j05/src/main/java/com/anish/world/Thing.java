package com.anish.world;

import java.awt.Color;

public class Thing extends Thread{
    //public int RankofThing;

    protected World world;

    public Tile<? extends Thing> tile;

    protected boolean capable;

    protected boolean isrough;

    private Creature creature;

    public int getX() {
        if(this.tile == null){
            System.out.println("this.tile == null");
            return -1;
        }
        else{
            return this.tile.getxPos();
        }
    }

    public int getY() {
        return this.tile.getyPos();
    }

    public void setTile(Tile<? extends Thing> tile) {
        this.tile = tile;
    }

    protected Thing(Color color, char glyph, World world) {
        this.color = color;
        this.glyph = glyph;
        this.world = world;
        this.creature = null;
    }

    private final Color color;

    public Color getColor() {
        return this.color;
    }

    private final char glyph;

    public char getGlyph() {
        return this.glyph;
    }

    public World getWorld() {
        return world;
    }

    public Creature getCreature() {
        return creature;
    }

    public synchronized void setCreature(Creature creature) {
        if(capable){
            this.creature = creature;
        }
        else{
            System.out.println("it is not capable");
        }

    }

    public void clearCreature() {
        if(capable){
            this.creature = null;
        }
        else{
            System.out.println("it is not capable");
        }

    }


    public boolean isCapable() {
        return capable;
    }
    public boolean isFree() {
        return this.creature == null;
    }
    public boolean isRough(){
        return isrough;
    }

}
