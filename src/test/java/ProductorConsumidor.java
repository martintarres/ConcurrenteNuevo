import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ProductorConsumidor {
    public static void main(String[] args){
        System.out.println("Sistema de manufacturazion automatizado"+"\n");

        String opcion;
        int numero;
        do{
            System.out.println("Seleccione la política = ");
            System.out.println("  1  para 1A-2B-1C");
            System.out.println("  2  para 3A-2B-1C");
            System.out.println("  3  para Politica Random y modo verborragico");
            //opcion = entrada.nextLine();
            Scanner entrada = new Scanner(System.in);
            numero = entrada.nextInt();

        }

        while(numero!=1&&numero!=2&&numero!=3);
        //System.out.println("Politica establecida =  "+ numero);

        Constantes constantes= new Constantes();
        ListasDeDisparos listas =  new ListasDeDisparos();

        Monitor monitor= new Monitor(constantes, numero);
        List<Integer> l1= new ArrayList();
        l1.add(0);
        l1.add(1);

        List<Integer> l2= new ArrayList();
        l2.add(2);
        l2.add(3);

        Hilo productor = new Hilo("Productor",l1, monitor);
        Hilo consumidor = new Hilo("Consumidor",l2, monitor);
        monitor.mapeo(productor);
        monitor.mapeo(consumidor);

        productor.start();
        consumidor.start();


    }
}
