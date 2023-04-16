package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Sphere class extending abstract class RadialGeometry,
 * represents a sphere in 3D Cartesian coordinate system
 */
public class Sphere extends RadialGeometry {
    final private Point center;

    /**
     * constructor
     * @param radius radius of geometry
     * @param point  center of sphere
     */
    public Sphere(double radius, Point point) {
        super(radius);
        this.center = point;
    }

    public Point getCenter() {
        return center;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
