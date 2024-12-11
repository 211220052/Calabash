package asciiPanel;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * This simulates a code page 437 ASCII terminal display.
 * 
 * @author Trystan Spangler
 */
public class AsciiPanel extends JPanel {
    private static final long serialVersionUID = -4167851861147593092L;

    public static Color black = new Color(0, 0, 0);


    public static Color green = new Color(0, 128, 0);

    public static Color yellow = new Color(128, 128, 0);

    public static Color cyan = new Color(0, 128, 128);

    public static Color white = new Color(192, 192, 192);

    public static Color brightBlack = new Color(128, 128, 128);

    public static Color brightBlue = new Color (0, 191, 255);

    public static Color powderBlue = new Color(176, 224, 230);

    public static Color ONE = new Color(204, 0, 0);
    public static Color TWO = new Color(255, 165, 0);
    public static Color THREE = new Color(252, 233, 79);
    public static Color FOUR = new Color(78, 154, 6);
    public static Color FIVE = new Color(50, 175, 255);
    public static Color SIX = new Color(114, 159, 207);
    public static Color SEVEN = new Color(173, 127, 168);

    private Image offscreenBuffer;
    private Graphics offscreenGraphics;
    private int widthInCharacters;
    private int heightInCharacters;
    private int charWidth = 9;
    private int charHeight = 16;
    private String terminalFontFile = "pic/cp437_16x16.png";

    private Color defaultBackgroundColor;
    private Color defaultForegroundColor;
    private int cursorX;
    private int cursorY;
    private BufferedImage glyphSprite;
    private BufferedImage[] glyphs;

    public BufferedImage getGlyph(char character) {
        if (character < 0 || character >= glyphs.length) {
            throw new IllegalArgumentException(
                    "character " + character + " must be within range [0," + glyphs.length + ").");
        }
        return glyphs[character];
    }
    private char[][] chars;
    private Color[][] backgroundColors;
    private Color[][] foregroundColors;
    private char[][] oldChars;
    private Color[][] oldBackgroundColors;
    private Color[][] oldForegroundColors;
    private AsciiFont asciiFont;

    public void setCursorX(int cursorX) {
        if (cursorX < 0 || cursorX >= widthInCharacters)
            throw new IllegalArgumentException(
                    "cursorX " + cursorX + " must be within range [0," + widthInCharacters + ").");

        this.cursorX = cursorX;
    }

    public void setCursorY(int cursorY) {
        if (cursorY < 0 || cursorY >= heightInCharacters)
            throw new IllegalArgumentException(
                    "cursorY " + cursorY + " must be within range [0," + heightInCharacters + ").");

        this.cursorY = cursorY;
    }

    /**
     * Sets the used font. It is advisable to make sure the parent component is
     * properly sized after setting the font as the panel dimensions will most
     * likely change
     *
     * @param font
     */
    public void setAsciiFont(AsciiFont font) {
        if (this.asciiFont == font) {
            return;
        }
        this.asciiFont = font;

        this.charHeight = font.getHeight();
        this.charWidth = font.getWidth();
        this.terminalFontFile = font.getFontFilename();

        Dimension panelSize = new Dimension(charWidth * widthInCharacters, charHeight * heightInCharacters);
        setPreferredSize(panelSize);

        glyphs = new BufferedImage[256];

        offscreenBuffer = new BufferedImage(panelSize.width, panelSize.height, BufferedImage.TYPE_INT_RGB);
        offscreenGraphics = offscreenBuffer.getGraphics();

        loadGlyphs();

        oldChars = new char[widthInCharacters][heightInCharacters];
    }

    /**
     * Class constructor. Default size is 80x24.
     */
    public AsciiPanel() {
        this(80, 24);
    }

    /**
     * Class constructor specifying the width and height in characters.
     * 
     * @param width
     * @param height
     */
    public AsciiPanel(int width, int height) {
        this(width, height, null);
    }

    /**
     * Class constructor specifying the width and height in characters and the
     * AsciiFont
     * 
     * @param width
     * @param height
     * @param font   if passing null, standard font CP437_9x16 will be used
     */
    public AsciiPanel(int width, int height, AsciiFont font) {
        super();


        if (width < 1) {
            throw new IllegalArgumentException("width " + width + " must be greater than 0.");
        }

        if (height < 1) {
            throw new IllegalArgumentException("height " + height + " must be greater than 0.");
        }

        widthInCharacters = width;
        heightInCharacters = height;

        defaultBackgroundColor = black;
        defaultForegroundColor = white;

        chars = new char[widthInCharacters][heightInCharacters];
        backgroundColors = new Color[widthInCharacters][heightInCharacters];
        foregroundColors = new Color[widthInCharacters][heightInCharacters];

        oldBackgroundColors = new Color[widthInCharacters][heightInCharacters];
        oldForegroundColors = new Color[widthInCharacters][heightInCharacters];

        font = AsciiFont.CP437_16x16;
        if (font == null) {
            font = AsciiFont.CP437_9x16;
        }
        setAsciiFont(font);


    }


    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (g == null)
            throw new NullPointerException();

        for (int x = 0; x < widthInCharacters; x++) {
            for (int y = 0; y < heightInCharacters; y++) {
                if (oldBackgroundColors[x][y] == backgroundColors[x][y]
                        && oldForegroundColors[x][y] == foregroundColors[x][y] && oldChars[x][y] == chars[x][y])
                    continue;

                Color bg = backgroundColors[x][y];
                Color fg = foregroundColors[x][y];

                LookupOp op = setColors(bg, fg);
                BufferedImage img = op.filter(glyphs[chars[x][y]], null);
                offscreenGraphics.drawImage(img, x * charWidth, y * charHeight, null);

                oldBackgroundColors[x][y] = backgroundColors[x][y];
                oldForegroundColors[x][y] = foregroundColors[x][y];
                oldChars[x][y] = chars[x][y];
            }
        }

        g.drawImage(offscreenBuffer, 0, 0, this);
    }

    private void loadGlyphs() {
        try {
            //glyphSprite = ImageIO.read(AsciiPanel.class.getClassLoader().getResource(terminalFontFile));
            glyphSprite = ImageIO.read(Objects.requireNonNull(AsciiPanel.class.getClassLoader().getResource(terminalFontFile)));
        } catch (IOException e) {
            System.err.println("loadGlyphs(): " + e.getMessage());
        }

        for (int i = 0; i < 256; i++) {
            int sx = (i % 16) * charWidth;
            int sy = (i / 16) * charHeight;

            glyphs[i] = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
            glyphs[i].getGraphics().drawImage(glyphSprite, 0, 0, charWidth, charHeight, sx, sy, sx + charWidth,
                    sy + charHeight, null);
        }
    }

    /**
     * Create a <code>LookupOp</code> object (lookup table) mapping the original
     * pixels to the background and foreground colors, respectively.
     * 
     * @param bgColor the background color
     * @param fgColor the foreground color
     * @return the <code>LookupOp</code> object (lookup table)
     */
    private LookupOp setColors(Color bgColor, Color fgColor) {
        short[] a = new short[256];
        short[] r = new short[256];
        short[] g = new short[256];
        short[] b = new short[256];

        byte bga = (byte) (bgColor.getAlpha());
        byte bgr = (byte) (bgColor.getRed());
        byte bgg = (byte) (bgColor.getGreen());
        byte bgb = (byte) (bgColor.getBlue());

        byte fga = (byte) (fgColor.getAlpha());
        byte fgr = (byte) (fgColor.getRed());
        byte fgg = (byte) (fgColor.getGreen());
        byte fgb = (byte) (fgColor.getBlue());

        for (int i = 0; i < 256; i++) {
            if (i == 0) {
                a[i] = bga;
                r[i] = bgr;
                g[i] = bgg;
                b[i] = bgb;
            } else {
                a[i] = fga;
                r[i] = fgr;
                g[i] = fgg;
                b[i] = fgb;
            }
        }

        short[][] table = { r, g, b, a };
        return new LookupOp(new ShortLookupTable(0, table), null);
    }

    /**
     * Clear the entire screen to whatever the default background color is.
     * 
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear() {
        return clear(' ', 0, 0, widthInCharacters, heightInCharacters, defaultForegroundColor, defaultBackgroundColor);
    }


    /**
     * Clear the section of the screen with the specified character and whatever the
     * specified foreground and background colors are.
     * 
     * @param character  the character to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param width      the height of the section to clear
     * @param height     the width of the section to clear
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel clear(char character, int x, int y, int width, int height, Color foreground, Color background) {
        if (character < 0 || character >= glyphs.length)
            throw new IllegalArgumentException(
                    "character " + character + " must be within range [0," + glyphs.length + "].");

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

        if (width < 1)
            throw new IllegalArgumentException("width " + width + " must be greater than 0.");

        if (height < 1)
            throw new IllegalArgumentException("height " + height + " must be greater than 0.");

        if (x + width > widthInCharacters)
            throw new IllegalArgumentException(
                    "x + width " + (x + width) + " must be less than " + (widthInCharacters + 1) + ".");

        if (y + height > heightInCharacters)
            throw new IllegalArgumentException(
                    "y + height " + (y + height) + " must be less than " + (heightInCharacters + 1) + ".");

        int originalCursorX = cursorX;
        int originalCursorY = cursorY;
        for (int xo = x; xo < x + width; xo++) {
            for (int yo = y; yo < y + height; yo++) {
                write(character, xo, yo, foreground, background);
            }
        }
        cursorX = originalCursorX;
        cursorY = originalCursorY;

        return this;
    }
    /**
     * Write a character to the specified position with the specified foreground
     * color. This updates the cursor's position but not the default foreground
     * color.
     * 
     * @param character  the character to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param foreground the foreground color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character, int x, int y, Color foreground) {
        if (character < 0 || character >= glyphs.length)
            throw new IllegalArgumentException(
                    "character " + character + " must be within range [0," + glyphs.length + "].");

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

        return write(character, x, y, foreground, defaultBackgroundColor);
    }

    /**
     * Write a character to the specified position with the specified foreground and
     * background colors. This updates the cursor's position but not the default
     * foreground or background colors.
     * 
     * @param character  the character to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(char character, int x, int y, Color foreground, Color background) {
        if (character < 0 || character >= glyphs.length)
            throw new IllegalArgumentException(
                    "character " + character + " must be within range [0," + glyphs.length + "].");

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ")");

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ")");

        if (foreground == null)
            foreground = defaultForegroundColor;
        if (background == null)
            background = defaultBackgroundColor;

        chars[x][y] = character;
        foregroundColors[x][y] = foreground;
        backgroundColors[x][y] = background;
        cursorX = x + 1;
        cursorY = y;
        return this;
    }

    /**
     * Write a string to the specified position with the specified foreground and
     * background colors. This updates the cursor's position but not the default
     * foreground or background colors.
     * 
     * @param string     the string to write
     * @param x          the distance from the left to begin writing from
     * @param y          the distance from the top to begin writing from
     * @param foreground the foreground color or null to use the default
     * @param background the background color or null to use the default
     * @return this for convenient chaining of method calls
     */
    public AsciiPanel write(String string, int x, int y, Color foreground, Color background) {
        if (string == null)
            throw new NullPointerException("string must not be null.");

        if (x + string.length() > widthInCharacters)
            throw new IllegalArgumentException(
                    "x + string.length() " + (x + string.length()) + " must be less than " + widthInCharacters + ".");

        if (x < 0 || x >= widthInCharacters)
            throw new IllegalArgumentException("x " + x + " must be within range [0," + widthInCharacters + ").");

        if (y < 0 || y >= heightInCharacters)
            throw new IllegalArgumentException("y " + y + " must be within range [0," + heightInCharacters + ").");

        if (foreground == null)
            foreground = defaultForegroundColor;

        if (background == null)
            background = defaultBackgroundColor;

        for (int i = 0; i < string.length(); i++) {
            write(string.charAt(i), x + i, y, foreground, background);
        }
        return this;
    }
}