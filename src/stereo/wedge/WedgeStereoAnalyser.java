package stereo.wedge;

import java.util.List;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.ILigand;
import org.openscience.cdk.geometry.cip.VisitedAtoms;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.tools.SaturationChecker;

/**
 * Analyse the stereo wedges around an atom, to determine if they are correct.
 * 
 * @author maclean
 *
 */
public class WedgeStereoAnalyser {
    
    public static WedgeStereoAnalysisResult getResult(IAtom atom, IAtomContainer atomContainer, WedgeStereoLifter lifter) {
        boolean isPotentialStereoCenter = hasPotentialStereoCenter(atom, atomContainer);
        IStereoElement element = lifter.lift(atom, atomContainer);
        return getResult(atomContainer, isPotentialStereoCenter, element);
    }
    
    private static WedgeStereoAnalysisResult getResult(IAtomContainer atomContainer, 
            boolean isPotentialStereoCenter, IStereoElement stereoElement) {
        if (isPotentialStereoCenter) {
            if (stereoElement == null) {
                return WedgeStereoAnalysisResult.MISSING;
            } else {
                CIP_CHIRALITY chirality = getChirality2D(stereoElement, atomContainer);
                WedgeStereoAnalysisResult result = convertCipToResult(chirality);
                if (result == WedgeStereoAnalysisResult.NONE) {
                    // should have R or S!
                    return WedgeStereoAnalysisResult.ERROR;
                } else {
                    return result;
                }
            }
        } else {
            if (stereoElement == null) {
                return WedgeStereoAnalysisResult.NONE;
            } else {
                return WedgeStereoAnalysisResult.ERROR;
            }
        }
        
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
    
    private static boolean hasPotentialStereoCenter(IAtom atom, IAtomContainer atomContainer) {
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

    private static WedgeStereoAnalysisResult convertCipToResult(CIP_CHIRALITY cipChirality) {
        switch (cipChirality) {
            case NONE : return WedgeStereoAnalysisResult.NONE;
            case R : return WedgeStereoAnalysisResult.CHIRAL_R;
            case S : return WedgeStereoAnalysisResult.CHIRAL_S;
            default: return WedgeStereoAnalysisResult.NONE;
        }
    }

}
