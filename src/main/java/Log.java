/**
 * Created by Fabrizio_p on 30/08/2017.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Log {
    private File marcados;
    private File registro;
    private String direccionMarcado;
    private String direccionRegistro;
    private BufferedReader br;
    private PrintWriter pw;
    private BufferedWriter bw;
    private List<String> lineasLeidas;

    public Log(String marcados,String registro){
        this.direccionMarcado = marcados;
        this.direccionRegistro = registro;
        this.marcados = new File(marcados);
        this.registro = new File(registro);

    }
    public List<String> leerLineas(){
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
                if(linea.length()!=0)
                lineasLeidas.add(linea);
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
    public void escribir(String linea,File destino){
        try{
            FileWriter fw = new FileWriter(destino,true);
            //fw.write(linea);

            //pw = new PrintWriter(fw);
            //pw.println(linea);
            bw = new BufferedWriter(fw);
            bw.write(linea);
           // bw.newLine();
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
    public Matriz getMarcado(String linea){
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
}
