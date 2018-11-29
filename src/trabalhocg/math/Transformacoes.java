/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhocg.math;

import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import trabalhocg.structures.Vertice;

/**
 *
 * @author Henrique K. Secchi
 */
public class Transformacoes implements Fatores{
    public static ArrayList<Vertice> Escala(ArrayList<Vertice> listaVertAtual, double deltay)
    {
        double escala;
        
        if(deltay < 0) //Mouse scroll baixo
            escala = 1 * FAT_ESCALA;
        else //Mouse scroll cima
            escala = 1 / FAT_ESCALA;
        
        double[][] ponto = new double[4][1];
        double[][] mescala = {{escala, 0, 0, 0},{0, escala, 0, 0},{0, 0, escala, 0},{0, 0, 0, 1}};
        double[][] matres = new double[4][1];
        
        Vertice v;
        for (int i = 0; i < listaVertAtual.size(); i++) {
            v = listaVertAtual.get(i);
            ponto = v.getPonto();

            matres = Matriz.multiplicar_matriz(mescala, ponto);

            v.setPonto(matres);
            listaVertAtual.set(i, v);
        }
        return listaVertAtual;
    }
    
    public static ArrayList<Vertice> Translacao(ArrayList<Vertice> listaVertAtual, KeyEvent event)
    {
        double[][] ponto = new double[4][1];
        if(event.getCode() == KeyCode.S)
            ponto[1][0] = FAT_TRANS;
        else if(event.getCode() == KeyCode.W)
            ponto[1][0] = -FAT_TRANS;
        else if(event.getCode() == KeyCode.A)
            ponto[0][0] = -FAT_TRANS;
        else if(event.getCode() == KeyCode.D)
            ponto[0][0] = FAT_TRANS;
        
        if(event.getCode() == KeyCode.S || event.getCode() == KeyCode.W || event.getCode() == KeyCode.A || event.getCode() == KeyCode.D)
        {
            double[][] mtrans = {{1, 0, 0, ponto[0][0]},{0, 1, 0, ponto[1][0]},{0, 0, 1, 1},{0, 0, 0, 1}};
            double[][] matres = new double[4][1];
            Vertice v;
            for (int i = 0; i < listaVertAtual.size(); i++) {
                v = listaVertAtual.get(i);
                ponto = v.getPonto();

                matres = Matriz.multiplicar_matriz(mtrans, ponto);
               
                v.setPonto(matres);
                listaVertAtual.set(i, v);
            }
        }
        return listaVertAtual;
    }
    
    public static ArrayList<Vertice> Rotacao(ArrayList<Vertice> listaVertAtual, MouseEvent event, int[] posIni, double centroX)
    {
        double[][] ponto = new double[4][1];
        double[][] matres = new double[4][1];
        double[][] mrotGen = new double[4][4];
        int[] posFinal = new int[2];
        Vertice v;
        
        posFinal[0] = (int)event.getX();
        posFinal[1] = (int)event.getY();
        
        int rotX, rotY, rotZ;
        rotX = rotY = rotZ = 0;
        if(posFinal[0] < posIni[0])
            rotX = FAT_ROTACAO;
        else if(posFinal[0] > posIni[0])
            rotX = -FAT_ROTACAO;
        
        if(posFinal[1] < posIni[1])
            rotY = -FAT_ROTACAO;
        else if(posFinal[1] > posIni[1])
            rotY = FAT_ROTACAO;
        
        if(posFinal[1] > posIni[1] && posFinal[0] > centroX)
            rotZ = FAT_ROTACAO;
        else if(posFinal[1] < posIni[1] && posFinal[0] < centroX)
            rotZ = FAT_ROTACAO;
        else
            rotZ = -FAT_ROTACAO;
            
       
        double[] rot = {rotY  * Math.PI / 180, rotX  * Math.PI / 180, rotZ * Math.PI / 180};
        
        if(event.getButton().equals(MouseButton.PRIMARY)) //Rotação X e Y
        {
            double[][] mrotX = {{Math.cos(rot[1]), 0, Math.sin(rot[1]), 0},{0, 1, 0, 0},{-Math.sin(rot[1]), 0, Math.cos(rot[1]), 0},{0, 0, 0, 1}};
            double[][] mrotY = {{1, 0, 0, 0},{0, Math.cos(rot[0]), -Math.sin(rot[0]), 0},{0, Math.sin(rot[0]), Math.cos(rot[0]), 0},{0, 0, 0, 0}};
            mrotGen = Matriz.multiplicar_matriz(mrotX, mrotY);
            
        }
        else if(event.getButton().equals(MouseButton.SECONDARY)) //Rotação Z
        {
            mrotGen[0][0] = mrotGen[1][1] = Math.cos(rot[2]);
            mrotGen[0][1] = -Math.sin(rot[2]);
            mrotGen[0][2] = mrotGen[0][3] = mrotGen[1][2] = mrotGen[1][3] = mrotGen[2][0] = mrotGen[2][1] = mrotGen[2][3] = mrotGen[3][0] = mrotGen[3][1] = mrotGen[3][2] = 0;
            mrotGen[1][0] = Math.sin(rot[2]);
            mrotGen[2][2] = mrotGen[3][3] = 1;
        }
        for (int i = 0; i < listaVertAtual.size(); i++) {
            v = listaVertAtual.get(i);
            ponto = v.getPonto();


            matres = Matriz.multiplicar_matriz(mrotGen, ponto);

            v.setPonto(matres);
            listaVertAtual.set(i, v);
        }
        return listaVertAtual;
    }
    
    public static ArrayList<Vertice> RotacaoViewPort(ArrayList<Vertice> listaVertAtual, double[][] matRot)
    {
        double[][] ponto = new double[4][1];
        double[][] matres = new double[4][1];
        double[][] mrotGen = new double[4][4];
        int[] posFinal = new int[2];
        Vertice v;    
       
        
        for (int i = 0; i < listaVertAtual.size(); i++) {
            v = listaVertAtual.get(i);
            ponto = v.getPonto();

            matres = Matriz.multiplicar_matriz(matRot, ponto);

            v.setPonto(matres);
            listaVertAtual.set(i, v);
        }
        return listaVertAtual;
    }
}
