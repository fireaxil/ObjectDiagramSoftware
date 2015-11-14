package dataset;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by Christian on 11/14/2015.
 */
public class GeneratorTestCase {

    @Test
    public void testNumClassesSize(){
        MasterSet  masterSet = new MasterSet();
        masterSet.randomize(3,6,10);

        assertTrue(masterSet.getClasses().size() == 3);
    }
    @Test
    public void testNumObjectsSize(){
        MasterSet  masterSet = new MasterSet();
        masterSet.randomize(3,6,10);

        assertTrue(masterSet.getObjects().size() == 6);
    }
    @Test
    public void testNumReferencesSize(){
        MasterSet  masterSet = new MasterSet();
        masterSet.randomize(3,6,10);

        assertTrue(masterSet.getReferences().size() == 10);
    }
    @Test
    public void testRoutine(){
        MasterSet  masterSet = new MasterSet();
        masterSet.randomize(3,6,10);
        for(int i = 0; i < masterSet.getClasses().size(); i++ ) {
            System.out.println(masterSet.getClasses().get(i).getName());
        }
        System.out.println("\n");
        for(int i = 0; i < masterSet.getObjects().size(); i++ ) {
            System.out.println(masterSet.getObjects().get(i).getType().getName());
        }
        System.out.println("\n");

        for(int i = 0; i < masterSet.getReferences().size(); i++ ) {
            System.out.println(masterSet.getReferences().get(i).getOrigin().getName());
        }
    }

}
