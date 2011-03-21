package stereo.wedge;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.vecmath.Point2d;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IStereoElement;

/**
 * 'Lifts' the 2D wedge stereo representation into a 3D one.
 * 
 * @author maclean
 *
 */
public class WedgeStereoLifter {
    
    private List<WedgeRule> rules;
    
    public WedgeStereoLifter() {
        rules = new ArrayList<WedgeRule>();
        rules.add(new CWTetrahedralWedgeRule());
        rules.add(new ACWTetrahedralWedgeRule());
        rules.add(new SingleUpWedgeRule());
        rules.add(new SingleDownWedgeRule());
        rules.add(new FullTetrahedralWedgeRule());
    }
    
    private WedgeRule getRule(IBond.Stereo[] stereos) {
        for (WedgeRule rule : rules) {
            if (rule.matches(stereos)) return rule;
        }
        return null;
    }
    
    public IStereoElement lift(IAtom atom, IAtomContainer atomContainer) {
        List<IBond> bonds = atomContainer.getConnectedBondsList(atom);
        if (bonds.size() < 3) return null;
        
        // it doesn't matter which atom is picked as the reference
        // as the wedge pattern matching is circular
        IBond referenceBond = bonds.get(0);
        IAtom reference = referenceBond.getConnectedAtom(atom);
        
        // calculate the full angle between the reference bond and the others
        SortedMap<Double, IBond> angleMap = new TreeMap<Double, IBond>();
        angleMap.put(0.0, referenceBond);
        for (int index = 1; index < bonds.size(); index++) {
            IBond bond = bonds.get(index);
            IAtom bondAtom = bond.getConnectedAtom(atom);
            double angle = getFullAngle(atom, reference, bondAtom); 
            angleMap.put((2 * Math.PI) - angle, bond);
        }
        
        // now, sort the bonds by these angles and get the IBond.Stereo array
        IBond.Stereo[] stereos = new IBond.Stereo[bonds.size()];
        int i = 0;
        for (Double angle : angleMap.keySet()) {
            IBond bond = angleMap.get(angle);
            stereos[i] = bond.getStereo();
            i++;
        }
        
        WedgeRule rule = getRule(stereos);
        if (rule != null) {
            return rule.execute(atom, atomContainer, angleMap);
        } else {
            return null;
        }
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
