package stereo;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Given a plane, projects the 3D coordinates of an atomcontainer into 2D coords.
 * 
 * @author maclean
 *
 */
public class AtomContainerProjector {
    
    public static void project(
            IAtomContainer atomContainer, Point3d planePoint, Vector3d planeNormal) {
        Plane plane = new Plane(planePoint, planeNormal);
        for (IAtom atom : atomContainer.atoms()) {
            Point3d point = atom.getPoint3d();
            if (point != null) {
                atom.setPoint2d(plane.project(point));
            }
        }
    }

}
