package com.anish.world.field;

import asciiPanel.AsciiPanel;
import com.anish.world.Thing;
import com.anish.world.World;

public class Wall extends Thing {
    public Wall(World world) {
        super(AsciiPanel.cyan, (char) 254, world);
        this.capable = false;
    }

}