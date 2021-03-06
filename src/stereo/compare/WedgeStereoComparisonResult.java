package stereo.compare;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import stereo.wedge.WedgeStereoAnalysisResult;

public class WedgeStereoComparisonResult {
    
    private IAtom firstAtom;
    
    private IAtomContainer firstAtomContainer;
    
    private WedgeStereoAnalysisResult resultForFirst;
    
    private IAtom secondAtom;
    
    private IAtomContainer secondAtomContainer;
    
    private WedgeStereoAnalysisResult resultForSecond;
    
    public WedgeStereoComparisonResult(
            IAtom firstAtom, IAtomContainer firstAtomContainer, WedgeStereoAnalysisResult resultForFirst,
            IAtom secondAtom, IAtomContainer secondAtomContainer, WedgeStereoAnalysisResult resultForSecond) {
        this.firstAtom = firstAtom;
        this.firstAtomContainer = firstAtomContainer;
        this.resultForFirst = resultForFirst;
        this.secondAtom = secondAtom;
        this.secondAtomContainer = secondAtomContainer;
        this.resultForSecond = resultForSecond;
    }

    public int getIndexOfFirst() {
        return firstAtomContainer.getAtomNumber(firstAtom);
    }

    public WedgeStereoAnalysisResult getResultForFirst() {
        return resultForFirst;
    }

    public int getIndexOfSecond() {
        return secondAtomContainer.getAtomNumber(secondAtom);
    }

    public WedgeStereoAnalysisResult getResultForSecond() {
        return resultForSecond;
    }
    
    public String toString() {
        String firstID;
        if (firstAtom.getID() == null) {
            firstID = String.valueOf(firstAtomContainer.getAtomNumber(firstAtom));
        } else {
            firstID = firstAtom.getID();
        }
        String secondID;
        if (secondAtom.getID() == null) {
            secondID = String.valueOf(secondAtomContainer.getAtomNumber(secondAtom));
        } else {
            secondID = secondAtom.getID();
        }
        return firstID + "\t:\t" + resultForFirst + "\t\t"
            + secondID + "\t:\t" + resultForSecond;
    }
}
