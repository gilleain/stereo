package stereo.wedge;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;

import stereo.StereoCenterAnalyser;

/**
 * Analyse the stereo wedges around an atom, to determine if they are correct.
 * 
 * @author maclean
 *
 */
public class WedgeStereoAnalyser {
    
    public static List<WedgeStereoAnalysisResult> getResults(IAtomContainer atomContainer) {
        WedgeStereoLifter lifter = new WedgeStereoLifter();
        return getResults(atomContainer, lifter);
    }
    
    public static List<WedgeStereoAnalysisResult> getResults(IAtomContainer atomContainer, WedgeStereoLifter lifter) {
        List<WedgeStereoAnalysisResult> results = new ArrayList<WedgeStereoAnalysisResult>();
        for (IAtom atom : atomContainer.atoms()) {
            results.add(getResult(atom, atomContainer, lifter));
        }
        return results;
    }
    
    public static WedgeStereoAnalysisResult getResult(IAtom atom, IAtomContainer atomContainer, WedgeStereoLifter lifter) {
        boolean isPotentialStereoCenter = 
            StereoCenterAnalyser.hasPotentialStereoCenter(atom, atomContainer);
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

    private static WedgeStereoAnalysisResult convertCipToResult(CIP_CHIRALITY cipChirality) {
        switch (cipChirality) {
            case NONE : return WedgeStereoAnalysisResult.NONE;
            case R : return WedgeStereoAnalysisResult.CHIRAL_R;
            case S : return WedgeStereoAnalysisResult.CHIRAL_S;
            default: return WedgeStereoAnalysisResult.NONE;
        }
    }

}
