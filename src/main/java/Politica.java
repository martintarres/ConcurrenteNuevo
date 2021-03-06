import java.util.Map;

/**
 * Created by YepezHinostroza on 31/8/2017.
 */
public abstract class Politica {
    protected Map<Integer,Hilo> mapa;
    //protected List<Integer> abc,acb,bac,bca,cba,cab;
    public int PiezaA;
    public int PiezaB;
    public int PiezaC;
    protected int [] secuencia;
    protected int [] equilibrio = {14,15,16,17,18,19,0,1,2,3,4,5,6,7,8,9,10,11,12,13};
    protected int [] preferenciaB = {10,11,12,13,0,1,2,3,4,5,6,7,8,9,14,15,16,17,18,19};
    protected int [] preferenciaA = {11,12,13,0,1,2,3,4,5,6,7,8,9,15,16,17,18,19};
    protected Vista v;



    public Politica(Map<Integer,Hilo> mapa){

        this.mapa = mapa;
        this.PiezaA=0;
        this.PiezaB=0;
        this.PiezaC=0;
        try {
            Vista ventana = new Vista(this);
            this.v = ventana;
            ventana.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public abstract Integer getLock(Matriz VectorAnd);

    public Integer getInteger(int entero){
        for (Integer i: mapa.keySet()) {
            if (i.intValue()==entero){
                return i;
            }
        }
        //Deberpia tirar una excepción por si falla, pero me la soba a estas alturas
        return null;
    }

    public void actualizarVista(){
        v.repintar();
        v.repaint();

    }
    public abstract boolean hayAlguienParaDespertar(Matriz And);


}
