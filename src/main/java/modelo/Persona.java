package modelo;

import java.io.Serializable;

/**
 *
 * @author chris
 */
public class Persona implements Serializable {
    //Atributos de la clase Persona
    private String cedula;
    private String nombre;
    private String telefono;
    private String email;
    
    //Constructores de Persona
    public Persona(String cedula, String nombre, String telefono, String email) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }
    
    public Persona(String cedula){
        this.cedula = cedula;
        this.nombre = "N/A";
        this.telefono = "N/A";
        this.email = "N/A";
    }
    
    //Método toString para mostrar información
    public String toString() {
        return "Cedula: "+cedula+", Nombre: "+nombre+", Telefono: "+telefono+", Email: "+email;
    }
    
    //Getters & Setters
    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    //metodo equals para comparar si dos personas son iguales
    /*retornara verdadero si coinciden en al menos uno de los
    siguientes atributos: cedula, telefono, email*/
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj != null && obj instanceof Persona) {
            Persona p = (Persona) obj;
            boolean mismaCedula = cedula.equals(p.cedula);
            boolean mismoTelefono = telefono.equals(p.telefono);
            boolean mismoEmail = email.equals(p.email);
            return mismaCedula || mismoTelefono || mismoEmail;
        }
        return false;
    }
}
