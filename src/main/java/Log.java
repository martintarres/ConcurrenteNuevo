/**
 * Created by Fabrizio_p on 30/08/2017.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Log {
    private File marcados;
    private File registro;
    final String direccionMarcado;
    final String direccionRegistro;
    private BufferedReader br;
    private BufferedWriter bw;


    public Log(final String marcados,final String registro){
        this.direccionMarcado = marcados;
        this.direccionRegistro = registro;
        this.marcados = new File(marcados);
        this.registro = new File(registro);

    }
    public List<String> leerLineas(){
        List<String> lineasLeidas;
        lineasLeidas = new ArrayList<String>();

        try{
         // String file = "file://" ;
         // String direccion =  "C:\\Users\\Fabrito\\Desktop\\Materias\\ProgramacionConcurrente\\Tp final\\src\\texto.txt";

          //System.out.println("---------");
          FileReader fr = new FileReader(this.marcados);
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
    public File getMarcados(){
        return this.marcados;
    }
    public File getRegistro(){
        return this.registro;
    }

    public  void limpiar(){
        if(this.marcados.exists())
        this.marcados.delete();
        this.marcados=new File(direccionMarcado);
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
        escribir(((Hilo) (Thread.currentThread())).getNombre() + cadena + m.traducirDisparo(transicion), this.getRegistro());


    }
    public synchronized void registrarBasico2(final Monitor m, final Matriz sensi, final Matriz enco){
        escribir("\n", this.getRegistro());
        escribir("Marcado Actual : ", this.getRegistro());
        escribir(m.lineaMarcados(), this.getRegistro());
        //escribir("  M1  M2  M3  M4 P10 P11 P12 P13 P14 P15 P16 P17 P18 P20 P21 P22 P23 P30 P31 P32 P33 P34 P35  R1  R2  R3  s1  s2", this.getRegistro());
        escribir(m.getPetri().marcadoActual().toString()+"\n", this.getRegistro());
        // this.log.escribir("----------------------------------------------------------------------",log.getRegistro());


        escribir(m.getPetri().marcadoActual().toString(), this.getMarcados());


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
}
