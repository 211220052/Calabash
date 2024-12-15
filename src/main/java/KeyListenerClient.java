import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class KeyListenerClient {

    private SocketChannel client;
    private final String clientName;

    public KeyListenerClient(String clientName) {
        this.clientName = clientName;
    }

    public void startClient() throws IOException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
        client = SocketChannel.open(hostAddress);
        client.configureBlocking(false);

        // Create a frame to listen for key events
        JFrame frame = new JFrame(clientName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);

        // Add key listener to the frame
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String message = null;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        message = "up";
                        break;
                    case KeyEvent.VK_DOWN:
                        message = "down";
                        break;
                    case KeyEvent.VK_LEFT:
                        message = "left";
                        break;
                    case KeyEvent.VK_RIGHT:
                        message = "right";
                        break;
                    case KeyEvent.VK_SPACE:
                        message = "attack";
                        break;
                    // Add more cases for other keys as needed
                }
                if (message != null) {
                    try {
                        ByteBuffer buffer = ByteBuffer.wrap((clientName + ": " + message).getBytes());
                        client.write(buffer);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        // Start a thread to read messages from the server
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            try {
                while (client.isOpen()) {
                    buffer.clear();
                    int bytesRead = client.read(buffer);
                    if (bytesRead > 0) {
                        buffer.flip();
                        byte[] data = new byte[bytesRead];
                        buffer.get(data);
                        System.out.println(clientName + " received: " + new String(data));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            new KeyListenerClient("Client-A").startClient();
            new KeyListenerClient("Client-B").startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}