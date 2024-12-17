import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import com.anish.screen.Screen;
import com.anish.screen.WorldScreen;
import com.anish.world.World;
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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;

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
        World.getInstance().setCreatures();

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
            clientA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            clientA.setVisible(true);
            clientA.startClient();

            while (true){
                TimeUnit.MICROSECONDS.sleep(500);
            }
        }

        catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    public void startClient() throws IOException, ClassNotFoundException {
        //InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
        //client = SocketChannel.open(hostAddress);
        //client.configureBlocking(false);

        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
        client = SocketChannel.open(hostAddress);
        client.configureBlocking(false);
        //Selector selector = Selector.open();
        //client.register(selector, SelectionKey.OP_READ);

        /*while (true) {
            // 选择事件
            selector.select();
            // 获取已选中的键的迭代器
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isReadable()) {
                    // 读取数据
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
                    int bytesRead = channel.read(buffer);
                    if (bytesRead == -1) {
                        break;
                    }
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
                    buffer.clear();
                }

                iterator.remove();
            }
        }

         */
    }

    @Override
    public void repaint() {
        //terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed");
        String message = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                message = "up";
                System.out.println("up");
                break;
            case KeyEvent.VK_DOWN:
                message = "down";
                System.out.println("down");
                break;
            case KeyEvent.VK_LEFT:
                message = "left";
                System.out.println("left");
                break;
            case KeyEvent.VK_RIGHT:
                message = "right";
                System.out.println("right");
                break;
            case KeyEvent.VK_SPACE:
                message = "attack";
                System.out.println("attack");
                break;
        }
        if (message != null) {
            try {

                ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
                buffer.put((clientName + ": " + message).getBytes());
                buffer.flip();
                client.write(buffer);
                buffer.clear();

                int bytesRead = client.read(buffer);
                while (bytesRead > 0) {
                    buffer.flip();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    GameSnapshot gameSnapshot = deserialize(data);
                    for (GlyphColorPair tileGlyph : gameSnapshot.getTileGlyphs()) {
                        terminal.write(tileGlyph.getGlyph(),tileGlyph.getX(),tileGlyph.getY(),tileGlyph.getColor());
                    }
                    for (GlyphColorPair creatureGlyph : gameSnapshot.getCreatureGlyphs()) {
                        terminal.write(creatureGlyph.getGlyph(),creatureGlyph.getX(),creatureGlyph.getY(),creatureGlyph.getColor());
                    }
                    repaint();
                    System.out.println("process data");
                    buffer.clear();
                }
                //client.close();


            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
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