package modelo;
import java.io.*;
import java.util.*;

/**
 *
 * @author Christopher Acosta
 */
public class Cliente extends Persona {
    //Atributos de la clase
    private Persona datosRepresentante;
    
    //Constructores de la clase
    public Cliente(String cedula, String nombre, String telefono, String email, Persona datosRepresentante) {
        super(cedula, nombre, telefono, email);
        this.datosRepresentante = datosRepresentante;
    }
    
    public Cliente(String cedula){
        super(cedula);
        this.datosRepresentante = new Persona("N/A");
    }

    //método para cambiar los datos del cliente
    public void editarCliente(Cliente c){
        this.setNombre(c.getNombre());
        this.setTelefono(c.getTelefono());
        this.setEmail(c.getEmail());
        this.datosRepresentante = c.datosRepresentante;
    }
    
    //metodo toString para mostrar informacion
    @Override
    public String toString() {
        return super.toString();
    }
    
    //Getters
    public Persona getDatosRepresentante() {
        return datosRepresentante;
    }
    
    /**
     * @param ruta recibe la ruta de un archivo binario
     * @return clientes con Arraylist de clientes del archivo binario
     */
    public static ArrayList<Cliente> cargarClientes(String ruta) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        //leer la lista de clientes del archivo serializado
        try ( ObjectInputStream oi = new ObjectInputStream(new FileInputStream(ruta))) {
            clientes = (ArrayList<Cliente>) oi.readObject();
        } catch (FileNotFoundException ex) {
            System.out.println("archivo no existe");
        } catch (IOException ex) {
            System.out.println("error io:" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("error class:" + ex.getMessage());
        }
        return clientes;
    }
    
    /**
     * @param ruta recibe una ruta de un archivo binario
     * crea el archivo binario con un ArrayList de clientes en la ruta recibida
     */
    public static void crearArchivo(String ruta){
        ArrayList<Cliente> clientes = new ArrayList<>();
        Persona representante1 = new Persona("0453462369","María","0987445643","maria@gmail.com");
        clientes.add(new Cliente("0832834824","Mario","0992837659","mario@gmail.com",representante1));
        Persona representante2 = new Persona("0123456758","Carlos","2346263234","carlos@hotmail.com");
        clientes.add(new Cliente("0729586956","Melanie","0939872750","melanie@hotmail.com",representante2));
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ruta))) {
            out.writeObject(clientes);
            out.flush();
        }catch (Exception e){}
    }
}