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
public class AET {
    private No inicio;
    private No fim;
    private int tl;
    
    public AET() {
        inicio = fim = null;
        tl = 0;
    }
    
    public void init() {
        inicio = fim = null;
        tl = 0;
    }
    
    public No getInicio()
    {
        return inicio;
    }
    
    public No getFim()
    {
        return fim;
    }
    
    public void removerY(int y)
    {
        No aux = inicio;
        //Remover do inicio.
        while(aux != null && aux.getYmax() == y)
        {
            aux = aux.getProx();
            inicio = aux;
            tl--;
        }
        
        
        //Tem mais de uma caixa
        if(inicio != null && inicio.getProx() != null)
        {
            inicio.setAnt(null);
            aux = inicio.getProx();
            while(aux.getProx() != null)
            {
                if(aux.getYmax() == y)
                {
                    aux.getAnt().setProx(aux.getProx());
                    aux.getProx().setAnt(aux.getAnt());
                    tl--;
                }
                aux = aux.getProx();
            }
            
            if(aux.getYmax() == y)
            {
                fim = aux.getAnt();
                fim.setProx(null);
                tl--;
            }
        }
        else //So tem uma caixa
            fim = inicio;
    }
    
    public int inicializaAET(int height, ET et) {
        No aux;
        int y = 0;
        boolean flag = true;
        for (int i = 0; i < height && flag; i++) {
            if(et.getPos(i) != null)
            {
                y = i;
                inicio = et.getPos(i);
                flag = false;
            }
        }
        aux = inicio;
        while(aux.getProx() != null)
        {
            tl++;
            aux = aux.getProx();
        }
        tl++;
        
        fim = aux;
        return y;
    }
    
    public void ordernarX()
    {
        for (No tl1 = fim; tl1 != inicio.getProx(); tl1 = tl1.getAnt()) {
            for (No i = inicio; i != tl1; i = i.getProx()) {
                if(i.getXmin() > i.getProx().getXmin())
                {
                    swap(i, i.getProx());
                }
            }
        }
    }
    
    private void swap(No j, No prox) {
        double xmin, ymax, incX, zmin, incZemY, rxmin, gymin, bzmin, incRx, incGx, incBx;
        xmin = j.getXmin();
        ymax = j.getYmax();
        incX = j.getXmin();
        
        incZemY = j.getIncZemY();
        rxmin = j.getRxmin();
        gymin = j.getGymin();
        bzmin = j.getBzmin();
        
        incRx = j.getIncRx();
        incGx = j.getIncGx();
        incBx = j.getIncBx();
        ////////////////////////////////
        j.setXmin(prox.getXmin());
        j.setYmax(prox.getYmax());
        j.setIncX(prox.getIncX());
        
        j.setIncZemY(prox.getIncZemY());
        j.setRxmin(prox.getRxmin());
        j.setGymin(prox.getGymin());
        j.setBzmin(prox.getBzmin());
        
        j.setIncRx(prox.getIncRx());
        j.setIncGx(prox.getIncGx());
        j.setIncBx(prox.getIncBx());
        /////////////////////////////////////
        prox.setXmin(xmin);
        prox.setYmax(ymax);
        prox.setIncX(incX);
        
        prox.setIncZemY(incZemY);
        prox.setRxmin(rxmin);
        prox.setGymin(gymin);
        prox.setBzmin(bzmin);
        
        prox.setIncRx(incRx);
        prox.setIncGx(incGx);
        prox.setIncBx(incBx);
    }
    
    public int getTl()
    {
        return tl;
    }
    
    public No getPos(int pos)
    {
        No aux = inicio;
        for (int i = 0; i < pos; i++) {
            aux = aux.getProx();
        }
        return aux;
    }
    
    public void incX()
    {
        No aux = inicio;
        while(aux != null)
        {
            aux.setRxmin(aux.getRxmin() + aux.getIncRx());
            aux.setGymin(aux.getGymin()+ aux.getIncGx());
            aux.setBzmin(aux.getBzmin()+ aux.getIncBx());
            
            aux.setXmin(aux.getXmin() + aux.getIncX());
            aux.setZmin(aux.getZmin() + aux.getIncZemY());
            aux = aux.getProx();
        }
    }
    
    public void setInc(double incr, double incg, double incb)
    {
        No aux = inicio;
        while(aux != null)
        {
            aux.setIncRx(incr);
            aux.setIncGx(incg);
            aux.setIncBx(incb);
            
            aux = aux.getProx();
        }
    }
    
    public void inserirNovasCaixas(No no)
    {
        No aux;
        if(inicio == null)
            inicio = no;
        else
        {
            no.setAnt(fim);
            fim.setProx(no);
        }
        tl = 1;
        aux = inicio;
        while(aux.getProx() != null)
        {
            tl++;
            aux = aux.getProx();
        }
        fim = aux;
    }
}
