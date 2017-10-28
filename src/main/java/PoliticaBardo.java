import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by YepezHinostroza on 25/10/2017.
 */
public class PoliticaBardo extends Politica {
    public PoliticaBardo(Map<Integer,Hilo> mapa){
        super(mapa);



    }


    public Integer getLock(Matriz VectorAnd) {



        if((this.PiezaB/50)<(this.PiezaC/50)*2){
            this.secuencia=this.preferenciaB;
        }
        else{
            this.secuencia=this.equilibrio;
        }

        for (int i = 0; i < this.secuencia.length; i++) {
            if(VectorAnd.getMatriz()[0][secuencia[i]]!=0){
                return getInteger(secuencia[i]);
            }

        }
        System.err.println("No se ha seleccionado ninguno hilo del vector AND");
        return getInteger(0);


        //return -1;




    }
}
