package stereo.wedge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point2d;

import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.stereo.TetrahedralChirality;

public class WedgeStereoComparisonTool {
    
    private class StereoEnv {
        
        private IBond.Stereo[] stereos;
        
        public StereoEnv(IBond.Stereo... stereos) {
            this.stereos = stereos;
        }
        
        public int hashCode() {
            int hashCode = 0;
            for (IBond.Stereo s : stereos) {
                hashCode += s.hashCode();
            }
            return hashCode;
        }
        
        private boolean compare(IBond.Stereo[] other) {
            for (int i = 0; i < stereos.length; i++) {
                if (stereos[i] != other[i]) return false;
            }
            return true;
        }
        
        public boolean equals(Object o) {
            if (o instanceof StereoEnv) {
                StereoEnv other = (StereoEnv)o;
                return compare(other.stereos);
            } else if (o instanceof IBond.Stereo[]) {
                return compare((IBond.Stereo[])o);
            } else {
                return false;
            }
        }
        
        public String toString() {
            return Arrays.toString(stereos);
        }
    }
    
    private StereoEnv UP_NO_UP = new StereoEnv(IBond.Stereo.UP, IBond.Stereo.NONE, IBond.Stereo.UP);
    private StereoEnv DO_NO_UP = new StereoEnv(IBond.Stereo.DOWN, IBond.Stereo.NONE, IBond.Stereo.UP);
    private StereoEnv UP_DO_NO = new StereoEnv(IBond.Stereo.UP, IBond.Stereo.DOWN, IBond.Stereo.NONE);
    private StereoEnv UP_NO_DO = new StereoEnv(IBond.Stereo.UP, IBond.Stereo.NONE, IBond.Stereo.DOWN);
    private StereoEnv DO_UP_NO = new StereoEnv(IBond.Stereo.UP, IBond.Stereo.NONE, IBond.Stereo.DOWN);
    
    private Map<StereoEnv, ITetrahedralChirality.Stereo> stereoMap
         = new HashMap<StereoEnv, ITetrahedralChirality.Stereo>();
    
    public WedgeStereoComparisonTool() {
        stereoMap.put(UP_NO_UP, ITetrahedralChirality.Stereo.ANTI_CLOCKWISE);
        stereoMap.put(DO_NO_UP, ITetrahedralChirality.Stereo.CLOCKWISE);
        stereoMap.put(UP_DO_NO, ITetrahedralChirality.Stereo.ANTI_CLOCKWISE);
        stereoMap.put(UP_NO_DO, ITetrahedralChirality.Stereo.CLOCKWISE);
        stereoMap.put(DO_UP_NO, ITetrahedralChirality.Stereo.CLOCKWISE);
        System.out.println(stereoMap);
    }
    
    private ITetrahedralChirality.Stereo getStereo(StereoEnv stereos) {
        if (stereoMap.containsKey(stereos)) {
            return stereoMap.get(stereos);
        } else {
            System.out.println(stereos + " not found!");
            return ITetrahedralChirality.Stereo.CLOCKWISE;
        }
    }
    
    public CIP_CHIRALITY getChirality2D(
            IAtom atom, IAtomContainer atomContainer) {
        List<IBond> neighbours = atomContainer.getConnectedBondsList(atom);
        if (neighbours.size() != 4) {
            return CIP_CHIRALITY.NONE;
        }
        
        IAtom reference = neighbours.get(0).getConnectedAtom(atom);
        Map<Double, IBond> atomNeighbourMap = new HashMap<Double, IBond>(); 
        for (int neighIndex = 1; neighIndex < neighbours.size(); neighIndex++) {
            IBond neighbourBond = neighbours.get(neighIndex);
            IAtom neighbourAtom = neighbourBond.getConnectedAtom(atom);
            double angle = giveAngle(atom, reference, neighbourAtom);
            atomNeighbourMap.put(angle, neighbourBond);
        }
        List<Double> angles = new ArrayList<Double>(atomNeighbourMap.keySet());
        Collections.sort(angles);
        Collections.reverse(angles);

        IAtom[] ligandAtoms = new IAtom[4];
        ligandAtoms[0] = reference;
        int i = 1;
        IBond.Stereo[] bondStereos = new IBond.Stereo[3];
        for (Double angle : angles) {
            IBond bond = atomNeighbourMap.get(angle);
            ligandAtoms[i] = bond.getConnectedAtom(atom);
            bondStereos[i - 1] = bond.getStereo();
            i++;
        }
        ITetrahedralChirality.Stereo stereo = getStereo(new StereoEnv(bondStereos));
        tmp_printLigandAtoms(ligandAtoms);
        tmp_printBonds(angles, atomNeighbourMap);
        System.out.println(stereo);
        ITetrahedralChirality stereoCenter = 
            new TetrahedralChirality(atom, ligandAtoms, stereo);
        return CIPTool.getCIPChirality(atomContainer, stereoCenter);
    }
    
    private static void tmp_printBonds(
            List<Double> sortedKeys, Map<Double, IBond> atomNeighbourMap) {
        System.out.print("|");
        for (Double key : sortedKeys) {
            System.out.print(atomNeighbourMap.get(key).getStereo() + "|");
        }
    }

    private static void tmp_printLigandAtoms(IAtom[] ligandAtoms) {
        System.out.print("|");
        for (IAtom atom : ligandAtoms) {
            System.out.print(atom.getSymbol() + "|");
        }
    }
    
    public static double giveAngle(IAtom atom, IAtom partner1, IAtom partner2) {
        Point2d atomP = atom.getPoint2d();
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
