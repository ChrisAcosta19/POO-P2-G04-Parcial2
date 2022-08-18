
package espol.edu.ec.borradorproyectofx;

import java.io.BufferedWriter;
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
    
    String[] images={"cow","cowg","cowh","duck","horse","horsea","horseb","pig","pigb","rooster","roosterb","sheep"};
    transient ImageView[] imagesLocation={img01,img02,img11,img12,img03,img13,img00,img10};
    private ArrayList <Integer> numImagenesXEjercicio=new ArrayList<>();
    private Boolean[] ToF={true,false};
    private int ejercicio=0;
    ArrayList<Ejercicio> ejercicios=new ArrayList<>();
    @FXML
    private transient ImageView respuestaVisualMal;
    public static int timePromedio;
    public static int timeTotal;
    public String infoPorPregunta="";
    
    
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
        //asumiendo que este es el numero ingresado en el text field del main controller
        
        ArrayList <Ejercicio> ejerciciosVacio= new ArrayList<>();
        Game g1=new Game(GameMainController.numEjercicios,ejerciciosVacio);
        numImagenesXEjercicio=imagesPerQuestion(GameMainController.numEjercicios);
        Thread t = new Thread( () ->
        {
            while (true){
                g1.time();
                try {
                    Thread.sleep(1000); //dormir 5 min, por ahora 1 para probar
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
                 );
        
        t.setDaemon(true);
        t.start();
        for(int x:numImagenesXEjercicio){
            ArrayList <String> imagenesModelo= new ArrayList <>();
            int j=(int) Math.floor(Math.random()*2); boolean bool=false;
            if (j==1){bool=true;}
            imagesSelection(x,bool,imagenesModelo);
            Ejercicio lista=new Ejercicio(x,imagenesModelo,1,false);
            g1.ejercicios.add(lista); 
        }        
        
        try {
            ejercicio(g1,ejercicio);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        try{
        btnVerificarRespuesta.setOnMouseClicked(eh -> {
            if(Integer.valueOf(fieldRespuesta.getText())==g1.ejercicios.get(ejercicio).respuesta){
            setImage("happy",respuestaVisual);
            if(!g1.ejercicios.get(ejercicio).done){
                g1.ejercicios.get(ejercicio).done();
            }
            } else{setGif("globoe",respuestaVisual);
            if(!g1.ejercicios.get(ejercicio).done){
               g1.ejercicios.get(ejercicio).intentosAumentar();
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
                for(Ejercicio e:g1.ejercicios){
                String xd=";"+e.respuesta+","+e.intentos+","+e.time;
                infoPorPregunta+=xd;
                timePromedio+=e.time;}
                timePromedio/=GameMainController.numEjercicios;
                timeTotal=g1.time;
                try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/Games.bin"))) {
                out.writeObject(g1);
                out.flush();
                }catch (Exception e){System.out.println("No se pudo guardar la sesión");
                e.printStackTrace();}
                
                
                
                try(BufferedWriter writer = new BufferedWriter(new FileWriter("archivos/GameResultados.txt",true))){
                    
                    System.out.println(timeTotal);
                    String registro=g1.numEjercicios+","+timeTotal+infoPorPregunta+"\n";
                    writer.write(registro);
                    writer.close();
                    
                                       
                }catch(Exception e){System.out.println("No se pudo registrar los resultados de la sesión");
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
        imagesLocation(g.ejercicios.get(ejercicio).imagenes);
        //cronometro por ejercicio
        Thread t = new Thread( () ->
        {
            while (!g.ejercicios.get(ejercicio).done){ //solo continua el contador si el parametro done es falso
                                                       //(el ejercicio no ha sido respondida correctamente)
                g.ejercicios.get(ejercicio).time();
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
   
    class Game implements Serializable{

    int numEjercicios;
    ArrayList<Ejercicio> ejercicios;
    int time=0;

    public Game(int numEjercicios){
        this.numEjercicios = numEjercicios;
    }

    public Game(int numEjercicios, ArrayList<Ejercicio> ejercicios) {
        this.numEjercicios = numEjercicios;
        this.ejercicios = ejercicios;
    }

    public void time(){
            time++;
                }
    
    
    
    }

 
    class Ejercicio implements Serializable{
        int respuesta;
        ArrayList<String> imagenes;
        int intentos;
        boolean done;
        int time=0;
        

        public Ejercicio(int respuesta, ArrayList<String> imagenes, int intentos, boolean done) {
            this.respuesta = respuesta;
            this.imagenes = imagenes;
            this.intentos = intentos;
            this.done= done;
        }
        
        public void intentosAumentar(){
            intentos++;
        }
        
        public void done(){
            done=true;
        }
        
        public void time(){
            time++;
                }

        @Override
        public String toString() {
            return "Ejercicio{" + "respuesta=" + respuesta + ", intentos=" + intentos + ", time=" + time + "s}";
        }


        
        
        
    }  
}