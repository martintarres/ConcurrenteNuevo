import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HiloTest {
    RdP petri;
    Constantes constantes;
    Log log;


    @Before
    public void setUp() throws Exception {
        final String file = "";
        final String path = (new File(".")).getCanonicalPath();
        final String registro = "/registro.txt";
        this.constantes = new Constantes();
        this.log = new Log(file + path + registro, constantes);
        petri = new RdP(constantes);
    }

    @Test
    public void showHilos() {
        List<String> palabras = new ArrayList<String>();
        List<String> palabras2 = new ArrayList<String>();
        palabras.add("hola");
        palabras.add("mundo");
        palabras2.add("C");

        palabras2.add("mundo");
        palabras2.add("hola");

        Set<String> conjunto = new TreeSet<String>(palabras);
        Set<String> conjunto2 = new TreeSet<String>(palabras2);
        System.out.println(conjunto.equals(conjunto2));

    }

}
