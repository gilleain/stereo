package stereo.wedge;

import static org.openscience.cdk.interfaces.IBond.Stereo.NONE;
import static org.openscience.cdk.interfaces.IBond.Stereo.UP;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.stereo.TetrahedralChirality;

public class SingleUpWedgeRule extends WedgeRule {
    
    private IBond.Stereo[] pattern = { UP, NONE, NONE };

    @Override
    public Stereo[] getPattern() {
        return pattern;
    }

    @Override
    public IStereoElement execute(IAtom centralAtom,
                                  IAtomContainer atomContainer,
                                  SortedMap<Double, IBond> angleMap) {
        int[] permutation = getMatchPermutation();
        List<IBond> bonds = new ArrayList<IBond>(angleMap.values());
        IAtom[] ligandAtoms = new IAtom[4];
        for (int index = 0; index < 3; index++) {
            IBond bond = bonds.get(permutation[index]);
            ligandAtoms[index] = bond.getConnectedAtom(centralAtom);
        }
        IChemObjectBuilder builder = centralAtom.getBuilder(); 
        IAtom explicitHydrogen = builder.newInstance(IAtom.class, "H");
        atomContainer.addAtom(explicitHydrogen);
        atomContainer.addBond(builder.newInstance(IBond.class, centralAtom, explicitHydrogen));
        ligandAtoms[3] = explicitHydrogen;
        ITetrahedralChirality.Stereo chirality = ITetrahedralChirality.Stereo.CLOCKWISE; 
        return new TetrahedralChirality(centralAtom, ligandAtoms, chirality);
    }

}
