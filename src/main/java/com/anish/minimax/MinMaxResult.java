package com.anish.minimax;

import com.anish.world.Creature;

public class MinMaxResult {
    public final double eval;

    public final Move move;



    public MinMaxResult(double eval, Move move) {
        this.eval = eval;
        this.move = move;
    }

}
