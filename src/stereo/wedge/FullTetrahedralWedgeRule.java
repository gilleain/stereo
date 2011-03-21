package stereo.wedge;

import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.interfaces.ITetrahedralChirality.Stereo;
import static org.openscience.cdk.interfaces.IBond.Stereo.UP;
import static org.openscience.cdk.interfaces.IBond.Stereo.DOWN;

public class FullTetrahedralWedgeRule extends AbstractTetrahedralWedgeRule {
    
    private IBond.Stereo[] pattern = new IBond.Stereo[] { UP, DOWN, UP, DOWN };
    
    @Override
    public IBond.Stereo[] getPattern() {
        return pattern;
    } 

    @Override
    public Stereo getStereo() {
        return ITetrahedralChirality.Stereo.CLOCKWISE;
    }

}
