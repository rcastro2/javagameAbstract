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
public class Sprite {
    public int x;
    public int y;
    private Image i;

    public Sprite(String fn, int x, int y) {
        this.x = x;
        this.y = y;
        i = new ImageIcon(fn).getImage();
    }

    public void draw(){
        Game.canvas.drawImage(i,x,y,null);
    }
    public void resizeTo(int w, int y){
        i = i.getScaledInstance(w, y,  java.awt.Image.SCALE_SMOOTH);
    }
    public void moveBy(int dx, int dy){
        x += dx;
        y += dy;
    }
}
