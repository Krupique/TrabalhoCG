/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhocg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import trabalhocg.math.Matriz;
import trabalhocg.math.Transformacoes;
import trabalhocg.structures.Face;
import trabalhocg.structures.Pixel;
import trabalhocg.structures.Vertice;
import trabalhocg.utils.Desenhar;
import trabalhocg.utils.Utils;

/**
 *
 * @author Henrique K. Secchi
 */
public class TelaPrincipalController implements Initializable {

    @FXML
    private JFXRadioButton rdbtParalela;
    @FXML
    private JFXRadioButton rdbtCavaleira;
    @FXML
    private JFXRadioButton rdbtCabinet;
    @FXML
    private JFXRadioButton rdbtPerspectiva;
    @FXML
    private JFXCheckBox checkFaces;
    @FXML
    private ImageView imageview;
    @FXML
    private ImageView btViewPort;
    @FXML
    private VBox vbox;
    
    private Image img;
    private Image imgViewport;
    private ArrayList<Vertice> listaVertice = new ArrayList<>();
    private ArrayList<Vertice> listaVertAtual = new ArrayList<>();
    private ArrayList<Vertice> listaVerticesNorma = new ArrayList<>();
    
    private ArrayList<Vertice> listaViewPortCima = new ArrayList<>();
    private ArrayList<Vertice> listaViewPortLado = new ArrayList<>();
    private ArrayList<Vertice> listaViewPortBaixo = new ArrayList<>();
    private ArrayList<Face> listaFaces = new ArrayList<>();
    private Pixel[][] matTela = null;
    private Pixel[][] matTelaViewPort = null;
    private double[] posLuz;
    private double[] intensidades;
    
    private int centroX;
    private int centroY;
    private int[] posIni;
    private Color corObj;
    private Color corAmb;
    private Color corBrilho;
    
    @FXML
    private VBox vboxMenuSide;
    @FXML
    private ImageView btViewPort1;
    @FXML
    private ImageView viewportcima;
    @FXML
    private ImageView viewportfrente;
    @FXML
    private ImageView viewportlado;
    @FXML
    private JFXButton btLuz;
    @FXML
    private JFXColorPicker corLuz;
    @FXML
    private JFXColorPicker corObjeto;
    @FXML
    private ImageView imageviewlogo;
    @FXML
    private JFXColorPicker corAmbiente;
    @FXML
    private JFXRadioButton rdAramado;
    @FXML
    private JFXRadioButton rdShading;
    @FXML
    private AnchorPane paneMetodo;
    @FXML
    private JFXRadioButton rdFlat;
    @FXML
    private JFXRadioButton rdGourard;
    @FXML
    private JFXRadioButton rdPhong;
    @FXML
    private AnchorPane paneIluminacao;
    @FXML
    private JFXSlider sliderPespec;
    @FXML
    private JFXSlider sliderLuz;
    @FXML
    private JFXSlider sliderObjeto;
    @FXML
    private JFXSlider sliderAmbiente;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        centroX = (int)img.getWidth() / 2;
        centroY = (int)img.getHeight()/ 2;
        posLuz = new double[3];
        posLuz[2] = 150;
        intensidades = new double[]{1,1,1};
        
        
        corObj = new Color(0.760, 0.149, 0.141, 1);
        corAmb = new Color(0.141, 0.176, 0.192, 1);
        corBrilho = new Color(0.996, 0.929, 0.635, 1);
        
        corAmbiente.setValue(corAmb);
        corObjeto.setValue(corObj);
        corLuz.setValue(corBrilho);
        btLuz.setStyle("-fx-background-color: " + Utils.toHEXCode(corLuz.getValue()) + ";-fx-background-radius: 15em");
        
        set_rdButtons(true, false, false, false); 
        rdAramado.setSelected(true);
        rdShading.setSelected(false);
        checkFaces.setSelected(false);
        paneMetodo.setVisible(false);
        paneIluminacao.setVisible(false);
        btLuz.setVisible(false);
    } 
    
    public void init()
    {
        img = new Image("trabalhocg/resources/3d.png");
        imageviewlogo.setImage(img);
        posIni = new int[2];
        clear_screen();
        vboxMenuSide.setVisible(false);
        listaVertice = new ArrayList<>();
        listaFaces = new ArrayList<>();
        listaVertice.add(new Vertice(0, 0, 0));
    }
    
    public void set_rdButtons(boolean v1, boolean v2, boolean v3, boolean v4)
    {
        rdbtParalela.setSelected(v1);
        rdbtCavaleira.setSelected(v2);
        rdbtCabinet.setSelected(v3);
        rdbtPerspectiva.setSelected(v4);
        sliderPespec.setVisible(v4);
    }
    
    public void clear_screen()
    {
        img = new Image("trabalhocg/resources/background.png");
        imageview.setImage(img);
        matTela = new Pixel[(int)img.getHeight()][(int)img.getWidth()];
        matTela = Matriz.init_matTela(matTela);
        
        imgViewport = new Image("trabalhocg/resources/background_viewport.png");
        viewportcima.setImage(imgViewport);
        viewportfrente.setImage(imgViewport);
        viewportlado.setImage(imgViewport);
        
        matTelaViewPort = new Pixel[210][210];
        matTelaViewPort = Matriz.init_matTela(matTela);
    }
    
    
    
    public void add_vertices_atuais()
    {
        listaVertAtual = new ArrayList<>();
        for (int i = 0; i < listaVertice.size(); i++)
            listaVertAtual.add(new Vertice(listaVertice.get(i)));
    }
    
    public void add_vertices_viewPort()
    {
        listaVertAtual = new ArrayList<>();
        for (int i = 0; i < listaVertice.size(); i++)
        {
            listaViewPortCima.add(new Vertice(listaVertAtual.get(i)));
            listaViewPortBaixo.add(new Vertice(listaVertAtual.get(i)));
            listaViewPortLado.add(new Vertice(listaVertAtual.get(i)));
        }
    }
    public void desenhar_shading_flat(double[][] mat, boolean flag)
    {
        matTela = Desenhar.desenhar_shading_flat(matTela, mat, listaVertAtual, listaFaces, centroX, centroY, corAmb, corBrilho, corObj, posLuz, flag, intensidades);
        img = Desenhar.convert_to_image(matTela, img);
        imageview.setImage(img);
    }
    
    public void desenhar_shading_gourard(double[][] mat, boolean flag)
    {
        matTela = Desenhar.desenhar_shading_gouraud(matTela, mat, listaVertAtual, listaFaces, centroX, centroY, corAmb, corBrilho, corObj, posLuz, flag, intensidades);
        img = Desenhar.convert_to_image(matTela, img);
        imageview.setImage(img);
    }
    
    public void desenhar_shading_phong(double[][] mat, boolean flag)
    {
        matTela = Desenhar.desenhar_shading_phong(matTela, mat, listaVertAtual, listaFaces, centroX, centroY, corAmb, corBrilho, corObj, posLuz, flag, intensidades);
        img = Desenhar.convert_to_image(matTela, img);
        imageview.setImage(img);
    }
    
    public void desenhar_com_faces_ocultas(double[][] mat)
    {
        matTela = Desenhar.desenhar_com_faces_ocultas(matTela, mat, listaVertAtual, listaFaces, centroX, centroY, Color.WHITE);
        img = Desenhar.convert_to_image(matTela, img);
        imageview.setImage(img);
    }
    
    public void desenhar_sem_faces_ocultas(double[][] mat)
    {    
        matTela = Desenhar.desenhar_sem_faces_ocultas(matTela, mat, listaVertAtual, listaFaces, centroX, centroY, Color.WHITE);
        img = Desenhar.convert_to_image(matTela, img);
        imageview.setImage(img);
    }
    
    public void desenhar_paralelo()
    {
        double[][] matParalela = {{1, 0, 0, 0}, 
        /**/                      {0, 1, 0, 0}, 
        /**/                      {0, 0, 0, 0}, 
        /**/                      {0, 0, 0, 1}};
        if(rdShading.isSelected())
        {
            if(rdFlat.isSelected())
                desenhar_shading_flat(matParalela, true);
            else if(rdGourard.isSelected())
            {
                desenhar_shading_gourard(matParalela, true);
            }
            else //if(rdPhong.isSelected())
            {
                desenhar_shading_phong(matParalela, true);
            }
        }
        else if(checkFaces.isSelected())
            desenhar_com_faces_ocultas(matParalela);
        else
            desenhar_sem_faces_ocultas(matParalela);
    }
    
    public void desenhar_cavaleira()
    {
        double[][] matCavaleira = {{1,0, 1 * Math.cos(45 * Math.PI / 180),0},
        /**/                       {0,1, 1 * Math.sin(45 * Math.PI / 180),0}, 
        /**/                       {0,0,0,0}, 
        /**/                       {0,0,0,1}};
        if(rdShading.isSelected())
        {
            if(rdFlat.isSelected())
                desenhar_shading_flat(matCavaleira, false);
            else if(rdGourard.isSelected())
            {
                desenhar_shading_gourard(matCavaleira, false);
            }
            else //if(rdPhong.isSelected())
            {
                
            }
        }
        else if(checkFaces.isSelected())
            desenhar_com_faces_ocultas(matCavaleira);
        else
            desenhar_sem_faces_ocultas(matCavaleira);
    }
    
    public void desenhar_cabinet()
    {
        double[][] matCabinet = {{1,0,0.5 * Math.cos(45 * Math.PI / 180),0},
        /**/                     {0,1, 0.5 * Math.sin(45 * Math.PI / 180),0}, 
        /**/                     {0,0,0,0}, 
        /**/                     {0,0,0,1}};
        if(rdShading.isSelected())
        {
            if(rdFlat.isSelected())
                desenhar_shading_flat(matCabinet, false);
            else if(rdGourard.isSelected())
            {
                desenhar_shading_gourard(matCabinet, false);
            }
            else //if(rdPhong.isSelected())
            {
                
            }
        }
        else if(checkFaces.isSelected())
            desenhar_com_faces_ocultas(matCabinet);
        else
            desenhar_sem_faces_ocultas(matCabinet);
    }
    
    public void desenhar_perspectiva()
    {
        double d;
        
        d = sliderPespec.getValue() / 100;
        double[][] matPerspectiva = {{d, 0, 0, 0}, 
        /**/                         {0, d, 0, 0}, 
        /**/                         {0, 0, 0, 0}, 
        /**/                         {0, 0, 1, d}};
        if(rdShading.isSelected())
        {
            if(rdFlat.isSelected())
                desenhar_shading_flat(matPerspectiva, true);
            else if(rdGourard.isSelected())
            {
                desenhar_shading_gourard(matPerspectiva, false);
            }
            else //if(rdPhong.isSelected())
            {
                
            }   
        }
        else if(checkFaces.isSelected())
            desenhar_com_faces_ocultas(matPerspectiva);
        else
            desenhar_sem_faces_ocultas(matPerspectiva);
    }
    
    public void draw_obj()
    {
        clear_screen();
        if(rdbtParalela.isSelected()) //Projecao paralela
        {
            desenhar_paralelo();
        } else if(rdbtCavaleira.isSelected()) //Projecao Cavaleira
        {
            desenhar_cavaleira();
        } else if(rdbtCabinet.isSelected()) //Projecao Cabinet
        {
            desenhar_cabinet();
        }
        else if(rdbtPerspectiva.isSelected()) //Projecao Perspectiva
        {
            desenhar_perspectiva();
        }

        if(vboxMenuSide.isVisible())
            desenhar_viewport();
      
    }

    @FXML
    private void evtAbrir(ActionEvent event) {
        Properties prop = System.getProperties();
        Object[] obj;
        FileChooser fc = new FileChooser();
        
        if(prop.getProperty("os.name").toLowerCase().contains("windows"))
            fc.setInitialDirectory(new File("C:/"));
        else
            fc.setInitialDirectory(new File("/home"));
        
        File arq = fc.showOpenDialog(null);
        if(arq != null)
        {
            init();
            obj = Utils.add_vert_face(arq, listaVertice, listaFaces); //Adicionar na lista de vértices e faces.
            listaVertice = (ArrayList<Vertice>)obj[0];
            listaFaces = (ArrayList<Face>)obj[1];
            add_vertices_atuais();
            
            draw_obj();//Desenhar na tela.
        }
    }
    
    @FXML
    private void evtDesfazer(ActionEvent event) {
        add_vertices_atuais();
        draw_obj();
    }

    @FXML
    private void evtLimpar(ActionEvent event) {
        init();
    }

    @FXML
    private void evtDragged(MouseEvent event) {
        listaVertAtual = Transformacoes.Rotacao(listaVertAtual, event, posIni, centroX);
        draw_obj();
    }

    @FXML
    private void evtScroll(ScrollEvent event) {
        double deltay = event.getDeltaY();
        listaVertAtual = Transformacoes.Escala(listaVertAtual, deltay);
        draw_obj();
    }

    @FXML
    private void evtProjecaoParalela(ActionEvent event) {
        set_rdButtons(true, false, false, false);
        draw_obj();
    }

    @FXML
    private void evtProjecaoCavaleira(ActionEvent event) {
        set_rdButtons(false, true, false, false);
        draw_obj();
    }

    @FXML
    private void evtProjecaoCabinet(ActionEvent event) {
        set_rdButtons(false, false, true, false);
        draw_obj();
    }

    @FXML
    private void evtProjecaoPerspectiva(ActionEvent event) {
        set_rdButtons(false, false, false, true);
        draw_obj();
    }
    
    public void desenhar_viewport()
    {
        double ang = 90 * Math.PI / 180;
        double[][] matCima = {{1, 0, 0, 0}, 
        /**/                  {0, Math.cos(ang), -Math.sin(ang), 0}, 
        /**/                  {0, Math.sin(ang), Math.cos(ang), 0}, 
        /**/                  {0, 0, 0, 1}};
        
        double[][] matLado = {{Math.cos(ang), 0, Math.sin(ang), 0}, 
        /**/                  {0, 1, 0, 0}, 
        /**/                  {-Math.sin(ang), 0, Math.cos(ang), 0}, 
        /**/                  {0, 0, 0, 1}};
        
        double[][] matBaixo = {{1, 0, 0, 0}, 
        /**/                   {0, Math.cos(-ang), -Math.sin(-ang), 0}, 
        /**/                   {0, Math.sin(-ang), Math.cos(-ang), 0}, 
        /**/                   {0, 0, 0, 1}};
        
        draw_viewport(matCima, matLado, matBaixo);
    }
    
    public void draw_viewport(double[][] vpCima, double[][] vpLado, double[][] vpBaixo)
    {
        double[][] mat = {{((double)210 )/ 680, 0, 0, 0}, 
        /**/              {0, ((double)210 )/ 680, 0, 0}, 
        /**/              {0, 0, 1, 0}, 
        /**/              {0, 0, 0, 1}};
        
        vpCima = Matriz.multiplicar_matriz(mat, vpCima);
        vpLado = Matriz.multiplicar_matriz(mat, vpLado);
        vpBaixo = Matriz.multiplicar_matriz(mat, vpBaixo);
        
        int centroX = (int)imgViewport.getWidth() / 2;
        int centroY = (int)imgViewport.getHeight()/ 2;
        
        imgViewport = Desenhar.desenhar_viewport(imgViewport ,matTelaViewPort, checkFaces, vpCima, listaVertAtual, listaFaces, centroX, centroY, matTela, Color.WHITE);
        viewportcima.setImage(imgViewport);
        
        imgViewport = Desenhar.desenhar_viewport(imgViewport ,matTelaViewPort, checkFaces, vpLado, listaVertAtual, listaFaces, centroX, centroY, matTela, Color.WHITE);
        viewportlado.setImage(imgViewport);
        
        imgViewport = Desenhar.desenhar_viewport(imgViewport ,matTelaViewPort, checkFaces, vpBaixo, listaVertAtual, listaFaces, centroX, centroY, matTela, Color.WHITE);
        viewportfrente.setImage(imgViewport);
    }
    
    @FXML
    private void evtAbrirTelaViewPort(MouseEvent event) {
        //desenhar_viewport();
        clear_screen();
        draw_obj();
        desenhar_viewport();
        vboxMenuSide.setVisible(true);
    }
    
    @FXML
    private void evtFecharViewPort(MouseEvent event) {
        vboxMenuSide.setVisible(false);
    }

    @FXML
    private void evtPressed(MouseEvent event) {
        posIni[0] = (int)event.getX();
        posIni[1] = (int)event.getY();
    }

    @FXML
    private void evtKeyPressed(KeyEvent event) {
        listaVertAtual = Transformacoes.Translacao(listaVertAtual, event);
        draw_obj();
    }

    @FXML
    private void evtSelectCheckBox(ActionEvent event) {
        draw_obj();
    }

    @FXML
    private void evtDraggedBtLuz(MouseEvent event) {
        posLuz[0] = event.getSceneX() - 300;
        posLuz[1] = event.getSceneY();
        
        if((posLuz[0] >= 0 && posLuz[0] <= 680) && (posLuz[1] >= 0 && posLuz[1] <= 680))
        {
            btLuz.setLayoutX(posLuz[0] - btLuz.getWidth() / 2);
            btLuz.setLayoutY(posLuz[1] - btLuz.getHeight() / 2);
            draw_obj();
        }
    }

    @FXML
    private void evtCorLuz(ActionEvent event) {
        btLuz.setStyle("-fx-background-color: " + Utils.toHEXCode(corLuz.getValue()) + ";-fx-background-radius: 15em");
        corBrilho = corLuz.getValue();
        draw_obj();
    }

    @FXML
    private void evtCorObjeto(ActionEvent event) {
        corObj = corObjeto.getValue();
        draw_obj();
    }


    @FXML
    private void evtCorAmbiente(ActionEvent event) {
        corAmb = corAmbiente.getValue();
        draw_obj();
    }

    @FXML
    private void evtCheckFlat(ActionEvent event) {
        set_rdButtonsShadding(true, false, false);
        draw_obj();
    }

    @FXML
    private void evtcheckGourard(ActionEvent event) {
        set_rdButtonsShadding(false, true, false);
        draw_obj();
    }

    @FXML
    private void evtcheckPhong(ActionEvent event) {
        set_rdButtonsShadding(false, false, true);
        draw_obj();
    }
    
    private void set_rdButtonsShadding(boolean v1, boolean v2, boolean v3)
    {
        rdFlat.setSelected(v1);
        rdGourard.setSelected(v2);
        rdPhong.setSelected(v3);
    }

    @FXML
    private void evtExibirAramado(ActionEvent event) {
        rdAramado.setSelected(true);
        checkFaces.setVisible(true);
        rdShading.setSelected(false);
        
        paneMetodo.setVisible(false);
        paneIluminacao.setVisible(false);
        btLuz.setVisible(false);
        
        draw_obj();
    }

    @FXML
    private void evtExibirShading(ActionEvent event) {
        rdAramado.setSelected(false);
        checkFaces.setVisible(false);
        rdShading.setSelected(true);
        
        paneMetodo.setVisible(true);
        paneIluminacao.setVisible(true);
        btLuz.setVisible(true);
        
        set_rdButtonsShadding(true, false, false);
        draw_obj();
    }

    @FXML
    private void evtLuzScroll(ScrollEvent event) {
        double deltay = event.getDeltaY();
        
        if(deltay < 0) //Mouse scroll baixo
        {
            posLuz[2] -= 30;
            btLuz.setPrefSize(btLuz.getPrefWidth() - 1, btLuz.getPrefHeight() - 1);
        }
        else //Mouse scroll cima
        {
            posLuz[2] += 30;
            btLuz.setPrefSize(btLuz.getPrefWidth() + 1, btLuz.getPrefHeight() + 1);
        }
        draw_obj();
    }

    @FXML
    private void evtSliderPerspec(MouseEvent event) {
        draw_obj();
        
    }

    @FXML
    private void evtsliderLuz(MouseEvent event) {
        intensidades[0] = sliderLuz.getValue() / 100;
        draw_obj();
    }

    @FXML
    private void evtsliderObjeto(MouseEvent event) {
        intensidades[1] = sliderObjeto.getValue() / 100;
        draw_obj();
    }

    @FXML
    private void evtsliderAmbiente(MouseEvent event) {
        intensidades[2] = sliderAmbiente.getValue() / 100;
        draw_obj();
    }

    
}
