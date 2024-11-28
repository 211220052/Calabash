package com.anish.world.field;

import asciiPanel.AsciiPanel;
import com.anish.world.Thing;
import com.anish.world.World;


public class Mountain extends Thing {
    public Mountain(World world) {
        //super(AsciiPanel.white, (char)32 , world);
        //super(AsciiPanel.white, (char)176 , world);
        super(AsciiPanel.brightBlack, (char)30, world);

        this.capable = true;
        this.isrough = true;
    }

}