import javax.swing.*;

public class App {
      public static JFrame frame;
      public static void main(String[] args) {
        frame = new JFrame();
        Window c = new Window(700,512);
        frame.add(c);
        frame.setTitle(c.game.getTitle());
        frame.setSize(Game.width, Game.height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

