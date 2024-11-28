import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.anish.world.World;
import com.anish.screen.Screen;
import com.anish.screen.WorldScreen;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

public class Main extends JFrame implements KeyListener {
    private AsciiPanel terminal;
    private Screen screen;




    public Main() {
        super();
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT, AsciiFont.CP437_16x16);
        add(terminal);
        pack();
        screen = new WorldScreen();
        addKeyListener(this);
        repaint();
    }

    @Override
    public void repaint() {

        terminal.clear();

        screen.displayOutput(terminal);

        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }



    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {

        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);

        while(true){
            app.repaint();
            TimeUnit.MICROSECONDS.sleep(500);
        }


    }

}
