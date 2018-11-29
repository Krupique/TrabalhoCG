package trabalhocg.structures;

/**
 * @author Henrique K. Secchi
 */
public class Face {
    private int face1;
    private int face2;
    private int face3;

    public Face(int face1, int face2, int face3) {
        this.face1 = face1;
        this.face2 = face2;
        this.face3 = face3;
    }

    public int getFace1() {
        return face1;
    }

    public void setFace1(int face1) {
        this.face1 = face1;
    }

    public int getFace2() {
        return face2;
    }

    public void setFace2(int face2) {
        this.face2 = face2;
    }

    public int getFace3() {
        return face3;
    }

    public void setFace3(int face3) {
        this.face3 = face3;
    }
}
