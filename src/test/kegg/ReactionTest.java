package test.kegg;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IReaction;

import test.BaseTest;

public class ReactionTest extends BaseTest {
    
    public File inputDir = new File("data/rxn");
    
    public void testReaction(String reactionID) throws FileNotFoundException, CDKException {
        String filePath = new File(inputDir, reactionID + ".rxn").toString();
        IReaction reaction = readReaction(filePath);
        
    }
    
    @Test
    public void r000026Test() throws FileNotFoundException, CDKException {
        testReaction("R000026");
    }

}
