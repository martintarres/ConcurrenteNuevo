/**
 * Created by YepezHinostroza on 25/8/2017.
 */

import org.jsoup.Jsoup;

import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class Lector {
    private URL urlob;
    private StringBuffer html;
    private StringBuffer textoPlano;
    public Lector(String direccion){

        //SOLO GUARDA EN EL STRINGBUFFER HTML
        try {
            //direccion= "file:C:\\Users\\alexa\\Desktop\\PC\\ProgramacionConcurrente\\archivo.html";
            String linea=null;

            urlob = new URL(direccion);
            InputStreamReader rea = new InputStreamReader(urlob.openStream());
            BufferedReader br= new BufferedReader(rea);
            html = new StringBuffer();
            textoPlano = new StringBuffer();
            while((linea= br.readLine()) != null){
                html.append(linea);
                html.append("\n");
            }


        }
        catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public StringBuffer getHtml(){
        return this.html;

    }
    public StringBuffer getTextoPlano(){
        convertir();
        return this.textoPlano;
    }
    public void convertir(){

        // CONVIERTE EN TEXTO PLANO LO QUE ESTE EN EL STRINGBUFFER HTML
        try {


            String linea;
            StringReader rea = new StringReader(this.getHtml().toString());
            BufferedReader br= new BufferedReader(rea);
            //this.textoPlano = new StringBuffer();
            //int contadorLineas =0;
            //int contadorSaltos =0;


            while((linea=br.readLine()) != null) {
                 this.textoPlano.append(Jsoup.parse(linea).text());
                 this.textoPlano.append("\n");
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public StringBuffer cortar(String desde , String hasta){
        // DEL TEXTO PLANO SOLO GUARDA LO QUE ESTE ENTRE LOS DOS STRING SIN INCLUIRLOS
        try {


            String linea;
            StringReader rea = new StringReader(this.getTextoPlano().toString());
            BufferedReader br= new BufferedReader(rea);
            StringBuffer pedazo = new StringBuffer();

            boolean copiando = false;

            while((linea= br.readLine()) != null) {

                if(!copiando&&linea.contains(desde)){
                    copiando=true;
                    continue;
                }

                if(linea.contains(hasta)){
                    break;
                }
                if(copiando){
                    pedazo.append(linea);
                    pedazo.append("\n");

                    /*if(linea.trim().equals("")){

                    }
                    else{
                        pedazo.append(linea);
                        pedazo.append("\n");
                    }*/

                }



            }
            return pedazo;

        }
        catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public int cantidadFilas(StringBuffer buffer){
        int m=0;
        int contadorSaltos=0;
        try {


            String linea;
            StringReader rea = new StringReader(buffer.toString());
            BufferedReader br= new BufferedReader(rea);
            StringBuffer pedazo = new StringBuffer();
            boolean contado = false;

            boolean copiando = false;

            while((linea= br.readLine()) != null) {
                if(linea.trim().equals("")){
                    contadorSaltos++;
                    if(contadorSaltos>2&&!contado){
                        contado=true;
                        m++;
                    }

                }
                else{
                    contado=false;
                    contadorSaltos=0;
                }

            }

        }
        catch (MalformedURLException e){
            e.printStackTrace();
            return 0;
        }
        catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        // ES MENOS DOS ES PARA RESTARLE LOS ESPACIOS DEL COMIENZO Y FINAL
        //ESTABA EL -2, PERO LE CAMBIE AL -1 POR EL COMENTARIO DE ABAJO
        // LO DEVUELTO ES EL TAMAÑO DE LA MATRIZ DEl PIPE, O SEA, +1 PARA LA PRIMERA FILA DE NOMBRES DE LAS COLUMNAS
        return m-1;
    }
    public int cantidadColumnas(StringBuffer buffer){
        // SOLO VAA SACAR LA CANTIDAD DE COLUMNAS TENIENDO EN CUENTA LA PRIMERA FILA
        // QUERIA QUE VERIFICARA QUE EN TODAS LAS LINEAS SE MANTUVIERA LA MISMA CANTIDAD PERO ES UN VIAJE
        //int m=cantidadFilas(buffer);
        int n=0;
        //int contadorFilas=0;
        try{
            String linea;
            StringReader rea = new StringReader(buffer.toString());
            BufferedReader br= new BufferedReader(rea);
            StringBuffer pedazo = new StringBuffer();
            int filasLeidas =0;
            int contadorSaltos=0;
            boolean yaLeido=false;
            while((linea= br.readLine()) != null&&!(filasLeidas==2)) {
                if(linea.trim().equals("")){
                    contadorSaltos++;


                }
                else{
                    //contado=false;
                    if(contadorSaltos<3){
                        n++;
                    }
                    else{
                        filasLeidas++;
                    }
                    contadorSaltos=0;
                }

            }
            // +2 PORQUE CONTE DIFERENTE EL ESPACIO LARO DEL INICIO Y FINAL Y DA PAJA CAMBIARLO
            return n+2;
        }
        catch(Exception e){
            return 0;

        }

    }
    public String[][] getTabla(StringBuffer buffer){

        try{
            String linea;
            StringReader rea = new StringReader(buffer.toString());
            BufferedReader br= new BufferedReader(rea);
            int m = cantidadFilas(buffer);
            int n = cantidadColumnas(buffer);
            boolean encontrado=false;

            String[][] tabla = new String[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    encontrado=false;
                    while((linea= br.readLine()) != null&&!encontrado) {
                        if(linea.trim().equals("")){


                        }
                        else{

                            tabla[i][j]=linea;
                            encontrado=true;
                        }

                    }



                }


            }
            /*for (int i = 0; i < m; i++) {
                for (int j = 0; j <n ; j++) {
                    System.out.print("  "+ tabla[i][j]);

                }
                System.out.print("\n");
                System.out.print("\n");

            }*/


            // TUVE QUE MOVER TODOS LOS DATOS UNA POSICION A LA DERECHA PORQUE EL PIPE ME DEVUELVE UNA TABLA CON VACIO EN LA  POSICION[0][0]
            //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
            String[][] arreglo= new String[m][n];
            arreglo[0][0] = "";
            for (int i = 0; i <m ; i++) {
                for (int j = 0; j < n; j++) {
                    if(j!=0){
                        arreglo[i][j]=tabla[i][j-1];
                        //System.out.print(arreglo[i][j]);
                    }
                    else{
                        if(i!=0){
                            arreglo[i][j]=tabla[i-1][n-1];
                            //System.out.print(arreglo[i][j]);

                        }
                        else{
                            arreglo[i][j]="\\";
                            //System.out.print(arreglo[i][j]);
                        }
                    }


                }
                //System.out.println();

            }
            return arreglo;

        }
        catch(Exception e){
           return null;
        }




    }
    public void setDireccion(){

    }
}
