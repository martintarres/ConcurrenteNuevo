import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class RdPTest {

    RdP petri;
    Constantes constantes;
    Log log;

    List<Matriz> historialMarcados;
    List<Integer> historialDisparos;
    List<Boolean> historialEstadoDisparos;
    List<String> historialContador;
    List<String> historialHilos;


    @Before
    public void setUp() throws Exception {
        final String file = "";
        final String path = (new File(".")).getCanonicalPath();
        final String registro = "/registro.txt";
        this.constantes = new Constantes();
        this.log = new Log(file + path + registro, constantes);
        //Lineas = log.leerLineas();

        petri = new RdP(constantes);

        this.historialMarcados = log.getHistorialMarcados();
        this.historialDisparos = log.getHistorialDisparos();
        this.historialEstadoDisparos = log.getHistorialEstadoDisparos();
        this.historialContador= log.extraerLineas("Contador de disparos :",0);
        this.historialHilos = log.getHistorialActividadHilos();
        

    }

    @Test
    public void verificarSensibilidadDisparo() {
        /*
        Verifica si el disparo debió o no dispararse de acuerdo al marcado previo.
         */
        Matriz marcadoPrevio = constantes.marcadoInicial;
        for (int i = 0; i < historialMarcados.size(); i++) {
            try{
                if (historialEstadoDisparos.get(i)){
                    assertTrue(historialContador.get(i)+"\n"+"No se pudo haber disparado = "+ log.traducirDisparo(historialDisparos.get(i)),
                            petri.transicionSensibilizada(historialDisparos.get(i),RdP.Sensibilizadas(constantes.incidenciaPrevia,marcadoPrevio)));
                }
                else{
                    assertFalse(historialContador.get(i)+"\n"+"Si se pudo haber disparado = "+ log.traducirDisparo(historialDisparos.get(i)),
                            petri.transicionSensibilizada(historialDisparos.get(i),RdP.Sensibilizadas(constantes.incidenciaPrevia,marcadoPrevio)));
                }
            }
            catch(Exception e){}
            marcadoPrevio=historialMarcados.get(i);
        }
    }

    @Test
    public void verificarMarcado() {
        /*
        Verifica que todos los marcados del historial se obtuvieron por el disparo de esa transición
        No tiene en cuenta si estaba sensibilizada o no, solo si se disparó.
         */
        Matriz marcadoPrevio = constantes.marcadoInicial;
        for (int i = 0; i < historialMarcados.size(); i++) {
            if(historialEstadoDisparos.get(i)){
                try{
                    Matriz calculada = Matriz.suma(marcadoPrevio,Matriz.obtenerColumna(petri.getIncidencia(),historialDisparos.get(i)));
                    assertTrue(historialContador.get(i)+"\n"+this.historialHilos.get(i)+"\n"+log.getLineaMarcados()+"\n"+calculada,
                            historialMarcados.get(i).esIgual(calculada));
                }
                catch(Exception e){}}
            else{
                assertTrue(historialContador.get(i)+"\n"+this.historialHilos.get(i)+"\n"+log.getLineaMarcados()+"\n"+marcadoPrevio,
                        historialMarcados.get(i).esIgual(marcadoPrevio));
            }

            marcadoPrevio=historialMarcados.get(i);
        }
    }

    @Test
    public void verificarHilosSensibilizados(){

    }

    @Test
    public void pInvariantes() throws Exception {
        /*
        No está sacado como producto punto, sino como producto vectorial
         */
        // POR AHÍ CONVIENE LLAMARLO MATRIZ DE PINVARIANTES Y NO PINVARIANTE
        System.out.println("Matriz de resultado de los P Invariantes = ");
        System.out.println(constantes.resultadoPInv);

        for (int i = 0; i < historialMarcados.size(); i++) {
            //assertEquals(constantes.resultadoPInv,Matriz.multiplicacion(constantes.PInvariante,historialMarcados.get(i)));
            Matriz resultado = Matriz.multiplicacion(constantes.PInvariante,historialMarcados.get(i));

            assertTrue(historialContador.get(i)+"\n"+this.historialHilos.get(i)+"\n"+resultado,
                    resultado.esIgual(constantes.resultadoPInv));
        }
    }
}