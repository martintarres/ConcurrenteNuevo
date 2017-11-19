import java.util.ArrayList;
import java.util.List;

public class RdP{
  private Matriz marcadoInicial;
  private Matriz marcadoActual;
  private Matriz incidencia;
  private Matriz incidenciaPrevia;

  public Matriz vectorSensibilizadas;
  private Matriz MInvariantes;
  public List<PInvariante> listaPI;

  int contador;

  public RdP(Matriz marcadoInicial,Matriz incidenciaPrevia,Matriz incidenciaPosterior,Matriz MInvariantes){
    try{
      //if (mInicial==null) throw
      this.marcadoInicial = marcadoInicial;

      this.marcadoActual = marcadoInicial;

      this.incidenciaPrevia = incidenciaPrevia;

      //this.incidenciaPosterior = incidenciaPosterior;

      this.MInvariantes = MInvariantes;
      this.listaPI = new ArrayList<PInvariante>();
      crearListaInvariantes();
      System.out.println("Lista Invariantes");
      for(PInvariante pi: this.listaPI){
        String plazas = "";
        for(Integer i: pi.getplazas()){
          plazas =plazas+":" +i;
        }
        //System.out.println(plazas + " = " +pi.cantidadTokens(marcadoInicial));
        System.out.println(plazas + " = " +pi.getConstante());
      }


      this.incidencia = Matriz.suma(incidenciaPosterior,Matriz.porEscalar(this.incidenciaPrevia,-1));
     // System.out.println("matriz incidenciaaaa");
     // incidencia.imprimir();
     // System.out.println("");
      this.vectorSensibilizadas = Sensibilizadas(incidenciaPrevia, marcadoInicial);
      //System.out.println("vector sensiblizadas");
      //vectorSensibilizadas.imprimir();
      //System.out.println("");
      System.out.println ("transiciones iniciales");
      vectorSensibilizadas.imprimir();
      contador=0;
    }
    catch(Exception e){
    //  throw e;
    }

  }
  public Matriz getIncidencia(){
    return this.incidencia;
  }
  public Matriz getIncidenciaPrevia(){
    return this.incidenciaPrevia;
  }
  public Matriz getMInvariantes(){
    return this.MInvariantes;
  }

  public Matriz marcadoInicial(){
    return this.marcadoInicial;
  }
  public Matriz marcadoActual(){
    return this.marcadoActual;
  }


  public boolean disparar(int x) throws RdPException{
    //System.out.println("entre a disparar con " + x);
    try{
      //System.out.println("entre al try con  " + x);
      if(x<0||x>this.incidencia.getMatriz()[0].length){
        throw new RdPException("Transicion no valida.");
      }
      if(this.vectorSensibilizadas.getMatriz()[0][x]!=0){
        this.marcadoActual = Matriz.suma(this.marcadoActual,Matriz.obtenerColumna(this.incidencia,x));
        vectorSensibilizadas = Sensibilizadas(this.incidenciaPrevia,this.marcadoActual);
        //System.out.println("el hilo " + ((Hilo)(Thread.currentThread())).getNombre() + " disparo " + x);
        //vectorSensibilizadas.imprimir();
        contador++;
        System.out.println("Contador de Disparos =  " + contador);
        return true;
      }
      else{
        //System.out.println("el hilo " + ((Hilo)(Thread.currentThread())).getNombre()+ " no pudo disparar " + x);
        //System.out.println("no pude disparar por no estar sensibilizada, queda igual");
        //vectorSensibilizadas.imprimir();
        return false;
      }

    }

    catch(Exception e)
    {
      throw new RdPException(e.getMessage());
    }

  }
  public static Matriz Sensibilizadas(Matriz ip, Matriz marcado) throws RdPException{
    try{
      if(marcado.getM()!=ip.getM()){
        throw new RdPException("Matrices de distinto tamaño");
      }
      int [][] prev = ip.getMatriz();
      int [][] marc = marcado.getMatriz();
      int [][] sensibilizadas = new int [1][ip.getN()]  ;
      for(int i=0; i < ip.getN(); i++){
        int j =0;
        boolean sensible = true;
        while((j<ip.getM())&& sensible){
          if(prev[j][i]>marc[j][0]){
            sensible = false;
            sensibilizadas [0][i] = 0;
          }
          j = j+1;
          if ((j == ip.getM()-1) && sensible){
            sensibilizadas[0][i]=1;
          }
        }

        }

        return new Matriz(sensibilizadas);
    }
    catch(Exception e){
      throw new RdPException("No se ha podido obtener las transiciones disponibles");
    }


  }

  public Matriz getVectorSensibilizadas(){
    return vectorSensibilizadas;
    //return null;
  }
  public void crearListaInvariantes(){
    int [][]  arreglo = this.getMInvariantes().getMatriz();
    for (int i = 0; i <arreglo.length ; i++) {
      List<Integer> posiciones = new ArrayList<Integer>();

      for (int j = 0; j < arreglo[0].length; j++) {
        if(arreglo[i][j]!=0){
          posiciones.add(j);
        }
      }
      PInvariante pi = new PInvariante(posiciones,marcadoInicial);
      listaPI.add(pi);


    }

  }
  public List<PInvariante> getPInvariantes(){
    return this.listaPI;
  }

  public boolean verificarMarcado(Matriz marcado){
    //boolean resultado=true;
    for (PInvariante pi :
            listaPI) {
      if(!pi.verificarPInvvariante(marcado)){
        return false;
      }

    }
    return true;
  }

  public boolean verificarDisparo(Matriz anterior, Matriz actual, int disparo){   // Monitor 123
    // Verificaria, usado desde el monitor, que el cambio de marcado coincide con el que provoca el disparo
    try{

      return actual.esIgual(Matriz.suma(anterior,Matriz.obtenerColumna(this.incidencia,disparo)));

    }catch(Exception e){
      return false;
    }
  }
  public boolean transicionSensibilizada(int transición,Matriz VectorSensi ){
    if(VectorSensi.getMatriz()[0][transición]==1){
      return true;
    }
    else{
      return false;
    }
  }
  public boolean marcadoPositivo(Matriz Marcado){
    for (int i = 0; i < Marcado.getM(); i++) {
      for (int j = 0; j < Marcado.getN(); j++) {
        if(Marcado.getMatriz()[i][j]<0){
          return false;
        }

      }

    }
    return true;
  }




}
