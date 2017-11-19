import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Hilo extends Thread {

  final String nombre;
  final Monitor monitor;
  final List<Integer> enteros;

  private int contadorDisparos;
  public List<Hilo> anteriores;
  public List<Hilo> posteriores;

  public Hilo(String nombre, List <Integer> enteros, Monitor monitor){
    this.nombre = nombre;
    this.monitor = monitor;
    this.enteros=enteros;
    //lock = new Object();
    this.contadorDisparos = 0;
    this.posteriores = new ArrayList<Hilo>();
    this.anteriores = new ArrayList<Hilo>();

  }

  public String getNombre(){
    return this.nombre;
  }

  public void run(){
    try{
      while(true){

        for(Integer i : enteros)
        {
          monitor.dispararTransicion(i);
        }

      }

    }catch(Exception e){
      System.err.println(e.getMessage());
    }
   // System.out.println("Soy hilo "+ nombre);


  }

  public List<Integer> getTransiciones(){
      return enteros;
  }
  /*public Object getLock(){
    return this.lock;
  }
  */

  public  void incrementarContador(){
    this.contadorDisparos++;
  }
  public int getContadorDisparos(){
    return this.contadorDisparos;
  }
  public boolean verificarSecuenciaT(final int disparada){    //120 de Monitor
    /*
    // Verifica que la lista de enteros del hilo se ejecute en la secuencia fijada
    // @disparada  entero que representa la última trasición que se disparó de ese hilo
    // Usa el operador Resto para conocer qué posición de la lista de enteros se tuvo que disparar
     */
    return disparada==this.getTransiciones().get(this.contadorDisparos % this.getTransiciones().size());

  }
  public void agregarALista(List<Hilo> lista, Hilo h){
      if(!lista.contains(h)){
          lista.add(h);
      }
  }
  public void agregarAnterior(final Hilo h){
      agregarALista(this.anteriores,h);
  }
  public void agregarPosterior(Hilo h){
        agregarALista(this.posteriores,h);
  }

  public boolean verificarVueltas(){
      // Tengo que dejar las drogas y aflojarle a los test flasheros
      int vueltasAnteriores =0;
      int vueltasParalelas =0;
      List<Hilo> leidos = new ArrayList<Hilo>();
      for (Hilo ant : this.anteriores) {
          vueltasAnteriores=vueltasAnteriores+ant.getContadorDisparos()/ant.getTransiciones().size();
          for (Hilo post:
               ant.posteriores) {
              if(!leidos.contains(post)){
                  vueltasParalelas=vueltasParalelas+post.getContadorDisparos()/post.getTransiciones().size();
                  leidos.add(post);
              }



          }

      }
      //System.out.println("Hilo =" + ((Hilo)Thread.currentThread()).getNombre());
      //System.out.println("Vueltas anteriores = "+ vueltasAnteriores+"    Vueltas Paralelas =  "+ vueltasParalelas);
      return vueltasAnteriores>=vueltasParalelas;
  }

  public boolean verificarInicio(){

      if(this.anteriores.size()==0){
          return true;
      }
      else{
          for (Hilo h:
               this.anteriores) {
              if(h.getContadorDisparos()%h.getTransiciones().size()==0){
                  return true;
              }


          }
          return false;
      }
  }

  public boolean verificarTransicionDormida(Matriz VectorEncolados, Map<Integer,Hilo> mapa){
      for (int i = 0; i < VectorEncolados.getN(); i++) {
          if(VectorEncolados.getMatriz()[0][i]==1){
              Hilo h = mapa.get(i);
              if(h.getTransiciones().get(h.getContadorDisparos()%h.getTransiciones().size())!=i){
                  return false;
              }
          }
      }
      return true;

  }



}
