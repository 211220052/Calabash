import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import com.anish.screen.Screen;
import com.anish.screen.WorldScreen;
import utils.GameSnapshot;
import utils.GlyphColorPair;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class IntegratedClientA extends JFrame implements KeyListener {

    private final AsciiPanel terminal;
    private Screen screen;
    private SocketChannel client;
    private final String clientName;

    public IntegratedClientA(String clientName) {
        super();
        setTitle("ClientA");
        this.clientName = clientName;
        terminal = new AsciiPanel(62, 62, AsciiFont.CP437_16x16);
        add(terminal);
        pack();
        screen = new WorldScreen();

        addKeyListener(this);
        // 设置窗口位置居中
        setLocationRelativeTo(null);

        requestFocusInWindow();
        setFocusable(true);

        terminal.clear();

        repaint();

    }

    public static void main(String[] args) {
        try {
            IntegratedClientA clientA = new IntegratedClientA("Client-A");
            clientA.startClient();
            clientA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            clientA.setVisible(true);

            while (true){
                //clientA.requestFocusInWindow();
                //clientA.setFocusable(true);
                //clientA.repaint();
                TimeUnit.MICROSECONDS.sleep(500);
            }
        }
//
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void startClient() throws IOException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
        client = SocketChannel.open(hostAddress);
        client.configureBlocking(false);

        // Add key listener to the frame


        // Start a thread to read messages from the server
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
            try {
                while (client.isOpen()) {
                    buffer.clear();
                    int bytesRead = client.read(buffer);
                    if (bytesRead > 0) {
                        buffer.flip();
                        byte[] data = new byte[bytesRead];
                        buffer.get(data);
                        GameSnapshot gameSnapshot = deserialize(data);
                        
                        for (GlyphColorPair tileGlyph : gameSnapshot.getTileGlyphs()) {
                            terminal.write(tileGlyph.getGlyph(),tileGlyph.getX(),tileGlyph.getY(),tileGlyph.getColor());
                            repaint();
                        }

                        for (GlyphColorPair creatureGlyph : gameSnapshot.getCreatureGlyphs()) {
                            terminal.write(creatureGlyph.getGlyph(),creatureGlyph.getX(),creatureGlyph.getY(),creatureGlyph.getColor());
                            repaint();
                        }
                        
                        
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void repaint() {
        //terminal.clear();
        screen.displayOutput(terminal,true);
        super.repaint();
        requestFocusInWindow();
        setFocusable(true);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed clientA");
        String message = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                message = "up";
                System.out.println("up clientA");
                break;
            case KeyEvent.VK_DOWN:
                message = "down";
                System.out.println("down clientA");
                break;
            case KeyEvent.VK_LEFT:
                message = "left";
                System.out.println("left clientA");
                break;
            case KeyEvent.VK_RIGHT:
                message = "right";
                System.out.println("right clientA");
                break;
            case KeyEvent.VK_SPACE:
                message = "attack";
                System.out.println("attack clientA");
                break;
        }
        if (message != null) {
            try {
                ByteBuffer buffer = ByteBuffer.wrap((clientName + ": " + message).getBytes());
                client.write(buffer);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        screen.respondToUserAInput(message);
        repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static GameSnapshot deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            // 读取 GameSnapshot 对象
            return (GameSnapshot) ois.readObject();
        }
    }
}