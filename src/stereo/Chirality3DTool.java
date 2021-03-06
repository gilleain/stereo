package stereo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.interfaces.ITetrahedralChirality.Stereo;
import org.openscience.cdk.stereo.StereoTool;
import org.openscience.cdk.stereo.TetrahedralChirality;

/**
 * Takes a molecule with 3D coordinates, and uses the {@link StereoTool} and
 * the {@link CIPTool} to determine chirality of the stereo centers.
 * 
 * @author maclean
 *
 */
public class Chirality3DTool {
    /**
     * Get R/S chirality assignments for an atomcontainer that should have
     * 3D coordinates as Point3d set in the atoms. Note that assignments of
     * CIP_CHIRALITY.NONE are not returned by default.
     * 
     * @param atomContainer the atom container to perform assignments on
     * @return a map of chiral atoms to assignments
     */
    public static Map<IAtom, CIP_CHIRALITY> getTetrahedralChiralities(
        IAtomContainer atomContainer) {
        return getTetrahedralChiralities(atomContainer, false);
    }

    /**
     * Get R/S chirality assignments for an atomcontainer that should have
     * 3D coordinates as Point3d set in the atoms. If getNoneAssignments is 
     * set to true, atoms with 4 neighbours that are not chiral will be mapped
     * to CIP_CHIRALITY.NONE.
     * 
     * @param atomContainer the atom container to perform assignments on
     * @param getNoneAssigments if true, map non-chiral tetrahedral centers to NONE
     * @return a map of chiral atoms to assignments
     */
    public static Map<IAtom, CIP_CHIRALITY> getTetrahedralChiralities(
                IAtomContainer atomContainer, boolean getNoneAssigments) {
        Map<IAtom, CIP_CHIRALITY> chiralMap = new HashMap<IAtom, CIP_CHIRALITY>();
        for (IAtom atom : atomContainer.atoms()) {
            List<IAtom> neighbours = atomContainer.getConnectedAtomsList(atom);
            if (neighbours.size() == 4) {
                IAtom n1 = neighbours.get(0);
                IAtom n2 = neighbours.get(1);
                IAtom n3 = neighbours.get(2);
                IAtom n4 = neighbours.get(3);
                Stereo stereo = StereoTool.getStereo(n1, n2, n3, n4); 
                IAtom[] ligands = new IAtom[] { n1, n2, n3, n4 };
                ITetrahedralChirality stereoCenter = 
                    new TetrahedralChirality(atom, ligands, stereo);
                CIP_CHIRALITY chirality = CIPTool.getCIPChirality(atomContainer, stereoCenter);
                if (chirality != CIP_CHIRALITY.NONE) {
                    chiralMap.put(atom, chirality);
                }
            }
        }
        return chiralMap;
    }
}
