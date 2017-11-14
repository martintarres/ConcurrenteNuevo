public class main {


    public static void main(String[] args){

        Constantes constantes= new Constantes();
        ListasDeDisparos listas =  new ListasDeDisparos();

        Monitor monitor= new Monitor(constantes);

        Hilo h1 = new Hilo("Hilo 1",listas.l1, monitor);
        Hilo h2 = new Hilo("Hilo 2",listas.l2, monitor);

        monitor.mapeo(h1);
        monitor.mapeo(h2);

        h1.start();
        h2.start();

    }
}
