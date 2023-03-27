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
public class Animation extends Sprite{
    private int frame_width,frame_height, current_frame, frames, frame_per_rows, frame_per_cols;
    private double frame_rate, frame_count;

    public Animation(String fn, int frames, int frame_width, int frame_height, double frame_rate) {
        super(fn, 0, 0);
        this.x = 0;
        this.y = 0;
        i = new ImageIcon(fn).getImage();
        this.frames = frames;
        this.frame_width = frame_width;
        this.frame_height = frame_height;
        this.frame_per_cols = i.getWidth(null) / frame_width;
        this.frame_per_rows = i.getHeight(null) / frame_height;
        this.frame_rate = frame_rate;
        this.frame_count = 0;
        this.current_frame = 0;
        this.width = frame_width;
        this.height = frame_height;
        //System.out.println(this.frame_per_cols + " " + this.frame_per_rows);
    }

    public void draw(){
        int x = (int)(this.x);
        int y = (int)(this.y);
        int sourceStartX = (current_frame % this.frame_per_cols) * frame_width;
        int sourceStartY = (current_frame / this.frame_per_cols) * frame_height;
        if(this.visible){
            Game.canvas.drawImage(i,x,y,x + frame_width, y + frame_height, sourceStartX, sourceStartY, sourceStartX + frame_width, sourceStartY + frame_height,null);
        }

        this.frame_count += this.frame_rate;
        if(this.frame_count > 1){
            this.frame_count = 0;
            current_frame++;
        }
        current_frame = current_frame % frames;

        this.left = this.x;
        this.top = this.y;
        this.right = this.x + this.frame_width;
        this.bottom = this.y + this.frame_height;

        drawBoundaries();
        
    }
    
}
