import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by rcastro on 2/1/2018.
 */
public class Shape {
    private int x;
    private int y;
    private int r;
    private Image i;

    public Shape(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
        //i = new ImageIcon("src\\ball.png").getImage();
    }

    public void draw(){
        //Game.canvas.drawImage(i,x,y,null);
        Game.canvas.fillOval(x,y,r,r);
    }
    public void moveBy(int dx, int dy){
        x += dx;
        y += dy;
    }
}
