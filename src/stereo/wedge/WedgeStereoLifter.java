package stereo.wedge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point2d;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/**
 * 'Lifts' the 2D wedge stereo representation into a 3D one.
 * 
 * @author maclean
 *
 */
public class WedgeStereoLifter {
    
    private Map<String, WedgeRule> ruleMap;
    
    public WedgeStereoLifter() {
        ruleMap = new HashMap<String, WedgeRule>();
        addRule("UDNN", new TetrahedralWedgeRule());
    }
    
    private void addRule(String pattern, WedgeRule rule) {
        // patterns are doubled to match circular permutations
        ruleMap.put(pattern + pattern, rule);
    }
    
    private WedgeRule getRule(String pattern) {
        for (String key : ruleMap.keySet()) {
            if (key.contains(pattern)) return ruleMap.get(key);
        }
        return null;
    }
    
    public void lift(IAtom atom, IAtomContainer atomContainer) {
        List<IBond> bonds = atomContainer.getConnectedBondsList(atom);
        
        // it doesn't matter which atom is picked as the reference
        // as the wedge pattern matching is circular
        IBond referenceBond = bonds.get(0);
        IAtom reference = referenceBond.getConnectedAtom(atom);
        
        // calculate the full angle between the reference bond and the others
        Map<Double, IBond> angleMap = new HashMap<Double, IBond>();
        angleMap.put(0.0, referenceBond);
        for (int index = 1; index < bonds.size(); index++) {
            IBond bond = bonds.get(index);
            IAtom bondAtom = bond.getConnectedAtom(atom);
            angleMap.put(getFullAngle(atom, reference, bondAtom), bond);
        }
        
        // now, sort the bonds by these angles and get the IBond.Stereo string
        List<Double> angles = new ArrayList<Double>(angleMap.keySet());
        Collections.sort(angles);
        Collections.reverse(angles);
        String stereoString = "";
        for (Double angle : angles) {
            IBond bond = angleMap.get(angle);
            IBond.Stereo stereo = bond.getStereo();
            switch (stereo) {
                case UP: stereoString += "U"; break;
                case DOWN: stereoString += "D"; break;
                case NONE: 
                default: stereoString += "N";
            }
        }
        
        WedgeRule rule = getRule(stereoString);
        rule.execute(atom, angleMap);
    }
    
    /**
     * Gets the 'full' angle (between 0&deg; and 360&deg;) between
     *   
     * @param atom
     * @param partner1
     * @param partner2
     * @return
     */
    private double getFullAngle(IAtom centralAtom, IAtom partner1, IAtom partner2) {
        Point2d atomP = centralAtom.getPoint2d();
        Point2d partner1P = partner1.getPoint2d();
        Point2d partner2P = partner2.getPoint2d();
        
        double angle1 = Math.atan2((partner1P.y - atomP.y), (partner1P.x - atomP.x));
        double angle2 = Math.atan2((partner2P.y - atomP.y), (partner2P.x - atomP.x));
        double angle = angle2 - angle1;
        if (angle2 < 0 && angle1 > 0 && angle2 < -(Math.PI / 2)) {
          angle = Math.PI + angle2 + Math.PI - angle1;
        }
        if (angle2 > 0 && angle1 < 0 && angle1 < -(Math.PI / 2)) {
          angle = -Math.PI + angle2 - Math.PI - angle1;
        }
        if (angle < 0) {
          return (2 * Math.PI + angle);
        } else {
          return (angle);
        }
    }

}
