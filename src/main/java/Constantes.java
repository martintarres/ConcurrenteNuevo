import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.File;

public class Constantes {


  public Vector <Integer> arregloT = new Vector <Integer>();
  public Vector <Integer> arregloMP = new Vector <Integer>();

  public Matriz incidenciaPrevia;
  public Matriz incidenciaPosterior;
  public Matriz marcadoInicial;
  public Matriz PInvariante;
  public String[] nombreTransiciones;
  public String[] nombreMarcados;
  public int inci1;
  public int inci2;
  public int TotalPInva;

  // por que no declare cuanto valia las plazas y las transiciones y armaba de una la mtraiz
  public Constantes() {
    try{
      final String file= "file:///" ;
      final String path = (new File(".")).getCanonicalPath();
      //System.out.println("Ruta actual ");
      //System.out.println(file+path);
      final String archivo = "/archivo.html";
      final String invariantes ="/analisisInvariante.html";
      //System.out.println(file+path+archivo);
      inci1 = 0;  // Para calcular numero de filas de las matrices
      inci2 = -1; // Para calcular numero de columnas de lsa matrices

      TotalPInva= -1;  // Para calcular el numero de filas de la matriz de P invariantes

     // System.out.println(file+path+archivo);


      final Lector lector = new Lector(file+path+archivo);
      final Lector lector1 = new Lector(file+path+invariantes);
      lector.convertir();
      lector1.convertir();


      final String[][] Posterior = lector.getTabla(lector.cortar("Forwards incidence matrix I+", "Backwards incidence matrix I-"));

      for (int i = 0; i < 1; i++) {
        for (int j = 0; j < lector.cantidadColumnas(lector.cortar("Forwards incidence matrix I+", "Backwards incidence matrix I-")); j++) {
          if (Posterior[i][j].contains("T")) {
            // System.out.println(Posterior[i][j]);
            arregloT.add(inci1);
            //System.out.println("Voy a mostrar mi vector arregloT " + arregloT.elementAt(inci1));
            inci1++;


          }

        }
        //System.out.println("\n");

      }
      System.out.println("Nombres de las transiciones");
      nombreTransiciones = new String[Posterior[0].length-1];
      for(int i=0;i<nombreTransiciones.length;i++){
        nombreTransiciones[i]=Posterior[0][i+1];
      }
      for(String  i: nombreTransiciones){
        System.out.print(i+" ");
      }
      System.out.println();
      nombreMarcados= new String[Posterior.length-1];
      for (int i = 0; i < nombreMarcados.length; i++) {
        nombreMarcados[i]= Posterior[i+1][0];

      }

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

/*
      System.out.println("");
      System.out.println("");
      System.out.println("");
*/
      int[][] posterior = new int[inci2][inci1];

      //System.out.println("voy a mostrar matriz de incidendia posterior");


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


      //incidenciaPosterior.imprimir();

/*
      System.out.println("");
      System.out.println("");
      System.out.println("");
*/
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

      //incidenciaPrevia.imprimir();

/*
      System.out.println("");
      System.out.println("");
      System.out.println("");
*/
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

      //marcadoInicial.imprimir();

/*
      System.out.println("");
      System.out.println("");
      System.out.println("");

*/

      //System.out.println("voy a mostrar P invariantes ");

      final List <Integer> lista = new ArrayList <Integer>();


      final String[][] PInvariantes= lector1.getTabla(lector1.cortar("P-Invariants","P-Invariant equations"));

      for (int i = 0; i < lector1.cantidadFilas(lector1.cortar("P-Invariants", "P-Invariant equations")); i++) {
        for(int j=0; j< 1;j++){

          TotalPInva++;
        }


      }



      int[][] copia = new int[TotalPInva][inci2];



      for (int i = 0; i < TotalPInva+1; i++) {      // aca hago TotalPInva+1 porque aun no saque las letras.
        //  System.out.println("soy i " + i);         //  entonces tengo una fila mas
        for (int j = 0; j < inci2+1 ; j++) {
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
        this.PInvariante= new Matriz(copia);
      } catch (MatrizException e) {
        e.printStackTrace();
      }

     // PInvariante.imprimir();

    }
    catch(Exception e){

    }




  }



  public static Boolean esNumero(final String nume){

    try {
      Integer.parseInt(nume);
      return true;
    }
    catch (NumberFormatException e){
      return false;
    }

  }
  public int traducir(final String transicion) throws Exception{

    for (int i = 0; i < nombreTransiciones.length; i++) {
      if(transicion.equals(nombreTransiciones[i])){
        return i;
      }
    }
    throw new Exception("No existe ese nombre de transicion");
  }

}
