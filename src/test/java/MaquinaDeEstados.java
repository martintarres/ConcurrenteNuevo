/**
 * Created by YepezHinostroza on 3/12/2017.
 */
public class MaquinaDeEstados {
    private Estado estado;

    public MaquinaDeEstados(){
        this.estado=Estado.Disponible;
        this.estado.setHilo(null);
    }
    public void desbloquear(String hilo) throws Exception{
        if(this.estado==Estado.Bloqueado&&this.estado.getHilo().equals(hilo)){
            this.estado=Estado.Disponible;
            this.estado.setHilo(null);
        }
        else{
            throw new Exception("Error");
        }
    }
    public void bloquear(String hilo) throws Exception{
        if(this.estado==Estado.Disponible){
            this.estado=Estado.Bloqueado;
            this.estado.setHilo(hilo);
        }
        else {
            throw new Exception("Error");
        }

    }
    public void despertar(String hilo) throws Exception{
        // hara falta dos atributos?
        if (this.estado==Estado.Bloqueado){
            this.estado.setHilo(hilo);
        }
        else{
            throw new Exception("Error");
        }
    }
}
