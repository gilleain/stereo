package stereo.wedge;

import java.util.Map;

import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;

public class WedgeStereoComparisonTool {
    
   
    
    /**
     * Compare a (mapped) pair of atom containers to check that they have
     * the same stereo centers.
     * 
     * @param atomContainerA an atom container
     * @param atomContainerB another atom container
     * @param equivMap mapped atoms between the pair
     * @return
     */
    public static boolean hasSameChiralities(
            IAtomContainer atomContainerA, IAtomContainer atomContainerB,
            Map<Integer, Integer> equivMap) throws StereoMatchException {
        
        WedgeStereoLifter lifter = new WedgeStereoLifter();
        for (int indexA = 0; indexA < atomContainerA.getAtomCount(); indexA++) {
            IAtom atomA = atomContainerA.getAtom(indexA);
            int indexB = equivMap.get(indexA);
            IAtom atomB = atomContainerB.getAtom(indexB);
            IStereoElement elementA = lifter.lift(atomA, atomContainerA);
            IStereoElement elementB = lifter.lift(atomB, atomContainerB);
            if (elementA == null && elementB == null) {
                continue;   // neither are stereo centers
            } else if (elementA == null && elementB != null) {
                String idA = atomContainerA.getID();
                String idB = atomContainerB.getID();
                String message = String.format(
                        "%s has null stereo for atom %d while " +
                        "%s has non-null stereo for atom %d", 
                        idA, indexA, idB, indexB);
                throw new StereoMatchException(message); 
            } else if (elementA != null && elementB == null) {
                String idA = atomContainerA.getID();
                String idB = atomContainerB.getID();
                String message = String.format(
                        "%s has non-null stereo for atom %d while ", 
                        "%s has null stereo for atom %d" +
                        idA, indexA, idB, indexB);
                throw new StereoMatchException(message); 
            } else {
                CIP_CHIRALITY chiralA = getChirality2D(elementA, atomContainerA);
                CIP_CHIRALITY chiralB = getChirality2D(elementB, atomContainerB);
                if (chiralA == CIP_CHIRALITY.NONE && chiralB == CIP_CHIRALITY.NONE) {
                    continue;
                } else if (chiralA == CIP_CHIRALITY.NONE && chiralB != CIP_CHIRALITY.NONE) {
                    
                }
            }
        }
        
        return true;
    }
    
    public static CIP_CHIRALITY getChirality2D(
            WedgeStereoLifter lifter, IAtom atom, IAtomContainer atomContainer) {
        IStereoElement stereoElement = lifter.lift(atom, atomContainer);
        return getChirality2D(stereoElement, atomContainer);
    }
    
    public static CIP_CHIRALITY getChirality2D(
            IStereoElement stereoElement, IAtomContainer atomContainer) {
        if (stereoElement instanceof ITetrahedralChirality) {
            return CIPTool.getCIPChirality(
                    atomContainer, (ITetrahedralChirality) stereoElement);
        } else {
            return CIPTool.CIP_CHIRALITY.NONE;
        }
    }
}
