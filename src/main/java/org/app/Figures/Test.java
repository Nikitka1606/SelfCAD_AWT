package org.app.Figures;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Test extends JFrame implements KeyListener {

    private static final int DIR_STEP = 2;
    private static final Random random = new Random();
    private final Thread thread;
    private boolean isLeft = false;
    private boolean isRight = false;
    private boolean isUp = false;
    private boolean isDown = false;
    private int x, y;

    public Test(int width, int height) {
        this.setSize(width, height);
        x = width / 2;
        y = height / 2;
        this.addKeyListener(this);
        thread = new MoveThread(this);
        thread.start();
    }

    //Start point

    public static void main(String... string) {
        JFrame frame = new Test(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    //Listener

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) isLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) isRight = true;
        if (e.getKeyCode() == KeyEvent.VK_UP) isUp = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) isDown = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) isLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) isRight = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) isUp = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) isDown = false;
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    //Graphics

    @Override
    public void paint(Graphics gr) {
        Graphics2D g2d = (Graphics2D) gr;
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        g2d.setColor(new Color(r, g, b));
        g2d.setStroke(new BasicStroke(4f));
        g2d.drawOval(x - 25, y - 25, 50, 50);
    }

    public void animate() {
        if (isLeft) x -= DIR_STEP;
        if (isRight) x += DIR_STEP;
        if (isUp) y -= DIR_STEP;
        if (isDown) y += DIR_STEP;
        this.repaint();
    }

    //Engine thread

    private class MoveThread extends Thread {
        Test runKeyboard;

        public MoveThread(Test runKeyboard) {
            super("MoveThread");
            this.runKeyboard = runKeyboard;
        }

        public void run() {
            while (true) {
                runKeyboard.animate();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}