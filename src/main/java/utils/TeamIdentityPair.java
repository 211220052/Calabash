package utils;

import java.awt.*;
import java.io.Serializable;

public class TeamIdentityPair implements Serializable {
    final int team;
    final int identity;
    final int health;
    final int x;
    final int y;

    TeamIdentityPair(int team, int identity, int health, int x, int y) {
        this.team = team;
        this.identity = identity;
        this.health = health;
        this.x = x;
        this.y = y;
    }

    public int getTeam() {
        return team;
    }

    public int getIdentity() {
        return identity;
    }

    public int getHealth() {
        return health;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}