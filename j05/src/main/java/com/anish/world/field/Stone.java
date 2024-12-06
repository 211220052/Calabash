package com.anish.world.field;

import asciiPanel.AsciiPanel;
import com.anish.world.Thing;
import com.anish.world.World;

public class Stone extends Thing {
    public Stone(World world) {
        super(AsciiPanel.brightBlack, (char) 35, world);
        this.capable = false;
    }
}
