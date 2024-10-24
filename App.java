import javax.swing.*;

public class App {
      public static void main(String[] args) {
        JFrame f = new JFrame();
        Window c = new Window(700,512);
        f.add(c);
        f.setSize(Game.width, Game.height);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

