package espol.edu.ec.borradorproyectofx;

import modelo.Ejercicio;
import modelo.Game;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
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
import javafx.scene.image.ImageView;
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
    
    private String[] images={"cow","cowg","cowh","duck","horse","horsea","horseb","pig","pigb","rooster","roosterb","sheep"};
    private transient ImageView[] imagesLocation={img01,img02,img11,img12,img03,img13,img00,img10};
    private ArrayList <Integer> numImagenesXEjercicio=new ArrayList<>();
    private Boolean[] ToF={true, true, false, true, true, true, true};
    private int ejercicio=0;
    private ArrayList<Ejercicio> ejercicios=new ArrayList<>();
    private ArrayList<Game> actividades = new ArrayList<>();
    private ArrayList<Game> resultados = new ArrayList<>();
    
    public static int timePromedio;
    public static int timeTotal;
    public static int fallosTotal;
    private String infoPorPregunta="";
    private String fecha;
    private String cliente; 
    private Game g2save;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setImage("arrow_right",App.pathImgGame,btnAvanzar);
        App.setImage("arrow_left",App.pathImgGame,btnRetroceder);
        
        //Se crea el juego con parámetros que se van a ir completando con la ejecución de cada método
        ArrayList <Ejercicio> ejercicios= new ArrayList<>();
        Game g = new Game(GameMainController.numEjercicios,ejercicios);
        numImagenesXEjercicio=imagesPerQuestion(GameMainController.numEjercicios);
        
        for(int x:numImagenesXEjercicio){
            ArrayList <String> imagenes= new ArrayList<>();
            int j=(int) Math.floor(Math.random()*ToF.length);
            boolean bool=false;
            bool=ToF[j];
            imagesSelection(x,bool,imagenes);
            Ejercicio ejercicio=new Ejercicio(x,imagenes,0,false);
            g.getEjercicios().add(ejercicio);
        }        
        
        System.out.println(g);
        
        try {
            g2save=(Game)g.clone(); // Se clona el objeto Game para guardarlo y poder rejugarlo posteriormnete, por lo que no tiene fallos ni tiempo registrado
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        
        
        boolean guardar=false; // Variable que determina si se guardan o no los resultados de la sesión
        
        //Evalúa los casos posibles, si se está registrando una atención, o se estpa rejugando una sesión
        if (ActividadesController.replayGame==null){ // Este caso es cuando se esta registrando una atención
            fecha=CitasController.citaARegistrar.getFecha();// Asigna el valor de la fecha de la cita de la que se registra la atención
            cliente=CitasController.citaARegistrar.getCliente().getCedula();// Asigna el valor de la cédula del cliente de quien se registra la atención
            resultados=Game.cargarResultados(cliente); //Recupera la lista del archivo
            jugar(g,true); 
        } else {// Este caso es cuando se esta rejugando una sesión 
            Alert alert=new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Guardado de Resultados");
            alert.setHeaderText(null);
            alert.setContentText("¿Desea guardar los resultados de esta sesión de juego?");
            Optional<ButtonType> result= alert.showAndWait();
            if(result.get()==ButtonType.OK){
                guardar=true; // De ser true, se guardan los resultados y los detalles, mas no la actividad
            }
            fecha="n/a";
            cliente=ClientesController.clienteSeleccionado.getCedula(); //Asigna el valor de la cédula del cliente del que se consultan sus actividades
            resultados=Game.cargarResultados(cliente); //Recupera la lista del archivo
            jugar(ActividadesController.replayGame,guardar);
            
        } 
    }
    
    /* Método que determina los comportamientos de jugabilidad y guardado de resultados de la actividad
    @param g Objeto tipo Game que se está jugando
    @param guardarResultados Indica si se guardan los resultados de ls sesión de juego actual
    */
    void jugar(Game g, boolean guardarResultados){
        try {
            ejercicio(g,ejercicio);
        } catch (Exception ex) {}
        
        try{
            btnVerificarRespuesta.setOnMouseClicked(eh -> { // Expresión lambda que determina el comportamiento del botón btnVerificarRespuesta 
                int respuesta;
                try {
                    respuesta = Integer.valueOf(fieldRespuesta.getText());
                    if (respuesta == g.getEjercicios().get(ejercicio).getRespuesta()) {
                        App.setImage("happy", App.pathImgGame, respuestaVisual);
                        sonido(true);
                        if (!g.getEjercicios().get(ejercicio).isDone()) {
                            g.getEjercicios().get(ejercicio).done();
                        }
                    } else {
                        App.setGif("globoe", respuestaVisual);
                        sonido(false);
                        if (!g.getEjercicios().get(ejercicio).isDone()) {
                            g.getEjercicios().get(ejercicio).intentosAumentar();
                        }
                    }
                } catch (Exception ex) { // Si se ingresa un valor que no sea un número, se emite una alerta
                    fieldRespuesta.clear();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al ingresar número");
                    alert.setHeaderText(null);
                    alert.setContentText("Ingrese un número válido");
                    alert.showAndWait();
                }
            });
        }catch (Exception ex) {}
        
        btnAvanzar.setOnMouseClicked(eh -> { // Expresión lambda que determina el comportamiento del botón btnAvanzar
            if(!g.getEjercicios().get(ejercicio).isDone()){ // Si se intenta avanzar al siguiente ejercicio sin haber respondido correctamente el actual, se emite una alerta
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("D:");
                alert.setHeaderText(null);
                alert.setContentText("La pregunta aún no ha sido respondida correctamente");
                alert.showAndWait();
            }else{ // Cuando se responde correctamente se limpian los ImageView  
                respuestaVisual.imageProperty().set(null);
                fieldRespuesta.clear();
                ejercicio++;
                try {
                    ejercicio(g, ejercicio);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (IndexOutOfBoundsException ex) {
                    
                    //Verifica que los directorios existan donde se guardan los resultados y los juegos, y si no, los crea
                    if (ActividadesController.replayGame == null) {
                        File directorioPrincipal = new File("archivos/registro"); 
                        File directorioCliente = new File("archivos/registro/" + cliente);
                        if (!directorioPrincipal.exists()) {
                            directorioPrincipal.mkdir();
                        }

                        if (directorioCliente.exists()) {

                            File fileResultados = new File("archivos/registro/" + cliente + "/GamesResults.bin");
                            File fileActividades = new File("archivos/registro/" + cliente + "/Games.bin");

                            if (fileResultados.exists() && fileActividades.exists()) {
                                actividades = Game.cargarActividades(cliente); // Si existe ya el archivo, se recupera la lista de juegos de este, si no, se utiliza la lista vacía por defecto
                                
                            }
                            
                            // Se serializa el objeto tipo Game para poder ser rejugado
                            try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/registro/" + cliente + "/Games.bin"))) {
                                actividades.add(g2save);
                                out.writeObject(actividades);
                                out.flush();
                                System.out.println("GUARDADO DE JUEGO EXITOSO");
                            } catch (Exception e) {
                                System.out.println("No se pudo guardar la sesión");
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                directorioCliente.mkdir();
                                try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/registro/" + cliente + "/Games.bin"))) {
                                    actividades.add(g2save);
                                    out.writeObject(actividades);
                                    out.flush();
                                    System.out.println("GUARDADO DE JUEGO EXITOSO");
                                } catch (Exception e) {
                                    System.out.println("No se pudo guardar la sesión");
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    try {
                        // Se llena el String info con los datos de cada ejercicio del juego
                        for (Ejercicio e : g.getEjercicios()) {
                            String info = ";Número de imágenes: " + e.getRespuesta() + ",Número de fallos: " + e.getFallos() + ",Tiempo: " + Game.timeFormat(e.getTime());
                            infoPorPregunta += info;
                            fallosTotal += e.getFallos(); // Se actualizan los fallos del ejercicio
                            timePromedio += e.getTime(); // Se actualiza la variable que contiene el tiempo en que se completó el ejercicio
                        }
                        timeTotal = timePromedio; // Se asigna a la variable que contiene el tiempo total
                        timePromedio /= g.getNumEjercicios(); // Se promedia para el número de ejercicios para obtener el tiempo promedio por ejercicio y se actualiza la variable
                        String tiempo = Game.timeFormat(timeTotal); // Se obtiene el tiempo en formato de minutos y segundos en String
                        // Se crea el objeto tipo Game que tendrá los resultados de la sesión 
                        Game g2 = new Game("¿Cuántos hay?", cliente, fecha, g.getNumEjercicios(), fallosTotal, tiempo);
                        System.out.println(g2);
                        resultados.add(g2); // Se agrega a la lista de juegos
                        // Se verifica si se guardan o no los resultados de la sesión
                        if (guardarResultados) {
                            
                            //Se serializa la lista 
                            try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/registro/" + cliente + "/GamesResults.bin"))) {
                                out.writeObject(resultados);
                                out.flush();
                                System.out.println("GUARDADO DE JUEGO EXITOSO");
                            } catch (Exception e) {
                                System.out.println("NO SE PUDO GUARDAR LA SESIÓN");
                                e.printStackTrace();
                            }
                            
                            //Se guardan los detalles de cada ejercicio en el txt correspondiente
                            try ( BufferedWriter writer = new BufferedWriter(new FileWriter("archivos/registro/" + cliente + "/GamesDetalles.txt", true))) {
                                String registro = "Fecha: " + g2.getFecha() + ",Número de ejercicios: " + g2.getNumEjercicios() + ",Número de fallos: " + g2.getFallos() + ",Tiempo total: " + g2.getTiempoEnFormato() + infoPorPregunta + "\n";
                                writer.write(registro);
                                writer.close();
                                System.out.println("GUARDADO DE RESULTADOS EXITOSO");
                            } catch (Exception e) {
                                System.out.println("NO SE PUDO REGISTRAR LOS RESULTADOS DE LA SESION");
                                e.printStackTrace();
                            }
                            
                            App.setRoot("gameEnd");

                        } else {
                            App.setRoot("gameEnd");
                        }

                    } catch (Exception ex1) {
                    }
                }
            }
        });
        
        
        btnRetroceder.setOnMouseClicked(eh -> { // Expresión lambda que determina el comportamiento del botón btnRetroceder
            //Si se encuentra en el primer ejercicio, se regresa a la página de inicio pidiendo una confirmación para ello
            if (ejercicio == 0) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("¡OJO!");
                alert.setHeaderText(null);
                alert.setContentText("Si sales ahora, no se guardará el juego ni sus resultados, ¿desea salir?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    
                    if(ActividadesController.replayGame==null){
                    try {
                        App.setRoot("gameMain");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    // Sin embargo, si se está rejugando una sesión, el botón redirige a la ventana de actividades del cliente seleccionado
                    } else{
                    try {
                        App.setRoot("actividades");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }    
                    }
                }
            } else {
                // Se limpian los ImageView
                respuestaVisual.imageProperty().set(null);
                fieldRespuesta.clear();
                ejercicio--;
                try {
                    ejercicio(g, ejercicio);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });    
    } 
    
    /*Método que reproduce un sonido dependiendo de la respuesta obtenida en el TextField
    @ param respuesta Indica si la respuesta es correcta o incorrecta
    */
    void sonido(boolean respuesta) {
        Media media;
        MediaPlayer mp;
        File file;

        try {
            if (respuesta) {
                file = new File(App.pathImgGame + "sonidoBien.wav");
                media = new Media(file.toURI().toString());
                mp = new MediaPlayer(media);
                mp.play();
            } else {
                file = new File(App.pathImgGame + "sonidoMal.wav");
                media = new Media(file.toURI().toString());
                mp = new MediaPlayer(media);
                mp.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nError en lectura de archivo:" + e.getMessage());
        }
    }
    
    /* Método que ejecuta las acciones necesarias para jugar cada ejercicio, coloca las imágenes de cada ejercicio llamando al método imagesLocation y mide el tiempo utilizando un hilo
    @param g Objeto tipo Game que se está jugando
    @param ejercicio Índice del ejercicio en la lista de ejercicios del objeto Game 
    */
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
        
    /*Método que dependiendo de la respuesta obtenida del TextField, aumenta un intento fallido al ejercicio o caso contrario cambia es estadio del ejercicio como respondido correctamente 
    @param e Ejercicio el cual se está jugando
    @param correcta Indica si la respuesta recibida es correcta o no
    */
    void respuesta(Ejercicio e, boolean correcta) {
        if (correcta) {
            e.done();
        } else {
            e.intentosAumentar();
        }
    }
    
    /* Método que retorna el ImageView correspondiente a la posición que tendrá la imagen según su índice en la lista
    @param i índice de imagen en la lista (siendo 7 el máximo, porque 8 es la cantidad máxima de imágenes posibles en el ejercicio)
    @return Devuelve un ImageView
    */
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
    
    /*Método que determina la cantidad de imágenes en cada ejercicio aleatoriamente, según la cantidad de ejercicios totales de la sesion de juego
    @param numEjercicios Número de ejercicios en total de la sesión de juego
    @return Devuelve una lista de enteros
    */
    ArrayList <Integer> imagesPerQuestion(int numEjercicios){
        ArrayList<Integer> imagesPerQ = new ArrayList<>();
        int a;
        if (numEjercicios <= 5) {
            for (int b = 0; b < numEjercicios; b++) {
                a = (int) Math.floor(Math.random() * 4) + 1;
                imagesPerQ.add(a);
            }

        } else if (numEjercicios <= 10) {
            for (int b = 0; b < numEjercicios; b++) {
                a = (int) Math.floor(Math.random() * 6) + 1;
                imagesPerQ.add(a);
            }

        } else if (numEjercicios > 10) {
            for (int b = 0; b < 5; b++) {
                a = (int) Math.floor(Math.random() * 5) + 1;
                imagesPerQ.add(a);
            }

            for (int b = 0; b < numEjercicios - 6; b++) {
                a = (int) Math.floor(Math.random() * 8)+1;
                imagesPerQ.add(a);
            }
        }
        return imagesPerQ;
    }
    
    /*Método que recorre la lista de imágenes para colocarlas en el ImageView correspondiente
    @param imagenes Lista de imágenes del ejercicio
    */
    void imagesLocation(ArrayList <String> imagenes){
        int n = imagenes.size();
        for (int r = 0; r <= (n - 1); r++) {
            String imagenElegida = imagenes.get(r);
            ImageView iv = getIView(r); 
            App.setImage(imagenElegida, App.pathImgGame, iv);
        }
    }
    
    /*
    Método para seleccionar imágenes (nombre de archivos .png) aleatoriamente para cada ejercicio
    @param n Número de imágenes a seleccionar en cada ejercicio
    @param imgDif Determina si las imágenes del ejercicio son todas iguales o diferentes
    @param imagenesElegidas Lista a la cual se agregan las imágenes por cada ejercicio
    */
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