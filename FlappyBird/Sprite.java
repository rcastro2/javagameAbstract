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
    public double x, y;
    public double width, height;
    public double left,right,top,bottom;
    public boolean visible;
    public String borderType;
    protected Image i;

    public Sprite(String fn, int x, int y) {
        this.x = x;
        this.y = y;
        this.visible = true;
        this.borderType = null;
        i = new ImageIcon(fn).getImage();
        this.width = i.getWidth(null);
        this.height = i.getHeight(null);
    }

    public void draw(){
        if(this.visible){
            Game.canvas.drawImage(i,(int)x,(int)y,null);
        }
        this.left = this.x;
        this.top = this.y;
        this.right = this.x + this.width;
        this.bottom = this.y + this.height;
        drawBoundaries();
    }

    public void resizeTo(int w, int h){
        i = i.getScaledInstance(w, h,  java.awt.Image.SCALE_SMOOTH);
    }

    public void moveBy(int dx, int dy){
        x += dx;
        y += dy;
    }
    
    public boolean collidedWith(Sprite obj, String shape){
        boolean collision = false;
        if(obj.visible){
            if(shape.equals("circle")){
                double dx = this.x - obj.x;
                double dy = this.y - obj.y;
                double d = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
                collision = d < (int)((this.width/2+this.height/2)/2) + (int)((obj.width/2+obj.height/2)/2);
            }else if(shape.equals("rect")){
                collision = !(obj.left > this.right || obj.right < this.left || obj.top > this.bottom || obj.bottom < this.top);
            }
        }
        
        
        return collision;
    }

    protected void drawBoundaries(){
        if(borderType != null && borderType.equals("circle")){
            Game.canvas.drawOval((int)this.x, (int)this.y, (int)this.width, (int)this.height);
        }else if (borderType != null && borderType.equals("rect")){
            Game.canvas.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height);
        }
    }

    public void print(Object obj){
        System.out.println(obj.toString());
    }
    
    
}
