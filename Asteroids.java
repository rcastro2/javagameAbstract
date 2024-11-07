import java.awt.*;
import java.util.ArrayList;

public class Asteroids implements GameLogic{
    Sprite bk,ship;
    Animation plasmaball, explosion, crash;
    Shape healthBar, ammoBar, asteroidBar;
    Font gameFont, basicFont, barFont, winFont, loseFont;
    int introCount = 0;
    
    ArrayList<Animation> asteroids;
    ArrayList<Animation> energies;
    class Player{
        public static int asteroidsAvaible;
        public static int health;
        public static int ammo;
    }

    public Asteroids() {
        bk = new Animation("images/field_5.png",5,1000,1000,1);
        Game.setBackground(bk);

        healthBar = new Shape("rectangle",100,10,Color.GREEN);
        healthBar.moveTo(120,20);
        ammoBar = new Shape("rectangle",100,10,Color.ORANGE);
        ammoBar.moveTo(120, 50);
        asteroidBar = new Shape("rectangle",100,10,Color.CYAN);
        asteroidBar.moveTo(120, 80);

        plasmaball = new Animation("images/plasmaball1.png",11,352/11,32,1);
        plasmaball.visible = false;
        explosion = new Animation("images/explosion.png",22,1254/22,64,0.30);
        explosion.visible = false;
        crash = new Animation("images/explosion4.png",20,640 / 5, 512 / 4,2);
        crash.visible = false;
        ship = new Sprite("images/hero.gif");

        gameFont = new Font("images/battlestar.ttf",100,Color.WHITE,Color.CYAN);
        basicFont = new Font("Arial",40,Color.WHITE,Color.CYAN);
        barFont = new Font("Arial",18,Color.BLACK,Color.WHITE);
        winFont = new Font("images/battlestar.ttf",100,Color.WHITE,Color.GREEN);
        loseFont = new Font("images/battlestar.ttf",100,Color.WHITE,Color.RED);

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
                level1();
                break;
            case "Level 2":
                level2();
                break;
            case "Level 3":
                level3();
                break;
            case "Game Over":
                gameOver();
                break;
        } 
    }

    public void startGame(){
        Game.drawBackground();
        Game.drawText("Asteroids", 180, 150, gameFont);
        Game.drawText("Press [ENTER] to start", 300,Game.height - 100,basicFont);
        ship.draw();
        if(Keys.pressed[Keys.ENTER]){
            Player.health = 100;
            healthBar.width = 100;
            healthBar.x = 120;
            Player.ammo = 20;
            ammoBar.width = 100;
            ammoBar.x = 120;
            asteroidBar.width = 100;
            asteroidBar.x = 120;
            ship.moveTo(Game.width / 2, Game.height / 2);
            ship.rotateAngle = 0;
            ship.moveAngle = 0;
            energies = generateEnergies(1,5);
            asteroids = generateAsteroids(1, 5,2);
            Game.state = "Level 1";
        }
    }
    public void gameOver(){
        Game.drawBackground();
        if(Player.health > 0){
            Game.drawText("You Win!", 190, 150, winFont);
        }else{
            Game.drawText("You Lose", 180, 150, loseFont);
        }
        
        Game.drawText("Play Again? [Y/N]", 300,Game.height - 100,basicFont);
        
        if(Keys.pressed[Keys.Y]){
            Game.state = "startGame";
        }else if(Keys.pressed[Keys.N]){
            Game.exit();
        }

    }
    public boolean levelCommonProcesses(){
        Game.drawBackground();
        healthBar.width = Player.health + 20;
        healthBar.x = (int)(40 + Player.health);
        healthBar.draw();
        Game.drawText(" " + Player.health, 30, healthBar.y + 5, barFont);
        ammoBar.width = Player.ammo * 5 + 20 ;
        ammoBar.x = (int)(40 + Player.ammo * 5);
        ammoBar.draw();
        Game.drawText(" " + Player.ammo, 30, ammoBar.y + 5, barFont);
        asteroidBar.width = Player.asteroidsAvaible * 5 + 20;
        asteroidBar.x = (int)(40 + Player.asteroidsAvaible * 5);
        asteroidBar.draw();
        Game.drawText(" " + Player.asteroidsAvaible, 30, asteroidBar.y + 5, barFont);

        ship.move();
        crash.draw(false);
        heroControl();
        if(introCount < 200){
            Game.drawText(Game.state, 250, 200, gameFont);
            introCount++;
            return false;
        }
        
        plasmaball.move();
        if(plasmaball.isOffScreen()){
            plasmaball.visible = false;
        }
        explosion.draw(false);
        return true;
    }
    public void level1(){
        if(levelCommonProcesses() == false){
            return;
        }else if(Player.asteroidsAvaible == 0){
            introCount = 0;
            energies = generateEnergies(1,10);
            asteroids = generateAsteroids(2, 10,3);
            Game.state = "Level 2";
        }else{
            processAsteroids(asteroids);
            processEnergies();
        }

    }
    public void level2(){
        if(levelCommonProcesses() == false){
            return;
        }else if(Player.asteroidsAvaible == 0){
            introCount = 0;
            energies = generateEnergies(1,10);
            asteroids = generateAsteroids(3, 5,2);
            Player.asteroidsAvaible = 15;
            Game.state = "Level 3";
        }else{
            processAsteroids(asteroids);
            processEnergies();
        }
    }
    public void level3(){
        
        if(levelCommonProcesses() == false){
            return;
        }else if(Player.asteroidsAvaible == 0){
            Game.state = "Game Over";
        }else{
            ArrayList<Animation> newAsteroids = new ArrayList<>();
            for(Animation asteroid: asteroids){
                asteroid.move();
                screenWrap(asteroid);
                if(plasmaball.collidedWith(asteroid,"circle")){
                    asteroid.visible = false;
                    Player.asteroidsAvaible--;
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
                }
                if(asteroid.collidedWith(ship,"circle")){
                    asteroid.visible = false;
                    crash.visible = true;
                    crash.moveTo(ship.x,ship.y);
                    if((Integer)asteroid.get("hits") > 1){
                        Player.asteroidsAvaible -= 3;
                        Player.health -= 30;
                    }else{
                        Player.asteroidsAvaible--;
                        Player.health -= 10;
                    }
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
        }
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
        }
        screenWrap(ship);
        if(Player.health <= 0){
            Game.state = "Game Over";
        }
    }
    public void screenWrap(Sprite obj){
        if(obj.x <= 0){
            obj.x = Game.width;
        }else if(obj.x >= Game.width){
            obj.x = 0;
        }else if(obj.y <= 0){
            obj.y = Game.height;
        }else if(obj.y >= Game.height){
            obj.y = 0;
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
            }
        }
    }

    public ArrayList<Animation> generateAsteroids(int type, int amount, int speed){
        Player.asteroidsAvaible = amount;
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
            asteroid.move();
            screenWrap(asteroid);
            if(plasmaball.collidedWith(asteroid,"circle")){
                asteroid.visible = false;
                plasmaball.visible = false;
                explosion.visible = true;
                explosion.moveTo(asteroid.x,asteroid.y);
                Player.asteroidsAvaible--;
            }
            if(asteroid.collidedWith(ship,"circle")){
                asteroid.visible = false;
                crash.visible = true;
                crash.moveTo(ship.x,ship.y);
                Player.asteroidsAvaible--;
                Player.health -= 10;
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
