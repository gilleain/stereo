package stereo;

import org.openscience.cdk.geometry.BondTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Temporary class to hold some code that is maybe not al that useful...
 * 
 * See, BondTools converts counts of wedges around an atom into a number, with
 * '0' as 'not tetrahedral' and '1-6' meaning various combinations.
 * 
 * @author maclean
 *
 */
public class BondToolHelper {
    
    public enum BondToolResult {
        NOT_TETRA,
        ONE_UP_ONE_DOWN,
        TWO_UP_TWO_DOWN_TETRA,      // up and down are geometrically opposite
        
        //XXX no way to detect!
//        TWO_UP_TWO_DOWN_SQPL,       // up and down are same side = square planar
        
        ONE_UP,                     // non-strict
        ONE_DOWN,                   // non-strict
        ONE_UP_TWO_DOWN,            // non-strict
        ONE_DOWN_TWO_UP             // non-strict
    }
    
    /**
     * Uses BondTools.isTetrahedral method to compare the bonds around two atoms
     * and see if they are IDENTICAL.
     * 
     * XXX Note that this is probably not the method you wanted!
     * 
     * @param atom1
     * @param ac1
     * @param atom2
     * @param ac2
     * @return
     */
    public static boolean hasSameStereoWedges(
            IAtom atom1, IAtomContainer ac1, IAtom atom2, IAtomContainer ac2) {
        BondToolResult result1 = convertBondToolResult(
                BondTools.isTetrahedral(ac1, atom1, false));
        BondToolResult result2 = convertBondToolResult(
                BondTools.isTetrahedral(ac2, atom2, false));
        
        return result1 != BondToolResult.NOT_TETRA 
            && result2 != BondToolResult.NOT_TETRA
            && result1 == result2;
    }
    
    public static BondToolResult convertBondToolResult(int result) {
        switch(result) {
            case 1: return BondToolResult.ONE_UP_ONE_DOWN;
            case 2: return BondToolResult.TWO_UP_TWO_DOWN_TETRA;
            case 3: return BondToolResult.ONE_UP;
            case 4: return BondToolResult.ONE_DOWN;
            case 5: return BondToolResult.ONE_UP_TWO_DOWN;
            case 6: return BondToolResult.ONE_UP_TWO_DOWN;
            default: return BondToolResult.NOT_TETRA;
        }
    }
    

}
