import java.awt.*;
import javax.swing.*;

/**
 * Created by rcastro on 2/6/2018.
 */
public class GameLogic {
    Color c;
    Circle circle;
    int r = 0,dr = 1;
    public GameLogic() {
        circle = new Circle(100,100,20);
    }
    public void gameLoop(){
	r+=dr;
	if(r >254 || r < 1) dr=-dr;
	Game.canvas.setPaint(new Color(r,0,255));
	System.out.println(1);
        circle.draw();
	circle.moveBy(1,1);
    }
}
