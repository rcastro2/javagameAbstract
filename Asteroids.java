import java.awt.*;
import java.util.ArrayList;

public class Asteroids implements GameLogic{
    Sprite bk,ship, boss;
    Animation plasmaball, explosion, crash, bossEnergy, shield;
    Shape healthBar, ammoBar, asteroidBar;
    Font gameFont, basicFont, barFont, winFont, loseFont;
    int introCount = 0;
    
    ArrayList<Animation> asteroids;
    ArrayList<Animation> energies;
    class Player{
        public static int asteroidsAvaible;
        public static int health;
        public static int ammo;
        public static int shield = 100;
        public static int bonusAmmo = 5;
        public static int bonusHeath = 10;
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
        shield = new Animation("images/aura.png",16,512/4,512/4,1);

        boss = new Sprite("images/boss2.png");
        bossEnergy = new Animation("images/nova.png",5, 80, 80,1);
        boss.resizeBy(-75);

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
            case "Level 4":
                level4();
                break;
            case "Level 5":
                level5();
                break;
            case "Final Level":
                levelFinal();
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
    public boolean levelCommonProcesses(int offsetX, int offsetY){
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
        if(Player.shield > 0){
            shield.moveTo(ship.x,ship.y);
        }
        ship.move();
        crash.draw(false);
        heroControl();
        if(introCount < 200){
            Game.drawText(Game.state, 250 + offsetX, 200 + offsetY, gameFont);
            introCount++;
            return false;
        }
        if(Player.shield > 0){
            Player.shield--;
        }
        
        plasmaball.move();
        if(plasmaball.isOffScreen()){
            plasmaball.visible = false;
        }
        explosion.draw(false);
        return true;
    }
    public boolean levelCommonProcesses(){
        return levelCommonProcesses(0,0);
    }
    public void level1(){
        if(levelCommonProcesses() == false){
            return;
        }else if(Player.asteroidsAvaible == 0){
            introCount = 0;
            energies = generateEnergies(1,10);
            asteroids = generateAsteroids(2, 10,2);
            Player.health += Player.bonusHeath;
            Player.ammo += Player.bonusAmmo;
            Player.shield = 100;
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
            asteroids = generateAsteroids(3, 4,2);
            Player.asteroidsAvaible = 16;
            Player.health += Player.bonusHeath;
            Player.ammo += Player.bonusAmmo;
            Player.shield = 100;
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
            introCount = 0;
            energies = generateEnergies(1,10);
            asteroids = generateAsteroids(4, 8,2);
            Player.asteroidsAvaible = 16;
            Player.health += Player.bonusHeath;
            Player.ammo += Player.bonusAmmo;
            Player.shield = 100;
            Game.state = "Level 4";
        }else{
            ArrayList<Animation> newAsteroids = new ArrayList<>();
            for(Animation asteroid: asteroids){
                asteroid.move();
                screenWrap(asteroid);
                if(plasmaball.collidedWith(asteroid,"circle")){
                    asteroid.visible = false;
                    Player.asteroidsAvaible--;
                    if((Integer)asteroid.get("hits") > 1){
                        for(int i = 0; i < 3; i++){
                            Animation a = new Animation("images/asteroid3.png",30, 510/6,500/5,0.5);
                            a.resizeBy(-50);
                            a.set("hits",1);
                            int angle = Game.rnd(0,10) * 36 + 30;
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
                if(asteroid.collidedWith(ship,"circle") && Player.shield == 0){
                    asteroid.visible = false;
                    crash.visible = true;
                    crash.moveTo(ship.x,ship.y);
                    if((Integer)asteroid.get("hits") > 1){
                        Player.asteroidsAvaible -= 4;
                        Player.health -= 40;
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

    public void level4(){
        if(levelCommonProcesses() == false){
            return;
        }else if(Player.asteroidsAvaible == 0){
            introCount = 0;
            energies = generateEnergies(1,10);
            asteroids = generateAsteroids(5, 12,2);
            double theta = Math.PI / 6;
            double angle = 0;
            for(Animation asteroid: asteroids){
                asteroid.set("angle",angle);
                angle += theta;
            }
            Player.health += Player.bonusHeath;
            Player.ammo += Player.bonusAmmo;
            Player.shield = 100;
            Game.state = "Level 5";
        }else{
            ArrayList<Animation> newAsteroids = new ArrayList<>();
            for(Animation asteroid: asteroids){
                if((Integer)asteroid.get("hits") == 1){
                    asteroid.rotateTowards(ship);
                }
                asteroid.move();
                screenWrap(asteroid);
                if(plasmaball.collidedWith(asteroid,"circle")){
                    asteroid.visible = false;
                    Player.asteroidsAvaible--;
                    if((Integer)asteroid.get("hits") > 1){
                        
                        Animation a = new Animation("images/flame_gas.png",16, 512/4,512/4,0.25);
                        a.resizeBy(-60);
                        a.set("hits",1);
                        a.speed = 2;
                        a.x = asteroid.x;
                        a.y = asteroid.y;
                        newAsteroids.add(a);
                        
                    }
                    plasmaball.visible = false;
                    explosion.visible = true;
                    explosion.moveTo(asteroid.x,asteroid.y);
                }
                if(asteroid.collidedWith(ship,"circle") && Player.shield == 0){
                    asteroid.visible = false;
                    crash.visible = true;
                    crash.moveTo(ship.x,ship.y);
                    if((Integer)asteroid.get("hits") > 1){
                        Player.asteroidsAvaible -= 2;
                        Player.health -= 20;
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

    public void level5(){
        
        if(levelCommonProcesses() == false){
            return;
        }else if(Player.asteroidsAvaible == 0){
            introCount = 0;
            energies = generateEnergies(1,5);
            asteroids = generateAsteroids(6, 10,2);
            for(Animation asteroid:asteroids){
                asteroid.moveTo(bossEnergy.x,bossEnergy.y);
            }
            Player.asteroidsAvaible = 20;
            Player.health += Player.bonusHeath;
            Player.ammo += Player.bonusAmmo;
            Player.shield = 100;
            Game.state = "Final Level";
        }else{
            double theta = Math.PI / 360;
            for(Animation asteroid: asteroids){
                if((Integer)asteroid.get("hits") == 1){
                    asteroid.rotateTowards(ship);
                    asteroid.speed = 2;
                    asteroid.move();
                }else{
                    double angle = (Double)asteroid.get("angle") + theta;
                    double x = 350 * Math.cos(angle) + Game.width / 2;
                    double y = 350 * Math.sin(angle) + Game.height / 2;
                    asteroid.moveTo((int)x,(int)y);
                    asteroid.set("angle",angle);  
                    if(Math.random() < 0.005){
                        asteroid.set("hits",1);
                    }
                }
              
                screenWrap(asteroid);
                if(plasmaball.collidedWith(asteroid,"circle")){
                    asteroid.visible = false;
                    Player.asteroidsAvaible--;
                    plasmaball.visible = false;
                    explosion.visible = true;
                    explosion.moveTo(asteroid.x,asteroid.y);
                }
                if(asteroid.collidedWith(ship,"circle") && Player.shield == 0){
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
            processEnergies();
        }
    }

    public void levelFinal(){

        if(levelCommonProcesses(-100,0) == false){
            return;
        }else if(Player.asteroidsAvaible == 0){
            Game.state = "Game Over";
        }else{
            boss.rotateBy(0.5, "left");
            boss.draw();
            bossEnergy.rotateBy(0.5,"right");
            bossEnergy.draw();
            for(Animation asteroid: asteroids){
                asteroid.move();
                if(asteroid.get("hits").equals("2")){
                    if(Math.random() < 0.005){
                        asteroid.set("hits",1);
                        int angle = Game.rnd(0,180) * 2 + 15;
                        asteroid.setSpeed(3, angle);
                    }
                }else{
                    asteroid.move();
                    if(asteroid.isOffScreen()){
                        int angle = Game.rnd(0,180) * 2 + 15;
                        asteroid.setSpeed(3, angle);
                        asteroid.moveTo(bossEnergy.x,bossEnergy.y);
                    }
                }
                if(asteroid.collidedWith(ship,"circle") && Player.shield == 0){
                    asteroid.visible = false;
                    crash.visible = true;
                    crash.moveTo(ship.x,ship.y);
                    Player.health -= 10;
                }
                if(plasmaball.collidedWith(asteroid,"circle")){
                    plasmaball.visible = false;
                    explosion.visible = true;
                    explosion.moveTo(asteroid.x,asteroid.y);
                }
                for(Animation energy:energies){
                    if(asteroid.collidedWith(energy,"circle")){
                        energy.visible = false;
                        explosion.visible = true;
                        explosion.moveTo(asteroid.x,asteroid.y);
                    }
                }
            }  
            for(Animation energy:energies){
                energy.draw();
                if(ship.collidedWith(energy,"circle")){
                    energy.visible = false;
                    Player.ammo += 2;
                }
                if(!energy.visible){
                    energy.x = Game.rnd(energy.width,Game.width-energy.width);
                    energy.y = Game.rnd(energy.height,Game.height-energy.height);
                    energy.visible = true;
                }
            }
        }
        if(plasmaball.collidedWith(boss,"circle")){
            Player.asteroidsAvaible--;
            plasmaball.visible = false;
            explosion.visible = true;
            explosion.moveTo(plasmaball.x,plasmaball.y);
        }
    }

    public void heroControl(){
        if(Keys.pressed[Keys.UP] || Keys.pressed[Keys.W]){
            ship.speed = 4;
        }else{
            ship.speed = 0;
        }
        if(Keys.pressed[Keys.LEFT] || Keys.pressed[Keys.A] ){
            ship.rotateBy(3,"left"); 
        }
        if(Keys.pressed[Keys.RIGHT] || Keys.pressed[Keys.D]){
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

    public void screenWrap(Sprite obj) {
        double x = obj.x,y = obj.y;
        
        if (obj.x - obj.width / 4 < 0) { 
            x = Game.width - obj.width / 4; 
        }else if (obj.x + obj.width / 4 > Game.width) { 
            x = obj.width / 4; 
        }
    
        if (obj.y - obj.height / 4 < 0) { 
            y = Game.height - obj.height / 4; 
        } else if (obj.y + obj.height / 4 > Game.height) { 
            y = obj.height / 4; 
        }
    
        if(obj.x != x){
            obj.x = (int) x;
        }   
        if(obj.y != y){
            obj.y = (int) y;
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
                Player.ammo += 2;
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
                case 4:
                    asteroid = new Animation("images/flame_gas.png",16, 512/4,512/4,0.25);
                    asteroid.resizeBy(-30);
                    asteroid.set("hits",2);
                    break;
                case 5:
                    asteroid = new Animation("images/asteroid4.png",48, 128,128,0.5);
                    asteroid.resizeBy(-25);
                    asteroid.set("hits",2);
                    break;
                case 6:
                    asteroid = new Animation("images/nova.png",5, 80, 80,1);
                    asteroid.resizeBy(-50);
                    asteroid.set("hits",2);
                    break;
                default:
                    asteroid = new Animation("images/asteroid1t.gif",41, 2173/41,52,1); 
            }
             
            int angle = Game.rnd(0,12) * 30 + 15;
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
            if(asteroid.collidedWith(ship,"circle") && Player.shield == 0){
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
