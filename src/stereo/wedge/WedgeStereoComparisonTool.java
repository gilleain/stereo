package stereo.wedge;

import java.util.HashMap;
import java.util.Map;

import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;

public class WedgeStereoComparisonTool {
    
    public enum ComparisonResult {
        NEITHER_CHIRAL,
        FIRST_NULL_SECOND_OK,
        FIRST_OK_SECOND_NULL,
        FIRST_NONE_SECOND_OK,
        FIRST_OK_SECOND_NONE,
        CHIRAL_MATCH,
        CHIRAL_MISMATCH
    }
    
    /**
     * Compare a (mapped) pair of atom containers to check that they have
     * the same stereo centers.
     * 
     * @param atomContainerA an atom container
     * @param atomContainerB another atom container
     * @param equivMap mapped atoms between the pair
     * @return
     */
    public static Map<Integer, ComparisonResult> hasSameChiralities(
            IAtomContainer atomContainerA, IAtomContainer atomContainerB,
            Map<Integer, Integer> equivMap) {
        Map<Integer, ComparisonResult> resultMap = 
            new HashMap<Integer, ComparisonResult>();
        
        WedgeStereoLifter lifter = new WedgeStereoLifter();
        for (int indexA = 0; indexA < atomContainerA.getAtomCount(); indexA++) {
            IAtom atomA = atomContainerA.getAtom(indexA);
            int indexB = equivMap.get(indexA);
            IAtom atomB = atomContainerB.getAtom(indexB);
            IStereoElement elementA = lifter.lift(atomA, atomContainerA);
            IStereoElement elementB = lifter.lift(atomB, atomContainerB);
            if (elementA == null && elementB == null) {
                resultMap.put(indexA, ComparisonResult.NEITHER_CHIRAL);
            } else if (elementA == null && elementB != null) {
                resultMap.put(indexA, ComparisonResult.FIRST_NULL_SECOND_OK);
            } else if (elementA != null && elementB == null) {
                resultMap.put(indexA, ComparisonResult.FIRST_OK_SECOND_NULL); 
            } else {
                CIP_CHIRALITY chiralA = getChirality2D(elementA, atomContainerA);
                CIP_CHIRALITY chiralB = getChirality2D(elementB, atomContainerB);
                if (chiralA == CIP_CHIRALITY.NONE && chiralB == CIP_CHIRALITY.NONE) {
                    resultMap.put(indexA, ComparisonResult.NEITHER_CHIRAL);
                } else if (chiralA == CIP_CHIRALITY.NONE && chiralB != CIP_CHIRALITY.NONE) {
                    resultMap.put(indexA, ComparisonResult.FIRST_NONE_SECOND_OK);
                } else if (chiralA == CIP_CHIRALITY.NONE && chiralB != CIP_CHIRALITY.NONE) {
                    resultMap.put(indexA, ComparisonResult.FIRST_OK_SECOND_NONE);
                } else {
                    if (chiralA == chiralB) {
                        resultMap.put(indexA, ComparisonResult.CHIRAL_MATCH);
                    } else {
                        resultMap.put(indexA, ComparisonResult.CHIRAL_MISMATCH);
                    }
                }
            }
        }
        
        return resultMap;
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
