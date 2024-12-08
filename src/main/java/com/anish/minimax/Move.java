package com.anish.minimax;

import com.anish.world.Creature;

public  class Move {
    public Creature creature;
    public String action;
    public Creature target;

    public Move(Creature creature, String action) {
        this.creature = creature;
        this.action = action;
    }

    public Move(Creature creature, String action, Creature target) {
        this.creature = creature;
        this.action = action;
        this.target = target;
    }
}
