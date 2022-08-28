package modelo;

import java.io.*;
import java.util.ArrayList;
import proyecto.App;

/**
 *
 * @author Christopher Acosta
 */
public class Atencion implements Serializable {
    //Atributos de la clase
    private Cita cita;
    private int duracion;
    private Empleado empleadoAtencion;
    private Cliente clienteCita;
    private String fechaCita;
    private String horaCita;

    /**
     * Constructor de la clase
     * @param cita cita de la atención
     * @param duracion duración real en minutos de la atención
     * @param empleadoAtencion terapista que atendió la cita
     */
    public Atencion(Cita cita, int duracion, Empleado empleadoAtencion) {
        this.cita = cita;
        this.duracion = duracion;
        this.empleadoAtencion = empleadoAtencion;
        this.clienteCita = cita.getCliente();
        this.fechaCita = cita.getFecha();
        this.horaCita = cita.getHora();
    }
    
    //Getters
    public Cliente getClienteCita() {
        return clienteCita;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }
    
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
    
    /**
     * método para cargar un ArrayList de atenciones de un archivo binario
     * @param ruta recibe ruta del archivo binario
     * @return atenciones con el ArrayList de atenciones del archivo binario
     */
    public static ArrayList<Atencion> cargarAtenciones(String ruta) {
        ArrayList<Atencion> atenciones = new ArrayList<>();
        //leer la lista de atenciones del archivo serializado
        try ( ObjectInputStream oi = new ObjectInputStream(new FileInputStream(ruta))) {
            atenciones = (ArrayList<Atencion>) oi.readObject();
        } catch (FileNotFoundException ex) {
            System.out.println("archivo no existe");
        } catch (IOException ex) {
            System.out.println("error io:" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("error class:" + ex.getMessage());
        }
        return atenciones;
    }
    
    /**
     * método para crear el archivo binario con un ArrayList de atenciones
     * @param ruta recibe ruta del archivo binario a crear
     */
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