package test;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.stereo.StereoTool;
import org.openscience.cdk.stereo.TetrahedralChirality;

public class CIPConfiguratorTest extends BaseTest {
    
    @Test
    public void from3DMolfiles() throws FileNotFoundException, CDKException {
//        String[] filenames = {"NDNU", "NUDN", "NUND", "NUNU" };
        String[] filenames = {"NNDU", "NNUD", "NDUN", "NDND" };
        for (String filename : filenames) {
            String path = "data/simples/" + filename + "_3D.mol";
            IMolecule mol = getMolecule(path);
            IAtom centralAtom = mol.getAtom(0);
            IAtom atom1 = mol.getAtom(1);
            IAtom atom2 = mol.getAtom(2);
            IAtom atom3 = mol.getAtom(3);
            IAtom atom4 = mol.getAtom(4);
            ITetrahedralChirality.Stereo stereo = 
                StereoTool.getStereo(atom1, atom2, atom3, atom4);
            IAtom[] ligandAtoms = { atom1, atom2, atom3, atom4};
            CIPTool.CIP_CHIRALITY chiral = CIPTool.getCIPChirality(mol, 
                    new TetrahedralChirality(centralAtom, ligandAtoms, stereo));
            System.out.println(filename + " " + chiral);
        }
    }

}
