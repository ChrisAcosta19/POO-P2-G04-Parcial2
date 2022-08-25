
package espol.edu.ec.borradorproyectofx;
import static espol.edu.ec.borradorproyectofx.GameMainController.numEjercicios;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.*;
/**
 * FXML Controller class
 *
 * @author Juan Pablo Plúas
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
    private Boolean[] ToF={true, true, false, true, true, true, true};
    private int ejercicio=0;
    ArrayList<Ejercicio> ejercicios=new ArrayList<>();
    ArrayList<Game> actividades = new ArrayList<>();
    ArrayList<Game> resultados = new ArrayList<>();
    
    public static int timePromedio;
    public static int timeTotal;
    public static int fallosTotal;
    public String infoPorPregunta="";
    
    // parametros de prueba
    
    int c1=(int) Math.floor(Math.random()*App.clientesCedulas.size());
    String cliente=App.clientesCedulas.get(c1);
    String fecha="a";
    Game g2save;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setImage("arrow_right",App.pathImgGame,btnAvanzar);
        App.setImage("arrow_left",App.pathImgGame,btnRetroceder);
        
            
        /*la atencion a partir de la cual se ejecuta el juego
        String fecha=AtencionController.atencion.getCita().getFecha();
        String cliente=a.getCita().getCliente().getCedula(); */
       
        ArrayList <Ejercicio> ejerciciosVacio= new ArrayList<>();
        Game g=new Game(GameMainController.numEjercicios,ejerciciosVacio);
        numImagenesXEjercicio=imagesPerQuestion(GameMainController.numEjercicios);
        
        for(int x:numImagenesXEjercicio){
            ArrayList <String> imagenesModelo= new ArrayList <>();
            int j=(int) Math.floor(Math.random()*ToF.length); boolean bool=false;
            bool=ToF[j];
            imagesSelection(x,bool,imagenesModelo);
            Ejercicio ejercicio=new Ejercicio(x,imagenesModelo,0,false);
            g.getEjercicios().add(ejercicio);
        }        
        
        System.out.println(g);
        
        try {
            g2save=(Game)g.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        
        //
        boolean guardar=false;
        if (ActividadesController.replayGame==null){
        
            jugar(g,true);
            
        } else{
            Alert alert=new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Guardado de Resultados");
            alert.setHeaderText(null);
            alert.setContentText("¿Desea guardar los resultados de esta sesión de juego?");
            Optional<ButtonType> result= alert.showAndWait();
            if(result.get()==ButtonType.OK){
                guardar=true;
            }
            cliente=ClientesController.clienteSeleccionado.getCedula();
            jugar(ActividadesController.replayGame,guardar);
        }
        
        
        
    }
    
    void jugar(Game g, boolean guardarResultados){
        
        try {
            ejercicio(g,ejercicio);
        } catch (Exception ex) {}
        
        try{
        btnVerificarRespuesta.setOnMouseClicked(eh -> {
            int respuesta;
            try {
                respuesta=Integer.valueOf(fieldRespuesta.getText());
            } catch (Exception ex) {
                fieldRespuesta.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al ingresar número");
                    alert.setHeaderText(null);
                    alert.setContentText("Ingrese un número válido");
                    alert.showAndWait();
                    
            }
            if(Integer.valueOf(fieldRespuesta.getText())==g.getEjercicios().get(ejercicio).getRespuesta()){
            App.setImage("happy",App.pathImgGame,respuestaVisual);
            sonido(true);
            if(!g.getEjercicios().get(ejercicio).isDone()){
                g.getEjercicios().get(ejercicio).done();
            }
            } else{setGif("globoe",respuestaVisual);
            sonido(false);
            if(!g.getEjercicios().get(ejercicio).isDone()){
               g.getEjercicios().get(ejercicio).intentosAumentar();
            }
            }
            });
        }catch (Exception ex) {}
        
        btnAvanzar.setOnMouseClicked(eh -> {
            if(!g.getEjercicios().get(ejercicio).isDone()){
               Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("D:");
                    alert.setHeaderText(null);
                    alert.setContentText("La pregunta aún no ha sido respondida correctamente");
                    alert.showAndWait(); 
            }else{
            respuestaVisual.imageProperty().set(null);
            fieldRespuesta.clear();
            ejercicio++;
            try {
                ejercicio(g,ejercicio);
                
            } catch (IOException ex) {
            } catch (IndexOutOfBoundsException ex){
                
                if (ActividadesController.replayGame==null){
                
                File directorioPrincipal= new File("archivos/registro");
                File directorioCliente = new File("archivos/registro/"+cliente);

                if(!directorioPrincipal.exists()){
                   directorioPrincipal.mkdir(); 
                }

                if(directorioCliente.exists()){


                File fileResultados = new File("archivos/registro/"+cliente+"GamesResults.bin");
                File fileActividades = new File("archivos/registro/"+cliente+"GamesResults.bin");

                if (fileResultados.exists() && fileActividades.exists()){
                actividades=Game.cargarActividades(cliente);
                resultados= Game.cargarResultados(cliente);
                }

                        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/registro/"+cliente+"/Games.bin"))) {
                        actividades.add(g2save);
                        out.writeObject(actividades);
                        out.flush();
                        System.out.println("GUARDADO DE JUEGO EXITOSO");
                        }catch (Exception e){System.out.println("No se pudo guardar la sesión");
                        e.printStackTrace();}
                } else {
                   try{
                    directorioCliente.mkdir();
                    try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/registro/"+cliente+"/Games.bin"))) {
                        actividades.add(g2save);
                        out.writeObject(actividades);
                        out.flush();
                        System.out.println("GUARDADO DE JUEGO EXITOSO");
                        }catch (Exception e){System.out.println("No se pudo guardar la sesión");
                        e.printStackTrace();}
                    } catch (Exception e){
                    e.printStackTrace();
                } 
                }
                } else{
                    resultados= Game.cargarResultados(cliente);
                }
                
                try {
                for(Ejercicio e:g.getEjercicios()){
                String xd=";Número de imágenes: "+e.getRespuesta()+",Número de fallos: "+e.getFallos()+",Tiempo: "+Game.timeFormat(e.getTime());
                infoPorPregunta+=xd;
                fallosTotal+=e.getFallos();
                timePromedio+=e.getTime();}
                timeTotal=timePromedio;
                timePromedio/=g.getNumEjercicios();
                String tiempo= Game.timeFormat(timeTotal);
                Game g2= new Game("¿Cuantos hay?",cliente,fecha,g.getNumEjercicios(),fallosTotal,tiempo);
                System.out.println(g2);
                resultados.add(g2);
                if(guardarResultados){
                    
                try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/registro/"+cliente+"/GamesResults.bin"))) {
                out.writeObject(resultados);
                out.flush();
                    System.out.println("GUARDADO DE JUEGO EXITOSO");
                }catch (Exception e){System.out.println("NO SE PUDO GUARDAR LA SESIÓN");
                e.printStackTrace();}
                               
                try(BufferedWriter writer = new BufferedWriter(new FileWriter("archivos/registro/"+cliente+"/GamesDetalles.txt",true))){
                    String registro="Fecha: "+g2.getFecha()+",Número de ejercicios: "+g2.getNumEjercicios()+",Número de fallos: "+g2.getFallos()+",Tiempo total: "+g2.getTiempoEnFormato()+infoPorPregunta+"\n";
                    writer.write(registro);
                    writer.close();
                    System.out.println("GUARDADO DE RESULTADOS EXITOSO");                     
                }catch(Exception e){System.out.println("NO SE PUDO REGISTRAR LOS RESULTADOS DE LA SESION");
                e.printStackTrace();}
                ActividadesController.replayGame=null;
                App.setRoot("gameEnd");
                
            } else App.setRoot("gameEnd");
                
            }   catch (Exception ex1) {}     
    }
            }});
        
        btnRetroceder.setOnMouseClicked(eh -> {
            if(ejercicio==0){
                Alert alert=new Alert(AlertType.CONFIRMATION);
                alert.setTitle("¡OJO!");
                alert.setHeaderText(null);
                alert.setContentText("Si sales ahora, no se guardará el juego ni sus resultados, ¿desea salir?");
                Optional<ButtonType> result= alert.showAndWait();
                if(result.get()==ButtonType.OK){
            try {
                App.setRoot("gameMain");
            } catch (IOException ex) {
                ex.printStackTrace();
            }} 
            } else{
            respuestaVisual.imageProperty().set(null);
            fieldRespuesta.clear();
            ejercicio--;
            try {
                ejercicio(g,ejercicio);
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
            }
        });
        
    } 
    
    void sonido(boolean respuesta){
        InputStream input=null;
        Media media= null;
        MediaPlayer mp= null;
        try{
            
        if(respuesta){
            File file = new File(App.pathImgGame + "sonidoBien.mp3");
            media = new Media(file.toURI().toString());
            mp = new MediaPlayer(media);
            mp.play();
            System.out.println("REPRODUCIENDO");
        } else{
            File file = new File(App.pathImgGame + "sonidoMal.mp3");
            media = new Media(file.toURI().toString());
            mp = new MediaPlayer(media);
            mp.play();
            System.out.println("REPRODUCIENDO");
        }
        }catch(Exception e){
        e.printStackTrace();}
    }
    void ejercicio(Game g, int ejercicio) throws IOException {
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
                       
            for(int b=0;b<numEjercicios;b++){
              a= (int)Math.floor(Math.random()*6)+1;
              imagesPerQ.add(a);}
        
        } else if (numEjercicios>10){
            for(int b=0;b<5;b++){
                a= (int)Math.floor(Math.random()*5)+1;
                imagesPerQ.add(a);}
            
            for(int b=0;b<numEjercicios-6;b++){
                a= (int)Math.floor(Math.random()*8);
                imagesPerQ.add(a);}
            
 
        }
        return imagesPerQ;
    }
    
    void imagesLocation(ArrayList <String> imagenes){
        int n=imagenes.size();
            for(int r=0;r<=(n-1);r++){
                String imagenElegida= imagenes.get(r);
                ImageView iv=getIView(r);
                App.setImage(imagenElegida,App.pathImgGame,iv);
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