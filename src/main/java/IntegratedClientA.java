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
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class IntegratedClientA extends JFrame implements KeyListener {

    private final AsciiPanel terminal;
    private Screen screen;
    private SocketChannel client;
    private final String clientName;

    private final Queue<String> messageQueue; // 用于存储键盘事件的消息

    public IntegratedClientA(String clientName) throws IOException {
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
        repaint();

        messageQueue = new LinkedList<>();

    }

    public static void main(String[] args) {
        try {
            IntegratedClientA clientA = new IntegratedClientA("Client-A");
            clientA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            clientA.setVisible(true);
            clientA.startClient();

        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public void startClient() throws IOException, ClassNotFoundException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
        client = SocketChannel.open(hostAddress);
        client.configureBlocking(false);

        while (true){
            while (!messageQueue.isEmpty()) {
                String message = messageQueue.poll();
                if (message != null) {
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    buffer.put((clientName + ": " + message).getBytes(StandardCharsets.UTF_8));
                    buffer.flip();
                    client.write(buffer);
                    System.out.println(clientName + ": " + message);
                    buffer.clear();

                }
            }

            ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
            int bytesRead = client.read(buffer);
            if (bytesRead == -1) {
                client.close();
                return;
            }
            if (bytesRead > 0) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                System.out.println("Received message: ");

                GameSnapshot gameSnapshot = deserialize(data);

                for(GlyphColorPair pair: gameSnapshot.getCreatureGlyphs()){
                    System.out.println(pair.getGlyph()+ ", " + pair.getX()+ ", " +pair.getY()+ ", " +pair.getColor().toString());
                }

                for (GlyphColorPair creatureGlyph : gameSnapshot.getCreatureGlyphs()) {
                    terminal.write(creatureGlyph.getGlyph(),creatureGlyph.getX(),creatureGlyph.getY(),creatureGlyph.getColor());

                }
                repaint();
            }
            TimeUnit.MILLISECONDS.sleep(50);
        }



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
            messageQueue.add(message);
        }
        screen.respondToUserAInput(message);
        //repaint();

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