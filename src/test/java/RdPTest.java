import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void setUp() throws Exception {
            Log lector = new Log("C:\\Users\\alexa\\Desktop\\ConcurrenteNuevo\\registro.txt",
                    "\"C:\\\\Users\\\\alexa\\\\Desktop\\\\ConcurrenteNuevo\\\\\\registroVacio.txt\"");
            // creo una lista de as lineas
            Lineas = lector.leerLineas();
            constantes = new Constantes();
            petri = new RdP(constantes);

    }
    @Test
    public void verificarDisparo(){
        /* Verifica que el siguiente debió disparar  o NO de acuerdo al marcado anterior.

         */
        // Debería comprobar sai la dimension de las matrcies coinciden con la del las de incidencia o algo asi???
        //List<Matriz> marcados = new ArrayList<Matriz>();
        //marcados.add(petri.marcadoInicial());

        /*

        EL NRO DE DISPARO ESTA DESFASADO UNA UNIDAD
         */

        Matriz marcadoPrevio = petri.marcadoInicial();
        String [] casteado = {""};
        String transicion="";
        String cadena = "";
        try{
            for (int i = 0; i < Lineas.size()-1; i++) {
                //System.out.println(marcadoPrevio);
                if (Lineas.get(i).contains("Contador de disparos :")) {
                    cadena = Lineas.get(i);
                }

                if (Lineas.get(i).contains(petri.lineaMarcados())){
                    marcadoPrevio=convertirMarcado(Lineas.get(i+1)).transpuesta();

                }


                if(Lineas.get(i).contains("ha disparado la transicion")||Lineas.get(i).contains("no ha podido disparar")){
                    casteado = Lineas.get(i).split(":");
                    transicion= casteado[1].trim();
                    //System.out.println(traducirTransicion(transicion,constantes));
                    Matriz Sensi= RdP.Sensibilizadas(constantes.incidenciaPrevia,marcadoPrevio);
                    //System.out.println(Sensi.getM()+"    "+Sensi.getN());
                    if (Lineas.get(i).contains("no")){
                        assertFalse(cadena+"\n"+ "La transición "+transicion+" SI estaba sensibilizada",petri.transicionSensibilizada(traducirTransicion(transicion,constantes),Sensi));

                    }
                    else{
                        assertTrue(cadena+"\n"+ "La transición "+transicion+" NO estaba sensibilizada",petri.transicionSensibilizada(traducirTransicion(transicion,constantes),Sensi));

                    }
                }
            }

        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    @Test
    public void verificarMarcado(){
        /*
       *Verifica que se obtuvo ese marcado por el disparo de esa transición y el marcado previo
        *No importa si estaba sensibilizado o no
         */
        String cadena = "";
        String[] casteado={};
        String transicion="";
        Matriz marcadoPrevio=petri.marcadoInicial();
        Matriz marcadoActual=petri.marcadoInicial();
        Matriz marcadoPrueba=petri.marcadoInicial();
        boolean disparado=false;

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

    }

    public Matriz convertirMarcado(String Linea) throws Exception{
        try{
            List<Integer> enteros = new ArrayList<>();
            String[] casteado= Linea.split(" ");
            for (String numero :
                    casteado) {
                if(constantes.esNumero(numero)){
                    enteros.add(Integer.parseInt(numero));
                }
            }

            int[][] arreglo= new int[1][enteros.size()];
            for (int i = 0; i <enteros.size() ; i++) {
                arreglo[0][i]=enteros.get(i);

            }
            Matriz marcado= new Matriz(arreglo);
            return marcado;

        }
        catch(Exception e){
            System.err.println("No se ha podido crear la matriz");
            return null;

        }

    }

    public int traducirTransicion(String transicion, Constantes constantes) throws Exception{

        for (int i = 0; i < constantes.nombreTransiciones.length; i++) {
            if(transicion.equals(constantes.nombreTransiciones[i].trim())){
                return i;
            }

        }
        throw new Exception("No se ha encontrado dicho nombre de transicion"+ transicion);

    }

}