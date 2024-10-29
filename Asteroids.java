import java.awt.*;
import java.util.ArrayList;

public class Asteroids implements GameLogic{
    Sprite bk,ship;
    Animation plasmaball, explosion, crash;
    
    ArrayList<Animation> asteroids = new ArrayList<Animation>();
    int speed = 2;
    Coordinate you = new Coordinate(Game.width/2, Game.height/2);

    public Asteroids() {
        bk = new Animation("images/field_5.png",5,1000,1000,1);
        for(int i = 0; i < 5; i++){
            Animation asteroid = new Animation("images/asteroid1t.gif",41, 2173/41,52,1); 
            int angle = Game.rnd(0,8) * 45 + 30;
            asteroid.setSpeed(4, angle);
            asteroid.x = Game.rnd(asteroid.width,Game.width-asteroid.width);
            asteroid.y = Game.rnd(asteroid.height,Game.height-asteroid.height);
            asteroids.add(asteroid);
        }
        plasmaball = new Animation("images/plasmaball1.png",11,352/11,32,1);
        plasmaball.visible = false;
        explosion = new Animation("images/explosion.png",22,1254/22,64,0.30);
        explosion.visible = false;
        crash = new Animation("images/explosion4.png",20,640 / 5, 512 / 4,2);
        crash.visible = false;
        ship = new Sprite("images/hero.gif");

        Game.setBackground(bk);
    }
    public String getTitle(){
        //Use in App.java to set the title of the JFrame
        return "Asteroids";
    }
    public void gameLoop(){
        Game.drawBackground();
        ship.move();
        crash.draw(false);
        for(Animation asteroid: asteroids){
            asteroid.move(true);
            if(plasmaball.collidedWith(asteroid,"circle")){
                asteroid.visible = false;
                plasmaball.visible = false;
                explosion.visible = true;
                explosion.moveTo(asteroid.x,asteroid.y);
            }
            if(asteroid.collidedWith(ship,"circle")){
                asteroid.visible = false;
                crash.visible = true;
                crash.moveTo(ship.x,ship.y);
            }
        }
        
        plasmaball.move();
        explosion.draw(false);

        if(Keys.pressed[Keys.UP]){
            ship.speed = 4;
        }else{
            ship.speed = 0;
        }
        if(Keys.pressed[Keys.SPACE] && !plasmaball.visible){
            plasmaball.visible = true;
            plasmaball.moveTo(ship.x,ship.y);
            plasmaball.moveAngle = ship.moveAngle;
            plasmaball.speed = 6;
        }
        if(plasmaball.isOffScreen()){
            plasmaball.visible = false;
        }

        if(Keys.pressed[Keys.LEFT]){
            ship.rotateBy(3,"left"); 
        }
        if(Keys.pressed[Keys.RIGHT]){
            ship.rotateBy(3,"right");  
        }   
    }

    public void print(Object obj){
        System.out.println(obj.toString());
    }
}
