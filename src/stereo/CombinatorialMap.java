package stereo;

import java.util.HashMap;
import java.util.Map;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

public class CombinatorialMap {
    
    private IAtomContainer atomContainer;
    
    /**
     * Mapping between the flags and the bonds.
     */
    private Map<Integer, IBond> flagBondMap;
    
    /**
     * Permutation of the darts (the half-bonds).
     */
    private int[] sigma;
    
    /**
     * Involution of the darts.
     */
    private int[] alpha;
    
    public CombinatorialMap(IAtomContainer atomContainer, int[] sigma) {
        this.atomContainer = atomContainer;
        this.sigma = sigma;
        int flagCount = atomContainer.getBondCount() * 2;
        flagBondMap = new HashMap<Integer, IBond>();
        sigma = new int[flagCount];
        alpha = new int[flagCount];
        int flag = 0;
        for (IBond bond : atomContainer.bonds()) {
            alpha[flag] = flag + 1;
            alpha[flag + 1] = flag;
            flagBondMap.put(flag, bond);
            flag++;
            flagBondMap.put(flag, bond);
            flag++;
        }
    }
    
    public IAtomContainer getAtomContainer() {
        return atomContainer;
    }
    
    public Map<Integer, IBond> getFlagBondMap() {
        return flagBondMap;
    }
    
    public int[] getFlagInvolution() {
        return alpha;
    }
    
    public int[] calculateDualPermutation() {
        int[] phi = new int[sigma.length];
        for (int index = 0; index < sigma.length; index++) {
            phi[alpha[sigma[index]]] = index;
        }
        return phi;
    }

}
