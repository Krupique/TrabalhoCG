/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhocg.structures.list;

/**
 *
 * @author Henrique K. Secchi
 */
public class No {
    private double xmin;
    private double ymax;
    private double incX;
    
    private double zmin;
    private double incZemY;
    
    private double rxmin;
    private double gymin;
    private double bzmin;
    
    private double incRx;
    private double incGx;
    private double incBx;

    private No ant;
    private No prox;
    
    public No() {
    }
    
    public No(No no)
    {
        this.xmin = no.getXmin();
        this.ymax = no.getYmax();
        this.incX = no.getIncX();
        this.zmin = no.getZmin();
        this.incZemY = no.getIncZemY();
        this.rxmin = no.getRxmin();
        this.gymin = no.getGymin();
        this.bzmin = no.getBzmin();
        this.incRx = no.getIncRx();
        this.incGx = no.getIncGx();
        this.incBx = no.getIncBx();
        
        this.ant = no.getAnt();
        this.prox = no.getProx();
    }

    public No(double xmin, double ymax, double zmin, double incZemY, double incX, No ant, No prox) {
        this.xmin = xmin;
        this.ymax = ymax;
        this.incX = incX;
        this.zmin = zmin;
        this.incZemY = incZemY;
        
        this.ant = ant;
        this.prox = prox;
        
        this.rxmin = 0;
        this.gymin = 0;
        this.bzmin = 0;
        this.incRx = 0;
        this.incGx = 0;
        this.incBx = 0;
    }

    public No(double xmin, double ymax, double zmin, double incZemY, double incX, double rxmin, double gymin, double bzmin, double incRx, double incGx, double incBx, No ant, No prox) {
        this.xmin = xmin;
        this.ymax = ymax;
        this.incX = incX;
        this.zmin = zmin;
        this.incZemY = incZemY;
        this.rxmin = rxmin;
        this.gymin = gymin;
        this.bzmin = bzmin;
        this.incRx = incRx;
        this.incGx = incGx;
        this.incBx = incBx;
        this.ant = ant;
        this.prox = prox;
    }

    public double getXmin() {
        return xmin;
    }

    public void setXmin(double xmin) {
        this.xmin = xmin;
    }

    public double getYmax() {
        return ymax;
    }

    public void setYmax(double ymax) {
        this.ymax = ymax;
    }

    public double getIncX() {
        return incX;
    }

    public void setIncX(double incX) {
        this.incX = incX;
    }

    public double getZmin() {
        return zmin;
    }

    public void setZmin(double zmin) {
        this.zmin = zmin;
    }

    public double getIncZemY() {
        return incZemY;
    }

    public void setIncZemY(double incZemY) {
        this.incZemY = incZemY;
    }

    public double getRxmin() {
        return rxmin;
    }

    public void setRxmin(double rxmin) {
        this.rxmin = rxmin < 0 ? 0 : rxmin;
        this.rxmin = rxmin > 1 ? 1 : rxmin;
    }

    public double getGymin() {
        return gymin;
    }

    public void setGymin(double gymin) {
        this.gymin = gymin < 0 ? 0 : gymin;
        this.gymin = gymin > 1 ? 1 : gymin;
    }

    public double getBzmin() {
        return bzmin;
    }

    public void setBzmin(double bzmin) {
        this.bzmin = bzmin < 0 ? 0 : bzmin;
        this.bzmin = bzmin > 1 ? 1 : bzmin;
    }

    public double getIncRx() {
        return incRx;
    }

    public void setIncRx(double incRx) {
        this.incRx = incRx;
    }

    public double getIncGx() {
        return incGx;
    }

    public void setIncGx(double incGx) {
        this.incGx = incGx;
    }

    public double getIncBx() {
        return incBx;
    }

    public void setIncBx(double incBx) {
        this.incBx = incBx;
    }

    public No getAnt() {
        return ant;
    }

    public void setAnt(No ant) {
        this.ant = ant;
    }

    public No getProx() {
        return prox;
    }

    public void setProx(No prox) {
        this.prox = prox;
    }
    
    public void setNo(No no)
    {
        this.xmin = no.getXmin();
        this.ymax = no.getYmax();
        this.incX = no.getIncX();
        this.zmin = no.getZmin();
        this.incZemY = no.getIncZemY();
        this.rxmin = no.getRxmin();
        this.gymin = no.getGymin();
        this.bzmin = no.getBzmin();
        this.incRx = no.getIncRx();
        this.incGx = no.getIncGx();
        this.incBx = no.getIncBx();
        this.ant = no.getAnt();
        this.prox = no.getProx();
    }
}
