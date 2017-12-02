import java.io.File;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Monitor {
    public Semaphore mutex;
    private boolean k;
    private RdP petri;
    private List<Hilo> listaHilos;
    private Map<Integer, Hilo> mapa;
    private Constantes constantes;
    private Matriz VectorSensibilizados;
    private Matriz VectorEncolados;
    private Matriz VectorAnd;
    private Log log;
    private static boolean cambio;
    private Politica politica;

    private List<Hilo> Vc;


    public Monitor(Constantes constantes, int pol) {

        try {
            mutex = new Semaphore(1, true);
            k = true;
            this.constantes = constantes;

            petri = new RdP(constantes);
            listaHilos = new ArrayList<Hilo>();
            mapa = new HashMap<Integer, Hilo>();

            VectorSensibilizados = RdP.Sensibilizadas(petri.getIncidenciaPrevia(), getPetri().marcadoActual());
            VectorEncolados = Matriz.matrizVacia(1, petri.getIncidenciaPrevia().getN());
            VectorAnd = Matriz.matrizVacia(1, getPetri().getIncidenciaPrevia().getN());
            Vc = new ArrayList<Hilo>();
            this.cambio = false;
            if (pol == 1) {
                this.politica = new Politica1A2B1C(mapa);

            } else {
                if (pol == 2) {
                    this.politica = new Politica3A2B1C(mapa);
                } else {
                    this.politica = new PoliticaRandom(mapa);
                }

            }
            final String file = "";
            final String path = (new File(".")).getCanonicalPath();
            final String invariantesregistro = "/registro.txt";


            this.log = new Log(file + path + invariantesregistro, constantes);
            log.limpiar();

        } catch (Exception e) {
            System.err.println("No se ha podido inicializar el monitor");

        }

    }

    public void dispararTransicion(Integer transicion) {
        try {
            //synchronized (mutex)
            this.log.escribir(((Hilo) (Thread.currentThread())).getNombre() + "  pide el mutex.", this.log.getRegistro());

            mutex.acquire();
            k = true;
            this.log.escribir(((Hilo) (Thread.currentThread())).getNombre() + "  obtiene el mutex.", this.log.getRegistro());
            this.log.escribir(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", this.log.getRegistro());


            while (k == true) {

                k = petri.disparar(transicion);


                if (k == true) {
                    VectorSensibilizados = getPetri().vectorSensibilizadas;
// que no ande con politica 3
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
                    //log.registrarBasico(this, transicion, true);
                    if (cambio) {
                        this.log.escribir("Cantidad de piezas producidas:  " + "A = " + politica.PiezaA + "   B = " + politica.PiezaB + "   C = " + politica.PiezaC, log.getRegistro());
                        politica.actualizarVista();
                    }
                    cambio = false;

                    //this.log.registrarBasico2(this, VectorSensibilizados, VectorEncolados);

                    VectorAnd.and(VectorSensibilizados, VectorEncolados);

                    if (politica.hayAlguienParaDespertar(VectorAnd)) {

                        Integer locker = politica.getLock(VectorAnd);
                        int t = locker.intValue();
                        this.log.registrar(this, transicion, true, mapa.get(locker));
                        VectorEncolados.getMatriz()[0][t] = 0;

                        //log.registrarEXtendido(this, VectorAnd, mapa.get(locker));
                        while (mapa.get(locker).getState() != Thread.State.WAITING) {
                            Thread.currentThread().sleep(1);
                            System.err.println("Esperando que se duerma para despertarlo : " + mapa.get(locker).getNombre());
                        }
//////
                        //this.log.registrar(this,transicion,true,mapa.get(locker));
                        synchronized (locker) {
                            locker.notifyAll();
                            return;
                        }
                    } else {
///
                        this.log.registrar(this, transicion, true, null);
                        //log.registrarEXtendido(this, VectorAnd, null);

                        k = false;
                    }

                } else {
                    //log.registrarEXtendido(this, VectorAnd, null);
                    VectorEncolados.getMatriz()[0][transicion] = 1;
                    //log.registrarBasico(this, transicion, false);
                    //log.registrarBasico2(this, VectorSensibilizados, VectorEncolados);
////
                    this.log.registrar(this, transicion, false, null);
                    synchronized (transicion) {
                        this.log.escribir(((Hilo) (Thread.currentThread())).getNombre() + "  devuelve el mutex", this.log.getRegistro());
                        this.log.escribir("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<", this.log.getRegistro());
                        mutex.release();
                        transicion.wait();
                    }
                }


            }
            this.log.escribir(((Hilo) (Thread.currentThread())).getNombre() + "  devuelve el mutex", this.log.getRegistro());
            this.log.escribir("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<", this.log.getRegistro());


            mutex.release();


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage() + "----");
        }
    }


    public void mapeo(Hilo hilo) {
        for (Integer i : hilo.getTransiciones()) {
            this.mapa.put(i, hilo);
        }
        this.log.registrarHilo(hilo);
    }

    public RdP getPetri() {
        return this.petri;
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

    public Matriz getVectorEncolados() {
        return this.VectorEncolados;
    }

    public Matriz getVectorAnd() {
        return this.VectorAnd;
    }

    public Matriz getVectorSensibilizados() {
        return this.VectorSensibilizados;
    }
}
