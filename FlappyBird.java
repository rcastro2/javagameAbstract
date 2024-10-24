import java.awt.*;

public class FlappyBird implements GameLogic{
    Color c;
    Sprite bk,pipeTop,pipeBottom;
    Sprite logo, gameover;
    Animation bar, bird, ring;
    Sound wing, point, hit;

    String state;
    public FlappyBird() {
        bk = new Sprite("images/day.png",(int)(Game.width/2),(int)(Game.height/2));
        Game.setBackground(bk);
        pipeTop = new Sprite("images/pipe_top.png");
        pipeBottom = new Sprite("images/pipe_bot.png");

        bar = new Animation("images/bar.png",3,700,110,0.5);
        bar.y = 450; 
        bird = new Animation("images/bird.png",3,44,34,0.5);
        bird.x = 300;

        ring = new Animation("images/ring2.png",64,64,64,2);

        logo = new Sprite("images/logo.png",(int)(Game.width/2),100);
        gameover = new Sprite("images/flappybird_end.png",(int)(Game.width/2),100);

        wing = new Sound("sounds/wing.wav");
        point = new Sound("sounds/point.wav");
        hit = new Sound("sounds/hit.wav");

        state = "start";
    }
    public void gameLoop(){
        switch(state){
            case "start":
                bird.x = 300;
                bird.y = 200;
                startScreen();
                break;
            case "main":
                bird.x = 200;
                mainScreen();
                break;
            case "gameOver":
                gameOverScreen();
                break;
        }
    }

    public void startScreen(){
        Game.scrollBackground("left",2);
        logo.draw();
        bird.draw();
        Game.drawText("Press [SPACE] to begin",200,300, new Font("Comic Sans MS", 24, Color.WHITE, Color.BLACK));
        bar.draw();
        if(Keys.pressed[Keys.SPACE]){
            state = "main";
            bird.y = 200;
            ring.x = Game.width;
            ring.y = (int)(Math.random() * 175) + 125;
            ring.visible = true;
            pipeTop.y = ring.y - 200;
            pipeBottom.y = ring.y + 200;
        }
    }

    public void mainScreen(){
        Game.scrollBackground("left",2);
        
        bird.draw();
        ring.draw();
        pipeTop.draw();
        pipeBottom.draw();
        bar.draw();
        
        if(Keys.pressed[Keys.SPACE]){
            bird.y -= 1;
            wing.play();
        }else{
            bird.y += 1;
        }
        pipeTop.x = ring.x;
        ring.x -= 4;
        pipeBottom.x = ring.x;

        if(ring.x < -100){
            ring.x = Game.width;
            ring.y = (int)(Math.random() * 175) + 125;
            ring.visible = true;
            pipeTop.y = ring.y - 200;
            pipeBottom.y = ring.y + 200;
        }
        if(bird.collidedWith(ring,"circle")){
            ring.visible = false;
            point.play();
        }
        if(bird.collidedWith(pipeTop,"rect") || bird.collidedWith(pipeBottom,"rect") || bird.collidedWith(bar,"rect")){
            hit.play();
            state = "gameOver";
        }
    }

    public void gameOverScreen(){
        Game.scrollBackground("left",2);
        gameover.draw();
        Game.drawText("Press [Y] to play again",200,300, new Font("Comic Sans MS", 24, Color.WHITE, Color.BLACK));
        bar.draw();
        if(Keys.pressed[Keys.Y]){
            state = "start";
        }
    }
}
