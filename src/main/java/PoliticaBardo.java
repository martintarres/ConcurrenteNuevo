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


    public Integer getLock(Matriz VectorAnd){
        int [] secuencias={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};

        for (int i = 0; i < secuencias.length; i++) {
            if(VectorAnd.getMatriz()[0][secuencias[i]]!=0){
                return getInteger(secuencias[i]);
            }

        }

        System.err.println("---------------------------------------------------");
        return -1;


    }
}
