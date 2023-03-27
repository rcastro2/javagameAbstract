import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by rcastro on 2/1/2018.
 */
public class Font {
    public java.awt.Font font;
    public Color color;
    public Color shadow;

    public Font(String family, int size) {
        this.font = new java.awt.Font(family, java.awt.Font.BOLD, size);
        this.color = Color.BLACK;
        this.shadow = null;
    }
    public Font(String family, int size, Color color) {
        this.font = new java.awt.Font(family, java.awt.Font.BOLD, size);
        this.color = color;
        this.shadow = null;
    }
    public Font(String family, int size, Color color, Color shadow) {
        this.font = new java.awt.Font(family, java.awt.Font.BOLD, size);
        this.color = color;
        this.shadow = shadow;
    }
}
