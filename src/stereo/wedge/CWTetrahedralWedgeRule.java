package stereo.wedge;

import static org.openscience.cdk.interfaces.IBond.Stereo.UP;
import static org.openscience.cdk.interfaces.IBond.Stereo.DOWN;
import static org.openscience.cdk.interfaces.IBond.Stereo.NONE;

import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.interfaces.ITetrahedralChirality.Stereo;

public class CWTetrahedralWedgeRule extends AbstractTetrahedralWedgeRule {
    
    private IBond.Stereo[] pattern = { UP, DOWN, NONE, NONE };
    
    public IBond.Stereo[] getPattern() {
        return pattern;
    }

    @Override
    public ITetrahedralChirality.Stereo getStereo() {
        return Stereo.CLOCKWISE;
    }

}
