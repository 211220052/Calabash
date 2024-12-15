/*
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

*/
/**
 *
 * Test client for NIO server
 *
 *//*

public class Play extends JFrame implements KeyListener {

    private String[] messages;
    public Play() {
        // 设置窗口标题
        setTitle("Test Client");
        // 设置窗口大小
        setSize(400, 400);
        setLocationRelativeTo(null);

        // 添加键盘监听器
        addKeyListener(this);
        // 显示窗口

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        requestFocusInWindow();
        setFocusable(true);

        JButton startGameButton = new JButton("开始游戏");
        startGameButton.addActionListener(e -> {
            System.out.println("开始游戏");

        });
        JPanel startGamePanel = new JPanel(new FlowLayout());
        startGamePanel.add(startGameButton);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(startGamePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    public static void main(String[] args) throws InterruptedException {
        Play app = new Play();
        app.requestFocusInWindow();
        app.setFocusable(true);
        while(true){
            app.requestFocusInWindow();
            app.setFocusable(true);
            //new Thread(client, "client-B").start();
            Thread.sleep(500);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keyTyped");
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                System.out.println("Up arrow key pressed");
                messages = new String[]{new String("up")};
                Runnable client = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new com.ruk.nio.TestClient().startClient();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                };
                new Thread(client, "client-A").start();
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("Down arrow key pressed");
                messages.add("down");
                break;
            case KeyEvent.VK_LEFT:
                System.out.println("Left arrow key pressed");
                messages.add("left");
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("Right arrow key pressed");
                messages.add("left");
                break;
            case KeyEvent.VK_SPACE:
                System.out.println("Space key pressed");
                messages.add("attack");
                break;
            default:
                System.out.println("Other key pressed: " + e.getKeyChar());
                messages.add("exit");
                break;
        }

    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}

*/
