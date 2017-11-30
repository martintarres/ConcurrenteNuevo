import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HiloTest {
    //  ListasDeDisparos listadisparos;
    ArrayList<String> hilo1;
    ArrayList<String> hilo2;
    ArrayList<String> hilo3;
    ArrayList<String> hilo4;
    ArrayList<String> hilo5;
    ArrayList<String> hilo6;
    ArrayList<String> hilo7;
    ArrayList<String> hilo8;
    ArrayList<String> hilo9;

    String h1voypor;
    String h2voypor;
    String h3voypor;
    String h4voypor;
    String h5voypor;
    String h6voypor;
    String h7voypor;
    String h8voypor;
    String h9voypor;
    String h1siguiente = "T21";
    String h2siguiente = "T23";
    String h3siguiente = "T10";
    String h4siguiente = "T11";
    String h5siguiente = "T13";
    String h6siguiente = "T12";
    String h7siguiente = "T19";
    String h8siguiente = "T35";
    String h9siguiente = "T31";
    List<String> Lineas;

    @Before
    public void setUp() throws Exception {
        //       listadisparos= new ListasDeDisparos();
        final String file = "";
        final String path = (new File(".")).getCanonicalPath();
        final String archivomarcados = "/marcados.txt";
        final String registro = "/registro.txt";
        Constantes constantes = new Constantes();
        Log lector = new Log(file + path + registro, constantes);
        Lineas = lector.leerLineas();
        hilo1 = new ArrayList<String>();
        hilo2 = new ArrayList<String>();
        hilo3 = new ArrayList<String>();
        hilo4 = new ArrayList<String>();
        hilo5 = new ArrayList<String>();
        hilo6 = new ArrayList<String>();
        hilo7 = new ArrayList<String>();
        hilo8 = new ArrayList<String>();
        hilo9 = new ArrayList<String>();
        hilo1.add("T21");
        hilo1.add("T22");
        hilo2.add("T23");
        hilo2.add("T24");
        hilo3.add("T10");
        hilo4.add("T11");
        hilo5.add("T13");
        hilo5.add("T15");
        hilo5.add("T17");
        hilo6.add("T12");
        hilo6.add("T14");
        hilo6.add("T16");
        hilo6.add("T18");
        hilo7.add("T19");
        hilo8.add("T35");
        hilo8.add("T36");
        hilo9.add("T31");
        hilo9.add("T32");
        hilo9.add("T33");
        hilo9.add("T34");

    }

    @Test
    public void hiloSiguePlaza() {
        String cadena = "";
        String[] casteado = {""};
        String[] casteado2 = {""};
        String numeroHilo = "";
        String numeroTransicion = "";

        for (int i = 0; i < Lineas.size() - 1; i++) {

            if (Lineas.get(i).contains("disparado la transicion")) {
               // System.out.println(Lineas.get(i));
                casteado = Lineas.get(i).split("ha");
                numeroHilo = casteado[0].trim();
                casteado2 = casteado[1].split(":");
                numeroTransicion = casteado2[1].trim();

                if (numeroHilo.equals("Hilo 1")) {
                    assertEquals(h1siguiente, numeroTransicion);
                    h1voypor = numeroHilo;
                    //System.out.println("Soy " + numeroHilo+ " y voy por "  + numeroTransicion);
                    for (int j = 0; j < hilo1.size(); j++) {
                        if (hilo1.get(j).equals(numeroTransicion)) {
                            if (j == hilo1.size() - 1) {
                                h1siguiente = hilo1.get(0);
                            } else

                                h1siguiente = hilo1.get(j + 1);

                        }
                    }
                    //System.out.println("el siguiente es :" + h1siguiente);


                }
                if (numeroHilo.equals("Hilo 2")) {
                    assertEquals(h2siguiente, numeroTransicion);
                    h2voypor = numeroHilo;
                    // System.out.println("Soy " + numeroHilo+ " y voy por "  + numeroTransicion);
                    for (int j = 0; j < hilo2.size(); j++) {
                        if (hilo2.get(j).equals(numeroTransicion)) {
                            if (j == hilo2.size() - 1) {
                                h2siguiente = hilo2.get(0);
                            } else

                                h2siguiente = hilo2.get(j + 1);

                        }
                    }
                    //System.out.println("el siguiente es :" + h2siguiente);


                }
                if (numeroHilo.equals("Hilo 3")) {
                    assertEquals(h3siguiente, numeroTransicion);
                    h3voypor = numeroHilo;
                     //System.out.println("Soy " + numeroHilo+ " y voy por "  + numeroTransicion);
                    for (int j = 0; j < hilo3.size(); j++) {
                        if (hilo3.get(j).equals(numeroTransicion)) {
                            if (j == hilo3.size() - 1) {
                                h3siguiente = hilo3.get(0);
                            } else

                                h3siguiente = hilo3.get(j + 1);

                        }
                    }
                   // System.out.println("el siguiente es :" + h2siguiente);
                }
                if (numeroHilo.equals("Hilo 4")) {
                    assertEquals(h4siguiente, numeroTransicion);
                    h4voypor = numeroHilo;
                    // System.out.println("Soy " + numeroHilo+ " y voy por "  + numeroTransicion);
                    for (int j = 0; j < hilo4.size(); j++) {
                        if (hilo4.get(j).equals(numeroTransicion)) {
                            if (j == hilo4.size() - 1) {
                                h4siguiente = hilo4.get(0);
                            } else

                                h4siguiente = hilo4.get(j + 1);

                        }
                    }
                }
                    //System.out.println("el siguiente es :" + h2siguiente);
                    if (numeroHilo.equals("Hilo 5")) {
                        assertEquals(h5siguiente,numeroTransicion);
                        h5voypor = numeroHilo;
                         //System.out.println("Soy " + numeroHilo+ " y voy por "  + numeroTransicion);
                        for(int j=0;j<hilo5.size(); j++){
                            if(hilo5.get(j).equals(numeroTransicion)){
                                if(j== hilo5.size()-1){
                                    h5siguiente=hilo5.get(0);
                                }else

                                    h5siguiente=hilo5.get(j+1);

                            }
                        }
                        //System.out.println("el siguiente es :" + h2siguiente);
                    }
                    if (numeroHilo.equals("Hilo 6")) {
                        assertEquals(h6siguiente,numeroTransicion);
                        h6voypor = numeroHilo;
                        // System.out.println("Soy " + numeroHilo+ " y voy por "  + numeroTransicion);
                        for(int j=0;j<hilo6.size(); j++){
                            if(hilo6.get(j).equals(numeroTransicion)){
                                if(j== hilo6.size()-1){
                                    h6siguiente=hilo6.get(0);
                                }else

                                    h6siguiente=hilo6.get(j+1);

                            }
                        }
                        //System.out.println("el siguiente es :" + h2siguiente);
                    }
                    if (numeroHilo.equals("Hilo 7")) {
                        assertEquals(h7siguiente,numeroTransicion);
                        h7voypor = numeroHilo;
                         //System.out.println("Soy " + numeroHilo+ " y voy por "  + numeroTransicion);
                        for(int j=0;j<hilo7.size(); j++){
                            if(hilo7.get(j).equals(numeroTransicion)){
                                if(j== hilo7.size()-1){
                                    h7siguiente=hilo7.get(0);
                                }else

                                    h7siguiente=hilo7.get(j+1);

                            }
                        }
                        //System.out.println("el siguiente es :" + h2siguiente);
                    }
                    if (numeroHilo.equals("Hilo 8")) {
                        assertEquals(h8siguiente,numeroTransicion);
                        h8voypor = numeroHilo;
                         //System.out.println("Soy " + numeroHilo+ " y voy por "  + numeroTransicion);
                        for(int j=0;j<hilo8.size(); j++){
                            if(hilo8.get(j).equals(numeroTransicion)){
                                if(j== hilo8.size()-1){
                                    h8siguiente=hilo8.get(0);
                                }else

                                    h8siguiente=hilo8.get(j+1);

                            }
                        }
                        //System.out.println("el siguiente es :" + h2siguiente);
                    }
                    if (numeroHilo.equals("Hilo 9")) {
                        assertEquals(h9siguiente,numeroTransicion);
                        h9voypor = numeroHilo;
                         //System.out.println("Soy " + numeroHilo+ " y voy por "  + numeroTransicion);
                        for(int j=0;j<hilo9.size(); j++){
                            if(hilo9.get(j).equals(numeroTransicion)){
                                if(j== hilo9.size()-1){
                                    h9siguiente=hilo9.get(0);
                                }else

                                    h9siguiente=hilo9.get(j+1);

                            }
                        }
                        //System.out.println("el siguiente es :" + h2siguiente);
                    }
                }
            }
        }

    }
