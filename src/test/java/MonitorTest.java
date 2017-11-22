
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.Line;

import static org.junit.Assert.*;
/**
 * Created by YepezHinostroza on 22/11/2017.
 */
public class MonitorTest {
    List<String> Lineas;
    boolean bloqueado;


    @Before
    public void setUp() throws Exception {
        Log lector = new Log("C:\\Users\\alexa\\Desktop\\ConcurrenteNuevo\\registro.txt",
                "\"C:\\\\Users\\\\alexa\\\\Desktop\\\\ConcurrenteNuevo\\\\\\registroVacio.txt\"");
        // creo una lista de as lineas
        Lineas = lector.leerLineas();
        //System.out.println("Cantidad de Marcados a testear : " + Lineas.size());
    }


    @Test
    public void monitorBloqueado(){
        /*
        Verifica que, habiendose llevado el mutex un hilo, no  es posible que un hilo obtenga el mutex
        hasta que no lo haya devuelto el hilo mencionado primero.

         */
        String cadena="";

        for (int i = 0; i < Lineas.size(); i++) {
            if(Lineas.get(i).contains(">>>")){
                bloqueado=true;
            }
            if(Lineas.get(i).contains("<<<")){
                bloqueado=false;
            }
            if(Lineas.get(i).contains("Contador de disparos :")){
                cadena=Lineas.get(i);
            }
            assertFalse(cadena+ "\n"+Lineas.get(i)+"\n",Lineas.get(i).contains("obtiene el mutex.")&&bloqueado);


        }

    }

    @Test
    public void devolverMutex(){

    }
    @Test
    public void relegarMutex(){

    }

    @Test
    public void dormir(){

    }
    @Test
    public void prioridadEncolado(){

    }



}