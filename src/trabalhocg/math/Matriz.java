package trabalhocg.math;

import trabalhocg.structures.Pixel;

/**
 *
 * @author Henrique K. Secchi.
 */
public class Matriz {
    public static double[][] multiplicar_matriz(double[][] matA, double[][] matB)
    {
        int linA = matA.length;
        int colB = matB[0].length;
        double[][] matC = new double[linA][colB];
        for (int i = 0; i < linA; i++) {
            for (int j = 0; j < colB; j++) {
                for (int k = 0; k < matB.length; k++) {
                    matC[i][j] += matA[i][k] * matB[k][j];
                }
            }
        }
        return matC;
    }
    
    public static Pixel[][] init_matTela(Pixel[][] matTela)
    {
        for (int i = 0; i < matTela.length; i++)
            for (int j = 0; j < matTela[0].length; j++)
                matTela[i][j] = new Pixel();
        return matTela;
    }
}
