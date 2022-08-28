package modelo;

import modelo.Servicio;
import modelo.Persona;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author chris
 */
public class Empleado extends Persona {
    //Atributos de la clase
    private boolean estado; // true = Activo, false = Inactivo
    private ArrayList<Servicio> listaServicios;
      
    //Constructores de la clase
    public Empleado(String cedula, String nombre, String telefono, String email, boolean estado) {
        super(cedula, nombre, telefono, email);
        this.estado = estado;
        this.listaServicios = new ArrayList<>();
    }
    
    public Empleado(String cedula) {
        super(cedula);
        this.listaServicios = new ArrayList<>();
    }

    //metodo para cambiar los datos del empleado
    public void editarEmpleado(Empleado e){
        this.setCedula(e.getCedula());
        this.setNombre(e.getNombre());
        this.setTelefono(e.getTelefono());
        this.setEmail(e.getEmail());
        this.estado = e.estado;
    }
    
    //metodo que cambiar estado del empleado a Inactivo
    public void eliminarEmpleado() {
        this.estado = false;
    }
    
    //metodo toString para mostrar informacion
    @Override
    public String toString(){
        return super.toString()+", Estado: "+(estado?"Activo":"Inactivo");
    }
    
    //Getters
    public ArrayList<Servicio> getListaServicios() {
        return listaServicios;
    }
    
    public void setListaServicios(ArrayList<Servicio> servicios) {
        this.listaServicios = servicios;
    }

    public String getEstado() {
        return estado?"Activo":"Inactivo";
    }
    
    public static ArrayList<Empleado> cargarEmpleados(String ruta){
        ArrayList<Empleado> empleados = new ArrayList<>();
       //leer la lista de empleados del archivo serializado
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(ruta))) {
            empleados = (ArrayList<Empleado>) oi.readObject();
        } catch (FileNotFoundException ex) {
            System.out.println("archivo no existe");
        } catch (IOException   ex) {
            System.out.println("error io:"+ex.getMessage());
        } catch (ClassNotFoundException  ex) {
            System.out.println("error class:"+ex.getMessage());
        } 
        return empleados;
    }
    
    public static void crearArchivo(String rutaEmp, String rutaSer){
        ArrayList<Servicio> servicios = Servicio.cargarServicios(rutaSer);
        ArrayList<Empleado> empleados = new ArrayList<>();
        empleados.add(new Empleado("0914345665","Juan","0923573567","juan@gmail.com", true));
        empleados.get(0).getListaServicios().add(servicios.get(0));
        empleados.get(0).getListaServicios().add(servicios.get(1));
        empleados.get(0).getListaServicios().add(servicios.get(2));
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(rutaEmp))) {
            out.writeObject(empleados);
            out.flush();
        }catch (Exception e){}
    }
}
