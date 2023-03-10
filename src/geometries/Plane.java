package geometries;
import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry {
    final private Point q0;
    final private Vector normal;

    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    public Plane(Point p1, Point p2, Point p3) {
        q0 = p1;
        //find first vector
        Vector U = p2.subtract(p1);
        //find second vector
        Vector V = p3.subtract(p1);

        //find normal orthogonal to the two vectors
        Vector N = U.crossProduct(V);

        //return normalized vector
        normal = N.normalize();
    }

    public Vector getNormal() {
        return normal;
    }
    @Override
    public Vector getNormal(Point p) {
        return getNormal();
    }
}
