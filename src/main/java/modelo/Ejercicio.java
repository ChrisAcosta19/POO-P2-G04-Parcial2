
package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Ejercicio implements Serializable{
        private static final long serialVersionUID = 1;
        int respuesta;
        ArrayList<String> imagenes;
        int fallos;
        boolean done;
        int time=0;
        

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

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }

    public int getFallos() {
        return fallos;
    }

    public void setFallos(int fallos) {
        this.fallos = fallos;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
        
        
    }  
