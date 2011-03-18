package stereo.wedge;

import java.util.Map;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;

import static org.openscience.cdk.interfaces.IBond.Stereo.UP;
import static org.openscience.cdk.interfaces.IBond.Stereo.DOWN;
import static org.openscience.cdk.interfaces.IBond.Stereo.NONE;;

public class TetrahedralWedgeRule extends WedgeRule {
    
    private IBond.Stereo[] pattern = { UP, DOWN, NONE, NONE };
    
    public IBond.Stereo[] getPattern() {
        return pattern;
    }

    @Override
    public void execute(IAtom centralAtom, Map<Double, IBond> angleMap) {
        // TODO Auto-generated method stub
        
    }

}
