
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
    public void encolados(){
        /*  Analiza sobre la linea Hilos encolados en contraposición de los intento fallidos de disparo e hilo despertado.
        *   Botonea en el caso que haya un encolado que falta  como también que sobre un encolado
        * */
        List<String> encolados= new ArrayList<String>();
        String[] casteado= new String[2];
        String cadena="";
        boolean encontrado=false;
        for (int i = 0; i < Lineas.size(); i++) {
            if(Lineas.get(i).contains("Contador de disparos :")){
                cadena=Lineas.get(i);
            }
            if(Lineas.get(i).contains("no ha podido disparar")){
                casteado=Lineas.get(i).split("no ha podido disparar");
                encolados.add(casteado[0].trim());
                //System.out.println(encolados);
            }
            if(Lineas.get(i).contains("Hilo despertado")){
                casteado=Lineas.get(i).split("=");
                encolados.remove(casteado[1].trim());
                //System.out.println(encolados);
            }
            if (Lineas.get(i).contains("Hilos Encolados")){
                casteado= Lineas.get(i).split("=");
                String [] hilos = casteado[1].split("\\|\\|");
                String [] hilos2 = new String[hilos.length-1];

                for (int j = 0; j <hilos.length-1; j++) {
                    hilos2[j]=hilos[j].trim();
                }
                for (String enco: encolados) {
                    encontrado=false;
                    for (int j = 0; j < hilos2.length; j++) {
                        if(enco.equals(hilos2[j])){
                            encontrado=true;
                            break;
                        }

                    }
                    assertTrue(cadena+"\n"+"Hilo no encontrado encolado = "+ enco,encontrado);

                }
                for (int j = 0; j < hilos2.length; j++) {
                    encontrado=false;
                    for (String enco: encolados) {
                        if(enco.equals(hilos2[j])){
                        encontrado=true;
                        break;
                    }
                    }
                    assertTrue(cadena+"\n"+"Hilo que no debiera estar encolado = "+ hilos2[j],encontrado);
                }

            }

        }
        

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

    @Test
    public void hiloDespertadoEncolado(){
        /*Verifica que sólo se haya despertado un hilo que estaba sensibilizado y encolado(que esté en ambas) .
         */
        String[] casteado= new String[2];
        String cadena = "";
        String hiloDespertado="";
        boolean encotrado=false;
        String[] hilos={""};
        String[] hilos2={""};
        for (int i = 0; i < Lineas.size(); i++) {
            if (Lineas.get(i).contains("Contador de disparos :")) {
                cadena = Lineas.get(i);
            }
            if (Lineas.get(i).contains("Hilos en ambas")) {
                casteado = Lineas.get(i).split("=");
                hilos = casteado[1].split("\\|\\|");
                hilos2 = new String[hilos.length - 1];
            }
            if(Lineas.get(i).contains("Hilo despertado  =")){
                encotrado=false;
                casteado=Lineas.get(i).split("=");
                hiloDespertado=casteado[1].trim();
                for (int j = 0; j <hilos.length-1; j++) {
                    hilos2[j]=hilos[j].trim();
                }
                for (int j = 0; j < hilos2.length; j++) {
                    if (hilos2[j].equals(hiloDespertado)){
                        encotrado=true;
                        break;
                    }
                }
                assertTrue(cadena+"\n"+"No se pudo haber despertado el hilo "+ hiloDespertado,encotrado);
            }
        }
    }

    @Test
    public void hiloDespertadoSensibilizado(){
        /*Si se despertó un hilo, tiene que haber podido disparar
         */
        String[] casteado= new String[2];
        String cadena = "";
        String hiloDespertado="";
        String hiloDisparado="";
        boolean despertoAlguien=false;
        String[] hilos={""};
        String[] hilos2={""};
        for (int i = 0; i < Lineas.size(); i++) {
            if (Lineas.get(i).contains("Contador de disparos :")) {
                cadena = Lineas.get(i);
            }

            if (Lineas.get(i).contains("Hilo despertado  =")) {
                casteado = Lineas.get(i).split("=");
                hiloDespertado = casteado[1].trim();
                despertoAlguien=true;
            }
            if(despertoAlguien&&(Lineas.get(i).contains("ha disparado la transicion")||Lineas.get(i).contains("no ha podido disparar"))){
                despertoAlguien=false;

                assertFalse(cadena+"\n"+" No se pudo disparar el siguiente hilo despertado:"+ hiloDisparado,Lineas.get(i).contains("no"));
            }
        }


    }
    @Test
    public void contadorDisparos(){

    }

    public void quienDevuelve(){

    }
    @Test
    public void queNoSerepita(){
        //Encararlo con un or por cada hilo encolado o sensi o ambas
        String cadena = "";
        String[] casteado= new String[2];
        for (int i = 0; i < Lineas.size(); i++) {
            if (Lineas.get(i).contains("Contador de disparos :")) {
                cadena = Lineas.get(i);
            }

            if (Lineas.get(i).contains("Hilos Sensibilizados  =")||Lineas.get(i).contains("Hilos Encolados  =")||Lineas.get(i).contains("Hilos en ambas  =")) {
                casteado = Lineas.get(i).split("=");
                String [] hilos = casteado[1].split("\\|\\|");
                List<String> hilos2= new ArrayList<String>();
                for (int j = 0; j < hilos.length-1; j++) {
                    hilos2.add(hilos[j].trim());
                }
                for (String hilo : hilos2) {
                    int cantidad=0;
                    for (String hiloPivote : hilos2) {
                        if(hilo.equals(hiloPivote)){
                            cantidad++;
                        }

                    }
                    assertTrue(cadena+"\n"+"El hilo "+ hilo+ "   se repite en: "+ Lineas.get(i),cantidad==1);

                }
            }

        }

    }

    @Test
    public void bufferLimitado(){
        //Deberia hacer la forma que la cantidad maxima no sea hardcodeada...

        String cadena = "";
        String[] casteado= new String[2];
        for (int i = 0; i < Lineas.size(); i++) {
            if (Lineas.get(i).contains("Contador de disparos :")) {
                cadena = Lineas.get(i);
            }

            if (Lineas.get(i).contains("Hilos Encolados  =")) {
                casteado = Lineas.get(i).split("=");
                String [] hilos = casteado[1].split("\\|\\|");
                List<String> hilos2= new ArrayList<String>();

                assertTrue(cadena+"\n"+ "Se ha excedido el buffer. Cantidad ="+ (hilos.length-1),hilos.length-1<9);


            }

        }

    }



}