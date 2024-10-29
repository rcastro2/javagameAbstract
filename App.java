import javax.swing.*;

public class App {
      public static JFrame frame;
      public static void main(String[] args) {
        frame = new JFrame();
        //args[0] contains the class name that implements GameLogic in order to run the gameLoop()
        Window c = new Window(800,600, args[0]);
        frame.add(c);
        frame.setTitle(c.game.getTitle());
        frame.setSize(Game.width, Game.height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

