/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhocg.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import trabalhocg.structures.Face;
import trabalhocg.structures.Vertice;

/**
 *
 * @author Henrique K. Secchi
 */
public class Utils implements TFVertices{
    public static Object[] add_vert_face(File arq, ArrayList<Vertice> listaVertice, ArrayList<Face> listaFaces)
    {
        String linha;
        String num;
        Object[] obj = new Object[2];
        double[] vet = new double[TF];
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(arq));
            while(br.ready())
            {
                linha = br.readLine();
                if(linha.length() > 1)
                {
                    num = "";
                    if(linha.subSequence(0, 2).equals("v ")) //Vertice
                    {
                        linha = linha.replace(",", ".");
                        for (int i = 2, j = 0; i < linha.length(); i++) {
                            if(linha.charAt(i) == 'E')
                            {
                                while(i + 1 < linha.length() && linha.charAt(i + 1) != ' ')
                                    i++;
                            }
                            if(linha.charAt(i) == ' ' || i + 1 == linha.length())
                            {
                                vet[j++] = Double.parseDouble(num);
                                num = "";
                            }
                            else
                                num += linha.charAt(i);
                        }
                        listaVertice.add(new Vertice(vet[0], vet[1], vet[2]));
                    }
                    else if(linha.subSequence(0, 2).equals("f ")) //Face
                    {
                        linha = linha.replace("//", "/");
                        for (int i = 1, j = 0; i < linha.length(); i++) {
                            if(linha.charAt(i) == '/')
                            {
                                vet[j++] = Double.parseDouble(num);
                                num = "";
                                while(i < linha.length() && linha.charAt(i) != ' ')
                                    i++;
                            }
                            else
                                num += linha.charAt(i);
                        }
                        listaFaces.add(new Face((int)vet[0], (int)vet[1], (int)vet[2]));
                    }
                }
            }
            
            obj[0] = listaVertice;
            obj[1] =listaFaces;
            return obj;
            
        }catch (Exception er)
        {
            System.out.println("Erro: " + er.getMessage());
        }
        return null;
    }
    
    public static Vertice calcular_produto_vetorial(Vertice ab, Vertice ac)
    {
        double i, j, k;
        i = (ab.getY() * ac.getZ()) - (ac.getY() * ab.getZ());
        j = (ab.getZ() * ac.getX()) - (ac.getZ() * ab.getX());
        k = (ab.getX() * ac.getY()) - (ab.getY() * ac.getX());   
        
        return new Vertice(i, j, k);
    }
    
    public static String toHEXCode(Color color )
    {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }

    public static ArrayList<double[][]> escala(ArrayList<double[][]> list, int centroX, int centroY) {
        double[][] ponto;
        for (int i = 0; i < list.size(); i++) {
            ponto = list.get(i);
            ponto[0][0] = ponto[0][0] * 2 + centroX;
            ponto[1][0] = ponto[1][0] * 2 + centroY;
            list.set(i, ponto);
        }
        
        return list;
    }
    
    public static double truncate(double value, int places) {
        double multiplier;
        multiplier = (float)Math.pow(10, places);
        return Math.floor(multiplier * value) / multiplier;
    }
    
    public static ArrayList<Vertice> calcula_normais_vertices(ArrayList<Vertice> vert, ArrayList<Face> faces)
    {
        ArrayList<Vertice> normais = new ArrayList<>();
        Face face;
        Vertice v1, v2, v3;
        Vertice vAB, vAC, vNormal, vAcumulado;
        
        for (int i = 0; i < vert.size(); i++) {
            normais.add(new Vertice(0, 0, 0));
        }
        
        for (int i = 0; i < faces.size(); i++) {
            face = faces.get(i);
            v1 = vert.get(face.getFace1());
            v2 = vert.get(face.getFace2());
            v3 = vert.get(face.getFace3());
            
            vAB = new Vertice(v2.getX() - v1.getX(), v2.getY()- v1.getY(), v2.getZ() - v1.getZ());
            vAC = new Vertice(v3.getX() - v1.getX(), v3.getY() - v1.getY(), v3.getZ()- v1.getZ());
            vNormal = calcular_produto_vetorial(vAB, vAC);
            
            
            vAcumulado = new Vertice(normais.get(face.getFace1()).getX() + vNormal.getX(),
                                     normais.get(face.getFace1()).getY() + vNormal.getY(),
                                     normais.get(face.getFace1()).getZ() + vNormal.getZ());
            normais.set(face.getFace1(), vAcumulado);
            
            vAcumulado = new Vertice(normais.get(face.getFace2()).getX() + vNormal.getX(),
                                     normais.get(face.getFace2()).getY() + vNormal.getY(),
                                     normais.get(face.getFace2()).getZ() + vNormal.getZ());
            normais.set(face.getFace2(), vAcumulado);
            
            vAcumulado = new Vertice(normais.get(face.getFace3()).getX() + vNormal.getX(),
                                     normais.get(face.getFace3()).getY() + vNormal.getY(),
                                     normais.get(face.getFace3()).getZ() + vNormal.getZ());
            normais.set(face.getFace3(), vAcumulado);
        }
        
        for (int i = 1; i < normais.size(); i++) {
            vNormal = normais.get(i);
            vNormal = calcula_normalizacao(vNormal);
            normais.set(i, vNormal);
        }
        
        return normais;
    }
    
    public static double[][] getPonto(Vertice vert, double[][] ponto)
    {
        ponto[0][0] = vert.getX();
        ponto[1][0] = vert.getY();
        ponto[2][0] = vert.getZ();
        ponto[3][0] = 1;
        
        return ponto;
    }
    
    public static Vertice calcula_normalizacao(Vertice vet)
    {
        double norma = Math.sqrt(vet.getX() * vet.getX() + vet.getY() * vet.getY() + vet.getZ() * vet.getZ());
        if(norma == 0)
        {
            vet.setX(0);
            vet.setY(0);
            vet.setZ(0);
        }
        else
        {
            vet.setX(vet.getX() / norma);
            vet.setY(vet.getZ() / norma);
            vet.setZ(vet.getY() / norma);
        }
        
        return vet;
    }
}
