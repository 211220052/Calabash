package com.anish.world.field;

import asciiPanel.AsciiPanel;
import com.anish.world.Thing;
import com.anish.world.World;

public class River extends Thing {

    public River(World world) {
        //super(AsciiPanel.white, (char)32 , world);
        //super(AsciiPanel.white, (char)176 , world);
        super(AsciiPanel.brightBlue,(char)247  , world);

        this.capable = true;
        this.isrough = true;
    }



}
