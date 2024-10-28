import java.awt.*;
import java.util.ArrayList;

public class Asteroids implements GameLogic{
    Sprite bk,ship;
    Animation asteroid;
    
    ArrayList<Coordinate> targets = new ArrayList<Coordinate>();
    int speed = 2;
    Coordinate you = new Coordinate(Game.width/2, Game.height/2);

    public Asteroids() {
        bk = new Animation("images/field_5.png",5,1000,1000,1);
        asteroid = new Animation("images/asteroid1t.gif",41, 2173/41,52,1);
        ship = new Sprite("images/hero.gif");
        asteroid.setSpeed(4, 60);
        Game.setBackground(bk);

    }
    public String getTitle(){
        //Use in App.java to set the title of the JFrame
        return "Asteroids";
    }
    public void gameLoop(){
        Game.drawBackground();
        ship.draw();
        asteroid.move(true);

        if(Keys.pressed[Keys.LEFT]){
            ship.rotateBy(2,"left"); 
        }
        if(Keys.pressed[Keys.RIGHT]){
            ship.rotateBy(2,"right");  
        }

        String loc = String.format("(%d)",(int)Math.toDegrees(ship.rotateAngle));
        Game.drawText(loc,50,50, new Font("Arial",20,Color.WHITE,Color.RED));
    }

    public void print(Object obj){
        System.out.println(obj.toString());
    }
}
