package trabalhocg.structures;

/**
 *
 * @author Henrique K. Secchi
 */
public class Vertice {
    private double x;
    private double y;
    private double z;

    public Vertice(double x, double y, double z) {
        //System.out.println("Mult: " + MultFactor.MULT);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertice(Vertice vertice) {
        this.x = vertice.getX();
        this.y = vertice.getY();
        this.z = vertice.getZ();
    }
    

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    
    public double[][] getPonto()
    {
        double[][] ponto = {{x},{y},{z},{1}};
        return ponto;
    }
    
    public void setPonto(double[][] ponto)
    {
        x = ponto[0][0];
        y = ponto[1][0];
        z = ponto[2][0];
    }
}
