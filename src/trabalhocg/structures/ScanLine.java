/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhocg.structures;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import trabalhocg.structures.list.AET;
import trabalhocg.structures.list.ET;
import trabalhocg.structures.list.No;
import trabalhocg.utils.Utils;


/**
 *
 * @author Henrique K. Secchi
 */
public class ScanLine {
    
    public static ET criar_et_flat(int height, ArrayList<double[][]> poli)
    {
        
        double a, b;
        double[] ini, fim;
        ini = new double[3];
        fim = new double[3];
        double incX, incZemY;
        ET et = new ET(height);
        
        for (int i = 0; i < poli.size() - 1; i++) {
            a = (int)poli.get(i)[1][0];
            b = (int)poli.get(i + 1)[1][0];
            
            if(a < b)
            {
                ini[0] = (int)poli.get(i)[0][0]; ini[1] = (int)poli.get(i)[1][0]; ini[2] = (int)poli.get(i)[2][0];
                fim[0] = (int)poli.get(i + 1)[0][0]; fim[1] = (int)poli.get(i + 1)[1][0]; fim[2] = (int)poli.get(i + 1)[2][0];
            }
            else
            {
                ini[0] = (int)poli.get(i + 1)[0][0]; ini[1] = (int)poli.get(i + 1)[1][0]; ini[2] = (int)poli.get(i + 1)[2][0];
                fim[0] = (int)poli.get(i)[0][0]; fim[1] = (int)poli.get(i)[1][0]; fim[2] = (int)poli.get(i)[2][0];
            }
            if(ini[1] == fim[1])
                incX = incZemY = 0;
            else
            {
                incX = (fim[0] - ini[0]) / (fim[1] - ini[1]);
                incZemY = (fim[2] - ini[2]) / (fim[1] - ini[1]);
            }
            et.inserePos((int)ini[1], new No(ini[0], fim[1], ini[2], incZemY, incX, null, null));
        }
        a = (int)poli.get(0)[1][0];
        b = (int)poli.get(poli.size() - 1)[1][0];
            
        if(a < b){
            ini[0] = (int)poli.get(0)[0][0]; ini[1] = (int)poli.get(0)[1][0]; ini[2] = (int)poli.get(0)[2][0];
            fim[0] = (int)poli.get(poli.size() - 1)[0][0]; fim[1] = (int)poli.get(poli.size() - 1)[1][0]; fim[2] = (int)poli.get(poli.size() - 1)[2][0];
        }
        else{
            ini[0] = (int)poli.get(poli.size() - 1)[0][0]; ini[1] = (int)poli.get(poli.size() - 1)[1][0]; ini[2] = (int)poli.get(poli.size() - 1)[2][0];
            fim[0] = (int)poli.get(0)[0][0]; fim[1] = (int)poli.get(0)[1][0]; fim[2] = (int)poli.get(0)[2][0];
        }
        
        if(ini[1] == fim[1])
            incX = incZemY = 0;
        else{
            incX = (fim[0] - ini[0]) / (fim[1] - ini[1]);
            incZemY = (fim[2] - ini[2]) / (fim[1] - ini[1]);
        }
        et.inserePos((int)ini[1], new No(ini[0], fim[1], ini[2], incZemY, incX, null, null));
        
        return et;
    }
   
    public static Object[] preenchimento_flat(Pixel[][] img, ArrayList<double[][]> poli, Color cor, double[][] zbuffer)
    {
        No a, b;
        int y;
        int height = img.length;
        double z, w;
        No ini, fim;
        ET et;
        AET aet = new AET();
        Object[] obj = new Object[2];
        et = criar_et_flat(height, poli);
        y = aet.inicializaAET(height, et);
        
        while(aet.getTl() > 0)
        {
            aet.removerY(y);
            
            if(aet.getTl() > 0)
            {
                aet.ordernarX();
                for (int i = 0; i < aet.getTl(); i+=2) {
                    a = aet.getPos(i);
                    b = aet.getPos(i + 1);
                    
                    ini = a.getXmin() < b.getXmin() ? a : b;
                    fim = a.getXmin() > b.getXmin() ? a : b;
                    
                    z = ini.getZmin();
                    w = (int)Math.ceil(fim.getXmin());
                    for (int j = (int)Math.ceil(ini.getXmin()); j < w; j++) {
                        if(z >= zbuffer[j][y])
                        {
                            img[j][y].setRGB(cor);
                            zbuffer[j][y] = z;
                        }
                        
                        z+= ini.getIncZemY();
                    }
                
                }
                aet.incX();
                y++;
                if(et.getPos(y) != null)
                    aet.inserirNovasCaixas(et.getPos(y));
            }
        }
        obj[0] = img;
        obj[1] = zbuffer;
        return obj;
    }
    
    public static Object[] preenchimento_gouraud(Pixel[][] img, ArrayList<double[][]> poli, ArrayList<Color> listC, double[][] zbuffer)
    {
        No a, b;
        int y;
        int height = img.length;
        No ini, fim;
        ET et;
        AET aet = new AET();
        Object[] obj = new Object[2];
        et = criar_et_gouraud(height, poli, listC);
        y = aet.inicializaAET(height, et);
        double incr, incg, incb, incRac, incGac, incBac;
        double z, w;
        Color cor;
        while(aet.getTl() > 0)
        {
            aet.removerY(y);
            
            if(aet.getTl() > 0)
            {
                aet.ordernarX();
                for (int i = 0; i < aet.getTl(); i+=2) {
                    a = aet.getPos(i);
                    b = aet.getPos(i + 1);
                    
                    ini = a.getXmin() < b.getXmin() ? a : b;
                    fim = a.getXmin() > b.getXmin() ? a : b;
                    
                    incr = incRGB(ini.getRxmin(), fim.getRxmin(), ini.getXmin(), fim.getXmin());
                    incg = incRGB(ini.getGymin(), fim.getGymin(), ini.getXmin(), fim.getXmin());
                    incb = incRGB(ini.getBzmin(), fim.getBzmin(), ini.getXmin(), fim.getXmin());
                    incRac = incGac = incBac = 0;
                    
                    z = ini.getZmin();
                    w = (int)Math.ceil(fim.getXmin());
                    for (int j = (int)Math.ceil(ini.getXmin()); j < w; j++) {
                        if(z >= zbuffer[j][y])
                        {
                            cor = novaCor(ini.getRxmin() + incRac, ini.getGymin() + incGac, ini.getBzmin() + incBac, 1);
                            img[j][y].setRGB(cor);
                            
                            incRac += incr;
                            incGac += incg;
                            incBac += incb;
                            zbuffer[j][y] = z;
                        }
                        z+= ini.getIncZemY();
                    }
                    ini.setZmin(ini.getZmin() + ini.getIncZemY());
                }
                
                
                aet.incX();
                y++;
                if(et.getPos(y) != null)
                    aet.inserirNovasCaixas(et.getPos(y));
            }
        }
        obj[0] = img;
        obj[1] = zbuffer;
        return obj;
    }

    private static double incRGB(double cmin, double cmax, double xmin, double xmax) {
        if(xmin == xmax)
            return 0;
        double cor = cmax * 255 - cmin * 255;
        double div = cor / (xmax - xmin);
        double res = div / 255;
        return res;
        //return ((cmax - cmin) / (xmax - xmin));
    }
    
    public static ET criar_et_gouraud(int height, ArrayList<double[][]> poli, ArrayList<Color> listC)
    {
        
        double a, b;
        double[] ini, fim;
        ini = new double[3];
        fim = new double[3];
        double incX, incZemY, incR, incG, incB;
        ET et = new ET(height);
        Color corIni, corFim;
        
        for (int i = 0; i < poli.size() - 1; i++) {
            a = (int)poli.get(i)[1][0];
            b = (int)poli.get(i + 1)[1][0];
            
            if(a < b)
            {
                ini[0] = (int)poli.get(i)[0][0]; ini[1] = (int)poli.get(i)[1][0]; ini[2] = (int)poli.get(i)[2][0];
                fim[0] = (int)poli.get(i + 1)[0][0]; fim[1] = (int)poli.get(i + 1)[1][0]; fim[2] = (int)poli.get(i + 1)[2][0];
                corIni = listC.get(i);
                corFim = listC.get(i + 1);
            }
            else
            {
                ini[0] = (int)poli.get(i + 1)[0][0]; ini[1] = (int)poli.get(i + 1)[1][0]; ini[2] = (int)poli.get(i + 1)[2][0];
                fim[0] = (int)poli.get(i)[0][0]; fim[1] = (int)poli.get(i)[1][0]; fim[2] = (int)poli.get(i)[2][0];
                corIni = listC.get(i + 1);
                corFim = listC.get(i);
            }
            if(ini[1] == fim[1])
                incX = incZemY = incR = incG = incB = 0;
            else
            {
                incX = (fim[0] - ini[0]) / (fim[1] - ini[1]);
                incZemY = (fim[2] - ini[2]) / (fim[1] - ini[1]);
                incR = (corFim.getRed() - corIni.getRed()) / (fim[1] - ini[1]);
                incG = (corFim.getGreen()- corIni.getGreen()) / (fim[1] - ini[1]);
                incB = (corFim.getBlue() - corIni.getBlue()) / (fim[1] - ini[1]);
            }
            et.inserePos((int)ini[1], new No(ini[0], fim[1], ini[2], incZemY, incX, corIni.getRed(), corIni.getGreen(), corIni.getBlue(), incR, incG, incB, null, null));
        }
        a = (int)poli.get(0)[1][0];
        b = (int)poli.get(poli.size() - 1)[1][0];
            
        if(a < b){
            ini[0] = (int)poli.get(0)[0][0]; ini[1] = (int)poli.get(0)[1][0]; ini[2] = (int)poli.get(0)[2][0];
            fim[0] = (int)poli.get(poli.size() - 1)[0][0]; fim[1] = (int)poli.get(poli.size() - 1)[1][0]; fim[2] = (int)poli.get(poli.size() - 1)[2][0];
            corIni = listC.get(0);
            corFim = listC.get(poli.size() - 1);
        }
        else{
            ini[0] = (int)poli.get(poli.size() - 1)[0][0]; ini[1] = (int)poli.get(poli.size() - 1)[1][0]; ini[2] = (int)poli.get(poli.size() - 1)[2][0];
            fim[0] = (int)poli.get(0)[0][0]; fim[1] = (int)poli.get(0)[1][0]; fim[2] = (int)poli.get(0)[2][0];
            corIni = listC.get(poli.size() - 1);
            corFim = listC.get(0);
        }
        
        if(ini[1] == fim[1])
            incX = incZemY = incR = incG = incB = 0;
        else{
            incX = (fim[0] - ini[0]) / (fim[1] - ini[1]);
            incZemY = (fim[2] - ini[2]) / (fim[1] - ini[1]);
            incR = (corFim.getRed() - corIni.getRed()) / (fim[1] - ini[1]);
            incG = (corFim.getGreen()- corIni.getGreen()) / (fim[1] - ini[1]);
            incB = (corFim.getBlue()- corIni.getBlue()) / (fim[1] - ini[1]);
        }
        et.inserePos((int)ini[1], new No(ini[0], fim[1], ini[2], incZemY, incX, corIni.getRed(), corIni.getGreen(), corIni.getBlue(), incR, incG, incB, null, null));
        
        return et;
    }

    private static Color novaCor(double r, double g, double b, int o) {
        double ri = r > 1 ? 1 : r;
        double gi = g > 1 ? 1 : g;
        double bi = b > 1 ? 1 : b;
        
        ri = ri < 0 ? 0 : ri;
        gi = gi < 0 ? 0 : gi;
        bi = bi < 0 ? 0 : bi;
        
        return new Color(ri, gi, bi, 1);
    }
    
    public static Object[] preenchimento_phong(Pixel[][] img, ArrayList<double[][]> poli, ArrayList<Color> listNormais, double[][] zbuffer)
    {
        No a, b;
        int y;
        int height = img.length;
        No ini, fim;
        ET et;
        AET aet = new AET();
        Object[] obj = new Object[2];
        et = criar_et_gouraud(height, poli, listNormais);
        y = aet.inicializaAET(height, et);
        double incr, incg, incb, incRac, incGac, incBac;
        double z, w;
        Color cor;
        while(aet.getTl() > 0)
        {
            aet.removerY(y);
            
            if(aet.getTl() > 0)
            {
                aet.ordernarX();
                for (int i = 0; i < aet.getTl(); i+=2) {
                    a = aet.getPos(i);
                    b = aet.getPos(i + 1);
                    
                    ini = a.getXmin() < b.getXmin() ? a : b;
                    fim = a.getXmin() > b.getXmin() ? a : b;
                    
                    incr = incRGB(ini.getRxmin(), fim.getRxmin(), ini.getXmin(), fim.getXmin());
                    incg = incRGB(ini.getGymin(), fim.getGymin(), ini.getXmin(), fim.getXmin());
                    incb = incRGB(ini.getBzmin(), fim.getBzmin(), ini.getXmin(), fim.getXmin());
                    incRac = incGac = incBac = 0;
                    
                    z = ini.getZmin();
                    w = (int)Math.ceil(fim.getXmin());
                    for (int j = (int)Math.ceil(ini.getXmin()); j < w; j++) {
                        if(z >= zbuffer[j][y])
                        {
                            cor = novaCor(ini.getRxmin() + incRac, ini.getGymin() + incGac, ini.getBzmin() + incBac, 1);
                            img[j][y].setRGB(cor);
                            
                            incRac += incr;
                            incGac += incg;
                            incBac += incb;
                            zbuffer[j][y] = z;
                        }
                        z+= ini.getIncZemY();
                    }
                    ini.setZmin(ini.getZmin() + ini.getIncZemY());
                }
                
                
                aet.incX();
                y++;
                if(et.getPos(y) != null)
                    aet.inserirNovasCaixas(et.getPos(y));
            }
        }
        obj[0] = img;
        obj[1] = zbuffer;
        return obj;
    }
}
