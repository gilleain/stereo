package stereo;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Given a plane, projects the 3D coordinates of an atomcontainer into 2D coords.
 * 
 * @author maclean
 *
 */
public class AtomContainerProjector {
    
    private static class Plane {
        
        public Point2d point;
        
        public Vector2d normal;
        
        public Plane(Point2d point, Vector2d normal) {
            this.point = point;
            this.normal = normal;
        }
        
    }
    
    public static void project(
            IAtomContainer atomContainer, Point2d point, Vector2d normal) {
        Plane plane = new Plane(point, normal);
    }

}
