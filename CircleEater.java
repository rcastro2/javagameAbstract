import java.awt.*;
import java.util.ArrayList;

public class CircleEater implements GameLogic{
    Sprite bk;
    Shape s;
    ArrayList<Coordinate> targets = new ArrayList<Coordinate>();
    int speed = 2;
    Coordinate you = new Coordinate(Game.width/2, Game.height/2);

    public CircleEater() {
        bk = new Animation("images/field_5.png",5,1000,1000,1);
        s = new Shape("ellipse",30,30,Color.CYAN);
        Game.setBackground(bk);

        for(int i = 0; i < 20; i++){
            int x = (int)(Math.random()*2000 - 1000);
            int y = (int)(Math.random()*2000 - 1000);
            System.out.println(x + " " + y);
            targets.add(new Coordinate(x,y));
        }
    }
    public void gameLoop(){
        String direction = "";
        if(Keys.pressed[Keys.UP]){
            you.y -= speed;  
            direction += "down";
        }
        if(Keys.pressed[Keys.DOWN]){
            you.y += speed;
            direction += "up";
        }
        if(Keys.pressed[Keys.LEFT]){
            you.x -= speed;  
            direction += "right";
        }
        if(Keys.pressed[Keys.RIGHT]){
            you.x += speed;
            direction += "left";
        }
        Game.scrollBackground(direction,2);
        for(int i = 0; i < targets.size(); i++){
            Coordinate t = targets.get(i);
            int screenX = t.x - you.x + Game.width / 2;
            int screenY = t.y - you.y + Game.height / 2;
            Game.canvas.fillOval(screenX, screenY,20,20);
            double d = Math.sqrt(Math.pow(t.x-you.x,2)+Math.pow(t.y-you.y,2));
            if(d < 10){
                targets.remove(i);
                s.width += 2;
                s.height += 2;
                print("======================================");
                for(Coordinate o: targets){
                    System.out.println(o.x + " " + o.y);
                }
            }
        }
        s.draw();
        String loc = String.format("(%d, %d)",you.x, you.y);
        Game.drawText(loc,s.x - 20,s.y + 10 + s.height);
    }

    public void print(Object obj){
        System.out.println(obj.toString());
    }
}
