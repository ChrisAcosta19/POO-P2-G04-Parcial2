package modelo;

import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author Christopher Acosta
 */
public class Servicio implements Serializable {
    //Atributos de la clase
    private String nombre;
    private int duracion;
    private double precio;
    private boolean estado; // false "Inactivo" true "Activo"

    /**
     * Constructor de la clase
     * @param nombre nombre del servicio
     * @param duracion duracion en minutos del servicio
     * @param precio precio del servicio
     * @param estado estado del servicio
     */
    public Servicio(String nombre, int duracion, double precio, boolean estado) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.precio = precio;
        this.estado = estado;
    }
       
    //metodo para cambiar los datos de un servicio
    public void editarServicio(Servicio s){
        this.nombre = s.nombre;
        this.duracion = s.duracion;
        this.precio = s.precio;
        this.estado = s.estado;
    }
    
    //metodo para cambiar estado de servicio a Inactivo
    public void eliminarServicio(){
        this.estado = false;
    }   
    
    //Getters
    public String getNombre() {
        return nombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public double getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado?"Activo":"Inactivo";
    }
    
    /**
     * metodo para mostrar información del objeto
     * @return String con datos del servicio
     */
    @Override
    public String toString(){
        return "Nombre: "+nombre+", Duración: "+duracion+" minutos, Precio: $"+precio+", Estado: "+(estado?"Activo":"Inactivo");       
    }
    
    /**
     * método que compara si dos servicios son iguales
     * @param obj recibe el objeto a comparar
     * @return true si coinciden en todos los atributos
     */
    @Override
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        } else if(obj != null && obj instanceof Servicio){
            Servicio servicio = (Servicio) obj;
            boolean mismoNombre = nombre.equals(servicio.nombre);
            boolean mismaDuracion = duracion == servicio.duracion;
            boolean mismoPrecio = precio == servicio.precio;
            boolean mismoEstado = estado == servicio.estado;
            return mismoNombre && mismaDuracion && mismoPrecio && mismoEstado;
        }
        return false;
    }
    
    /**
     * método para cargar un ArrayList de servicios de un archivo binario
     * @param ruta recibe ruta del archivo binario
     * @return el ArrayList de servicios del archivo binario
     */
    public static ArrayList<Servicio> cargarServicios(String ruta){
        ArrayList<Servicio> servicios = new ArrayList<>();
       //leer la lista de servicios del archivo serializado
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(ruta))) {
            servicios = (ArrayList<Servicio>) oi.readObject();
        } catch (FileNotFoundException ex) {
            System.out.println("archivo no existe");
        } catch (IOException   ex) {
            System.out.println("error io:"+ex.getMessage());
        } catch (ClassNotFoundException  ex) {
            System.out.println("error class:"+ex.getMessage());
        } 
        return servicios;
    }
    
    /**
     * metodo para crear archivo binario con ArrayList de servicios
     * @param ruta recibe ruta del archivo binario a crear
     */
    public static void crearArchivo(String ruta){
        ArrayList<Servicio> servicios = new ArrayList<>();
        servicios.add(new Servicio("Terapia de Lenguaje",15,22.50, true));
        servicios.add(new Servicio("Terapia Psicopedagógica",35,37.50, true));
        servicios.add(new Servicio("Terapia Relajante",20,15.15, true));
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ruta))) {
            out.writeObject(servicios);
            out.flush();
        }catch (Exception e){}
    }
}