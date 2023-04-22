package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

/**
 * Triangle class extending Polygon class,
 * represents two-dimensional triangle in 3D Cartesian coordinate system
 */
public class Triangle extends Polygon {
    /**
     * constructor that received 3 points
     *
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersection = plane.findIntersections(ray);
        if (intersection == null)
            return null; //ray doesn't intersect with plane

        Vector v1 = vertices.get(0).subtract(ray.getP0());
        Vector v2 = vertices.get(1).subtract(ray.getP0());
        Vector n1 = v1.crossProduct(v2).normalize();
        double sign1 = alignZero(ray.getDir().dotProduct(n1));
        if (sign1 == 0) return null;

        Vector v3 = vertices.get(2).subtract(ray.getP0());
        Vector n2 = v2.crossProduct(v3).normalize();
        double sign2 = alignZero(ray.getDir().dotProduct(n2));
        if (sign1 * sign2 <= 0) return null;

        Vector n3 = (v3.crossProduct(v1)).normalize();
        double sign3 = alignZero(ray.getDir().dotProduct(n3));
        if (sign1 * sign3 <= 0) return null;

        return intersection;

    }

    @Override
    public String toString() {
        return "Triangle{" +
                "vertices=" + vertices +
                ", plane=" + plane +
                '}';
    }
}
