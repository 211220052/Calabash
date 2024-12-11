package com.anish.world;

public class Tile<T extends Thing> {

    protected T thing;
    protected int xPos;
    protected int yPos;
    public T getThing() {
        return thing;
    }
    public void setThing(T thing) {
        this.thing = thing;
        this.thing.setTile(this);
    }

    public synchronized void setCreatureOnThing(Creature creature) {
        assert (this.thing.isCapable());
        this.thing.setCreature(creature);
        creature.setTile(this);
        creature.setPosition(new Position(xPos, yPos));
    }

    public void clearCreatureOnThing() {
        if(this.thing.isCapable()){
            this.thing.clearCreature();
        }
        else{
            System.out.println("it is not capable");
        }

    }

    public int getxPos() {
        return xPos;
    }



    public int getyPos() {
        return yPos;
    }

    public Tile() {
        this.xPos = -1;
        this.yPos = -1;
    }

    public Tile(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

}
