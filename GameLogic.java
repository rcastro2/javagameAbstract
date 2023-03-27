import java.awt.*;
import javax.swing.*;

/**
 * Created by rcastro on 2/6/2018.
 */
public class GameLogic {
    Color c;
    Sprite s;
    public GameLogic() {
        s = new Sprite("ball.png",0,0);
        s.resizeTo(50,50);
    }
;    public void gameLoop(){
        s.draw();

        //Simple Circle Example
        /*if(Mouse.leftClick) {
            c = Color.BLUE;
        }
        if(Mouse.rightClick) {
            c = Color.RED;
        }
        Game.canvas.setColor(c);
        Game.canvas.fillOval(Mouse.x,Mouse.y,100,100);
        */
    }
}
