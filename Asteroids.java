import java.awt.*;
import java.util.ArrayList;

public class Asteroids implements GameLogic{
    Sprite bk,ship;
    Animation plasmaball, explosion, crash;
    Shape healthBar, ammoBar;
    Font gameFont, basicFont;
    
    ArrayList<Animation> asteroidsLevel1;
    ArrayList<Animation> energies;
    class Player{
        public static int score = 0;
        public static int health = 100;
        public static int ammo = 10;
    }

    public Asteroids() {
        bk = new Animation("images/field_5.png",5,1000,1000,1);
        Game.setBackground(bk);

        healthBar = new Shape("rectangle",100,10,Color.GREEN);
        healthBar.moveTo(110,20);
        ammoBar = new Shape("rectangle",100,10,Color.ORANGE);
        ammoBar.moveTo(110, 50);
        
        asteroidsLevel1 = new ArrayList<Animation>();
        for(int i = 0; i < 5; i++){
            Animation asteroid = new Animation("images/asteroid1t.gif",41, 2173/41,52,1); 
            int angle = Game.rnd(0,8) * 45 + 30;
            asteroid.setSpeed(2, angle);
            asteroid.x = Game.rnd(asteroid.width,Game.width-asteroid.width);
            asteroid.y = Game.rnd(asteroid.height,Game.height-asteroid.height);
            asteroidsLevel1.add(asteroid);
        }

        energies = new ArrayList<Animation>();
        for(int i = 0; i < 5; i++){
            Animation energy = new Animation("images/plasmaball3.png",5,60,60,2);
            energy.x = Game.rnd(energy.width,Game.width-energy.width);
            energy.y = Game.rnd(energy.height,Game.height-energy.height);
            energies.add(energy);
        }
        plasmaball = new Animation("images/plasmaball1.png",11,352/11,32,1);
        plasmaball.visible = false;
        explosion = new Animation("images/explosion.png",22,1254/22,64,0.30);
        explosion.visible = false;
        crash = new Animation("images/explosion4.png",20,640 / 5, 512 / 4,2);
        crash.visible = false;
        ship = new Sprite("images/hero.gif");

        gameFont = new Font("images/Arka_solid.ttf",100,Color.WHITE,Color.CYAN);
        basicFont = new Font("Arial",40,Color.WHITE,Color.CYAN);

        Game.state = "startGame";
    }

    public String getTitle(){
        //Use in App.java to set the title of the JFrame
        return "Asteroids";
    }

    public void gameLoop(){
        switch(Game.state){
            case "startGame":
                startGame();
                break;
            case "level1":
                level1();
                break;
        } 
    }

    public void startGame(){
        Game.drawBackground();
        Game.drawText("Asteroids", 180, 150, gameFont);
        Game.drawText("Press [SPACE] to start", 300,Game.height - 100,basicFont);
        ship.draw();
        if(Keys.pressed[Keys.SPACE]){
            Game.state = "level1";
        }
    }
    public void level1(){
        Game.drawBackground();
        healthBar.draw();
        ammoBar.draw();

        ship.move();
        crash.draw(false);

        processAsteroids(asteroidsLevel1);
        
        for(Animation energy:energies){
            energy.draw();
            if(ship.collidedWith(energy,"circle")){
                energy.visible = false;
                Player.ammo += 1;
                ammoBar.width += 10;
                ammoBar.x += 10;
            }
        }
        heroControl();
        plasmaball.move();
        if(plasmaball.isOffScreen()){
            plasmaball.visible = false;
        }
        explosion.draw(false);
    }
    public void heroControl(){
        if(Keys.pressed[Keys.UP]){
            ship.speed = 4;
        }else{
            ship.speed = 0;
        }
        if(Keys.pressed[Keys.LEFT]){
            ship.rotateBy(3,"left"); 
        }
        if(Keys.pressed[Keys.RIGHT]){
            ship.rotateBy(3,"right");  
        } 
        if(Keys.pressed[Keys.SPACE] && !plasmaball.visible && Player.ammo > 0){
            plasmaball.visible = true;
            plasmaball.moveTo(ship.x,ship.y);
            plasmaball.moveAngle = ship.moveAngle;
            plasmaball.speed = 6;
            Player.ammo -= 1;
            ammoBar.width -= 10;
            ammoBar.x -= 10;
        }
    }

    public void processAsteroids(ArrayList<Animation> asteroids){
        for(Animation asteroid: asteroids){
            asteroid.move(true);
            if(plasmaball.collidedWith(asteroid,"circle")){
                asteroid.visible = false;
                plasmaball.visible = false;
                explosion.visible = true;
                explosion.moveTo(asteroid.x,asteroid.y);
                Player.score += 10;
            }
            if(asteroid.collidedWith(ship,"circle")){
                asteroid.visible = false;
                crash.visible = true;
                crash.moveTo(ship.x,ship.y);
                Player.health -= 10;
                healthBar.width = Player.health;
                healthBar.x -= 10;
            }
            for(Animation energy:energies){
                if(asteroid.collidedWith(energy,"circle")){
                    energy.visible = false;
                    explosion.visible = true;
                    explosion.moveTo(asteroid.x,asteroid.y);
                }
            }
        }  
    }
    public void print(Object obj){
        System.out.println(obj.toString());
    }

    
}
