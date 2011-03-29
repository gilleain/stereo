package stereo.compare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import stereo.wedge.WedgeStereoAnalyser;
import stereo.wedge.WedgeStereoAnalysisResult;
import stereo.wedge.WedgeStereoLifter;

/**
 * Tool to check mapped pairs of atoms, to see if they have compatible
 * stereo wedges.
 * 
 * @author maclean
 *
 */
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
    public static List<WedgeStereoComparisonResult> compare(
            IAtomContainer atomContainerA, IAtomContainer atomContainerB,
            Map<Integer, Integer> equivMap) {
        List<WedgeStereoComparisonResult> resultMap = 
            new ArrayList<WedgeStereoComparisonResult>();
        
        WedgeStereoLifter lifter = new WedgeStereoLifter();
        for (int indexA = 0; indexA < atomContainerA.getAtomCount(); indexA++) {
            int indexB = equivMap.get(indexA);
            IAtom atomA = atomContainerA.getAtom(indexA);
            IAtom atomB = atomContainerB.getAtom(indexB);
            resultMap.add(compare(atomA, atomContainerA, atomB, atomContainerB, lifter));
        }
        
        return resultMap;
    }
    
    public static WedgeStereoComparisonResult compare(
            IAtom atomA, IAtomContainer atomContainerA,
            IAtom atomB, IAtomContainer atomContainerB,
            WedgeStereoLifter lifter) {
        
        WedgeStereoAnalysisResult resultForA = WedgeStereoAnalyser.getResult(atomA, atomContainerA, lifter);
        WedgeStereoAnalysisResult resultForB = WedgeStereoAnalyser.getResult(atomB, atomContainerB, lifter);
       
        return new WedgeStereoComparisonResult(
                        atomA, atomContainerA, resultForA, atomB, atomContainerB, resultForB);
    }
    
   
}
