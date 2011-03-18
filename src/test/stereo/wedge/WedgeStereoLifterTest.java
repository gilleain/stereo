package test.stereo.wedge;

import static org.openscience.cdk.interfaces.IBond.Stereo.DOWN;
import static org.openscience.cdk.interfaces.IBond.Stereo.NONE;
import static org.openscience.cdk.interfaces.IBond.Stereo.UP;

import org.junit.Test;
import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.ITetrahedralChirality;

import stereo.wedge.WedgeStereoLifter;
import test.BaseTest;

public class WedgeStereoLifterTest extends BaseTest {
    
    @Test
    public void tetraRTest() {
        IBond.Stereo[] stereos = { NONE, NONE, UP, DOWN };
        IAtomContainer atomContainer = getTetra(Shape.CROSS, stereos);
        WedgeStereoLifter lifter = new WedgeStereoLifter();
        ITetrahedralChirality element = (ITetrahedralChirality) 
                lifter.lift(atomContainer.getAtom(0), atomContainer);
        CIP_CHIRALITY assignment = CIPTool.getCIPChirality(atomContainer, element);
        System.out.println(assignment);
    }

}
