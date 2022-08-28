package modelo;

import espol.edu.ec.borradorproyectofx.ActividadesController;
import espol.edu.ec.borradorproyectofx.App;
import espol.edu.ec.borradorproyectofx.ClientesController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Juan Pablo Plúas
 */
public class Game implements Serializable, Cloneable{
        
    private static final long serialVersionUID = 1;
    int numEjercicios;
    ArrayList<Ejercicio> ejercicios;
    String cliente;
    String fecha;
    int fallos;
    String actividad;
    String tiempoEnFormato;
    /*
    Constructor inicial de la clase Game
    @param numEjercicios Número de ejercicios que tiene el juego
    @param ejercicios Lista de ejercicios
    */
    public Game(int numEjercicios, ArrayList<Ejercicio> ejercicios) {
        this.numEjercicios = numEjercicios;
        this.ejercicios = ejercicios;
    }
    
    /*
    Constructor de la clase Game para guardar resultados
    @param actividad Nombre de la actividad
    @param cliente Cédula de cliente
    @param fecha Fecha de realización de juego
    @param numEjercicios Número de ejercicios que tiene el juego
    @param fallos Número de fallos totales del juego
    @param tiempoEnFormato String que posee el tiempo que se tomó en completar la sesión en formato de minutos y segundos
    */
    public Game(String actividad,String cliente, String fecha,int numEjercicios, int fallos, String tiempoEnFormato) {
        this.numEjercicios = numEjercicios;
        this.cliente = cliente;
        this.fecha = fecha;
        this.fallos=fallos;
        this.tiempoEnFormato=tiempoEnFormato;
        this.actividad=actividad;
    }

    @Override
    public String toString() {
        return "Game {" + "numEjercicios=" + numEjercicios + ", cliente=" + cliente + ", fecha=" + fecha + ", fallos=" + fallos + ", tiempo=" + tiempoEnFormato + ", actividad=" + actividad + '}';
    }
    /*
    Método que devuelve un String con el tiempo ingresado en segundos, en formato de minutos y segundos según corresponda
    @return Devuelve una String
    */
    public static String timeFormat(int tiempo){
        if(tiempo<60){ // De ser menor a un minuto, devuelve con a etiqueta segundos
            if (tiempo<10){
                return "0"+tiempo+" seg";
            }
            return tiempo+" seg";
        } else{ // De ser mayor al minuto, devuelve con a etiqueta minutos y segundos
            int min=0; int sec=0;
            min=tiempo/60;
            sec=tiempo%60;
            if (sec<10){
                return min+" min 0"+sec+" seg";
            }
            return min+" min "+sec+" seg";
        }  
    }
    /*
    Método que carga la lista de objetos Game rejugables
    @param cedulaCliente Cédula del cliente del cual se va a cargar la lista
    @return Devuelve una lista de objetos Game rejugables
    */
    public static ArrayList<Game> cargarActividades(String cedulaCliente) {
        ArrayList<Game> games = new ArrayList<>();
        System.out.println("xxxxxxxxxxxxx");
        try ( ObjectInputStream oi = new ObjectInputStream(new FileInputStream("archivos/registro/"+cedulaCliente+"/Games.bin"))) {
            games = (ArrayList<Game>) oi.readObject();
            System.out.println("=============");
        } catch (FileNotFoundException ex) {
            System.out.println("archivo no existe");
        } catch (IOException ex) {
            System.out.println("error io:" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("error class:" + ex.getMessage());
        }
        return games;
    }
    
    /*
    Método que carga la lista de objetos Game con los resultados
    @param cedulaCliente Cédula del cliente del cual se va a cargar la lista
    @return Devuelve una lista de objetos Game con resultados
    */
    public static ArrayList<Game> cargarResultados(String cedulaCliente) {
        ArrayList<Game> games = new ArrayList<>();
        System.out.println("xxxxxxxxxxxxx");
        try ( ObjectInputStream oi = new ObjectInputStream(new FileInputStream("archivos/registro/"+cedulaCliente+"/GamesResults.bin"))) {
            games = (ArrayList<Game>) oi.readObject();
            System.out.println("=============");
        } catch (FileNotFoundException ex) {
            // Cambia el estado de la variable para que sea no redirija a la ventana de actividades, y solo s emuestre una alerta
            ClientesController.juegosExists=false;
        } catch (IOException ex) {
            System.out.println("error io:" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("error class:" + ex.getMessage());
        }
        return games;
    }
    
    /*
    Método que carga una sesión de juego relacionada a la atención que también se carga por defecto
    */
    public static void crearArchivo(){
        File directorioPrincipal = new File("archivos/registro");
        File directorioCliente = new File("archivos/registro/0832834824");
        directorioPrincipal.mkdir();
        directorioCliente.mkdir();
        
        ArrayList<Game> actividades = new ArrayList<>();
        ArrayList<String> images=new ArrayList<>();
        images.add("cow");
        ArrayList<Ejercicio> ejercicios= new ArrayList<>();
        ejercicios.add(new Ejercicio(1,images,0,false));
        actividades.add(new Game(1,ejercicios));
        
        ArrayList<Game> resultados = new ArrayList<>();
        resultados.add(new Game("¿Cuántos hay?","0832834824","2022-02-01",1,0,"05 seg"));
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/registro/0832834824/Games.bin"))) {
            out.writeObject(actividades);
            out.flush();
        }catch (Exception e){}
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("archivos/registro/0832834824/GamesResults.bin"))) {
            out.writeObject(resultados);
            out.flush();
        }catch (Exception e){}
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter("archivos/registro/0832834824/GamesDetalles.txt", true))){
            String registro = "Fecha: 2022-02-01" + ",Número de ejercicios: 1" + ",Número de fallos: 0" + ",Tiempo total: 05 seg" + ";Número de imágenes: 1" + ",Número de fallos: 0" + ",Tiempo: 05 seg" + "\n";
            writer.write(registro);
            writer.close();
        }catch (Exception e){}
    }
    
    
    public Object clone() throws CloneNotSupportedException {
        return (Game) super.clone();
    }
    
    public int getNumEjercicios() {
        return numEjercicios;
    }

    public ArrayList<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public String getCliente() {
        return cliente;
    }

    public String getFecha() {
        return fecha;
    }
    
    public int getFallos() {
        return fallos;
    }

    public String getTiempoEnFormato() {
        return tiempoEnFormato;
    }

    public void setTiempoEnFormato(int tiempo) {
        this.tiempoEnFormato = timeFormat(tiempo);
    }

    public String getActividad() {
        return actividad;
    }

}