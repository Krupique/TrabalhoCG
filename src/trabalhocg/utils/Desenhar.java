/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhocg.utils;

import com.jfoenix.controls.JFXCheckBox;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import trabalhocg.math.Matriz;
import trabalhocg.structures.Face;
import trabalhocg.structures.ModeloIluminacaoPhong;
import trabalhocg.structures.Pixel;
import trabalhocg.structures.PontoMedio;
import trabalhocg.structures.ScanLine;
import trabalhocg.structures.Vertice;

/**
 *
 * @author Henrique K. Secchi
 */
public class Desenhar {
    public static Image convert_to_image(Pixel[][] mat, Image img)
    {
        int[] pixel = {255, 255, 255, 0};
        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(img, null);
        WritableRaster raster = bimg.getRaster();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                if(mat[i][j].getRGB() != 0)
                {
                    //System.out.println("i:"+ i + " j:" + j + " r:" + mat[i][j].getR() + " g:" + mat[i][j].getG() + " b:" + mat[i][j].getB());
                    pixel[0] = mat[i][j].getR();
                    pixel[1] = mat[i][j].getG();
                    pixel[2] = mat[i][j].getB();
                    raster.setPixel(i, j, pixel);
                }
            }
        }
        
        return SwingFXUtils.toFXImage(bimg, null);
    }

    public static Pixel[][] desenhar_sem_faces_ocultas(Pixel[][] matTela, double[][] mat, ArrayList<Vertice> listaVertAtual, ArrayList<Face> listaFaces, int centroX, int centroY, Color cor) {
        Face face;
        Vertice vertAB, vertAC, verNormal;
        Vertice vert1, vert2, vert3;
        ArrayList<double[][]> list;
        double[][] ponto1 = new double[4][1];
        double[][] ponto2 = new double[4][1];
        double[][] ponto3 = new double[4][1];
        for (int i = 0; i < listaFaces.size(); i++) {
            list = new ArrayList<>();
            face = listaFaces.get(i);
            vert1 = listaVertAtual.get(face.getFace1());
            vert2 = listaVertAtual.get(face.getFace2());
            vert3 = listaVertAtual.get(face.getFace3());
            
            ponto1 = Utils.getPonto(vert1, ponto1);
            ponto2 = Utils.getPonto(vert2, ponto2);
            ponto3 = Utils.getPonto(vert3, ponto3);
            
            ponto1 = Matriz.multiplicar_matriz(mat, ponto1);
            ponto2 = Matriz.multiplicar_matriz(mat, ponto2);
            ponto3 = Matriz.multiplicar_matriz(mat, ponto3);
            
            vertAB = new Vertice(ponto2[0][0] - ponto1[0][0], ponto2[1][0]- ponto1[1][0], ponto2[2][0] - ponto1[2][0]);
            vertAC = new Vertice(ponto3[0][0] - ponto1[0][0], ponto3[1][0]- ponto1[1][0], ponto3[2][0] - ponto1[2][0]);
            verNormal = Utils.calcular_produto_vetorial(vertAB, vertAC);
            list.add(ponto1);
            list.add(ponto2);
            list.add(ponto3);
            list = Utils.escala(list, centroX, centroY);
            if(verNormal.getZ() > 0)
                matTela = PontoMedio.desenhar(list.get(0), list.get(1), list.get(2), matTela, cor);  
        }
        return matTela;
    }
    
    

    public static Pixel[][] desenhar_com_faces_ocultas(Pixel[][] matTela, double[][] mat, ArrayList<Vertice> listaVertAtual, ArrayList<Face> listaFaces, int centroX, int centroY, Color cor) {
        Face face;
        double[][] ponto1 = new double[4][1];
        double[][] ponto2 = new double[4][1];
        double[][] ponto3 = new double[4][1];
        
        ArrayList<double[][]> list;
        Vertice vert1, vert2, vert3;
        for (int i = 0; i < listaFaces.size(); i++) {
            list = new ArrayList<>();
            face = listaFaces.get(i);
            vert1 = listaVertAtual.get(face.getFace1());
            vert2 = listaVertAtual.get(face.getFace2());
            vert3 = listaVertAtual.get(face.getFace3());
            
            ponto1 = Utils.getPonto(vert1, ponto1);
            ponto2 = Utils.getPonto(vert2, ponto2);
            ponto3 = Utils.getPonto(vert3, ponto3);
            
            ponto1 = Matriz.multiplicar_matriz(mat, ponto1);
            ponto2 = Matriz.multiplicar_matriz(mat, ponto2);
            ponto3 = Matriz.multiplicar_matriz(mat, ponto3);
            
            list.add(ponto1);
            list.add(ponto2);
            list.add(ponto3);
            list = Utils.escala(list, centroX, centroY);
            matTela = PontoMedio.desenhar(list.get(0), list.get(1), list.get(2), matTela, cor);
        }
        return matTela;
    }

    public static Image desenhar_viewport(Image img, Pixel[][] matTelaViewPort, JFXCheckBox checkFaces, double[][] vpCima, ArrayList<Vertice> listaVertAtual, ArrayList<Face> listaFaces, int centroX, int centroY, Pixel[][] matTela, Color cor) {
        img = new Image("trabalhocg/resources/background_viewport.png");
        matTelaViewPort = Matriz.init_matTela(matTela);
        if(checkFaces.isSelected())
            matTelaViewPort = Desenhar.desenhar_com_faces_ocultas(matTelaViewPort, vpCima, listaVertAtual, listaFaces, centroX, centroY, cor);
        else
            matTelaViewPort = Desenhar.desenhar_sem_faces_ocultas(matTelaViewPort, vpCima, listaVertAtual, listaFaces, centroX, centroY, cor);
        return Desenhar.convert_to_image(matTelaViewPort, img);
    }

    public static Pixel[][] desenhar_shading_flat(Pixel[][] matTela, double[][] mat, ArrayList<Vertice> listaVertAtual, ArrayList<Face> listaFaces, int centroX, int centroY, Color corAmb, Color corBrilho, Color corObj, double[] posLuz, boolean flag, double[] intensidades) {
        Face face;
        Object[] obj = new Object[2];
        Vertice vertAB, vertAC, verNormal;
        Vertice vert1, vert2, vert3;
        ArrayList<double[][]> list;
        double[][] zbuffer = new double[matTela.length][matTela[0].length];
        double[] ia = {intensidades[2],intensidades[2],intensidades[2]};//Intensidade da cor que não pega luz.
        double[] id = {intensidades[1],intensidades[1],intensidades[1]};//Intensidade da própria cor do objeto.
        double[] ie = {intensidades[0],intensidades[0],intensidades[0]};//Intensidade da luz.
        
        double[] ka = {corAmb.getRed(),corAmb.getGreen(),corAmb.getBlue()};
        double[] kd = {corObj.getRed(),corObj.getGreen(),corObj.getBlue()};
        double[] ke = {corBrilho.getRed(),corBrilho.getGreen(),corBrilho.getBlue()};
        double n = 10; //Distância da luz?
        double[] eye = {0,0,1};
        double[] luz = {posLuz[0] - centroX, posLuz[1] - centroY, posLuz[2]};
        ModeloIluminacaoPhong model = new ModeloIluminacaoPhong(ia, id, ie, ka, kd, ke, n, eye, luz);
        double[][] ponto1 = new double[4][1];
        double[][] ponto2 = new double[4][1];
        double[][] ponto3 = new double[4][1];
        
        for (int i = 0; i < zbuffer.length; i++)
            for (int j = 0; j < zbuffer[0].length; j++)
                zbuffer[i][j] = -9999;
        
        for (int i = 0; i < listaFaces.size(); i++) {
            list = new ArrayList<>();
            face = listaFaces.get(i);
            vert1 = listaVertAtual.get(face.getFace1());
            vert2 = listaVertAtual.get(face.getFace2());
            vert3 = listaVertAtual.get(face.getFace3());
            
            ponto1 = Utils.getPonto(vert1, ponto1);
            ponto2 = Utils.getPonto(vert2, ponto2);
            ponto3 = Utils.getPonto(vert3, ponto3);
            
            vertAB = new Vertice(ponto2[0][0] - ponto1[0][0], ponto2[1][0]- ponto1[1][0], ponto2[2][0] - ponto1[2][0]);
            vertAC = new Vertice(ponto3[0][0] - ponto1[0][0], ponto3[1][0]- ponto1[1][0], ponto3[2][0] - ponto1[2][0]);
            verNormal = Utils.calcular_produto_vetorial(vertAB, vertAC);
            
            ponto1 = Matriz.multiplicar_matriz(mat, ponto1);
            ponto2 = Matriz.multiplicar_matriz(mat, ponto2);
            ponto3 = Matriz.multiplicar_matriz(mat, ponto3);
            
            ponto1[2][0] = vert1.getZ();
            ponto2[2][0] = vert2.getZ();
            ponto3[2][0] = vert3.getZ();
            
            list.add(ponto1);
            list.add(ponto2);
            list.add(ponto3);
            list = Utils.escala(list, centroX, centroY);
            
            if(flag)
            {
                if(verNormal.getZ() > 0)
                {
                    Color cor = model.getCor(new double[]{verNormal.getX(), verNormal.getY(), verNormal.getZ()});
                    obj = ScanLine.preenchimento_flat(matTela, list, cor, zbuffer);
                    matTela = (Pixel[][])obj[0];
                    zbuffer = (double[][])obj[1];
                }
            }
            else
            {
                Color cor = model.getCor(new double[]{verNormal.getX(), verNormal.getY(), verNormal.getZ()});
                obj = ScanLine.preenchimento_flat(matTela, list, cor, zbuffer);
                matTela = (Pixel[][])obj[0];
                zbuffer = (double[][])obj[1];
            }
                
        }
        return matTela;
    }
    
    public static Pixel[][] desenhar_shading_gouraud(Pixel[][] matTela, double[][] mat, ArrayList<Vertice> listaVertAtual, ArrayList<Face> listaFaces, int centroX, int centroY, Color corAmb, Color corBrilho, Color corObj, double[] posLuz, boolean flag, double[] intensidades) {
        Face face;
        Object[] obj = new Object[2];
        Vertice vertAB, vertAC, verNormal;
        Vertice vert1, vert2, vert3;
        ArrayList<double[][]> listV;
        ArrayList<Color> listC;
        double[][] zbuffer = new double[matTela.length][matTela[0].length];
        
        double[] ia = {intensidades[2],intensidades[2],intensidades[2]};
        double[] id = {intensidades[1],intensidades[1],intensidades[1]};
        double[] ie = {intensidades[0],intensidades[0],intensidades[0]};
        double[] ka = {corAmb.getRed(),corAmb.getGreen(),corAmb.getBlue()};
        double[] kd = {corObj.getRed(),corObj.getGreen(),corObj.getBlue()};
        double[] ke = {corBrilho.getRed(),corBrilho.getGreen(),corBrilho.getBlue()};
        double n = 10;
        double[] eye = {0,0,1};
        double[] luz = {posLuz[0] - centroX, posLuz[1] + 2 * centroY, posLuz[2] - 1400};
        ModeloIluminacaoPhong model = new ModeloIluminacaoPhong(ia, id, ie, ka, kd, ke, n, eye, luz);
        double[][] ponto1 = new double[4][1];
        double[][] ponto2 = new double[4][1];
        double[][] ponto3 = new double[4][1];
        
        ArrayList<Vertice> normais = new ArrayList<>();
        normais = Utils.calcula_normais_vertices(listaVertAtual, listaFaces);
        for (int i = 0; i < zbuffer.length; i++) {
            for (int j = 0; j < zbuffer[0].length; j++) {
                zbuffer[i][j] = -9999;
            }
        }
        
        for (int i = 0; i < listaFaces.size(); i++) {
            listV = new ArrayList<>();
            listC = new ArrayList<>();
            face = listaFaces.get(i);
            vert1 = listaVertAtual.get(face.getFace1());
            vert2 = listaVertAtual.get(face.getFace2());
            vert3 = listaVertAtual.get(face.getFace3());
            
            ponto1 = Utils.getPonto(vert1, ponto1);
            ponto2 = Utils.getPonto(vert2, ponto2);
            ponto3 = Utils.getPonto(vert3, ponto3);
            
            vertAB = new Vertice(ponto2[0][0] - ponto1[0][0], ponto2[1][0]- ponto1[1][0], ponto2[2][0] - ponto1[2][0]);
            vertAC = new Vertice(ponto3[0][0] - ponto1[0][0], ponto3[1][0]- ponto1[1][0], ponto3[2][0] - ponto1[2][0]);
            verNormal = Utils.calcular_produto_vetorial(vertAB, vertAC);
            
            ponto1 = Matriz.multiplicar_matriz(mat, ponto1);
            ponto2 = Matriz.multiplicar_matriz(mat, ponto2);
            ponto3 = Matriz.multiplicar_matriz(mat, ponto3);
            
            ponto1[2][0] = vert1.getZ();
            ponto2[2][0] = vert2.getZ();
            ponto3[2][0] = vert3.getZ();
            
            listV.add(ponto1);
            listV.add(ponto2);
            listV.add(ponto3);
            listV = Utils.escala(listV, centroX, centroY);
            
            //Color cor1 = model.getCor(new double[]{vert1.getX(), vert1.getY(), vert1.getZ()});
            //Color cor2 = model.getCor(new double[]{vert2.getX(), vert2.getY(), vert2.getZ()});
            //Color cor3 = model.getCor(new double[]{vert3.getX(), vert3.getY(), vert3.getZ()});
            
            Color cor1 = model.getCor(new double[]{normais.get(face.getFace1()).getX(), normais.get(face.getFace1()).getY(), normais.get(face.getFace1()).getZ()});
            Color cor2 = model.getCor(new double[]{normais.get(face.getFace2()).getX(), normais.get(face.getFace2()).getY(), normais.get(face.getFace2()).getZ()});
            Color cor3 = model.getCor(new double[]{normais.get(face.getFace3()).getX(), normais.get(face.getFace3()).getY(), normais.get(face.getFace3()).getZ()});
            
            listC.add(cor1);
            listC.add(cor2);
            listC.add(cor3);
            
            if(flag)
            {
                if(verNormal.getZ() > 0)
                {   
                    obj = ScanLine.preenchimento_gouraud(matTela, listV, listC, zbuffer);
                    matTela = (Pixel[][])obj[0];
                    zbuffer = (double[][])obj[1];
                }
            }
            else
            {
                obj = ScanLine.preenchimento_gouraud(matTela, listV, listC, zbuffer);
                matTela = (Pixel[][])obj[0];
                zbuffer = (double[][])obj[1];
            }
            
        }
        return matTela;
    }
    
    public static Pixel[][] desenhar_shading_phong(Pixel[][] matTela, double[][] mat, ArrayList<Vertice> listaVertAtual, ArrayList<Face> listaFaces, int centroX, int centroY, Color corAmb, Color corBrilho, Color corObj, double[] posLuz, boolean flag, double[] intensidades) {
        Face face;
        Object[] obj = new Object[2];
        Vertice vertAB, vertAC, verNormal;
        Vertice vert1, vert2, vert3;
        ArrayList<double[][]> listV;
        ArrayList<Color> normaisVert;
        double[][] zbuffer = new double[matTela.length][matTela[0].length];
        
        double[] ia = {intensidades[2],intensidades[2],intensidades[2]};
        double[] id = {intensidades[1],intensidades[1],intensidades[1]};
        double[] ie = {intensidades[0] * 2,intensidades[0] * 2, intensidades[0] * 2};
        double[] ka = {corAmb.getRed(),corAmb.getGreen(),corAmb.getBlue()};
        double[] kd = {corObj.getRed(),corObj.getGreen(),corObj.getBlue()};
        double[] ke = {corBrilho.getRed(),corBrilho.getGreen(),corBrilho.getBlue()};
        double n = 10;
        double[] eye = {0,0,1};
        double[] luz = {posLuz[0] - centroX, posLuz[1] + 2 * centroY, posLuz[2] - 1400};
        ModeloIluminacaoPhong model = new ModeloIluminacaoPhong(ia, id, ie, ka, kd, ke, n, eye, luz);
        double[][] ponto1 = new double[4][1];
        double[][] ponto2 = new double[4][1];
        double[][] ponto3 = new double[4][1];
        
        ArrayList<Vertice> normais = new ArrayList<>();
        normais = Utils.calcula_normais_vertices(listaVertAtual, listaFaces);
        for (int i = 0; i < zbuffer.length; i++) {
            for (int j = 0; j < zbuffer[0].length; j++) {
                zbuffer[i][j] = -9999;
            }
        }
        
        for (int i = 0; i < listaFaces.size(); i++) {
            listV = new ArrayList<>();
            normaisVert = new ArrayList<>();
            face = listaFaces.get(i);
            vert1 = listaVertAtual.get(face.getFace1());
            vert2 = listaVertAtual.get(face.getFace2());
            vert3 = listaVertAtual.get(face.getFace3());
            
            ponto1 = Utils.getPonto(vert1, ponto1);
            ponto2 = Utils.getPonto(vert2, ponto2);
            ponto3 = Utils.getPonto(vert3, ponto3);
            
            vertAB = new Vertice(ponto2[0][0] - ponto1[0][0], ponto2[1][0]- ponto1[1][0], ponto2[2][0] - ponto1[2][0]);
            vertAC = new Vertice(ponto3[0][0] - ponto1[0][0], ponto3[1][0]- ponto1[1][0], ponto3[2][0] - ponto1[2][0]);
            verNormal = Utils.calcular_produto_vetorial(vertAB, vertAC);
            
            ponto1 = Matriz.multiplicar_matriz(mat, ponto1);
            ponto2 = Matriz.multiplicar_matriz(mat, ponto2);
            ponto3 = Matriz.multiplicar_matriz(mat, ponto3);
            
            ponto1[2][0] = vert1.getZ();
            ponto2[2][0] = vert2.getZ();
            ponto3[2][0] = vert3.getZ();
            
            listV.add(ponto1);
            listV.add(ponto2);
            listV.add(ponto3);
            listV = Utils.escala(listV, centroX, centroY);
            
            Color normal1 = model.getCor(new double[]{normais.get(face.getFace1()).getX(), normais.get(face.getFace1()).getY(), normais.get(face.getFace1()).getZ()});
            Color normal2 = model.getCor(new double[]{normais.get(face.getFace2()).getX(), normais.get(face.getFace2()).getY(), normais.get(face.getFace2()).getZ()});
            Color normal3 = model.getCor(new double[]{normais.get(face.getFace3()).getX(), normais.get(face.getFace3()).getY(), normais.get(face.getFace3()).getZ()});
            
            normaisVert.add(normal1);
            normaisVert.add(normal2);
            normaisVert.add(normal3);
            
            if(flag)
            {
                if(verNormal.getZ() > 0)
                {   
                    obj = ScanLine.preenchimento_phong(matTela, listV, normaisVert, zbuffer);
                    matTela = (Pixel[][])obj[0];
                    zbuffer = (double[][])obj[1];
                }
            }
            else
            {
                obj = ScanLine.preenchimento_phong(matTela, listV, normaisVert, zbuffer);
                matTela = (Pixel[][])obj[0];
                zbuffer = (double[][])obj[1];
            }
            
        }
        return matTela;
    }
    
}
