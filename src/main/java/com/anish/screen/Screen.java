package com.anish.screen;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;



public interface Screen {

    public void displayOutput(AsciiPanel terminal, boolean flag);

    public Screen respondToUserAInput(String str);

    public Screen respondToUserBInput(String str);
}
