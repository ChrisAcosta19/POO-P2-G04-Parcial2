/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import espol.edu.ec.borradorproyectofx.App;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author chris
 */
public class Atencion implements Serializable {

    private Cita cita;
    private int duracion;
    private Empleado empleadoAtencion;

    //Constructor de la clase
    public Atencion(Cita cita, int duracion, Empleado empleadoAtencion) {
        this.cita = cita;
        this.duracion = duracion;
        this.empleadoAtencion = empleadoAtencion;
    }
    
    //Getters
    public Cita getCita() {
        return cita;
    }

    public int getDuracion() {
        return duracion;
    }

    public Empleado getEmpleadoAtencion() {
        return empleadoAtencion;
    }
    
    //metodo toString para mostrar informacion
    @Override
    public String toString(){
        return "Cita registrada: "+cita.toString()+"\nduración: "+duracion+" minutos"
                +", Empleado que atendió: "+empleadoAtencion.getNombre();
    }
    
    public static ArrayList<Atencion> cargarAtenciones(String ruta) {
        ArrayList<Atencion> atenciones = new ArrayList<>();
        System.out.println("xxxxxxxxxxxxx");
        //leer la lista de personas del archivo serializado
        try ( ObjectInputStream oi = new ObjectInputStream(new FileInputStream(ruta))) {
            atenciones = (ArrayList<Atencion>) oi.readObject();
            System.out.println("=============");
            // System.out.println(empleados);
        } catch (FileNotFoundException ex) {
            System.out.println("archivo no existe");
        } catch (IOException ex) {
            System.out.println("error io:" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("error class:" + ex.getMessage());
        }
        return atenciones;
    }
    
    public static void crearArchivo(String ruta){
        ArrayList<Atencion> atenciones = new ArrayList<>();
        ArrayList<Cita> citas = Cita.cargarCitas(App.pathCitas);
        ArrayList<Empleado> empleados = Empleado.cargarEmpleados(App.pathEmpleados);
        atenciones.add(new Atencion(citas.get(0),34,empleados.get(0)));
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ruta))) {
            out.writeObject(atenciones);
            out.flush();
        }catch (Exception e){}
    }
}