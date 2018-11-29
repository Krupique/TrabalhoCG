package trabalhocg.structures;

import javafx.scene.paint.Color;

/**
 *
 * @author Henrique K. Secchi
 */
public class Pixel {
    private int R;
    private int G;
    private int B;

    public Pixel() {
        this.R = this.G = this.B = 0;
    }

    public Pixel(int R, int G, int B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }

    public int getR() {
        return R;
    }

    public void setR(int R) {
        this.R = R;
    }

    public int getG() {
        return G;
    }

    public void setG(int G) {
        this.G = G;
    }

    public int getB() {
        return B;
    }

    public void setB(int B) {
        this.B = B;
    }
    
    public int getRGB()
    {
        int res = (R + G + B) / 3;
        return res;
    }
    
    public void setRGB(Color cor)
    {
        R = (int)(cor.getRed() * 255);
        G = (int)(cor.getGreen() * 255);
        B = (int)(cor.getBlue() * 255);
    }
}
