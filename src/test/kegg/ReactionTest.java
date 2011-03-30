package test.kegg;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;

import stereo.compare.WedgeStereoComparisonResult;
import stereo.compare.WedgeStereoComparisonTool;
import test.BaseTest;

public class ReactionTest extends BaseTest {
    
    public File inputDir = new File("data/rxn");
    
    public File outputDir = new File("img/rxn");
    
    public static int w = 1200;
    public static int h = 500;
    
    public void testReaction(String reactionID) throws CDKException, IOException {
        String filePath = new File(inputDir, reactionID + ".rxn").toString();
        IReaction reaction = readReaction(filePath);
        String outputPath = new File(outputDir, reactionID + ".png").toString();
        int globalID = 0;
        for (IAtomContainer atomContainer : ReactionManipulator.getAllAtomContainers(reaction)) {
            for (IAtom atom : atomContainer.atoms()) {
                atom.setID(String.valueOf(globalID));
                globalID++;
            }
        }
        drawReaction(reaction, outputPath, w, h);
        for (WedgeStereoComparisonResult result : WedgeStereoComparisonTool.compare(reaction)) {
            System.out.println(result);
        }
    }
    
    @Test
    public void r000026Test() throws CDKException, IOException {
        testReaction("R000026");
    }
    
    @Test
    public void m5Test() throws CDKException, IOException {
        testReaction("M5_MAPPED_");
    }

}
