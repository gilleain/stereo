package stereo.compare;

import java.util.HashMap;
import java.util.Map;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;

import stereo.Chirality3DTool;
import stereo.wedge.WedgeStereoAnalyser;
import stereo.wedge.WedgeStereoAnalysisResult;
import stereo.wedge.WedgeStereoLifter;

/**
 * Tool for comparing chiralities.
 * 
 * @author maclean
 *
 */
public class ChiralityComparisonTool {
    
    public static void compare(IAtomContainer atomContainerA, 
                               IAtomContainer atomContainerB) throws CDKException {
        Map<IAtom, CIPTool.CIP_CHIRALITY> chiralityMapA;
        if (has3DCoordinates(atomContainerA)) {
            chiralityMapA = Chirality3DTool.getTetrahedralChiralities(atomContainerA);
        } else {
            chiralityMapA = getChiralityMapFromWedges(atomContainerA);
        }
        
        Map<IAtom, CIP_CHIRALITY> chiralityMapB;
        if (has3DCoordinates(atomContainerB)) {
            chiralityMapB = Chirality3DTool.getTetrahedralChiralities(atomContainerB);
        } else {
            chiralityMapB = getChiralityMapFromWedges(atomContainerB);
        }
        
        Isomorphism isomorphism = new Isomorphism(Algorithm.DEFAULT, true);
        isomorphism.init(atomContainerA, atomContainerB, false, false);
        Map<IAtom, IAtom> atomMap = isomorphism.getFirstAtomMapping();
        for (IAtom atomA : atomMap.keySet()) {
            IAtom atomB = atomMap.get(atomA);
            boolean isStereoA = chiralityMapA.containsKey(atomA);
            boolean isStereoB = chiralityMapB.containsKey(atomB);
            if (isStereoA && isStereoB) {
                CIP_CHIRALITY stereoA = chiralityMapA.get(atomA);
                CIP_CHIRALITY stereoB = chiralityMapB.get(atomB);
                System.out.println(stereoA + " " + stereoB);
            }
        }
    }

    private static Map<IAtom, CIP_CHIRALITY> getChiralityMapFromWedges(IAtomContainer atomContainer) {
        Map<IAtom, CIP_CHIRALITY> chiralityMap = new HashMap<IAtom, CIP_CHIRALITY>();
        WedgeStereoLifter lifter = new WedgeStereoLifter(); 
        for (IAtom atom : atomContainer.atoms()) { 
            WedgeStereoAnalysisResult result = 
                WedgeStereoAnalyser.getResult(atom, atomContainer, lifter);
            if (result == WedgeStereoAnalysisResult.CHIRAL_R) {
                chiralityMap.put(atom, CIP_CHIRALITY.R);
            } else if (result == WedgeStereoAnalysisResult.CHIRAL_S) {
                chiralityMap.put(atom, CIP_CHIRALITY.S);
            } else {
                // nothing
            }
        }
        return chiralityMap;
    }

    private static boolean has3DCoordinates(IAtomContainer atomContainerA) {
        // XXX - check all atoms?
        for (IAtom atom : atomContainerA.atoms()) {
            if (atom.getPoint3d() == null) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
