package stereo.wedge;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.stereo.TetrahedralChirality;

public abstract class AbstractTetrahedralWedgeRule extends WedgeRule {
    
    @Override
    public IStereoElement execute(
            IAtom centralAtom, SortedMap<Double, IBond> angleMap) {
        int[] permutation = getMatchPermutation();
        List<IBond> bonds = new ArrayList<IBond>(angleMap.values());
        IAtom[] ligandAtoms = new IAtom[4];
        for (int index = 0; index < 4; index++) {
            IBond bond = bonds.get(permutation[index]);
            ligandAtoms[index] = bond.getConnectedAtom(centralAtom);
        }
        return new TetrahedralChirality(centralAtom, ligandAtoms, getStereo());
    }
    
    public abstract ITetrahedralChirality.Stereo getStereo();

}
