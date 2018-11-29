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
public class ET {
    private No[] lista;
    
    public ET(int tamanho)
    {
        lista = new No[tamanho];
        init(tamanho);
    }
    
    public void init(int tam)
    {
        for (int i = 0; i < tam; i++) {
            lista[i] = null;
        }
    }
    
    public int getHeight()
    {
        return lista.length;
    }
    
    public No getPos(int pos)
    {
        return lista[pos];
    }
    
    public void inserePos(int pos, No no)
    {
        No aux;
        if(lista[pos] == null)
            lista[pos] = no;
        else
        {
            aux = lista[pos];
            while(aux.getProx() != null)
                aux = aux.getProx();
            aux.setProx(no);
            aux.getProx().setAnt(aux);
        }
    }
}
