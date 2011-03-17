package stereo;

import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;

public class CIPConfigurator {
    
    public static void configure(IMolecule molecule) {
        for (IAtom atom : molecule.atoms()) {
            List<IAtom> neighbours = molecule.getConnectedAtomsList(atom);
            // just do the 4-valent ones for the moment
            if (neighbours.size() == 4) {
                
            }
        }
    }

}
