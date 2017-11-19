import org.jsoup.Jsoup;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class leerArchivo {



    public static void main(String [] args)  {
        URL urlob;
        String codigohtml;
        String codigocomun = null;
        boolean paso1= false;
        String matriz1;

       // File file= new File("/home/martin/Documentos/ProgramacionConcurrente/archivo.html");

        try {

            urlob = new URL("file:///home/martin/Documentos/ProgramacionConcurrente/archivo.html");
            InputStreamReader rea = new InputStreamReader(urlob.openStream());
            BufferedReader br= new BufferedReader(rea);

            while((codigohtml= br.readLine()) != null) {


                //  System.out.println(codigohtml);


                codigocomun = as(codigohtml);

                //   if(codigocomun.contains("Forwards incidence matrix I+")){

                //   }

                if (codigocomun.contains("Forwards")) {

                    paso1 = true;
                }

                    if(paso1==true) {

                     //   while (!(codigocomun.contains("Backwards"))) {

                            // if (codigocomun.contains("Backwards")) {

                            // br.close();
                            //} else {
                            System.out.println(codigocomun);

                            if(codigocomun.contains("Backwards")){
                                br.close();
                            }
                       // }

                    //}
                /*
                if( esNumero(codigocomun)){

                    br.close();

                }else {

                    System.out.println(codigocomun);
                }
                   */
                }

            }




        }
        catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

