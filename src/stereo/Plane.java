package stereo;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Plane {
    
    public Point3d origin;
    
    public Vector3d normal;
    
    public double nDotN;
    
    public double planeD;
    
    public Plane(Point3d point, Vector3d normal) {
        this.origin = point;
        this.normal = normal;
        this.normal.normalize();
        nDotN = new Vector3d(normal).dot(normal);
        planeD = - normal.dot(new Vector3d(point));
    }

    public Point2d project(Point3d point) {
        Point3d pointInPlane = getPointInPlane(point);
        return null;
    }
    
    public Point2d project(Point3d point, Vector3d axisX, Vector3d axisY) {
        Point3d pointInPlane = getPointInPlane(point);
        return null;
    }
    
    public Point3d getPointInPlane(Point3d point) {
        double nDotP = new Vector3d(point).dot(normal);
        double lambda = -(nDotP + planeD) / nDotN;
        Point3d pointInPlane = new Point3d(point);
        Vector3d lambdaN = new Vector3d(normal);
        lambdaN.scale(lambda);
        pointInPlane.add(lambdaN);
        return pointInPlane;
    }
    
}
