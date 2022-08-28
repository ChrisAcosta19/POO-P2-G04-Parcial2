package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import proyecto.App;

/**
 *
 * @author Christopher Acosta
 */

//Esta clase se encarga de validar numeros, nombres, fechas y horas
public class Validacion {
    //variable estática para almcenar mensajes en caso de que algún dato no sea válido
    public static String mensaje = "";
    
    /**
     * metodo que recibe un String y trata de convertirlo a entero
     * se opto por Long porque tanto cedula como telefono tienen 10 digitos
     * y Long acepta valores de esa longitud
     * @param atributo para ver que entero se está validando
     * @param entrada el entero a validar en formato String
     * @return true si es válido el entero
     */
    public static boolean validarEntero(String atributo,String entrada){
        try{
            if(atributo.contains("Telefono")||atributo.contains("Cedula")){
                if(entrada.length() != 10) {
                    mensaje += atributo + ": debe digitar 10 dígitos\n";
                    return false;
                }
            }
            Long.parseLong(entrada);
            return true;
        } catch (Exception e) {
            mensaje += atributo + ": Por favor ingrese un entero\n";
            return false;
        }
    }
    
    /**
     * metodo que recibe un String y trata de convertirlo a double
     * @param atributo para ver que double se está validando
     * @param entrada el double a validar
     * @return true si es válido el double
     */
    public static boolean validarDouble(String atributo, String entrada){
        try{
            Double.parseDouble(entrada);
            return true;
        } catch (Exception e) {
            mensaje += atributo + ": Por favor ingrese un numero con decimales usando el punto\n";
            return false;
        }
    }
    
    /**
     * método para tratar de convertir un String a una fecha
     * La sentencia LocalDate.parse(fecha) se encarga de hacer todas las revisiones necesarias
     * para que se pueda crear una fecha a partir del String recibido
     * El formato de la fecha debe ser AAAA-MM-DD para pueda ser válida
     * @param fecha fecha a validar en formato String
     * @return true si la fecha es válida
     */
    public static boolean validarFecha(String fecha){
        try{
            LocalDate.parse(fecha);
            return true;
	} catch (Exception e) {
            mensaje += "Fecha no válida\n";
            return false;
        }
    }
    
    /**
     * metodo que recibe un String y trata de convertirlo a una hora
     * La sentencia LocalTime.parse(hora) se encarga de hacer todas las revisiones necesarias
     * para que se pueda crear una hora a partir del String recibido
     * El formato de la hora debe ser hh:mm:ss o por lo menos hh:mm para que pueda ser válida
     * @param hora recibe la hora a validar en formato String
     * @return true ai la hora recibida es válida
     */
    public static boolean validarHora(String hora){
        try{
            LocalTime.parse(hora);
            return true;
	} catch (Exception e) {
            mensaje+="Hora no válida\n";
            return false;
        }
    }
    
    /**
     * metodo que recibe un String y trata de ver si califica para un nombre
     * @param objeto el nombre de la clase del objeto cuyo nombre se va a validar
     * @param nombre el nombre que se va a validar
     * @return true si el nombre solo contiene letras o letrasEpeciales
     */
    public static boolean validarNombre(String objeto, String nombre){
        char[] letrasEspeciales = {'á','é','í','ó','ú','Á','É','Í','Ó','Ú','ñ','Ñ','Ü','ü'};
        ArrayList<Character> listaLetras = new ArrayList<>();
        for (char letra:letrasEspeciales)
            listaLetras.add(letra);
        if(nombre.isEmpty()){
            mensaje+="Nombre de "+objeto+" no válido\n";
            return false;
        }
        
        for(int i=0;i<nombre.length();i++){
            char letra = nombre.charAt(i);
            int valorASCII = (int) letra;
            boolean esMayuscula = valorASCII >= 65 && valorASCII <= 90;
            boolean esMinuscula = valorASCII >= 97 && valorASCII <= 122;
            boolean esLetraEspecial = listaLetras.contains(letra);
            if(!esMayuscula && !esMinuscula && !esLetraEspecial && valorASCII != 32) { //el 32 es el espacio en blanco
                mensaje+="Nombre de "+objeto+" no válido\n";
                return false;
            }
        }
        return true;
    }
    
    /**
     * metodo que recibe un String y trata de ver si califica para un email
     * @param objeto tipo de persona cuyo correo se va a validar
     * @param email email que se va a validar
     * @return true si el email contiene al menos el @ entre sus caracteres
     */
    public static boolean validarEmail(String objeto, String email){
        if(email.contains("@"))
            return true;
        else {
            mensaje+="Correo de "+objeto+" no válido, falta el @\n";
            return false;
        }
    }
    
    /**
     * metodo que revisa si es posible crear una persona en el programa
     * @param tipo tipo de persona a validar
     * @param persona persona que se va a validar
     * @param per persona que se quita de la lista "personas" en caso de que se esté editando los
     * datos de esa persona para que no se detecte a sí misma
     * @return true si es posible crear dicha persona, caso contrario muestra un mensaje con la
     * persona en la que encontró alguna coincidencia con su cédula, teléfono o email
     */
    public static boolean validarPersona(String tipo, Persona persona, Persona per){
        ArrayList<Persona> personas = new ArrayList<>();
        ArrayList<Empleado> empleados = Empleado.cargarEmpleados(App.pathEmpleados);
        ArrayList<Cliente> clientes = Cliente.cargarClientes(App.pathClientes);
        for(Empleado e: empleados){
            personas.add(e);
        }
        for(Cliente c: clientes){
            personas.add(c);
            personas.add(c.getDatosRepresentante());
        }
        
        if(per != null){
            personas.remove(per);
        }
        
        if(personas.contains(persona)){
            int indice = personas.indexOf(persona);
            Persona p = personas.get(indice);
            mensaje += "Alguno de los datos del "+tipo+" coincide con los de esta persona:\n" + p + "\n";
            return false;
        }
        else {
            return true;
        }
    }
}