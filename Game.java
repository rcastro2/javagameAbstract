import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;


/**
 * Created by rcastro on 1/31/2018.
 */
public class Game extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
    GameLogic game;
    public Game() {
        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        setFocusable(true);
        game = new GameLogic();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Game.canvas = (Graphics2D)g;
        game.gameLoop();
        Game.width = this.getWidth();
        Game.height = this.getHeight();
        update(g);
    }
    public void update(Graphics g){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

    public static Graphics2D canvas;
    public static int width;
    public static int height;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Keys.pressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Keys.pressed[e.getKeyCode()] = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Mouse.x = e.getX();
        Mouse.y = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Mouse.leftClick = e.getButton() == 1;
        Mouse.rightClick = e.getButton() == 3;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1){
            Mouse.leftPressed = true;
        }
        if(e.getButton() == 3){
            Mouse.rightPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == 1){
            Mouse.leftPressed = false;
        }
        if(e.getButton() == 3){
            Mouse.rightPressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}