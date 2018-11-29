/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhocg.structures;

import static java.lang.Double.NaN;
import javafx.scene.paint.Color;
import trabalhocg.utils.Utils;

/**
 *
 * @author Henrique K. Secchi
 */
public class ModeloIluminacaoPhong {
    private double[] ia;
    private double[] id;
    private double[] ie;
    
    private double[] ka;
    private double[] kd;
    private double[] ke;
    
    private double n;
    
    private double[] eye;
    private double[] luz;
    private double[] h;
    
    private double[] ponto;

    public ModeloIluminacaoPhong(double[] ia, double[] id, double[] ie, double[] ka, double[] kd, double[] ke, double n, double[] eye, double[] luz) {
        this.ia = ia;
        this.id = id;
        this.ie = ie;
        this.ka = ka;
        this.kd = kd;
        this.ke = ke;
        this.n = n;
        this.eye = eye;
        this.luz = luz;
        double norma = Math.sqrt(luz[0] * luz[0] + luz[1] * luz[1] + luz[2] * luz[2]);
        if(norma == 0){
            this.luz[0] = this.luz[1] = this.luz[2] = 0;
        }
        else
        {
            this.luz[0] = luz[0] / norma;
            this.luz[1] = luz[1] / norma;
            this.luz[2] = luz[2] / norma;
        }
         
        h = new double[3];
        h[0] = this.luz[0] + this.eye[0];
        h[1] = this.luz[1] + this.eye[1];
        h[2] = this.luz[2] + this.eye[2];
        
        norma = Math.sqrt(h[0] * h[0] + h[1] * h[1] + h[2] * h[2]);
        if(norma == 0){
            h[0] = h[1] = h[2] = 0;
        }
        else
        {
            h[0] = h[0] / norma;
            h[1] = h[1] / norma;
            h[2] = h[2] / norma;
        }
    }
    
    public Color getCor(double[] vn)
    {
        double norma = Math.sqrt(vn[0] * vn[0] + vn[1] * vn[1] + vn[2] * vn[2]);
        vn[0] = vn[0] / norma;
        vn[1] = vn[1] / norma;
        vn[2] = vn[2] / norma;
        
        vn[0] = Utils.truncate(vn[0], 6);
        vn[1] = Utils.truncate(vn[1], 6);
        vn[2] = Utils.truncate(vn[2], 6);
                
        double[] res = new double[3];
        double difusa = luz[0] * vn[0] + luz[1] * vn[1] + luz[2] * vn[2];
        double especular = h[0] * vn[0] + h[1] * vn[1] + h[2] * vn[2];
        especular = Utils.truncate(especular, 7);
        especular = Math.pow(especular, n);
        
        difusa = Utils.truncate(difusa, 8);
        especular = Utils.truncate(especular, 8);
        
        res[0] = (ia[0] * ka[0]) + (id[0] * kd[0] * difusa) + (ie[0] * ke[0] * especular);
        res[1] = (ia[1] * ka[1]) + (id[1] * kd[1] * difusa) + (ie[1] * ke[1] * especular);
        res[2] = (ia[2] * ka[2]) + (id[2] * kd[2] * difusa) + (ie[2] * ke[2] * especular);
        
        res[0] = Utils.truncate(res[0], 5);
        res[1] = Utils.truncate(res[1], 5);
        res[2] = Utils.truncate(res[2], 5);
        
        res[0] = res[0] > 1 ? 1 : res[0];
        res[1] = res[1] > 1 ? 1 : res[1];
        res[2] = res[2] > 1 ? 1 : res[2];
        
        res[0] = res[0] < 0 ? 0 : res[0];
        res[1] = res[1] < 0 ? 0 : res[1];
        res[2] = res[2] < 0 ? 0 : res[2];
        
        return new Color(res[0], res[1], res[2], 1);
    }
    
    
    public double[] getIa() {
        return ia;
    }

    public void setIa(double[] ia) {
        this.ia = ia;
    }

    public double[] getId() {
        return id;
    }

    public void setId(double[] id) {
        this.id = id;
    }

    public double[] getIe() {
        return ie;
    }

    public void setIe(double[] ie) {
        this.ie = ie;
    }

    public double[] getKa() {
        return ka;
    }

    public void setKa(double[] ka) {
        this.ka = ka;
    }

    public double[] getKd() {
        return kd;
    }

    public void setKd(double[] kd) {
        this.kd = kd;
    }

    public double[] getKe() {
        return ke;
    }

    public void setKe(double[] ke) {
        this.ke = ke;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double[] getEye() {
        return eye;
    }

    public void setEye(double[] eye) {
        this.eye = eye;
    }

    public double[] getLuz() {
        return luz;
    }

    public void setLuz(double[] luz) {
        this.luz = luz;
    }
    
}
