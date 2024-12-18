package com.anish.world;

import java.awt.Color;

public class Thing extends Thread{

    protected World world;
    private final Color color;
    private final char glyph;
    public Tile<? extends Thing> tile;
    protected boolean capable;
    protected boolean isRough;
    private Creature creature;


    public int getX() {
        assert (tile!=null);
        return this.tile.getxPos();

    }
    public int getY() {
        assert (tile!=null);
        return this.tile.getyPos();
    }

    public void setTile(Tile<? extends Thing> tile) {
        this.tile = tile;
    }

    public Thing(Color color, char glyph, World world) {
        this.color = color;
        this.glyph = glyph;
        this.world = world;
        this.creature = null;
    }


    public Color getColor() {
        return this.color;
    }
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
        assert (this.capable);
        assert (this.isFree());
        this.creature = creature;
    }
    public void clearCreature() {
        assert (this.capable);
        this.creature = null;
    }
    public boolean isCapable() {
        return capable;
    }
    public boolean isFree() {
        return this.creature == null;
    }
    public boolean isRough(){
        return isRough;
    }

}
