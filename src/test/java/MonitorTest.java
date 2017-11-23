
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
        /*  Verifica que, habiendose llevado el mutex un hilo, no  es posible que un hilo obtenga el mutex
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
    public void despertar(){

    }

    @Test
    public void dormir(){

    }
    @Test
    public void prioridadEncolado(){
        

    }
    @Test
    public void autorizado(){
        /*Verifica que el próximo en intentar disparar sea el que obtuvo el mutex o el recien despertado.
        No importa si pudo disparar o no
        */
        /*En sí, tambien verifico tambien que tenga prioridad sobre el mutex el recién despertado
        *
        * */
        String cadena="";
        String [] casteado={"",""};
        String [] disparado={"",""};
        String autorizado="";

        //String

        for (int i = 0; i < Lineas.size(); i++) {

            /*
            if(Lineas.get(i).contains("Hilo despertado")){
                System.out.println(Lineas.get(i));
                autorizado=Lineas.get(i).split("=");
                System.out.println(autorizado[1]);
            }
            */
            if(Lineas.get(i).contains("Contador de disparos :")){
                cadena=Lineas.get(i);
            }

            if(Lineas.get(i).contains("obtiene el mutex.")){
                casteado= Lineas.get(i).split("obtiene el mutex.");
                autorizado=casteado[0].trim();
            }
            if(Lineas.get(i).contains("Hilo despertado")){
                casteado= Lineas.get(i).split("=");
                autorizado=casteado[1].trim();
            }

            if(Lineas.get(i).contains("no ha podido disparar la transicion")||Lineas.get(i).contains("ha disparado la transicion")){
                if(Lineas.get(i).contains("no ha podido disparar la transicion")){
                    disparado= Lineas.get(i).split("no ha podido disparar la transicion");
                    disparado[0]=disparado[0].trim();
                }
                else{
                    disparado= Lineas.get(i).split("ha disparado la transicion");
                    disparado[0]=disparado[0].trim();
                }
                assertTrue(cadena+"\n"+Lineas.get(i),autorizado.equals(disparado[0]));
            }



        }

    }



}