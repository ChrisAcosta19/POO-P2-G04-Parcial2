/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.modelo;
import java.util.*;
import java.io.*;
import proyecto.App;

/**
 *
 * @author chris
 */
public class Cita implements Serializable {

    private String fecha;
    private String hora;
    private Cliente cliente;
    private Servicio servicio;
    private Empleado encargadoServicio;
    
    //Constructor de la clase
    public Cita(String fecha, String hora, Cliente cliente, Servicio servicio, Empleado encargadoServicio){
        this.fecha = fecha;
        this.hora = hora;
        this.cliente = cliente;
        this.servicio = servicio;
        this.encargadoServicio = encargadoServicio;
    }
    
    //Getters
    public String getFecha() {
        return fecha;
    }
    
    public String getHora(){
        return hora;
    }
    
    public Cliente getCliente(){
        return cliente;
    }
    
    public Servicio getServicio(){
        return servicio;
    }
    
    public Empleado getEncargadoServicio(){
        return encargadoServicio;
    }
    
    //metodo toString para mostrar informacion del objeto
    @Override
    public String toString(){
        return "Fecha: " +fecha+ ", Hora: " +hora+ ", Cliente: " +cliente.getNombre()
                +", Encargado de la cita: "+encargadoServicio.getNombre();
    }
    
    
    /*metodo equals para comparar si dos citas son iguales
      primero evalua si coinciden en fecha, hora y encargado
      si lo anterior es falso, entonces evalua si coinciden en fecha, hora y cliente
    */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        } else if(obj != null && obj instanceof Cita) {
            Cita cita = (Cita) obj;
            boolean mismaFecha = fecha.equals(cita.fecha);
            boolean mismaHora = hora.equals(cita.hora);
            boolean mismoCliente = cliente.equals(cita.cliente);
            boolean mismoEncargado = encargadoServicio.equals(cita.encargadoServicio);
            if(mismaFecha && mismaHora && mismoEncargado){
                return true;
            }
            return mismaFecha && mismaHora && mismoCliente;
        }
        return false;
    }
    
    public static ArrayList<Cita> cargarCitas(String ruta) {
        ArrayList<Cita> citas = new ArrayList<>();
        //leer la lista de citas del archivo serializado
        try ( ObjectInputStream oi = new ObjectInputStream(new FileInputStream(ruta))) {
            citas = (ArrayList<Cita>) oi.readObject();
        } catch (FileNotFoundException ex) {
            System.out.println("archivo no existe");
        } catch (IOException ex) {
            System.out.println("error io:" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("error class:" + ex.getMessage());
        }
        return citas;
    }
    
    public static void crearArchivo(String ruta){
        ArrayList<Cita> citas = new ArrayList<>();
        ArrayList<Cliente> clientes = Cliente.cargarClientes(App.pathClientes);
        ArrayList<Servicio> servicios = Servicio.cargarServicios(App.pathServicios);
        ArrayList<Empleado> empleados = Empleado.cargarEmpleados(App.pathEmpleados);
        citas.add(new Cita("2022-02-01","11:30:00",clientes.get(0),servicios.get(0),empleados.get(0)));
        citas.add(new Cita("2022-09-21","08:00:00",clientes.get(1),servicios.get(1),empleados.get(0)));
        citas.add(new Cita("2022-11-11","10:30:00",clientes.get(0),servicios.get(2),empleados.get(0)));
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ruta))) {
            out.writeObject(citas);
            out.flush();
        }catch (Exception e){}
    }
}
