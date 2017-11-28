import org.jsoup.Jsoup;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class leerArchivo {



    public static void main(String [] args)  {
        try{
            List<String> ListaPInvariantes= new ArrayList<String>();
            URL urlob;
            String codigohtml;
            String codigocomun = null;
            boolean paso1= false;
            String matriz1;

            // File file= new File("/home/martin/Documentos/ProgramacionConcurrente/archivo.html");

            final String file= "file:///" ;
            final String path = (new File(".")).getCanonicalPath();

            final String invariantes ="/analisisInvariante.html";


            final Lector lector1 = new Lector(file+path+invariantes);

            lector1.convertir();

            System.out.println(lector1.getTextoPlano());


            String [] casteado = lector1.getTextoPlano().toString().split("P-Invariant equations");
            casteado = casteado[1].split("Analysis time:");

            System.out.println("Voy a imprimir a continuacion sólo la parte correspondiente a las ecuaciones P Invariantes obtenidas del texcto plano" +
                    ", la cual fue obtenida del html analisisInvariante.html" +
                    "                     Martin trolo");
            System.out.println(casteado[0]);



        }
        catch(Exception e ){

        }


    }

    public static String as(String html){
        return Jsoup.parse(html).text();
    }

    public static Boolean esNumero(String nume){

        try {
            Integer.parseInt(nume);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }

        }
    }

