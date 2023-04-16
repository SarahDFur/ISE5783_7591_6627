package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Plane class represents a plane,
 * based on a point and normal vector
 */
public class Plane implements Geometry {
    final private Point q0;
    final private Vector normal;

    /**
     * constructor
     * normalize the vector received
     * @param q0 point
     * @param normal direction vector
     */
    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    /**
     * constructor that received 3 points
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
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
        return normal;
    }

    public Point getQ0() {
        return q0;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
