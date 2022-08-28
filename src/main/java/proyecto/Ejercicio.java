
package proyecto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Juan Pablo Plúas
 */

public class Ejercicio implements Serializable{
        private static final long serialVersionUID = 1;
        int respuesta;
        ArrayList<String> imagenes;
        int fallos;
        boolean done;
        int time=0;
        
        /**
        Constructor de la clase Ejercicio
        @param respuestas Número de imágenes que tiene el ejercicio, por ende, la respuesta del mismo
        @param imagenes Lista de imágenes del ejercicio
        @param fallos Número de fallos totales del ejercicio
        @param done Indica si el ejercicio ya ha sido respondido correctamente
        */
        public Ejercicio(int respuesta, ArrayList<String> imagenes, int fallos, boolean done) {
            this.respuesta = respuesta;
            this.imagenes = imagenes;
            this.fallos = fallos;
            this.done= done;
        }
        
        public void intentosAumentar(){
            fallos++;
        }
        
        public void done(){
            done=true;
        }
        
        public void time(){
            time++;
                }

        @Override
        public String toString() {
            return "Ejercicio{" + "respuesta=" + respuesta + ", fallos=" + fallos + ", time=" + time + "s}";
        }

    
    public int getRespuesta() {
        return respuesta;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public int getFallos() {
        return fallos;
    }

    public boolean isDone() {
        return done;
    }

    public int getTime() {
        return time;
    }

    
    
        
        
    }  
