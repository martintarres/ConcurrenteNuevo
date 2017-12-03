import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

public class Constantes {


    public Vector<Integer> arregloT = new Vector<Integer>();
    public Vector<Integer> arregloMP = new Vector<Integer>();

    public Matriz incidenciaPrevia;
    public Matriz incidenciaPosterior;
    public Matriz marcadoInicial;
    public Matriz PInvariante;
    public String[] nombreTransiciones;
    public String[] nombreMarcados;
    public int inci1;
    public int inci2;
    public int TotalPInva;
    private Lector lector;
    Lector lector1;
    String[][] Posterior;
    List <String>PInvariantes;        // esta lista guarda el resultado de los p invariantes
    String file;
    String path;
    String archivo;
    String invariantes;
    public Matriz resultadoPInv;

    // por que no declare cuanto valia las plazas y las transiciones y armaba de una la mtraiz
    public Constantes() {
        file = "file:///";
        try {
            path = (new File(".")).getCanonicalPath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //System.out.println("Ruta actual ");
        //System.out.println(file+path);
        archivo = "/archivo.html";
        invariantes = "/analisisInvariante.html";
        //System.out.println(file+path+archivo);
        inci1 = 0;  // Para calcular numero de filas de las matrices
        inci2 = -1; // Para calcular numero de columnas de lsa matrices

        TotalPInva = -1;  // Para calcular el numero de filas de la matriz de P invariantes

        lector = new Lector(file + path + archivo);
        lector1 = new Lector(file + path + invariantes);
        lector.convertir();
        lector1.convertir();
        calculoInci1();
        nombreTransiciones();
        inci2();
        crearMatrizIncidenciaPosterior();
        crearMatrizIncidenciaPrevia();
        crearMatrizMarcadoInicial();
        crearMatrizPInvariantes();
        paraTInvariantes();

    }

    private void calculoInci1() {


        Posterior = lector.getTabla(lector.cortar("Forwards incidence matrix I+", "Backwards incidence matrix I-"));

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < lector.cantidadColumnas(lector.cortar("Forwards incidence matrix I+", "Backwards incidence matrix I-")); j++) {
                if (Posterior[i][j].contains("T")) {
                    // System.out.println(Posterior[i][j]);
                    arregloT.add(inci1);
                    //System.out.println("Voy a mostrar mi vector arregloT " + arregloT.elementAt(inci1));
                    inci1++;

                }
            }
        }
    }

    private void nombreTransiciones() {

        //System.out.println("Nombres de las transiciones");
        nombreTransiciones = new String[Posterior[0].length - 1];
        for (int i = 0; i < nombreTransiciones.length; i++) {
            nombreTransiciones[i] = Posterior[0][i + 1];
        }
        for (String i : nombreTransiciones) {
            //System.out.print(i + " ");
        }
        //System.out.println();
        nombreMarcados = new String[Posterior.length - 1];
        for (int i = 0; i < nombreMarcados.length; i++) {
            nombreMarcados[i] = Posterior[i + 1][0];

        }
    }

    private void inci2() {
        // String[][] tablaPosterior = lector.getTabla(lector.cortar("Forwards incidence matrix I+","Backwards incidence matrix I-"));
        for (int i = 0; i < lector.cantidadFilas(lector.cortar("Forwards incidence matrix I+", "Backwards incidence matrix I-")); i++) {
            for (int j = 0; j < 1; j++) {
                // System.out.println(tablaPevia[i][j]);
                if (inci2 != -1) {
                    arregloMP.add(inci2);
                    //System.out.println("Voy a mostrar mi vector arregloMP " + arregloMP.elementAt(inci2));
                }

                inci2++;
            }
            // System.out.println("\n");

        }
    }


    private void crearMatrizIncidenciaPosterior() {
        int[][] posterior = new int[inci2][inci1];

        for (int i = 0; i < inci2 + 1; i++) {
            for (int j = 0; j < inci1 + 1; j++) {

                if (esNumero(Posterior[i][j])) {

                    //  System.out.print(tablaPosterior[i][j]);
                    posterior[i - 1][j - 1] = Integer.parseInt(Posterior[i][j]);
                    // System.out.print(Integer.parseInt(Posterior[i][j]));

                }

            }
            //System.out.println("\n");
        }

        try {
            this.incidenciaPosterior = new Matriz(posterior);
        } catch (MatrizException e) {
            e.printStackTrace();
        }
    }


    private void crearMatrizIncidenciaPrevia() {
        int[][] previa = new int[inci2][inci1];
        //System.out.println("voy a mostrar tabla previa ");
        final String[][] tablaPrevia = lector.getTabla(lector.cortar("Backwards incidence matrix I-", "Combined incidence matrix I"));
        for (int i = 0; i < inci2 + 1; i++) {
            for (int j = 0; j < inci1 + 1; j++) {
                if (esNumero(tablaPrevia[i][j])) {

                    //System.out.print(tablaPrevia[i][j]);
                    previa[i - 1][j - 1] = Integer.parseInt(tablaPrevia[i][j]);
                }
            }
            // System.out.println("\n");

        }

        try {
            this.incidenciaPrevia = new Matriz(previa);
        } catch (MatrizException e) {
            e.printStackTrace();
        }
    }

    private void crearMatrizMarcadoInicial() {

        int[][] incial = new int[inci2][1];
        //System.out.println("voy a mostrar marcado incial ");

        final String[][] tablaInicial = lector.getTabla(lector.cortar("Marking", "Current"));
        for (int i = 0; i < lector.cantidadFilas(lector.cortar("Marking", "Current")); i++) {
            for (int j = 0; j < inci2 + 1; j++) {

                if (esNumero(tablaInicial[i][j])) {
                    incial[j - 1][i - 1] = Integer.parseInt(tablaInicial[i][j]);
                    //  System.out.print(tablaInicial[i][j]);
                }

            }
            // System.out.println("");
        }

        try {
            this.marcadoInicial = new Matriz(incial);
        } catch (MatrizException e) {
            e.printStackTrace();
        }
    }

    private void crearMatrizPInvariantes() {
        final List<Integer> lista = new ArrayList<Integer>();


        final String[][] PInvariantes = lector1.getTabla(lector1.cortar("P-Invariants", "P-Invariant equations"));

        for (int i = 0; i < lector1.cantidadFilas(lector1.cortar("P-Invariants", "P-Invariant equations")); i++) {
            for (int j = 0; j < 1; j++) {

                TotalPInva++;
            }


        }

        int[][] copia = new int[TotalPInva][inci2];


        for (int i = 0; i < TotalPInva + 1; i++) {      // aca hago TotalPInva+1 porque aun no saque las letras.
            //  System.out.println("soy i " + i);         //  entonces tengo una fila mas
            for (int j = 0; j < inci2 + 1; j++) {
                //   System.out.println("soy j " + j);
                //   System.out.println();
                if (esNumero(PInvariantes[i][j])) {
                    // System.out.println("soy i de if" + i);
                    lista.add(Integer.parseInt(PInvariantes[i][j]));
                    // hola.add(Integer.parseInt(tumama[i][j]));
                    //System.out.print(PInvariantes[i][j]);

                }
            }
        }

        for (int i = 0; i < TotalPInva; i++) {

            for (int j = 0; j < inci2; j++) {

                copia[i][j] = lista.get(0);


                lista.remove(0);


            }
        }

        try {
            this.PInvariante = new Matriz(copia);
        } catch (MatrizException e) {
            e.printStackTrace();
        }
    }


    public void paraTInvariantes() {

        URL urlob;
        StringBuffer html;
        StringBuffer textoPlano;
        PInvariantes = new ArrayList<String>();


        StringBuffer a = new StringBuffer();
        try {

            String linea = null;
            urlob = new URL(file + path + invariantes);
            InputStreamReader rea = new InputStreamReader(urlob.openStream());
            BufferedReader br = new BufferedReader(rea);
            html = new StringBuffer();
            textoPlano = new StringBuffer();
            while ((linea = br.readLine()) != null) {
                html.append(linea);


            }

            List<String> hola = new ArrayList<String>();
            hola = cortar("P-Invariant equations", "Analysis time:", html);


            for (int i = 0; i < hola.size(); i++) {

                StringBuffer q = new StringBuffer(hola.get(i));

                for (int j = 0; j < q.length(); j++) {
                    if (q.charAt(j) == '=') {
                        q.delete(0, j + 2);


                    }
                }
                for (int j = 0; j < q.length(); j++) {

                    if (q.charAt(j) == '<') {
                        q.delete(j, q.length());
                        PInvariantes.add(q.toString());
                    }
                }

            }
            PInvariantes.remove(PInvariantes.size() - 1);        // aca resto estos dos porque en la lista me agrega los lugares
            PInvariantes.remove(PInvariantes.size() - 1);        // del <br> que son dos al final, entonces lo saco

            int[][] pi = new int [this.PInvariante.getM()][1];
            for (int i = 0; i < this.PInvariante.getM(); i++) {
////
                pi[i][0] = Integer.parseInt(PInvariantes.get(i).trim());
            }

            this.resultadoPInv = new Matriz(pi);

            // System.out.println("esta es la lista con los numeros despues del igual");
            //System.out.println(fin);  /// ESTA RECIEN ES LA LISTA QUE TIENE LOS NUMEROS DESPUES DEL IGUAL.

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MatrizException e) {
            e.printStackTrace();
        }
    }


    public List cortar(String desde, String hasta, StringBuffer textoplano) {
        // DEL TEXTO PLANO SOLO GUARDA LO QUE ESTE ENTRE LOS DOS STRING SIN INCLUIRLOS
        List<String> listasdas = new ArrayList<String>();

        //  System.out.println(textoplano.toString());
        // System.out.println(textoplano.length());
        for (int i = 0; i < textoplano.length(); i++) {

            if (textoplano.charAt(i) == '<') {
                textoplano.insert(i + 4, "\n");

            }
            //System.out.print(textoplano.charAt(i));
        }

        // System.out.print(textoplano.toString());


        String linea;
        StringReader rea = new StringReader(textoplano.toString());
        BufferedReader br = new BufferedReader(rea);
        StringBuffer pedazo = new StringBuffer();

        List<String> ASD = new ArrayList<String>();

        boolean copiando = false;
        try {
            while ((linea = br.readLine()) != null) {

                if (!copiando && linea.contains(desde)) {
                    copiando = true;
                    continue;
                }

                if (linea.contains(hasta)) {
                    break;
                }
                if (copiando) {
                    pedazo.append(linea);
                    pedazo.append("\n");
                    ASD.add(linea);

                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("ASD" + ASD.get(1));


        return ASD;


    }

    public static Boolean esNumero(final String nume) {

        try {
            Integer.parseInt(nume);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public int traducir(final String transicion) throws Exception {

        for (int i = 0; i < nombreTransiciones.length; i++) {
            if (transicion.equals(nombreTransiciones[i])) {
                return i;
            }
        }
        throw new Exception("No existe ese nombre de transicion");
    }

}
