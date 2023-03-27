import java.awt.*;
import javax.swing.*;

/**
 * Created by rcastro on 2/6/2018.
 */
public class GameLogic {
    Color c;
    Sprite bk1,bk2,pipeTop,pipeBottom;
    Sprite logo, gameover;
    Animation bar, bird, ring;
    Sound wing, point, hit;

    String state;
    public GameLogic() {
        bk1 = new Sprite("day.png",0,0);
        bk2 = new Sprite("day.png",700,0);
        pipeTop = new Sprite("pipe_top.png",0,0);
        pipeBottom = new Sprite("pipe_bot.png",0,0);
        pipeTop.borderType = "rect";
        pipeBottom.borderType = "rect";

        bar = new Animation("bar.png",3,700,110,0.5);
        bar.y = 450; 
        bird = new Animation("bird.png",3,44,34,0.5);
        bird.borderType = "circle";
        bird.x = 300;

        ring = new Animation("ring2.png",64,64,64,2);
        ring.borderType = "circle";

        logo = new Sprite("logo.png",250,100);
        gameover = new Sprite("flappybird_end.png",50,100);
        state = "start";
        //wing = new Sound("wing.ogg");
        wing = new Sound("wing.wav");
        point = new Sound("point.wav");
        hit = new Sound("hit.wav");


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
        scrollBackground(2);
        logo.draw();
        bird.draw();
        Game.drawText("Press [SPACE] to begin",200,300, new Font("Comic Sans MS", 24, Color.WHITE, Color.BLACK));
        bar.draw();
        if(Keys.pressed[Keys.SPACE]){
            state = "main";

            bird.y = 200;
            ring.x = Game.width;
            ring.y = (int)(Math.random() * 200) + 75;
            ring.visible = true;
            pipeTop.y = ring.y - 300;
            pipeBottom.y = ring.y + 100;
        }
    }

    public void mainScreen(){
        scrollBackground(2);
        
        bird.draw();
        ring.draw();
        pipeTop.draw();
        pipeBottom.draw();
        bar.draw();
        
        if(Keys.pressed[Keys.SPACE]){
            bird.y -= 1;
            //s.play("C:\\Users\\PREDATOR\\Dropbox\\Projects\\gameabstract\\gameabstract_java\\FlappyBird\\fly1.wav");
            //Sound.FLY.play();
            wing.play();
        }else{
            bird.y += 1;
        }
        pipeTop.x = ring.x;
        ring.x -= 4;
        pipeBottom.x = ring.x;

        if(ring.x < -100){
            ring.x = Game.width;
            ring.y = (int)(Math.random() * 200) + 75;
            pipeTop.y = ring.y - 300;
            pipeBottom.y = ring.y + 100;
            ring.visible = true;
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
        scrollBackground(2);
        gameover.draw();
        Game.drawText("Press [Y] to play again",200,300, new Font("Comic Sans MS", 24, Color.WHITE, Color.BLACK));
        bar.draw();
        if(Keys.pressed[Keys.Y]){
            state = "start";
        }
    }

    public void scrollBackground(int amt){
        bk1.x -= amt;
        bk2.x -= amt;
        if(bk1.x < -700){
            bk1.x = bk2.x + 700;
        }
        if(bk2.x < -700){
            bk2.x = bk1.x + 700;
        }
        bk1.draw();
        bk2.draw();
    }

    public void print(Object obj){
        System.out.println(obj.toString());
    }
}
