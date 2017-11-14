import java.util.List;

/**
 * Created by YepezHinostroza on 30/8/2017.
 */
public class PInvariante {
      private final int  constante;
      private List<Integer> plazas;

      public PInvariante(List<Integer> plazas,Matriz inicial ){
          this.plazas=plazas;
          this.constante= cantidadTokens(inicial);
      }
      public List<Integer> getplazas(){
          return this.plazas;
      }
      public int getConstante(){
          return this.constante;
      }

      public int cantidadTokens(Matriz marcado){
          int sumatoria = 0;
          for(Integer i : plazas){
              sumatoria = sumatoria+ marcado.getMatriz()[i][0];
          }


          return sumatoria;

      }
      public boolean verificarPInvvariante(Matriz marcado){
          return this.getConstante()==cantidadTokens(marcado);
      }
}
