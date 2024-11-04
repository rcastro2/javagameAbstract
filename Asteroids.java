import java.awt.*;
import java.util.ArrayList;

public class Asteroids implements GameLogic{
    Sprite bk,ship;
    Animation plasmaball, explosion, crash;
    Shape healthBar, ammoBar;
    Font gameFont, basicFont;
    int introCount = 0;
    
    ArrayList<Animation> asteroids;
    ArrayList<Animation> energies;
    class Player{
        public static int asteroidsDestroyed = 0;
        public static int health = 100;
        public static int ammo = 20;
    }

    public Asteroids() {
        bk = new Animation("images/field_5.png",5,1000,1000,1);
        Game.setBackground(bk);

        healthBar = new Shape("rectangle",100,10,Color.GREEN);
        healthBar.moveTo(110,20);
        ammoBar = new Shape("rectangle",100,10,Color.ORANGE);
        ammoBar.moveTo(110, 50);

        plasmaball = new Animation("images/plasmaball1.png",11,352/11,32,1);
        plasmaball.visible = false;
        explosion = new Animation("images/explosion.png",22,1254/22,64,0.30);
        explosion.visible = false;
        crash = new Animation("images/explosion4.png",20,640 / 5, 512 / 4,2);
        crash.visible = false;
        ship = new Sprite("images/hero.gif");

        gameFont = new Font("images/battlestar.ttf",100,Color.WHITE,Color.CYAN);
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
            case "Level 1":
                level(1);
                break;
            case "Level 2":
                level(2);
                break;
            case "Level 3":
                level3();
                break;
        } 
    }

    public void startGame(){
        Game.drawBackground();
        Game.drawText("Asteroids", 180, 150, gameFont);
        Game.drawText("Press [SPACE] to start", 300,Game.height - 100,basicFont);
        ship.draw();
        if(Keys.pressed[Keys.SPACE]){
            energies = generateEnergies(1,5);
            asteroids = generateAsteroids(1, 5,2);
            Game.state = "Level 1";
        }
    }
    public void level(int n){
        Game.drawBackground();
        healthBar.draw();
        ammoBar.draw();

        ship.move();
        crash.draw(false);
        if(introCount < 200){
            Game.drawText(Game.state, 250, 200, gameFont);
            introCount++;
            return;
        }
        if(Player.asteroidsDestroyed == asteroids.size()){
            Player.asteroidsDestroyed = 0;
            introCount = 0;
            energies = generateEnergies(1,n * 4);
            asteroids = generateAsteroids(n + 1, 5*(n+1),n + 2);
            Game.state = "Level " + (n + 1);
        }
        processAsteroids(asteroids);
        processEnergies();

        heroControl();
        plasmaball.move();
        if(plasmaball.isOffScreen()){
            plasmaball.visible = false;
        }
        explosion.draw(false);
    }
    public void level3(){
        ArrayList<Animation> newAsteroids = new ArrayList<>();
        Game.drawBackground();
        healthBar.draw();
        ammoBar.draw();

        ship.move();
        crash.draw(false);
        if(introCount < 200){
            Game.drawText(Game.state, 250, 200, gameFont);
            introCount++;
            return;
        }
        if(Player.asteroidsDestroyed == asteroids.size() * 3){
            Game.state = "Game Over";
        }
        for(Animation asteroid: asteroids){
            asteroid.move(true);
            if(plasmaball.collidedWith(asteroid,"circle")){
                asteroid.visible = false;
                if((Integer)asteroid.get("hits") > 1){
                    for(int i = 0; i < 2; i++){
                        Animation a = new Animation("images/asteroid3.png",30, 510/6,500/5,0.5);
                        a.resizeBy(-50);
                        a.set("hits",1);
                        int angle = Game.rnd(0,8) * 45 + 30;
                        a.setSpeed(2, angle);
                        a.x = asteroid.x;
                        a.y = asteroid.y;
                        newAsteroids.add(a);
                    }
                }
                
                plasmaball.visible = false;
                explosion.visible = true;
                explosion.moveTo(asteroid.x,asteroid.y);
                Player.asteroidsDestroyed++;
            }
            if(asteroid.collidedWith(ship,"circle")){
                asteroid.visible = false;
                crash.visible = true;
                crash.moveTo(ship.x,ship.y);
                Player.asteroidsDestroyed++;
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
        for(Animation a: newAsteroids){
            asteroids.add(a);
        }
        processEnergies();

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
            ammoBar.width -= 5;
            ammoBar.x -= 5;
        }
    }
    public ArrayList<Animation> generateEnergies(int type, int amount){
        ArrayList<Animation> energies = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            Animation energy;
            switch(type){
                case 1:
                energy = new Animation("images/plasmaball3.png",5,60,60,2);
                    break;
                default:
                    energy = new Animation("images/plasmaball3.png",5,60,60,2);
            }
            energy.x = Game.rnd(energy.width,Game.width-energy.width);
            energy.y = Game.rnd(energy.height,Game.height-energy.height);
            energies.add(energy);
        }
        return energies;
    }
    public void processEnergies(){
        for(Animation energy:energies){
            energy.draw();
            if(ship.collidedWith(energy,"circle")){
                energy.visible = false;
                Player.ammo += 1;
                ammoBar.width += 10;
                ammoBar.x += 10;
            }
        }
    }

    public ArrayList<Animation> generateAsteroids(int type, int amount, int speed){
        ArrayList<Animation> asteroids = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            Animation asteroid;
            switch(type){
                case 1:
                    asteroid = new Animation("images/asteroid1t.gif",41, 2173/41,52,1); 
                    break;
                case 2:
                    asteroid = new Animation("images/asteroid2.png",30, 960/30,32,1);
                    break;
                case 3:
                    asteroid = new Animation("images/asteroid3.png",30, 510/6,500/5,0.5);
                    asteroid.set("hits",2);
                    break;
                default:
                    asteroid = new Animation("images/asteroid1t.gif",41, 2173/41,52,1); 
            }
             
            int angle = Game.rnd(0,8) * 45 + 30;
            asteroid.setSpeed(speed, angle);
            asteroid.x = Game.rnd(asteroid.width,Game.width-asteroid.width);
            asteroid.y = Game.rnd(asteroid.height,Game.height-asteroid.height);
            asteroids.add(asteroid);
        }
        return asteroids;
    }
    public void processAsteroids(ArrayList<Animation> asteroids){
        for(Animation asteroid: asteroids){
            asteroid.move(true);
            if(plasmaball.collidedWith(asteroid,"circle")){
                asteroid.visible = false;
                plasmaball.visible = false;
                explosion.visible = true;
                explosion.moveTo(asteroid.x,asteroid.y);
                Player.asteroidsDestroyed++;
            }
            if(asteroid.collidedWith(ship,"circle")){
                asteroid.visible = false;
                crash.visible = true;
                crash.moveTo(ship.x,ship.y);
                Player.asteroidsDestroyed++;
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
