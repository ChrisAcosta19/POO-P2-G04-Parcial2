
package modelo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    
    public Game(int numEjercicios, ArrayList<Ejercicio> ejercicios) {
        this.numEjercicios = numEjercicios;
        this.ejercicios = ejercicios;
    }
    
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
            return "Game {" + "numEjercicios=" + numEjercicios +", cliente=" + cliente + ", fecha=" + fecha + ", fallos=" + fallos + ", tiempo=" + tiempoEnFormato + ", actividad=" + actividad + '}';
        }
    
    public static String timeFormat(int tiempo){
        if(tiempo<60){
            if (tiempo<10){
                return "0"+tiempo+" seg";
            }
            return tiempo+" seg";
        } else{
            int min=0; int sec=0;
            min=tiempo/60;
            sec=tiempo%60;
            if (sec<10){
                return min+" min 0"+sec+" seg";
            }
            return min+" min "+sec+" seg";
        }  
    }
    
    public static ArrayList<Game> cargarActividades(String cedulaCliente) {
        ArrayList<Game> games = new ArrayList<>();
        System.out.println("xxxxxxxxxxxxx");
        //leer la lista de personas del archivo serializado
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
    
    public static ArrayList<Game> cargarResultados(String cedulaCliente) {
        ArrayList<Game> games = new ArrayList<>();
        System.out.println("xxxxxxxxxxxxx");
        //leer la lista de personas del archivo serializado
        try ( ObjectInputStream oi = new ObjectInputStream(new FileInputStream("archivos/registro/"+cedulaCliente+"/GamesResults.bin"))) {
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
    
    public Object clone() throws CloneNotSupportedException 
   {
      return (Game)super.clone();
   }
    
        public int getNumEjercicios() {
            return numEjercicios;
        }

        public void setNumEjercicios(int numEjercicios) {
            this.numEjercicios = numEjercicios;
        }

        public ArrayList<Ejercicio> getEjercicios() {
            return ejercicios;
        }

        public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
            this.ejercicios = ejercicios;
        }

        public String getCliente() {
            return cliente;
        }

        public void setCliente(String cliente) {
            this.cliente = cliente;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public int getFallos() {
            return fallos;
        }

        public void setFallos(int fallos) {
            this.fallos = fallos;
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

        public void setActividad(String actividad) {
            this.actividad = actividad;
        }
    
        
    }

 
       
