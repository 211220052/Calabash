package utils;

import java.awt.*;
import java.io.Serializable;

public class GlyphColorPair implements Serializable {


    final char glyph;
    final Color color;

    final int x;
    final int y;

    GlyphColorPair(char glyph, Color color, int x, int y) {
        this.glyph = glyph;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public char getGlyph() {
        return glyph;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}