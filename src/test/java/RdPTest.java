import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by YepezHinostroza on 24/11/2017.
 */
public class RdPTest {
    List<String> Lineas;
    RdP petri;
    Constantes constantes;
    Log log;

    List<Matriz> historialMarcados;
    List<Integer> historialDisparos;
    List<Boolean> historialEstadoDisparos;
    List<String> historialContador;

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
        this.historialEstadoDisparos=log.getHistorialEstadoDisparos();
        this.historialContador= log.extraerLineas("Contador de disparos :",0);
        

    }

    @Test
    public void verificarDisparo() {
        /* Verifica que el siguiente debió disparar  o NO de acuerdo al marcado anterior.

         */
        // Debería comprobar sai la dimension de las matrcies coinciden con la del las de incidencia o algo asi???
        //List<Matriz> marcados = new ArrayList<Matriz>();
        //marcados.add(petri.marcadoInicial());

        /*

        EL NRO DE DISPARO ESTA DESFASADO UNA UNIDAD
         */
/*
        Matriz marcadoPrevio = petri.marcadoInicial();
        String[] casteado = {""};
        String transicion = "";
        String cadena = "";
        try {
            for (int i = 0; i < Lineas.size() - 1; i++) {
                //System.out.println(marcadoPrevio);
                if (Lineas.get(i).contains("Contador de disparos :")) {
                    cadena = Lineas.get(i);
                }

                if (Lineas.get(i).contains(log.lineaMarcados())) {
                    marcadoPrevio = log.convertirMarcado(Lineas.get(i + 1)).transpuesta();

                }


                if (Lineas.get(i).contains("ha disparado la transicion") || Lineas.get(i).contains("no ha podido disparar")) {
                    casteado = Lineas.get(i).split(":");
                    transicion = casteado[1].trim();
                    //System.out.println(traducirTransicion(transicion,constantes));
                    Matriz Sensi = RdP.Sensibilizadas(constantes.incidenciaPrevia, marcadoPrevio);
                    //System.out.println(Sensi.getM()+"    "+Sensi.getN());
                    if (Lineas.get(i).contains("no")) {
                        assertFalse(cadena + "\n" + "La transición " + transicion + " SI estaba sensibilizada", petri.transicionSensibilizada(traducirTransicion(transicion, constantes), Sensi));

                    } else {
                        assertTrue(cadena + "\n" + "La transición " + transicion + " NO estaba sensibilizada", petri.transicionSensibilizada(traducirTransicion(transicion, constantes), Sensi));

                    }
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        */
    }

    @Test
    public void verificarMarcado() {
        for (String s :
                historialContador) {
            System.out.println(s);
        }

        /*
       *Verifica que se obtuvo ese marcado por el disparo de esa transición y el marcado previo
        *No importa si estaba sensibilizado o no
         */
        /*
        String cadena = "";
        String[] casteado={};
        String transicion="";
        Matriz marcadoPrevio=petri.marcadoInicial();
        Matriz marcadoActual=petri.marcadoInicial();
        Matriz marcadoPrueba=petri.marcadoInicial();
        boolean disparado=false;

        System.out.println(petri.lineaMarcados());
*/


        //System.out.println(log.extraerLineas(petri.lineaMarcados(),0));

        /*

        for (int i = 0; i < Lineas.size(); i++) {
            try{
                if (Lineas.get(i).contains("Contador de disparos :")) {
                    cadena = Lineas.get(i);
                }
                if (Lineas.get(i).contains(petri.lineaMarcados())){
                    marcadoPrevio=marcadoActual;
                    marcadoActual=convertirMarcado(Lineas.get(i+1)).transpuesta();
                    if(disparado){
                        marcadoPrueba=Matriz.suma(marcadoPrevio,Matriz.obtenerColumna(petri.getIncidencia(),traducirTransicion(transicion,constantes)));
                        assertTrue(cadena+"\n"+"Marcado Actual tuvo que hber sido ="+"\n"+
                                petri.lineaMarcados()+"\n"+marcadoPrevio,marcadoActual.esIgual(marcadoPrueba));

                    }
                    else{
                        assertTrue(cadena+"\n"+"Marcado Actual tuvo que hber sido ="+"\n"+
                                petri.lineaMarcados()+"\n"+marcadoPrevio,marcadoActual.esIgual(marcadoPrevio));
                    }


                }
                if(Lineas.get(i).contains("ha disparado la transicion")||Lineas.get(i).contains("no ha podido disparar")){
                    casteado = Lineas.get(i).split(":");
                    transicion= casteado[1].trim();
                    if(Lineas.get(i).contains("no")){
                        disparado=false;
                    }
                    else{
                        disparado=true;
                    }
                }

            }
            catch(Exception e){

            }

        }
        */

    }

    @Test
    public void pInvariantes() throws Exception {
        String cadena = "";
        Matriz marcadoActual = null;
        for (int i = 0; i < Lineas.size() - 1; i++) {
            if (Lineas.get(i).contains("Contador de disparos :")) {
                cadena = Lineas.get(i);

            }
            if (Lineas.get(i).contains(log.lineaMarcados())) {
                marcadoActual = log.convertirMarcado(Lineas.get(i + 1)).transpuesta();
                //System.out.println("Voy a mostrar el marcado actual que estoy comprobando");
                //marcadoActual.transpuesta().imprimir();


                List<String> prodInterno = new ArrayList<String>();

                int[][] matrizPInvariante = new int[1][constantes.PInvariante.getN()];

                for (int ii = 0; ii < constantes.PInvariante.getM(); ii++) {
                    for (int j = 0; j < constantes.PInvariante.getN(); j++) {
                        matrizPInvariante[0][j] = constantes.PInvariante.getMatriz()[ii][j];
                    }


                    Matriz matrizPInva = null;
                    try {
                        matrizPInva = new Matriz(matrizPInvariante);
                    } catch (MatrizException e) {
                        e.printStackTrace();
                    }

                    int resultado = 0;

                    for (int g = 0; g < constantes.PInvariante.getN(); g++) {
                        int multiplicacion = 0;
                        multiplicacion = marcadoActual.getMatriz()[g][0] * matrizPInva.getMatriz()[0][g];
                        resultado = resultado + multiplicacion;
                    }
                    prodInterno.add(Integer.toString(resultado));
                }


                //System.out.println("voy a mostrar lista de resultado del producto interno");
                //System.out.println(prodInterno);

                //  System.out.println("Voy a mostrar lista de resultado de ecuaciones");
                //  System.out.println(constantes.fin);


                assertEquals(cadena, constantes.PInvariantes, prodInterno);

                //System.out.println("");
                //System.out.println("");
                //System.out.println("");

            }
        }
    }




}