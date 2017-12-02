
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;


public class MonitorTest {
    List<String> Lineas;
    boolean bloqueado;
    Log log;
    List<String> historialContador;
    List<Boolean> historialEstadoDisparos;


    @Before
    public void setUp() throws Exception {
        final String file = "";
        final String path = (new File(".")).getCanonicalPath();
        final String registro = "/registro.txt";
        Constantes constante = new Constantes();
        log = new Log(file + path + registro, constante);
        this.historialContador = log.extraerLineas("Contador de disparos :", 0);
        this.historialEstadoDisparos = log.getHistorialEstadoDisparos();
        Lineas = log.leerLineas();

    }


    @Test
    public void monitorBloqueado() {
        /*  Verifica que, habiendose llevado el mutex un hilo, no  es posible que un hilo obtenga el mutex
            hasta que no lo haya devuelto el hilo mencionado primero.
         */
        /*
        String cadena = "";

        for (int i = 0; i < Lineas.size(); i++) {
            if (Lineas.get(i).contains(">>>")) {
                bloqueado = true;
            }
            if (Lineas.get(i).contains("<<<")) {
                bloqueado = false;
            }
            if (Lineas.get(i).contains("Contador de disparos :")) {
                cadena = Lineas.get(i);
            }
            assertFalse(cadena + "\n" + Lineas.get(i) + "\n", Lineas.get(i).contains("obtiene el mutex.") && bloqueado);


        }
        */

    }

    @Test
    public void encolados() {
        /*  Analiza sobre la linea Hilos encolados en contraposición de los intento fallidos de disparo e hilo despertado.
        *   Botonea en el caso que haya un encolado que falta  como también que sobre un encolado
        * */

        // NO ANALIZA LA ÚLTIMA MODIFICACIÓN POR IndexOutOfBoundsException

        List<String> actividadHilos = log.getHistorialActividadHilos();
        List<Boolean> estadosDisparos = log.getHistorialEstadoDisparos();
        List<String> hilosDespertados = log.getHistorialHilosDespertados();
        List<List<String>> hilosEncolados = log.getHistorialHilosEncolados();
        
        List<String> encolados = new ArrayList<>();
        for (int i = 0; i < hilosEncolados.size()-1; i++) {
            if (!estadosDisparos.get(i)){
                encolados.add(actividadHilos.get(i));
            }
            //System.out.println(encolados+"\n"+hilosEncolados.get(i));
            assertTrue(this.historialContador.get(i)+"\n"+"Encolados correctos = "+ encolados,
                    hilosIguales(encolados,hilosEncolados.get(i)));
            if(hilosDespertados.get(i).trim().length()!=0){
                encolados.remove(hilosDespertados.get(i));
            }
            
        }




    }

    @Test
    public void autorizado() {
        /*Verifica que el próximo en intentar disparar sea el que obtuvo el mutex o el recien despertado.
        En sí, tambien verifico tambien que tenga prioridad sobre el mutex el recién despertado
         */
        List<String> hilosDisparando = log.getHistorialActividadHilos();
        List<String> hilosPermitods = log.getHistorialHilosPermitidos();

        for (int i = 0; i < hilosDisparando.size(); i++) {
            assertTrue(this.historialContador.get(i),
                    hilosDisparando.get(i).equals(hilosPermitods.get(i)));
        }

    }

    @Test
    public void hiloDespertadoEncoladoSensibilizado() {
        /*Verifica que sólo se haya despertado un hilo que estaba sensibilizado y encolado(que esté en ambas) .
         */
        List<List<String>> listaHilosSensibilizados = log.getHistorialHilosSensibilizados();
        List<List<String>> listaHilosEncolados = log.getHistorialHilosEncolados();
        List<String> hilosDespertados = log.getHistorialHilosDespertados();
        for (int i = 0; i < hilosDespertados.size(); i++) {
            if (hilosDespertados.get(i).length() != 0) {
                assertTrue(this.historialContador.get(i),
                        listaHilosEncolados.get(i).contains(hilosDespertados.get(i)) &&
                                listaHilosSensibilizados.get(i).contains(hilosDespertados.get(i)));
            }
        }
    }

    @Test
    public void hiloDespertadoDisparado() {
        /*Si se despertó un hilo, tiene que haber podido disparar
         */
        List<String> hilosDespertados = log.getHistorialHilosDespertados();
        for (int i = 0; i < hilosDespertados.size() - 1; i++) {
            if (hilosDespertados.get(i).length() != 0) {
                assertTrue(this.historialContador.get(i + 1),
                        this.historialEstadoDisparos.get(i + 1));
            }
        }


    }

    @Test
    public void hiloRepetido() {
        List<List<String>> listaHilosEncolados = log.getHistorialHilosEncolados();
        List<List<String>> listaHilosSensibilizados = log.getHistorialHilosSensibilizados();
        List<List<String>> listaHilosEnAmbas = log.getHistorialHilosEnAmbas();
        for (int i = 0; i < listaHilosEncolados.size(); i++) {
            assertFalse(this.historialContador.get(i)+"\n"+" Encolado Repetido"
                    ,hiloRepetido(listaHilosEncolados.get(i)));
        }
        for (int i = 0; i < listaHilosSensibilizados.size(); i++) {
            assertFalse(this.historialContador.get(i)+"\n"+" Sensibilizado Repetido"
                    ,hiloRepetido(listaHilosSensibilizados.get(i)));
        }
        for (int i = 0; i < listaHilosEnAmbas.size(); i++) {
            assertFalse(this.historialContador.get(i)+"\n"+" En ambas Repetido"
                    ,hiloRepetido(listaHilosEnAmbas.get(i)));
        }
    }

    @Test
    public void bufferLimitado() {
        //Deberia hacer la forma que la cantidad maxima no sea hardcodeada...
        int MAXBUFFER = 9;

        List<List<String>> listaHilosEncolados = log.getHistorialHilosEncolados();
        for (int i = 0; i < listaHilosEncolados.size(); i++) {
            assertTrue(this.historialContador.get(i),
                    listaHilosEncolados.get(i).size() < MAXBUFFER);
        }

    }

    @Test
    public void contadorDisparos() {

    }

    @Test
    public void quienDevuelve() {

    }

    public boolean hiloRepetido(List<String> lista){
        Set<String> conjunto = new TreeSet<String>(lista);
        return !(lista.size()==conjunto.size());

    }

    public boolean hilosIguales(List<String> hilo1, List<String> hilo2){
        Set<String> conjunto = new TreeSet<String>(hilo1);
        Set<String> conjunto2 = new TreeSet<String>(hilo2);
        return (conjunto.equals(conjunto2));

    }


}