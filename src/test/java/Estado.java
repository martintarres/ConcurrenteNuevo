/**
 * Created by YepezHinostroza on 3/12/2017.
 */
public enum Estado {
    Disponible, Bloqueado, BloqueadoConPrioridad;
    private String hilo;

    public String getHilo(){
        return hilo;
    }
    public void setHilo(String hilo){
        this.hilo = hilo;
    }


}
