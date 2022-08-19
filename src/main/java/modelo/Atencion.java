/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import modelo.Cita;
import modelo.Empleado;

/**
 *
 * @author Christopher Acosta
 */
public class Atencion {
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
}