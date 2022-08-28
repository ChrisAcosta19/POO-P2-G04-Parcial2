/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.modelo;

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

    //Constructor de la clase
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
    
    //metodo toString para mostrar informacion
    @Override
    public String toString(){
        return "Nombre: "+nombre+", Duración: "+duracion+" minutos, Precio: $"+precio+", Estado: "+(estado?"Activo":"Inactivo");       
    }
    
    //método que compara si dos servicios son iguales
    //Devuelve true si coinciden en todos los atributos
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