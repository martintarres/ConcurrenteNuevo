import java.util.*;
import java.util.concurrent.Semaphore;

public class Monitor {
    public Semaphore mutex;
    private boolean k;
    public RdP petri;
    private List<Hilo> listaHilos;
    private Map<Integer, Hilo> mapa;
    private Constantes constantes;
    private Matriz VectorSensibilizados;
    private Matriz VectorEncolados;
    private Matriz VectorAnd;
    private Matriz VectorHistorialSensibilizadas;
    private List<Hilo> Vc;  // Lista de hilos encolados porque sus transiciones no estaban sensibilizadas

    private Hilo hiloDespertado;
    private Log log;
    private static boolean cambio;
    private Politica politica;
    private int MaxBuffer;
    private ListasDeDisparos listasDeDisparos;
    private boolean prioridadDespertado;
    private boolean modoVerborragico;

    public Monitor(Constantes constantes,int pol) {
        int m;
        try {
            mutex = new Semaphore(1, true);
            k = true;
            this.constantes = constantes;
            //petri = new RdP(constantes.marcadoInicial, constantes.incidenciaPrevia, constantes.incidenciaPosterior, constantes.PInvariante);
            petri = new RdP(constantes);
            listaHilos = new ArrayList<Hilo>();
            mapa = new HashMap<Integer, Hilo>();

            VectorSensibilizados = RdP.Sensibilizadas(petri.getIncidenciaPrevia(), getPetri().marcadoActual());
            VectorEncolados = Matriz.matrizVacia(1, petri.getIncidenciaPrevia().getN());
            VectorAnd = Matriz.matrizVacia(1, getPetri().getIncidenciaPrevia().getN());
            VectorHistorialSensibilizadas = Matriz.matrizVacia(1, petri.getIncidenciaPrevia().getN());
            Vc = new ArrayList<Hilo>();
            this.cambio = false;
            this.listasDeDisparos = listasDeDisparos;
            //this.politica = new PoliticaRandom(mapa,listasDeDisparos);
            modoVerborragico=false;

            if(pol==1){
                this.politica = new Politica1A2B1C(mapa);

            }
            else{
                if(pol==2){
                    this.politica = new Politica3A2B1C(mapa);
                }
                else{
                    this.politica = new PoliticaRandom(mapa);
                    this.modoVerborragico=true;
                }

            }

            m = 0;
            this.MaxBuffer = 9;
            this.prioridadDespertado=false;
            this.hiloDespertado=null;

            this.log = new Log("C:\\Users\\alexa\\Desktop\\ConcurrenteNuevo\\marcados.txt",
                    "C:\\Users\\alexa\\Desktop\\ConcurrenteNuevo\\registro.txt");
            log.limpiar();


        } catch (Exception e) {
            System.err.println("No se ha podido inicializar el monitor");

        }

    }

    public  void dispararTransicion(Integer transicion) {
        try {
            //synchronized (mutex)
            this.log.escribir(((Hilo)(Thread.currentThread())).getNombre()+ "  pide el mutex.",this.log.getRegistro());
            if(modoVerborragico){
                System.out.println(((Hilo)(Thread.currentThread())).getNombre()+ "  pide el mutex.");
            }

            mutex.acquire();
            k = true;
            this.log.escribir(((Hilo)(Thread.currentThread())).getNombre()+ "  obtiene el mutex.",this.log.getRegistro());
            this.log.escribir(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", this.log.getRegistro());


            while (k == true) {
                if(modoVerborragico){
                    System.out.println(((Hilo)(Thread.currentThread())).getNombre()+ "  obtiene el mutex.");
                }
                if (prioridadDespertado) {
                    assert (Thread.currentThread() == hiloDespertado);
                    this.prioridadDespertado = false;
                }
                Hilo actual = (Hilo) (Thread.currentThread());
                Matriz previo = this.petri.marcadoActual().clonar();
                Matriz SensiPrevio = this.VectorSensibilizados.clonar();
                int Buffersize = Vc.size();
                k = petri.disparar(transicion);   // Disparo la transicion


                if (k == true) {
                    //this.log.escribir(((Hilo)(Thread.currentThread())).getNombre()+ "  dispara la transicion  "+traducirDisparo(transicion),this.log.getRegistro());
                    if(modoVerborragico){
                        System.out.println(((Hilo)(Thread.currentThread())).getNombre()+ "  dispara la transicion  "+traducirDisparo(transicion) );
                    }
                    if (((Hilo) Thread.currentThread()).getContadorDisparos() % ((Hilo) Thread.currentThread()).getTransiciones().size() == 0) {
//                        assert ((Hilo) Thread.currentThread()).verificarInicio();
                    }
                    assert(this.getPetri().transicionSensibilizada(transicion,SensiPrevio));
                    assert (this.getPetri().marcadoPositivo(this.getPetri().marcadoActual()));

                    VectorSensibilizados = getPetri().vectorSensibilizadas;
                    assert unicaTransicionPorHilo(VectorSensibilizados);
                    VectorHistorialSensibilizadas.or(VectorHistorialSensibilizadas, VectorSensibilizados);

                    assert (verificarHistorialSensibilizadas());
                    if (this.getPetri().contador % 500 == 0 && this.getPetri().contador != 0) {
                        this.VectorHistorialSensibilizadas = Matriz.matrizVacia(1, petri.getIncidenciaPrevia().getN());
                    }


                    assert ((Hilo) (Thread.currentThread())).verificarVueltas();
                    assert ((Hilo) Thread.currentThread()).verificarSecuenciaT(transicion);
                    ((Hilo) Thread.currentThread()).incrementarContador();
                    assert this.getPetri().verificarDisparo(previo, this.petri.marcadoActual(), transicion);
                    assert (Buffersize == Vc.size());

                    cambio = false;
                    if (transicion == 9) {
                        politica.PiezaA++;
                        cambio = true;
                    }
                    if (transicion == 13) {
                        politica.PiezaB++;
                        cambio = true;
                    }
                    if (transicion == 19) {
                        politica.PiezaC++;
                        cambio = true;
                    }
                    log.registrarBasico(this, transicion, true);
                    //this.log.escribir("Contador "+ this.getPetri().contador,log.getRegistro());
                    if (cambio) {
                        this.log.escribir("Cantidad de piezas producidas:  " + "A = " + politica.PiezaA + "   B = " + politica.PiezaB + "   C = " + politica.PiezaC, log.getRegistro());
                        politica.actualizarVista();
                    }
                    cambio = false;

                    log.registrarBasico2(this, VectorSensibilizados, VectorEncolados);

                    VectorAnd.and(VectorSensibilizados, VectorEncolados);
                    //VectorAnd.getMatriz()[0][10]=0;
                    //VectorAnd.getMatriz()[0][14]=0;
                    //System.out.println(actual.getNombre() + "verifica and");

                    if (politica.hayAlguienParaDespertar(VectorAnd)) {

                        Integer locker = politica.getLock(VectorAnd);
                        int t = locker.intValue();
                        VectorEncolados.getMatriz()[0][t] = 0;
                        assert (VectorEncolados.getMatriz()[0][t] == 0);
                        this.hiloDespertado = mapa.get(locker);
                        if(modoVerborragico){
                            System.out.println(((Hilo)(Thread.currentThread())).getNombre()+ "  despierta al hilo "+ this.hiloDespertado.getNombre());
                        }
                        log.registrarEXtendido(this, VectorAnd, mapa.get(locker));

                        assert BufferOverflow();
                        Vc.remove(mapa.get(locker));
                        //System.out.println(mapa.get(locker).getState()+"   "+mapa.get(locker).getNombre());
                        //assert (mapa.get(locker).getState()==Thread.State.WAITING);

                        while (mapa.get(locker).getState() != Thread.State.WAITING) {
                            Thread.currentThread().sleep(1);
                            System.err.println("Esperando que se duerma para despertarlo : "+mapa.get(locker).getNombre());
                            //System.out.println(mapa.get(locker).getState() + "   " + mapa.get(locker).getNombre());
                        }


                        synchronized (locker) {


                            locker.notifyAll();
                            if(modoVerborragico){
                                System.out.println(((Hilo)(Thread.currentThread())).getNombre()+ "  sale del monitor");
                                this.log.escribir("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<", this.log.getRegistro());
//
                            }
                            return;

                            //assert (mapa.get(locker).getState()==Thread.State.);


                        }



                        //return;
                    } else {

                        k = false;
                    }

                } else {
                    VectorEncolados.getMatriz()[0][transicion] = 1;
                    Vc.add((Hilo) Thread.currentThread());


                    if(modoVerborragico){
                        System.out.println(((Hilo)(Thread.currentThread())).getNombre()+ "  no pudo disparar la transicion  "+ traducirDisparo(transicion));
                    }
                    log.registrarBasico(this, transicion, false);
                    log.registrarBasico2(this, VectorSensibilizados, VectorEncolados);

                    assert(!this.getPetri().transicionSensibilizada(transicion,SensiPrevio));
                    //assert (false);
                    assert (previo.esIgual(getPetri().marcadoActual()));
                    Vc.add((Hilo) Thread.currentThread());
                    assert (Vc.get(Vc.size() - 1).equals((Hilo) Thread.currentThread()));
                    //System.out.println("Hilos encolados: " + Vc);
                    assert BufferOverflow();
                    assert (cantidadDeUnos(VectorEncolados) < MaxBuffer);
                    assert encoladosRepetidos();

                    assert (Buffersize + 1 == cantidadDeUnos(VectorEncolados));

                    assert unicaTransicionPorHilo(VectorEncolados);
                    assert (((Hilo) Thread.currentThread()).verificarTransicionDormida(VectorEncolados, mapa));

                    synchronized (transicion) {
                        if(modoVerborragico){
                            System.out.println(((Hilo)(Thread.currentThread())).getNombre()+ "  devuelve el mutex");
                        }
                        this.log.escribir(((Hilo)(Thread.currentThread())).getNombre()+ "  devuelve el mutex", this.log.getRegistro());

                        this.log.escribir("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<", this.log.getRegistro());

                        mutex.release();
                        if(modoVerborragico){
                            System.out.println(((Hilo)(Thread.currentThread())).getNombre()+ "  procede a dormir");
                        }
                        transicion.wait();


                    }
                }


            }
            if(modoVerborragico){
                System.out.println(((Hilo)(Thread.currentThread())).getNombre()+ "  devuelve el mutex");
            }
            this.log.escribir(((Hilo)(Thread.currentThread())).getNombre()+ "  devuelve el mutex",this.log.getRegistro());
            this.log.escribir("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<", this.log.getRegistro());



            mutex.release();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage() + "laralara");
        }
    }


    public void setHilos(Hilo hilo) {
        listaHilos.add(hilo);
    }


    public List<Hilo> getHilosSensibilizados() {
        List<Hilo> lista = new ArrayList<Hilo>();
        int[][] array = petri.getVectorSensibilizadas().getMatriz();
        for (int i = 0; i < petri.getVectorSensibilizadas().getN(); i++) {
            if (array[0][i] != 0) {
                if (mapa.containsKey(i) && !lista.contains(mapa.get(i))) {
                    lista.add(mapa.get(i));
                }
            }
        }
        return lista;
    }

    /*public void getHilos(){

      System.out.println("voy a mostrar todos los hilos del programa");
      for(Hilo t : listaHilos){
         // System.out.println("soy hilo " + t.getNombre());
          System.out.println("soy transiciones del hilo " + t.getNombre()+ t.getTransiciones());
      }
    }
  */

    public void mapeo(Hilo hilo) {
        for (Integer i : hilo.getTransiciones()) {
            this.mapa.put(i, hilo);
        }
    }

    public void showMapa() {
        System.out.println("Mapa de Transiciones e Hilos");
        for (Integer i : this.mapa.keySet()) {
            System.out.println("Transicion " + i + " correspondiente al hilo  " + this.mapa.get(i));
        }
    }

    public void showHilos() {
        for (Hilo h :
                mapa.values()) {
            System.out.println(h.getNombre() + " = " + h.anteriores + " | " + h.posteriores);

        }
    }

    /*
    public List<Hilo> and() {
        List<Hilo> and = new ArrayList<Hilo>();
        try {
            for (Hilo s : Vc) {
                for (Hilo c : Vs) {
                    if (s.equals(c) && !(and.contains(s))) {
                        and.add(s);

                    }
                }
            }
            return and;

        } catch (Exception e) {
            return new ArrayList<Hilo>();
        }
    }
    */

    public RdP getPetri() {
        return this.petri;
    }

    public String traducirDisparo(int i) {
        String transicion = constantes.nombreTransiciones[i];
        return transicion;

    }

    public boolean BufferOverflow() {    //231
    /*
    //Verifica que no haya más hilos encolados de los que se inicializaron
    //También podría veriicar que al menos uno esté vivo al compararlo con this.MaxBuffer -1
     */
        return this.Vc.size() < this.MaxBuffer;
    }

    public boolean encoladosRepetidos() {    //232

        for (Hilo h : this.Vc) {
            int cantidad = 0;
            for (Hilo r : Vc) {
                if (h.equals(r)) {
                    cantidad++;
                }

            }
            if (cantidad != 1) {
                return false;
            }
        }
        return true;
    }

    public Hilo buscarHilo(String nombre) {
        for (Hilo h : mapa.values()) {
            if (h.getNombre().equals(nombre)) {
                return h;
            }

        }
        return null;
    }

    public int cantidadDeUnos(Matriz A) {
        int cantidad = 0;
        int[][] arreglo = A.getMatriz();
        for (int i = 0; i < A.getN(); i++) {
            if (arreglo[0][i] != 0) {
                cantidad++;
            }

        }
        return cantidad;
    }

    public String printHilosDeVector(String inicio, Matriz Vector) {
        String cadena = inicio;
        for (int i = 0; i < Vector.getN(); i++) {
            if (Vector.getMatriz()[0][i] != 0) {
                cadena = cadena + mapa.get(i).getNombre();
                cadena = cadena + " || ";
            }
        }
        return cadena;

    }

    public boolean unicaTransicionPorHilo(Matriz Vector){

        for (int i = 0; i < Vector.getN(); i++) {
            if(Vector.getMatriz()[0][i]==1){
                Hilo h = mapa.get(i);
                int cantidad = 0;
                for (int j = 0; j < Vector.getN(); j++) {
                    if(Vector.getMatriz()[0][j]!=0&&mapa.get(j)==h){
                        cantidad++;
                    }

                }
                if(cantidad!=1){
                    return false;
                }

            }

        }
        return true;
    }

    public boolean verificarHistorialSensibilizadas(){

        return true;

/*
        if(this.getPetri().contador%500==0&&this.getPetri().contador!=0){
            System.out.println(VectorHistorialSensibilizadas);
            return (cantidadDeUnos(VectorHistorialSensibilizadas)==VectorHistorialSensibilizadas.getN());
        }
        else return true;
*/
    }


    public void setearAntPost() {
        for (Hilo h : this.mapa.values()) {
            switch (h.getNombre()) {
                case "Hilo 1":
                    h.agregarPosterior(buscarHilo("Hilo 2"));
                    break;

                case "Hilo 2":
                    h.agregarAnterior(buscarHilo("Hilo 1"));
                    break;

                case "Hilo 3":
                    h.agregarPosterior(buscarHilo("Hilo 4"));
                    h.agregarPosterior(buscarHilo("Hilo 6"));
                    break;

                case "Hilo 4":
                    h.agregarAnterior(buscarHilo("Hilo 3"));
                    h.agregarPosterior(buscarHilo("Hilo 5"));
                    break;

                case "Hilo 5":
                    h.agregarAnterior(buscarHilo("Hilo 4"));
                    h.agregarPosterior(buscarHilo("Hilo 7"));
                    break;

                case "Hilo 6":
                    h.agregarAnterior(buscarHilo("Hilo 3"));
                    h.agregarPosterior(buscarHilo("Hilo 7"));
                    break;

                case "Hilo 7":
                    h.agregarAnterior(buscarHilo("Hilo 5"));
                    h.agregarAnterior(buscarHilo("Hilo 6"));
                    break;

                case "Hilo 8":
                    h.agregarAnterior(buscarHilo("Hilo 9"));
                    break;

                case "Hilo 9":
                    h.agregarPosterior(buscarHilo("Hilo 8"));
                    break;

                default:
                    break;


            }

        }
    }


}
