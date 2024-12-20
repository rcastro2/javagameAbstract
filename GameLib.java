import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.net.URL;
import java.util.HashMap;

import javax.sound.sampled.*;

class Keys {
    public static boolean[] pressed = new boolean[200];
    public static int LEFT = 37, RIGHT = 39, UP = 38, DOWN = 40, A = 65, 	B = 66, 	C = 67, 	D = 68, 	E = 69, 	F = 70, 	G = 71, 	H = 72, 	I = 73, 	J = 74, 	K = 75, 	L = 76, 	M = 77, 	N = 78, 	O = 79, 	P = 80, 	Q = 81, 	R = 82, 	S = 83, 	T = 84, 	U = 85, 	V = 86, 	W = 87, 	X = 88, 	Y = 89, 	Z = 90, SPACE = 32, ENTER = 10, D0 = 48, D1 = 49, D2 = 50, D3 = 51, D4 = 52, D5 = 53, D6 = 54, D7 = 55, D8 = 56, D9 = 57, PLUS = 43, MINUS = 44, SHIFT = 16, CTRL = 17, ALT = 18;
}

class Mouse {
    public static int x;
    public static int y;
    public static boolean leftClick;
    public static boolean rightClick;
    public static boolean leftPressed;
    public static boolean rightPressed;
}

class Font {
    public java.awt.Font font;
    public Color color;
    public Color shadow;

    public Font(String family, int size) {
        this.selectFont(family, size);        
        this.color = Color.BLACK;
        this.shadow = null;
    }
    public Font(String family, int size, Color color) {
        this.selectFont(family, size); 
        this.color = color;
        this.shadow = null;
    }
    public Font(String family, int size, Color color, Color shadow) {
        this.selectFont(family, size); 
        this.color = color;
        this.shadow = shadow;
    }
    public void selectFont(String family, int size){
        if(family.substring(family.length()-3).equals("ttf")){
            try{
                java.io.File fontFile = new File(family);
                java.awt.Font customFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontFile);
                font = customFont.deriveFont((float)size); // Set the desired font size
            }catch (FontFormatException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(this.font == null){
                    this.font = new java.awt.Font("Arial", java.awt.Font.BOLD, size);
                }
            }
        }else{
            this.font = new java.awt.Font(family, java.awt.Font.BOLD, size);
        }
    }
}

class Coordinate{
    public int x;
    public int y;
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
}

class Sound {  
    private Clip clip;
    
    public Sound(String soundFileName) {
       try {
          URL url = this.getClass().getClassLoader().getResource(soundFileName);
          AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
          clip = AudioSystem.getClip();
          clip.open(audioInputStream);
          setVolume(0);
          clip.start();
       } catch (UnsupportedAudioFileException e) {
          e.printStackTrace();
       } catch (IOException e) {
          e.printStackTrace();
       } catch (LineUnavailableException e) {
          e.printStackTrace();
       }
    }
    public void play() {
        clip.loop(1);
    }
    public void setVolume(double level) {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (volume != null) {
            volume.setValue((int)(level / 100));     
        }
    } 
}

class Game{
    public static Graphics2D canvas;
    public static int width;
    public static int height;
    public static Sprite background;
    public static Coordinate[][] backgroundXY = new Coordinate[3][3];
    public static String state;

    public static void drawText(String msg, int x, int y){
        canvas.drawString(msg,x,y);
    }
    public static void drawText(String msg, int x, int y, Font f){
        canvas.setFont(f.font);
        if(f.shadow != null){
            canvas.setColor(f.shadow);
            canvas.drawString(msg,x + 2,y + 2);
        }
        canvas.setColor(f.color);
        canvas.drawString(msg,x,y);
    }

    public static int mod(int a, int b) {
        //Mimic how Python treats modulus of negative numbers
        int c = a % b;
        return (c < 0) ? c + b : c;
    }

    public static void setBackground(Sprite bkGraphics){
        background = bkGraphics;
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
              backgroundXY[r][c] = new Coordinate((int)(background.width * (c-1) + background.width / 2),(int)(background.height * (r-1) + background.height / 2));
            }
        }
    }
    public static void drawBackground(){
        if(background != null){
            background.draw();
        }
    }
    public static void scrollBackground(String direction, int amt){
        int width = (int)background.width;
        int height = (int)background.height;

        if(direction.contains("left")){
            for(int r = 0; r < 3; r++){
                for(int c = 0; c < 3; c++){
                    if(backgroundXY[r][c].x + width  / 2 <= 0){
                        backgroundXY[r][c].x = backgroundXY[r][mod((c+2),3)].x + width;
                    }  
                    backgroundXY[r][c].x -= amt;
                }
            }
        }
        if(direction.contains("right")){
            for(int r = 0; r < 3; r++){
                for(int c = 2; c >= 0; c--){
                    if(backgroundXY[r][c].x - width  / 2 >= Game.width){
                        backgroundXY[r][c].x = backgroundXY[r][mod((c-2),3)].x - width;
                    }  
                    backgroundXY[r][c].x += amt;
                }
            }
        }
        if(direction.contains("up")){
            for(int c = 0; c < 3; c++){
                for(int r = 0; r < 3; r++){
                    if(backgroundXY[r][c].y + height  / 2 <= 0){
                        backgroundXY[r][c].y = backgroundXY[mod((r+2),3)][c].y + height;
                    }  
                    backgroundXY[r][c].y -= amt;
                }
            }
        }
        if(direction.contains("down")){
            for(int c = 0; c < 3; c++){
                for(int r = 2; r >= 0; r--){
                    if(backgroundXY[r][c].y - height  / 2 >= Game.height){
                        backgroundXY[r][c].y = backgroundXY[mod((r-2),3)][c].y - height;
                    }  
                    backgroundXY[r][c].y += amt;
                }
            }
        }
        for(int r = 0; r < 3; r++)
            for(int c = 0; c < 3; c++){
                background.moveTo(backgroundXY[r][c].x,backgroundXY[r][c].y);
            }           
    }
    public static int rnd(double l,double u){
        return (int)(Math.random()*(u-l)+l);
    }
    public static void exit(){
        App.frame.dispose();
    }
}

abstract class GameObject{
    public int x, y;
    public double dx, dy, dxsign, dysign;
    public double width, height;
    public int left,right,top,bottom;
    public boolean visible;
    public String borderType, rotate;
    public double scale, rotateAngle, speed, moveAngle;
    public HashMap<String,Object> properties;

    public GameObject(){
        this.x = (int)(Game.width / 2);
        this.y = (int)(Game.height / 2);
        this.visible = true;
        this.borderType = null;
        this.scale = 1;
        this.rotateAngle = 0;
        this.dxsign = 1;
        this.dysign = 1;
        properties = new HashMap<>();
    }
    public void set(String key, Object value){
        properties.put(key, value);
    }
    public Object get(String key){
        return properties.get(key);
    }

    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
        if(Game.canvas != null){
            this.draw();
        }
    }

    public void setSpeed(double speed, double angle){
        this.speed = speed;
        this.moveAngle = Math.toRadians(angle);
        this.calculateSpeedDeltas();
        
    }
    public void move(boolean bounce){
        int rebound = 1;
        if(bounce){
            this.updateRect();
            if(this.left - this.dx <= 0 || this.right + this.dx >= Game.width){
                this.dxsign = -this.dxsign;
                rebound = 2;
            }
            if(this.top - this.dy <= 0 || this.bottom + this.dy >= Game.height ){
                this.dysign = -this.dysign;
                rebound = 2;
            }
        }
        this.calculateSpeedDeltas();
        this.x += this.dx * this.dxsign * rebound;
        this.y += this.dy * this.dysign * rebound;
        this.draw();
    }
    public void move(){
        this.move(false);
    }

    public void calculateSpeedDeltas(){
        this.dx = this.speed * Math.cos(this.moveAngle );
        this.dy = this.speed * Math.sin(this.moveAngle );
    }

    public void rotateBy(double angle, String direction){
        double rad = Math.toRadians(angle);
        this.rotate = direction;
        if(direction.equals("left")){
            rad = -rad;
        }
        this.rotateAngle = (this.rotateAngle + rad ) % (2 * Math.PI) ;
        this.moveAngle = (this.moveAngle + rad) % (2 * Math.PI);
        this.calculateSpeedDeltas();
    }

    public double angleTo(GameObject obj){
        double dx = obj.x - this.x;
        double dy = obj.y - this.y;

        if(dx == 0){
            dx = 0.0001;
        }
        double angle = Math.atan(dy/dx) * 180 / Math.PI;
        if(dx < 0){
            angle += 180;
        }
        return angle;
    }

    public void rotateTowards(GameObject obj){
        this.rotateAngle = Math.toRadians(this.angleTo(obj)) % (2 * Math.PI);
        this.moveAngle = Math.toRadians(this.angleTo(obj)) % (2 * Math.PI);

    }

    abstract public void draw();
    
    public boolean collidedWith(GameObject obj, String shape){
        boolean collision = false;
        if(obj.visible && this.visible){
            if(shape.equals("circle")){
                double dx = this.x - obj.x;
                double dy = this.y - obj.y;
                double d = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
                collision = d < (int)((this.width/2*this.scale+this.height/2*this.scale)/2) + (int)((obj.width/2*obj.scale+obj.height/2*obj.scale)/2);
            }else if(shape.equals("rect")){
                collision = !(obj.left > this.right || obj.right < this.left || obj.top > this.bottom || obj.bottom < this.top);
            }
        }
        return collision;
    }

    protected void drawBoundaries(){
        if(borderType != null && borderType.equals("circle")){
            Game.canvas.fillOval(this.x,this.y,5,5);
            Game.canvas.drawOval((int)(this.x - this.width/2*this.scale),(int)(this.y - this.height/2*this.scale),(int)(this.width*this.scale),(int)(this.height*this.scale));
        }else if (borderType != null && borderType.equals("rect")){
            Game.canvas.drawRect((int)(this.x - this.width/2*this.scale),(int)(this.y - this.height/2*this.scale),(int)(this.width*this.scale),(int)(this.height*this.scale));
        }
    }

    public boolean isOffScreen(String side){
        boolean offscreen = false;
        if(side=="all")
                offscreen = this.right < 0 || this.left > Game.width || this.top > Game.height || this.bottom < 0;
        else if(side == "bottom")
                offscreen = this.top > Game.height;
        else if(side == "top")
                offscreen = this.bottom < 0;
        else if(side == "left")
                offscreen = this.right < 0;
        else if(side == "right")
                offscreen = this.left > Game.width;	
        return offscreen;
    }
    public boolean isOffScreen(){
        return this.isOffScreen("all");
    }

    public void updateRect(){
        int width = (int)(this.width*this.scale);
        int height = (int)(this.height*this.scale);
  
        this.left = this.x - width/2;
        this.top = this.y - height/2;
        this.right = this.x + width/2;
        this.bottom = this.y + height/2;
    }
}

class Sprite extends GameObject{
    protected Image i;
    protected Image original;

    public Sprite(String fn) {
        URL url = getClass().getClassLoader().getResource(fn);
        //System.out.println(url);
        if (url != null) {
          this.i = new ImageIcon(url).getImage();
          this.original = new ImageIcon(url).getImage();
          this.width = this.i.getWidth(null);
          this.height = this.i.getHeight(null);
        } else {
          System.err.println("Failed to load image: " + fn);
        }
        
    }
    public Sprite(String fn, int x, int y) {
        this.x = x;
        this.y = y;
        URL url = getClass().getClassLoader().getResource(fn);
        if (url != null) {
          this.i = new ImageIcon(url).getImage();
          this.original = new ImageIcon(url).getImage();
          this.width = this.i.getWidth(null);
          this.height = this.i.getHeight(null);
        } else {
          System.err.println("Failed to load image: " + fn);
        }
    }

    public void draw(){
        if(this.visible){
            Graphics2D g2d = (Graphics2D) Game.canvas;
            AffineTransform oldTransform = g2d.getTransform();
    
            // Set the rotation and scaling transformation
            AffineTransform transform = new AffineTransform();
            
            // Translate to the object's position
            transform.translate(this.x, this.y);
            
            // Rotate around the center of the image (width / 2, height / 2)
            transform.rotate(this.rotateAngle + Math.PI / 2, 0, 0);
            
            // Apply the scaling
            transform.scale(this.scale, this.scale);
            
            // Set the transformation to the graphics context
            g2d.setTransform(transform);
    
            // Draw the image centered at (0, 0) in the transformed space
            g2d.drawImage(i, (int) (-this.width / 2), (int) (-this.height / 2), null);
    
            // Restore the previous transformation
            g2d.setTransform(oldTransform);
        }
        updateRect();
        drawBoundaries();
    }

    public void resizeTo(int w, int h){
        Image image = this.original.getScaledInstance(w, h,  java.awt.Image.SCALE_SMOOTH);
        this.i = new ImageIcon(image).getImage();
    }
    public void resizeBy(double by){
        double pct = 1 + by / 100;
        this.width *= pct;
        this.height *= pct;
        this.resizeTo((int)this.width, (int)this.height);
    }
}

class Animation extends Sprite{
    public int current_frame, frames, frame_per_rows, frame_per_cols;
    private double frame_width,frame_height, frame_rate, frame_count;
    public boolean loop;

    public Animation(String fn, int frames, int frame_width, int frame_height, double frame_rate) {
        super(fn);
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
    }
    public void resizeBy(double by){
        this.scale += by / 100;
    }

    public void draw(boolean loop){      
        if(this.visible){  
            int sourceStartX = (this.current_frame % this.frame_per_cols) * (int)this.frame_width;
            int sourceStartY = (this.current_frame / this.frame_per_cols) * (int)this.frame_height;             
            // Save the current transformation
            Graphics2D g2d = (Graphics2D) Game.canvas;
            AffineTransform oldTransform = g2d.getTransform();

            // Set the rotation and scaling transformation
            AffineTransform transform = new AffineTransform();

            // Translate to the object's position (center of the object)
            transform.translate(this.x, this.y);

            // Rotate around the center of the image (width / 2, height / 2)
            transform.rotate(this.rotateAngle, 0, 0);

            // Apply the scaling
            transform.scale(this.scale, this.scale);

            // Set the transformation to the graphics context
            g2d.setTransform(transform);

            // Draw the correct frame from the sprite sheet
            g2d.drawImage(i,
                (int) (-this.frame_width / 2), (int) (-this.frame_height / 2), 
                (int) (this.frame_width / 2), (int) (this.frame_height / 2), 
                sourceStartX, sourceStartY, 
                sourceStartX + (int) this.frame_width, 
                sourceStartY + (int) this.frame_height, 
                null);
                
            // Restore the previous transformation
            g2d.setTransform(oldTransform);   
            this.frame_count += this.frame_rate;
            if(this.frame_count > 1){
                this.frame_count = 0;
                this.current_frame++;
            }
            this.current_frame = this.current_frame % this.frames;
            
            if(!loop && this.current_frame == this.frame_count){
                this.visible = false;
                this.current_frame = 0;
            }
            
        }

        updateRect();
        drawBoundaries();     
    }
    public void draw(){
        this.draw(true);
    }
}

class Shape extends GameObject{
    public String shape;
    public int side, size;
    public Color color;
    public int XPoints[], YPoints[];
    public double reference_angle, angle_offset, reference_angles[]; 

    public Shape(String shape,int arg1,int arg2,Color color){
        this.shape = shape;
        this.color = color;
        if(shape.equals("ellipse")){
            this.width = arg1;
            this.height = arg2;
        }else if(shape.equals("polygon")){
            this.side = arg1;
            this.size = arg2;
            this.width = this.size * 2;
            this.height = this.size * 2;
            this.reference_angle = 2 * Math.PI / this.side;
            this.angle_offset = Math.PI / this.side;
            this.XPoints = new int[this.size];
            this.YPoints = new int[this.size];
            this.updatePoints();
        }else if(shape.equals("rectangle")){
            this.side = 4;
            this.width = arg1;
            this.height = arg2;
            this.updateRectangleAngles();
            this.XPoints = new int[4];
            this.YPoints = new int[4];
        }
    }
    public void updateRectangleAngles(){
        this.size = (int)(Math.sqrt(Math.pow(this.width,2) + Math.pow(this.height,2) / 2));
        double angle1 = Math.PI - 2*Math.atan(this.width/this.height);
        double angle2 = Math.PI - 2*Math.atan(this.height/this.width);
        double tmp[] = {angle1/2,angle2,angle1,angle2};
        this.reference_angles =  tmp;
    }
    public void updatePoints(){
        double x = 0, y = 0, theta = 0;

        this.updateRectangleAngles();
        for(int index = 0; index < this.side; index++){
            if(this.shape.equals("polygon")){
                x = this.x + this.size * Math.cos(this.rotateAngle + this.reference_angle * index + this.angle_offset);
                y = this.y + this.size * Math.sin(this.rotateAngle + this.reference_angle * index + this.angle_offset);
            }else if(this.shape.equals("rectangle")){
                theta += this.reference_angles[index];
                  x = this.x + this.size * Math.cos(this.rotateAngle + theta);
                  y = this.y + this.size * Math.sin(this.rotateAngle + theta);
            }
            this.XPoints[index] = (int)x;
            this.YPoints[index] = (int)y;
        }
    }
    public void draw(){
        if(this.visible){
            Game.canvas.setPaint(this.color);
            this.updatePoints();
            if(shape.equals("ellipse")){
                Game.canvas.fillOval((int)(this.x - this.width/2),(int)(this.y - this.height/2),(int)this.width,(int)this.height);
            }else if(this.shape.equals("polygon") || this.shape.equals("rectangle")){
                this.updatePoints();
                Game.canvas.fillPolygon(this.XPoints,this.YPoints,this.side);
            }
        } 
    }
}

