import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Monitor {
    CountDownLatch contador;
    public Semaphore mutex;
    //private Semaphore mutex;
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


    private int m;
    private Hilo hiloDesencolado;
    private Log log;
    //private int piezaA;
    //private int piezaB;
    //private int piezaC;
    private boolean cambio;
    private Politica politica;
    private int MaxBuffer;
    //private List<Object>
    private ListasDeDisparos listasDeDisparos;

    public Monitor(Constantes constantes) {
        try {
            mutex = new Semaphore(1, true);
            k = true;
            this.constantes = constantes;
            petri = new RdP(constantes.marcadoInicial, constantes.incidenciaPrevia, constantes.incidenciaPosterior, constantes.PInvariante);
            listaHilos = new ArrayList<Hilo>();
            mapa = new HashMap<Integer, Hilo>();

            VectorSensibilizados = RdP.Sensibilizadas(petri.getIncidenciaPrevia(), getPetri().marcadoActual());
            VectorEncolados = Matriz.matrizVacia(1, petri.getIncidenciaPrevia().getN());
            VectorAnd = Matriz.matrizVacia(1, getPetri().getIncidenciaPrevia().getN());
            VectorHistorialSensibilizadas = Matriz.matrizVacia(1, petri.getIncidenciaPrevia().getN());



            Vc = new ArrayList<Hilo>();

            //this.piezaA = 0;
            //this.piezaB = 0;
            //this.piezaC = 0;
            this.cambio = false;
            this.listasDeDisparos = listasDeDisparos;
            //this.politica = new PoliticaRandom(mapa,listasDeDisparos);
            this.politica = new PoliticaBardo(mapa);
            m = 0;
            this.MaxBuffer = 9;




            this.log = new Log("C:\\Users\\alexa\\Desktop\\CONCURRENTE\\ProgramacionConcurrente\\marcados.txt",
                    "C:\\Users\\alexa\\Desktop\\CONCURRENTE\\ProgramacionConcurrente\\registro.txt");
            log.limpiar();


        } catch (Exception e) {

        }

    }

    public void dispararTransicion(Integer transicion) {
        try {

            mutex.acquire();
            k = true;

            while (k == true) {

                Matriz previo = this.petri.marcadoActual().clonar();
                int Buffersize = Vc.size();
                k = petri.disparar(transicion);   // Disparo la transicion


                if (k == true) {
                    if(((Hilo) Thread.currentThread()).getContadorDisparos()%((Hilo) Thread.currentThread()).getTransiciones().size()==0){
                        assert ((Hilo) Thread.currentThread()).verificarInicio();
                    }
                    VectorSensibilizados = getPetri().vectorSensibilizadas;
                    assert unicaTransicionPorHilo(VectorSensibilizados);
                    VectorHistorialSensibilizadas.or(VectorHistorialSensibilizadas,VectorSensibilizados);

                    assert (verificarHistorialSensibilizadas());
                    if(this.getPetri().contador%500==0&&this.getPetri().contador!=0){
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
                    this.log.escribir("------------------------------------------------------------------------------------------------------------------", log.getRegistro());
                    this.log.escribir("Contador de disparos : " + this.getPetri().contador, log.getRegistro());
                    this.log.escribir(((Hilo) (Thread.currentThread())).getNombre() + " ha disparado la transicion  : " + traducirDisparo(transicion), log.getRegistro());
                    //this.log.escribir("Contador "+ this.getPetri().contador,log.getRegistro());
                    if (cambio) {
                        this.log.escribir("Cantidad de piezas producidas:  " + "A = " + politica.PiezaA + "   B = " + politica.PiezaB + "   C = " + politica.PiezaC, log.getRegistro());
                    }
                    cambio = false;

                    this.log.escribir("\n", log.getRegistro());
                    this.log.escribir("Marcado Actual : ", log.getRegistro());
                    this.log.escribir("  M1  M2  M3  M4 P10 P11 P12 P13 P14 P15 P16 P17 P18 P20 P21 P22 P23 P30 P31 P32 P33 P34 P35  R1  R2  R3  s1  s2", log.getRegistro());
                    this.log.escribir(this.getPetri().marcadoActual().toString(), log.getRegistro());
                    // this.log.escribir("----------------------------------------------------------------------",log.getRegistro());


                    this.log.escribir(this.getPetri().marcadoActual().toString(), log.getMarcados());


                    this.log.escribir("\n", log.getRegistro());
                    this.log.escribir(printHilosDeVector("Hilos Sensibilizados  =  ", VectorSensibilizados), log.getRegistro());
                    this.log.escribir("\n", log.getRegistro());
                    this.log.escribir(printHilosDeVector("Hilos Encolados  =  ", VectorEncolados), log.getRegistro());
                    this.log.escribir("\n", log.getRegistro());


                    VectorAnd.and(VectorSensibilizados, VectorEncolados);

                    if (cantidadDeUnos(VectorAnd) != 0) {

                        this.log.escribir(printHilosDeVector("Hilos en ambas  =  ", VectorAnd), log.getRegistro());
                        this.log.escribir("\n", log.getRegistro());


                        Integer locker = politica.getLock(VectorAnd);
                        VectorEncolados.getMatriz()[0][locker]=0;
                        synchronized (locker) {
                            assert BufferOverflow();
                            Vc.remove(mapa.get(locker));
                            locker.notify();

                        }

                        return;
                    } else {

                        k = false;
                    }

                } else {
                    //assert (false);
                    assert(previo.esIgual(getPetri().marcadoActual()));
                    Vc.add((Hilo) Thread.currentThread());
                    assert (Vc.get(Vc.size()-1).equals((Hilo)Thread.currentThread()));
                    //System.out.println("Hilos encolados: " + Vc);
                    assert BufferOverflow();
                    assert (cantidadDeUnos(VectorEncolados)<MaxBuffer);
                    assert encoladosRepetidos();
                    VectorEncolados.getMatriz()[0][transicion]=1;
                    assert (Buffersize + 1 == cantidadDeUnos(VectorEncolados));
                    mutex.release();
                    assert unicaTransicionPorHilo(VectorEncolados);
                    assert (((Hilo) Thread.currentThread()).verificarTransicionDormida(VectorEncolados,mapa));

                    synchronized (transicion) {
                        transicion.wait();

                    }
                }


            }

            mutex.release();


        } catch (Exception e) {
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
