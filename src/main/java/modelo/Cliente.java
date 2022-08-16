/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.io.*;
import java.util.*;

/**
 *
 * @author chris
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
        return super.toString()+"\nDatos del representante:\n"+datosRepresentante+"\n";
    }
    
    //Getters
    public Persona getDatosRepresentante() {
        return datosRepresentante;
    }
    
    public static ArrayList<Cliente> cargarClientes(String ruta) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        System.out.println("xxxxxxxxxxxxx");
        //leer la lista de personas del archivo serializado
        try ( ObjectInputStream oi = new ObjectInputStream(new FileInputStream(ruta))) {
            clientes = (ArrayList<Cliente>) oi.readObject();
            System.out.println("=============");
            // System.out.println(empleados);
        } catch (FileNotFoundException ex) {
            System.out.println("archivo no existe");
        } catch (IOException ex) {
            System.out.println("error io:" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("error class:" + ex.getMessage());
        }
        return clientes;
    }
    
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