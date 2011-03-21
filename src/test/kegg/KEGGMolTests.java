package test.kegg;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;

import stereo.CIPConfigurator;
import test.BaseTest;

public class KEGGMolTests extends BaseTest {
    
    public static final String DATA_DIR = "data/mols/";
    
    public void basicTest(String compoundID) throws FileNotFoundException, CDKException {
        IMolecule mol = getMolecule(DATA_DIR + compoundID + ".mol");
        CIPConfigurator.configureWedges(mol);
        for (IStereoElement stereo : mol.stereoElements()) {
            if (stereo instanceof ITetrahedralChirality) {
                ITetrahedralChirality tetrahedral = 
                    (ITetrahedralChirality) stereo;
                int atomIndex = mol.getAtomNumber(tetrahedral.getChiralAtom());
                CIP_CHIRALITY chiral = CIPTool.getCIPChirality(mol, tetrahedral);
                System.out.println(atomIndex + ":" + chiral);
            }
        }
    }
    
    @Test
    public void C00025Test() throws FileNotFoundException, CDKException {
        basicTest("C00025");
    }

}
