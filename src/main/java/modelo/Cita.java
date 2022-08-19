package modelo;

import modelo.Cliente;
import modelo.Empleado;
import modelo.Servicio;

/**
 * 
 * @author Juan Pablo Plúas
 */

public class Cita {
    //Atributos de clase Cita
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
}