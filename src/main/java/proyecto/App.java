package proyecto;

import modelo.Empleado;
import modelo.Servicio;
import modelo.Game;
import modelo.Atencion;
import modelo.Cliente;
import modelo.Cita;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    public static String pathServicios = "archivos/Servicio.bin";
    public static String pathEmpleados = "archivos/Empleado.bin";
    public static String pathClientes = "archivos/Clientes.bin";
    public static String pathCitas = "archivos/Citas.bin";
    public static String pathAtenciones = "archivos/Atenciones.bin";
    
    public static String pathImg = "src/main/resources/media/";
    public static String pathImgGame = "src/main/resources/game/";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    /*
    Método que asigna una imagen al ImageView ingresado como parámetro
    @param name Nombre del archivo png
    @param path Ruta donde se encuentra el archivo
    @param iView ImageView al cual se asignará la imagen seleccionada
    */
    public static void setImage(String name,String path,ImageView iView){
        InputStream input = null;
        Image image = null;
        try {
            input = new FileInputStream(path + name + ".png");
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
    
    /*
    Método que asigna un gif al ImageView ingresado como parámetro
    @param name Nombre del archivo gif
    @param iView ImageView al cual se asignará la imagen seleccionada
    */
    public static void setGif(String name,ImageView iView){
        InputStream input = null;
        Image image = null;
        try {
            input = new FileInputStream(App.pathImgGame + name + ".gif");
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
     
    public static void main(String[] args) {   
        try(BufferedReader br = new BufferedReader(new FileReader("archivos/Iniciar.txt"))){
            String linea = br.readLine();
            if(linea.equalsIgnoreCase("false")){
                Servicio.crearArchivo(pathServicios);
                Empleado.crearArchivo(pathEmpleados, pathServicios);
                Cliente.crearArchivo(pathClientes);
                Cita.crearArchivo(pathCitas);
                Atencion.crearArchivo(pathAtenciones);
                Game.crearArchivo();
                try(BufferedWriter bw = new BufferedWriter(new FileWriter("archivos/Iniciar.txt"))){
                    bw.write("true");
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        ActividadesController.replayGame=null;
        launch();
    }

    //metodo para cambiar el contenido de la escena
    static void changeRoot(Parent rootNode) {
        scene.setRoot(rootNode);
    }
}