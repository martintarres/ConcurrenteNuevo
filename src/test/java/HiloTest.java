import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    public void showHilos(){

        for (String despertado :
                log.getHistorialHilosDespertados()) {
            System.out.println(despertado.length());
        }
    }

}
