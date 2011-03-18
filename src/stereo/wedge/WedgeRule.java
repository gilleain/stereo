package stereo.wedge;

import java.util.Map;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;

public abstract class WedgeRule {
    
    public boolean matches(IBond.Stereo[] stereoList) {
        IBond.Stereo[] pattern = getPattern();
        if (stereoList.length > pattern.length) return false;
        
        int patternIndex = 0;
        int matchIndex = 0;
        while (matchIndex < stereoList.length) {
            IBond.Stereo patternStereo;
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
        
        return true;
    }
    
    public abstract IBond.Stereo[] getPattern();
    
    public abstract void execute(
            IAtom centralAtom, Map<Double, IBond> angleMap);

}
