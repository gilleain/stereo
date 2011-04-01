package stereo;

import java.util.List;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.ILigand;
import org.openscience.cdk.geometry.cip.VisitedAtoms;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.SaturationChecker;

/**
 * Small utility class to check an atom to see if it is a tetrahedral stereo
 * center - that is, it has 4 different neighbours. It uses the {@link CIPTool}
 * to determine this. 
 * 
 * @author maclean
 *
 */
public class StereoCenterAnalyser {
    
    /**
     * Check an atom to see if it has a potential tetrahedral stereo center.
     * This can only be true if:
     * <ol>
     * <li>It has 4 neighbours OR 3 neighbours and a single implicit hydrogen</li>
     * <li>These four neighbours are different according to CIP rules</li>
     * </ol>
     * If these conditions are met, it returns true.
     * 
     * @param atom the central atom of the stereocenter
     * @param atomContainer the atom container the atom is in
     * @return true if all conditions for a stereocenter are met
     */
    public static boolean hasPotentialStereoCenter(
            IAtom atom, IAtomContainer atomContainer) {
        List<IAtom> neighbours = atomContainer.getConnectedAtomsList(atom);
        int numberOfNeighbours = neighbours.size();
        boolean hasImplicitHydrogen = false;
        if (numberOfNeighbours == 4) {
            hasImplicitHydrogen = false;
        } else if (numberOfNeighbours == 3) {
            Integer implicitCount = atom.getImplicitHydrogenCount(); 
            if (implicitCount != null && implicitCount == 1) {
                hasImplicitHydrogen = true;
            } else {
                SaturationChecker checker = new SaturationChecker();
                try {
                    if (checker.calculateNumberOfImplicitHydrogens(
                            atom, atomContainer) == 1) {
                        hasImplicitHydrogen = true;
                    }
                } catch (CDKException e) {
                    e.printStackTrace();
                }
            }
            if (!hasImplicitHydrogen) {
                return false;
            }
        } else if (numberOfNeighbours > 4) {
            return false;   // not tetrahedral, anyway
        } else if (numberOfNeighbours < 3) {
            return false;   // definitely not chiral
        }
        ILigand[] ligands = new ILigand[4];
        int index = 0;
        VisitedAtoms bitSet = new VisitedAtoms();
        int chiralAtomIndex = atomContainer.getAtomNumber(atom);
        for (IAtom neighbour : neighbours) {
            int ligandAtomIndex = atomContainer.getAtomNumber(neighbour);
            ligands[index] = CIPTool.defineLigand(
                    atomContainer, bitSet, chiralAtomIndex, ligandAtomIndex);
            index++;
        }
        if (hasImplicitHydrogen) {
            ligands[index] = CIPTool.defineLigand(
                    atomContainer, bitSet, chiralAtomIndex, CIPTool.HYDROGEN);
        }
        CIPTool.order(ligands);
        return CIPTool.checkIfAllLigandsAreDifferent(ligands);
    }

}
