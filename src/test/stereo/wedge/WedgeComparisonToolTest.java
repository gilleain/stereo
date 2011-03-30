package test.stereo.wedge;

import java.io.IOException;


import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.smsd.labelling.AtomContainerPrinter;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;

import stereo.compare.WedgeStereoComparisonResult;
import stereo.compare.WedgeStereoComparisonTool;
import test.BaseTest;

public class WedgeComparisonToolTest extends BaseTest {
    
    public static final int w = 1200;
    public static final int h = 600;
    
    public void testReaction(IReaction reaction, String name) throws IOException {
        AtomContainerPrinter acp = new AtomContainerPrinter();
        int globalID = 0;
        for (IAtomContainer atomContainer : ReactionManipulator.getAllAtomContainers(reaction)) {
            setAtomNumber(atomContainer);
            for (IAtom atom : atomContainer.atoms()) {
                atom.setID(String.valueOf(globalID));
                System.out.println("setting id to " + globalID);
                globalID++;
            }
//            System.out.println(acp.toString(atomContainer));
        }
        int i = 0;
        for (IMapping mapping : reaction.mappings()) {
            mapping.getChemObject(0).setID(String.valueOf(i));
            mapping.getChemObject(1).setID(String.valueOf(i));
            i++;
        }
        drawReaction(reaction, "img/rxn/" + name + ".png", w, h);
        for (WedgeStereoComparisonResult result : WedgeStereoComparisonTool.compare(reaction)) {
            System.out.println(result);
        }
        
        for (IAtomContainer atomContainer : ReactionManipulator.getAllAtomContainers(reaction)) {
            System.out.println(acp.toString(atomContainer));
        }
    }
    
    @Test
    public void testCOAMapped() throws CDKException, IOException {
        testReaction(readReaction("data/rxn/COA_MAPPED_CANON_.rxn"), "COA");
    }
    
    @Test
    public void testR00103Mapped() throws CDKException, IOException {
        testReaction(readReaction("data/rxn/R00103_MAPPED_.rxn"), "R00103");
    }
    
    @Test
    public void testR08967Mapped() throws CDKException, IOException {
        testReaction(readReaction("data/rxn/R08967_MAPPED_.rxn"), "R08967");
    }

}
