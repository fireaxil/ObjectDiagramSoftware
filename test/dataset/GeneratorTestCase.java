package dataset;

import org.junit.Test;

/**
 * Created by Christian on 11/14/2015.
 */
public class GeneratorTestCase {
    MasterSet  masterSet;

    @Test
    public void testRoutine(){
        masterSet = new MasterSet();
        masterSet.randomize(2,3,4);

        //System.out.println(masterSet.toString());
    }

}
