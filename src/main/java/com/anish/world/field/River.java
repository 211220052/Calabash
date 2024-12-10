package com.anish.world.field;

import asciiPanel.AsciiPanel;
import com.anish.world.Thing;
import com.anish.world.World;

public class River extends Thing {

    public River(World world) {
        super(AsciiPanel.brightBlue,(char)247  , world);
        this.capable = true;
        this.isRough = true;
    }



}
