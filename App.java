import javax.swing.*;

public class App {
      public static JFrame frame;
      public static void main(String[] args) {
        frame = new JFrame();
        String className = args[0];
        int width = 800;
        int height = 600; 
        //args[0] contains the class name that implements GameLogic in order to run the gameLoop()
        if(args.length == 3){
          width = Integer.parseInt(args[1]);
          height = Integer.parseInt(args[2]);
        }
        Window c = new Window(width,height, className);
        frame.add(c);
        frame.setTitle(c.game.getTitle());
        frame.setSize(Game.width, Game.height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

