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
        log.leerHilos();
        for (int i = 0; i < log.getNombreHilos().size(); i++) {
            System.out.println("Hilo : "+ log.getNombreHilos().get(i));
            System.out.println("Transiciones : "+ log.getNombreTransiciones().get(i));
        }

    }

}
