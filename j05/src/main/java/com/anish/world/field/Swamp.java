package com.anish.world.field;

import asciiPanel.AsciiPanel;
import com.anish.world.Thing;
import com.anish.world.World;

public class Swamp extends Thing {
    public Swamp(World world) {
        super(AsciiPanel.yellow,(char)176,world);
        this.capable = true;
        this.isrough = true;
    }

}