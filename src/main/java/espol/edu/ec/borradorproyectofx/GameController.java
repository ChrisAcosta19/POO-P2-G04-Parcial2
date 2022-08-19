
package espol.edu.ec.borradorproyectofx;
import modelo.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class GameController implements Initializable, Serializable {

    @FXML private transient BorderPane mainPane;
    @FXML private transient GridPane buttonsPane;
    @FXML private transient TextField fieldRespuesta;
    @FXML private transient ImageView img00;
    @FXML private transient ImageView img01;
    @FXML private transient ImageView img02;
    @FXML private transient ImageView img03;
    @FXML private transient ImageView img10;
    @FXML private transient ImageView img11;
    @FXML private transient ImageView img12;
    @FXML private transient ImageView img13;
    @FXML private transient ImageView btnAvanzar;
    @FXML private transient ImageView btnRetroceder;
    @FXML private transient ImageView respuestaVisual;
    @FXML private transient Button btnVerificarRespuesta;
    @FXML private transient ImageView respuestaVisualMal;
    
    String[] images={"cow","cowg","cowh","duck","horse","horsea","horseb","pig","pigb","rooster","roosterb","sheep"};
    transient ImageView[] imagesLocation={img01,img02,img11,img12,img03,img13,img00,img10};
    private ArrayList <Integer> numImagenesXEjercicio=new ArrayList<>();
    private Boolean[] ToF={true,false};
    private int ejercicio=0;
    ArrayList<Ejercicio> ejercicios=new ArrayList<>();
    ArrayList<Game> actividades = new ArrayList<>();
    ArrayList<Game> resultados = new ArrayList<>();
    
    public static int timePromedio;
    public static int timeTotal;
    public static int fallosTotal;
    public String infoPorPregunta="";
    public Atencion a;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImage("arrow_right",btnAvanzar);
        setImage("arrow_left",btnRetroceder);
        
        btnRetroceder.setOnMouseClicked(eh -> {
            if(ejercicio==0){
            try {
                App.setRoot("gameMain");
            } catch (IOException ex) {
                ex.printStackTrace();
            }} else{ejercicio--;}
        });
        
        /*la atencion a partir de la cual se ejecuta el juego
        String fecha=a.getCita().getFecha();
        String cliente=a.getCita().getCliente().getCedula(); */
        
        String fecha="a";
        String cliente="0832834824";
        
        ArrayList <Ejercicio> ejerciciosVacio= new ArrayList<>();
        Game g1=new Game(GameMainController.numEjercicios,ejerciciosVacio);
        numImagenesXEjercicio=imagesPerQuestion(GameMainController.numEjercicios);
        
        
        
        for(int x:numImagenesXEjercicio){
            ArrayList <String> imagenesModelo= new ArrayList <>();
            int j=(int) Math.floor(Math.random()*2); boolean bool=false;
            if (j==1){bool=true;}
            imagesSelection(x,bool,imagenesModelo);
            Ejercicio ejercicio=new Ejercicio(x,imagenesModelo,0,false);
            g1.getEjercicios().add(ejercicio);
        }        
        
        File directorioCliente = new File("archivos/"+cliente);
        if(directorioCliente.exists()){
        actividades=Game.cargarActividades(cliente);
        resultados= Game.cargarResultados(cliente);
                try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/"+cliente+"/Games.bin"))) {
                actividades.add(g1);
                out.writeObject(actividades);
                out.flush();
                System.out.println("GUARDADO DE JUEGO EXITOSO");
                }catch (Exception e){System.out.println("No se pudo guardar la sesión");
                e.printStackTrace();}
        } else {
           try{
            directorioCliente.mkdir();
            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/"+cliente+"/Games.bin"))) {
                actividades.add(g1);
                out.writeObject(actividades);
                out.flush();
                System.out.println("GUARDADO DE JUEGO EXITOSO");
                }catch (Exception e){System.out.println("No se pudo guardar la sesión");
                e.printStackTrace();}
            } catch (Exception e){
            e.printStackTrace();
        } 
        }
        
        
        
        
        try {
            ejercicio(g1,ejercicio);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        try{
        btnVerificarRespuesta.setOnMouseClicked(eh -> {
            if(Integer.valueOf(fieldRespuesta.getText())==g1.getEjercicios().get(ejercicio).getRespuesta()){
            setImage("happy",respuestaVisual);
            if(!g1.getEjercicios().get(ejercicio).isDone()){
                g1.getEjercicios().get(ejercicio).done();
            }
            } else{setGif("globoe",respuestaVisual);
            if(!g1.getEjercicios().get(ejercicio).isDone()){
               g1.getEjercicios().get(ejercicio).intentosAumentar();
            }
            }
            });
        }catch (Exception ex) {
                ex.printStackTrace();}
        
        btnAvanzar.setOnMouseClicked(eh -> {
            respuestaVisual.imageProperty().set(null);
            fieldRespuesta.clear();
            ejercicio++;
            try {
                ejercicio(g1,ejercicio);
                
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (IndexOutOfBoundsException ex){
                try {
                for(Ejercicio e:g1.getEjercicios()){
                String xd=";"+e.getRespuesta()+","+e.getFallos()+","+e.getTime();
                infoPorPregunta+=xd;
                fallosTotal+=e.getFallos();
                timePromedio+=e.getTime();}
                timeTotal=timePromedio;
                timePromedio/=GameMainController.numEjercicios;
                String tiempo= Game.timeFormat(timeTotal);
                Game g2= new Game("¿Cuantos hay?",cliente,fecha,GameMainController.numEjercicios,fallosTotal,tiempo);
                System.out.println(g2);
                resultados.add(g2);
                
                try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/"+cliente+"/GamesResults.bin"))) {
                out.writeObject(resultados);
                out.flush();
                    System.out.println("GUARDADO DE JUEGO EXITOSO");
                }catch (Exception e){System.out.println("NO SE PUDO GUARDAR LA SESIÓN");
                e.printStackTrace();}
                               
                try(BufferedWriter writer = new BufferedWriter(new FileWriter("archivos/"+cliente+"/GamesDetalles.txt",true))){
                    String registro=g2.getCliente()+","+g2.getFecha()+","+g2.getNumEjercicios()+","+g2.getFallos()+","+g2.getTiempoEnFormato()+infoPorPregunta+"\n";
                    writer.write(registro);
                    writer.close();
                    System.out.println("GUARDADO DE RESULTADOS EXITOSO");                     
                }catch(Exception e){System.out.println("NO SE PUDO REGISTRAR LOS RESULTADOS DE LA SESION");
                e.printStackTrace();}
                
                App.setRoot("gameEnd");
            } catch (IOException exe){
                exe.printStackTrace();
            }       
            }
        });
        
        btnRetroceder.setOnMouseClicked(eh -> {
            if(ejercicio==0){
            try {
                App.setRoot("gameMain");
            } catch (IOException ex) {
                ex.printStackTrace();
            }} else{
            respuestaVisual.imageProperty().set(null);
            fieldRespuesta.clear();
            ejercicio--;
            try {
                ejercicio(g1,ejercicio);
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
            }
        });
        
        
    }
    
    
    void ejercicio(Game g, int ejercicio)throws IOException {
        img00.imageProperty().set(null);img01.imageProperty().set(null);img02.imageProperty().set(null);
        img03.imageProperty().set(null);img10.imageProperty().set(null);img11.imageProperty().set(null);
        img12.imageProperty().set(null);img13.imageProperty().set(null);
        ArrayList<String> imgs=new ArrayList<>();
        imagesLocation(g.getEjercicios().get(ejercicio).getImagenes());
        //cronometro por ejercicio
        Thread t = new Thread( () ->
        {
            while (!g.getEjercicios().get(ejercicio).isDone()){ //solo continua el contador si el parametro done es falso
                                                       //(el ejercicio no ha sido respondida correctamente)
                g.getEjercicios().get(ejercicio).time();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
                 );
        t.setDaemon(true);
        t.start();
        }
        
    
    void setImage(String name,ImageView iView){
        InputStream input = null;
           Image image = null;
            try {
                input = new FileInputStream(App.pathImgGame + name +".png");
                image = new Image(input, 100, 100, false, false);
                iView.setImage(image);

            } catch (Exception ex) {
                System.out.println("No se pudo cargar imagen");
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception ex) {
                        System.out.println("Error al cerrar el recurso");
                    }
                }
            }
    }
    
    
    
    void setGif(String name,ImageView iView){
        InputStream input = null;
           Image image = null;
            try {
                input = new FileInputStream(App.pathImgGame + name +".gif");
                image = new Image(input, 100, 100, false, false);
                iView.setImage(image);

            } catch (Exception ex) {
                System.out.println("No se pudo cargar imagen");
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception ex) {
                        System.out.println("Error al cerrar el recurso");
                    }
                }
            }
    }
    
    void respuesta(Ejercicio e,boolean a){
        if(a) {e.done();} else e.intentosAumentar();
        }
                 
   ImageView getIView(int i){
        ImageView iv=null;
        if(i==0){iv= img01;}
        if(i==1){iv= img02;}
        if(i==2){iv= img11;}
        if(i==3){iv= img12;}
        if(i==4){iv= img03;}
        if(i==5){iv= img13;}
        if(i==6){iv= img00;}
        if(i==7){iv= img10;}
        return iv;
    }
    
    ArrayList <Integer> imagesPerQuestion(int numEjercicios){
    ArrayList <Integer> imagesPerQ=new ArrayList<>();
        int a;
        if (numEjercicios<=5){
          for(int b=0;b<numEjercicios;b++){
            a= (int)Math.floor(Math.random()*4)+1;
            imagesPerQ.add(a);}
          
        } else if (numEjercicios<=10){
            for(int b=0;b<4;b++){
              a= (int)Math.floor(Math.random()*4)+1;
              imagesPerQ.add(a);}
            
            for(int b=0;b<numEjercicios-4;b++){
              a= (int)Math.floor(Math.random()*6)+1;
              imagesPerQ.add(a);}
        
        } else if (numEjercicios>10){
            for(int b=0;b<4;b++){
              a= (int)Math.floor(Math.random()*4)+1;
              imagesPerQ.add(a);}
            
            for(int b=0;b<4;b++){
              a= (int)Math.floor(Math.random()*6)+1;
              imagesPerQ.add(a);}
  
            if (numEjercicios==11){
              for(int b=0;b<3;b++){
                a= (int)Math.floor(Math.random()*7)+2;
                imagesPerQ.add(a);}
              
            } else{
              for(int b=0;b<3;b++){
                a= (int)Math.floor(Math.random()*5)+4;
                imagesPerQ.add(a);}
              
              for(int b=0;b<numEjercicios-11;b++){
                a= (int)Math.floor(Math.random()*8)+1;
                imagesPerQ.add(a);}
            }
 
        }
        return imagesPerQ;
    }
    
    void imagesLocation(ArrayList <String> imagenes){
        int n=imagenes.size();
            for(int r=0;r<=(n-1);r++){
                String imagenElegida= imagenes.get(r);
                ImageView iv=getIView(r);
                setImage(imagenElegida,iv);
            }
    }
    
    void imagesSelection(int n, boolean imgDif,ArrayList<String> imagenesElegidas){
        String imagenElegida=null;
        ImageView iv=null;
        if (imgDif){
            for(int r=0;r<=(n-1);r++){
                imagenElegida=images[(int) Math.floor(Math.random()*12)];
                imagenesElegidas.add(imagenElegida);
            }   
        } else if (!imgDif){
            imagenElegida=images[(int) Math.floor(Math.random()*12)];
            for(int r=0;r<=(n-1);r++){
                imagenesElegidas.add(imagenElegida);
            }
        }
    }
   
    
}