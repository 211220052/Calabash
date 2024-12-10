package com.anish.world.field;

import asciiPanel.AsciiPanel;
import com.anish.world.Thing;
import com.anish.world.World;

public class Land extends Thing {

    public Land(World world) {
        super(AsciiPanel.green, (char)177, world);
        this.capable = true;
        this.isRough = false;
    }



}
