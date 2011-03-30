package test.kegg;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.smsd.labelling.AtomContainerPrinter;

import stereo.CIPConfigurator;
import test.BaseTest;

public class KEGGMolTests extends BaseTest {
    
    public static final String DATA_DIR = "data/mols/kegg/";
    
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
        AtomContainerPrinter printer = new AtomContainerPrinter();
        System.out.println(printer.toString(mol));
    }
    
    @Test
    public void C00002Test() throws FileNotFoundException, CDKException {
        basicTest("C00002");
    }
    
    @Test
    public void C00008Test() throws FileNotFoundException, CDKException {
        basicTest("C00008");
    }
    
    @Test
    public void C00019Test() throws FileNotFoundException, CDKException {
        basicTest("C00019");
    }
    
    @Test
    public void C00025Test() throws FileNotFoundException, CDKException {
        basicTest("C00002");
    }
    
    @Test
    public void C00026Test() throws FileNotFoundException, CDKException {
        basicTest("C00026");
    }
    
    @Test
    public void C00036Test() throws FileNotFoundException, CDKException {
        basicTest("C00036");
    }
    
    @Test
    public void C00041Test() throws FileNotFoundException, CDKException {
        basicTest("C00041");
    }
    
    @Test
    public void C00049Test() throws FileNotFoundException, CDKException {
        basicTest("C00049");
    }
    
    @Test
    public void C00170Test() throws FileNotFoundException, CDKException {
        basicTest("C00170");
    }
    
    @Test
    public void C00263Test() throws FileNotFoundException, CDKException {
        basicTest("C00263");
    }
    
    @Test
    public void C01879Test() throws FileNotFoundException, CDKException {
        basicTest("C01879");
    }
    
    @Test
    public void C02926Test() throws FileNotFoundException, CDKException {
        basicTest("C02926");
    }

}
