package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

/**
 * Plane class represents a plane,
 * based on a point and normal vector
 * @author Sarah Daatyah Furmanski and Efrat Kartman
 */
public class Plane extends Geometry {
    final private Point q0;
    final private Vector normal;

    /**
     * constructor
     * normalize the vector received
     *
     * @param q0     point
     * @param normal direction vector
     */
    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    /**
     * constructor that received 3 points
     *
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
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Vector QMinusP0;
        try {
            QMinusP0 = q0.subtract(ray.getP0());
        } catch (IllegalArgumentException ex) {
            return null; // ray begins at plane's reference point
        }
        double nv = normal.dotProduct(ray.getDir());
        if (isZero(nv))
            return null; // ray is parallel to plane

        double nQMinusP0 = normal.dotProduct(QMinusP0);
        double t = alignZero(nQMinusP0 / nv);
        if (t > 0)
            return List.of(new GeoPoint(this, ray.getP0().add(ray.getDir().scale(t))));

        return null; // no intersection with plane
    }

    @Override
    public int[][] calcBoundary() {//there is no boundary to infinite geometric entity
        return null;
    }
}
