package first.auto;


import com.sun.javafx.geom.Edge;
import jxl.write.WriteException;
import org.junit.Test;

import java.io.IOException;

public class Runner {

    MaxTestChrome Chrome= new MaxTestChrome();
    MaxTestFireFox FireFox = new MaxTestFireFox();
    MaxTestEdge Edge = new MaxTestEdge();

    @Test
    public void Zapusk() throws InterruptedException, IOException, WriteException {

        Chrome.setup();
        Chrome.chrome_scn_1();
       // Chrome.chrome_scn_2();
       // Chrome.chrome_scn_3();
       // Chrome.chrome_scn_4();
        Chrome.tearDown();

        //FireFox.setup();
       // FireFox.fire_scn_1();
       // FireFox.fire_scn_2();
       // FireFox.fire_scn_3();
       // FireFox.fire_scn_4();


       // Edge.setup();
       // Edge.edge_scn_1();
       // Edge.edge_scn_2();
       // Edge.edge_scn_3();
       // Edge.edge_scn_4();

    }


}
