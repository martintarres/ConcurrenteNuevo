/**
 * Created by Fabrizio_p on 30/08/2017.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Log {
    private File registro;
    final String direccionRegistro;
    private BufferedReader br;
    private BufferedWriter bw;
    private Constantes constantes;
    private String lineaMarcados;


    public Log(final String registro, Constantes constantes){

        this.direccionRegistro = registro;
        this.registro = new File(registro);
        this.constantes = constantes;
        this.lineaMarcados=lineaMarcados();

    }
    public List<String> leerLineas(){
        List<String> lineasLeidas;
        lineasLeidas = new ArrayList<String>();

        try{
          FileReader fr = new FileReader(this.registro);
          br = new BufferedReader(fr);
          String linea;
            while((linea=br.readLine())!=null){
                // Me agregaba una línea con un string vacio sino
                if(linea.length()!=0) {
                    lineasLeidas.add(linea);
                }
            }
            return lineasLeidas;

        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        finally {
            try{
                br.close();

            }
            catch(Exception e){

            }
            return lineasLeidas;
        }
    }
    public synchronized void  escribir(final String linea,final File destino){
        try{
            FileWriter fw = new FileWriter(destino,true);
            //fw.write(linea);

            //pw = new PrintWriter(fw);
            //pw.println(linea);
            bw = new BufferedWriter(fw);
            bw.write(linea);
            //bw.write("\n");
           bw.newLine();
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        finally {
            try{
                bw.close();
                //pw.close();
            }
            catch(Exception e){
                return;
            }

        }


    }
    public File getRegistro(){
        return this.registro;
    }

    public  void limpiar(){
        if(this.registro.exists())
            this.registro.delete();
        this.registro=new File(direccionRegistro);
    }
    public Matriz getMarcado(final String linea){
        List<Integer> enteros = new ArrayList<Integer>();
        String[] partes = linea.split(" ");
        for(String parte: partes){
            if(Constantes.esNumero(parte)){
                enteros.add(Integer.parseInt(parte));
            }
        }
        int[][] arreglo = new int[enteros.size()][1];
        for (int i = 0; i < arreglo.length; i++) {
            arreglo[i][0]=enteros.get(0);
            enteros.remove(0);

        }
        try{
            Matriz marcadoActual = new Matriz(arreglo);
            //System.out.println("Matriz Marcado Actual: ");
            //marcadoActual.transpuesta().imprimir();
            return marcadoActual;
        }catch(Exception e){
            return null;
        }
    }
    public String traducirDisparo(int i) {
        String transicion = constantes.nombreTransiciones[i];
        return transicion;

    }
    public int traducirTransicion(String transicion) throws Exception {

        for (int i = 0; i < constantes.nombreTransiciones.length; i++) {
            if (transicion.equals(constantes.nombreTransiciones[i].trim())) {
                return i;
            }

        }
        throw new Exception("No se ha encontrado dicho nombre de transicion" + transicion);

    }

    public synchronized void registrarBasico(final Monitor m, final int transicion,boolean bool){
        //escribir("------------------------------------------------------------------------------------------------------------------", this.getRegistro());
        escribir("\n", this.getRegistro());
        escribir("Contador de disparos : " + m.getPetri().contador, this.getRegistro());
        escribir("\n", this.getRegistro());
        String cadena;
        if(bool){
            cadena= "  ha disparado la transicion  : ";
        }
        else {
            cadena = "  no ha podido disparar la transicion  : ";
        }
        escribir(((Hilo) (Thread.currentThread())).getNombre() + cadena + traducirDisparo(transicion), this.getRegistro());


    }
    public synchronized void registrarBasico2(final Monitor m, final Matriz sensi, final Matriz enco){
        escribir("\n", this.getRegistro());
        escribir("Marcado Actual : ", this.getRegistro());
        escribir(this.lineaMarcados(), this.getRegistro());
        //escribir("  M1  M2  M3  M4 P10 P11 P12 P13 P14 P15 P16 P17 P18 P20 P21 P22 P23 P30 P31 P32 P33 P34 P35  R1  R2  R3  s1  s2", this.getRegistro());
        escribir(m.getPetri().marcadoActual().toString()+"\n", this.getRegistro());
        // this.log.escribir("----------------------------------------------------------------------",log.getRegistro());


        escribir("\n", this.getRegistro());
        escribir(m.printHilosDeVector("Hilos Sensibilizados  =  ", sensi), this.getRegistro());
        escribir("\n", this.getRegistro());
        escribir(m.printHilosDeVector("Hilos Encolados  =  ", enco), this.getRegistro());
        escribir("\n", this.getRegistro());


    }


    public void registrarEXtendido(Monitor m,Matriz and, Hilo h){
        escribir(m.printHilosDeVector("Hilos en ambas  =  ", and), this.getRegistro());
        escribir("\n", this.getRegistro());
        escribir("Hilo despertado  =  "+ h.getNombre(), this.getRegistro());
        escribir("\n", this.getRegistro());


    }


    public List<String> extraerLineas(String coincidencia, int desfasaje){
        List<String> lineas = new ArrayList<String>();
        List<String> lineasALeer = leerLineas();
        for (int i = 0; i < lineasALeer.size(); i++) {
            if (lineasALeer.get(i).contains(coincidencia)) {
                lineas.add(lineasALeer.get(i+desfasaje));
            }

        }

        return lineas;
    }
    public List<String> extraerMarcados(){
        return extraerLineas(this.getLineaMarcados(),1);
    }

    public Matriz convertirMarcado(String Linea) throws Exception {
        try {
            List<Integer> enteros = new ArrayList<>();
            String[] casteado = Linea.split(" ");
            for (String numero :
                    casteado) {
                if (constantes.esNumero(numero)) {
                    enteros.add(Integer.parseInt(numero));
                }
            }

            int[][] arreglo = new int[1][enteros.size()];
            for (int i = 0; i < enteros.size(); i++) {
                arreglo[0][i] = enteros.get(i);

            }
            Matriz marcado = new Matriz(arreglo);
            return marcado;

        } catch (Exception e) {
            System.err.println("No se ha podido crear la matriz");
            return null;

        }

    }
    public List<Matriz> getHistorialMarcados(){
        List<Matriz> marcados = new ArrayList<Matriz>();
        List<String> lineas= extraerMarcados();
        try{

            for (String linea:
                    lineas) {
                marcados.add(this.convertirMarcado(linea).transpuesta());
            }

        }catch(Exception e){

        }

        return marcados;

    }
    public String lineaMarcados(){
        String cadena = "";

        for (int i = 0; i <constantes.nombreMarcados.length; i++) {
            String campo = constantes.nombreMarcados[i];

            while(campo.length()<4){
                campo= " "+campo;
            }
            cadena=cadena+campo;

        }

        return cadena;
    }
    public String getLineaMarcados(){
        return this.lineaMarcados;
    }

    public List<String> extraerDisparos(){
        List<String> lineas = new ArrayList<String>();
        List<String> lineasALeer = leerLineas();
        for (int i = 0; i < lineasALeer.size(); i++) {
            if (lineasALeer.get(i).contains("no ha podido disparar la transicion")||
                    lineasALeer.get(i).contains("ha disparado la transicion  :")) {
                lineas.add(lineasALeer.get(i));
            }

        }
        return lineas;
    }

    public List<Integer> getHistorialDisparos(){
        List<String> lineasDisparos= extraerDisparos();
        List<Integer> disparos = new ArrayList<Integer>();
        String [] casteado;
        for (int i = 0; i < lineasDisparos.size(); i++) {
            casteado = lineasDisparos.get(i).split(":");
            try{
                disparos.add(traducirTransicion(casteado[1].trim()));
            }
            catch (Exception e){
                System.err.println(e.getMessage());
            }

        }
        return disparos;
    }
    public List<Boolean> getHistorialEstadoDisparos(){
        List<String> lineasDisparos= extraerDisparos();
        List<Boolean> estados = new ArrayList<Boolean>();
        String [] casteado;
        for (int i = 0; i < lineasDisparos.size(); i++) {
            if(lineasDisparos.get(i).contains("no")){
                estados.add(false);
            }
            else{
                estados.add(true);
            }

        }
        return estados;
    }
    public List<String> getHistorialHilos(){
        List<String> hilos = new ArrayList<String>();
        List<String> lineasALeer = this.extraerDisparos();
        for (String s:
             lineasALeer) {
            String [] cast;
            String hilo="";
            if (s.contains("no")){
                cast = s.split("no");
            }
            else{
                cast = s.split("ha disparado");
            }
            hilo=cast[0].trim();
            hilos.add(hilo);
        }
        return hilos;
    }

}
