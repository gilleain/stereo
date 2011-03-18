package stereo.wedge;

import java.util.Map;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;

public abstract class WedgeRule {
    
    private int matchPoint;
    
    /**
     * A match between a particular bond stereo list and the pattern in a rule
     * may be cyclicly permuted - this method returns the permutation.
     * 
     * @return a permutation as an int array
     */
    public int[] getMatchPermutation() {
        int length = getPattern().length;
        int[] permutation = new int[length];
        if (matchPoint != -1) {
            int pi = matchPoint;
            for (int i = 0; i < length; i++) {
                permutation[i] = pi;
                pi++;
                if (pi >= length) {
                    pi = 0;
                }
            }
        } else {
            // Didn't match - should have called this method!
        }
        return permutation;
    }
    
    /**
     * Match a list of {@link IBond.Stereo}s to the pattern list of stereos,
     * allowing for circular permutation of the list. As a side-effect, it
     * stores the point in the pattern where the match was made.
     * 
     * @param stereoList the array of stereo constants to match
     * @return true if this Rule's pattern matches
     */
    public boolean matches(IBond.Stereo[] stereoList) {
        IBond.Stereo[] pattern = getPattern();
        if (stereoList.length != pattern.length) return false;
        
        int patternIndex = 0;
        int matchIndex = 0;
        
        // reset the match point
        matchPoint = -1;
        while (matchIndex < stereoList.length) {
            IBond.Stereo patternStereo;
            
            // XXX could fail faster here : pI - l > l?
            if (patternIndex == (2 * pattern.length) - 1) {
                return false;
            } else if (patternIndex < pattern.length) {
                patternStereo = pattern[patternIndex];
            } else {
                patternStereo = pattern[patternIndex - pattern.length];
            }
            
            if (patternStereo == stereoList[matchIndex]) {
                patternIndex++;
                matchIndex++;
            } else {
                patternIndex++;
            }
        }
        
        // store the point where the match started
        matchPoint = patternIndex - pattern.length;
        
        return true;
    }
    
    public abstract IBond.Stereo[] getPattern();
    
    public abstract void execute(
            IAtom centralAtom, Map<Double, IBond> angleMap);

}
