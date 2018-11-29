/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhocg.structures;

import javafx.scene.paint.Color;

/**
 *
 * @author Henrique K. Secchi
 */
public class PontoMedio {
    
    public static Pixel[][] draw(int x1, int y1, int x2, int y2, Pixel[][] raster, Color cor)
    {
        if(x1 < 1) x1 = 1;
        else if(x1 > 679) x1 = 679;
        if(x2 < 1) x2 = 1;
        else if(x2 > 679) x2 = 679;
        
        if(y1 < 1) y1 = 1;
        else if(y1 > 679) y1 = 679;
        if(y2 < 1) y2 = 1;
        else if(y2 > 679) y2 = 679;
        
        
        if (Math.abs(y2 - y1) > Math.abs(x2 - x1))
            if (x1 < x2)
                if (y1 < y2)
                    raster = reta_breseham(y1, y2, x1, x2, raster, 1, -1, cor);
                else
                    raster = reta_breseham(-y1, -y2, x1, x2, raster, -1, -1, cor);
            else
                if (y1 < y2)
                    raster = reta_breseham(y1, y2, x1, x2, raster, 1, -1, cor);
            else
                raster = reta_breseham(y2, y1, x2, x1, raster, 1, -1, cor);
        else if (x1 < x2)
            if (y1 < y2)
                raster = reta_breseham(x1, x2, y1, y2, raster, 1, 1, cor);
            else
                raster = reta_breseham(x1, x2, -y1, -y2, raster, -1, 1, cor);
        else
            raster = reta_breseham(x2, x1, y2, y1, raster, 1, 1, cor);

        return raster;
    }
    
    private static Pixel[][] reta_breseham(int x1, int x2, int y1, int y2, Pixel[][] raster, int sinal, int flag, Color cor)
    {
        int declive, dx, dy, incE, incNE, d, x, y;
        
        dx = x2 - x1;
        dy = y2 - y1;
        declive = 1;
        if (dy < 0)
        {
            declive = -1;
            dy = -dy;
        }

        incE = 2 * dy;
        incNE = 2 * dy - 2 * dx;
        d = 2 * dy - dx;

        y = y1;
        for (x = x1; x <= x2; x++)
        {
            if(flag == 1)
                raster[x][y * sinal].setRGB(cor);
            else
                raster[y][x * sinal].setRGB(cor);
                
            if (d <= 0)
                d += incE;
            else
            {
                d += incNE;
                y += declive;
            }
        }
        return raster;
    }

    public static Pixel[][] desenhar(double[][] ponto1, double[][] ponto2, double[][] ponto3, Pixel[][] matTela, Color cor) {
        
        matTela = draw((int)ponto1[0][0], (int)ponto1[1][0], (int)ponto2[0][0], (int)ponto2[1][0], matTela, cor);
        matTela = draw((int)ponto2[0][0], (int)ponto2[1][0], (int)ponto3[0][0], (int)ponto3[1][0], matTela, cor);
        matTela = draw((int)ponto3[0][0], (int)ponto3[1][0], (int)ponto1[0][0], (int)ponto1[1][0], matTela, cor);
        
        return matTela;
    }
}
