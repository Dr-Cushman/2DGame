package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // Screen Settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3; // 3x scale

    final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16; // 16 tiles wide
    final int maxScreenRow = 12; // 12 tiles tall 4:3 aspect ratio
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels tall

    // Set FPS
    final int fps = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    // Set players default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 2;

    public GamePanel () {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawTime = 1000000000 / fps; // equals 0,0166666666666667 seconds
        double nextDrawTime = System.nanoTime() + drawTime;

        while (gameThread != null) {

            long currentTime = System.nanoTime();

            System.out.println("Gameloop running");

            // 1 Update game information such character pos
            update();
            // 2 Draw the game
            repaint();



            try {
                double remainingTime = nextDrawTime - currentTime;
                remainingTime = remainingTime / 1000000; // convert to milliseconds

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) (remainingTime / 1000000));

                nextDrawTime += drawTime;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void update() {


        // Handle player movement. Update player position based on key press
        if (keyHandler.upPressed == true) {
            playerY -= playerSpeed;
        } else if (keyHandler.downPressed == true) {
            playerY += playerSpeed;
        } else if (keyHandler.leftPressed == true) {
            playerX -= playerSpeed;
        } else if (keyHandler.rightPressed == true) {
            playerX += playerSpeed;
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(playerX, playerY, tileSize, tileSize);
        g2d.dispose();
    }
}
