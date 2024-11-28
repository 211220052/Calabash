package gamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GamePanel extends JPanel {
    private Timer gameLoop;
    private int playerX = 100;
    private int playerY = 100;

    public GamePanel() {
        setFocusable(true); // 设置为可聚焦
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GamePanel.this.keyPressed(e);
            }
        });
        startGameLoop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
        drawEntities(g);
    }

    private void drawMap(Graphics g) {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                g.setColor(Color.GRAY);
                g.fillRect(x * 10, y * 10, 10, 10);
            }
        }
    }

    private void drawEntities(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(playerX * 10, playerY * 10, 10, 10);
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                playerY -= 10;
                break;
            case KeyEvent.VK_DOWN:
                playerY += 10;
                break;
            case KeyEvent.VK_LEFT:
                playerX -= 10;
                break;
            case KeyEvent.VK_RIGHT:
                playerX += 10;
                break;
        }
        repaint();
    }

    private void updateGame() {
        // 更新游戏逻辑
        System.out.println("Game updated");
    }

    public void startGameLoop() {
        gameLoop = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                repaint();
            }
        });
        gameLoop.start();
    }
}